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
   private PrintWriter out = null;
	
   private static String OK = "250";
   private static String DATA = "354";
   private static String PASS = "220";
   private static String QUIT = "221";
	
   private JTextArea log = null;
	
   private String user = "";
   private String pass = "";
    
   SmtpRelay(boolean sending, String toEmailAddress, String fromEmailAddress, String data, JTextArea log, String _relay) throws Exception {
      this.log = log;
      socket = new Socket(_relay ,42069);
      br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
      String clientID = ("<"+this.socket.getInetAddress().getHostAddress() + ":" + this.socket.getPort()+">");
      log.append("Connection Established w/Client: " + clientID + "\n");
      if(sending){
         try{
            smtp("relay");
            log.append(clientID + br.readLine() +"\n");
            smtp("FROM " + fromEmailAddress);
            smtp("TO " + toEmailAddress);
            smtp("DATA");
            smtp(data);
            smtp(".");
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
               } catch (Exception e) {
                  break;
               }
         } catch (Exception e) {
            log.append("Exception (ClientThread): " + e + "\n");
         }
      }
   }
    
   void smtp(String command) throws Exception {
      out.println(command);
      out.flush();
   }
}