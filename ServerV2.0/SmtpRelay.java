import java.text.*;
import java.util.*;
import java.net.*;
import java.io.*;

public class SmtpRelay {
    private Socket socket = null;
    private BufferedReader br = null;
    private OutputStream os = null;
   
    SmtpRelay(String toEmailAddress, String fromEmailAddress, String data) throws Exception {
       socket = new Socket("your.smtp.server",42069);
       br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
       os = socket.getOutputStream();
       smtp("HELO" + toEmailAddress);
       smtp("MAIL FROM" + fromEmailAddress);
       smtp("DATA");
       smtp(data);
    }
    
    void smtp(String command) throws Exception {
       br.readLine();
       os.write(command.getBytes());
       os.flush();
    }
}