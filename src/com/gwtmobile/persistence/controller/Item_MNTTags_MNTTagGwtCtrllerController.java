package com.gwtmobile.persistence.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Iterator;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

import com.google.appengine.repackaged.org.json.JSONArray;
import com.google.appengine.repackaged.org.json.JSONStringer;
import com.gwtmobile.persistence.meta.Item_MNTTags_MNTTagGwtMeta;
import com.gwtmobile.persistence.model.Item_MNTTags_MNTTagGwt;


public class Item_MNTTags_MNTTagGwtCtrllerController extends Controller {

    @Override
    public Navigation run() throws Exception {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Item_MNTTags_MNTTagGwtMeta meta = Item_MNTTags_MNTTagGwtMeta.get();
        if (isGet()) {
            Iterator<Item_MNTTags_MNTTagGwt> mIterator =
                Datastore
                    .query(meta)
                    .filter(meta.lastChange.greaterThan(asLong("since")))
                    .asList()
                    .iterator();

            JSONArray arr = new JSONArray();

            while (mIterator.hasNext()) {
                arr.put(meta.modelToJSON(mIterator.next()));
            }

            response.getWriter().write(
                new JSONStringer()
                    .object()
                    .key("now")
                    .value(new Date().getTime())
                    .key("updates")
                    .value(arr)
                    .endObject()
                    .toString());
        } else if (isPost()) {
            BufferedReader input =
                new BufferedReader(new InputStreamReader(
                    request.getInputStream()));
            String str = "";
            for (String line; (line = input.readLine()) != null; str += line);
            input.close();

            JSONArray arr = new JSONArray(str);
            long timestamp = new Date().getTime();

            for (int i = 0; i < arr.length(); i++) {
                Item_MNTTags_MNTTagGwt m = meta.JSONtoModel(
                    arr.getJSONObject(i), timestamp);
                m.setSyncDirty(true);
                Datastore.put(m);
                m.setSyncDirty(false);
            }

            response.getWriter().write(
                new JSONStringer()
                    .object()
                    .key("status")
                    .value("ok")
                    .key("now")
                    .value(new Date().getTime())
                    .endObject()
                    .toString());
        }

        return null;
    }
}
