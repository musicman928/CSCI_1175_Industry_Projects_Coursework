import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Exercise33_09Client extends Application {
  private TextArea taServer = new TextArea();
  private TextArea taClient = new TextArea();

  private DataInputStream serverIn;
  private DataOutputStream serverOut;
 
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
    primaryStage.setTitle("Exercise31_09Client"); // Set the stage title
    primaryStage.setScene(scene); // Place the scene in the stage
    primaryStage.show(); // Display the stage

    Thread thread = new Thread(() -> {
    try {
      Socket socket = new Socket("localhost", 6969);

      serverIn = new DataInputStream(socket.getInputStream());
      serverOut = new DataOutputStream(socket.getOutputStream());

    } catch (IOException e) {
      e.printStackTrace();
    }

    taClient.setOnKeyPressed(e -> {
      KeyCode key = e.getCode();
      if (key == KeyCode.ENTER) {
        try {
          sendMessage();
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }
    });

    while (true) {
      try {
        taServer.setText(serverIn.readUTF());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    });
    thread.start();

  }

  public void sendMessage() throws IOException {
    String message = taClient.getText().trim();
    taClient.clear();
    serverOut.writeUTF("Client: " + message);
    serverOut.flush();

    //taServer.setText(serverIn.readUTF());

  }

  /**
   * The main method is only needed for the IDE with limited
   * JavaFX support. Not needed for running from the command line.
   */
  public static void main(String[] args) {
    launch(args);
  }
}
