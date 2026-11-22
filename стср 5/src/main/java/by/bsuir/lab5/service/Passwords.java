package by.bsuir.lab5.service;
import java.security.*;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
final class Passwords {
    private static final int ITERATIONS=210000;
    private static byte[] derive(String password,byte[] salt,int iterations) throws GeneralSecurityException {
        PBEKeySpec spec=new PBEKeySpec(password.toCharArray(),salt,iterations,256);
        try { return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded(); } finally {spec.clearPassword();}
    }
    static String hash(String password) throws GeneralSecurityException {byte[] salt=new byte[16];new SecureRandom().nextBytes(salt);return ITERATIONS+":"+Base64.getEncoder().encodeToString(salt)+":"+Base64.getEncoder().encodeToString(derive(password,salt,ITERATIONS));}
    static boolean verify(String password,String stored) throws GeneralSecurityException {
        try {String[] parts=stored.split(":");int count=Integer.parseInt(parts[0]);if(count<100000 || count>1000000) return false;byte[] salt=Base64.getDecoder().decode(parts[1]);byte[] expected=Base64.getDecoder().decode(parts[2]);return MessageDigest.isEqual(expected,derive(password,salt,count));}
        catch(IllegalArgumentException | ArrayIndexOutOfBoundsException e){return false;}
    }
}
