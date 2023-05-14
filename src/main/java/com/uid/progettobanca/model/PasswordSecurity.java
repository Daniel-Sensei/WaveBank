package com.uid.progettobanca.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordSecurity {

    public static String hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException {

        // Crea un oggetto MessageDigest per applicare l'algoritmo di hashing SHA-256
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // Aggiunge il "sale" alla password
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes());

        // Converte il risultato in una stringa esadecimale
        StringBuilder sb = new StringBuilder();
        for (byte b : hashedPassword) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public static byte[] generateSalt() {
        // Crea un oggetto SecureRandom per generare un salt casuale
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }
}
