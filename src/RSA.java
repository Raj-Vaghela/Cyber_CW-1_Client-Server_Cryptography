import java.io.*;
import java.util.*;
import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import java.nio.file.*;

public class RSA {

    public static void main(String args[]) throws Exception {

        if (args.length != 1) {
            System.out.println("Usage: java RSA -e|-d");
            System.exit(0);
        }
        else if(args[0].equals("-e")) {

            // taking input and converting to bytes
            System.out.println("Enter a message: ");
            Scanner sc = new Scanner(System.in);
            String msg = sc.nextLine();
            byte[] stringBytes = msg.getBytes("UTF8");
            sc.close();

            // read the public key
            File f = new File("raj.pub");
            byte[] keyBytes = Files.readAllBytes(f.toPath());
            X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey pubKey = kf.generatePublic(pubSpec);

            //Encryption using doFinal()
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] raw = cipher.doFinal(stringBytes);

            // write bytes to file called encrypted.msg
            System.out.println("Encrypted message: " + raw);

        }
        else if(args[0].equals("-d")) {
            //Read Private Key
            File f = new File("raj.prv");
            byte[] keyBytes = Files.readAllBytes(f.toPath());
            PKCS8EncodedKeySpec prvSpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey prvKey = kf.generatePrivate(prvSpec);

            // read file
            File file = new File("encrypted.msg");
            byte[] raw = Files.readAllBytes(file.toPath());

            // decrypt
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, prvKey);
            byte[] stringBytes = cipher.doFinal(raw);
            String result = new String(stringBytes, "UTF8");
            System.out.println(result);
        }
        else {
            System.out.println("Usage: java RSA -e|-d");
            System.exit(0);
        }
    }
}