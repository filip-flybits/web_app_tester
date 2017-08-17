package com.flybits.webapptester.models;

/**
 * Created by Fil on 8/17/2017.
 */

public class JSMessage {

    private long timestamp;
    private String json;

    public JSMessage(long time, String json)
    {
        this.timestamp = time;
        this.json = json;
    }

    public long getTime()
    {
        return timestamp;
    }

    public String getJson()
    {
        return json;
    }

}
