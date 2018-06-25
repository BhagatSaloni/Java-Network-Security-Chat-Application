//package chat;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package project.phase2;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.Provider.Service;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 *
 * @author Abhishek
 */


public class JCATime{
    
    final String firstCipher = "AES/CBC/PKCS5Padding";
    final String secondCipher = "DES/CBC/PKCS5Padding";
    final String firstCipherName = "AES";
    final String secondCipherName = "DES";
    
    
    // To convert bytes of cipher to String
    public String convertBytesToStringEncrypt(byte[] cipher){
        
        return Base64.getEncoder().encodeToString(cipher);
    }
    
    // To convert bytes of plaintext to String
    public byte[] convertBytesToStringDecrypt(String plainText){
        
        
        return Base64.getDecoder().decode(plainText);
        
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    //    This function fetches the information from a file in the form of String
    ////////////////////////////////////////////////////////////////////////////
    static String getStringFromFile(String PathOfFile) throws IOException{
        
        String data = "";
        data = new String(Files.readAllBytes(Paths.get(PathOfFile)));
        
        return data;
    }
    
    
    
    ////////////////////////////////////////////////////////////////////////////
    //
    //  This function is used to encrypt data fetched from a large file in
    //  the form of String usinf AES. The function divides large string 
    //  into predefined sized chunks of 100000 bytes and converts them to 
    //  encrypted bytes one by one.
    //
    ////////////////////////////////////////////////////////////////////////////
    public void singleKeyEncryption() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IOException{
        
        Cipher cipher = Cipher.getInstance(firstCipher);
        KeyGenerator generator = KeyGenerator.getInstance(firstCipherName);
        SecureRandom secureRandom = new SecureRandom();
        int keySize = 128;
        generator.init(keySize, secureRandom);
        SecretKey secretKey = generator.generateKey();
        
        byte[] initialVector = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        IvParameterSpec specifics = new IvParameterSpec(initialVector);
        
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, specifics);
        
        String encryptString = getStringFromFile("D:\\Tes.txt");
        
        //byte[] byteData = encryptString.getBytes("UTF-8");
        //byte[] cipherText = cipher.doFinal(byteData);
        
        StringBuilder cipherText = new StringBuilder();
        int start=0;
      
        for(start=0; start<encryptString.length()-100000; start+=100000){
            
            String tmp = encryptString.substring(start, start+100000);
            byte[] temCipher = cipher.update(tmp.getBytes("UTF-8"));
            String temp = convertBytesToStringEncrypt(temCipher);
            cipherText.append(temp);
        }
        cipherText.append(convertBytesToStringEncrypt(cipher.doFinal(encryptString.substring(start, encryptString.length()).getBytes("UTF-8"))));
        System.out.println("Input String Lenght : " + encryptString.length() + "\n" + "Cipher text lenght : " +cipherText.length());
        System.gc();
    }
    
    
    ///////////////////////////////////////////////////////////////////////////
    //
    //  This method is used to test the accuracy of the encryption using DES. a small
    //  string is used to as a test string and encrypted by 2500000 keys one-
    //  by-one to process them.
    //
    ////////////////////////////////////////////////////////////////////////////
    
    public void keyTestingRateDES() throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
        
        Cipher cipher = Cipher.getInstance(secondCipher);
        KeyGenerator generator = KeyGenerator.getInstance(secondCipherName);
        SecureRandom secureRandom = new SecureRandom();
        int keySize = 56;
        generator.init(keySize, secureRandom);
        byte[] testString = "It's nice weather in here. Best month of the year".getBytes("UTF-8");
        
        //String outputString = "";
        SecretKey secretKey;
        ArrayList<SecretKey> keyArray = new ArrayList<>();
        
        for(int i=0; i<2500000; i++){
            
            secretKey = generator.generateKey();
            keyArray.add(secretKey);
        }
        for(int i=0; i<2500000; i++){
            SecretKey secret = keyArray.get(i);
                    
            cipher.init(cipher.ENCRYPT_MODE, secret);
            
            testString = cipher.update(testString);
            
        }
        System.out.println("Processed Testing using DES. " + "Size of Array : "+ keyArray.size());
        System.gc();

        
    }
    
      
    ///////////////////////////////////////////////////////////////////////////
    //
    //  This method is used to test the accuracy of the encryption using AES. 
    //  a small string is used to as a test string and encrypted by 2500000 
    // keys one-by-one to process them.
    //
    ////////////////////////////////////////////////////////////////////////////
    
    public void keyTestingRateAES() throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
        
        Cipher cipher = Cipher.getInstance(firstCipher);
        KeyGenerator generator = KeyGenerator.getInstance(firstCipherName);
        SecureRandom secureRandom = new SecureRandom();
        int keySize = 128;
        generator.init(keySize, secureRandom);
        byte[] testString = "It's nice weather in here. Best month of the year".getBytes("UTF-8");
        
        //String outputString = "";
        SecretKey secretKey;
        ArrayList<SecretKey> keyArray = new ArrayList<>();
        
        for(int i=0; i<2500000; i++){
            
            secretKey = generator.generateKey();
            keyArray.add(secretKey);
        }
        for(int i=0; i<2500000; i++){
            SecretKey secret = keyArray.get(i);
                    
            cipher.init(cipher.ENCRYPT_MODE, secret);
            
            testString = cipher.update(testString);
            
        }
        System.out.println("Processed Testing using AES. " + "Size of Key Array : " + keyArray.size());
        System.gc();

        
    }
    
}


