import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import java.sql.*;
//javac -cp sqlite-jdbc-3.23.1.jar; BasicHttpServerExample.java

public class BasicHttpServerExample {

    public static void main(String[] args) throws IOException {
        int port = (args.length == 0)?8500:Integer.parseInt(args[0]);
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        
        Database db = new Database("jdbc:sqlite:chinook.db");

        //Routes for JSON created data from database
        String data = db.selectData("SELECT CustomerId, LastName FROM customers");
        server.createContext("/data", new RouteHandler(data));

        data = db.selectData("SELECT ArtistId, Name FROM artists");
        server.createContext("/artists", new RouteHandler(data));

       
        server.start(); 
        System.out.println("Server is listening on port " + port );
    }    
}