import java.util.ArrayList;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.stage.Stage;

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
 * The GameControl class acts as a bridge between the data of the card
 * game and the various display functions. It manages the flow of
 * activity during the game.
 */

public class GameControl {
    @SuppressWarnings("unused")
    private SiliconGame game;
    @SuppressWarnings("unused")
    private Stage stage;
    private GameState gameState;
    private GameRules gameRules;
    GameBoard gameBoard = null;
    private Player[] players;
    private int playerTurn;
    private ArrayList<Card> playerCards;
    private AttackRoll attack;
    private boolean gameOver;
    private LoadGame loadGame;

    private ArrayList<String> gameLog;

    public GameControl(SiliconGame game, Stage primaryStage) {
	this.game = game;
	stage = primaryStage;
	gameRules = new GameRules();
	gameOver = false;
	gameLog = new ArrayList<String>();
	attack = null;
    }

    void newGame() {
	gameState = new GameState(gameRules.getNumberOfPlayers(), gameRules.getNumberOfCards());
	gameState.setGameControl(this);
	gameState.setGameBoard(gameBoard);

	initialisePlayers();
	playerTurn = 0;
	// Set up a deck of cards to be used in the gameState for
	// testing purposes.
	Card[] deck = gameState.getDeck();
	deck = initialiseDeck(deck);
	shuffleDeck(deck);
	gameState.setDeck(deck);
    }

    void loadGame() {
	gameState = new GameState(gameRules.getNumberOfPlayers(), gameRules.getNumberOfCards());
	gameState.setGameControl(this);
	gameState.setGameBoard(gameBoard);

	// New values for deckPointer gameRound playerTurn
	loadGame = gameBoard.getLoadGame();
	players = loadGame.getPlayers();
	upgradeAbilities();
	gameState.setPlayers(players);
	playerTurn = loadGame.getPlayerTurn();
	gameState.setPlayerTurn(playerTurn);
	gameState.setDeckPointer(loadGame.getDeckPointer());
	gameState.setGameRound(loadGame.getGameRound());

	Card[] deck = gameState.getDeck();
	deck = initialiseDeck(deck);
	deck = placePlayerCards(deck);
	gameState.setDeck(deck);
    }

    Card[] placePlayerCards(Card[] deck) {
	Card[] newOrder = new Card[gameRules.getNumberOfCards()];
	int newOrderNumber = 0;

	for (Player player : players) {
	    String[] cardNames = player.getCardLoadData();
	    Location[] locations = player.getLocationLoadData();
	    for (int i = 0; i < cardNames.length; i++) {
		for (Card card : deck) {
		    if (cardNames[i].equals(card.getName())) {
			card.setLocation(locations[i]);
			player.addCard(card);
			card.setView(new ImageView(card.getImage()));
			newOrder[newOrderNumber] = card;
			newOrderNumber++;
		    }
		}
	    }
	}

	Card[] remainder = new Card[gameRules.getNumberOfCards() - newOrderNumber];
	int remainderCount = 0;
	for (Card card : deck) {
	    if (card.getOwner() == null) {
		remainder[remainderCount] = card;
		Location location = new Location(gameRules.getMainDeckX(), gameRules.getMainDeckY(),
			gameRules.getHorizontal());
		card.setLocation(location);

		remainderCount++;
	    }
	}

	shuffleDeck(remainder);
	for (Card card : remainder) {
	    newOrder[newOrderNumber] = card;
	    newOrderNumber++;
	}

	return newOrder;
    }

    void resumeGame() {
	gameBoard.displayScores();
	for (Player player : players) {
	    gameBoard.cardOutlines(player);
	}
	attack = new AttackRoll(gameBoard, this, players[playerTurn]);
	attack.canAttack();
	if ((gameState.getDeckPointer() >= gameRules.getNumberOfCards() - 1) || (players[playerTurn].getMoney() < 10)) {
	    gameBoard.disableBuy();
	} else {
	    gameBoard.enableBuy();
	}
	if (canBuyResearch()) {
	    gameBoard.enableResearch();
	} else {
	    gameBoard.disableResearch();
	}
    }

