import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
 * This Class creates the "Credits" screen.  Its sole method is public and static, 
 * not because this constitutes good coding practice (it would usually be taken 
 * as a strong indication that the code belongs in another class), but to make 
 * it easier for team members to work with the Class.
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
	    System.out.println("Credits Class (lines 28-29): Unable to load 'credits.jpg' - check file system.");
	}
	return showCredits;
    }
}
