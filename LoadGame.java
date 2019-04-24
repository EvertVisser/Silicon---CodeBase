import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

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
// The LoadGame class will be able
// to take text data from the file system
// and re-compose the game conditions
// to allow the resumption of play.

public class LoadGame {
    @SuppressWarnings("unused")
    private SiliconGame game;
    private String loadData;
    private GameRules gameRules;

    private int playerTurn;
    private int gameRound;
    Player[] players;
    private int deckPointer;

    public LoadGame() {
	gameRules = new GameRules();
    }

    String loadData() {
	String loadStatus = "Game Data Loaded";

	loadData = "";

	try {
	    File file = new File("data/save_game.txt");
	    Scanner scanner = new Scanner(file);
	    loadData = scanner.nextLine();

	    scanner.close();

	} catch (Exception ex) {
	    loadStatus = "File error: check folder";
	    return loadStatus;
	}

	return loadStatus;
    }

    void resetData() {
	loadData = "";
    }

    void createGame(SiliconGame game, Stage primaryStage, GameControl gameControl) {
	if (loadData.equals(""))
	    return;

	this.game = game;
	@SuppressWarnings("unused")
	boolean gameLoaded = true;
	game.setGameLoaded(true);

	int dataPointer = 0;
	String[] data = loadData.split(",");

	playerTurn = Integer.parseInt(data[1]);
	gameRound = Integer.parseInt(data[3]);
	dataPointer = 4;
	deckPointer = 0;

	players = new Player[gameRules.getNumberOfPlayers()];
	for (int i = 0; i < gameRules.getNumberOfPlayers(); i++) {
	    players[i] = new Player(gameRules.getColours(i));
	    players[i].setName(data[dataPointer]);
	    dataPointer++;
	    players[i].setHuman(Boolean.parseBoolean(data[dataPointer]));
	    dataPointer++;
	    players[i].setMoney(Integer.parseInt(data[dataPointer]));
	    dataPointer++;
	    players[i].setResearch(Integer.parseInt(data[dataPointer]));
	    dataPointer++;
	    players[i].setModuleLevel(Integer.parseInt(data[dataPointer]));
	    dataPointer++;

	    int numberOfCards = Integer.parseInt(data[dataPointer]);
	    dataPointer++;
	    deckPointer += numberOfCards;

	    String[] cardNames = new String[numberOfCards];
	    Location[] locations = new Location[numberOfCards];
	    for (int j = 0; j < numberOfCards; j++) {
		cardNames[j] = data[dataPointer];
		dataPointer++;
		Location location = new Location();
		location.setXCoord(Integer.parseInt(data[dataPointer]));
		dataPointer++;
		location.setYCoord(Integer.parseInt(data[dataPointer]));
		dataPointer++;
		location.setHorizontal(Boolean.parseBoolean(data[dataPointer]));
		dataPointer++;
		locations[j] = location;
	    }
	    players[i].setCardLoadData(cardNames);
	    players[i].setLocationLoadData(locations);
	}

	dataPointer++;
	ArrayList<String> gameLog = new ArrayList<String>();

	for (int i = dataPointer; i < data.length; i++) {
	    gameLog.add(data[i]);
	}

	gameControl.setGameLog(gameLog);

	@SuppressWarnings("unused")
	GameBoard gameBoard = new GameBoard(game, primaryStage, gameControl, this);
    }

    int getPlayerTurn() {
	return playerTurn;
    }

    int getGameRound() {
	return gameRound;
    }

    Player[] getPlayers() {
	return players;
    }

    int getDeckPointer() {
	return deckPointer;
    }

}
