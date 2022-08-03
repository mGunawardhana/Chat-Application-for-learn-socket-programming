/*
 * Developed by - mGunawardhana
 * Contact email - mrgunawardhana27368@gmail.com
 * what's app - 071 - 9043372
 */

package client;

import com.jfoenix.controls.JFXButton;
import com.sun.xml.internal.messaging.saaj.soap.impl.TextImpl;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import server.Server;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    public JFXButton button_send;

    @FXML
    public TextField tf_message;

    @FXML
    public ScrollPane sp_main;

    @FXML
    public VBox vbox_messages;
    private Client client;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try{
           client = new Client(new Socket("localhost",1234));
            System.out.println("Connected to Server");
        }catch (IOException e){}

        vbox_messages.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sp_main.setVvalue((Double) newValue);
            }
        });

//        client.recieveMessageFromServer(vbox_messages);

        client.receiveMessageFromServer(vbox_messages);
        button_send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String messageToSend = tf_message.getText();
                if(!messageToSend.isEmpty()){
                    HBox hBox= new HBox();
                    hBox.setAlignment(Pos.CENTER_RIGHT);

                    hBox.setPadding(new Insets(5,5,5,10));


                    javafx.scene.text.Text text = new javafx.scene.text.Text(messageToSend);
                    TextFlow textFlow = new TextFlow(text);


                    textFlow.setStyle("-fx-color: rgb(239,242,255);" +
                            "-fx-background-color: rgb(15,125,242);" +
                            " -fx-background-radius: 20px");



                    textFlow.setPadding(new Insets(5, 10, 5, 10));
                    text.setFill(Color.color(0.934, 0.945, 0.996));

                    hBox.getChildren().add(textFlow);
                    vbox_messages.getChildren().add(hBox);

                    client.sendMessageToServer(messageToSend);
                    tf_message.clear();
                }
            }
        });
    }

    public static  void addLabel(String msgFromServer, VBox vBox){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5,5,5,10));

        javafx.scene.text.Text text = new Text(msgFromServer);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: rgb(233,233,235);" +
                " -fx-background-radius: 20px");

        textFlow.setPadding(new Insets(5,10,5,10));
        hBox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);

            }
        });
    }
}
