package com.powder.restapi;

import com.powder.server.Exception.BadQueryTypeException;
import com.powder.server.Query;
import com.powder.server.QueryFactory;
import com.powder.server.Response;
import com.powder.server.Storage;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;


@RestController
@RequestMapping("/")
public class OtherController{

    private Storage storage;

    OtherController(){
        try {
            storage = new Storage("test");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public String index(@RequestBody String string) throws JSONException {
        System.out.println(string);
        JSONObject jsonObject = new JSONObject(string);
        Response response = null;
        try {
            Query query = QueryFactory.getInstance().getQuery(jsonObject);
            response = storage.executeQuery(query);
        } catch (BadQueryTypeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response == null ? "ERROR, no response" : response.getMessage();
    }
}
