package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.geometry.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.nio.channels.WritableByteChannel;


public class Main extends Application {
    /**
     * Daca nu se va specifica nici o dimensiune prin panelul de configurare
     * se va crea un patrat de dimensiuni 2(lat) culoare ramei fiind neagra
     */
    int dimensiune = 2;
    String culoare = "0xffffffff";

    /**
     * Setter/getter pentru dimensiune/culoare
     */
    public void setDimensiune(int dim){
        dimensiune = dim;
    }
    public void setCuloare(String color){
        culoare = color;
    }
    public int getDimensiune(){
        return dimensiune;
    }
    public String getCuloare(){
        return culoare;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Drawing");
        primaryStage.setScene(new Scene(root, 600, 500));
        primaryStage.show();
        primaryStage.setResizable(false);
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(primaryStage);


        Scene scene = primaryStage.getScene();
        ColorPicker colorPicker = (ColorPicker) scene.lookup("#culori");
        Button set2 = (Button) scene.lookup("#set2");
        TextField sizeInput = (TextField) scene.lookup("#dimensiune");
        Button exit = (Button) scene.lookup("#exit");
        Button load = (Button) scene.lookup("#load");
        Button reset = (Button) scene.lookup("#reset");
        Button save = (Button) scene.lookup("#save");

        Canvas canvas = (Canvas) scene.lookup("#canvas");

        GraphicsContext gc = canvas.getGraphicsContext2D();

        colorPicker.setOnAction(e -> {
            String color = String.valueOf(colorPicker.getValue());
            System.out.println(color);
            setCuloare(color);
        });
        set2.setOnAction(e -> {
            try{
                if(!sizeInput.getText().isEmpty()){
                    int size = Integer.parseInt(sizeInput.getText());
                    System.out.println(size);
                    setDimensiune(size);
                }
            }
            catch (Exception s){
                System.out.println(s.toString());
            }

        });
        exit.setOnAction(e -> Platform.exit());
        reset.setOnAction(e -> {
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        });
        save.setOnAction(e -> {
            if(file != null){
                WritableImage wi = new WritableImage((int) 599, (int) 230);
                try{
                    SnapshotParameters sp = new SnapshotParameters();
                    sp.setFill(Color.TRANSPARENT);
                    ImageIO.write(SwingFXUtils.fromFXImage(canvas.snapshot(null,wi),null),"png",file);
                }
                catch (Exception s){
                    System.out.println(s.toString());
                }
            }
        });
        load.setOnAction(e -> {

        });

        canvas.setOnMouseClicked(e ->{
            System.out.println("canvas");
            if(e.getButton() == MouseButton.PRIMARY){
                drawRect(gc, e);
            }
        });

    }

    public void drawRect(GraphicsContext gc, MouseEvent event){
        Rectangle2D rectangle2D = getRect(event);
        Color color = Color.web(getCuloare());
        gc.setFill(color);
        gc.fillRect(rectangle2D.getMinX(), rectangle2D.getMinY(), getDimensiune(), getDimensiune());
    }

    private Rectangle2D getRect(MouseEvent event){
        return new Rectangle2D(event.getX(), event.getY(), getDimensiune(), getDimensiune());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
