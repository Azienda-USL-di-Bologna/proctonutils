/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.bologna.ausl.proctonutils;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;

/**
 *
 * @author andrea Zuk
 */
public class PasswordCrypto {

    public static class EncryptedPassword {

        private String password;
        private String salt;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getSalt() {
            return salt;
        }

        public void setSalt(String salt) {
            this.salt = salt;
        }

        @Override
        public String toString() {
            return String.format("Encrypted Password : %s Salt: %s", password, salt);
        }

    }

    public static EncryptedPassword encrypt(String clearTextPassword, String key) {
        String salt = KeyGenerators.string().generateKey();
        TextEncryptor encryptor = Encryptors.text(key, salt);
        String encryptedPassword = encryptor.encrypt(clearTextPassword);
        EncryptedPassword res = new EncryptedPassword();
        res.setPassword(encryptedPassword);
        res.setSalt(salt);
        return res;
    }

    public static String decrypt(String EncryptedPassword, String key, String salt) {
        TextEncryptor decryptor = Encryptors.text(key, salt);
        String decryptedText = decryptor.decrypt(EncryptedPassword);
        return decryptedText;
    }

    public static void main(String[] args) {
        String testString = "test string";
        String key = "test key";
        EncryptedPassword encryptedPassword = encrypt(testString, key);
        System.out.println("Encrypted password: " + encryptedPassword);
        String clearPassword = decrypt(encryptedPassword.getPassword(), key, encryptedPassword.getSalt());
        System.out.println("Decripted password: " + clearPassword);
//        clearPassword = clearPassword + "culo";
        if (!clearPassword.equals(testString)) {
            throw new IllegalStateException("Encryption broken!");
        }
    }
}