    void firstRound() {

	Card[] deck = gameState.getDeck();
	for (int i = 0; i < gameRules.getNumberOfPlayers(); i++) {
	    Card newCard = deck[gameState.getDeckPointer()];
	    players[i].addCard(newCard);
	    gameBoard.placeCard(gameRules.getStartLocation(i));
	    newCard.setLocation(gameRules.getStartLocation(i));
	    gameState.movePointer();
	}

	newLogEntry("  ***  Silicon - game commencing  ***");
	gameBoard.displayScores();
	newLogEntry("Player 1, please choose an option ...");
	// No need to test for the attack button option at the start of the game
	return;
    }

    void updateGameState(PlayerMove playerMove) {
	Player player = players[playerTurn];

	if (playerMove.getMoveType().equals("Buy Card")) {
	    player.setMoney(player.getMoney() - playerMove.getCost());
	    player.addCard(playerMove.getCard());
	    newLogEntry(players[playerTurn].getName() + " has purchased the " + playerMove.getCard().getName() + " for "
		    + playerMove.getCost() + " mega-bucks.");
	    gameState.movePointer();

	} else if (playerMove.getMoveType().equals("Attack Card")) {

	} else {
	    // Add research equivalent to current research potential
	    // and remove equivalent amount of money
	    int cost = player.getNewResearchCost();
	    player.setMoney(player.getMoney() - cost);
	    player.addResearch(cost);

	    newLogEntry(player.getName() + " converted " + cost + " mega-bucks into research.");
	    // Need to check for victory here
	    if (player.getResearch() >= (player.getModuleLevel() + 1) * 100) {
		newLogEntry(player.getName() + " has upgraded to a new level.");
		int newLevel = player.getResearch() / 100;
		player.setModuleLevel(newLevel);
		upgradeAbilities(player);

		if (player.getModuleLevel() == 5) {
		    gameBoard.pane.setBackground(new Background(Monitor.getBackground(6)));
		    newLogEntry(player.getName() + " has achieved victory.");
		    gameOver = true;
		    gameBoard.gameOver();
		    return;
		}
	    }
	}

	if (playerTurn == players.length - 1) {
	    updateRound();
	} else {
	    gameBoard.cardOutlines(players[playerTurn]);
	    nextPlayer();
	    giveEarnings();
	    gameBoard.displayScores();

	    // Check if the current player is able to make a move
	    // must have more than 10 mega-bucks to buy a card
	    // must have a card adjacent to attack
	    // must have enough money to convert mega-bucks to research

	    if ((players[playerTurn].getMoney() < 10) && (!attack.canAttack())
		    && (players[playerTurn].getMoney() < players[playerTurn].getNewResearchCost())) {
		newLogEntry(players[playerTurn].getName() + " cannot make a" + " move - action moves to next player.");
		nextPlayer();
		giveEarnings();
		gameBoard.displayScores();
	    }

	    if (players[playerTurn].getHuman()) {
		newLogEntry(players[playerTurn].getName() + " choose an option.");
		attack = new AttackRoll(gameBoard, this, players[playerTurn]);
		attack.canAttack();
		if ((gameState.getDeckPointer() >= gameRules.getNumberOfCards() - 1)
			|| (players[playerTurn].getMoney() < 10)) {
		    gameBoard.disableBuy();
		} else {
		    gameBoard.enableBuy();
		}
		if (canBuyResearch()) {
		    gameBoard.enableResearch();
		} else {
		    gameBoard.disableResearch();
		}
	    } else {
		newLogEntry(players[playerTurn].getName() + " to move.");
		updateGameState(computerTurn());
	    }
	}
    }

    void updateRound() {
	gameBoard.cardOutlines(players[playerTurn]);
	addResearch();
	int[] index = rankPlayers();
	gameBoard.pane.setBackground(new Background(Monitor.getBackground(players[index[0]].getModuleLevel() + 1)));
	if (gameOver)
	    return;
	gameState.nextRound();
	nextPlayer();
	giveEarnings();
	gameBoard.displayScores();

	// Check if the current player is able to make a move
	// must have more than 10 mega-bucks to buy a card
	// must have a card adjacent to attack
	// must have enough money to convert mega-bucks to research

	if ((players[playerTurn].getMoney() < 10) && (!attack.canAttack())
		&& (players[playerTurn].getMoney() < players[playerTurn].getNewResearchCost())) {
	    newLogEntry(players[playerTurn].getName() + " cannot make a" + " move - action moves to next player.");
	    nextPlayer();
	    giveEarnings();
	    gameBoard.displayScores();
	}

	newLogEntry(players[playerTurn].getName() + " choose an option.");
	attack = new AttackRoll(gameBoard, this, players[playerTurn]);
	attack.canAttack();
	if ((gameState.getDeckPointer() >= gameRules.getNumberOfCards() - 1) || (players[playerTurn].getMoney() < 10)) {
	    gameBoard.disableBuy();
	} else {
	    gameBoard.enableBuy();
	}
	if (canBuyResearch()) {
	    gameBoard.enableResearch();
	} else {
	    gameBoard.disableResearch();
	}
    }

