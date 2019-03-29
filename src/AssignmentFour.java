/**
 * This application simulates a barcode scanner...
 * 
 * @author Team 6: Jared Cheney, Andrew Meraz, Chul Kim and Agustin Garcia
 *
 */
public class AssignmentFour
{

   public static void main(String[] args)
   {
      testBarcodeImage();
   }

   public static void testBarcodeImage() {
      String[] sImageIn =
         {
            "                                               ",
            "                                               ",
            "                                               ",
            "     * * * * * * * * * * * * * * * * * * * * * ",
            "     *                                       * ",
            "     ****** **** ****** ******* ** *** *****   ",
            "     *     *    ****************************** ",
            "     * **    * *        **  *    * * *   *     ",
            "     *   *    *  *****    *   * *   *  **  *** ",
            "     *  **     * *** **   **  *    **  ***  *  ",
            "     ***  * **   **  *   ****    *  *  ** * ** ",
            "     *****  ***  *  * *   ** ** **  *   * *    ",
            "     ***************************************** ",
            "                                               ",
            "                                               ",
            "                                               "

         };
      BarcodeImage bc = new BarcodeImage(sImageIn);
      
      //Test display to console.
      //Check to make sure the image is positioned at the bottom left corner.
      System.out.println("Test display to console.----------------------------");
      bc.displayToConsole();

      //Test clone.
      BarcodeImage clonedBarcodeImage = bc.clone();
      System.out.println("Test Clone----------------------------");
      clonedBarcodeImage.displayToConsole();

      //Test setting all of pixels to *
      System.out.println("Test setting all of pixels to *----------------------------");
      for(int i = 0; i < BarcodeImage.MAX_HEIGHT; i ++) {
         for(int j = 0; j < BarcodeImage.MAX_WIDTH; j++) {
            bc.setPixel(i, j, true);
         }
      }
      bc.displayToConsole();
      
      //Test check size
      String[] bigImage =
         {
            "                                               ",
            "                                               ",
            "                                               ",
            "     * * * * * * * * * * * * * * * * * * * * *                                          ",
            "     *                                       * ",
            "     ****** **** ****** ******* ** *** *****   ",
            "     *     *    ****************************** ",
            "     * **    * *        **  *    * * *   *     ",
            "     *   *    *  *****    *   * *   *  **  *** ",
            "     *  **     * *** **   **  *    **  ***  *  ",
            "     ***  * **   **  *   ****    *  *  ** * ** ",
            "     *****  ***  *  * *   ** ** **  *   * *    ",
            "     ***************************************** ",
            "                                               ",
            "                                               ",
            "                                               "

         };
      try {
         new BarcodeImage(bigImage);
      } catch (IllegalArgumentException e) {
         System.out.println("Excepted exception: " + e.getMessage());
      }

   }
}

/*
 * BarcodeIO interface...
 */
interface BarcodeIO
{
   public boolean scan(BarcodeImage bc);
   public boolean readText(String text);
   public boolean generateImageFromText();
   public boolean translateImageToText();
   public void displayTextToConsole();
   public void displayImageToConsole();
}

/**
 * This class will realize all the essential data and methods associated with a 2D pattern, 
 * thought of conceptually as an image of a square or rectangular bar code.  Here are the essential ingredients.  
 * This class has very little "smarts" in it, except for the parameterized constructor.  
 * It mostly just stores and retrieves 2D data.
 * 
 * @author charlesk
 *
 */
class BarcodeImage implements Cloneable
{
   public static final int MAX_HEIGHT = 30;
   public static final int MAX_WIDTH = 65;
   // imageData[height/row][width/column]
   
   //This data will be false for elements that are white, and true for elements that are black.
   private boolean[][] imageData;
   
   /**
    * Default Constructor -  instantiates a 2D array (MAX_HEIGHT x MAX_WIDTH) and stuffs it all with blanks (false).
    */
   public BarcodeImage()
   {
      imageData = new boolean[MAX_HEIGHT][MAX_WIDTH];
   }
   
