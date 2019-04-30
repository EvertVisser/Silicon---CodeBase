import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
// Card class used to represent the details of a
// a card such as name and revenue. Also holds a
// location value in relation to the card table.
public class Card {
    private String name;
    private int revenue = 0;
    private int research = 0;
    private String cardType = "default";
    private Image display;
    private Image flipSide;
    private ImageView cardView;
    private Location location = new Location();
    private Player owner = null;

    private boolean socialMedia;
    private boolean robotic;

    public Card(String name, int revenue, int research, String imageName, boolean socialMedia, boolean robotic) {
	this.name = name;
	this.revenue = revenue;
	this.research = research;
	location.setOwner(this);

	try {
	    flipSide = new Image("card.png");
	    display = new Image(imageName);
	    cardView = new ImageView(flipSide);
	} catch (Exception ex) {
	    System.out.println("Card Class (lines 39-40): Failed to load card images - check file system.");
	}

	this.socialMedia = socialMedia;
	this.robotic = robotic;
    }

    String getName() {
	return name;
    }

    int getRevenue() {
	return revenue;
    }

    int getResearch() {
	return research;
    }

    String getCardType() {
	return cardType;
    }

    Image getImage() {
	return display;
    }

    Image getFlipSide() {
	return flipSide;
    }

    ImageView getView() {
	return cardView;
    }

    void setView(ImageView imageView) {
	imageView.setFitWidth(108.0);
	imageView.setFitHeight(72.0);

	cardView = imageView;
    }

    Location getLocation() {
	return location;
    }

    void setLocation(Location location) {
	this.location = location;
	location.setOwner(this);
    }

    Player getOwner() {
	return owner;
    }

    void setOwner(Player player) {
	owner = player;
    }

    boolean getSocialMedia() {
	return socialMedia;
    }

    boolean getRobotic() {
	return robotic;
    }

}
