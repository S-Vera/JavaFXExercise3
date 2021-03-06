package fi.utu.tech.gui.javafx.zipper;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class ZipperController {
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private TextField sourceField;

    @FXML
    private TextField destField;

    @FXML
    private Button zipItButton;

    public Label statusLabel;

    public void setLabel(String text) {
        if (!statusLabel.textProperty().isBound())
            statusLabel.setText(text);
    }

    @FXML
    void chooseDest(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Zip files (.zip)", "*.zip"));
        chooser.setTitle("Destination zip");
        File selected = chooser.showSaveDialog(stage);
        if (selected == null) return;
        destField.setText(selected.getAbsolutePath());
        if (!destField.getText().endsWith(".zip"))
            destField.setText(destField.getText() + ".zip");
        setButtonState(null);
    }

    @FXML
    void chooseSource(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Source folder");
        File selected = chooser.showDialog(stage);
        if (selected == null) return;
        sourceField.setText(selected.getAbsolutePath());
    }

    @FXML
    void zipIt(ActionEvent event){
        try {
        	zipItButton.setText("Cancel");
        	Zipper z = new Zipper(sourceField.getText(), destField.getText());

            Thread th = new Thread(z);
        	zipItButton.setOnAction(new EventHandler<ActionEvent>() {
        		public void handle(ActionEvent event) {
        			z.cancel();
        		}
        	});
            th.setDaemon(true);
            th.start();
            
            setLabel("Valmis!");
            zipItButton.setText("Zip it!");
            
        } catch (Exception e) {
            setLabel(e.getMessage());
        }
    }


    <T> void setButtonState(T t) {
        zipItButton.setDisable(!destField.getText().endsWith(".zip"));
    }

    public void initialize() {
        setButtonState(null);
        destField.setOnKeyTyped(this::setButtonState);
    }
    
   public class NameLoader{
    	public void labelName(String name) {
    		setLabel("name");
    	}
    	
    }
}