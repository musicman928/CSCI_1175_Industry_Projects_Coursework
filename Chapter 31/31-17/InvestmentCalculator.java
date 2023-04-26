import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.xml.soap.Text;

public class InvestmentCalculator extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane gridPane = new GridPane();
        MenuBar menuBar = new MenuBar(new Menu("Operation"));
        GridPane textFields = new GridPane();
        BorderPane buttonPane = new BorderPane();

        Scene scene = new Scene(gridPane);
        primaryStage.setScene(scene);
        gridPane.addRow(0, menuBar);
        menuBar.setPrefWidth(scene.getWidth());
        MenuItem calculateMenu = new MenuItem("Calculate");
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(e -> System.exit(0));
        menuBar.getMenus().get(0).getItems().add(0, calculateMenu);
        menuBar.getMenus().get(0).getItems().add(1, exit);

        gridPane.addRow(1,textFields);
        TextField amtField = new TextField();
        amtField.setAlignment(Pos.CENTER_RIGHT);
        textFields.addRow(0, new BorderPane(null, null, amtField, null, new Label("Investment Amount")));
        TextField yearsField = new TextField();
        yearsField.setAlignment(Pos.CENTER_RIGHT);
        textFields.addRow(1, new BorderPane(null, null, yearsField, null, new Label("Number of Years:")));
        TextField interestField = new TextField();
        interestField.setAlignment(Pos.CENTER_RIGHT);
        textFields.addRow(2, new BorderPane(null, null, interestField, null, new Label("Annual Interest Rate:")));
        TextField valueField = new TextField();
        valueField.setAlignment(Pos.CENTER_RIGHT);
        textFields.addRow(3, new BorderPane(null, null, valueField, null, new Label("Future Value:")));
        Button calculate = new Button("Calculate");
        textFields.addRow(4, new BorderPane(null, null, calculate, null, null));

        calculate.setOnAction(e -> calculateValue(amtField, yearsField, interestField, valueField));
        calculateMenu.setOnAction(calculate.getOnAction());
        textFields.applyCss();
        primaryStage.setWidth(textFields.getWidth());




        primaryStage.show();

    }

    public static void calculateValue(TextField investmentAmt, TextField numOfYears, TextField annualInterest, TextField futureValue) {
        double amt = Double.parseDouble(investmentAmt.getText());
        double years = Double.parseDouble(numOfYears.getText());
        double interest = Double.parseDouble(annualInterest.getText());
        double monthlyInterest = interest / 12;
        futureValue.setText('$' + String.format("%.2f", amt * Math.pow((1 + monthlyInterest / 100), years * 12)));
    }
}