    void addResearch() {
	// Add research value gained during the round
	for (int i = 0; i < gameRules.getNumberOfPlayers(); i++) {
	    playerCards = players[i].getCards();
	    int newResearch = 0;
	    for (Card card : playerCards) {
		newResearch += card.getResearch();
	    }

	    players[i].addResearch(newResearch);

	    if (players[i].getResearch() >= (players[i].getModuleLevel() + 1) * 100) {
		newLogEntry(players[i].getName() + " has upgraded to a new level.");
		int newLevel = players[i].getResearch() / 100;
		players[i].setModuleLevel(newLevel);
		upgradeAbilities(players[i]);

		if (players[i].getModuleLevel() == 5) {
		    newLogEntry(players[i].getName() + " has achieved victory.");
		    gameBoard.pane.setBackground(new Background(Monitor.getBackground(6)));
		    gameOver = true;
		    gameBoard.gameOver();
		    return;
		}
	    }
	}
    }

    /*
     * rankPlayers(): Simple binary sort to rank all four players based on (1) their
     * research level, (2) accumulated research, and then (3) accumulated money. The
     * method returns an array of indexes into the Player.players[] array, ordered
     * from highest-ranked (index[0]) to lowest (index[3]).
     * 
     * Obviously, if we decide to increase the number of players beyond 4, this
     * method will need modest expansion to accommodate the extras. Ultimately, we
     * could simply overload "compare" with the maxPlayers() method and use the
     * system sort instead.
     * 
     * GameBoard.displayScores uses rankPlayers() to sort the players in the Current
     * Scores table. GameControl.updateRound also uses it to identify the highest
     * research level any player has achieved (which must equal the level of the
     * highest-ranked player), which in turn determines the background image.
     */
    public int[] rankPlayers() {
	int temp;
	int[] index = { 0, 1, 2, 3 };

	// Rank the first pair of players
	if (maxPlayers(0, 1) == 1) {
	    index[0] = 1;
	    index[1] = 0;
	}
	// Rank the second pair of players
	if (maxPlayers(2, 3) == 3) {
	    index[2] = 3;
	    index[3] = 2;
	}
	// Identify the top-ranked player ("best of the best")
	if (maxPlayers(index[0], index[2]) == index[2]) {
	    temp = index[0];
	    index[0] = index[2];
	    index[2] = temp;
	}
	// Identify the bottom-ranked player ("worst of the worst")
	if (maxPlayers(index[1], index[3]) == index[3]) {
	    temp = index[1];
	    index[1] = index[3];
	    index[3] = temp;
	}
	// Rank the middle pair of players ("best of the rest") and we're done
	if (maxPlayers(index[1], index[2]) == index[2]) {
	    temp = index[1];
	    index[1] = index[2];
	    index[2] = temp;
	}
	return index;
    }

    /*
     * maxPlayers(): As noted above, this method ranks two players based on (1)
     * their research level, (2) accumulated research, and then (3) accumulated
     * money. It takes, as parameters, the index for each player in the
     * Player.players[] array and returns the index for the higher-ranked of the two
     * players.
     */
    private int maxPlayers(int p1, int p2) {
	// Does one player have a higher research level than the other?
	int diff = players[p2].getModuleLevel() - players[p1].getModuleLevel();
	if (diff > 0) {
	    return p2;
	} else if (diff < 0) {
	    return p1;
	}

	// No: Does one player have more accumulated research than the other?
	diff = players[p2].getResearch() - players[p1].getResearch();
	if (diff > 0) {
	    return p2;
	} else if (diff < 0) {
	    return p1;
	}

	// No: Does one player have more accumulated mega-bucks than the other?
	// If there's still a tie, return the index for the first of the two players
	diff = players[p2].getMoney() - players[p1].getMoney();
	if (diff > 0) {
	    return p2;
	} else {
	    return p1;
	}
    }

