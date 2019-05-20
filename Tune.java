
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
 * This was to be the code for a new thread that would create the slider controls
 * for the media player in MiNiSYNTH.  However, it turned out to be easier to use 
 * a lambda function for the new runnable (refer to the first line of the 
 * createMP3player() method in Sound.java (part of the MiNiSYNTH source-files).
 * This class ended up merely providing "convenience" constructors for associated 
 * Media, MediaPlayer and MediaView objects.
 *
 * Duncan can answer queries in relation to this Class.
 */

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class Tune implements Runnable {
    String filename;
    Media m;
    public MediaPlayer mp;
    public MediaView mv;

    public Tune(String filename) {
	this.filename = filename;
	m = new Media(new File(filename).toURI().toASCIIString());
	mp = new MediaPlayer(m);
	mv = new MediaView(mp);
    }

    public Tune(File file) {
	m = new Media(file.toURI().toASCIIString());
	mp = new MediaPlayer(m);
	mv = new MediaView(mp);
    }

    /*
     * @see java.lang.Runnable#run()
     */
    public void run() {
    }
}
