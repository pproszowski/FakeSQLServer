package com.powder.server.logic;

import org.json.JSONException;
import org.json.JSONObject;

public class Response {
    private boolean isValid;
    private String message;
    private JSONObject jsonObject;

    public Response(){
        isValid = true;
        message = "";
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setJsonObject(JSONObject _jsonObject){
        jsonObject = _jsonObject;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject jsonObjectToReturn = new JSONObject();
        jsonObjectToReturn.put("IsValid", isValid);
        jsonObjectToReturn.put("Message", message);
        jsonObjectToReturn.put("JsonObject", jsonObject);
        return jsonObjectToReturn;
    }
}
