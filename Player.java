import java.util.ArrayList;

import javafx.scene.paint.Color;

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
// The player class is used to represent a player in the card game
// whether human or computer controlled.

public class Player {
    private String name;
    private Color colour;
    private boolean human;
    private int money = 0;
    private int research = 0;
    private int moduleLevel = 0;
    private String score;
    private ArrayList<Card> cards = new ArrayList<Card>();

    private String[] cardLoadData;
    private Location[] locationLoadData;

    private boolean[] abilities = new boolean[4];

    public Player(Color colour) {
	this.colour = colour;
	for (@SuppressWarnings("unused")
	boolean ability : abilities) {
	    ability = false;
	}
    }

    String getName() {
	return name;
    }

    boolean getHuman() {
	return human;
    }

    int getMoney() {
	return money;
    }

    int getResearch() {
	return research;
    }

    int getModuleLevel() {
	return moduleLevel;
    }

    ArrayList<Card> getCards() {
	return cards;
    }

    Card getPlayerCard(int index) {
	return cards.get(index);
    }

    int getNewResearchCost() {
	int cost = 0;
	for (Card card : cards) {
	    cost += card.getResearch();
	}

	return cost;
    }

    String getScore() {
	score = " " + money + "      " + research + "         " + moduleLevel + " ";
	return score;
    }

    Color getColour() {
	return colour;
    }

    void setName(String name) {
	this.name = name;
    }

    void setHuman(boolean human) {
	this.human = human;
    }

    void setMoney(int money) {
	this.money = money;
    }

    void addResearch(int newResearch) {
	research += newResearch;
    }

    void setResearch(int research) {
	this.research = research;
    }

    void upgradeModuleLevel() {
	moduleLevel++;
    }

    void setModuleLevel(int moduleLevel) {
	this.moduleLevel = moduleLevel;
    }

    void addCard(Card newCard) {
	cards.add(newCard);
	newCard.setOwner(this);
    }

    void removeCard(Card card) {
	cards.remove(card);
    }

    void setCardLoadData(String[] cardLoadData) {
	this.cardLoadData = cardLoadData;
    }

    void setLocationLoadData(Location[] locationLoadData) {
	this.locationLoadData = locationLoadData;
    }

    String[] getCardLoadData() {
	return cardLoadData;
    }

    Location[] getLocationLoadData() {
	return locationLoadData;
    }

    boolean hasMindSlaves() {
	return abilities[0];
    }

    boolean hasSurveillance() {
	return abilities[1];
    }

    boolean hasCredentials() {
	return abilities[2];
    }

    boolean hasDroneMercenaries() {
	return abilities[3];
    }

    void setMindSlaves(boolean value) {
	abilities[0] = value;
    }

    void setSurveillance(boolean value) {
	abilities[1] = value;
    }

    void setCredentials(boolean value) {
	abilities[2] = value;
    }

    void setDroneMercenaries(boolean value) {
	abilities[3] = value;
    }
}
