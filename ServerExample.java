import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class ServerExample {

    public static void main(String[] args) throws IOException {
        int port = 8500;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        
      
        server.createContext("/", new RouteHandler("Main"));
        server.createContext("/hello", new RouteHandler("Hello"));
        server.createContext("/goodbye", new RouteHandler("Good Bye"));

        String html = "<html><body><h2>Ouch</h2></body></html>";
        server.createContext("/punch", new RouteHandler(html));

        html = Input.readFile("samplePage.html");
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