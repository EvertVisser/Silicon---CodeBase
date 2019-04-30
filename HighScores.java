
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

public class HighScores {
    public static TextArea createHighScoresScreen() {
	TextArea showHighScores = new TextArea();
	showHighScores.getStyleClass().add("TextArea");
	showHighScores.setMinWidth(Monitor.defaultWidth / 4);
	showHighScores.setMaxWidth(Monitor.defaultWidth / 4);
	showHighScores.setMinHeight(Monitor.defaultHeight / 2);
	showHighScores.setMaxHeight(Monitor.defaultHeight / 2);

	String highScoresTable = "";
	try {
	    InputStream inFile = ClassLoader.getSystemResourceAsStream("high_scores.txt");
	    Scanner scanner = new Scanner(inFile);
	    highScoresTable = scanner.nextLine();
	    scanner.close();

	    String[] highScoresLine = highScoresTable.split(",");
	    for (int i = 0; i < 5; i++) {
		showHighScores
			.appendText(String.format("%-16s %8s\n", highScoresLine[i * 3], highScoresLine[i * 3 + 2]));
	    }
	} catch (Exception e) {
	    System.out.println("HighScores Class (lines 31-38): Unable to load 'high_scores.txt' - file error.");
	}
	return showHighScores;
    }
}
