import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;

import java.net.InetSocketAddress;
import java.util.Map;
import java.sql.*;
//javac HttpExample.java

public class HttpExplore{

    public static void main(String[] args) throws IOException {
        int port = (args.length == 0)?8500:Integer.parseInt(args[0]);
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
                
        server.createContext("/", new RouteHandler("Hi Boss"));

        String html = "<html><body><marquee>Rolling down the river</marquee></body></hmtl>";
        server.createContext("/message", new RouteHandler(html));

        html = Input.readFile("sample.html");
        server.createContext("/sample", new RouteHandler(html));

        server.start(); 
        System.out.println("Server is listening on port " + port );
    }    

}
/*
Resources
https://www.codeproject.com/Tips/1040097/Create-a-Simple-Web-Server-in-Java-HTTP-Server
https://www.programcreek.com/java-api-examples/?api=com.sun.net.httpserver.HttpHandler

*/