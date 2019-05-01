import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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
// The GameBoard class represents the main game area. It is
// initiated when the user requests to play a new game. The data for
// this scene is encapsulated in order to limit complication with
// coding in the SiliconGame class

public class GameBoard {
    private SiliconGame game;
    private Settings settingsScreen;
    private GridPane gpGame;
    private Pane cardPane;
    private Group cardGuideGroup;
    private Group group;
    private Group outlineGroup;
    private BorderPane root;
    private GameControl gameControl;
    private Card[] deck;
    private ImageView[] cardViews;

    // Variable used to represent views of the deck
    private int selectedCard;
    private ImageView selectedCardView;
    private Location selectedCardLocation;
    private boolean selectToggle = false;

    private int mainDeckXCoord;
    private int mainDeckYCoord;

    private double cardXOffset = 54.0;
    private double cardYOffset = 36.0;
    private double cardVertXOffset = 18.0;
    private double cardVertYOffset = 18.0;
    private double cardLayoutMargin = 5.0;

    private Button buyCard;
    private Button attackCard;
    private Button buyResearch;
    private TextArea taLog;
    private TextArea taScores;
    private Button saveButton;
    private Button settings;
    private Button returnButton;

    private LoadGame loadGame;
    private boolean gameLoaded;

    private VBox scores;
    private Label round;
    private Label[] playerNames;
    private Label[] playerScores;

    // Keyboard commands available to use from the gameboard:
    // shift B to buy a card
    // shift A to attack another player's card
    // shift R to convert gold to research
    // shift S to save the game
    // shift F to toggle full screen/windowed mode
    // shift M to play the music track

    private final KeyCombination shiftB = new KeyCodeCombination(KeyCode.B, KeyCombination.SHIFT_DOWN);
    private final KeyCombination shiftA = new KeyCodeCombination(KeyCode.A, KeyCombination.SHIFT_DOWN);
    private final KeyCombination shiftR = new KeyCodeCombination(KeyCode.R, KeyCombination.SHIFT_DOWN);
    private final KeyCombination shiftS = new KeyCodeCombination(KeyCode.S, KeyCombination.SHIFT_DOWN);
    private final KeyCombination shiftF = new KeyCodeCombination(KeyCode.F, KeyCombination.SHIFT_DOWN);
    private final KeyCombination shiftM = new KeyCodeCombination(KeyCode.M, KeyCombination.SHIFT_DOWN);

    public GameBoard(SiliconGame game, BorderPane root, GameControl gC, boolean gameLoaded) {
	// Import references to the main stage and GameControl object
	this.game = game;
	settingsScreen = this.game.getSettings();
	settingsScreen.setGameBoard(this);
//	stage.setFullScreenExitHint("");
	this.root = root;
	gameControl = gC;
	this.gameLoaded = gameLoaded;
	gameControl.setGameBoard(this);
	gameControl.newGame();
	deck = gameControl.getGameState().getDeck();
	mainDeckXCoord = gameControl.getGameRules().getMainDeckX();
	mainDeckYCoord = gameControl.getGameRules().getMainDeckY();

	this.root.setCenter(setupBoard(this.root));
	if (settingsScreen.getFullScreen()) {
	    settingsScreen.setFullScreen(false);
	    settingsScreen.changeScreen();
	} else {
	    settingsScreen.setFullScreen(true);
	    settingsScreen.changeScreen();
	}
	if (settingsScreen.getMusic()) {
	    settingsScreen.playTrack();
	}

	placeDeck();
	gameControl.firstRound();
    }

    public GameBoard(SiliconGame game, BorderPane root, GameControl gC, LoadGame loadGame) {
	// Import references to the main stage and GameControl object
	this.game = game;
//	stage.setFullScreenExitHint("");
	settingsScreen = this.game.getSettings();
	settingsScreen.setGameBoard(this);
	this.root = root;
	gameControl = gC;
	gameLoaded = true;
	gameControl.setGameBoard(this);
	this.loadGame = loadGame;

	gameControl.loadGame();
	deck = gameControl.getGameState().getDeck();
	mainDeckXCoord = gameControl.getGameRules().getMainDeckX();
	mainDeckYCoord = gameControl.getGameRules().getMainDeckY();
	this.root.setTop(setupBoard(this.root));
	if (settingsScreen.getFullScreen()) {
	    settingsScreen.setFullScreen(false);
	    settingsScreen.changeScreen();
	} else {
	    settingsScreen.setFullScreen(true);
	    settingsScreen.changeScreen();
	}
	if (settingsScreen.getMusic()) {
	    settingsScreen.playTrack();
	}

	redrawAllCards();
	ArrayList<String> gameLog = gameControl.getGameLog();
	for (String entry : gameLog) {
	    logDisplay(entry);
	}
	gameControl.resumeGame();
    }

