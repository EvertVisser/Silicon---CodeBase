import java.io.InputStream;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// GRP-COSC2635 2D
//
// SILICON - A JavaFX GAME BY:
// Clark Lavery (mentor)
// Evert Visser (s3727884)
// Duncan Baxter (s3737140)
// Kira Macarthur (s3742864)
// Dao Kun Nie (s3691571)
// Michael Power (s3162668)
// John Zealand-Doyle (s3319550)
//
// SiliconGame is the main class of the game that extends from the
// Application class. It includes the GameControl object that
// manages the flow of the game.

public class SiliconGame extends Application {
    private Stage primaryStage;
    public StackPane root;
    private Scene scene;
    private static GameControl gameControl;
    private DisplaySetting display;
    private SettingsScreen stings;

    // Display related constants and variables are initialised below;
    final static int DEFAULT_SCREEN_WIDTH = 972;
    final static int DEFAULT_SCREEN_HEIGHT = 648;

    protected StackPane mainMenu;
    protected StackPane newGame;
    protected StackPane loadGame;
    protected StackPane highScores;
    protected StackPane settings;
    protected StackPane credits;

    private LoadGame loader = new LoadGame();
    private boolean gameLoaded;
    protected Label logo = new Label("SILICON");
    private Label loadMessage = new Label();

    public void start(Stage stage) {
	logo.setId("logo-text");
	display = new DisplaySetting();
	stings = new SettingsScreen(stage);

	primaryStage = stage;
	gameControl = new GameControl(this, primaryStage);

	// Set values to determine window width and height.
	primaryStage.setWidth(DEFAULT_SCREEN_WIDTH);
	primaryStage.setHeight(DEFAULT_SCREEN_HEIGHT);
	primaryStage.setResizable(false);
	primaryStage.centerOnScreen();
	primaryStage.setTitle("Silicon");

	// Setup the title screen and set the stage
	root = createMainMenu(this);
	scene = new Scene(root);
	scene.getStylesheets().add("data/SoundStyles.css");
	primaryStage.setScene(scene);

	// The following command will remove the window decoration
	// primaryStage.initStyle(StageStyle.UNDECORATED);

	// Show stage/window and ensure primaryStage is active
	primaryStage.show();
	primaryStage.toFront();
    }

    // The following method sets up the title screen at the beginning
    // of the game
    protected StackPane createMainMenu(SiliconGame game) {
	mainMenu = new StackPane();
	// The program must find the initial background image on the file system
	try {
	    Image backGround = new Image("images/background_for_BIT.jpeg");
	    ImageView imageView = new ImageView(backGround);
	    imageView.setFitWidth(DEFAULT_SCREEN_WIDTH);
	    imageView.setFitHeight(DEFAULT_SCREEN_HEIGHT);
	    mainMenu.getChildren().add(imageView);
	} catch (Exception ex) {
	    System.out.println("SiliconGame(88): Unable to load 'images/background_for_BIT.jpeg' - check file system.");
	}
	StackPane.setAlignment(logo, Pos.TOP_CENTER);
	mainMenu.getChildren().add(logo);

	// The buttons represent user options at the beginning of the game
	Button newGame = new Button("Start New Game");
	Button loadGame = new Button("Load Game");
	Button highScores = new Button("High Scores");
	Button settings = new Button("Settings");
	Button credits = new Button("Credits");
	Button exitGame = new Button("Exit Game");
	VBox vbMain = new VBox(newGame, loadGame, highScores, settings, credits, exitGame);
	vbMain.getStyleClass().add("VBox");

	// Assign actions to each of the title screen buttons
	newGame.setOnAction(e -> {
	    new Thread(new Tone(262, 100)).start();
	    gameLoaded = false;
	    loadMessage.setText("");
	    @SuppressWarnings("unused")
	    GameBoard gameBoard = new GameBoard(game, primaryStage, gameControl, gameLoaded);
	});
	loadGame.setOnAction(e -> {
	    new Thread(new Tone(262, 100)).start();
	    loader.resetData();
	    loadMessage.setText(loader.loadData());
	    loader.createGame(game, primaryStage, gameControl);
	});
	highScores.setOnAction(e -> {
	    new Thread(new Tone(262, 100)).start();
	    root = createHighScores(game);
	    scene.setRoot(root);
	});
	settings.setOnAction(e -> {
	    new Thread(new Tone(262, 100)).start();
	    root = createSettings(game);
	    scene.setRoot(root);
	});
	credits.setOnAction(e -> {
	    new Thread(new Tone(262, 100)).start();
	    root = createCredits(game);
	    scene.setRoot(root);
	});
	exitGame.setOnAction(e -> {
	    new Thread(new Tone(262, 100)).start();
	    primaryStage.close();
	});

//	vbMain.getChildren().add(loadMessage);
	mainMenu.getChildren().add(vbMain);
	StackPane.setAlignment(vbMain, Pos.CENTER);
	mainMenu.setOnKeyPressed(e -> {
	    if (e.getCode() == KeyCode.ESCAPE || e.getCode() == KeyCode.Q) {
		exitGame.fire();
	    } else if (e.getCode() == KeyCode.S) {
		newGame.fire();
	    } else if (e.getCode() == KeyCode.L) {
		loadGame.fire();
	    } else if (e.getCode() == KeyCode.H) {
		highScores.fire();
	    }
	});
	return mainMenu;
    }

