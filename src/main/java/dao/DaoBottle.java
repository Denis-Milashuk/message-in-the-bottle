package dao;
import java.sql.SQLException;

public interface DaoBottle {
    public void addMessange (String messange);
    public String readRandomMessange();
    public void deleteAllMessage();
    public void initialization() throws SQLException;
}

