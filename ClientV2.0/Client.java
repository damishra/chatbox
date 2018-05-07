//GUI
import javax.swing.*;
//Event
import java.awt.*;
import java.awt.event.*;
//IO
import java.io.*;
//Util
import java.util.*;
//net
import java.net.*;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;

public class Client extends JFrame implements ActionListener{
   
   private static final int PORT_NUMBER = 42069;

   private Socket socket = null;
   private PrintWriter out = null;
   private Scanner in = null;
   private EncryptDecrypt ed = new EncryptDecrypt();
      
   private JFrame login = new JFrame();
   private JFrame viewEmails = new JFrame();

   private JPanel jpNorthContainer = new JPanel();
   private JPanel jpNorthTop = new JPanel();
   private JPanel jpNorthBot = new JPanel();
   private JPanel jpEast = new JPanel();
   private JPanel jpSouth = new JPanel();
   private JPanel jpWest = new JPanel();
   private JPanel jpCenter = new JPanel();
   
      //menu bar   
   private JMenuBar jmb = new JMenuBar();
         //help menu
   private JMenu jmHelp = new JMenu("Help?");
   private JMenuItem jmiAbout = new JMenuItem("About");
         //email menu
   private JMenu jmEmail = new JMenu("Email Client Controls");
   private JMenuItem jmiConnect = new JMenuItem("Connect to Server");
   private JMenuItem jmiDisconnect = new JMenuItem("Disconnect from Server");
   private JMenuItem jmiLine1 = new JMenuItem("------------------------------");
   private JMenuItem jmiSend = new JMenuItem("Send Email");
   private JMenuItem jmiFetch = new JMenuItem("Fetch Emails");
   private JMenuItem jmiRead = new JMenuItem("Open Emails");
   private JMenuItem jmiLine2 = new JMenuItem("------------------------------");
   private JMenuItem jmiQuit = new JMenuItem("Quit Client");
      //end menu bar
   
      //to and subj
   private JLabel jlRcpt = new JLabel("Rcpt: ");
   private JLabel jlSubj = new JLabel("Subj: ");
   private JTextField jtfRcpt = new JTextField(20);
   private JTextField jtfSubj = new JTextField(20);
      //end to and subj
   
      //body and log
   private JTextArea jtaEmail = new JTextArea(35,35);
   private JScrollPane jspEmail = new JScrollPane(jtaEmail);
   private JTextArea jtaLog = new JTextArea(35,15);
   private JScrollPane jspLog = new JScrollPane(jtaLog);
      //end body and log
   
      //login popup
   private JPanel jpLogin = new JPanel();
   private JPanel jpLoginSouth = new JPanel();
   private JLabel jlServerIP = new JLabel("Server Address: ");
   private JLabel jlUsername = new JLabel("Username: ");
   private JLabel jlPassword = new JLabel("Password: ");
   private JTextField jtfServerIP = new JTextField(15);
   private JTextField jtfUsername = new JTextField(15);
   private JTextField jtfPassword = new JTextField(15);
   private JButton jbLogin = new JButton("Login");
      //end login popup
      
      //email viewer popup
   private JTextArea jtaViewer = new JTextArea(50,50);
   private JScrollPane jspViewer = new JScrollPane(jtaViewer);
      //end email viewer popup
   
   public static void main(String[] args) {
      new Client();
   }
   private Client(){
      //Font font = new FontHandler().setFont1();
   
      this.setLocationRelativeTo(null);
      this.setTitle("Client Placeholder");
      this.setDefaultCloseOperation(EXIT_ON_CLOSE);
      this.setSize(700,700);
      
      this.setJMenuBar(jmb);
      jmb.add(jmEmail);
      jmEmail.add(jmiConnect);
      jmEmail.add(jmiDisconnect);
      jmEmail.add(jmiLine1);
      jmiLine1.setEnabled(false);
      jmEmail.add(jmiSend);
      jmEmail.add(jmiFetch);
      jmEmail.add(jmiRead);
      jmEmail.add(jmiLine2);
      jmiLine2.setEnabled(false);
      jmEmail.add(jmiQuit);
      jmb.add(jmHelp);
      jmHelp.add(jmiAbout);
      
      this.add(jpCenter,BorderLayout.CENTER);
      jpCenter.add(jspEmail, BorderLayout.WEST);
      jpCenter.add(jspLog, BorderLayout.EAST);
      
      this.add(jpNorthContainer,BorderLayout.NORTH);
      jpNorthContainer.setLayout(new GridLayout(2,1));
      jpNorthContainer.add(jpNorthTop);
      jpNorthContainer.add(jpNorthBot);
      jpNorthTop.setLayout(new FlowLayout(FlowLayout.LEFT));
      jpNorthBot.setLayout(new FlowLayout(FlowLayout.LEFT));
      jpNorthTop.add(jlRcpt);
      jpNorthTop.add(jtfRcpt);
      jpNorthBot.add(jlSubj);
      jpNorthBot.add(jtfSubj);
   
      login(this);
      viewEmails(this);
   
      this.pack();
      this.setVisible(true);
      
      jmiConnect.addActionListener(this);
      jmiSend.addActionListener(this);
      jmiFetch.addActionListener(this);
      jbLogin.addActionListener(this);
      jmiAbout.addActionListener(this);
      jmiQuit.addActionListener(this);
      jmiDisconnect.addActionListener(this);
      jmiRead.addActionListener(this);
      
   } 
   
