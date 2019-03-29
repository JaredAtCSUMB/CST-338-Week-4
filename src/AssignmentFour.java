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
      BarcodeImage image = new BarcodeImage();
      image.displayToConsole();
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

/*
 * BarcodeImage class..
 */
class BarcodeImage implements Cloneable
{
   public static final int MAX_HEIGHT = 30;
   public static final int MAX_WIDTH = 65;
   // imageData[height/row][width/column]
   private boolean[][] imageData;
   
   public BarcodeImage()
   {
      imageData = new boolean[MAX_HEIGHT][MAX_WIDTH];
   }
   
   public BarcodeImage(String[] strData)
   {
      if (checkSize(strData)) {
         // TODO: position strData in lower left of imageData
      }
   }
   
   public BarcodeImage clone()
   {
      try {
         return (BarcodeImage)super.clone();
      } catch (CloneNotSupportedException e) {
         return this;
      }
   }
   
   public void displayToConsole()
   {
      for(int i = 0; i < imageData.length; i ++) {
         for(int j = 0; j < imageData[i].length; j++) {
            System.out.println("(" + i + "," + j + ") " + imageData[i][j]);
         }
      }
   }
   
   public boolean getPixel(int row, int col)
   {
      return true;
   }
   
   public boolean setPixel(int row, int col, boolean value)
   {
      return true;
   }
   
   private boolean checkSize(String[] data)
   {
      return true;
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
