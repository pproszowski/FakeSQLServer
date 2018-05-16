
package com.powder.server.logic;
import org.json.JSONException;

import java.io.IOException;

public abstract class Query {
    public abstract Response execute(Storage storage) throws JSONException, IOException;
}
