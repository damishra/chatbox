import java.io.Serializable;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;

public class UsrPass implements Serializable {
    private int id;
    private String username;
    private String password;
    EncryptDecrypt ed = new EncryptDecrypt();

    UsrPass(int id) {
        this.id = id;
    }
    
    UsrPass(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
    
    String hashed(String toEncrypt) throws Exception {
        byte[] ba = ed.encryptText(toEncrypt, ed.getSecretEncryptionKey());
        return ed.bytesToHex(ba);
    }
    
    String encryptUser() throws Exception {
        return hashed(username);
    }
    
    String encryptPass() throws Exception {
        return hashed(password);
    }
    
    String getUsername() {
        return encryptUser();
    }

    int Id() {
        return id;
    }
}
