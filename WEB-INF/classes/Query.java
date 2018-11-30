//
// Created by Isaias Perez Vega
//

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.sql.Connection;

// import com.mysql.jdbc.MySQLConnection;

public class Query {

    private Connection connection;
    private ResultSetMetaData metaData;
    private Vector<String> columns;

    // Get a valid connection to database
    public Query(Connection connection){
        this.connection = connection;
    }

    // Run queries, returns result from DB as vector of vectors.
    public Vector <Vector<String>> runQuery (String mQuery) throws SQLException {

        Vector<Vector<String>> _results = new Vector<Vector<String>>();

        // Create a aql statement to query
        Statement statement = (Statement) this.connection.createStatement();

        // Execute  statement
        ResultSet results = statement.executeQuery(mQuery);
        metaData = results.getMetaData();

        int numCols = metaData.getColumnCount();
        setColumns(numCols,metaData);

        // Create table row by row
        while(results.next()){
            Vector<String> row = new Vector<String>();
            for(int i = 1; i <= numCols; i++){
                row.add(results.getString(i));
            }
            _results.add(row);
        }
        return _results;
    }

    // Gets columns from previous query
    public Vector<String> getColumns() throws SQLException{
        return this.columns;
    }

    // Check to see if command is read properly
    public int runUpdate(String mQuery) throws SQLException{
        Statement statement = this.connection.createStatement();
        return statement.executeUpdate(mQuery);
    }

    // Set column variables
    public void setColumns(int mNumColumns, ResultSetMetaData mMetaData) throws SQLException{
        columns = new Vector<String>();
        for(int i = 1; i <= mNumColumns; i++){
            columns.add(mMetaData.getColumnName(i));
        }
    }
}
