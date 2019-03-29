import java.util.HashMap;
import java.util.Map;

class Row{
    private Map<String, String> map = new HashMap<>();
    private String dataRow;
    public Row(String headerRow, String dataRow){
        this.dataRow = dataRow;
        String[] field = headerRow.split(",");
        String[] data = dataRow.split(",");
        for(int pos = 0; pos < field.length; pos++){
            map.put(field[pos],data[pos]);
        }
    }
    public String get(String field){
        return map.get(field);
    }

    public String toString(){
        return dataRow;
    }
}