    private StackPane setupBoard(BorderPane root) {

	StackPane pane = new StackPane();

	pane.setBackground(new Background(Monitor.getBackground(1)));

	HBox hBox = new HBox(0);
	hBox.setAlignment(Pos.CENTER);
	hBox.setMaxSize(972, 648);
	hBox.setMinSize(972, 648);

	// Create a TextArea for the current scores
	VBox.setVgrow(createScoreDisplay(), Priority.ALWAYS);
	SiliconGame.vbRight.getChildren().add(0, taScores);

	// Create a TextArea to display a log of the game's events
	taLog = new TextArea();
	taLog.setWrapText(false);
	taLog.setEditable(false);
	VBox.setVgrow(taLog, Priority.ALWAYS);

	// Create buttons for "Buy a card", "Buy research", "Attack a
	// card" and "Save game".
	buyCard = new Button("", new ImageView(new Image(getClass().getResourceAsStream("money.png"))));
	buyCard.setId("button-round");
	buyCard.setTooltip(new Tooltip("Press this button to buy a card for mega-dollars"));
	attackCard = new Button("", new ImageView(new Image(getClass().getResourceAsStream("ddos.png"))));
	attackCard.setId("button-round");
	attackCard.setTooltip(new Tooltip("Press this button to attack another player's card"));
	buyResearch = new Button("", new ImageView(new Image(getClass().getResourceAsStream("research.png"))));
	buyResearch.setId("button-round");
	buyResearch.setTooltip(new Tooltip("Press this button to put some mega-dollars into research"));
	saveButton = new Button("",
		new ImageView(new Image(getClass().getResourceAsStream("save-icon-silhouette.png"))));
	saveButton.setId("button-round");
	saveButton.setTooltip(new Tooltip("Press this button to save the game"));

	// Create a Label for each Button
	Label lBuy = new Label("Buy a Card");
	lBuy.setId("button-label");
	Label lAttack = new Label("Attack a Card");
	lAttack.setId("button-label");
	Label lResearch = new Label("Buy Research");
	lResearch.setId("button-label");
	Label lSave = new Label("Save Game");
	lSave.setId("button-label");

	// Specify the actions for each Game button (lambda definitions)
	// *Buy a card*
	buyCard.setOnAction(e -> {
	    new Thread(new Tone(262, 100)).start();

	    if (gameControl.getGameOver()) {
		gameControl.newLogEntry("Game Over.");
	    } else if (!gameControl.canBuy()) {
		gameControl.newLogEntry("Not enough money.");
	    } else {
		gameControl.newLogEntry("Select a card.");
		manageMouseClick();
	    }
	});

	// *Attack a card*
	attackCard.setOnAction(e -> {
	    new Thread(new Tone(262, 100)).start();

	    gameControl.newLogEntry("Select opponent card to attack.");
	    // Turn off other buttons
	    buyCard.setDisable(true);
	    attackCard.setDisable(true);
	    buyResearch.setDisable(true);
	    gameControl.getAttack().chooseAttack();
	});
	attackCard.setDisable(true);

	// *Buy research*
	buyResearch.setOnAction(e -> {
	    new Thread(new Tone(262, 100)).start();
	    gameControl.newLogEntry("Research button pressed.");

	    if (gameControl.getGameOver()) {
		gameControl.newLogEntry("Game Over.");
	    } else if (!gameControl.canBuyResearch()) {
		gameControl.newLogEntry("Not enough money.");
	    } else {
		// Add research produced, subtract money
		PlayerMove move = new PlayerMove("Research");
		gameControl.updateGameState(move);
	    }
	});

	// *Save the Game*
	saveButton.setOnAction(e -> {
	    new Thread(new Tone(262, 100)).start();
	    gameControl.getGameState().saveGame();
	});

	// Create a GridPane and add the 4 x Game buttons (with their Labels) to it
	// Add the GridPane to vbLeft
	gpGame = new GridPane();
	gpGame.setId("grid-pane-nav-buttons");
	gpGame.addColumn(0, buyCard, attackCard, buyResearch, saveButton);
	gpGame.addColumn(1, lBuy, lAttack, lResearch, lSave);
	SiliconGame.vbLeft.getChildren().addAll(taLog, gpGame);

	// The cardPane will represent the playing area
	cardPane = new Pane();
	cardPane.setMaxSize(648, 648);
	cardPane.setMinSize(648, 648);

	cardGuideGroup = new Group();
	cardGuideGroup.setManaged(false);
	cardPane.getChildren().add(cardGuideGroup);
	cardGuideGroup.setVisible(false);

	// Add a layer for player card outlines
	outlineGroup = new Group();
	outlineGroup.setManaged(false);
	cardPane.getChildren().add(outlineGroup);

	// This layer will hold the ImageView of each card
	group = new Group();
	group.setManaged(false);
	cardPane.getChildren().add(group);

	Group labelGroup = new Group();
	labelGroup.setManaged(false);

	// Place names at the starting area for each player
	Label[] playerNames = new Label[gameControl.getGameRules().getNumberOfPlayers()];
	Player[] players = gameControl.getPlayers();
	for (int i = 0; i < gameControl.getGameRules().getNumberOfPlayers(); i++) {
	    playerNames[i] = new Label();
	    playerNames[i].setText(players[i].getName());
	    Location location = gameControl.getGameRules().getStartLocation(i);
	    playerNames[i].setLayoutX(location.getXCoord() + 18.0);
	    playerNames[i].setLayoutY(location.getYCoord() + 28.0);
	    playerNames[i].setBackground(
		    new Background(new BackgroundFill(players[i].getColour(), CornerRadii.EMPTY, Insets.EMPTY)));
	    labelGroup.getChildren().add(playerNames[i]);
	}
	playerNames[1].setTranslateX(25.0);
	playerNames[1].setRotate(90.0);
	playerNames[3].setTranslateX(-5.0);
	playerNames[3].setRotate(90.0);
	cardPane.getChildren().add(labelGroup);

	hBox.getChildren().add(cardPane);

	// The rightPanel will hold the score display and some
	// other buttons
	pane.getChildren().add(hBox);

	root.setCenter(pane);

	// Add keyboard shortcuts for each button and an option to
	// toggle full screen mode
	pane.setOnKeyPressed(e -> {
	    if (e.getCode() == KeyCode.ESCAPE) {
		returnButton.fire();
	    } else if (shiftS.match(e)) {
		saveButton.fire();
	    } else if (shiftB.match(e)) {
		buyCard.fire();
	    } else if (shiftA.match(e)) {
		attackCard.fire();
	    } else if (shiftR.match(e)) {
		buyResearch.fire();
	    } else if (shiftF.match(e)) {
		settingsScreen.changeScreen();
	    } else if (shiftM.match(e)) {
		// link to settingsScreen class
		if (settingsScreen.getMusic()) {
		    settingsScreen.setMusic(false);
		    settingsScreen.stopTrack();
		} else {
		    settingsScreen.setMusic(true);
		    settingsScreen.playTrack();
		}
	    }
	});
	pane.requestLayout();
	return pane;
    }