    /*
     * NOTE TO DAO: Looks like this is another piece of the existing AI. It has no
     * bias in favour of any one of the possible moves, which definitely provides
     * scope for improvement. Moreover, it places its new cards completely randomly,
     * both as regards to orientation (horizontal/vertical) and location. It should
     * not be too difficult to, instead, have each AI maintain a "preferred target",
     * and have it place its cards in the direction of the target.
     */
    PlayerMove computerTurn() {
	PlayerMove compMove = null;
	boolean[] choices = new boolean[3];

	choices[0] = canBuy();

	attack = new AttackRoll(gameBoard, this, players[playerTurn]);

	choices[1] = attack.canAttack();

	choices[2] = canBuyResearch();

	int moveChoice = 3;
	while (moveChoice == 3) {
	    moveChoice = (int) (Math.random() * 3);
	    if (!choices[moveChoice]) {
		moveChoice = 3;
	    }
	}

	if (moveChoice == 0) {
	    boolean canPlace = false;
	    Card card = gameState.getDeck()[gameState.getDeckPointer()];

	    ArrayList<Location> locations = checkPlayerLocations();

	    Location location = new Location(0, 0, true);
	    while (!canPlace) {
		int randomCardIndex = (int) (Math.random() * locations.size());
		Location lastLocation = locations.get(randomCardIndex);

		location.setXCoord(lastLocation.getXCoord());
		location.setYCoord(lastLocation.getYCoord());

		int orientation = (int) (Math.random() * 2);
		if (orientation == 0) {
		    location.setHorizontal(true);
		} else if (orientation == 1) {
		    location.setHorizontal(false);
		}

		int direction = (int) (Math.random() * 4) + 1;
		if (direction == 1) {
		    if (location.getHorizontal() && lastLocation.getHorizontal()) {
			location.setXCoord(location.getXCoord() - 108);
		    } else if (location.getHorizontal() && !lastLocation.getHorizontal()) {
			location.setXCoord(location.getXCoord() - 90);
			location.setYCoord(location.getYCoord() - 18);
		    } else if (!location.getHorizontal() && lastLocation.getHorizontal()) {
			location.setXCoord(location.getXCoord() - 90);
			location.setYCoord(location.getYCoord() - 18);
		    } else if (!location.getHorizontal() && !lastLocation.getHorizontal()) {
			location.setXCoord(location.getXCoord() - 72);
		    }
		} else if (direction == 2) {
		    if (location.getHorizontal() && lastLocation.getHorizontal()) {
			location.setYCoord(location.getYCoord() - 72);
		    } else if (location.getHorizontal() && !lastLocation.getHorizontal()) {
			location.setYCoord(location.getYCoord() - 90);
		    } else if (!location.getHorizontal() && lastLocation.getHorizontal()) {
			location.setYCoord(location.getYCoord() - 90);
		    } else if (!location.getHorizontal() && !lastLocation.getHorizontal()) {
			location.setYCoord(location.getYCoord() - 108);
		    }
		} else if (direction == 3) {
		    if (location.getHorizontal() && lastLocation.getHorizontal()) {
			location.setXCoord(location.getXCoord() + 108);
		    } else if (location.getHorizontal() && !lastLocation.getHorizontal()) {
			location.setXCoord(location.getXCoord() + 90);
			location.setYCoord(location.getYCoord() - 18);
		    } else if (!location.getHorizontal() && lastLocation.getHorizontal()) {
			location.setXCoord(location.getXCoord() + 90);
			location.setYCoord(location.getYCoord() - 18);
		    } else if (!location.getHorizontal() && !lastLocation.getHorizontal()) {
			location.setXCoord(location.getXCoord() + 72);
		    }
		} else if (direction == 4) {
		    if (location.getHorizontal() && lastLocation.getHorizontal()) {
			location.setYCoord(location.getYCoord() + 72);
		    } else if (location.getHorizontal() && !lastLocation.getHorizontal()) {
			location.setYCoord(location.getYCoord() + 90);
		    } else if (!location.getHorizontal() && lastLocation.getHorizontal()) {
			location.setYCoord(location.getYCoord() + 90);
		    } else if (!location.getHorizontal() && !lastLocation.getHorizontal()) {
			location.setYCoord(location.getYCoord() + 108);
		    }
		}

		canPlace = gameBoard.canPlaceCard(location);

		if (canPlace) {
		    canPlace = gameBoard.isWithinBoard(location);
		    if (!canPlace) {
			location.setXCoord(lastLocation.getXCoord());
			location.setYCoord(lastLocation.getYCoord());
			location.setHorizontal(lastLocation.getHorizontal());
		    }
		}
	    }

	    gameBoard.placeCard(location);
	    card.setLocation(location);

	    compMove = new PlayerMove("Buy Card", card, 10);
	} else if (moveChoice == 1) {
	    newLogEntry(players[playerTurn].getName() + " attacks!");

	    attack.randomCompAttack();

	    compMove = new PlayerMove("Attack Card");

	} else if (moveChoice == 2) {
	    compMove = new PlayerMove("Research");
	}

	return compMove;
    }

