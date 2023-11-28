package activeRecord;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnexion {

    private static Connection connect;

    public static Connection getConnexion() throws SQLException {
        if((connect == null) || (connect.isClosed())) {
            try {
                connect = ConnexionFactory.createConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return connect;
    }


}
