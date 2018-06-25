/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
///package project.phase2;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64.Decoder;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Abhishek
 */
public class Conversation {

    final String fixedAlgo = "AES/CBC/PKCS5Padding"; 
    final String algo = "AES";
    byte[] key;
    byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    IvParameterSpec ivspec = new IvParameterSpec(iv);
    
   
    
    ////////////////////////////////////////////////////////////////////////////
    //
    // This method is used to generate a random key for the encryption and decryption.
    //
    ////////////////////////////////////////////////////////////////////////////
    
    public void generateKeyTemp(byte[] sharedKey) throws NoSuchAlgorithmException{
        int start = 0;
       
        key = Arrays.copyOfRange(sharedKey, 16, sharedKey.length);
        System.out.println(key + " | " + key.length);
        
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    //
    // This method is used to encrypt input string into cipher text in String
    // format.
    //
    ////////////////////////////////////////////////////////////////////////////
    
    public String encryptString(String input) throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException{
        
        Cipher cipherE = Cipher.getInstance(fixedAlgo);
        SecretKeySpec secretKey = new SecretKeySpec(key, algo);
        cipherE.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
        byte[] tempEncrypt = cipherE.doFinal(input.getBytes("UTF-8"));
        JCATime object = new JCATime();    
       String encryptedText = object.convertBytesToStringEncrypt(tempEncrypt);
       byte[] b = encryptedText.getBytes();
        return encryptedText;
        
    }

    ////////////////////////////////////////////////////////////////////////////
    //
    // This method is used to decrypt input string into plainText in String
    // format.
    //
    ////////////////////////////////////////////////////////////////////////////
    public String decryptString(String input) throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException{
        
        String plainText = "";
       
        Cipher cipherD = Cipher.getInstance(fixedAlgo);
        SecretKeySpec secretKey = new SecretKeySpec(key, algo);
        cipherD.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
        JCATime object = new JCATime();
        byte[] cipherText = object.convertBytesToStringDecrypt(input);
        plainText = new String(cipherD.doFinal(cipherText), "UTF-8");
        return plainText;
    }
    
}
