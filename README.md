# DistributedWebApp

Three-Tier Distributed Web-Based Application. Uses servlets running on a Tomcat server to access and maintain a persistent MySQL database using JDBC.

## Query Screenshots

![](http://www.easymachinelearning.net/GitHub/command1.PNG)

![](http://www.easymachinelearning.net/GitHub/command2a.PNG)

## Architecture

![](http://www.easymachinelearning.net/GitHub/arch_diagram.png)


### First Tier

The client-level front-end of the application consists of a JSP page that allows the client to enter SQL commands into a window and submit them to the server application for processing. 

The client front-end can run on any web browser, the contents of the page consist of an input are where the user can enter SQL commands and execute them. The results get returned and displayed for the user. 


### Second Tier

The second-tier servlet, in addition to handling the SQL command interface it also implements the business aplication logic. 


#### Fictitious Business Logic

This logic will increment by 5, the status of a supplier anytime that supplier is involved in the insertion/update of a shipment record in which the quantity is greater than or equal to 100. 

Note  that  any  update  of  quantity  >=  100  will  affect  any  supplier  involved  in  a  shipment  with  a  
quantity >= 100. 

The example screen shots illustrate this case.  An insert of a shipment tuple (S5, P6, J7, 400) will cause the status of every supplier who has a shipment with a quantity of 100 or greater to be increased by 5.
  
In other words, even if a supplier’s shipment is not directly affected by the update, 
their status will be affected if they have any shipment with quantity >= 100. (See page 9 for a bonus problem  that  implements a modified version of this business  rule.) The business logic of the second tier will reside in the servlet on the Tomcat web-application server (server-side application). This means that the business logic is not to be implemented in the DBMS via a trigger.

### Third Tier

The back-end consists of a persistent MySQL database under control of the MySQL DBMS server.

#### MySQL DBMS

![](http://www.easymachinelearning.net/GitHub/DBconnection.PNG)

## Usage

Application runs on Apache Tomcat/9.0.13 server, simply place the DistributedWebApp in `C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps\` 

Connection to DB uses "root" for username, and password as default

The SQL script populates a sample database that follows the schema of the fictitious business dealing with shipments and suppliers.

### Sample Queries

```sql
# Query 1: list the supplier number and supplier name for those suppliers who ship every part.
select snum, sname from suppliers
where not exists 
    ( select *from partswhere not exists
        ( select * from shipments where shipments.snum = suppliers.snum and shipments.pnum = parts.pnum));
 ```

```sql
# Query 2: Insert new entry into suppliers
insert into suppliers values ('S39','Candice Swanepoel',10,'Cardiff')
```

```sql
# Query 3: Increase the quantity of all shipments with part P3 by 10
update shipments 
set quantity = quantity + 10
where pnum = 'P3';
```

```sql
# Query 4: Get all entries in suppliers table
select * from suppliers;
```

![](http://www.easymachinelearning.net/GitHub/selectq.PNG)
Query 4
