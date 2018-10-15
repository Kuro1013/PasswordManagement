package com.zcba.abcz.android.passwordmanagement;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 文字列を暗号化、複合化するクラス
 */
public class CryptUtil {
    // アルゴリズム
    private static final String ALGORITHM = "AES";
    private static final String MODE ="AES/CBC/PKCS5Padding";
    private static final String IV = "er9hJJBMp8ef9hjs";

    // 暗号複合キー
    private static final String SECRETKEY = "SiKftPF5LyyJ9iuz";

    /**
     * 文字列を暗号化する
     *
     * @param value     暗号化する文字列
     * @return String   暗号化済み文字列
     */
    public String encrypt(String value) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRETKEY.getBytes(StandardCharsets.UTF_8),ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
        Cipher cipher = Cipher.getInstance(MODE);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);
        byte[] values = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeToString(values, Base64.DEFAULT);
    }

    /**
     * 文字列を複合化する
     *
     * @param value     複合化する文字列
     * @return String   複合化済み文字列
     */
    public String decrypt(String value) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] values = Base64.decode(value, Base64.DEFAULT);
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRETKEY.getBytes(StandardCharsets.UTF_8),ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
        Cipher cipher = Cipher.getInstance(MODE);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
        return new String(cipher.doFinal(values));
    }
}
