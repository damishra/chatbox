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

public class Client extends JFrame implements ActionListener{

   //Simple fields for sending message
   private JLabel jlFrom = new JLabel("From:");
   private JLabel jlTo = new JLabel("To:");
   private JLabel jlIP = new JLabel("IP:");
   private JLabel jlSubject = new JLabel("Subject:");
   private JLabel jlMessage = new JLabel("Message:");
   private JLabel jlUser = new JLabel("Username:");
   private JLabel jlPass = new JLabel("Password:");

   private JTextField jtfFrom = new JTextField(14);
   private JTextField jtfTo = new JTextField(14);
   private JTextField jtfIP = new JTextField(14);
   private JTextField jtfSubject = new JTextField(14);
   private JTextArea jtaMessage = new JTextArea(20,40);

   private JButton jbConnect = new JButton("Connect");

   //simple fields for ining message
   private JLabel jlFromR = new JLabel("From:");
   private JLabel jlToR = new JLabel("To:");
   private JLabel jlSubjectR = new JLabel("Subject:");
   private JLabel jlMessageR = new JLabel("Message content:");

   private JTextField jtfFromR = new JTextField(14);
   private JTextField jtfToR = new JTextField(14);
   private JTextField jtfSubjectR = new JTextField(14);
   private JTextArea jtaMessageR = new JTextArea(20,40);
   private JTextField jtfUsername = new JTextField(10);
   private JTextField jtfPassword = new JTextField(10);

   private JButton jbSend = new JButton("Send");
   private JButton jbFetch = new JButton("Fetch");
   
   //java.net
   private Socket socket = null;
   private int PORT_NUMBER = 42069;
   private PrintWriter out = null;
   private Scanner in = null;

   public static void main(String args[]){
      new Client();
   }
   public Client(){
      JPanel jpTop = new JPanel();
      jpTop.setLayout(new GridLayout(5,2));
   
         //Send side
      JPanel jpServer = new JPanel();
      jpServer.setLayout(new FlowLayout(FlowLayout.LEFT));
      jpServer.add(jlIP);
      jpServer.add(jtfIP);
      jpServer.add(jbConnect);
   
      JPanel jpTo = new JPanel();
      jpTo.setLayout(new FlowLayout(FlowLayout.LEFT));
      jpTo.add(jlTo);
      jpTo.add(jtfTo);
   
      JPanel jpFrom = new JPanel();
      jpFrom.setLayout(new FlowLayout(FlowLayout.LEFT));
      jpFrom.add(jlFrom);
      jpFrom.add(jtfFrom);
   
      JPanel jpSubject = new JPanel();
      jpSubject.setLayout(new FlowLayout(FlowLayout.LEFT));
      jpSubject.add(jlSubject);
      jpSubject.add(jtfSubject);
   
         //view side
      JPanel jpFromR = new JPanel();
      jpFromR.setLayout(new FlowLayout(FlowLayout.LEFT));
      jpFromR.add(jlFromR);
      jpFromR.add(jtfFromR);
   
      JPanel jpToR = new JPanel();
      jpToR.setLayout(new FlowLayout(FlowLayout.LEFT));
      jpToR.add(jlToR);
      jpToR.add(jtfToR);
   
      JPanel jpSubjectR = new JPanel();
      jpSubjectR.setLayout(new FlowLayout(FlowLayout.LEFT));
      jpSubjectR.add(jlSubjectR);
      jpSubjectR.add(jtfSubjectR);
   
      //5 rows by 2 columns
      jpTop.add(jpServer);  jpTop.add(new JPanel());
      jpTop.add(jpTo);      //jpTop.add(jpToR);
      jpTop.add(jpFrom);    //jpTop.add(jpFromR);
      jpTop.add(jpSubject); //jpTop.add(jpSubjectR);
   
      JPanel jpCenter = new JPanel();
      jpCenter.setLayout(new GridLayout(1,2));
      jpCenter.add(jtaMessage);
      jpCenter.add(jtaMessageR);
   
      JPanel jpSouth = new JPanel();
      jpSouth.setLayout(new FlowLayout(FlowLayout.LEFT));
      jpSouth.add(jlUser);
      jpSouth.add(jtfUsername);
      jpSouth.add(jlPass);
      jpSouth.add(jtfPassword);
      jpSouth.add(jbSend);
      jpSouth.add(jbFetch);
   
      this.add(jpSouth,BorderLayout.SOUTH);
      this.add(jpCenter, BorderLayout.CENTER);
      this.add(jpTop, BorderLayout.NORTH);
   
      jbConnect.addActionListener(this);
      jbSend.addActionListener(this);
      jbFetch.addActionListener(this);
   
      setTitle("Client");
      setVisible(true);
      setSize(800,600);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setLocation(0,0);
   }
   
   public void actionPerformed(ActionEvent ae){
   
      switch(ae.getActionCommand()){
         case "Connect":
            doConnect();
            break;
      
         case "Send":
            doSend();
            break;
      
         case "Fetch":
            doFetch();
            break;
      }
   }
   
   public void doConnect(){
      try{
         socket = new Socket(jtfIP.getText(),PORT_NUMBER);
         out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
         in = new Scanner(new InputStreamReader(socket.getInputStream()));
         doOutIn(jtfUsername.getText());
         doOutIn(jtfPassword.getText());
         doOutIn("HELO relay.group_three.org");         
         
      }catch(ConnectException ce){
         System.out.println("Could not connect to the server, is it on?");
      }catch(InterruptedIOException iioe){
         System.out.println("Network timeout, looks like the server turned off");
      }catch(IOException ioe){
         ioe.printStackTrace();
      }
   }
   
   public void doSend(){
      Message msg = new Message(jtfTo.getText(),jtfFrom.getText(),jtfSubject.getText(),jtaMessage.getText(),jtfIP.getText());
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
            System.out.println(fileName+"");
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)));
            for(int j = 0; j<fileLines; j++){
               bw.write(doInString());
               bw.newLine();
               bw.flush();
            }
            bw.close();
         }
      }catch(Exception e){}
   }
   
   public String doInString(){
      return in.nextLine();
   }
   
   public void doIn(){
      jtaMessageR.append(in.nextLine() + "\n");
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
