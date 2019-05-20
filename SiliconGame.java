import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
 * SiliconGame is the main class of the game that extends from the
 * Application class. It includes the GameControl object that
 * manages the flow of the game.
 */

public class SiliconGame extends Application {
    private Stage stage;
    private Scene scene;
    public BorderPane root;
    protected VBox vbLeft;
    protected VBox vbRight;
    private Button return2main;
    private Button return2game;
    protected GameControl gameControl;
    protected GameBoard gameBoard;
    private Monitor monitor;
    private Settings settings;

    // Display related constants and variables are initialised below;
    protected VBox showMainMenu = null;
    protected StackPane startGame = null;
    protected StackPane loadGame = null;
    protected VBox showSettings = null;
    protected VBox showHighScores = null;
    protected ImageView showCredits = null;
    protected TextArea showHelp = null;

    private LoadGame loader = new LoadGame();
    private boolean gameLoaded;
    private Label loadMessage = new Label();

    /*
     * JavaFX programs have a JavaFX application thread (labelled "start") that
     * commences automatically. This is equivalent to the "main" method in a normal
     * Java program. The loader passes a handle to the Stage window, to the JavaFX
     * application, as a parameter to "start". Note that, once a JavaFX component
     * has been displayed on the screen, you can only change it in the JavaFX
     * application thread.
     */
    public void start(Stage primaryStage) {
	// Get the dimensions of the user's primary screen and use 75% of its width and
	// height to set the default dimensions of the Scene
	monitor = new Monitor(primaryStage);
	settings = new Settings(primaryStage, showSettings);

	// Initialise the Stage
	stage = primaryStage;
	stage.setTitle("Silicon");
	stage.initStyle(StageStyle.DECORATED);
	stage.setAlwaysOnTop(true);
	stage.setResizable(false);
	stage.centerOnScreen();

	// Create the Scene and Root, then import the JavaFX CSS Stylesheet into Scene.
	// Root provides a standard backdrop for every screen, with a background, logo
	// and a set of navigation buttons.
	root = createRoot(this);
	scene = new Scene(root, Monitor.defaultWidth, Monitor.defaultHeight, Color.TRANSPARENT);
	stage.setScene(scene);
	scene.getStylesheets().add("SiliconStyles.css");

	// Create the main menu screen
	showMainMenu = createMainMenu(this);

	// Show the Stage and ensure it is active
	root.requestLayout();
	this.stage.show();
    }