   private void login(JFrame jf){
      
      login.setLocationRelativeTo(null);
      login.setTitle("Login");
      login.setSize(100,100);
      
      login.add(jpLogin,BorderLayout.CENTER);
      login.add(jpLoginSouth,BorderLayout.SOUTH);
      
      jpLogin.setLayout(new GridLayout(3,2));
      
      jpLogin.add(jlServerIP);
      jpLogin.add(jtfServerIP);
      jpLogin.add(jlUsername);
      jpLogin.add(jtfUsername);
      jpLogin.add(jlPassword);
      jpLogin.add(jtfPassword);
      jpLoginSouth.add(jbLogin);
      
      login.pack();
      login.setVisible(false);
   }
   
   private void viewEmails(JFrame jf){
   
      viewEmails.setLocationRelativeTo(null);
      viewEmails.setTitle("Viewer");
      viewEmails.setSize(100,100);
      viewEmails.add(jspViewer,BorderLayout.CENTER);
      viewEmails.pack();
      viewEmails.setVisible(false);
      
   }
   
   public void actionPerformed(ActionEvent ae){
   
      switch(ae.getActionCommand()){
         case "Connect to Server":
            doConnect();
            break;
         
         case "Login":
            doLogin();
            break;
            
         case"Disconnect from Server":
            doQuit();
            break;
      
         case "Send Email":
            doSend();
            break;
      
         case "Fetch Emails":
            doFetch();
            break;
            
         case "Open Emails":
            doOpenEmails();
            break;
      }
   }
   
   public void doConnect(){
      login.setVisible(true);
   }
   
   public void doLogin(){
      try{
         socket = new Socket(jtfServerIP.getText(),PORT_NUMBER);
         out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
         in = new Scanner(new InputStreamReader(socket.getInputStream()));
         doOutIn(jtfUsername.getText());
         doOutIn(jtfPassword.getText());
         doOutIn("HELO relay.group_three.org");         
         login.setVisible(false);
         
      }catch(ConnectException ce){
         System.out.println("Could not connect to the server, is it on?");
      }catch(InterruptedIOException iioe){
         System.out.println("Network timeout, looks like the server turned off");
      }catch(IOException ioe){
         ioe.printStackTrace();
      }
   }
   
   public void doSend(){
      Message msg = new Message(jtfRcpt.getText(),jtfUsername.getText(),jtfSubj.getText(),jtaEmail.getText(),jtfServerIP.getText());
      if(msg.isValid()){
         msg.parseMessage();
         try{
            doOutIn("FROM " + msg.getFrom());
            doOutIn("TO " + msg.getTo());
            doOutIn("DATA");
            ArrayList<String> arr = msg.formatMessage();
            String line = "";
            ListIterator<String> it = arr.listIterator();
            while(it.hasNext()){
               line = it.next();
               doOut(line);
            }
            doIn();
         }catch(Exception e){}
      }else{
         System.out.println("Invalid");
      }
   }
   
   public void doQuit(){
      doOutIn("QUIT");
   }  
   
   public void doFetch(){
      String fileName = "";
      String serverFTP = "";
      int fileLines = 0;
      int numberOfFiles = 0;
      BufferedWriter bw = null;
   
      try{
         doOutIn("FETCH");
         numberOfFiles = Integer.parseInt(doInString());
         System.out.println(numberOfFiles+"");
         for(int i = 0; i<numberOfFiles; i++){
            fileLines = Integer.parseInt(doInString());
            System.out.println(fileLines+"");
            fileName = doInString();
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("inbox/"+fileName)));
            for(int j = 0; j<fileLines; j++){
               String message = doInString();
               System.out.println(message);
               bw.write(message);
               bw.newLine();
               bw.flush();
            }
            bw.close();
         }
      }catch(Exception e){}
   }
   
   public void doOpenEmails(){
      try{
         jtaViewer.setText("");
         Scanner scnViewer = null;
         String viewerLine = "";
         JFileChooser fileChooser = new JFileChooser();
         fileChooser.setCurrentDirectory(new File("inbox"));
         fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
         int result = fileChooser.showOpenDialog(this);
         if (result == JFileChooser.APPROVE_OPTION) {
            viewEmails.setVisible(true);
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            scnViewer = new Scanner(selectedFile);
         }
         while(!viewerLine.equals(".")){
            for(int i = 1; i <= 4; i++){
               viewerLine = scnViewer.nextLine()+"\n";
               jtaViewer.append(viewerLine);
            }
            viewerLine = scnViewer.nextLine();
            byte[] messageBytes = ed.hexStringToByteArray(viewerLine);
            String unencryptedViewer = ed.decryptText(messageBytes, ed.getSecretEncryptionKey());
            jtaViewer.append(unencryptedViewer);
         }
         scnViewer.close();
      }catch(Exception e){
      }      
   }
   
   public String doInString(){
      return in.nextLine();
   }
   
   public void doIn(){
      jtaLog.append(in.nextLine() + "\n");
   }
   public void doOut(String message){
      out.println(message);
      out.flush();
   }
   public void doOutIn(String message){
      doOut(message);
      doIn();
   }
}
