// Exercise31_01Client.java: The client sends the input to the server and receives
// result back from the server
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Exercise33_01Client extends Application {
  // Text field for receiving radius
  private TextField tfAnnualInterestRate = new TextField();
  private TextField tfNumOfYears = new TextField();
  private TextField tfLoanAmount = new TextField();
  private Button btSubmit= new Button("Submit");

  // Text area to display contents
  private TextArea ta = new TextArea();
  DataOutputStream serverOut;
  DataInputStream serverIn;
  @Override // Override the start method in the Application class
  public void start(Stage primaryStage) throws IOException {


    ta.setWrapText(true);
   
    GridPane gridPane = new GridPane();
    gridPane.add(new Label("Annual Interest Rate"), 0, 0);
    gridPane.add(new Label("Number Of Years"), 0, 1);
    gridPane.add(new Label("Loan Amount"), 0, 2);
    gridPane.add(tfAnnualInterestRate, 1, 0);
    gridPane.add(tfNumOfYears, 1, 1);
    gridPane.add(tfLoanAmount, 1, 2);
    gridPane.add(btSubmit, 2, 1);
    
    tfAnnualInterestRate.setAlignment(Pos.BASELINE_RIGHT);
    tfNumOfYears.setAlignment(Pos.BASELINE_RIGHT);
    tfLoanAmount.setAlignment(Pos.BASELINE_RIGHT);
    
    tfLoanAmount.setPrefColumnCount(5);
    tfNumOfYears.setPrefColumnCount(5);
    tfLoanAmount.setPrefColumnCount(5);
            
    BorderPane pane = new BorderPane();
    pane.setCenter(new ScrollPane(ta));
    pane.setTop(gridPane);
    
    // Create a scene and place it in the stage
    Scene scene = new Scene(pane, 400, 250);
    primaryStage.setTitle("Exercise31_01Client"); // Set the stage title
    primaryStage.setScene(scene); // Place the scene in the stage
    primaryStage.show(); // Display the stage





    btSubmit.setOnAction(e -> connectToServer());
      try {
        Socket socket = new Socket("localhost", 6969);

        serverOut = new DataOutputStream(socket.getOutputStream());
        serverIn = new DataInputStream(socket.getInputStream());

      } catch (IOException ex) {
        ex.printStackTrace();
      }



  }
  public void connectToServer() {
    try {

      serverOut.writeDouble(Double.parseDouble(tfAnnualInterestRate.getText().trim()));
      serverOut.writeInt(Integer.parseInt(tfNumOfYears.getText().trim()));
      serverOut.writeDouble(Double.parseDouble(tfLoanAmount.getText().trim()));
      serverOut.flush();

      double monthlyPayment = serverIn.readDouble();
      double totalPayment = serverIn.readDouble();

      ta.appendText("Annual Interest Rate: " + tfAnnualInterestRate.getText().trim() +"\nNumber of years: "
              + tfNumOfYears.getText().trim() + "\nLoan Amount: " + tfLoanAmount.getText().trim()
              + "\nMonthly Payment: " + monthlyPayment + "\nTotal Payment: " + totalPayment + '\n');

    } catch (IOException e) {
      System.err.println(e);
    }

  }
  
  /**
   * The main method is only needed for the IDE with limited
   * JavaFX support. Not needed for running from the command line.
   */
  public static void main(String[] args) {
    launch(args);
  }
}
