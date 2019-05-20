
/*
 * GRP-COSC2635 2D
 * (a.k.a. ERROR 404: TEAM NAME NOT FOUND)
 * SiLiCON - A JavaFX game by:
 * - Clark Lavery (mentor)
 * - Evert Visser (s3727884)
 * - Duncan Baxter (s3737140)
 * - Kira Macarthur (s3742864)
 * - Dao Kun Nie (s3691571)
 * - John Zealand-Doyle (s3319550)
 * - ex-team member Michael Power (s3162668)
 * 
 * This Class creates the "Help" screen.  Its sole method is public and static, 
 * not because this constitutes good coding practice (it would usually be taken 
 * as a strong indication that the code belongs in another class), but to make 
 * it easier for team members to work with the Class.
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
		help.appendText(line + "\n");
	    }
	    scanner.close();
	} catch (Exception ex) {
	    System.out.println("Help Class (lines 36-41): Unable to load 'silicon_help.txt' - file error.");
	}
	help.home();
	return help;
    }
}
