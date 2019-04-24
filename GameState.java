import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

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
// The GameState class is intended to embody the current state of
// play in the card game - data is managed by the GameControl class.

public class GameState {
    private Player[] players;
    private Card[] deck;
    private int deckPointer;
    private int gameRound;
    private int playerTurn;

    private GameControl gameControl;
    @SuppressWarnings("unused")
    private GameBoard gameBoard;

    private int numberOfPlayers;
    private String string;

    public GameState(int numberOfPlayers, int deckSize) {
	// Initialise main variables for the game
	this.numberOfPlayers = numberOfPlayers;
	players = new Player[this.numberOfPlayers];
	deck = new Card[deckSize];
	deckPointer = 0;
	gameRound = 1;
	// Point the playerTurn variable to the start of the players array
	playerTurn = 0;
    }

    public String toString() {

	string = "playerTurn," + playerTurn + ",";
	string += "gameRound," + gameRound + ",";

	for (Player player : players) {
	    string += player.getName() + ",";
	    string += player.getHuman() + ",";
	    string += player.getMoney() + ",";
	    string += player.getResearch() + ",";
	    string += player.getModuleLevel() + ",";

	    ArrayList<Card> cards = player.getCards();
	    string += cards.size() + ",";
	    for (Card card : cards) {
		string += card.getName() + ",";
		Location location = card.getLocation();
		string += location.getXCoord() + ",";
		string += location.getYCoord() + ",";
		string += location.getHorizontal() + ",";
	    }
	}

	string += "GameLog,";

	ArrayList<String> gameLog = gameControl.getGameLog();

	for (String entry : gameLog) {
	    string += entry + ",";
	}

	return string;
    }

    void saveGame() {
	String output = toString();

	try {
	    File file = new File("bin/data/save_game.txt");
	    PrintWriter printWriter = new PrintWriter(file);
	    printWriter.println(output);

	    printWriter.close();

	    gameControl.newLogEntry("Game saved to file.");

	} catch (Exception ex) {
	    gameControl.newLogEntry("Save error - unable to write to file.");
	}
    }

    Player[] getPlayers() {
	return players;
    }

    Card[] getDeck() {
	return deck;
    }

    int getDeckPointer() {
	return deckPointer;
    }

    int getGameRound() {
	return gameRound;
    }

    int getPlayerTurn() {
	return playerTurn;
    }

    void addPlayer(int playerNumber, Player player) {
	try {
	    players[playerNumber] = player;
	} catch (Exception ex) {
	    System.out.println("Unable to add player to the game.");
	}
    }

    void setGameBoard(GameBoard gameBoard) {
	this.gameBoard = gameBoard;
    }

    void setGameControl(GameControl gameControl) {
	this.gameControl = gameControl;
    }

    void setDeck(Card[] deck) {
	this.deck = deck;
    }

    void movePointer() {
	deckPointer++;
    }

    void nextRound() {
	gameRound++;
    }

    void nextTurn(int playerTurn) {
	this.playerTurn = playerTurn;
    }

    void setPlayerTurn(int playerTurn) {
	this.playerTurn = playerTurn;
    }

    void setDeckPointer(int deckPointer) {
	this.deckPointer = deckPointer;
    }

    void setGameRound(int gameRound) {
	this.gameRound = gameRound;
    }

    void setPlayers(Player[] players) {
	this.players = players;
    }

}