    /*
     * Create the Root node (a BorderPane). The Root holds the background, main logo
     * and sub-logo, 4 x navigation buttons (for Settings, High Scores, Credits and
     * Help), and a button to return to the Main Menu screen. The Center section is
     * free for individual screens to use. We switch between screens by swapping the
     * contents of this section in and out.
     */
    public BorderPane createRoot(SiliconGame game) {
	root = new BorderPane();
	root.setId("bp-root");

	// Set the background image for Root to the generic (non-era) background
	root.setBackground(new Background(Monitor.getBackground(0)));

	// Create the logo and sub-logo and add them both to Root. The JavaFX CSS file
	// contains the text settings that make these logos work.
	Text mainLogo = new Text("SILICON");
	mainLogo.setId("main-logo");
	Text subLogo = new Text("ERROR 404: TEAM NAME NOT FOUND");
	subLogo.setId("sub-logo");
	VBox vbLogo = new VBox(mainLogo, subLogo);
	vbLogo.setId("VBox-invis");
	BorderPane.setAlignment(vbLogo, Pos.TOP_CENTER);

	// Create the "Return to Main Menu" button
	return2main = new Button("Return to Main Menu");
	return2main.setTooltip(new Tooltip("Press this button to return to the Main Menu"));
	return2main.setVisible(false);

	return2main.setOnAction(e -> {
	    new Thread(new Tone(262, 100)).start();
	    root.setCenter(showMainMenu);
	    return2main.setVisible(false);
	    vbLeft.setVisible(false);
	    // If vbRight has more than one child then there must be a Current Scores table
	    if (vbRight.getChildren().size() > 1) {
		gameBoard.tfScores.setVisible(false);
		gameBoard.taScores.setVisible(false);
		return2game.setVisible(true);
	    }
	});

	// Create the "Return to Game" button
	return2game = new Button("Return to Game");
	return2game.setTooltip(new Tooltip("Press this button to return to the Game Board"));
	return2game.setVisible(false);

	return2game.setOnAction(e -> {
	    new Thread(new Tone(262, 100)).start();
	    root.setCenter(gameBoard.pane);
	    return2main.setVisible(true);
	    return2game.setVisible(false);
	    vbLeft.setVisible(true);
	    // If vbRight has more than one child then there must be a Current Scores table
	    if (vbRight.getChildren().size() > 1) {
		gameBoard.tfScores.setVisible(true);
		gameBoard.taScores.setVisible(true);
	    }
	});

	GridPane gpReturn = new GridPane();
	gpReturn.setPrefWidth(Monitor.fullWidth);
//	gpReturn.setGridLinesVisible(true);
	gpReturn.setId("grid-pane-return-buttons");
	gpReturn.add(return2game, 0, 0);
	VBox vbNull = new VBox();
	GridPane.setHgrow(vbNull, Priority.ALWAYS);
	gpReturn.add(vbNull, 1, 0);
	gpReturn.add(return2main, 2, 0);
	GridPane.setHalignment(return2main, HPos.RIGHT);
	GridPane.setHalignment(return2game, HPos.LEFT);
	BorderPane.setAlignment(gpReturn, Pos.CENTER);

	// Create a Label for each navigation button
	Label lSettings = new Label("Show Settings");
	lSettings.setId("button-label-right");
	Label lScores = new Label("High Scores");
	lScores.setId("button-label-right");
	Label lCredits = new Label("View Credits");
	lCredits.setId("button-label-right");
	Label lHelp = new Label("Help");
	lHelp.setId("button-label-right");

	// Create navigation buttons for Settings, High Scores, Credits and Help
	Button bSettings = new Button("",
		new ImageView(new Image(getClass().getResourceAsStream("settings-work-tool.png"))));
	bSettings.setId("button-round");
	bSettings.setTooltip(new Tooltip("Press this button to adjust settings"));
	Button bScores = new Button("", new ImageView(new Image(getClass().getResourceAsStream("trophy_3.png"))));
	bScores.setId("button-round");
	bScores.setTooltip(new Tooltip("Press this button to see the High Score table"));
	Button bCredits = new Button("", new ImageView(new Image(getClass().getResourceAsStream("film-roll_1.png"))));
	bCredits.setId("button-round");
	bCredits.setTooltip(new Tooltip("Press this button to see the game credits"));
	Button bHelp = new Button("", new ImageView(new Image(getClass().getResourceAsStream("question_3.png"))));
	bHelp.setId("button-round");
	bHelp.setTooltip(new Tooltip("Press this button to view the Help file"));

	// Create a GridPane for root.Right and add the 4 x navigation buttons (with
	// their Labels) to it
	GridPane gpNavButtons = new GridPane();
	// gpNavButtons.setGridLinesVisible(true);
	gpNavButtons.setId("grid-pane-nav-buttons");
	gpNavButtons.addColumn(0, lSettings, lScores, lCredits, lHelp);
	gpNavButtons.addColumn(1, bSettings, bScores, bCredits, bHelp);

	// Create a keyboard shortcut for each navigation button
	root.setOnKeyPressed(e -> {
	    if (e.getCode() == KeyCode.MULTIPLY) { // Settings = number pad "*"
		bSettings.fire();
	    } else if (e.getCode() == KeyCode.NUMPAD9) { // High Scores = number pad "9"
		bScores.fire();
	    } else if (e.getCode() == KeyCode.NUMPAD6) { // Credits = number pad "6"
		bCredits.fire();
	    } else if (e.getCode() == KeyCode.NUMPAD3) { // Help = number pad "3"
		bHelp.fire();
	    }
	});

	// Specify the actions for each navigation button (lambda definitions)
	// *Settings*
	bSettings.setOnAction(e -> {
	    new Thread(new Tone(262, 100)).start();
	    if (showSettings == null) {
		showSettings = settings.createSettingsScreen(stage, root);
	    }
	    BorderPane.setAlignment(showSettings, Pos.CENTER);
	    root.setCenter(showSettings);
	    return2main.setVisible(true);
	    if (vbRight.getChildren().size() > 1) {
		return2game.setVisible(true);
	    }
	});

	// *High Scores*
	bScores.setOnAction(e -> {
	    new Thread(new Tone(262, 100)).start();
	    if (showHighScores == null) {
		showHighScores = HighScores.createHighScoresScreen();
	    }
	    BorderPane.setAlignment(showHighScores, Pos.CENTER);
	    root.setCenter(showHighScores);
	    return2main.setVisible(true);
	    if (vbRight.getChildren().size() > 1) {
		return2game.setVisible(true);
	    }
	});

	// *Credits*
	bCredits.setOnAction(e -> {
	    new Thread(new Tone(262, 100)).start();
	    if (showCredits == null) {
		showCredits = Credits.createCreditsScreen();
	    }
	    BorderPane.setAlignment(showCredits, Pos.CENTER);
	    root.setCenter(showCredits);
	    return2main.setVisible(true);
	    if (vbRight.getChildren().size() > 1) {
		return2game.setVisible(true);
	    }
	});

	// *Help*
	bHelp.setOnAction(e -> {
	    new Thread(new Tone(262, 100)).start();
	    if (showHelp == null) {
		showHelp = Help.createHelpScreen(root);
	    }
	    BorderPane.setAlignment(showHelp, Pos.CENTER);
	    root.setCenter(showHelp);
	    return2main.setVisible(true);
	    if (vbRight.getChildren().size() > 1) {
		return2game.setVisible(true);
	    }
	});

	// Add everything to root (NB: Center and the VBoxes are free for other screens
	// to use)
	vbLeft = new VBox();
	vbLeft.setId("VBox-left");
	BorderPane.setAlignment(vbLeft, Pos.TOP_LEFT);
	vbRight = new VBox(gpNavButtons);
	vbRight.setId("VBox-right");
	BorderPane.setAlignment(vbRight, Pos.TOP_RIGHT);
	root.setTop(vbLogo);
	root.setLeft(vbLeft);
	root.setRight(vbRight);
	root.setBottom(gpReturn);
	return root;
    }

