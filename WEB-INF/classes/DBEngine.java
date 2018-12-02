//
//  Created by Isaias Perez Vega
//

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBEngine {
    private String _USER;
    private String _PASSWORD;
    private String _URL;
    private Connection DBconnection;

    public DBEngine(String URL, String Username, String Password){
        this._URL = URL;
        this._USER = Username;
        this._PASSWORD = Password;
    }

    // Establish the database connection
    public void EstablishConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        DBconnection = DriverManager.getConnection(this._URL, this._USER, this._PASSWORD);
    }


    // Close connection to database
    public void CloseConnection() throws SQLException {
        DBconnection.close();
    }

    // Return current connection
    public Connection getConnection() {
        return this.DBconnection;
    }


}
