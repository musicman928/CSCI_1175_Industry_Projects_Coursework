import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Exercise33_09Server extends Application {
  private TextArea taServer = new TextArea();
  private TextArea taClient = new TextArea();

  private Socket client;
  private DataInputStream clientIn;
  private DataOutputStream clientOut;
 
  @Override // Override the start method in the Application class
  public void start(Stage primaryStage) throws IOException {
    taServer.setWrapText(true);
    taClient.setWrapText(true);
    taServer.setEditable(false);

    BorderPane pane1 = new BorderPane();
    pane1.setTop(new Label("History"));
    pane1.setCenter(new ScrollPane(taServer));
    BorderPane pane2 = new BorderPane();
    pane2.setTop(new Label("New Message"));
    pane2.setCenter(new ScrollPane(taClient));
    
    VBox vBox = new VBox(5);
    vBox.getChildren().addAll(pane1, pane2);

    // Create a scene and place it in the stage
    Scene scene = new Scene(vBox, 200, 200);
    primaryStage.setTitle("Exercise31_09Server"); // Set the stage title
    primaryStage.setScene(scene); // Place the scene in the stage
    primaryStage.show(); // Display the stage


    Thread socketThread = new Thread(() -> {
      try {
        ServerSocket socket = new ServerSocket(6969);

        client = socket.accept();
        clientIn = new DataInputStream(client.getInputStream());
        clientOut = new DataOutputStream(client.getOutputStream());
        String message;
        while (true) {

          message = clientIn.readUTF();

          taServer.appendText(message + '\n');
          System.out.println(message);
          sendHistory();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    });


    taClient.setOnKeyPressed(e -> {
      KeyCode key = e.getCode();
      if (key == KeyCode.ENTER) {
        try {
          sendMessage(taClient.getText().trim());
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }
    });
    socketThread.start();
  }

  public void sendMessage(String message) throws IOException {
    taServer.appendText("Server: " + message + '\n');
    taClient.clear();

    sendHistory();
  }

  private void sendHistory() throws IOException {
    String history = taServer.getText();
    clientOut.writeUTF(history);
    //clientOut.write((char) -1);
    clientOut.flush();
  }

  /**
   * The main method is only needed for the IDE with limited
   * JavaFX support. Not needed for running from the command line.
   */
  public static void main(String[] args) {
    launch(args);
  }
}
