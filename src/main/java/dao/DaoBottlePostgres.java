package dao;

import java.io.*;
import java.sql.*;


public class DaoBottlePostgres implements DaoBottle {
    private static DaoBottle daoBottle;
    private static String database;
    private static String user;
    private static String password;

    private DaoBottlePostgres(){
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver loading success");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static DaoBottle getInstance(){
        if (daoBottle == null){
            daoBottle = new DaoBottlePostgres();
        }
        return daoBottle;
    }

    @Override
    public void addMessange(String messange) {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("insert into bottle values (default,?,current_timestamp,default)");
            statement.setString(1,messange);
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @Override
    public String readRandomMessange() {
        Connection connection = getConnection();
        Statement statement = null;
        String randomMessage = null;
        try {
            statement = connection.createStatement();
            ResultSet before = statement.executeQuery("select count(id) as id from bottle");
            before.next();
            if (before.getInt("id")!=0){
            ResultSet resultSet = statement.executeQuery("select message,id,numberofreadings from bottle limit 1 offset (select (count(id) - 1) * random()  from bottle)");
            resultSet.next();
            String message = resultSet.getString("message");
            int id = resultSet.getInt("id");
            int numberOfFreadings = resultSet.getInt("numberofreadings");
            randomMessage = "\"" + message + "\"" + " Раз прочитано:" + numberOfFreadings;
            statement.execute("update bottle set numberofreadings = numberofreadings + 1 where id = " + id);
            }else {
                randomMessage = "Бутылка пока пустая";
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return randomMessage;
    }

    @Override
    public void deleteAllMessage() {
        Connection connection = getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute("truncate bottle");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @Override
    public void initialization() throws SQLException {
        File DBC = new File("target/classes/DBC");
        try(FileReader fileReader = new FileReader(DBC);
            BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            database = bufferedReader.readLine();
            user = bufferedReader.readLine();
            password = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Connection connection = null;
        Statement statement = null;
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + database, user, password);
            statement = connection.createStatement();
            statement.executeUpdate("create table if not exists bottle  (" +
                    "id serial primary key ," +
                    "Message varchar," +
                    "Timestamp timestamp not null," +
                    "NumberOfReadings int default 0)");
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    private Connection getConnection(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + database, user, password);
            return connection;
        } catch (SQLException throwables) {
            DaoConnection.launch();
        }
        return null;
    }
}
