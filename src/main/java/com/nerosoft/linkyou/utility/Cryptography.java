package com.nerosoft.linkyou.utility;

import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * 密码学工具类，提供各种加密解密算法的实现。
 * 
 * 该类集成了 BouncyCastle 加密库，提供了 DES 和 AES 两种常用的对称加密算法实现。
 * 
 * @author nerosoft
 * @version 1.0
 */
public class Cryptography {
    static {
        // 注册 BouncyCastle 提供者
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * DES 加密解密工具类
     * 
     * 提供基于 DES 算法的加密和解密方法，使用 CBC 模式和 PKCS5 填充。
     * 
     * 注意：DES 算法由于密钥长度较短（56位），在现代应用中已不推荐使用，
     * 建议使用 AES 等更安全的加密算法。
     */
    public static class DES {
        private static final String TRANSFORMATION = "DES/CBC/PKCS5Padding";

        /**
         * DES 加密方法
         * 
         * 使用指定的盐值对数据进行 DES 加密
         * 
         * @param data 待加密的数据
         * @param salt 加密使用的盐值，必须为8字节
         * @return 加密后的 Base64 编码字符串
         * @throws Exception 加密过程中可能抛出的异常
         */
        public static String encrypt(String data, String salt) throws Exception {
            DESKeySpec desKeySpec = new DESKeySpec(salt.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            IvParameterSpec iv = new IvParameterSpec(salt.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

            byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedData);
        }

        /**
         * DES 解密方法
         * 
         * 使用指定的盐值对数据进行 DES 解密
         * 
         * @param data 待解密的 Base64 编码字符串
         * @param salt 解密使用的盐值，必须与加密时使用的盐值相同
         * @return 解密后的原始数据
         * @throws Exception 解密过程中可能抛出的异常
         */
        public static String decrypt(String data, String salt) throws Exception {
            DESKeySpec desKeySpec = new DESKeySpec(salt.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            IvParameterSpec iv = new IvParameterSpec(salt.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

            byte[] decodedData = Base64.getDecoder().decode(data);
            byte[] decryptedData = cipher.doFinal(decodedData);
            return new String(decryptedData, StandardCharsets.UTF_8);
        }

    }

    /**
     * AES 加密解密工具类
     * 
     * 提供基于 AES 算法的加密和解密方法，使用 ECB 模式和 PKCS5 填充。
     * 
     * 注意：ECB 模式不推荐用于加密敏感数据，因为它不提供数据的完整性保护，
     * 建议使用 CBC 或 GCM 模式。
     */
    public static class AES {
        /**
         * AES CBC PKCS7 加密
         * 
         * @param data 待加密的数据
         * @param salt 加密使用的盐值
         * @return 加密后的 Base64 编码字符串
         * @throws Exception 加密过程中可能抛出的异常
         */
        public static String encrypt(String data, String salt) throws Exception {
            SecretKeySpec secretKeySpec = new SecretKeySpec(salt.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedData);
        }

        /**
         * AES CBC PKCS7 解密
         * 
         * @param data 待解密的 Base64 编码字符串
         * @param salt 解密使用的盐值，必须与加密时使用的盐值相同
         * @return 解密后的原始数据
         * @throws Exception 解密过程中可能抛出的异常
         */
        public static String decrypt(String data, String salt) throws Exception {
            SecretKeySpec secretKeySpec = new SecretKeySpec(salt.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

            byte[] decodedData = Base64.getDecoder().decode(data);
            byte[] decryptedData = cipher.doFinal(decodedData);
            return new String(decryptedData, StandardCharsets.UTF_8);
        }
    }
}
