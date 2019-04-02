/**
 * This application models how a scanner would scan a DataMatrix code to
 * actual text by decoding the DataMatrix pixels. In this assignment, the
 * pixels are documented with * for a black pixel, and ' '(space) for a white
 * pixel. We will be using BarcodeIO interface, BarcodeImage class, and 
 * DataMatrix class.
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
            "* * * * * * * * * * * * * * * * * * * *     ",
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
            "* * * * * * * * * * * * * * * * * ",    
            "*                                *",    
            "*** ******* **  * * *** ********  ",   
            "* ************** *****************",   
            "**   ***     *    *   *  * **     ",    
            "*       *   *        *      ***  *",    
            "***   *  **  *   *  **  ***  ***  ",    
            "**  ** *         ** ***  * * **  *",    
            "*** * ** *  * * ***  *  * * ** ** ",    
            "**********************************"
      };
      
      System.out.println((char)170);
     
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
 * BarcodeImage objects. 
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
 * DataMatrix class works on converting the data matrix image to a readable
 * text, or converting readable text to a data matrix image. This class 
 * implements BarcodeIO interface and works with a BarcodeImage object.
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
         actualHeight = computeSignalHeight();
         actualWidth = computeSignalWidth();
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
   
   /* Method that computes width of image through looping and sending
    * attributes through getPixel returns width. Assumes image is positioned
    * lower left
    */
   private int computeSignalWidth()
   {
      int counterWidth = 0;
      
      for(int i = 0; i < BarcodeImage.MAX_WIDTH; i++) {
         if(image.getPixel(BarcodeImage.MAX_HEIGHT - 1, i)) {
            counterWidth++;
         }
      }
      
      return counterWidth;
   }
   
   /*
    * Method that computes height of image through looping and sending
    * attributes through getPixel returns height. Assumes image is positioned
    * lower left
    */
   private int computeSignalHeight()
   {
      int counterHeight = 0;
      
      for(int k = BarcodeImage.MAX_HEIGHT - 1; k >= 0; k--) {
         if(image.getPixel(k, 0)) {
            counterHeight++;
         }
      }
      
      return counterHeight;
   }
   
   /**
    * Move the signal to the lower-left of the larger 2D array
    */
   private void cleanImage()
   {
      int horizontalOffset = -1;
      int verticalOffset = -1;

      // loop bottom to top
      for(int i = BarcodeImage.MAX_HEIGHT - 1; i > 0; i--) {
         // loop left to right
         for(int j = 0; j < BarcodeImage.MAX_WIDTH; j++) {
            if (image.getPixel(i, j)) {
               // offsets should only be set once
               if (horizontalOffset < 0) {
                  horizontalOffset = j;
                  verticalOffset = BarcodeImage.MAX_HEIGHT - i - 1;
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

      for(int i = BarcodeImage.MAX_HEIGHT - getActualHeight();
            i < BarcodeImage.MAX_HEIGHT; i++) {
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
   
   /*
    * Generates a new BarcodeImage based off the text sent into the readText()
    * method. Calls the getConvertedChars() and getFormattedImage() helper
    * methods to do the text-to-image conversions.
    */
   public boolean generateImageFromText()
   {
      String[] convertedChars = getConvertedChars();
      String[] formattedImage = getFormattedImage(convertedChars);
      this.image = new BarcodeImage(formattedImage);
      return true;
   }
   
   /*
    * This method first converts the characters of text var into their ascii
    * values. It then converts those values into their binary representation.
    * Finally, it converts the binary representation into either an asterisk
    * or space, depending on its true/false state.
    */
   private String[] getConvertedChars()
   {
      int ascii;
      String binary;
      int numberOfChars = text.length();
      String[] str = new String[numberOfChars];

      for(int i = 0; i < numberOfChars; i++) {
         str[i] = "";
         // Convert char to ascii
         ascii = (int)text.charAt(i);
         // Convert ascii to binary
         binary = Integer.toBinaryString(ascii);
         // Ensure we have 8 bits
         while (binary.length() < 8) {
            binary = "0" + binary;
         }
         // Convert binary values to strings: true = "*" or false = " "
         for (int j = 0; j < binary.length(); j++) {
            if (binary.charAt(j) == '1') {
               str[i] += '*';
            } else {
               str[i] += ' ';
            }
         }
      }

      return str;
   }
   
   /*
    * This method takes the formatted character array, and converts it into
    * an image that will be used to create a new BarcodeImage. It adds the 
    * 'spines' to the image as well
    */
   private String[] getFormattedImage(String[] str)
   {
      // Ascii char values are between 2^0 and 2^8, we add 2 for the 'spines'
      int maxHeight = 10;
      int pos = 0;
      String[] img = new String[maxHeight];

      // Populates the img array with the converted text values
      for (int i = 0; i < img.length; i++) {
         img[i] = "*";
         // First row consists of asterisks and spaces
         if (i == 0) {
            for(int j = 0; j <= str.length; j++) {
               //if column is odd then *, else even then space
               if(j%2 == 1) {
                  img[i] += "*";
               }else{
                  img[i] += " ";
               }
            }
         // Last row consists of solid *
         } else if (i == 9) {
            for(int j = 0; j <= str.length; j++) {
               img[i] += "*";
            }
         // Populate the body with the correct value ("*" or " ")
         } else {
            for(int j = 0; j <= str.length; j++) {
               if(j == str.length)
               {
                  //if row is odd then *, else space
                  if(i%2 == 1){
                     img[i] += "*";
                  }else {
                     img[i] += " ";
                  }
               }else{
                  img[i] += str[j].charAt(pos);
               }
            }
            // pos only counts from 0-8; powers of 2
            pos++;
         }
      }

      return img;
   }
   
   //Helps get the value of the row position in a column and returns the int 
   private int getAscii(int row, int position, int column) {
      //ascii values to be added are: 1,2,4,8,16,32,64,128

      boolean value = this.image.getPixel(row - position, column);
      int ret = 0;
      int num[] = {1, 2, 4, 8, 16, 32, 64, 128};
      if (value) {
         ret = num[position];
      }
      return ret;
   }
  
   public boolean translateImageToText()
   {
      if (this.image == null) {
         return false;
      }
      /*we are expecting cleanImage() method to remove extra whitespaces on the 
       * image and position the image to lower left corer of the grid.
       * so the image stored internally at this point can be translated using 
       * Datamatrix rule
       * we remove the last row as it's the Closed Limitation Line
       **/
      int startingRow = BarcodeImage.MAX_HEIGHT - 2;
      //we remove the first column starting with 1 as it's the Closed Limitation Line
      int startingColumn = 1;
      
      StringBuilder ret = new StringBuilder();
      for(int j = startingColumn; j < actualWidth - 1; j++) {
         int ascii = 0;
         //ascii values to be added are: 1,2,4,8,16,32,64,128
         for (int i = 0; i < 8; i++){
            ascii = ascii + getAscii(startingRow, i, j);
         }
         //Convert to char and store the letter/char to an array
         //Double check http://www.asciitable.com/
         ret.append((char) ascii);
      }
      
      this.text = ret.toString();
      return true;
   }  
}
/********************************* Output ************************************
ª
Test 1: 
CSUMB CSIT online program is top notch.
* * * * * * * * * * * * * * * * * * * * *
*                                       *
****** **** ****** ******* ** *** *****  
*     *    ******************************
* **    * *        **  *    * * *   *    
*   *    *  *****    *   * *   *  **  ***
*  **     * *** **   **  *    **  ***  * 
***  * **   **  *   ****    *  *  ** * **
*****  ***  *  * *   ** ** **  *   * *   
*****************************************

Test 2: 
You did it!  Great work.  Celebrate.
* * * * * * * * * * * * * * * * * * * 
*                                    *
**** *** **   ***** ****   *********  
* ************ ************ **********
** *      *    *  * * *         * *   
***   *  *           * **    *      **
* ** * *  *   * * * **  *   ***   *** 
* *           **    *****  *   **   **
****  *  * *  * **  ** *   ** *  * *  
**************************************

Test 3: 
We crushed it! A's for everyone!
* * * * * * * * * * * * * * * * *     
*                                *    
*** ******* **  * * *** ********      
* ************** *****************    
**   ***     *    *   *  * **         
*       *   *        *      ***  *    
***   *  **  *   *  **  ***  ***      
**  ** *         ** ***  * * **  *    
*** * ** *  * * ***  *  * * ** **     
**********************************    

Test 4: 
We crushed it! A's for everyone!
* * * * * * * * * * * * * * * * * 
*                                *
*** ******* **  * * *** ********  
* ************** *****************
**   ***     *    *   *  * **     
*       *   *        *      ***  *
***   *  **  *   *  **  ***  ***  
**  ** *         ** ***  * * **  *
*** * ** *  * * ***  *  * * ** ** 
**********************************
*****************************************************************************/
