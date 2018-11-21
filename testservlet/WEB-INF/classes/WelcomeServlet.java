// A simple servlet to process get requests.

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class WelcomeServlet extends HttpServlet {   
   // process "get" requests from clients
   protected void doGet( HttpServletRequest request, 
                         HttpServletResponse response ) throws ServletException, IOException  {

      response.setContentType( "text/html" );
      PrintWriter out = response.getWriter();  
      // send HTML5 page to client
      // start HTML5 document
      out.println( "<!DOCTYPE html>" ); 
      out.println( "<html lang=\"en\">" );
	  out.println( "<meta charset=\"utf-8\">" );
           // head section of document
      out.println( "<head>" );
	  out.println( "<style type='text/css'>");
	  out.println( "<!--  body{background-color:blue; color:white;} h1{font-size:60pt;} -->");
	  out.println( "</style>");
      out.println( "<title>Welcome to Servlets!</title>" );
      out.println( "</head>" );
      // body section of document
      out.println( "<body>" );
      out.println( "<h1>Hello!!</h1>");
      out.println( "<h1>Welcome To The Exciting World Of Servlet Technology!</h1>" );
      out.println( "</body>" );
      // end HTML5 document

      out.println( "</html>" );
      out.close();  // close stream to complete the page
   }   
}