    // If I was following OOP strictly, there'd be a separate class for the root and
    // MainMenu. However, they are very closely connected, so I've stayed with a
    // single class to contain both. If this creates any issues with other screens,
    // then please let me know (of course).
    protected VBox createMainMenu(SiliconGame game) {
	// Create main menu buttons for Start New Game, Load Game and Exit.
	// Add the buttons to a visible VBox
	Button newGame = new Button("Start New Game");
	Button loadGame = new Button("Load Game");
	Button exitGame = new Button("Exit Game");
	showMainMenu = new VBox(newGame, loadGame, exitGame);
	showMainMenu.getStyleClass().add("VBox");

	// Define the actions for each main menu button
	// *Start New Game*
	newGame.setOnAction(e -> {
	    new Thread(new Tone(262, 100)).start();
	    return2main.setVisible(true);
	    return2game.setVisible(false);
	    // If there was a previous game then clear the left and right sections of root
	    if (!vbLeft.getChildren().isEmpty()) {
		vbLeft.getChildren().clear();
		vbRight.getChildren().remove(0);
		vbRight.getChildren().remove(0);
	    }
	    gameControl = new GameControl(this, stage);
	    gameLoaded = false;
	    loadMessage.setText("");
	    gameBoard = new GameBoard(game, root, gameControl, gameLoaded);
	});

	// *Load Game*
	loadGame.setOnAction(e -> {
	    new Thread(new Tone(262, 100)).start();
	    return2main.setVisible(true);
	    return2game.setVisible(false);
	    // If there was a previous game then clear the left and right sections of root
	    if (!vbLeft.getChildren().isEmpty()) {
		vbLeft.getChildren().clear();
		vbRight.getChildren().remove(0);
		vbRight.getChildren().remove(0);
	    }
	    gameControl = new GameControl(this, stage);
	    loader.resetData();
	    loadMessage.setText(loader.loadData());
	    gameBoard = loader.createGame(game, root, gameControl);
	});

	// *Exit*
	exitGame.setOnAction(e -> {
	    new Thread(new Tone(262, 100)).start();
	    Platform.exit();
	});

	// Create a keyboard shortcut for each main menu button
	showMainMenu.setOnKeyPressed(e -> {
	    if (e.getCode() == KeyCode.ESCAPE || e.getCode() == KeyCode.Q) { // Exit = <ESC> or Q
		exitGame.fire();
	    } else if (e.getCode() == KeyCode.S) { // Start New Game = S
		newGame.fire();
	    } else if (e.getCode() == KeyCode.L) { // Load Game = L
		loadGame.fire();
	    }
	});

	// Add the main menu buttons to Root (Center section)
	BorderPane.setAlignment(showMainMenu, Pos.CENTER);
	root.setCenter(showMainMenu);
	return showMainMenu;
    }

    Monitor getDisplaySetting() {
	return monitor;
    }

    Settings getSettings() {
	return settings;
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
