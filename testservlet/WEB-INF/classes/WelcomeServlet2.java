// WelcomeServlet2.java
// Processing HTTP get requests containing data.

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class WelcomeServlet2 extends HttpServlet {
    // process "get" request from client
    protected void doGet( HttpServletRequest request,  HttpServletResponse response )
            throws ServletException, IOException
    {
        String clientName = request.getParameter( "clientname" );

        response.setContentType( "text/html" );
        PrintWriter out = response.getWriter();

        // send HTML document to client
        // start HTML document
        out.println( "<!DOCTYPE html>" );
        out.println( "<html lang='en'>" );
        out.println( "<head>" );
        out.println( "<title>Processing get requests with data</title>" );
        out.println( "<meta charset=\"utf-8\">" );
        out.println( "<style type='text/css'>");
        out.println( "<!--  body{background-image: url(images/background.jpg); font-family:calibri;} span{color:blue;} -->");
        out.println( "</style>");
        out.println( "</head>" );
        // body section of document
        out.println( "<body>" );
        out.println( "<h1>Hello <span>" + clientName + "</span>,</span><br />" );
        out.println();
        out.println( "Welcome to the Exciting World of Servlet Technology!</h1>" );
        out.println( "</body>" );
        // end HTML document
        out.println( "</html>" );
        out.close();  // close stream to complete the page
    }//end doGet() method
}//end class WelcomeServlet2
