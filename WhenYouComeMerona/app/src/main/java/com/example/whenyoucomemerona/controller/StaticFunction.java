package com.example.whenyoucomemerona.controller;

import android.content.pm.PackageInfo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StaticFunction {
    // 암호화
    public static String EncBySha256(String data) {
        String retVal = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(data.getBytes());

            byte byteData[] = md.digest();
            StringBuffer sb = new StringBuffer();

            for (byte byteDatum : byteData) {
                sb.append(Integer.toString((byteDatum & 0xff) + 0x100, 16).substring(1));
            }

            StringBuffer hexString = new StringBuffer();
            for (byte byteDatum : byteData) {
                String hex = Integer.toHexString(0xff & byteDatum);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            retVal = hexString.toString();
        } catch(NoSuchAlgorithmException e){
            System.out.println("EncBySHA256 Error:" + e.toString());
        }
        return retVal;
    }

}
