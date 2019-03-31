import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import java.sql.*;
//javac -cp sqlite-jdbc-3.23.1.jar; BasicHttpServerExample.java

public class BasicHttpServerExample {

    public static void main(String[] args) throws IOException {
        int port = (args.length == 0)?8500:Integer.parseInt(args[0]);
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        
        final Database db = new Database("jdbc:sqlite:chinook.db");

       //No frills simple text routes
        server.createContext("/", new RouteHandler("Hi Boss"));

        //Routes for HMTL templates
        String template = Input.readFile("test.html");
        server.createContext("/test", new RouteHandler(template));

        template = Input.readFile("sqltest.html");
        server.createContext("/sqltest", new RouteHandler(template));

        //Routes for static data from database
        //db.runQuery("INSERT INTO customers(CustomerId,FirstName,Lastname,Email) Values (1234,'Renne','Castro','r@aol.com')");
        String data = db.selectData("SELECT CustomerId, LastName FROM customers");
        server.createContext("/customers", new RouteHandler(data));
        
        data = db.selectData("SELECT ArtistId, Name FROM artists");
        server.createContext("/artists", new RouteHandler(data));

        //Routes for live data from database
        server.createContext("/livecustomers", new RouteHandler(db,"SELECT CustomerId, LastName FROM customers"));

        //More advance examples
        server.createContext("/customers2", new HttpHandler(){
            public void handle(HttpExchange exchange) throws IOException {
                String response = db.selectData("SELECT CustomerId, LastName FROM customers");
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        });
        server.createContext("/postTest", new HttpHandler(){
            public void handle(HttpExchange exchange) throws IOException {
                    // parse request
                    Map<String, Object> parameters = new HashMap<String, Object>();
                    InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
                    BufferedReader br = new BufferedReader(isr);
                    String response = br.readLine();
                    System.out.println("Info: " + response);
                    exchange.sendResponseHeaders(200, response.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.toString().getBytes());
                    os.close();
            }
        });
        server.createContext("/getTest", new HttpHandler(){
            public void handle(HttpExchange exchange) throws IOException {
                    // parse request
                    Map<String, Object> parameters = new HashMap<String, Object>();
                    URI requestedUri = exchange.getRequestURI();
                    String response = requestedUri.getRawQuery();
                    System.out.println("Info: " + response);
                    exchange.sendResponseHeaders(200, response.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.toString().getBytes());
                    os.close();
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