    /*
     * logDisplay: adds an event to the log TextArea. Importantly, the game
     * maintains a separate ArrayList of events in GameControl.gameLog. Accordingly,
     * GameControl.newLogEntry (which calls both gameLog.add and logDisplay) should
     * be used in preference to accessing logDisplay directly.
     */
    void logDisplay(String logEntry) {
	taLog.appendText(logEntry + "\n");
    }

    /*
     * createScoreDisplay: Create a TextArea to display each player's mega-bucks,
     * accumulated research, and current research level.
     */
    private TextArea createScoreDisplay() {
	taScores = new TextArea();
	taScores.setWrapText(false);
	taScores.setEditable(false);
	Player[] players = gameControl.getPlayers();
	taScores.setText("  ***   CURRENT SCORES   ***\n");
	taScores.appendText("Round: " + gameControl.getGameState().getGameRound() + "\n");
	taScores.appendText(String.format("%-10s%10s%10s%10s\n", "Player", "MB", "Research", "Level"));
	taScores.appendText(String.format("%-10s%10s%10s%10s\n", "------", "--", "--------", "-----"));
	for (int i = 0; i < gameControl.getGameRules().getNumberOfPlayers(); i++) {
	    taScores.appendText(String.format("%-10s", players[i].getName()));
	    taScores.appendText(players[i].getScore());
	}
	return taScores;
    }

