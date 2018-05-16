
package com.powder.server.logic;
import com.powder.server.Exception.CurrentDatabaseNotSetException;
import com.powder.server.Exception.TableAlreadyExistsException;
import com.powder.server.Exception.TableNotFoundException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class DatabaseQuery extends Query{
    private JSONObject query;

    public DatabaseQuery(JSONObject _query){
        query = _query;
    }

    @Override
    public Response execute(Storage storage) throws JSONException, IOException {
        Response response = new Response();
        try {
            String type = query.getString("Operation");
            Database database = storage.getCurrentDatabase();
            switch (type.toLowerCase()) {
                case "addtable":
                    Table table = new Table(query.getJSONObject("Table"));
                    database.addTable(table);
                    table.saveToFile(database.getName());
                    database.saveToFile();
                    response.setMessage("Table " + table.getName() + " has been added");
                    break;
                case "droptable":
                    database.removeTable(query.getString("Name"));
                    database.saveToFile();
                    break;
            }
            response.setValid(true);
        } catch (CurrentDatabaseNotSetException | TableNotFoundException | TableAlreadyExistsException e) {
            response.setValid(false);
            response.setMessage(e.getMessage());
            return response;
        }

        return response;
    }
}
