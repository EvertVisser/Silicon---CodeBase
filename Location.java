
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
 * Location class used to represent the location of
 * a card on the card table.
 */

public class Location {
    private int xCoord = 500;
    private int yCoord = 380;
    private boolean horizontal = true;
    private Card owner;

    public Location() {

    }

    public Location(int xCoord, int yCoord, boolean horizontal) {
	this.xCoord = xCoord;
	this.yCoord = yCoord;
	this.horizontal = horizontal;
    }

    int getXCoord() {
	return xCoord;
    }

    int getYCoord() {
	return yCoord;
    }

    boolean getHorizontal() {
	return horizontal;
    }

    void setXCoord(int xCoord) {
	this.xCoord = xCoord;
    }

    void setYCoord(int yCoord) {
	this.yCoord = yCoord;
    }

    void setHorizontal(boolean horizontal) {
	this.horizontal = horizontal;
    }

    Card getOwner() {
	return owner;
    }

    void setOwner(Card card) {
	owner = card;
    }
}
