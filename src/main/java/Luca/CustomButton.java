package Luca;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.text.Font;

public class CustomButton {
    private Button button;
    CustomButton(String style, String buttonMessage, EventHandler<ActionEvent> event){
        Font font = Font.loadFont("file:src/main/resources/Luca/Roboto-Regular.ttf", 20);
        button = new Button(buttonMessage);
        button.setOnAction(event);

        ColorAdjust buttonHovered = new ColorAdjust();
        buttonHovered.setBrightness(-0.1);
        ColorAdjust buttonNotHovered = new ColorAdjust();
        buttonNotHovered.setBrightness(0);

        button.setPrefWidth(200);
        button.setStyle(style);
        button.setOnMouseEntered(e -> button.setEffect(buttonHovered));
        button.setOnMouseExited(e -> button.setEffect(buttonNotHovered));
        button.setFont(font);
    }

    public Button getButton() {
        return button;
    }
}
