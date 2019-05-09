
import java.io.IOException;

import java.sql.*;
//javac -cp sqlite-jdbc-3.23.1.jar; DatabaseExplore.java

public class DatabaseExplore {

    public static void main(String[] args) throws IOException {
        
        Database db = new Database("jdbc:sqlite:chinook.db");

        String result = db.selectData("SELECT * FROM customers");
        System.out.println("Example 1:\n" + result);

        String query = "SELECT * FROM invoices WHERE CustomerId = 2"; + 
        result = db.selectData(query);
        System.out.println("\nExample 2:\n" + result);

        String query = "SELECT DISTINCT artists.ArtistId, artists.Name, genres.Name as Genre, genres.GenreId FROM artists " + 
                "INNER JOIN albums ON artists.ArtistId = albums.AlbumId " + 
                "INNER JOIN tracks ON albums.AlbumId = tracks.AlbumId " +
                "INNER JOIN genres ON tracks.GenreId = genres.GenreId " + 
                "ORDER BY genres.Name ASC";
        result = db.selectData(query);
        System.out.println("\nExample 3:\n" + result);

       
    }    

}
