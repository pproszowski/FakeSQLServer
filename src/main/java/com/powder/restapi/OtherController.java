package com.powder.restapi;

import com.powder.server.Exception.BadQueryTypeException;
import com.powder.server.Exception.CurrentDatabaseNotSetException;
import com.powder.server.Query;
import com.powder.server.QueryFactory;
import com.powder.server.Response;
import com.powder.server.Storage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.io.*;


@RestController
@RequestMapping("/")
public class OtherController{

    private Storage storage;

    OtherController(){
        storage = Storage.getInstance();
    }

    @RequestMapping(value = "/", produces = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public String index(@RequestBody String string) throws JSONException {
        JSONObject jsonObject;
        Response response = new Response();
        try {
            jsonObject = new JSONObject(string);
        }catch (JSONException e){
            return e.getMessage();
        }

        switch (jsonObject.getString("Type")){
            case "Authentication":
                response = authenticate(jsonObject);
                break;
            case "Update":
                response = update();
                break;
            case "Query":
                response = executeQuery(jsonObject);
                break;
            default:
                response.setValid(false);
                break;
        }


        return response.toJson().toString();
    }

    private Response executeQuery(JSONObject jsonObject) {
        Response response = null;
        try {
            Query query = QueryFactory.getInstance().getQuery(jsonObject);
            response = storage.executeQuery(query);
        } catch (BadQueryTypeException | IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

    private Response update() {
        Response response = new Response();
        try {
            JSONArray databaseNames = storage.toJSON().getJSONArray("DatabaseNames");
            JSONArray tableNames = storage.getCurrentDatabase().toJson().getJSONArray("Tables");
            JSONObject jsonResponse = new JSONObject()
                                        .put("DatabaseNames", databaseNames)
                                        .put("TableNames", tableNames);
            response.setValid(true);
            response.setJsonObject(jsonResponse);
            return response;
        } catch (JSONException | CurrentDatabaseNotSetException e) {
            e.printStackTrace();
            response.setMessage(e.getMessage());
        }
        response.setValid(false);
        return response;
    }

    private Response authenticate(JSONObject jsonObject) {
        try{
            if(jsonObject.getString("password").equals("aaa")){
                Response _response = new Response();
                _response.setJsonObject(storage.toJSON());
                _response.setValid(true);
                return _response;
            }else{
                Response _response = new Response();
                _response.setJsonObject(new JSONObject());
                _response.setValid(false);
                return _response;
            }
        }catch(JSONException e){
        }

        return new Response();
    }

    @RequestMapping(value = "/test", produces = "application/json")
    @ResponseBody
    public String test() throws JSONException {
        JSONObject result = new JSONObject()
                .put("name", "Dade")
                .put("age", 23)
                .put("married", false);
        return result.toString();
    }
}
