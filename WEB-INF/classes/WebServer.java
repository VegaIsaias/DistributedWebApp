//
// Created by Isaias Perez Vega
//

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebServer extends HttpServlet {

    private DBEngine _DBEngine;
    private DBQuery _DBQuery;


    public WebServer() {
        super();
    }

    // Client request contains information for business server to process
    protected  void doGet(HttpServletRequest request, HttpServletResponse response) throws  ServletException, IOException {
        Map parameters = request.getParameterMap();
        if (parameters.containsKey("execute")) {
            _DBEngine = new DBEngine("jdbc:mysql://localhost:3306/project4", "root", "admin");
            _DBQuery = new DBQuery(_DBEngine.getConnection());
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/client.jsp");
        dispatcher.forward(request, response);
    }


    // Business logic server responds to client request
    protected  void doPost(HttpServletRequest request, HttpServletResponse response) throws  ServletException, IOException {

    }



}
