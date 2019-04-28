import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.stage.Screen;

//GRP-COSC2635 2D
//
//SILICON - A JavaFX GAME BY:
//Clark Lavery (mentor)
//Evert Visser (s3727884)
//Duncan Baxter (s3737140)
//Kira Macarthur (s3742864)
//Dao Kun Nie (s3691571)
//Michael Power (s3162668)
//John Zealand-Doyle (s3319550)
//
// A class that holds variables related to the screen

public class Monitor {
    protected static double fullWidth;
    protected static double fullHeight;
    protected static double defaultWidth;
    protected static double defaultHeight;
    private double widthRatio;
    private double heightRatio;

    private int numBackgrounds;
    private String[] fileNames = { "generic.jpeg", "0-computer.jpg", "1-socialMedia.jpg", "2-surveillance.jpg",
	    "3-deepState.jpg", "4-drone.jpg", "5-robot.jpg" };
    private Image[] images;
    private BackgroundImage[] backgrounds;

    public Monitor() {
	fullWidth = Screen.getPrimary().getVisualBounds().getWidth();
	fullHeight = Screen.getPrimary().getVisualBounds().getHeight();

	defaultWidth = fullWidth * 0.75;
	defaultHeight = fullHeight * 0.75;

	widthRatio = fullWidth / defaultWidth;
	heightRatio = fullHeight / defaultHeight;
    }

    /*
     * initBackgrounds(): Populates the arrays of BackgroundImages and their base
     * Images. SiliconGame.createRoot() calls this method during startup, so we
     * return the "generic.jpeg" BackgroundImage (at index 0) that it needs.
     */
    protected BackgroundImage initBackgrounds() {
	numBackgrounds = new GameRules().getNumberOfLevels() + 2;
	images = new Image[numBackgrounds];
	backgrounds = new BackgroundImage[numBackgrounds];
	for (int i = 0; i < numBackgrounds; i++) {
	    try {
		images[i] = new Image(getClass().getResourceAsStream(fileNames[i]));
		backgrounds[i] = new BackgroundImage(images[i], BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
			BackgroundPosition.CENTER,
			new BackgroundSize(defaultWidth, defaultHeight, false, false, false, true));
	    } catch (Exception e) {
		System.out.println(
			"Monitor Class (lines 57-60): Unable to load '" + fileNames[i] + "' - check file system.");
	    }
	}
	return backgrounds[0];
    }

    /*
     * getBackground(): Returns one of the BackgroundImages in response to an index.
     * Index 0 is the "generic.jpeg" background for Root, index 1 is the initial
     * (i.e. research level = 0) background for the game board, indexes 2 to 5
     * correspond to research levels 1 to 4, and index 6 is the "victory" background
     * for research level = 5.
     * 
     * If the index falls outside these limits (i.e. < 0 or > 6), we return null to
     * signify the error.
     */
    protected BackgroundImage getBackground(int index) {
	if ((index >= 0) && (index < numBackgrounds)) {
	    return backgrounds[index];
	} else {
	    return null;
	}
    }

    double getWidthRatio() {
	return widthRatio;
    }

    double getHeightRatio() {
	return heightRatio;
    }
}
