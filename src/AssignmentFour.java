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
      // Below are the actual tests needed for the assignment
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

      String[] sImageIn_2 =
      {
            "                                          ",
            "                                          ",
            "* * * * * * * * * * * * * * * * * * *     ",
            "*                                    *    ",
            "**** *** **   ***** ****   *********      ",
            "* ************ ************ **********    ",
            "** *      *    *  * * *         * *       ",
            "***   *  *           * **    *      **    ",
            "* ** * *  *   * * * **  *   ***   ***     ",
            "* *           **    *****  *   **   **    ",
            "****  *  * *  * **  ** *   ** *  * *      ",
            "**************************************    ",
            "                                          ",
            "                                          ",
            "                                          ",
            "                                          "
      };

      String[] textOut =
      {
            "* * * * * * * * * * * * * * * * *   ",  
            "*                                   ",   
            "*** ******* **  * * *** ********    ",   
            "* ************** ****************   ",     
            "**   ***     *    *   *  * **       ",     
            "*       *   *        *      ***     ",     
            "***   *  **  *   *  **  ***  ***    ",     
            "**  ** *         ** ***  * * **     ",     
            "*** * ** *  * * ***  *  * * ** **   ",     
            "*********************************   ",
      };
     
      BarcodeImage bc = new BarcodeImage(sImageIn);
      DataMatrix dm = new DataMatrix(bc);
     
      // First secret message
      System.out.println("Test 1: ");
      dm.translateImageToText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();
      
      // Second secret message
      System.out.println("\nTest 2: ");
      bc = new BarcodeImage(sImageIn_2);
      dm.scan(bc);
      dm.translateImageToText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();
      
      // Create your own message
      System.out.println("\nTest 3: ");
      dm.readText("We crushed it! A's for everyone!");
      dm.generateImageFromText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();
      
      // Test the output from image generated from text
      System.out.println("\nTest 4: ");
      bc = new BarcodeImage(textOut);
      dm.scan(bc);
      dm.translateImageToText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();
   }
}

/*
 * BarcodeIO interface defines the methods we will need to manipulate
 * BarcodeImage objects
 */
interface BarcodeIO
{
   /**
    * Clones the image and positions it in the lower left of the array
    * @param bc BarcodeImage to be cloned
    */
   public boolean scan(BarcodeImage bc);
   
   /**
    * A mutator for setting the text property
    * @param String text to be converted into BarcodeImage
    */
   public boolean readText(String text);
   
   /**
    * Generates a BarcodeImage from the text passed into readText()
    */
   public boolean generateImageFromText();
   
   /**
    * Translates a BarcodeImage into readable text
    */
   public boolean translateImageToText();
   
   /**
    * Prints out the text string to the console
    */
   public void displayTextToConsole();
   
   /**
    * Prints the BarcodeImage to the console
    */
   public void displayImageToConsole();
}

/**
 * This class will realize all the essential data and methods associated with a
 * 2D pattern, thought of conceptually as an image of a square or rectangular
 * bar code. Here are the essential ingredients. This class has very little
 * "smarts" in it, except for the parameterized constructor. It mostly just
 * stores and retrieves 2D data.
 * 
 * @author charlesk
 *
 */
class BarcodeImage implements Cloneable
{
   public static final int MAX_HEIGHT = 30;
   public static final int MAX_WIDTH = 65;   
   // Spaces are represented as false; asterisks as true
   private boolean[][] imageData;

   public BarcodeImage()
   {
      imageData = new boolean[MAX_HEIGHT][MAX_WIDTH];
   }
   
   /**
    * Takes a 1D array of Strings and converts it to the internal 2D array of
    * booleans. Depends on DataMatrix class that there is no extra space below
    * or left of the image. Puts image into the lower-left corner of the array
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

      // Position image in lower left corner
      // Start from the last element (row) and walk backwards.  
      for(int i = strData.length - 1; i >= 0; i--) {
         data = strData[i];
         // we process each column
         for(int j = 0; j < data.length(); j++) {
            charData = data.charAt(j);
            fillData = false;
            // check if this is space
            if (charData != ' ') {
               fillData = true;
            }
            
            imageData[k][j] = fillData;
         }
         
         k --;
      }
   }
   
   /**
    * A simple clone method
    * @throws CloneNotSupportedException 
    * 
    */
   public BarcodeImage clone() throws CloneNotSupportedException
   {
      return (BarcodeImage)super.clone();
   }
      
