package com.uestc.group14.backend.common.utils;

import org.springframework.util.DigestUtils;

/**
 * MD5加密工具类
 */
public class Md5Util {

    private static final String SALT = "vtp_salt_2026";

    public static String encrypt(String password) {
        return DigestUtils.md5DigestAsHex((SALT + password).getBytes());
    }

    public static boolean verify(String password, String encrypted) {
        return encrypt(password).equals(encrypted);
    }
}