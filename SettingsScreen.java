
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class SettingsScreen {
    private Stage primaryStage;
    private DisplaySetting display;
    private boolean fullScreen;
    private boolean music;
    private String track;
    private GameBoard gameBoard;
    private boolean[] buttonsEnabled = new boolean[4];
    private Media media;
    private MediaPlayer mediaPlayer;

    public SettingsScreen(Stage primaryStage) {
	this.primaryStage = primaryStage;
	display = new DisplaySetting();
	gameBoard = null;
	fullScreen = true;
	music = true;
	track = "sound/Silicon_Theme_Funk.mp3";
    }

    public SettingsScreen(GameBoard gameBoard, Stage primaryStage) {
	this.gameBoard = gameBoard;
	this.primaryStage = primaryStage;
	display = new DisplaySetting();
	fullScreen = true;
	music = true;
	track = "sound/Silicon_Theme_Funk.mp3";
    }

    void showSettings(StackPane pane) {

	if (gameBoard != null) {
	    buttonsEnabled[0] = gameBoard.buyButtonEnabled();
	    buttonsEnabled[1] = gameBoard.attackButtonEnabled();
	    buttonsEnabled[2] = gameBoard.buyResearchEnabled();
	    buttonsEnabled[3] = gameBoard.saveButtonEnabled();

	    gameBoard.disableBuy();
	    gameBoard.disableAttack();
	    gameBoard.disableResearch();
	    gameBoard.disableSave();
	    gameBoard.disableSettings();
	    gameBoard.disableReturn();
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
	    if (gameBoard != null) {
		fullScreen = fullCheck.isSelected() ? false : true;
		changeScreen();
	    } else {
		fullScreen = fullCheck.isSelected() ? true : false;
	    }
	});

	musicCheck.setOnAction(e -> {
	    music = musicCheck.isSelected() ? true : false;
	    if (gameBoard != null && music) {
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

	    if (gameBoard != null) {
		if (buttonsEnabled[0])
		    gameBoard.enableBuy();
		if (buttonsEnabled[1])
		    gameBoard.enableAttack();
		if (buttonsEnabled[2])
		    gameBoard.enableResearch();
		if (buttonsEnabled[3])
		    gameBoard.enableSave();

		gameBoard.enableSettings();
		gameBoard.enableReturn();
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
	return gameBoard;
    }

    void setGameBoard(GameBoard gameBoard) {
	this.gameBoard = gameBoard;
    }

    void changeScreen() {
	if (gameBoard == null)
	    return;

	if (fullScreen) {
	    primaryStage.setFullScreen(false);
	    primaryStage.setMaxWidth(972);
	    primaryStage.setMinWidth(972);
	    primaryStage.setMaxHeight(677);
	    primaryStage.setMinHeight(677);
	    primaryStage.centerOnScreen();
	    fullScreen = false;
	} else {
	    primaryStage.setMaxWidth(display.getFullWidth());
	    primaryStage.setMaxHeight(display.getFullHeight());
	    primaryStage.setFullScreen(true);
	    fullScreen = true;
	}
    }

    void playTrack() {
	if (mediaPlayer != null) {
	    mediaPlayer.stop();
	}

	media = new Media(this.getClass().getResource("sound/Silicon_Theme_Funk.mp3").toString());
	mediaPlayer = new MediaPlayer(media);

	mediaPlayer.play();
    }

    void stopTrack() {
	if (mediaPlayer != null) {
	    mediaPlayer.stop();
	}
    }

}