    /*
     * displayScores: Use the Scores TextArea to display each player's current
     * mega-bucks, accumulated research, and research level.
     */
    void displayScores() {
	Player[] players = gameControl.getPlayers();
	taScores.setText("  ***   CURRENT SCORES   ***\n");
	taScores.appendText("Round: " + gameControl.getGameState().getGameRound() + "\n");
	taScores.appendText(String.format("%-10s%10s%10s%10s\n", "Player", "MB", "Research", "Level"));
	taScores.appendText(String.format("%-10s%10s%10s%10s\n", "------", "--", "--------", "-----"));
	for (int i = 0; i < gameControl.getGameRules().getNumberOfPlayers(); i++) {
	    taScores.appendText(String.format("%-10s", players[i].getName()));
	    taScores.appendText(players[i].getScore());
	}
    }

    void placeDeck() {
	cardViews = new ImageView[deck.length];
	Location[] cardLocations = new Location[deck.length];
	for (int i = 0; i < deck.length; i++) {
	    cardLocations[i] = deck[i].getLocation();
	    cardLocations[i].setXCoord(mainDeckXCoord);
	    cardLocations[i].setYCoord(mainDeckYCoord);
	    cardViews[i] = deck[i].getView();
	    if (!cardLocations[i].getHorizontal())
		cardViews[i].setRotate(90);
	    cardViews[i].setX(cardLocations[i].getXCoord());
	    cardViews[i].setY(cardLocations[i].getYCoord());
	    // Add each card to the cardGroup
	    group.getChildren().add(cardViews[i]);
	}
    }

    void placeCard(Location location) {
	selectedCard = gameControl.getGameState().getDeckPointer();
	deck[selectedCard].setView(new ImageView(deck[selectedCard].getImage()));
	ImageView cardView = cardViews[selectedCard];
	plotCard(cardView, location);
    }

    void plotCard(ImageView cardView, Location location) {
	group.getChildren().remove(cardView);
	cardView = deck[selectedCard].getView();
	if (!location.getHorizontal())
	    cardView.setRotate(270);
	cardView.setX(location.getXCoord());
	cardView.setY(location.getYCoord());
	deck[selectedCard].setView(cardView);
	group.getChildren().add(cardView);
    }

    void redrawAllCards() {
	cardViews = new ImageView[deck.length];

	for (int i = 0; i < deck.length; i++) {
	    cardViews[i] = deck[i].getView();
	    Location location = deck[i].getLocation();
	    if (!location.getHorizontal())
		cardViews[i].setRotate(270);
	    cardViews[i].setX(location.getXCoord());
	    cardViews[i].setY(location.getYCoord());
	    group.getChildren().add(cardViews[i]);
	}
	if (gameControl.getGameState().getDeckPointer() < gameControl.getGameRules().getNumberOfCards()) {
	    selectedCard = gameControl.getGameState().getDeckPointer();
	    selectedCardView = deck[selectedCard].getView();
	    selectedCardLocation = deck[selectedCard].getLocation();
	}
    }