    // The following method sets up a scene to display the high
    // score table
    StackPane createHighScores(SiliconGame game) {
	highScores = new StackPane();
	highScores.getChildren().add(logo);
	StackPane.setAlignment(logo, Pos.TOP_CENTER);

	// Attempt to load the high score background
	try {
	    Image backGround = new Image("images/background_for_BIT.jpeg");
	    ImageView imageView = new ImageView(backGround);
	    imageView.setFitWidth(DEFAULT_SCREEN_WIDTH);
	    imageView.setFitHeight(DEFAULT_SCREEN_HEIGHT);
	    highScores.getChildren().add(imageView);
	} catch (Exception ex) {
	    System.out
		    .println("SiliconGame(172): Unable to load 'images/background_for_BIT.jpeg' - check file system.");
	}

	VBox vbScores = new VBox();
	vbScores.getStyleClass().add("VBox");
	String high_score_data = "default";
	try {
	    InputStream newFile = ClassLoader.getSystemResourceAsStream("data/high_scores.txt");
	    Scanner scanner = new Scanner(newFile);
	    high_score_data = scanner.nextLine();
	    scanner.close();

	    String[] h_values = high_score_data.split(",");

	    Label highScore1 = new Label(h_values[0] + " " + h_values[2]);
	    highScore1.setStyle("-fx-font: 20 Arial");
	    vbScores.getChildren().add(highScore1);
	    Label highScore2 = new Label(h_values[3] + " " + h_values[5]);
	    highScore2.setStyle("-fx-font: 20 Arial");
	    vbScores.getChildren().add(highScore2);
	    Label highScore3 = new Label(h_values[6] + " " + h_values[8]);
	    highScore3.setStyle("-fx-font: 20 Arial");
	    vbScores.getChildren().add(highScore3);
	    Label highScore4 = new Label(h_values[9] + " " + h_values[11]);
	    highScore4.setStyle("-fx-font: 20 Arial");
	    vbScores.getChildren().add(highScore4);
	    Label highScore5 = new Label(h_values[12] + " " + h_values[14]);
	    highScore5.setStyle("-fx-font: 20 Arial");
	    vbScores.getChildren().add(highScore5);
	} catch (Exception ex) {
	    System.out.println("SiliconGame(186-189): Unable to load 'data/high_scores.txt' - file error");
	}

	Button returnMainMenu = new Button("Return to Main Menu");
	vbScores.getChildren().add(returnMainMenu);
	returnMainMenu.setOnAction(e -> {
	    new Thread(new Tone(262, 100)).start();
	    root = mainMenu;
	    scene.setRoot(root);
	});

	highScores.getChildren().add(vbScores);

	highScores.setOnKeyPressed(e -> {
	    if (e.getCode() == KeyCode.ESCAPE) {
		returnMainMenu.fire();
	    }
	});
	return highScores;
    }

    StackPane createSettings(SiliconGame game) {
	settings = new StackPane();
	new SettingsScreen(primaryStage);
	Button returnMainMenu = new Button("Return to Main Menu");
	returnMainMenu.setOnAction(e -> {
	    new Thread(new Tone(262, 100)).start();
	    root = mainMenu;
	    scene.setRoot(root);
	});
	settings.getChildren().add(returnMainMenu);
	StackPane.setAlignment(returnMainMenu, Pos.BOTTOM_CENTER);

	return settings;
    }

    StackPane createCredits(SiliconGame game) {
	credits = new StackPane();

	try {
	    Image backGround = new Image("images/credits.jpg");
	    ImageView imageView = new ImageView(backGround);
	    imageView.setFitWidth(DEFAULT_SCREEN_WIDTH);
	    imageView.setFitHeight(DEFAULT_SCREEN_HEIGHT);
	    credits.getChildren().add(imageView);
	} catch (Exception ex) {
	    System.out.println("SiliconGame(247): Unable to load 'images/credits.jpg' - check file system.");
	}

	Button returnMainMenu = new Button("Return to Main Menu");
	returnMainMenu.setOnAction(e -> {
	    new Thread(new Tone(262, 100)).start();
	    root = mainMenu;
	    scene.setRoot(root);
	});
	credits.getChildren().add(returnMainMenu);
	StackPane.setAlignment(returnMainMenu, Pos.BOTTOM_CENTER);
	return credits;
    }

    DisplaySetting getDisplaySetting() {
	return display;
    }

    SettingsScreen getSettings() {
	return stings;
    }

    boolean getGameLoaded() {
	return gameLoaded;
    }

    void setGameLoaded(boolean gameLoaded) {
	this.gameLoaded = gameLoaded;
    }

    // The main method launches the program
    public static void main(String[] args) {
	Application.launch(args);
    }
}
