import java.text.*;
import java.nio.file.*;
import java.util.*;
import java.awt.*;
import java.net.*;
import java.io.*;

import javax.swing.*;

public class SmtpRelay {
   private Socket socket = null;
   private BufferedReader br = null;
   private OutputStream os = null;
	
   private static String OK = "250";
   private static String DATA = "354";
   private static String PASS = "220";
   private static String QUIT = "221";
	
   private EncryptDecrypt ed = new EncryptDecrypt();
	
   private JTextArea log = null;
	
   private String user = "";
   private String pass = "";
    
   SmtpRelay(boolean sending, String toEmailAddress, String fromEmailAddress, String data, JTextArea log, String _relay) throws Exception {
      this.log = log;
      socket = new Socket("129.21.129.220" ,42069);
      br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      os = socket.getOutputStream();
      String clientID = ("<"+this.socket.getInetAddress().getHostAddress() + ":" + this.socket.getPort()+">");
      log.append("Connection Established w/Client: " + clientID + "\n");
      if(sending){
         try{
            smtp("FROM " + fromEmailAddress);
            smtp("TO " + toEmailAddress);
            smtp("DATA "+ data);
            log.append(clientID + " Relay set to: " + "test" +"\n");
         }catch(Exception e){}
      }
      if(!sending){
         try {
            while(true) 
               try {
                  String str = br.readLine();
                  System.out.println(str);
                  if (str.substring(0, 4).equals("HELO")) {
                     String relay = str.substring(5);
                     smtp(OK);
                     log.append(clientID + " Relay set to: " + relay+"\n");
                  }
                  if (str.substring(0, 4).equals("FROM")) {
                     String from = str.substring(5);
                     smtp(OK);
                     log.append(clientID + " Rcpt set to: " + from+"\n");
                  }
                  if (str.substring(0, 2).equals("TO")) {
                     String to = str.substring(3);
                     smtp(OK);
                     log.append(clientID + " Sender set to: " + to+"\n");
                  }
                  if (str.equals("DATA")) {
                     smtp(DATA);
                     String line = "";
                     while (true) {
                        line = br.readLine();
                        if (!line.equals(".")){
                           data += line + "\n";
                        }else{                  
                           smtp("250: Queued as: null");
                           break;
                        }
                     }
                  }
                  if (str.equals("QUIT")) {
                     smtp(QUIT);
                     br.close();
                     os.close();
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
   }
    
   void smtp(String command) throws Exception {
      os.write(command.getBytes());
      os.flush();
   }
	
   public void doFetch() throws Exception {
      smtp(OK);
      String message = "";
      try{
         Scanner scn = null;    
         File[] emails = new File("accounts/"+user+"/inbox/").listFiles();
         smtp(emails.length+"");
         for (File file : emails) {
            Path path = Paths.get(file.getPath());
            long lineCount = Files.lines(path).count();
            smtp(lineCount+"");
            smtp(file.getName());
            scn = new Scanner(new InputStreamReader(new FileInputStream(file)));
            while(scn.hasNextLine()){
               message = scn.nextLine();
               byte[] cipherText = ed.encryptText(message, ed.getSecretEncryptionKey());
               String hexText = ed.bytesToHex(cipherText);
               smtp(hexText);
            }
            scn.close();
         }
         scn.close();
      }catch(Exception e){
      }
   }
}