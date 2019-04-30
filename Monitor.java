import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.stage.Screen;
import javafx.stage.Stage;

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
    static private Stage stage; // Storage for Monitor Class's copy of Stage (used by Fullscreen methods)
    protected static double fullWidth;
    protected static double fullHeight;
    protected static double defaultWidth;
    protected static double defaultHeight;
    private double widthRatio;
    private double heightRatio;

    static private int numBackgrounds;
    private String[] fileNames = { "generic.jpeg", "0-computer.jpg", "1-socialMedia.jpg", "2-surveillance.jpg",
	    "3-deepState.jpg", "4-drone.jpg", "5-robot.jpg" };
    private Image[] images;
    static private BackgroundImage[] backgrounds;

    static private String oldHint; // Storage for previous hint about KeyCombination to leave Fullscreen mode
    static private KeyCombination oldKey; // Storage for previous KeyCombination to leave Fullscreen mode

    public Monitor(Stage primaryStage) {
	stage = primaryStage;
	fullWidth = Screen.getPrimary().getVisualBounds().getWidth();
	fullHeight = Screen.getPrimary().getVisualBounds().getHeight();

	defaultWidth = fullWidth * 0.75;
	defaultHeight = fullHeight * 0.75;

	widthRatio = fullWidth / defaultWidth;
	heightRatio = fullHeight / defaultHeight;

	initBackgrounds();
    }

    /*
     * initBackgrounds(): Populates the arrays of BackgroundImages and their base
     * Images. SiliconGame.createRoot() calls this method during startup, so we
     * return the "generic.jpeg" BackgroundImage (at index 0) that it needs.
     */
    protected void initBackgrounds() {
	numBackgrounds = new GameRules().getNumberOfLevels() + 2;
	images = new Image[numBackgrounds];
	backgrounds = new BackgroundImage[numBackgrounds];
	for (int i = 0; i < numBackgrounds; i++) {
	    try {
		images[i] = new Image(getClass().getResourceAsStream(fileNames[i]));
		backgrounds[i] = new BackgroundImage(images[i], BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
			BackgroundPosition.CENTER,
			new BackgroundSize(defaultWidth, defaultHeight, false, false, true, false));
	    } catch (Exception e) {
		System.out.println(
			"Monitor Class (lines 57-60): Unable to load '" + fileNames[i] + "' - check file system.");
	    }
	}
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
    protected static BackgroundImage getBackground(int index) {
	if ((index >= 0) && (index < numBackgrounds)) {
	    return backgrounds[index];
	} else {
	    return null;
	}
    }

    /*
     * Methods to enter and leave FullScreen mode. Save and restore the on-screen
     * hint. Save and restore the key combination (usually ESC). Disable use of the
     * ESC key to leave FullScreen mode.
     */
    protected static void enterFullScreen() {
	oldHint = stage.getFullScreenExitHint();
	stage.setFullScreenExitHint("");
	oldKey = stage.getFullScreenExitKeyCombination();
	stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
	stage.setFullScreen(true);
    }

    protected static void leaveFullScreen() {
	stage.setFullScreenExitHint(oldHint);
	stage.setFullScreenExitKeyCombination(oldKey);
	stage.setFullScreen(false);
    }

    double getWidthRatio() {
	return widthRatio;
    }

    double getHeightRatio() {
	return heightRatio;
    }
}
