import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

public class Credits {
    public static ImageView createCreditsScreen() {
	ImageView showCredits = null;
	try {
	    Image image = new Image("credits.jpg", Monitor.defaultWidth / 2, Monitor.defaultWidth / 2.5, true, true);
	    showCredits = new ImageView(image);
	} catch (Exception ex) {
	    System.out.println("Credits Class (line 21-22): Unable to load 'credits.jpg' - check file system.");
	}
	return showCredits;
    }
}
