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
    private Database db;
    private String sql;

    public RouteHandler(String response){
        this.response = response;
        this.contentType = "text/html";
    }
    public RouteHandler(String response, String contentType){
        this.response = response;
        this.contentType = contentType;
    }
    public RouteHandler(Database db, String sql){
        this.db = db;
        this.sql = sql;
        this.contentType = "text/json";
    }
    public void handle(HttpExchange exchange) throws IOException {
      if(this.contentType.equals("text/json")){
          this.response = this.db.selectData(sql);
      }
      exchange.getResponseHeaders().add( "Content-Type", this.contentType );
      exchange.getResponseHeaders().add("Access-Control-Allow-Origin","*");
      exchange.sendResponseHeaders(200, response.length());
      OutputStream os = exchange.getResponseBody();
      os.write(response.getBytes());
      os.close();
    }
}