    // This method manages the assigning of a new Event Handler to a card view
    void manageMouseClick() {

	selectedCard = gameControl.getGameState().getDeckPointer();

	// If we have not reached the end of the deck then make a
	// new Event Handler for the next card.
	if (selectedCard < deck.length) {
	    selectedCardLocation = deck[selectedCard].getLocation();
	    selectedCardView = cardViews[selectedCard];

	    // Ensure new selected card is on the top
	    group.getChildren().remove(selectedCardView);
	    group.getChildren().add(selectedCardView);

	    // Disable other buttons while the player moves
	    // the card
	    buyCard.setDisable(true);
	    attackCard.setDisable(true);
	    buyResearch.setDisable(true);
	    removeEventHandlers();

	    selectedCardView.setOnMouseClicked(e -> {
		if (e.getButton().equals(MouseButton.PRIMARY)) {
		    handleLeftClick();
		    if (!selectToggle) {
			// Return to handling the buy card option
			removeEventHandlers();
			PlayerMove move = new PlayerMove("Buy Card", deck[selectedCard], 10);
			gameControl.updateGameState(move);
		    }

		}

		// Ensure right mouse button only working if the new
		// card has already been clicked on
		if (selectToggle && e.getButton().equals(MouseButton.SECONDARY)) {
		    handleRightClick();
		}
	    });

	} else {
	    // Should not reach this area
	}
    }

    // Method for handling the left click of a card
    void handleLeftClick() {
	if (selectToggle) {

	    // check to see if the card can be placed on the
	    // location indicated by the cursor
	    if (!canPlaceCard())
		return;

	    // Card was deselected
	    cardGuideGroup.setVisible(false);
	    cardPane.setOnMouseMoved(null);

	    try {

		group.getChildren().remove(selectedCardView);
		// Add a new ImageView representing the flip side of the card
		// showing the name of the card.
		selectedCardLocation.setXCoord((int) selectedCardView.getX());
		selectedCardLocation.setYCoord((int) selectedCardView.getY());

		deck[selectedCard].setView(new ImageView(deck[selectedCard].getImage()));
		selectedCardView = deck[selectedCard].getView();
		if (!selectedCardLocation.getHorizontal())
		    selectedCardView.setRotate(270);
		selectedCardView.setX(selectedCardLocation.getXCoord());
		selectedCardView.setY(selectedCardLocation.getYCoord());
		deck[selectedCard].setView(selectedCardView);

		// After determining the location and orientation, the ImageView
		// can be added to the cardGroup
		group.getChildren().add(selectedCardView);

	    } catch (Exception ex) {
		System.out.println("GameBoard Class (line 518): Unable to load card image from file - check folder.");
	    }

	    selectToggle = false;
	} else {
	    // Card was selected
	    placeCardGuide();
	    cardGuideGroup.setVisible(true);

	    group.getChildren().remove(selectedCardView);
	    group.getChildren().add(selectedCardView);
	    // Now create another EventHandler that detects mouse
	    // movement - card coordinates are plotted to follow the
	    // mouse cursor
	    cardPane.setOnMouseMoved(event -> {
		// Setting limits for card movement to the table.
		double xVal = event.getX() - cardXOffset;
		double yVal = event.getY() - cardYOffset;
		if (selectedCardLocation.getHorizontal()) {
		    if (xVal <= 0.0)
			xVal = 0.0;
		    if (xVal >= 540.0)
			xVal = 540.0;
		    selectedCardView.setX(xVal);
		    if (yVal <= 0.0)
			yVal = 0.0;
		    if (yVal >= 576.0)
			yVal = 576.0;
		    selectedCardView.setY(yVal);
		} else {
		    if (xVal <= 0.0 - cardVertXOffset)
			xVal = 0.0 - cardVertXOffset;
		    if (xVal >= 576.0 - cardVertXOffset)
			xVal = 576.0 - cardVertXOffset;
		    selectedCardView.setX(xVal);
		    if (yVal <= 0.0 + cardVertYOffset)
			yVal = 0.0 + cardVertYOffset;
		    if (yVal >= 540.0 + cardVertYOffset)
			yVal = 540.0 + cardVertYOffset;
		    selectedCardView.setY(yVal);
		}
	    });
	    selectToggle = true;
	}
    }

