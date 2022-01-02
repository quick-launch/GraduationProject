package org.yxr_qrx.graduationproject.utils;

import org.apache.tomcat.util.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName:PasswordEncryptionUtils
 * @Author:41713
 * @Date 2021/11/1  15:00
 * @Version 1.0
 **/
public class PasswordEncryptionUtils {
    public static String planTextToMD5Encrypt(String password){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] output = md.digest(password.getBytes());
            return Base64.encodeBase64String(output);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
