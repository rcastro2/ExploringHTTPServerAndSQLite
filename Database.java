import java.sql.*;

public class Database{
    private Connection conn;
    
    public Database(String url){
        try{
            //Create a connection to the database
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");    
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
    }
    public String selectData(String sql){
        int pos = sql.toUpperCase().indexOf("FROM");
        String fields[] = sql.substring(7,pos).replaceAll(" ","").split(",");
        
        String build = "[";
        try (Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
                build += "{";
                for(String field: fields){
                    build += "\"" + field + "\":\"" + rs.getString(field) + "\",";
                }
                build = build.substring(0,build.length()-1) + "},";
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return build.substring(0,build.length()-1) + "]";
    }
    public void runQuery(String query){
    
    }
}