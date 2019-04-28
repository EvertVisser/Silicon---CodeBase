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

    public Monitor() {
	fullWidth = Screen.getPrimary().getVisualBounds().getWidth();
	fullHeight = Screen.getPrimary().getVisualBounds().getHeight();

	defaultWidth = fullWidth * 0.5;
	defaultHeight = fullHeight * 0.5;

	widthRatio = fullWidth / defaultWidth;
	heightRatio = fullHeight / defaultHeight;
    }

    double getWidthRatio() {
	return widthRatio;
    }

    double getHeightRatio() {
	return heightRatio;
    }
}
