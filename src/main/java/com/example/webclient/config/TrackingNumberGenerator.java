package com.example.webclient.config;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TrackingNumberGenerator {

    public static String trackingNumberFunc(String resi) {
        String k = "MGViZmZmZTYzZDJhNDgxY2Y1N2ZlN2Q1ZWJkYzlmZDY=";
        long r = System.currentTimeMillis() / 1000L;
        String rs = String.valueOf(r);
        String data = resi + rs + k;

        String hash;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(data.getBytes());

            // Convert byte array to hexadecimal representation
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            hash = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error creating SHA-256 hash", e);
        }

        return resi + "|" + rs + hash;
    }

    public static void main(String[] args) {
        String resi = "SPXVN048106749441";
        String trackingNumber = trackingNumberFunc(resi);
        System.out.println("Tracking Number: " + trackingNumber);
    }
}
