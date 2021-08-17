package com.cmhi.log.helper.encryptUtil;

import java.io.IOException;

public class Base64Utils {

    private static char[] base64EncodeChars = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    private static byte[] base64DecodeChars = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1};

    public static String encode(byte[] paramArrayOfByte) {
        StringBuffer localStringBuffer = new StringBuffer();
        int i = paramArrayOfByte.length;
        int j = 0;
        while (j < i) {
            int k = paramArrayOfByte[(j++)] & 0xFF;
            if (j == i) {
                localStringBuffer.append(base64EncodeChars[(k >>> 2)]);
                localStringBuffer.append(base64EncodeChars[((k & 0x3) << 4)]);
                localStringBuffer.append("==");
                break;
            }
            int m = paramArrayOfByte[(j++)] & 0xFF;
            if (j == i) {
                localStringBuffer.append(base64EncodeChars[(k >>> 2)]);
                localStringBuffer.append(base64EncodeChars[((k & 0x3) << 4 | (m & 0xF0) >>> 4)]);
                localStringBuffer.append(base64EncodeChars[((m & 0xF) << 2)]);
                localStringBuffer.append("=");
                break;
            }
            int n = paramArrayOfByte[(j++)] & 0xFF;
            localStringBuffer.append(base64EncodeChars[(k >>> 2)]);
            localStringBuffer.append(base64EncodeChars[((k & 0x3) << 4 | (m & 0xF0) >>> 4)]);
            localStringBuffer.append(base64EncodeChars[((m & 0xF) << 2 | (n & 0xC0) >>> 6)]);
            localStringBuffer.append(base64EncodeChars[(n & 0x3F)]);
        }
        return localStringBuffer.toString();
    }

    public static byte[] decode(byte[] bytes) throws IOException {
        byte[] res = new byte[bytes.length * 3 / 4];
        for (int i = 0; i < bytes.length; i += 4) {
            byte a = unmap64(bytes[i]);
            byte b = unmap64(bytes[i + 1]);
            byte c = unmap64(bytes[i + 2]);
            byte d = unmap64(bytes[i + 3]);
            res[i * 3 / 4 + 0] = (byte) (a << 2 | b >> 4);
            res[i * 3 / 4 + 1] = (byte) (b << 4 | c >> 2);
            res[i * 3 / 4 + 2] = (byte) (c << 6 | d);
        }
        int l = bytes.length;
        int pad = bytes[l - 2] == '=' ? 2 : bytes[l - 1] == '=' ? 1 : 0;
        if (pad > 0) {
            byte[] ret = new byte[res.length - pad];
            System.arraycopy(res, 0, ret, 0, res.length - pad);
            return ret;
        }
        return res;
    }

    static public byte unmap64(byte i) {
        // +:43 /:47 0:48 =:61 A:65 a:97
        if (i == '=') return 0;
        byte index = (byte) (i == '+' ? 62 : i == '/' ? 63 : i < 'A' ? i - '0' + 52 : i < 'a' ? i - 'A' : i - 'a' + 26);
        return (byte) index;
    }
}
