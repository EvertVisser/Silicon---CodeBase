
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

import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

public class Help {
    public static TextArea createHelpScreen(BorderPane root) {
	TextArea help = new TextArea();
	help.getStyleClass().add("TextArea");
	help.setPrefColumnCount(20);
	help.setPrefRowCount(10);
	return help;
    }
}
