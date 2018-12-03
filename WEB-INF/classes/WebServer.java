//
// Created by Isaias Perez Vega
//

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Vector;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns={"/WebServer"})
public class WebServer extends HttpServlet {

    private DBEngine _DBEngine;
    private DBQuery _DBQuery;
    public WebServer() {
        super();
    }

    // Client request communication to the WebServer
    protected  void doGet(HttpServletRequest request, HttpServletResponse response) throws  ServletException, IOException {
        Map parameters = request.getParameterMap();

        String connectionURL = "jdbc:mysql://localhost:3306/project4?"
                + "useSSL=false&useUnicode=true&"
                + "useJDBCCompliantTimezoneShift=true&"
                + "useLegacyDatetimeCode=false&serverTimezone=UTC";

        // Connecting to DB
        if (parameters.containsKey("execute")) {
            _DBEngine = new DBEngine(connectionURL, "root", "root");
            _DBQuery = new DBQuery(_DBEngine.getConnection());
        }

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/client.jsp");
        dispatcher.forward(request, response);
    }


    // Business logic server responds to client request
    protected  void doPost(HttpServletRequest request, HttpServletResponse response) throws  ServletException, IOException {
        Vector<Vector<String>> results = new Vector<Vector<String>>();
        Vector<String> columns = new Vector<String>();
        Map parameters = request.getParameterMap();
        String affectedSuppliers = "";
        String sql = "";
        String partnum = "";
        HttpSession session = request.getSession();
        String destination  ="/client.jsp";
        String queryString = request.getParameter("textarea");
        String HTML;
        String connectionURL = "jdbc:mysql://localhost:3306/project4?"
                + "useSSL=false&useUnicode=true&"
                + "useJDBCCompliantTimezoneShift=true&"
                + "useLegacyDatetimeCode=false&serverTimezone=UTC";

        // Default query if no input in text area
        if (queryString == "") queryString = "select * from suppliers";

        // Connect to the backend DB
        _DBEngine = new DBEngine(connectionURL, "root", "root");
        try {
            _DBEngine.EstablishConnection();
            _DBQuery = new DBQuery(_DBEngine.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // If the query is a SELECT type, get results from DB and generate html for client
        if (queryString.toLowerCase().startsWith("select")) {
            try {
                results = _DBQuery.runQuery(queryString);
                columns = _DBQuery.getColumns();

                HTML = generateHTML(results,columns);
            } catch (SQLException e1) {
                HTML = generateHTMLError(e1.getMessage());
            }
        } else {

            // Not a SELECT type
            try {
                String supplierSnum = "";
                boolean updateSupplier = false;
                boolean updateSuPart = false;

                // Check if it is an insert type or update type
                if (queryString.toLowerCase().contains("insert into shipments") || queryString.toLowerCase().contains("replace into shipments")) {
                    // Get the values
                    int first = queryString.indexOf("(");
                    int last = queryString.indexOf(")");
                    String temp = queryString.substring(first+1, last);
                    temp = temp.replaceAll("'", "");
                    temp = temp.replaceAll(" ", "");
                    String[] brokenString = temp.split(",");

                    // Check if any new shipment has a quantity of >= 100  and if so
                    // flag the supplier number
                    for (String word: brokenString) {
                        try{
                            if (Integer.valueOf(word) >= 100) {
                                updateSupplier = true;
                            }
                        } catch (NumberFormatException e) {
                            if (word.startsWith("S")) {
                                supplierSnum = word;
                            }
                        }
                    }
                }

                // The query requires an update of quantity for all shipments
                if (queryString.toLowerCase().contains("update shipments")) {

                    // Get the part number being increased
                    if (queryString.toLowerCase().contains("set quantity")) {
                        int lp = queryString.indexOf("'");
                        partnum = queryString.substring(lp+1, lp + 3);
                        updateSupplier = false; updateSuPart = true;
                    }
                }

                // Run if the query requires an update for a supplier
                if (updateSupplier) {
                    Vector<Vector<String>> temp = _DBQuery.runQuery (
                            "select distinct(suppliers.snum) from suppliers join shipments " +
                                    "on suppliers.snum = shipments.snum and shipments.quantity >= 100");

                    affectedSuppliers += "'" + supplierSnum + "'";

                    sql = "UPDATE suppliers set status = (status+" + 5 + ") where snum = " + affectedSuppliers;
                    HTML = generateHTMLOK(_DBQuery.runUpdate(queryString),_DBQuery.runUpdate(sql));

                // Run if the query involves changing the quantity of a certain part
                } else if (updateSuPart) {

                    String qstring = "select distinct t1.snum from (select shipments.snum from shipments where shipments.pnum = '" + partnum + "') t1 " +
                            "join (select distinct(suppliers.snum) from suppliers join shipments on suppliers.snum = shipments.snum " +
                            "and shipments.quantity >= 100) f1 on t1.snum = f1.snum";
                    String qstring2 = "select distinct snum from shipments left join bshipments using (snum, pnum, jnum, quantity) where bshipments.snum is null and quantity > 100;";
                    Vector<Vector<String>> temp = _DBQuery.runQuery(qstring);
                    affectedSuppliers = "";

                    // Getiing results from backend and parsing the update string
                    for (Vector row: temp) {
                        if (affectedSuppliers == "") {
                            affectedSuppliers += "'" + row.get(0) + "'";
                        } else {
                            affectedSuppliers += ",'" + row.get(0) + "'";
                        }
                    }

                    // Updating backend, and creating response HTML for client
                    sql = "UPDATE suppliers set status = (status+"+5+") where snum in (" + affectedSuppliers + ")";
                    HTML = generateHTMLOK(_DBQuery.runUpdate(queryString),_DBQuery.runUpdate(sql));

                } else {
                    HTML = generateHTMLOK(_DBQuery.runUpdate(queryString));
                }
            } catch (SQLException e1) {
                HTML = generateHTMLError(e1.getMessage());
            }
        }

        // Responding to client
        session.setAttribute("results", HTML);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(destination);
        dispatcher.forward(request, response);

        // Close connection to the backend DB
        try {
            _DBEngine.CloseConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Generates the HTML for the query result table
    private String generateHTML(Vector<Vector<String>> results,Vector<String> columns) {
        String htmlString = "<div id='table' style='margin:auto;text-align:center;width: 650px;'><table border='1' style='margin: auto;'><tr>";

        for (String mCol: columns) {
            htmlString+= "<td style='background-color: #99ff99;'>"+mCol+"</td>";
        }

        htmlString+="</tr>";
        int colorRow = 0;

        for (Vector row: results) {
            if (colorRow % 2 == 0) {
                htmlString += "<tr>";
            } else {
                htmlString+= "<tr style='background-color: #f2f2f2;'>";
            }

            colorRow = colorRow + 1;
            for (int i=0; i < row.size(); i++) {
                htmlString += "<td>";
                htmlString += row.get(i);
                htmlString += "</td>";
            }
            htmlString += "</tr>";
        }
        return htmlString+="</table></div>";
    }

    private String generateHTMLError(String message){
        String htmlString = "<div id='table' style='margin:auto; text-align:center;width: 800px;'><table border='1' style='margin: auto;background-color: red;'><tr>";
        htmlString += "<td>Error executing the SQL statement:<br/>" + message + "</td>";
        return htmlString;
    }
    private String generateHTMLOK(int columns){
        String htmlString = "<div id='table' style='margin:auto; text-align:center;width: 800px;'><table border='1' style='margin: auto;background-color: green;'><tr>";
        htmlString += "<td>The SQL statement completed successfully:<br/>" + columns + " row(s) affected.</td>";
        return htmlString;
    }
    private String generateHTMLOK(int columns, int suppliers){
        String htmlString = "<div id='table' style='margin:auto; text-align:center;width: 800px;'><table border='1' style='margin: auto;background-color: green;'><tr>";
        htmlString += "<td>The SQL statement completed successfully:<br/>" + columns + " row(s) affected.<br> " +
                "Business Logic Detected! - Updating Suppliers Status<br> Business Logic update " + suppliers + " supplier status marks.</td>";
        return htmlString;
    }
}
