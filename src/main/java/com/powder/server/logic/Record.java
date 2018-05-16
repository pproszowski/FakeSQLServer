package com.powder.server.logic;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.powder.server.Exception.ColumnNotFoundException;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class Record {
    private Map<String, Tuple> values;

    public Record(Map<String, Tuple> _values){
        values = new HashMap<>();
        for(Map.Entry<String, Tuple> entry : _values.entrySet()){
            values.put(entry.getKey().toLowerCase(), entry.getValue());
        }
    }

    public Record(Record _record){
        this.values = new HashMap<>(_record.values);
    }

    public Record(JSONObject record) {
        values = new Gson().fromJson(
                record.toString(), new TypeToken<HashMap<String, Tuple>>(){}.getType()
        );
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Record){
            Record _record = new Record((Record) obj);

            if(values.size() != ((Record) obj).values.size()){
                return false;
            }

            for(Map.Entry<String, Tuple> entry : values.entrySet()){
                if(!_record.getValues().containsKey(entry.getKey())){
                    return false;
                }
                if(!_record.getValues().containsValue(entry.getValue())){
                    return false;
                }
            }
            return true;
        }else{
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }

    public Record getRecordWithOnlySpecifiedColumns(List<String> whichColumns, String tableName) throws ColumnNotFoundException {
        Map<String, Tuple> newValues = new HashMap<>();

        for(String columnName : whichColumns){
            if(values.keySet().contains(columnName.toLowerCase())){
                newValues.put(columnName, values.get(columnName.toLowerCase()));
            }else{
                throw new ColumnNotFoundException(columnName, tableName);
            }
        }

        return new Record(newValues);
    }

    public Map<String, Tuple> getValues(){
        return values;
    }

    @Override
    public String toString() {
        StringBuilder sB = new StringBuilder();
        for(Map.Entry<String, Tuple> entry : values.entrySet()){
            sB.append(entry.getValue().toString()).append(" ").append("\n");
        }

        return sB.toString();
    }

    public Tuple getValueFromColumn(String columnName, String tableName) throws ColumnNotFoundException {
        for(Map.Entry<String, Tuple> entry : values.entrySet()){
            if(entry.getKey().equalsIgnoreCase(columnName)){
                return entry.getValue();
            }
        }
        throw new ColumnNotFoundException(columnName, tableName);
    }

    public boolean update(String whichColumn, Tuple newValue){
        if(values.keySet().contains(whichColumn) && !values.get(whichColumn.toLowerCase()).equals(newValue)){
            values.remove(whichColumn.toLowerCase());
            values.put(whichColumn.toLowerCase(), newValue);
            return true;
        }else{
            return false;
        }
    }

    public boolean meetConditions(List<Condition> conditions) {
        for(Condition condition : conditions){
            for(Map.Entry entry : values.entrySet()){
                String columnName = (String) entry.getKey();
                Tuple tuple = (Tuple) entry.getValue();
                if(columnName.equalsIgnoreCase(condition.getColumnName())){
                    if(condition.getResult(tuple)){
                        if( condition.getConnector().equals("null") || condition.getConnector().equalsIgnoreCase("OR")){
                            return true;
                        }
                    }else{
                        if(condition.getConnector().equals("null") || condition.getConnector().equalsIgnoreCase("AND")){
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }

    public JSONObject toJSON() throws JSONException {
        return new JSONObject().put("Values", values);
    }
}