    GameState getGameState() {
	return gameState;
    }

    void setGameBoard(GameBoard gameBoard) {
	this.gameBoard = gameBoard;
    }

    // This method is used to initialise the players for the game.
    // Configured for testing purposes.
    void initialisePlayers() {
	for (int i = 0; i < gameRules.getNumberOfPlayers(); i++) {
	    Player player = new Player(getGameRules().getColours(i));
	    if (i == 0) {
		player.setName("Player" + (i + 1));
		player.setMoney(gameRules.getStartingMoney());
		player.setHuman(true);
	    } else {
		player.setName("RandomAI" + i);
		player.setMoney(gameRules.getStartingMoney());
		player.setHuman(false);
	    }
	    gameState.addPlayer(i, player);
	}
	players = gameState.getPlayers();
    }

    // This method is used to initialise the cards for the game.
    // Configured for testing purposes. Assumes a deck of 54 cards
    // while in testing phase.
    Card[] initialiseDeck(Card[] deck) {
	if (deck.length != 54) {
	    System.out.println("Cannot set up the deck - wrong size.");
	    return deck;
	}
	// Set up names for each card
	String[] suits = new String[] { " of diamonds", " of clubs", " of spades", " of hearts" };

	deck[0] = new Card("Ace" + suits[0], 14, 0, "14Diamonds.png", false, false);
	deck[1] = new Card("Two" + suits[0], 2, 0, "2Diamonds.png", false, false);
	deck[2] = new Card("Three" + suits[0], 3, 0, "3Diamonds.png", false, false);
	deck[3] = new Card("Four" + suits[0], 4, 0, "4Diamonds.png", false, false);
	deck[4] = new Card("Five" + suits[0], 5, 0, "5Diamonds.png", false, false);
	deck[5] = new Card("Six" + suits[0], 6, 0, "6Diamonds.png", false, false);
	deck[6] = new Card("Seven" + suits[0], 7, 0, "7Diamonds.png", false, false);
	deck[7] = new Card("Eight" + suits[0], 8, 0, "8Diamonds.png", false, false);
	deck[8] = new Card("Nine" + suits[0], 9, 0, "9Diamonds.png", false, false);
	deck[9] = new Card("Ten" + suits[0], 10, 0, "10Diamonds.png", false, false);
	deck[10] = new Card("Jack" + suits[0], 11, 0, "11Diamonds.png", false, false);
	deck[11] = new Card("Queen" + suits[0], 12, 0, "12Diamonds.png", false, false);
	deck[12] = new Card("King" + suits[0], 13, 0, "13Diamonds.png", false, false);

	deck[13] = new Card("Ace" + suits[1], 14, 0, "14Clubs.png", true, false);
	deck[14] = new Card("Two" + suits[1], 2, 0, "2Clubs.png", true, false);
	deck[15] = new Card("Three" + suits[1], 3, 0, "3Clubs.png", true, false);
	deck[16] = new Card("Four" + suits[1], 4, 0, "4Clubs.png", true, false);
	deck[17] = new Card("Five" + suits[1], 5, 0, "5Clubs.png", true, false);
	deck[18] = new Card("Six" + suits[1], 6, 0, "6Clubs.png", true, false);
	deck[19] = new Card("Seven" + suits[1], 7, 0, "7Clubs.png", true, false);
	deck[20] = new Card("Eight" + suits[1], 8, 0, "8Clubs.png", true, false);
	deck[21] = new Card("Nine" + suits[1], 9, 0, "9Clubs.png", true, false);
	deck[22] = new Card("Ten" + suits[1], 10, 0, "10Clubs.png", true, false);
	deck[23] = new Card("Jack" + suits[1], 11, 0, "11Clubs.png", true, false);
	deck[24] = new Card("Queen" + suits[1], 12, 0, "12Clubs.png", true, false);
	deck[25] = new Card("King" + suits[1], 13, 0, "13Clubs.png", true, false);

	deck[26] = new Card("Ace" + suits[2], 0, 14, "14Spades.png", false, true);
	deck[27] = new Card("Two" + suits[2], 0, 2, "2Spades.png", false, true);
	deck[28] = new Card("Three" + suits[2], 0, 3, "3Spades.png", false, true);
	deck[29] = new Card("Four" + suits[2], 0, 4, "4Spades.png", false, true);
	deck[30] = new Card("Five" + suits[2], 0, 5, "5Spades.png", false, true);
	deck[31] = new Card("Six" + suits[2], 0, 6, "6Spades.png", false, true);
	deck[32] = new Card("Seven" + suits[2], 0, 7, "7Spades.png", false, true);
	deck[33] = new Card("Eight" + suits[2], 0, 8, "8Spades.png", false, true);
	deck[34] = new Card("Nine" + suits[2], 0, 9, "9Spades.png", false, true);
	deck[35] = new Card("Ten" + suits[2], 0, 10, "10Spades.png", false, true);
	deck[36] = new Card("Jack" + suits[2], 0, 11, "11Spades.png", false, true);
	deck[37] = new Card("Queen" + suits[2], 0, 12, "12Spades.png", false, true);
	deck[38] = new Card("King" + suits[2], 0, 13, "13Spades.png", false, true);

	deck[39] = new Card("Ace" + suits[3], 0, 14, "14Hearts.png", false, false);
	deck[40] = new Card("Two" + suits[3], 0, 2, "2Hearts.png", false, false);
	deck[41] = new Card("Three" + suits[3], 0, 3, "3Hearts.png", false, false);
	deck[42] = new Card("Four" + suits[3], 0, 4, "4Hearts.png", false, false);
	deck[43] = new Card("Five" + suits[3], 0, 5, "5Hearts.png", false, false);
	deck[44] = new Card("Six" + suits[3], 0, 6, "6Hearts.png", false, false);
	deck[45] = new Card("Seven" + suits[3], 0, 7, "7Hearts.png", false, false);
	deck[46] = new Card("Eight" + suits[3], 0, 8, "8Hearts.png", false, false);
	deck[47] = new Card("Nine" + suits[3], 0, 9, "9Hearts.png", false, false);
	deck[48] = new Card("Ten" + suits[3], 0, 10, "10Hearts.png", false, false);
	deck[49] = new Card("Jack" + suits[3], 0, 11, "11Hearts.png", false, false);
	deck[50] = new Card("Queen" + suits[3], 0, 12, "12Hearts.png", false, false);
	deck[51] = new Card("King" + suits[3], 0, 13, "13Hearts.png", false, false);

	deck[52] = new Card("Joker1", 2, 2, "1Joker.png", false, false);
	deck[53] = new Card("Joker2", 2, 2, "2Joker.png", false, false);

	return deck;
    }

