import javafx.stage.Screen;

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
// A class that holds variables related to the screen


public class DisplaySetting
{
   private double screenWidth;
   private double screenHeight;
   private double fullWidth;
   private double fullHeight;
   private double widthRatio;
   private double heightRatio;
   
   public DisplaySetting()
   {
	   screenWidth = SiliconGame.DEFAULT_SCREEN_WIDTH;
	   screenHeight = SiliconGame.DEFAULT_SCREEN_HEIGHT;
	   
	   fullWidth = Screen.getPrimary().getVisualBounds().getWidth();
	   fullHeight = Screen.getPrimary().getVisualBounds().getHeight();
	   
	   widthRatio = fullWidth / screenWidth;
	   heightRatio = fullHeight / screenHeight;
	   
	   
   }
   
   double getScreenWidth()
   {
	   return screenWidth;
   }
   
   double getScreenHeight()
   {
	   return screenHeight;
   }
   
   double getFullWidth()
   {
	   return fullWidth;
   }
   
   double getFullHeight()
   {
	   return fullHeight;
   }
   
   double getWidthRatio()
   {
	   return widthRatio;
   }
   
   double getHeightRatio()
   {
	   return heightRatio;
   }
}