    boolean canPlaceCard() {
	ArrayList<Location> locations = gameControl.checkPlayerLocations();

	selectedCardLocation.setXCoord((int) selectedCardView.getX());
	selectedCardLocation.setYCoord((int) selectedCardView.getY());
	boolean horizontal = selectedCardView.getRotate() == 0.0 ? true : false;
	selectedCardLocation.setHorizontal(horizontal);

	Rectangle2D cardRect = getRect(selectedCardLocation);

	for (Location location : locations) {
	    double playerCardWidth = location.getHorizontal() ? 108.0 : 72.0;
	    double playerCardHeight = location.getHorizontal() ? 72.0 : 108.0;
	    double playerXCoord = location.getXCoord();
	    double playerYCoord = location.getYCoord();
	    if (!location.getHorizontal()) {
		playerXCoord += 18.0;
		playerYCoord -= 18.0;
	    }

	    Rectangle2D cardZone = new Rectangle2D(playerXCoord - cardLayoutMargin, playerYCoord - cardLayoutMargin,
		    playerCardWidth + cardLayoutMargin * 2.0, playerCardHeight + cardLayoutMargin * 2.0);

	    if (cardRect.intersects(cardZone)) {
		for (Location otherLocation : locations) {
		    Rectangle2D otherCard = getRect(otherLocation);

		    if (cardRect.intersects(otherCard))
			return false;
		}

		// Also check that the card is not over the center deck
		Rectangle2D center = getRect(new Location(mainDeckXCoord, mainDeckYCoord, true));
		if (cardRect.intersects(center))
		    return false;

		return true;
	    }
	}

	return false;
    }

    boolean canPlaceCard(Location newLocation) {
	ArrayList<Location> locations = gameControl.checkPlayerLocations();

	Rectangle2D cardRect = getRect(newLocation);

	for (Location location : locations) {
	    double playerCardWidth = location.getHorizontal() ? 108.0 : 72.0;
	    double playerCardHeight = location.getHorizontal() ? 72.0 : 108.0;
	    double playerXCoord = location.getXCoord();
	    double playerYCoord = location.getYCoord();
	    if (!location.getHorizontal()) {
		playerXCoord += 18.0;
		playerYCoord -= 18.0;
	    }

	    Rectangle2D cardZone = new Rectangle2D(playerXCoord - cardLayoutMargin, playerYCoord - cardLayoutMargin,
		    playerCardWidth + cardLayoutMargin * 2.0, playerCardHeight + cardLayoutMargin * 2.0);

	    if (cardRect.intersects(cardZone)) {
		for (Location otherLocation : locations) {
		    Rectangle2D otherCard = getRect(otherLocation);
		    if (cardRect.intersects(otherCard)) {
			return false;
		    }
		}

		// Also check that the card is not over the center deck
		Rectangle2D center = getRect(new Location(mainDeckXCoord, mainDeckYCoord, true));
		if (cardRect.intersects(center))
		    return false;

		return true;
	    }
	}
	return false;
    }

    boolean isWithinBoard(Location location) {
	boolean withinBoard = true;

	if (location.getHorizontal()) {
	    if (location.getXCoord() < 0 || location.getYCoord() < 0 || location.getXCoord() + 144 > 684
		    || location.getYCoord() + 108 > 684) {
		withinBoard = false;
	    }
	} else {
	    if (location.getXCoord() < -18 || location.getYCoord() < 18 || location.getXCoord() + 126 > 684
		    || location.getYCoord() + 126 > 684) {
		withinBoard = false;
	    }
	}

	return withinBoard;
    }

    void handleRightClick() {
	if (selectedCardLocation.getHorizontal()) {
	    selectedCardLocation.setHorizontal(false);
	    selectedCardView.setRotate(90);
	} else {
	    selectedCardLocation.setHorizontal(true);
	    selectedCardView.setRotate(0);
	}
    }

    void placeCardGuide() {
	cardGuideGroup.getChildren().clear();

	ArrayList<Location> locations = gameControl.checkPlayerLocations();

	for (Location location : locations) {
	    double width = location.getHorizontal() ? 108.0 : 72.0;
	    double height = location.getHorizontal() ? 72.0 : 108.0;
	    double xCoord = location.getXCoord();
	    double yCoord = location.getYCoord();
	    if (!location.getHorizontal()) {
		xCoord += 18.0;
		yCoord -= 18.0;
	    }

	    // Shave if border of the rectangle is outside of
	    // the board area
	    Rectangle rectangle;
	    if (xCoord <= 0.0) {
		rectangle = new Rectangle(0.0, yCoord - cardLayoutMargin, width + cardLayoutMargin,
			height + cardLayoutMargin * 2.0);
	    } else if (xCoord + width >= 648.0) {
		rectangle = new Rectangle(xCoord - cardLayoutMargin, yCoord - cardLayoutMargin,
			width + cardLayoutMargin, height + cardLayoutMargin * 2.0);
	    } else if (yCoord <= 0.0) {
		rectangle = new Rectangle(xCoord - cardLayoutMargin, 0.0, width + cardLayoutMargin * 2.0,
			height + cardLayoutMargin);
	    } else if (yCoord + height >= 648.0) {
		rectangle = new Rectangle(xCoord - cardLayoutMargin, yCoord - cardLayoutMargin,
			width + cardLayoutMargin * 2.0, height + cardLayoutMargin);
	    } else {
		rectangle = new Rectangle(xCoord - cardLayoutMargin, yCoord - cardLayoutMargin,
			width + cardLayoutMargin * 2.0, height + cardLayoutMargin * 2.0);
	    }

	    rectangle.setFill(Color.AZURE);

	    cardGuideGroup.getChildren().add(rectangle);
	}
    }

