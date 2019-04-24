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
// The following class is intended to encapsulate aspects of the
// game rules. The variables/methods of this class represent a
// reference point for the GameController class in managing the
// game.
public class GameRules
{
   final private int NUMBER_OF_PLAYERS = 4;
   final private int NUMBER_OF_CARDS = 54;
   final private int STARTING_MONEY = 100;
   
   final private int MAIN_DECK_X = 270;
   final private int MAIN_DECK_Y = 288;
   final private boolean HORIZONTAL = true;
   
   final private Location[] locations = {new Location(270, 0, true),
      new Location(558, 288, false), new Location(270, 576, true), 
      new Location(-18, 288, false)};
   
   final private Color[] colours = {Color.CADETBLUE, Color.CRIMSON, Color.GOLD,
      Color.GREEN};
   
   public GameRules()
   {
	   
   }
   
   int getNumberOfPlayers()
   {
	   return NUMBER_OF_PLAYERS;
   }
   
   int getNumberOfCards()
   {
	   return NUMBER_OF_CARDS;
   }
   
   int getStartingMoney()
   {
	   return STARTING_MONEY;
   }
   
   Location getStartLocation(int index)
   {
	   return locations[index];
   }
   
   Color getColours(int index)
   {
	   return colours[index];
   }
   
   int getMainDeckX()
   {
	   return MAIN_DECK_X;
   }
   
   int getMainDeckY()
   {
	   return MAIN_DECK_Y;
   }
   
   boolean getHorizontal()
   {
	   return HORIZONTAL;
   }
}
