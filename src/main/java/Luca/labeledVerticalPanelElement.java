package Luca;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class labeledVerticalPanelElement {
    private Object node;
    private VBox vBox;
    private Label label;
    
    labeledVerticalPanelElement(String labelMessage, Node node){
        initVerticalPanelElem(labelMessage, node);
        vBox.getChildren().add(label);
        vBox.getChildren().add(node);
    }
    labeledVerticalPanelElement(String labelMessage, DrawingPanel panel){
        initVerticalPanelElem(labelMessage, panel);
        vBox.getChildren().add(label);
        vBox.getChildren().add(panel.getPane());
    }
    private void initVerticalPanelElem(String labelMessage, Object node){
        Font font = Font.loadFont("file:src/main/resources/Luca/Roboto-Regular.ttf", 15);
        this.node = node;
        label = new Label(labelMessage);
        label.setFont(font);
        vBox = new VBox();
        vBox.setSpacing(5);
    }

    public Object getNode(){
        return node;
    }

    public VBox getvBox() {
        return vBox;
    }
}
