package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toList;

public class Controller {

    @FXML
    private Button but;

    @FXML
    private ImageView viewer;

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private Button zoomButtonIn;

    @FXML
    private Button zoomButtonOut;

    @FXML
    private TextArea textArea;

    @FXML
    private RadioButton inverseButton;

    @FXML
    private Button saveButton;

    public File file;
    public BufferedImage coreImg;
    public BufferedImage res;

    public String fileUrl;

    public List<Character> order;
    public String charBuffer;

    public boolean mode;
    public boolean invMode;

    static int SIZE_OF_FONT;

    private static final int IMG_SIZE = 128;

    void Presets()
    {
        SIZE_OF_FONT = 5;
        textArea.setEditable(false);
        textArea.setStyle(
                "-fx-font-family: 'Lucida Console';" +
                " -fx-font-size: 10;");
        comboBox.setEditable(true);
        comboBox.getStylesheets().add(getClass().getResource("styles/comboBoxStyle.css").toExternalForm());

        comboBox.setPromptText("@#MBHAGh93X25Sisr;:, ");
        comboBox.getItems().addAll(" ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
                ,"@#MBHAGh93X25Sisr;:, ", "█▓▒░ ", " 1234567890",
                "M@WB08Za2SX7r;i:;. ", " #,.0123456789:;@ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz$");

        mode = false;
        invMode = false;

        charBuffer = "@#MBHAGh93X25Sisr;:, ";
        orderMaker(charBuffer);
    }

    //region Display
    public String reverseFinalChar(int grey) //reversed
    {
        String finalChar;
        int cellsCount, index;

        finalChar ="";
        cellsCount = (255/(order.size()-1));

        index = grey/cellsCount;
        index = (index >= order.size())? order.size()-1 : index;

        finalChar = Character.toString(order.get(index));

        return finalChar;
    }

    public String finalChar(int grey)//normal
    {
        String fChar;
        int cellsCount, index;

        fChar ="";
        cellsCount = (255/(order.size()-1));
        index = grey/cellsCount;
        index = (index >= order.size())? order.size()-1 : index;
        index = (order.size()-1) - index;
        fChar = Character.toString(order.get(index));

        return fChar;
    }
    //endregion

    @FXML//choose image////
    void Show(ActionEvent event) throws IOException
    {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG files", "*.png"),
                new FileChooser.ExtensionFilter("JPG files", "*.jpg", "*.JPEG"),
                new FileChooser.ExtensionFilter("BMP files", "*.bmp"));

         file = fc.showOpenDialog(null);
         fileUrl = file.toURI().toURL().toString();
         coreImg = ImageIO.read(file);
         mode = true;

         this.ToGrayscale();
    }

    @FXML
    void saveTXT(ActionEvent event) throws FileNotFoundException {
        if(mode)
        {
            FileChooser saver = new FileChooser();
            saver.setInitialFileName("unnamed");
            saver.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("text file", "*.txt"),
                    new FileChooser.ExtensionFilter("pdf file", "*.pdf"));
            saver.setInitialDirectory(new File("C:\\"));

            File saveFile = saver.showSaveDialog(null);
            PrintWriter writer = new PrintWriter(saveFile);
            writer.write(textArea.getText());
            writer.close();
        }
    }

    @FXML /////COMBOBOXEVENT/////
    void BoxAction(ActionEvent event)
    {
        charBuffer = comboBox.getValue();
        orderMaker(charBuffer);
        if(mode)
        {
            this.ToGrayscale();
        }
    }

    //region ZOOM
    @FXML
    void ZoomPlus(ActionEvent event)
    {
        SIZE_OF_FONT +=2;
        String c;
        c = "-fx-font-size:" + Integer.toString(SIZE_OF_FONT);
        textArea.setStyle(
                "-fx-font-family: 'Lucida Console';" + c);
        if(mode)
        {
            this.ToGrayscale();
        }
    }

    @FXML
    void ZoomOut(ActionEvent event)
    {
        if (SIZE_OF_FONT != 1) {
            SIZE_OF_FONT -= 1;
            String c;
            c = "-fx-font-size:" + Integer.toString(SIZE_OF_FONT);
            textArea.setStyle(
                    "-fx-font-family: 'Lucida Console';" + c);
            if(mode)
            {
                this.ToGrayscale();
            }
        }
    }
   //endregion

    //region toGrayScale
     public void ToGrayscale()
     {
      BufferedImage result = new BufferedImage( coreImg.getWidth(), coreImg.getHeight(), coreImg.getType());
      int y,x;
      StringBuilder str = new StringBuilder();

      for(y = 0; y < coreImg.getHeight();y++)
      {
          for( x = 0 ;  x < coreImg.getWidth();x++)
          {
              Color col = new Color(coreImg.getRGB(x,y));

              int grey = (((int)(col.getRed()*0.2989)+(int)(col.getBlue()*0.5870)+(int)(col.getGreen()*0.1140)));

              if(invMode)
              {
                  str.append(reverseFinalChar(grey));
              }
              else
              {
                  str.append(finalChar(grey));
              }
              Color newCol = new Color((int)grey, (int)grey, (int)grey);
              result.setRGB(x, y, newCol.getRGB());
          }
          str.append("\n");
      }
      textArea.setText(str.toString());
    }
    //endregion

    @FXML
    void inverseMode(ActionEvent event) {
        invMode = !invMode;
        if(mode)
        {
            this.ToGrayscale();
        }
    }

    //region UtilitsAlgos
    private static float GetBrightness(char c)
    {
        BufferedImage img = new BufferedImage(IMG_SIZE, IMG_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics canvas = img.getGraphics();
        canvas.setFont(new Font("Monaco", Font.PLAIN, (int) (img.getHeight() * 0.5)));

        canvas.drawString(c+"",0 , canvas.getFont().getSize());
        canvas.dispose();

        int totalBrightness = 0;
        for(int i = 0; i < img.getWidth(); i++)
        {
            for(int j = 0; j < img.getWidth(); j++)
            {
                totalBrightness+=img.getRGB(i, j) & 0xFF;
            }
        }
        return  totalBrightness / (float)(img.getWidth() * img.getHeight());
    }

    public void orderMaker(String charBuffer)
    {
        HashMap<Character, Float> brightness = new HashMap<Character, Float>();

        for(int i = 0; i < charBuffer.length(); i++)
        {
            brightness.put(charBuffer.charAt(i),GetBrightness(charBuffer.charAt(i)));
        }
        order = brightness.entrySet().stream()
                .sorted(comparingByValue())
                .map(Map.Entry::getKey)
                .collect(toList());
    }


//endregion

}


