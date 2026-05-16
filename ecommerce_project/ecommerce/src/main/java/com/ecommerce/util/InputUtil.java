package com.ecommerce.util;

import java.util.Scanner;

/**
 * Bao bọc Scanner dùng chung — tránh tạo nhiều Scanner trên System.in.
 */
public class InputUtil {

    private static final Scanner sc = new Scanner(System.in);

    private InputUtil() {}

    public static String readLine(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("  ⚠ Vui lòng nhập số nguyên.");
            }
        }
    }

    public static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                System.out.println("  ⚠ Vui lòng nhập số hợp lệ.");
            }
        }
    }

    /** Đọc mật khẩu (không ẩn ký tự trong console Java thường) */
    public static String readPassword(String prompt) {
        return readLine(prompt);
    }

    public static void pressEnter() {
        System.out.print("\n[Nhấn Enter để tiếp tục...]");
        sc.nextLine();
    }
}
