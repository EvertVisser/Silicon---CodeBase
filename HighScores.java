
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
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class HighScores {
    public static VBox createHighScoresScreen() {
	// Create the header for the High Scores table
	Text tHighScores = new Text("High Scores Table");
	tHighScores.setId("text-flow-header");
	TextFlow tfHighScores = new TextFlow(tHighScores);
	tfHighScores.getStyleClass().add("text-flow");
	tfHighScores.setMinWidth(Monitor.defaultWidth / 4);
	tfHighScores.setMaxWidth(Monitor.defaultWidth / 4);
	
	// Create the TextArea for the High Scores table
	TextArea taHighScores = new TextArea();
	taHighScores.getStyleClass().add("TextArea");
	taHighScores.setMinWidth(Monitor.defaultWidth / 4);
	taHighScores.setMaxWidth(Monitor.defaultWidth / 4);
	taHighScores.setMinHeight(Monitor.defaultHeight / 2);
	taHighScores.setMaxHeight(Monitor.defaultHeight / 2);

	// Populate the High Scores table from the file
	String highScoresTable = "";
	try {
	    InputStream inFile = ClassLoader.getSystemResourceAsStream("high_scores.txt");
	    Scanner scanner = new Scanner(inFile);
	    highScoresTable = scanner.nextLine();
	    scanner.close();

	    String[] highScoresLine = highScoresTable.split(",");
	    for (int i = 0; i < 5; i++) {
		taHighScores
			.appendText(String.format("%-16s%24s\n", highScoresLine[i * 3], highScoresLine[i * 3 + 2]));
	    }
	} catch (Exception e) {
	    System.out.println("HighScores Class (lines 31-38): Unable to load 'high_scores.txt' - file error.");
	}

	// Create a VBox to hold the header and High Scores table
	VBox showHighScores = new VBox(tfHighScores, taHighScores);
	showHighScores.setId("VBox-invis");
	return showHighScores;
    }
}
