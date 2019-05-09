import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;

import java.net.InetSocketAddress;
import java.util.Map;
import java.sql.*;
//javac -cp sqlite-jdbc-3.23.1.jar; ClassExample.java

public class ClassExample {

    public static void main(String[] args) throws IOException {
        int port = (args.length == 0)?8500:Integer.parseInt(args[0]);
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        
        final Database db = new Database("jdbc:sqlite:chinook.db");
        String query =  "";

        //Routes for dynamic data        
        server.createContext("/customers", new RouteHandler(db,"SELECT CustomerId, LastName, FirstName FROM customers"));

        query = "SELECT invoices.CustomerId, invoices.InvoiceId FROM customers INNER JOIN invoices on customers.CustomerId = invoices.CustomerId";
        server.createContext("/invoices", new RouteHandler(db,query));

        query = "SELECT DISTINCT artists.ArtistId, artists.Name, genres.Name as Genre, genres.GenreId FROM artists " + 
                "INNER JOIN albums ON artists.ArtistId = albums.AlbumId " + 
                "INNER JOIN tracks ON albums.AlbumId = tracks.AlbumId " +
                "INNER JOIN genres ON tracks.GenreId = genres.GenreId " + 
                "ORDER BY genres.Name ASC";
        server.createContext("/artistsbygenre", new RouteHandler(db,query));

        //Routes with parameters
        server.createContext("/postTest", new HttpHandler(){
            public void handle(HttpExchange exchange) throws IOException {
					Map<String, Object> parameters = RouteHandler.parseParameters("post",exchange);
					
                    System.out.println("Info: " + parameters.get("id"));
					String response = "Info: " + parameters.get("id");

                    RouteHandler.send(response,exchange);
            }
        });
        server.createContext("/getCustomer", new HttpHandler(){
            public void handle(HttpExchange exchange) throws IOException {
					Map<String, Object> parameters = RouteHandler.parseParameters("get",exchange);

					String id = parameters.get("id").toString();
                    String query = "SELECT * FROM customers WHERE CustomerID = " + id;
                    String response = db.selectData(query);
					
                    RouteHandler.send(response,exchange);
            }
        });
        server.createContext("/tracksbyartist", new HttpHandler(){
            public void handle(HttpExchange exchange) throws IOException {
					Map<String, Object> parameters = RouteHandler.parseParameters("get",exchange);
                   
					String id = parameters.get("id").toString();
                    String query = "SELECT * FROM albums " + 
                                   "INNER JOIN tracks ON albums.AlbumId = tracks.AlbumId " +
                                   "WHERE artistId = " + id;
                    String response = db.selectData(query);
					
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