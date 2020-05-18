package com.hardrock;

import dao.DaoBottle;
import dao.DaoBottlePostgres;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.sql.SQLException;

/**
 * JavaFX App
 */
public class App extends Application {
    private static boolean a = true;
    private static boolean b = true;

    @Override
    public void start(Stage stage) {
        DaoBottle daoBottle = DaoBottlePostgres.getInstance();
        while (a) {
            try {
                daoBottle.initialization();
                a = false;
            } catch (SQLException throwables) {
                Stage stageSqlConnection = new Stage();
                stageSqlConnection.setTitle("Ошибка подключения");
                Label label = new Label("Не удалось подключиться к базе данных!!\n" +
                        "    Введите данные подключения.");
                TextField database = new TextField("database");
                TextField user = new TextField("login");
                TextField password = new TextField("password");
                Button enter = new Button("Ввод");
                enter.setOnAction(x -> {
                    File DBC = new File("target/classes/DBC");//"src/main/resources/DBC"
                    try (PrintWriter printWriter = new PrintWriter(DBC);) {
                        printWriter.println(database.getText());
                        printWriter.println(user.getText());
                        printWriter.println(password.getText());
                        printWriter.flush();
                        stageSqlConnection.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                });
                Button exit = new Button("Выход");
                exit.setOnAction(x -> {
                    stageSqlConnection.close();
                    a = false;
                    b = false;
                });
                VBox vBox = new VBox(label, database, user, password, enter, exit);
                vBox.setSpacing(5);
                vBox.setPadding(new Insets(10));
                vBox.setAlignment(Pos.CENTER);
                Scene scene = new Scene(vBox, 400, 250);
                stageSqlConnection.setScene(scene);
//                stageSqlConnection.initStyle(StageStyle.UTILITY);
                stageSqlConnection.showAndWait();
            }
        }
        if (b) {
            stage.setTitle("Послания в бутылке от Володьки");
            TextField textField = new TextField("Введите ваше послание");
            textField.setOnAction(x -> {
                String messange = textField.getText();
                daoBottle.addMessange(messange);
                textField.setText("");
            });
            Button enter = new Button("Добавить послание");
            enter.setOnAction(x -> {
                daoBottle.addMessange(textField.getText());
                textField.setText("");
            });
            Label messagefromBottle = new Label();
            Button randomMessage = new Button("Достать из бутылки случайное послание");
            randomMessage.setOnAction(x -> {
                messagefromBottle.setText(daoBottle.readRandomMessange());
            });
            Button deleteAllMessage = new Button("Вытряхнуть все послания из бутылки");
            deleteAllMessage.setOnAction(x -> {
                daoBottle.deleteAllMessage();
            });
            Button close = new Button("Выход");
            close.setOnAction(x -> {
                stage.close();
            });
            VBox vBox = new VBox(textField, enter, randomMessage, messagefromBottle, deleteAllMessage, close);
            vBox.setSpacing(5);
            vBox.setPadding(new Insets(10));
            vBox.setAlignment(Pos.CENTER);
            Scene myScene = new Scene(vBox, 640, 200);
            stage.setScene(myScene);
//            stage.initStyle(StageStyle.UNIFIED);
            stage.show();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}