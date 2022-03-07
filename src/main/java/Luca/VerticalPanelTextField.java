package Luca;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class VerticalPanelTextField {
    private TextField field;
    private VBox vBox;
    private Label label;
    
    VerticalPanelTextField(String labelMessage, String fieldMessage){
        Font font = Font.loadFont("file:src/main/resources/Luca/Roboto-Regular.ttf", 15);
        field = new TextField(fieldMessage);
        field.setFont(font);
        label = new Label(labelMessage);
        label.setFont(font);
        vBox = new VBox();
        vBox.setSpacing(5);
        vBox.getChildren().add(label);
        vBox.getChildren().add(field);
    }

    public String getText(){
        return field.getText();
    }

    public VBox getvBox() {
        return vBox;
    }
}
