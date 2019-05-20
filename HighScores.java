
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
 * This Class creates the "High Scores" screen.  Its sole method is public and static, 
 * not because this constitutes good coding practice (it would usually be taken 
 * as a strong indication that the code belongs in another class), but to make 
 * it easier for team members to work with the Class.
 * 
 * Note: At this stage, the screen is a place-holder for actual High Scores functionality.
 * 	 The only victory condition implemented, is reaching 500 research points, which
 *       does not allow much scope for differential high scores. 
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
