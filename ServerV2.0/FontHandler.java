import java.awt.Font;
import java.awt.FontFormatException;
import java.io.FileInputStream;
import java.io.IOException;

class FontHandler {
    private Font font1 = null;
    FontHandler() {

        try {
            font1 = Font.createFont( Font.TRUETYPE_FONT, new FileInputStream("IBMPlexMono-Text.ttf") );
            font1 = font1.deriveFont(Font.PLAIN, 10);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

    }

    Font setFont1() {
        new FontHandler();
        return font1;
    }
}
