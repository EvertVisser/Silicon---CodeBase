
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
// The SettingsScreen class encapsulates data
// for the game settings along with a screen
// that allows the user to review and adjust
// the settings.

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class Settings {
    public static GridPane settingsScreen;
    private Stage stage;
    private boolean fullScreen;
    private boolean music;
    private String track;
    private GameBoard board;
    private boolean[] buttonsEnabled = new boolean[4];
    private Media media;
    private MediaPlayer mediaPlayer;

    public Settings(Stage primaryStage, GridPane settings) {
	stage = primaryStage;
	board = null;
	fullScreen = true;
	music = true;
	track = "Silicon_Theme_Funk.mp3";
    }

    public Settings(GameBoard gameBoard, Stage primaryStage) {
	board = gameBoard;
	stage = primaryStage;
	fullScreen = true;
	music = true;
	track = "Silicon_Theme_Funk.mp3";
    }

    // Create the Settings Screen
    public static GridPane createSettingsScreen(Stage primaryStage, BorderPane root) {
	settingsScreen = new GridPane();
	return settingsScreen;
    }

    void showSettings(StackPane pane) {

	if (board != null) {
	    buttonsEnabled[0] = board.buyButtonEnabled();
	    buttonsEnabled[1] = board.attackButtonEnabled();
	    buttonsEnabled[2] = board.buyResearchEnabled();
	    buttonsEnabled[3] = board.saveButtonEnabled();

	    board.disableBuy();
	    board.disableAttack();
	    board.disableResearch();
	    board.disableSave();
	    board.disableSettings();
	    board.disableReturn();
	}

	VBox vBox = new VBox(20);
	vBox.setAlignment(Pos.CENTER);
	vBox.setTranslateY(50.0);
	vBox.setMinSize(420, 480);
	vBox.setMaxSize(420, 480);

	// Example of settings to include - not yet functional
	HBox fullScreenBox = new HBox(150);
	fullScreenBox.setAlignment(Pos.CENTER_RIGHT);
	Label fullScreenLabel = new Label("Full Screen");
	fullScreenBox.getChildren().add(fullScreenLabel);
	CheckBox fullCheck = new CheckBox();
	fullCheck.setSelected(fullScreen);
	fullScreenBox.getChildren().add(fullCheck);
	vBox.getChildren().add(fullScreenBox);

	// Example of settings to include - not yet functional
	HBox musicBox = new HBox(150);
	musicBox.setAlignment(Pos.CENTER_RIGHT);
	Label musicLabel = new Label("Music");
	musicBox.getChildren().add(musicLabel);
	CheckBox musicCheck = new CheckBox();
	musicCheck.setSelected(music);
	musicBox.getChildren().add(musicCheck);
	vBox.getChildren().add(musicBox);

	// Add event handlers to change settings if boxes are changed
	fullCheck.setOnAction(e -> {
	    if (board != null) {
		fullScreen = fullCheck.isSelected() ? false : true;
		changeScreen();
	    } else {
		fullScreen = fullCheck.isSelected() ? true : false;
	    }
	});

	musicCheck.setOnAction(e -> {
	    music = musicCheck.isSelected() ? true : false;
	    if (board != null && music) {
		playTrack();
	    }

	    if (!music && (mediaPlayer != null)) {
		mediaPlayer.stop();
	    }
	});

	// Add a button that will allow us to return to the main screen
	Button returnButton = new Button("Return");
	returnButton.setOnAction(e -> {
	    new Thread(new Tone(262, 100)).start();

	    vBox.setVisible(false);
	    returnButton.setDisable(true);

	    if (board != null) {
		if (buttonsEnabled[0])
		    board.enableBuy();
		if (buttonsEnabled[1])
		    board.enableAttack();
		if (buttonsEnabled[2])
		    board.enableResearch();
		if (buttonsEnabled[3])
		    board.enableSave();

		board.enableSettings();
		board.enableReturn();
	    }
	});
	vBox.getChildren().add(returnButton);
	StackPane.setAlignment(vBox, Pos.BOTTOM_CENTER);
	pane.getChildren().add(vBox);
    }

    boolean getFullScreen() {
	return fullScreen;
    }

    boolean getMusic() {
	return music;
    }

    String getTrack() {
	return track;
    }

    void setFullScreen(boolean fullScreen) {
	this.fullScreen = fullScreen;
    }

    void setMusic(boolean music) {
	this.music = music;
    }

    void setTrack(String track) {
	this.track = track;
    }

    GameBoard getGameBoard() {
	return board;
    }

    void setGameBoard(GameBoard gameBoard) {
	this.board = gameBoard;
    }

    void changeScreen() {
	if (board == null)
	    return;

	if (fullScreen) {
	    stage.setFullScreen(false);
	    stage.setMaxWidth(972);
	    stage.setMinWidth(972);
	    stage.setMaxHeight(677);
	    stage.setMinHeight(677);
	    stage.centerOnScreen();
	    fullScreen = false;
	} else {
	    stage.setMaxWidth(Monitor.fullWidth);
	    stage.setMaxHeight(Monitor.fullHeight);
	    stage.setFullScreen(true);
	    fullScreen = true;
	}
    }

    void playTrack() {
	if (mediaPlayer != null) {
	    mediaPlayer.stop();
	}

	try {
	    media = new Media(this.getClass().getResource(this.track).toString());
	} catch (Exception e) {
	    System.out.println("Settings Class (line 216) - Could not load '" + track + "' - check file system.");
	}
	mediaPlayer = new MediaPlayer(media);
	mediaPlayer.play();
    }

    void stopTrack() {
	if (mediaPlayer != null) {
	    mediaPlayer.stop();
	}
    }

}
