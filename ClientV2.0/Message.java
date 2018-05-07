import java.time.LocalDateTime;
import java.util.ArrayList;


class Message {


   private String to = "null1";
   private String from = "null1";
   private String subject = "null1";
   private String message = "null1";
   private String ip = "null1";
   private LocalDateTime ldt = LocalDateTime.now();
   
   private EncryptDecrypt ed = new EncryptDecrypt();

   ArrayList<String> cc = new ArrayList<String>();

   public Message(String to_, String from_, String subject_, String message_, String ip_) {
      to = to_;
      from = from_;
      subject = subject_;
      message = message_;
      ip = ip_;
   }

   boolean isValid() {
   
      return true;
   }

   void parseMessage() {
   }

   String getFrom() {
      return from;
   }
    
   String getTo() {
      return to;
   }
    
   String getMessage() throws Exception{
	   byte[] ba = ed.encryptText(message, ed.getSecretEncryptionKey());
	   message = ed.bytesToHex(ba);
      return message;
   }

   ArrayList<String> formatMessage() throws Exception {
      ArrayList<String> formatMessage = new ArrayList<>();
      formatMessage.add(from);
      formatMessage.add(to);
      formatMessage.add(""+ldt);
      formatMessage.add(subject);
      formatMessage.add(getMessage());
      formatMessage.add(".");
      return formatMessage;
   }
}
