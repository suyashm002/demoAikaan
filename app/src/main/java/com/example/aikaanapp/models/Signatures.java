package com.example.aikaanapp.models;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.LinkedList;
import java.util.List;
import android.content.pm.PackageInfo;
import android.content.pm.Signature;

import com.example.aikaanapp.models.data.AppSignature;
import com.example.aikaanapp.util.LogUtils;
import com.example.aikaanapp.util.StringHelper;

/**
 * Signatures.
 */
public class Signatures {

    private static final String TAG = "Signatures";

    public static List<AppSignature> getSignatureList(PackageInfo pak) {
        List<AppSignature> signatureList = new LinkedList<>();
        String[] pmInfos = pak.requestedPermissions;

        if (pmInfos != null) {
            byte[] bytes = Permissions.getPermissionBytes(pmInfos);
            String hexB = StringHelper.convertToHex(bytes);
            signatureList.add(new AppSignature(hexB));
        }
        Signature[] sigs = pak.signatures;

        for (Signature s : sigs) {
            MessageDigest md;
            try {
                md = MessageDigest.getInstance("SHA-1");
                md.update(s.toByteArray());
                byte[] dig = md.digest();
                // Add SHA-1
                signatureList.add(new AppSignature(StringHelper.convertToHex(dig)));

                CertificateFactory fac = CertificateFactory.getInstance("X.509");
                if (fac == null)
                    continue;
                X509Certificate cert = (X509Certificate) 
                        fac.generateCertificate(new ByteArrayInputStream(s.toByteArray()));
                if (cert == null)
                    continue;
                PublicKey pkPublic = cert.getPublicKey();
                if (pkPublic == null)
                    continue;
                String al = pkPublic.getAlgorithm();
                switch (al) {
                    case "RSA": {
                        md = MessageDigest.getInstance("SHA-256");
                        RSAPublicKey rsa = (RSAPublicKey) pkPublic;
                        byte[] data = rsa.getModulus().toByteArray();
                        if (data[0] == 0) {
                            byte[] copy = new byte[data.length - 1];
                            System.arraycopy(data, 1, copy, 0, data.length - 1);
                            md.update(copy);
                        } else
                            md.update(data);
                        dig = md.digest();
                        // Add SHA-256 of modulus
                        signatureList.add(new AppSignature(StringHelper.convertToHex(dig)));
                        break;
                    }
                    case "DSA": {
                        DSAPublicKey dsa = (DSAPublicKey) pkPublic;
                        md = MessageDigest.getInstance("SHA-256");
                        byte[] data = dsa.getY().toByteArray();
                        if (data[0] == 0) {
                            byte[] copy = new byte[data.length - 1];
                            System.arraycopy(data, 1, copy, 0, data.length - 1);
                            md.update(copy);
                        } else
                            md.update(data);
                        dig = md.digest();
                        // Add SHA-256 of public key (DSA)
                        signatureList.add(new AppSignature(StringHelper.convertToHex(dig)));
                        break;
                    }
                    default:
                        LogUtils.logE(TAG, "Weird algorithm: " + al + " for " + pak.packageName);
                        break;
                }
            } catch (NoSuchAlgorithmException | CertificateException e) {
                e.printStackTrace();
            }

        }
        return signatureList;
    }
}