    boolean buyButtonEnabled() {
	if (buyCard.isDisabled()) {
	    return false;
	} else {
	    return true;
	}
    }

    boolean attackButtonEnabled() {
	if (attackCard.isDisabled()) {
	    return false;
	} else {
	    return true;
	}
    }

    boolean buyResearchEnabled() {
	if (buyResearch.isDisabled()) {
	    return false;
	} else {
	    return true;
	}
    }

    boolean saveButtonEnabled() {
	if (saveButton.isDisabled()) {
	    return false;
	} else {
	    return true;
	}
    }

    void enableBuy() {
	buyCard.setDisable(false);
    }

    void disableBuy() {
	buyCard.setDisable(true);
    }

    void enableAttack() {
	attackCard.setDisable(false);
    }

    void disableAttack() {
	attackCard.setDisable(true);
    }

    void enableResearch() {
	buyResearch.setDisable(false);
    }

    void disableResearch() {
	buyResearch.setDisable(true);
    }

    void enableSave() {
	saveButton.setDisable(false);
    }

    void disableSave() {
	saveButton.setDisable(true);
    }

    void enableSettings() {
	settings.setDisable(false);
    }

    void disableSettings() {
	settings.setDisable(true);
    }

    void enableReturn() {
	returnButton.setDisable(false);
    }

    void disableReturn() {
	returnButton.setDisable(true);
    }

    Rectangle2D getRect(Location location) {
	double width = location.getHorizontal() ? 108.0 : 72.0;
	double height = location.getHorizontal() ? 72.0 : 108.0;
	double xCoord = location.getXCoord();
	double yCoord = location.getYCoord();
	if (!location.getHorizontal()) {
	    xCoord += 18.0;
	    yCoord -= 18.0;
	}

	Rectangle2D rectangle = new Rectangle2D(xCoord, yCoord, width, height);

	return rectangle;
    }

    void removeEventHandlers() {
	for (int i = 0; i < deck.length; i++) {
	    ImageView view = deck[i].getView();
	    view.setOnMouseClicked(null);
	}
    }

    void cardOutlines(Player player) {
	// Add player's colours to the cards on the board
	Color playerColour = player.getColour();

	ArrayList<Location> locations = gameControl.checkPlayerLocations(player);

	for (Location location : locations) {
	    double width = location.getHorizontal() ? 108.0 : 72.0;
	    double height = location.getHorizontal() ? 72.0 : 108.0;
	    double xCoord = location.getXCoord();
	    double yCoord = location.getYCoord();
	    if (!location.getHorizontal()) {
		xCoord += 18.0;
		yCoord -= 18.0;
	    }

	    Rectangle rectangle = new Rectangle(xCoord, yCoord, width, height);

	    rectangle.setStroke(playerColour);
	    rectangle.setStrokeWidth(2);
	    rectangle.setFill(Color.TRANSPARENT);
	    outlineGroup.getChildren().add(rectangle);
	}
    }

    void gameOver() {
	displayScores();
	gameControl.newLogEntry("Game won in " + gameControl.getGameState().getGameRound() + " rounds.");
	buyCard.setDisable(true);
	attackCard.setDisable(true);
	buyResearch.setDisable(true);
	saveButton.setDisable(true);
    }

    LoadGame getLoadGame() {
	return loadGame;
    }
}
