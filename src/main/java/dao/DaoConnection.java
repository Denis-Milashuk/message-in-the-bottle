package dao;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class DaoConnection extends Application {
    @Override
    public void start(Stage stage)  {
        TextField dataBase = new TextField("Введите название базы данных");
        TextField user = new TextField("Введите имя пользователя");
        TextField password = new TextField("Введите пароль");
        Button enter = new Button("Ввод");
        enter.setOnAction(x->{
            File DBC = new File("src/main/resources/DBC");
            try(FileWriter fileWriter = new FileWriter(DBC);
                PrintWriter printWriter = new PrintWriter(fileWriter);) {
                printWriter.println(dataBase.getText());
                printWriter.println(user.getText());
                printWriter.println(password.getText());
                stage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        VBox vBox = new VBox (dataBase,user,password,enter);
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();
    }
}
