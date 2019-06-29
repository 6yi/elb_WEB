package com.lzheng.elbweb.entities;

/**
 * @ClassName MD5
 * @Author lzheng
 * @Date 2019/6/27 21:49
 * @Version 1.0
 * @Description:
 */
import java.security.MessageDigest;

public class MD5 {
    private static char[] md5Chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static String bufferToHex(byte[] bytes) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte[] bytes, int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(n * 2);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = md5Chars[(bt & 240) >> 4];
        char c1 = md5Chars[bt & ar.m];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    public static String getStringMD5String(String str) throws Exception {
        MessageDigest messagedigest = MessageDigest.getInstance("MD5");
        messagedigest.update(str.getBytes());
        return bufferToHex(messagedigest.digest());
    }

}