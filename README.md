# DistributedWebApp

Three-Tier Distributed Web-Based Application. Uses servlets running on a Tomcat server to access and maintain a persistent MySQL database using JDBC.

### First-tier

The client-level front-end of the application consists of a JSP page that allows the client to enter SQL commands into a window and submit them to the server application for processing. 

The client front-end can run on any web browser, the contents of the page consist of an input are where the user can enter SQL commands and execute them. The results get returned and displayed for the user. 


### Second-tier

The second-tier servlet, in addition to handling the SQL command interface it also implements the business aplication logic. 


### Third-tier

The back-end consists of a persistent MySQL database under control of the MySQL DBMS server.

