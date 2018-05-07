import javax.swing.*;
import java.awt.*;

public class guiPurgatory extends JFrame {

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
   private JLabel jlServerIP = new JLabel("Server Address: ");
   private JLabel jlUsername = new JLabel("Username: ");
   private JLabel jlPassword = new JLabel("Password: ");
   private JTextField jtfServerIP = new JTextField(15);
   private JTextField jtfUsername = new JTextField(15);
   private JTextField jtfPassword = new JTextField(15);
   private JButton jbLogin = new JButton("Login");
      //end login popup
   
   public static void main(String[] args) {
      new guiPurgatory();
   }
   private guiPurgatory(){
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

      this.pack();
      this.setVisible(true);
   } 
   private void login(JFrame jf){
      JFrame login = new JFrame();
      
      this.setLocationRelativeTo(null);
      this.setTitle("Login");
      this.setDefaultCloseOperation(EXIT_ON_CLOSE);
      this.setSize(100,100);
      
      login.add(jpLogin,BorderLayout.CENTER);
      jpLogin.setLayout(new GridLayout(3,2));
      jpLogin.add(jlServerIP);
      jpLogin.add(jtfServerIP);
      jpLogin.add(jlUsername);
      jpLogin.add(jtfUsername);
      jpLogin.add(jlPassword);
      jpLogin.add(jtfPassword);
      
      
      login.pack();
      login.setVisible(false);
   }
}