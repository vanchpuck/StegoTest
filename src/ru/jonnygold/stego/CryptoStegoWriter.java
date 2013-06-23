/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jonnygold.stego;

import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Vanchpuck
 */
public class CryptoStegoWriter implements Writeble{

    protected static final String KEY_FACTORY = "PBKDF2WithHmacSHA1";
    
    protected static final String SECRET_KEY_ALGORITHM = "AES";
    
    protected static final String ENCRIPTION_ALGORITHM = "AES/CBC/PKCS5Padding";
    
    protected static final byte[] SALT = "megasalt".getBytes();
    
    
    
    private char[] password;
    
    private Writeble writer;
    
    private SecretKeyFactory factory;
    
    private KeySpec spec;
    
    private SecretKey key;
    
    private Cipher cipher;
    
    public CryptoStegoWriter(Writeble writer){
        this.writer = writer;
    }
    
    @Override
    public void write(Secret data) throws IOException {
        
    }
    
    protected byte[] encrypt(byte[] data, ) throws NoSuchAlgorithmException{
        
        /* Derive the key, given password and salt. */
        factory = SecretKeyFactory.getInstance(KEY_FACTORY);
        
        spec = new PBEKeySpec(password, SALT, 65536, 128);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
        
        /* Encrypt the message. */
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = cipher.getParameters();
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        byte[] ciphertext = cipher.doFinal("Hello, World!".getBytes("UTF-8"));
        
    }
    
}
