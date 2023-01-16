package zzq.zzqsimpleframeworkcommon.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES加密解密
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2022-04-24 12:01
 */
public class AESUtil {
    public static String[] GetFixedKeys() {
        return new String[]{"ll7uz0DREVGBA9IJxmnwoEsJoQtgpGPqXQOzmYgaS6o=", "yuntM97GbF5ISjSsx0qKqA=="};
    }

    public static String[] GetKeys() {
        try {
            var keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256);
            var secretKey = keyGen.generateKey();
            var byteKey = secretKey.getEncoded();
            var key = Base64.getEncoder().encodeToString(byteKey);
            var iv = Base64.getEncoder().encodeToString(SecureRandom.getInstanceStrong().generateSeed(16));
            return new String[]{key, iv};
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 原文String-->使用utf8编码成byte[]-->加密成byte[]-->使用Base64转码成String-->密文String
     *
     * @param input 原文
     * @param key
     * @param iv
     * @return
     * @
     */
    public static String AESEncrypt(String input, String key, String iv) {
        try {
            var inputBytes = input.getBytes(StandardCharsets.UTF_8);

            var cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, getKeySpec(key), getIvSpec(iv));
            var cipherByte = cipher.doFinal(inputBytes);

            var result = Base64.getEncoder().encodeToString(cipherByte);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 密文String-->使用Base64转码成byte[]-->解密成byte[]-->使用utf8解码成String-->原文String
     *
     * @param input 密文
     * @param key
     * @param iv
     * @return
     * @
     */
    public static String AESDecrypt(String input, String key, String iv) {
        try {
            var inputBytes = Base64.getDecoder().decode(input);

            var cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, getKeySpec(key), getIvSpec(iv));
            var cipherByte = cipher.doFinal(inputBytes);

            var result = new String(cipherByte, StandardCharsets.UTF_8);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static SecretKeySpec getKeySpec(String key) {
        var bytesKey = Base64.getDecoder().decode(key);
        var keySpec = new SecretKeySpec(bytesKey, "AES");
        return keySpec;
    }

    private static IvParameterSpec getIvSpec(String iv) {
        var bytesIV = Base64.getDecoder().decode(iv);
        var ivSpec = new IvParameterSpec(bytesIV);
        return ivSpec;
    }

    public static void main(String[] args) {
        String[] keys = GetKeys();
        System.out.println("key:" + keys[0] + ",iv:" + keys[1]);
        String aesEncrypt = AESEncrypt("ZZQ", keys[0], keys[1]);
        String aesDecrypt = AESDecrypt(aesEncrypt, keys[0], keys[1]);
        System.out.println(aesEncrypt);
        System.out.println(aesDecrypt);
    }


}

