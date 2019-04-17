import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;

import java.net.InetSocketAddress;
import java.util.Map;
import java.sql.*;
//javac -cp sqlite-jdbc-3.23.1.jar; BasicHttpServerExample.java

public class BasicHttpServerExplore {

    public static void main(String[] args) throws IOException {
        int port = (args.length == 0)?8500:Integer.parseInt(args[0]);
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        
        final Database db = new Database("jdbc:sqlite:chinook.db");

       //No frills simple text routes
        server.createContext("/", new RouteHandler("Hi Boss"));

        //Routes for HMTL templates
        String template = Input.readFile("test.html");
        server.createContext("/test", new RouteHandler(template));

        template = Input.readFile("retrieveCustomers.html");
        server.createContext("/retrieveCustomers", new RouteHandler(template));

        //Routes for static data from database
        //db.runQuery("INSERT INTO customers(CustomerId,FirstName,Lastname,Email) Values (1234,'Renne','Castro','r@aol.com')");
        String data = db.selectData("SELECT CustomerId, LastName FROM customers");
        server.createContext("/customers", new RouteHandler(data));
        
        data = db.selectData("SELECT ArtistId, Name FROM artists");
        server.createContext("/artists", new RouteHandler(data));

        data = db.selectData("SELECT invoices.CustomerId, invoices.InvoiceId FROM customers INNER JOIN invoices on customers.CustomerId = invoices.CustomerId;");
        server.createContext("/invoices", new RouteHandler(data));

        //Routes for live data from database
        server.createContext("/livecustomers", new RouteHandler(db,"SELECT CustomerId, LastName FROM customers"));

        //More advance examples
        server.createContext("/livecustomers2", new HttpHandler(){
            public void handle(HttpExchange exchange) throws IOException {
                String response = db.selectData("SELECT CustomerId, FirstName, LastName FROM customers");
                RouteHandler.send(response,exchange);
            }
        });
        server.createContext("/postTest", new HttpHandler(){
            public void handle(HttpExchange exchange) throws IOException {
					Map<String, Object> parameters = RouteHandler.parseParameters("post",exchange);
					
                    System.out.println("Info: " + parameters.get("id"));
					String response = "Info: " + parameters.get("id");

                    RouteHandler.send(response,exchange);
            }
        });
        server.createContext("/getTest", new HttpHandler(){
            public void handle(HttpExchange exchange) throws IOException {
					Map<String, Object> parameters = RouteHandler.parseParameters("get",exchange);
					
                    System.out.println("Info: " + parameters.get("data"));
					String response = "Info: " + parameters.get("data");
					
                    RouteHandler.send(response,exchange);
            }
        });


        server.start(); 
        System.out.println("Server is listening on port " + port );
    }    

}
/*
Resources
https://www.codeproject.com/Tips/1040097/Create-a-Simple-Web-Server-in-Java-HTTP-Server
https://www.programcreek.com/java-api-examples/?api=com.sun.net.httpserver.HttpHandler

*/