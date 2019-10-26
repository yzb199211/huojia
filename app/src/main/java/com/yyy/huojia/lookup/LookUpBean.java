package com.yyy.huojia.lookup;

import android.util.Log;

public class LookUpBean {
    private Object id;
    private String name;
    private String link_id;

    public String getId() {
        if (id instanceof Integer || id instanceof Double) {
            Log.e("int", "true");
            double Id = (double) id;
            return (int)Id + "";
        } else {
            return id.toString();
        }
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink_id() {
        return link_id;
    }

    public void setLink_id(String link_id) {
        this.link_id = link_id;
    }
}
