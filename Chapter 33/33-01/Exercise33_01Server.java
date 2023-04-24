// Exercise31_01Server.java: The server can communicate with
// multiple clients concurrently using the multiple threads
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Exercise33_01Server extends Application {
  // Text area for displaying contents
  private TextArea ta = new TextArea();
  private ArrayList<Loan> loans = new ArrayList<>();
  DataInputStream clientIn;
  DataOutputStream clientOut;

  double annualInterestRate;
  int numberOfYears;
  double loanAmount;

  @Override // Override the start method in the Application class
  public void start(Stage primaryStage) throws IOException, InterruptedException {

    ta.setWrapText(true);
   
    // Create a scene and place it in the stage
    Scene scene = new Scene(new ScrollPane(ta), 400, 200);
    primaryStage.setTitle("Exercise31_01Server"); // Set the stage title
    primaryStage.setScene(scene); // Place the scene in the stage
    primaryStage.show(); // Display the stage

    Thread thread = new Thread(() -> {
      try {
        ServerSocket socket = new ServerSocket(6969);
        Platform.runLater(() -> {
          ta.appendText("Exercise31_01Server started at " + new Date() + '\n');
        });
        Socket client = socket.accept();

        Platform.runLater(() -> {
          ta.appendText("Connected to a client at " + new Date() + '\n');
        });

        clientIn = new DataInputStream(client.getInputStream());
        clientOut = new DataOutputStream(client.getOutputStream());

        while (true) {
          annualInterestRate = clientIn.readDouble();
          numberOfYears = clientIn.readInt();
          loanAmount = clientIn.readDouble();

          Loan loan = new Loan(annualInterestRate, numberOfYears, loanAmount);
          loans.add(loan);

          clientOut.writeDouble(loan.getMonthlyPayment());
          clientOut.writeDouble(loan.getTotalPayment());
          clientOut.flush();

          ta.appendText("Annual Interest Rate: " + loan.getAnnualInterestRate()
                  + "\nNumber of years: " + loan.getNumberOfYears()
                  + "\nLoan Amount: " + loan.getLoanAmount()
                  + "\nMonthly Payment: " + loan.getMonthlyPayment()
                  + "\nTotal Payment: " + loan.getTotalPayment() + '\n');
        }

      } catch (IOException e) {
        e.printStackTrace();
      }
    });

    thread.start();

    }


    
  /**
   * The main method is only needed for the IDE with limited
   * JavaFX support. Not needed for running from the command line.
   */
  public static void main(String[] args) {
    launch(args);
  }
}
