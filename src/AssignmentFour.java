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
      testDataMatrix();

   }

   private static void testDataMatrix() {
      System.out.println("Test testDataMatrix----------------------------");

      //this example is already cleanup.  scan should clean it.
      String[] sImageIn =
         {
            "* * * * * * * * * * * * * * * * * * * * * ",
            "*                                       * ",
            "****** **** ****** ******* ** *** *****   ",
            "*     *    ****************************** ",
            "* **    * *        **  *    * * *   *     ",
            "*   *    *  *****    *   * *   *  **  *** ",
            "*  **     * *** **   **  *    **  ***  *  ",
            "***  * **   **  *   ****    *  *  ** * ** ",
            "*****  ***  *  * *   ** ** **  *   * *    ",
            "***************************************** ",
         };
      BarcodeImage bc = new BarcodeImage(sImageIn);
      DataMatrix dm = new DataMatrix(bc);
      dm.translateImageToText();
      dm.displayTextToConsole();
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
      System.out.println("Test BarcodeImage display to console.----------------------------");
      bc.displayToConsole();
      System.out.println("End BarcodeImage display to console.----------------------------");
      
      DataMatrix dm = new DataMatrix(bc);
      System.out.println("Test DataMatrix display to console.----------------------------");
      dm.displayImageToConsole();
      System.out.println("END DataMatrix display to console.----------------------------");

      //Test clone.
      BarcodeImage clonedBarcodeImage = null;
      try {
         clonedBarcodeImage = bc.clone();
         System.out.println("Test Clone----------------------------");
         clonedBarcodeImage.displayToConsole();
      } catch (CloneNotSupportedException e) {
         System.out.println("CloneNotSupportedException: " + e.getMessage());
      }

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
   /**
    *  Accepts some image, represented as a BarcodeImage object to be described below, and stores a copy of this image.  
    *  Depending on the sophistication of the implementing class, the internally stored image might be an exact clone of the parameter, 
    *  or a refined, cleaned and processed image.  Technically, there is no requirement that an implementing class use a BarcodeImage object internally, 
    *  although we will do so.  For the basic DataMatrix option, it will be an exact clone.  
    *  Also, no translation is done here - i.e., any text string that might be part of an implementing class is not touched, updated or defined during the scan.
    * @param bc
    * @return
    */
   public boolean scan(BarcodeImage bc);
   
   /**
    * Accepts a text string to be eventually encoded in an image. No translation is done here - i.e., any BarcodeImage 
    * that might be part of an implementing class is not touched, updated or defined during the reading of the text.
    * @param text
    * @return
    */
   public boolean readText(String text);
   
   /**
    * Not technically an I/O operation, this method looks at the internal text stored in the implementing class and produces a companion BarcodeImage, 
    * internally (or an image in whatever format the implementing class uses).  After this is called, we expect the implementing object to 
    * contain a fully-defined image and text that are in agreement with each other.
    * @return
    */
   public boolean generateImageFromText();
   
   /**
    * Not technically an I/O operation, this method looks at the internal image stored in the implementing class, 
    * and produces a companion text string, internally.  
    * After this is called, we expect the implementing object to contain a fully defined image and text that are in agreement with each other.
    * @return
    */
   public boolean translateImageToText();
   
   /**
    * prints out the text string to the console.
    */
   public void displayTextToConsole();
   
   /**
    * prints out the image to the console.  In our implementation, we will do this in the form of a dot-matrix of blanks and asterisks.
    */
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

      int k = MAX_HEIGHT - 1;
      String data;
      char charData;
      boolean fillData;

      //position image in lower left corner
      //Start from the last element (row) and walk backwards.  
      for(int i = strData.length - 1; i >= 0; i--) {
         data = strData[i];
         // we process each column
         for(int j = 0; j < data.length(); j++) {
            charData = data.charAt(j);
            fillData = false;
            //check if this is space
            if (charData != ' ') {
               fillData = true;
            }
            
            imageData[k][j] = fillData;
         }
         
         k --;
      }
   }
   
   /**
    * @throws CloneNotSupportedException 
    * 
    */
   public BarcodeImage clone() throws CloneNotSupportedException
   {
      return (BarcodeImage)super.clone();
   }
   
   /**
    * Print out the image to the console.
    * Optional - useful for debugging this class
    */
   public void displayToConsole()
   {
      StringBuilder ret;
      for(int i = 0; i < imageData.length; i ++) {
         ret = new StringBuilder();
         for(int j = 0; j < imageData[i].length; j++) {
            if (getPixel(i,j)) {
               ret.append("*");
            } else {
               ret.append("s");
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
   
   // a single internal copy of any image scanned-in OR passed-into the constructor OR created by BarcodeIO's generateImageFromText().
   private BarcodeImage image;
   
   // a single internal copy of any text read-in OR passed-into the constructor OR created by BarcodeIO's translateImageToText().
   private String text;
   
   //typically less than BarcodeImage.MAX_WIDTH and BarcodeImage.MAX_HEIGHT which represent the actual portion of the BarcodeImage that has the real signal.
   //This is dependent on the data in the image, and can change as the image changes through mutators.  It can be computed from the "spine" of the image.
   private int actualWidth;
   private int actualHeight;

   /**
    * Default Constructor -  constructs an empty, but non-null, image and text value.
    * The initial image should be all white, however, actualWidth and actualHeight should start at 0, 
    * so it won't really matter what's in this default image, in practice.  The text can be set to blank, "", or something like "undefined".
    */
   public DataMatrix()
   {
      readText("");
      this.image = new BarcodeImage();
   }
   
   /**
    * Sets the image but leaves the text at its default value.  Call scan() and avoid duplication of code here.
    * @param image
    */
   public DataMatrix(BarcodeImage image)
   {
      scan(image);
   }
   
   /**
    * sets the text but leaves the image at its default value. Call readText() and avoid duplication of code here.
    * @param text
    */
   public DataMatrix(String text)
   {
      readText(text);
   }
   
   public boolean readText(String text)
   {
      this.text = text;
      //TODO: What do we need to check here size of text?
      return true;
   }
   
   /**
    * Besides calling the clone() method of the BarcodeImage class, this method will do a couple of things including calling cleanImage() 
    * and then set the actualWidth and actualHeight.
    * 
    * Because scan() calls clone(), it should deal with the CloneNotSupportedException by embedding the clone() 
    * call within a try/catch block.  
    * 
    * Don't attempt to hand-off the exception using a "throws" clause in the function header since that will not be 
    * compatible with the underlying BarcodeIO interface.  
    * 
    * The catches(...) clause can have an empty body that does nothing.
    */
   public boolean scan(BarcodeImage bc)
   {
      boolean ret = true;

      try {
         //Create a clone image.
         this.image = bc.clone();
         
         //call cleanImage to correct the position.
         cleanImage();
         
         // first pixel that is false determines height/width
         // loop from bottom-top, left-right
         for(int i = image.MAX_HEIGHT - 1; i > 0; i--) {
            if (!image.getPixel(i, 0) && actualHeight <= 0) {
               actualHeight = (image.MAX_HEIGHT - 1 - i);
            }
            for(int j = 0; j < image.MAX_WIDTH; j++) {
               if (!image.getPixel(i, j) && actualWidth <= 0) {
                  actualWidth = j;
               }
            }
         }
      } catch (CloneNotSupportedException t) {
         ret = false;
      }

      return ret;
   }
   
   public int getActualWidth()
   {
      return actualWidth;
   }
   
   public int getActualHeight()
   {
      return actualHeight;
   }
   
   /*
    * Assuming that the image is correctly situated in the lower-left corner of
    * the larger boolean array, these methods use the "spine" of the array
    * (left and bottom BLACK) to determine the actual size.
    */
   private int computeSignalWidth()
   {
      return 0;
   }
   
   private int computeSignalHeight()
   {
      return 0;
   }
   
   /**
    * move the signal to the lower-left of the larger 2D array
    */
   private void cleanImage()
   {
      int horizontalOffset = -1;
      int verticalOffset = -1;

      // loop bottom to top
      for(int i = image.MAX_HEIGHT - 1; i > 0; i--) {
         // loop left to right
         for(int j = 0; j < image.MAX_WIDTH; j++) {
            if (image.getPixel(i, j)) {
               // offsets should only be set once
               if (horizontalOffset < 0) {
                  horizontalOffset = j;
                  verticalOffset = image.MAX_HEIGHT - i - 1;
               }

               image.setPixel(i, j, false);
               image.setPixel(i + verticalOffset, j - horizontalOffset, true);
            }
         }
      }
   }

   /**
    *  display only the relevant portion of the image, clipping the excess blank/white from the top and right.  
    */
   public void displayImageToConsole()
   {
      StringBuilder ret;

      for(int i = image.MAX_HEIGHT - getActualHeight(); i < image.MAX_HEIGHT; i++) {
         ret = new StringBuilder();
         for(int j = 0; j < getActualWidth(); j++) {
            if (image.getPixel(i,j)) {
               ret.append("*");
            } else {
               ret.append(" ");
            }
         }
         System.out.println(ret.toString());
      }
   }
   
   public boolean generateImageFromText()
   {
      return true;
   }
   
   private int getAscii(int row, int position, int column) {
      //ascii values to be added are: 1,2,4,8,16,32,64,128

      boolean value = this.image.getPixel(row - position, column);
      int ret = 0;
      if (value) {
         if (position == 0) {
            ret = 1;
         } else if (position == 1) {
            ret = 2;
         } else if (position == 2) {
            ret = 4;
         } else if (position == 3) {
            ret = 8;
         } else if (position == 4) {
            ret = 16;
         } else if (position == 5) {
            ret = 32;
         } else if (position == 6) {
            ret = 64;
         }else if (position == 7) {
            ret = 128;
         }
      }
      return ret;
   }
   
   public boolean translateImageToText()
   {
      if (this.image == null) {
         return false;
      }
      //we are expecting cleanImage() method to remove extra whitespaces on the image and position the image to lower left corer of the grid.
      //so the image stored internally at this point can be translated using Datamatrix rule
      
      //we remove the last row as it's the Closed Limitation Line
      int startingRow = BarcodeImage.MAX_HEIGHT - 2;
      //we remove the first column starting with 1 as it's the Closed Limitation Line
      int startingColumn = 1;
      
      StringBuilder ret = new StringBuilder();
      for(int j = startingColumn; j < BarcodeImage.MAX_WIDTH - 1; j++) {
         int ascii = 0;
         //ascii values to be added are: 1,2,4,8,16,32,64,128
         //TODO: this is not really optimal
         ascii = ascii + getAscii(startingRow, 0, j);
         ascii = ascii + getAscii(startingRow, 1, j);
         ascii = ascii + getAscii(startingRow, 2, j);
         ascii = ascii + getAscii(startingRow, 3, j);
         ascii = ascii + getAscii(startingRow, 4, j);
         ascii = ascii + getAscii(startingRow, 5, j);
         ascii = ascii + getAscii(startingRow, 6, j);
         ascii = ascii + getAscii(startingRow, 7, j);
         
         //Convert to char and store the letter/char to an array
         //Double check http://www.asciitable.com/
         ret.append((char) ascii);
      }
      
      this.text = ret.toString();
      return true;
   }
   
   public void displayTextToConsole()
   {
      System.out.println(text);
   }
  
}
