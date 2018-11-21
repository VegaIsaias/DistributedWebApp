//  WelcomeServlet3.java
// Processing post requests containing data.
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class WelcomeServlet3 extends HttpServlet
{
    // process "post" request from client
    protected void doPost( HttpServletRequest request,  HttpServletResponse response )
            throws ServletException, IOException
    {
        String clientName = request.getParameter( "clientname" );

        response.setContentType( "text/html" );
        PrintWriter out = response.getWriter();
        // send HTML page to client
        // start HTML document
        out.println( "<!DOCTYPE html>" );
        out.println( "<html lang='en'>" );
        out.println( "<head>" );
        out.println( "<title>Processing post requests with data</title>" );
        out.println( "<meta charset=\"utf-8\">" );
        out.println( "<style type='text/css'>");
        out.println( "<!--  body{background-image: url(images/background.jpg); font-family:calibri;} span{color:red;} -->");
        out.println( "</style>");
        out.println( "</head>" );
        // body section of document
        out.println( "<body>" );

        // body section of document
        out.println( "<body>" );
        out.println( "<h1>Hello <span>" + clientName + ",</span><br />" );
        out.println( "Welcome to Servlets!</h1>" );
        out.println( "</body>" );

        // end HTML document
        out.println( "</html>" );
        out.close(); // close stream to complete the page
    } // end method doPost
} // end class WelcomeServlet3

