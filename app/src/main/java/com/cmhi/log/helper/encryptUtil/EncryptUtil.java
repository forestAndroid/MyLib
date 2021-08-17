package com.cmhi.log.helper.encryptUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * �ӽ��ܹ�����
 *
 * @author HW
 */
public class EncryptUtil {
    private static Properties prop = null;

    static {
        prop = new Properties();
        try {
            String path = EncryptUtil.class.getClassLoader().
                    getResource("").toURI().getPath();
            prop.load(new FileInputStream(path + "keys.properties"));
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) {
        try {
            String oriMessage = "{\"tvInfo\": {\"DPI\":\"240\",\"RAM\":\"1.38 GB\",\"ROM\":\"3.87 GB\",\"androidVersion\":\"5.0.1\",\"brand\":\"TCL\",\"chipModel\":\"Muji\",\"cpuCoreNumber\":\"4\",\"cpuModel\":\"Muji\",\"frequency\":\"1.44\",\"mac\":\"3C591E2CFA38\",\"model\":\"TCL Android TV\",\"networkOperator\":\"CMCC\",\"resolution\":\"1920*1080\",\"storage\":\"3.87 GB\"}}";
            String sign = EncryptUtil.genMsgSignStr(oriMessage);
            //2.拼接数据
            //String requestDataStr = oriMessage + sign;
            //3.获取密文
            //String  encryptStr = enCode(requestDataStr, "79BA750C8B1CD49E");
            //System.out.println(encryptStr);

            //encryptStr="rPaClNfU6p3uNoowtIZO+kMcKxicm6920NjCELwU9LjJG0nmYHhADRIaVzBdnQRK1oAubTPUQo7pm5BKClwFt2ganmRAlpq088ou2Y+uwNre3MImCZlqpHgflwDZx2uFVvv2wxEFKr6s+bCPH6qWkJO4LCwNhmKvQkqqKnovyNCbfvCUAiZGgIlByf7a4J9exMBwAm54CY+iGDWaC1wPkx1eE/EeOYjDf1P04JN5MXBsHXuLfSJkBToXo3Fy5QoevYy/4V9bdcX100Z4yRZAg53Y/FBb5AdyW57RYZimJSYzByuIw/bvuQkQqNLe4GThsrafSHmUcwBzWzpmKikjb9MKYU36O6bQ8anQ7Kel2i1wtvz5YiFKuM/3gVVNmVtksm8gAadsfL6smnb5DCSSbW+QLzx/UplB6r0cr0HZIW84AIQb4IA47DHsIfuvWkOd2ZleApj/uicZHrzCYU6tYUxH6oYsuhe6AJAjx7tvP5I0oC9aJbG52uSA0tkxqDPHiY77mTS8P+9afDxIjACV3AeCP7FFJFzTc6U/WRaPLWnw9wJI";
            System.out.println(sign);
            //9fea8a621de1f684a3ba969d455767773679d9c4 9fea8a621de1f684a3ba969d455767773679d9c4
            //System.out.println(EncryptUtil.deCode(encryptStr,"79BA750C8B1CD49E"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String enCode(String str, String key) throws UnsupportedEncodingException {
        return XXTEA.encryptWithBase64(str.getBytes("UTF-8"), key.getBytes("UTF-8"));
    }

    public static String deCode(String str, String key) throws IOException {
        byte[] result = XXTEA.decryptWithBase64(str.getBytes("UTF-8"), key.getBytes("UTF-8"));
        if (result == null) {
            return null;
        }
        return new String(result);
    }

    /**
     * �Ͱ����SIG_VALֵ���㷽��
     *
     * @param bytes
     * @return
     */
    public static String byteArrayToHex(byte[] bytes) {
        String retorno = "";
        if (bytes == null || bytes.length == 0) {
            return retorno;
        }
        for (int i = 0; i < bytes.length; i++) {
            byte valor = bytes[i];
            int d1 = valor & 0xF;
            d1 += (d1 < 10) ? 48 : 55;
            int d2 = (valor & 0xF0) >> 4;
            d2 += (d2 < 10) ? 48 : 55;
            retorno = retorno + (char) d2 + (char) d1;
        }
        return retorno;
    }

    public static String complieData(Object inputMsgStr) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        try {
            if (!(inputMsgStr instanceof Map)) {
                paramMap = JsonUtils.getJosn(inputMsgStr.toString());
            } else {
                paramMap = (HashMap) inputMsgStr;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            return String.valueOf(inputMsgStr);
        }
        String[] keyArray = paramMap.keySet().toArray(new String[0]);
        StringBuilder stringBuilder = new StringBuilder();
        Arrays.sort(keyArray);
        for (String key : keyArray) {
            String value = complieData(paramMap.get(key));
            stringBuilder.append(key).append(value);
        }
        return stringBuilder.toString();
    }

    public static String genMsgSignStr(String inputMsgStr) {
        String codes = complieData(inputMsgStr);
        String messageSignStr = SHA1.SHA_1(codes);
        return messageSignStr;
    }

    public static String encryptStr(String oriMessage, String clientId) {
        //加解密密钥，软终端为对接系统分配的
        //1.计算签名
        String sign = EncryptUtil.genMsgSignStr(oriMessage);
        //2.拼接数据
        String requestDataStr = oriMessage + sign;
        //3.获取密文
        String encryptStr = null;
        try {
            String key = getKey(clientId);
            if (key != null) {
                encryptStr = enCode(requestDataStr, key);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encryptStr;
    }

    /**
     * 解密
     *
     * @param encryptData
     * @param clientId
     * @return
     */
    public static String decryptionStr(String encryptData, String clientId) {
        //1.解密数据
        String decodedData = null;
        try {
            String key = getKey(clientId);
            if (key != null) {
                decodedData = deCode(encryptData, key);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //2.获取加密数据中的签名值
        String sign1 = decodedData.substring(decodedData.length() - 40, decodedData.length());
        //3.计算本地签名
        String sign2 = EncryptUtil.genMsgSignStr(decodedData.substring(0, decodedData.length() - 40));
        //4.比较签名
        if (sign1.equals(sign2)) {
            //5.得到原始数据
            String oriData = decodedData.substring(0, decodedData.length() - 40);
            return oriData;
        } else {
            return null;
        }
    }

    private static String getKey(String clientId) {
        switch (clientId) {
            case "320000":
                return "47049217402dfc7d718d9b6e7fee7d0d";
            case "110000":
                return "79BA750C8B1CD49E";
            case "120000":
                return "FAJGI8HFOR5F6ONW";
            case "130000":
                return "JT4RHJRTHHBPFD7Y";
            default:
                return null;
        }
    }
}