    // Shuffles the deck of Cards
    Card[] shuffleDeck(Card[] deck) {
	Card tempCard;
	for (int i = 0; i < deck.length; i++) {
	    tempCard = deck[i];
	    int randomCard = (int) (Math.random() * deck.length);
	    deck[i] = deck[randomCard];
	    deck[randomCard] = tempCard;
	}
	return deck;
    }

    // The following method prints out the card names
    void printCardNames(Card[] deck) {
	for (Card card : deck) {
	    System.out.println(card.getName());
	}
    }

    void printScores() {
	// Output current scores
	System.out.println("Scores after round " + gameState.getGameRound() + " are as follows:");
	for (int i = 0; i < gameRules.getNumberOfPlayers(); i++) {
	    System.out.println(players[i].getName() + " mega-bucks: " + players[i].getMoney() + " research: "
		    + players[i].getResearch());
	}
    }

    void giveEarnings() {
	Player player = players[playerTurn];
	// Add mega-bucks earned from previous period based
	// on cards owned.
	playerCards = player.getCards();
	int earnings = 0;
	for (Card card : playerCards) {
	    earnings += card.getRevenue();
	    if (card.getSocialMedia() && player.hasMindSlaves()) {
		earnings += card.getRevenue();
	    }
	    if (card.getRobotic() && player.hasDroneMercenaries()) {
		earnings += card.getResearch();
	    }
	}
	player.setMoney(player.getMoney() + earnings);

	// Deduct legal fees based on any Joker cards owned
	// unless player has the deep state credentials
	// ability
	if (!player.hasCredentials()) {
	    for (Card card : playerCards) {
		if (card.getName().equals("Joker1") || card.getName().equals("Joker2")) {
		    earnings -= 10;
		}
	    }

	    if (earnings < 0)
		earnings = 0;
	}

	newLogEntry(player.getName() + " earned " + earnings + " mega-bucks from their portfolio last round.");
    }

