package com.ecommerce.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Tiện ích băm mật khẩu bằng SHA-256.
 * Dùng chung cho UC1 (đăng nhập) và UC2 (đăng ký).
 */
public class PasswordUtil {

    private PasswordUtil() {}

    /**
     * Băm chuỗi plaintext bằng SHA-256, trả về hex string 64 ký tự.
     */
    public static String hash(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(plainText.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 không khả dụng", e);
        }
    }

    /**
     * Kiểm tra plainText có khớp với hash lưu trong DB không.
     */
    public static boolean verify(String plainText, String storedHash) {
        return hash(plainText).equals(storedHash);
    }
}
