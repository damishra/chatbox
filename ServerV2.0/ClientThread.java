import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.*;
import java.nio.file.*;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;

public class ClientThread extends Thread {
   private Socket clientSocket;
   private JTextArea log;
   
   private EncryptDecrypt ed = new EncryptDecrypt();

   private static String OK = "250";
   private static String DATA = "354";
   private static String PASS = "220";
   private static String QUIT = "221";

   private PrintWriter out = null;
   private Scanner in = null;
   
   private String user = "";
   private String pass = "";

   private int ccon;

   ClientThread(Socket clientSocket, JTextArea log, int ccon) {
      this.clientSocket = clientSocket;
      this.log = log;
      this.ccon = ccon;
   }

   public synchronized void run() {
      try {
         ccon += 1;
         in = new Scanner(new InputStreamReader(clientSocket.getInputStream()));
         out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
         String clientID = ("<"+this.clientSocket.getInetAddress().getHostAddress() + ":" + this.clientSocket.getPort()+">");
         log.append("Connection Established w/Client: " + clientID + "\n");
         user = in.nextLine();
         log.append(clientID + " Command: " + user + "\n");
         doReply(OK);
         pass = in.nextLine();
         log.append(clientID + " Command: " + pass + "\n");
         doReply(PASS);
         
         while(true) 
            try {
               String str = in.nextLine();
               System.out.println(str);
               if (str.substring(0, 4).equals("HELO")) {
                  String relay = str.substring(5);
                  doReply(OK);
                  log.append(clientID + " Relay set to: " + relay+"\n");
               }
               if (str.substring(0, 4).equals("FROM")) {
                  String from = str.substring(5);
                  doReply(OK);
                  log.append(clientID + " Rcpt set to: " + from+"\n");
               }
               if (str.substring(0, 2).equals("TO")) {
                  String to = str.substring(3);
                  doReply(OK);
                  log.append(clientID + " Sender set to: " + to+"\n");
               }
               if (str.equals("DATA")) {
                  doReply(DATA);
                  String line = "";
                  String data = "";
                  while (true) {
                     line = in.nextLine();
                     if (!line.equals(".")){
                        data += line + "\n";
                     }else{                  
                        doReply("250: Queued as: 1");
                        doSend();
                        break;
                     }
                  }
               }
               if (str.equals("QUIT")) {
                  doReply(QUIT);
                  out.close();
                  in.close();
                  this.kill();
               }
               
               //retrieve will gather all emails for user and send it over to them
               if (str.equals("FETCH")){
                  doFetch();
               }
               
            } catch (Exception e) {
               break;
            }
      } catch (Exception e) {
         log.append("Exception (ClientThread): " + e + "\n");
      }
   }
   public void kill() {
      try{
         this.interrupt();
      }catch(Exception e){}
   }
   public void doReply(String CODE){
      out.println(CODE);
      out.flush();
   }
   
   public void doFetch(){
      doReply(OK);
      String message = "";
      try{
         Scanner scn = null;    
         File[] emails = new File("accounts/"+user+"/inbox/").listFiles();
         doReply(emails.length+"");
         for (File file : emails) {
            Path path = Paths.get(file.getPath());
            long lineCount = Files.lines(path).count();
            doReply(lineCount+"");
            doReply(file.getName());
            scn = new Scanner(new InputStreamReader(new FileInputStream(file)));
            while(scn.hasNextLine()){
               message = scn.nextLine();
               SecretKey secKey = ed.getSecretEncryptionKey();
               byte[] cipherText = ed.encryptText(message, secKey);
               String hexText = ed.bytesToHex(cipherText);
               doReply(hexText);
            }
            scn.close();
         }
         scn.close();
      }catch(Exception e){
      }
   }
   
   public void doSend(){
      SmtpRelay(to, from, data, log, relay);
   }

   
}