   /**
    * Simple accessor method to get a specific 'pixel' in the BarcodeImage
    * @param row
    * @param col
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
    * Simple mutator method to set a specific 'pixel' in the BarcodeImage
    * @param row
    * @param col
    * @param value true for asterisk, false for space
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
    * Ensures that data is not null, nor bigger than the max width/height
    * @param data
    * @return false if failed to validate size of an incoming array.
    */
   private boolean checkSize(String[] data)
   {
      boolean validate = true;

      if (data == null || data.length == 0) {
         return false;
      }
      // check width
      for (String in: data) {
         if (in.length() > MAX_WIDTH) {
            validate =  false;
            break;
         }
      }
      // check height
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

   public DataMatrix()
   {
      readText("");
      this.image = new BarcodeImage();
   }
   
   /**
    * Sets the image but leaves the text at its default value
    * @param image
    */
   public DataMatrix(BarcodeImage image)
   {
      scan(image);
   }
   
   /**
    * Sets the text but leaves the image at its default value
    * @param text
    */
   public DataMatrix(String text)
   {
      readText(text);
   }
   
   public boolean readText(String text)
   {
      this.text = text;
      return true;
   }
   
   /**
    * Attempts to clone the incoming image. If successful, it positions the
    * image in the lower left of the array. It then calculates the actualHeight
    * and actualWidth. 
    */
   public boolean scan(BarcodeImage bc)
   {
      boolean ret = true;

      try {
         this.image = bc.clone();
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
   
   public void displayTextToConsole()
   {
      System.out.println(text);
   }
   
   /*
    * Assuming that the image is correctly situated in the lower-left corner of
    * the larger boolean array, these methods use the "spine" of the array
    * (left and bottom BLACK) to determine the actual size.
    */
   // TODO: complete this method
   private int computeSignalWidth()
   {
      return 0;
   }
   
   // TODO: complete this method
   private int computeSignalHeight()
   {
      return 0;
   }
   
   /**
    * Move the signal to the lower-left of the larger 2D array
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
    *  Display only the relevant portion of the image, clipping the excess
    *  blank/white from the top and right
    */
   public void displayImageToConsole()
   {
      StringBuilder ret;

      for(int i = image.MAX_HEIGHT - getActualHeight();
            i < image.MAX_HEIGHT; i++) {
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
   
   // TODO: clean this bad boy up
   public boolean generateImageFromText()
   {
      int numberOfChars = text.length();
      int[] asciiValues = new int[numberOfChars];
      String[] str = new String[numberOfChars];
      String[] img = new String[10];
      for(int i = 0; i < numberOfChars; i++) {
         asciiValues[i] = (int)text.charAt(i);
      }
      
      for(int i = 0; i < asciiValues.length; i++) {
         String binary = Integer.toBinaryString(asciiValues[i]);
         while (binary.length() < 8) {
            binary = "0" + binary;
         }
         str[i] = "";
         for (int j = 0; j < binary.length(); j++) {
            if (binary.charAt(j) == '1') {
               str[i] += '*';
            } else {
               str[i] += ' ';
            }
         }
      }

      int pos = 0;
      for (int i = 0; i < 10; i++) {
         img[i] = "*";
         if (i == 0) {
            for(int j = 0; j < str.length / 2; j++) {
               img[i] += " *";
            }
         } else if (i == 9) {
            for(int j = 0; j < str.length; j++) {
               img[i] += "*";
            }
         } else {
            for(int j = 0; j < str.length; j++) {
               img[i] += str[j].charAt(pos);
            }
            pos++;
         }
      }
      this.image = new BarcodeImage(img);
      return true;
   }
   
   // TODO: llok for clean up
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
   
   // TODO: look for clean up
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
}
