import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.swing.JTextArea;

/**
 * Work in progress.
 */

@SuppressWarnings("deprecated")
class EncryptDecrypt {
    EncryptDecrypt(String args , JTextArea log) {

        if(!args.equals("")) try {

            SecretKey secKey = getSecretEncryptionKey();
            byte[] cipherText = encryptText(args, secKey);
            String decryptedText = decryptText(cipherText, secKey);
            log.append("\n\nText to be encrypted: " + args);
            log.append("\n\nKey: " + bytesToHex(secKey.getEncoded()));
            log.append("\n\nEncrypted text: " + bytesToHex(cipherText));
            log.append("\n\nDecrypted text: " + decryptedText);

        } catch (Exception ignored) {}
        else System.out.println("No text entered, aborting ...");
    }

    private static SecretKey getSecretEncryptionKey() throws Exception {

        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128);
        return generator.generateKey();

    }

    private static byte[] encryptText(String toEncrypt, SecretKey secKey) throws Exception {

        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        return aesCipher.doFinal(toEncrypt.getBytes());

    }

    private static String decryptText(byte[] byteCipherText, SecretKey secKey) throws Exception {
        
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, secKey);
        byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
        return new String(bytePlainText);

    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}