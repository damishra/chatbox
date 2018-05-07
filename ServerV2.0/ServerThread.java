import javax.swing.*;
import java.net.*;

public class ServerThread extends Thread {

   private ServerSocket servSock = null;

   private JTextArea log;

   private Socket clientSocket = null;

   private int ccon;

   ServerThread(JTextArea log, int ccon) {
      this.log = log;
      this.ccon = ccon;
   }

   public synchronized void run() {
      try {
         servSock = new ServerSocket(42069);
         clientSocket = null;
         while(true){
            try {
               clientSocket = servSock.accept();
               ClientThread cthread = new ClientThread(clientSocket, log, ccon);
               cthread.start();
            } catch (Exception e) {
               return;
            }
         }
      } catch (Exception ioe) {
         if (ioe.toString().contains("socket closed")) {
            return;
         }
         log.append("IOException (ServerThread[1]): " + ioe + "\n");
      }
   }

   void kill() {
      try {
         servSock.close();
         clientSocket.close();
      } catch (Exception e) {
         log.append("Exception (ServerThread[2]): " + e + "\n");
      }
   }

}
