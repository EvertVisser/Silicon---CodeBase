import java.util.ArrayList;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;

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
 * The following class encapsulates the variables
 * and methods used to process a sequence where
 * one player attacks another player's card
 */

public class AttackRoll {
    private GameBoard gameBoard;
    private GameControl gameControl;
    @SuppressWarnings("unused")
    private Player currentPlayer;

    ArrayList<Location> locations;
    ArrayList<Location> otherLocations;
    ArrayList<Location> targets;
    ArrayList<Card> targetCards;
    ArrayList<ImageView> targetViews;

    public AttackRoll(GameBoard gameBoard, GameControl gameControl, Player currentPlayer) {
	this.gameBoard = gameBoard;
	this.gameControl = gameControl;
	this.currentPlayer = currentPlayer;

	locations = new ArrayList<Location>();
	otherLocations = new ArrayList<Location>();
	targets = new ArrayList<Location>();
	targetCards = new ArrayList<Card>();
	targetViews = new ArrayList<ImageView>();
    }

    boolean canAttack() {
	locations = gameControl.checkPlayerLocations();

	for (Location location : locations) {
	    Rectangle2D rectangle = gameBoard.getRect(location);

	    otherLocations = gameControl.checkOtherLocations();

	    for (Location otherLocation : otherLocations) {
		Rectangle2D otherRectangle = gameBoard.getRect(otherLocation);

		if (rectangle.intersects(otherRectangle)) {
		    if (!targets.contains(otherLocation)) {
			targets.add(otherLocation);
			targetCards.add(otherLocation.getOwner());
		    }
		}
	    }
	}

	if (!targets.isEmpty()) {
	    gameBoard.enableAttack();
	    return true;
	} else {
	    gameBoard.disableAttack();
	    return false;
	}
    }

    void chooseAttack() {

	gameBoard.removeEventHandlers();
	for (Card card : targetCards) {
	    targetViews.add(card.getView());
	}

	for (ImageView targetView : targetViews) {
	    targetView.setOnMouseClicked(e -> {
		Card card = targetCards.get(targetViews.indexOf(targetView));
		Location location = card.getLocation();
		ArrayList<Card> attackCards = new ArrayList<Card>();

		for (Location attackerLocation : locations) {
		    Rectangle2D locationRect = gameBoard.getRect(location);
		    Rectangle2D attackerRect = gameBoard.getRect(attackerLocation);
		    if (locationRect.intersects(attackerRect)) {
			Card attackerCard = attackerLocation.getOwner();
			attackCards.add(attackerCard);
		    }

		}
		Card attacker = attackCards.get(0);
		if (attackCards.size() > 1) {
		    for (Card attackCard : attackCards) {
			if (attackCard.getRevenue() > attacker.getRevenue()) {
			    attacker = attackCard;
			}
		    }
		}

		rollDice(attacker, card);

		gameBoard.removeEventHandlers();
		gameBoard.enableBuy();
		gameBoard.enableResearch();
		PlayerMove move = new PlayerMove("Attack Card");
		gameControl.updateGameState(move);

	    });
	}
    }

    /*
     * NOTE TO DAO: this looks like part of the existing AI algorithm. The AI picks
     * a random card from the various possible targets. It then selects its
     * strongest card (based only on revenue production per round) that is able to
     * attack the target. This will lead to sub-optimal outcomes because, as noted
     * below, the combat system also takes into account research production per
     * round.
     */
    void randomCompAttack() {
	int choices = targets.size();
	int choice = (int) (Math.random() * choices);
	Location targetLocation = targets.get(choice);
	Card targetCard = targetLocation.getOwner();
	ArrayList<Card> attackCards = new ArrayList<Card>();

	for (Location attackerLocation : locations) {
	    Rectangle2D locationRect = gameBoard.getRect(targetLocation);
	    Rectangle2D attackerRect = gameBoard.getRect(attackerLocation);
	    if (locationRect.intersects(attackerRect)) {
		Card attackerCard = attackerLocation.getOwner();
		attackCards.add(attackerCard);
	    }
	}

	Card attacker = attackCards.get(0);
	if (attackCards.size() > 1) {
	    for (Card attackCard : attackCards) {
		if (attackCard.getRevenue() > attacker.getRevenue()) {
		    attacker = attackCard;
		}
	    }
	}
	rollDice(attacker, targetCard);
    }

    /*
     * The combat system values a card at the *greater* of its revenue and research
     * production per round. If the attacker has reached Research Level 2
     * (Surveillance) then their strength is doubled. (NOTE TO DAO: This suggests
     * that the AI could be strengthened by increasing the weight given to spending
     * on research, below this level). The attacker's chance of victory equals their
     * percentage share of the combined strength in the fight. So, if the attacker's
     * and defender's strengths are equal, the chance of victory is 50%. If the
     * attacker's strength is double the defender's, this chance rises to
     * two-thirds; if only half, it falls to one-third. The winner keeps both cards.
     */
    void rollDice(Card attacker, Card defender) {
	int attackerStrength = Math.max(attacker.getRevenue(), attacker.getResearch());
	int defenderStrength = Math.max(defender.getRevenue(), defender.getResearch());

	if (attacker.getOwner().hasSurveillance()) {
	    attackerStrength *= 2;
	}
	double chance = (double) (attackerStrength / (double) (attackerStrength + defenderStrength)) * 100.0;
	double outcome = Math.random() * 100.0;
	if (chance > outcome) {
	    defender.getOwner().removeCard(defender);
	    attacker.getOwner().addCard(defender);
	    gameBoard.cardOutlines(attacker.getOwner());
	    gameControl.newLogEntry("Attacker wins. The " + defender.getName() + " has been awarded to "
		    + attacker.getOwner().getName());
	} else {
	    attacker.getOwner().removeCard(attacker);
	    defender.getOwner().addCard(attacker);
	    gameBoard.cardOutlines(defender.getOwner());
	    gameControl.newLogEntry("Defender wins. The " + attacker.getName() + " has been awarded to "
		    + defender.getOwner().getName());
	}
    }
}
