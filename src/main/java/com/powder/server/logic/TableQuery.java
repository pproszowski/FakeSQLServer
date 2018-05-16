package com.powder.server.logic;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.powder.server.Exception.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableQuery extends Query{
    private JSONObject query;

    public TableQuery(JSONObject _query){
        query = _query;
    }

    @Override
    public Response execute(Storage storage) throws JSONException{
        Response response = new Response();
        try {
            String type = query.getString("Operation");
            Database database = storage.getCurrentDatabase();
            Table table = database.getTable(query.getString("Name"));
            int howMany;
            List<Condition> conditions;
            switch(type.toLowerCase()){
                case "select":
                        JSONArray columns = query.getJSONArray("ColumnNames");
                        List<String> columnNames = new ArrayList<>();
                        for(int i = 0; i < columns.length(); i++){
                            columnNames.add(columns.getString(i));
                        }
                        conditions = Condition.extractConditionsFromJSONArray(query.getJSONArray("Conditions"));

                        if(columnNames.size() == 1){
                            if(columnNames.get(0).equals("*")){
                                response.setJsonObject(table.selectAll(conditions).toJSON());
                            }else{
                                response.setJsonObject(table.select(columnNames, conditions).toJSON());
                            }
                        }else{
                            response.setJsonObject(table.select(columnNames, conditions).toJSON());
                        }
                    break;
                case "insert":
                   table.insert(new Record(query.getJSONObject("Record")));
                   table.saveToFile(storage.getCurrentDatabase().getName());
                   response.setMessage("Record has been inserted properly");
                    break;
                case "delete":
                    conditions = Condition.extractConditionsFromJSONArray(query.getJSONArray("Conditions"));
                    howMany = table.delete(conditions);
                    table.saveToFile(storage.getCurrentDatabase().getName());
                    response.setMessage("(" + howMany + ") rows has been removed");
                    break;
                case "update":
                    Map<String, Tuple> changes = new Gson().fromJson(
                            query.getJSONObject("Changes").toString(), new TypeToken<HashMap<String, Tuple>>(){}.getType()
                    );
                    conditions = Condition.extractConditionsFromJSONArray(query.getJSONArray("Conditions"));
                    howMany = table.update(conditions, changes);
                    table.saveToFile(storage.getCurrentDatabase().getName());
                    response.setMessage("(" + howMany + ") records have been affected");
                    break;
            }
            response.setValid(true);
        } catch (FakeSQLException e) {
            response.setValid(false);
            response.setMessage(e.getReason());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

}