   /**
    * Takes a 1D array of Strings and converts it to the internal 2D array of booleans.
    * Depends on DataMatrix class that there is no extra space below or left of the image.
    * Puts image into the lower-left corner of the array
    * @param strData
    */
   public BarcodeImage(String[] strData)
   {
      this();
      if (!checkSize(strData)) {
         throw new IllegalArgumentException("Invalid image size.");
      }
      //position image in lower left corner
      //Start from the last element (row) and walk backwards.
      
      int k = MAX_HEIGHT - 1;
      for(int i = strData.length - 1; i >= 0; i--) {
         String data = strData[i];
         // we process each column
         for(int j = 0; j < data.length(); j++) {
            char charData = data.charAt(j);
            //check if this is space
            boolean fillData = false;
            if (charData != ' ') {
               fillData = true;
            }
            imageData[k][j] = fillData;
         }
         k --;
      }
   }
   
   /**
    * 
    */
   public BarcodeImage clone()
   {
      try {
         return (BarcodeImage)super.clone();
      } catch (CloneNotSupportedException e) {
         return this;
      }
   }
   
   /**
    * Print out the image to the console.
    * Optional - useful for debugging this class
    */
   public void displayToConsole()
   {
      for(int i = 0; i < imageData.length; i ++) {
         StringBuilder ret = new StringBuilder();
         for(int j = 0; j < imageData[i].length; j++) {
            if (getPixel(i,j)) {
               ret.append("*");
            } else {
               ret.append(" ");
            }
         }
         System.out.println(ret.toString());
      }
   }
   
   /**
    * you can use the return value for both the actual data and also the error condition -- so that we don't "create a scene" 
    * if there is an error; we just return false.
    * @param row
    * @param col
    * @return
    */
   public boolean getPixel(int row, int col)
   {
      boolean ret = true;
      if (this.imageData == null || this.imageData.length == 0) {
         ret = false; 
      }
      ret = this.imageData[row][col];
      return ret;
   }
   
   /**
    * Sets the pixel value
    * @param row
    * @param col
    * @param value
    * @return
    */
   public boolean setPixel(int row, int col, boolean value)
   {
      boolean ret = true;
      if (this.imageData == null || this.imageData.length == 0) {
         ret = false; 
      }
      try {
         this.imageData[row][col] = value;
      } catch (Throwable e) {
         //if there was any error trying to add to an array, return false.
         ret = false;
      }
      return ret;
   }
   
   /**
    * Optional - Checks the incoming data for every conceivable size or null error.  Smaller is okay.  Bigger or null is not.
    * @param data
    * @return false if failed to validate size of an incoming array.
    */
   private boolean checkSize(String[] data)
   {
      boolean validate = true;
      if (data == null || data.length == 0) {
         return false;
      }
      //if the length of any string in the data array is greater than the MAX_WIDTH, we can't put them.
      for (String in: data) {
         if (in.length() > MAX_WIDTH) {
            validate =  false;
            break;
         }
      }
      // if the length of data array is greater than the MAX_HEIGHT, we can't put them.
      if (data.length > MAX_HEIGHT) {
         validate = false;
      }
      return validate;
   }
}

/*
 * DataMatrix class...
 */
class DataMatrix implements BarcodeIO
{
   public static final char BLACK_CHAR = '*';
   public static final char WHITE_CHAR = ' ';
   private BarcodeImage image;
   private String text;
   private int actualWidth;
   private int actualHeight;

   public boolean scan(BarcodeImage bc)
   {
      return true;
   }
   
   public boolean readText(String text)
   {
      return true;
   }
   
   public boolean generateImageFromText()
   {
      return true;
   }
   
   public boolean translateImageToText()
   {
      return true;
   }
   
   public void displayTextToConsole()
   {
      
   }
   
   public void displayImageToConsole()
   {
      
   }
}
