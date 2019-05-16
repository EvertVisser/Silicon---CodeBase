
/*
 * SiLiCON - A JavaFX game by:
 * - Clark Lavery (mentor)
 * - Evert Visser (s3727884)
 * - Duncan Baxter (s3737140)
 * - Kira Macarthur (s3742864)
 * - Dao Kun Nie (s3691571)
 * - Michael Power (s3162668)
 * - John Zealand-Doyle (s3319550)
 * 
 * Duncan can answer queries in relation to this Class.
 */

import java.io.InputStream;
import java.util.Scanner;

import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

public class Help {
    public static TextArea createHelpScreen(BorderPane root) {
	TextArea help = new TextArea();
	help.getStyleClass().add("TextArea");
	help.setWrapText(true);
	help.setPrefColumnCount(20);
	help.setPrefRowCount(10);

	try {
	    InputStream inFile = ClassLoader.getSystemResourceAsStream("silicon_help.txt");
	    Scanner scanner = new Scanner(inFile);
	    String line;
	    while (scanner.hasNextLine()) {
		line = scanner.nextLine();
		help.appendText(line+"\n");
	    }
	    scanner.close();
	} catch (Exception ex) {
	    System.out.println("Help Class (lines 31-38): Unable to load 'silicon_help.txt' - file error.");
	}
	help.home();
	return help;
    }
}
