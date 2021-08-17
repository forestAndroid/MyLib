package com.cmhi.log.helper.encryptUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Title: SHA1<br/>
 * Description: sha1������<br/>
 * Company: cloudfn<br/>
 *
 * @author lai_jj
 * @date 2015��1��27������5:01:47
 */
public class SHA1 {
    /**
     * Title: SHA_1<br/>
     * Description: ��decript����SHA-1����<br/>
     *
     * @param decript
     * @return
     * @author lai_jj
     * @date 2015��1��27������5:00:43
     */
    public static String SHA_1(String decript) {
        try {
            MessageDigest digest = MessageDigest
                    .getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // �ֽ�����ת��Ϊ ʮ������ ��
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * ����byte����
     *
     * @param data
     * @return
     */
    public static byte[] SHA_2(byte[] data) {
        byte[] bytes = (byte[]) null;
        MessageDigest sha;
        try {
            sha = MessageDigest.getInstance("sha-1");
            sha.update(data);
            bytes = sha.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static void main(String[] args) {
        String str = SHA_1("123456");
        System.out.println("SHA-1���ܽ��:" + str);
    }
}
