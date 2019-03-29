import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.Headers;
import java.io.OutputStream;
import java.io.IOException;

public class RouteHandler implements HttpHandler {
    private String response;
    private String contentType;

    public RouteHandler(String response){
        this.response = response;
        this.contentType = "text/html";
    }
    public RouteHandler(String response, String contentType){
        this.response = response;
        this.contentType = contentType;
    }
    public void handle(HttpExchange exchange) throws IOException {
      //Headers h = exchange.getResponseHeaders();
      //h.add( "Content-Type", this.contentType );
      exchange.sendResponseHeaders(200, response.length());
      OutputStream os = exchange.getResponseBody();
      os.write(response.getBytes());
      os.close();
    }
}