    boolean canBuy() {
	return (players[playerTurn].getMoney() >= 10) && (gameState.getDeckPointer() < gameRules.getNumberOfCards());
    }

    boolean canBuyResearch() {
	return players[playerTurn].getMoney() >= players[playerTurn].getNewResearchCost();
    }

    boolean getGameOver() {
	return gameOver;
    }

    GameRules getGameRules() {
	return gameRules;
    }

    Player[] getPlayers() {
	return players;
    }

    int getPlayerTurn() {
	return playerTurn;
    }

    AttackRoll getAttack() {
	return attack;
    }

    ArrayList<String> getGameLog() {
	return gameLog;
    }

    void nextPlayer() {
	playerTurn++;
	if (playerTurn == gameRules.getNumberOfPlayers()) {
	    playerTurn = 0;
	}
	gameState.nextTurn(playerTurn);
    }

    void newLogEntry(String entry) {
	gameLog.add(entry);
	gameBoard.logDisplay(entry);
    }

    void setGameLog(ArrayList<String> gameLog) {
	this.gameLog = gameLog;
    }

    ArrayList<Location> checkPlayerLocations() {
	ArrayList<Location> locations = new ArrayList<Location>();
	ArrayList<Card> cards = players[playerTurn].getCards();
	for (Card card : cards) {
	    locations.add(card.getLocation());
	}

	return locations;
    }

    ArrayList<Location> checkPlayerLocations(Player player) {
	ArrayList<Location> locations = new ArrayList<Location>();
	ArrayList<Card> cards = player.getCards();
	for (Card card : cards) {
	    locations.add(card.getLocation());
	}

	return locations;
    }

    ArrayList<Location> checkOtherLocations() {
	ArrayList<Location> locations = new ArrayList<Location>();
	for (int i = 0; i < gameRules.getNumberOfPlayers(); i++) {
	    if (!(playerTurn == i)) {
		ArrayList<Card> cards = players[i].getCards();
		for (Card card : cards) {
		    locations.add(card.getLocation());
		}
	    }
	}

	return locations;
    }

    void upgradeAbilities() {
	for (Player player : players) {
	    int level = player.getModuleLevel();

	    if (level > 0) {
		player.setMindSlaves(true);
	    }
	    if (level > 1) {
		player.setSurveillance(true);
	    }
	    if (level > 2) {
		player.setCredentials(true);
	    }
	    if (level > 3) {
		player.setDroneMercenaries(true);
	    }
	}
    }

    void upgradeAbilities(Player player) {
	int level = player.getModuleLevel();

	if (level > 0 && !player.hasMindSlaves()) {
	    player.setMindSlaves(true);
	    newLogEntry(player.getName() + " now has the social media" + " mind slaves ability.");
	}
	if (level > 1 && !player.hasSurveillance()) {
	    player.setSurveillance(true);
	    newLogEntry(player.getName() + " now has the all seeing" + " eye surveillance ability.");
	}
	if (level > 2 && !player.hasCredentials()) {
	    player.setCredentials(true);
	    newLogEntry(player.getName() + " now has the deep state" + " credentials ability.");
	}
	if (level > 3 && !player.hasDroneMercenaries()) {
	    player.setDroneMercenaries(true);
	    newLogEntry(player.getName() + " now has the self-aware" + " drone mercenaries ability.");
	}
    }
}
