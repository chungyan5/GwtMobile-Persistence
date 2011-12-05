package com.gwtmobile.persistence.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Iterator;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.repackaged.org.json.JSONArray;
import com.google.appengine.repackaged.org.json.JSONStringer;
import com.gwtmobile.persistence.meta.MNTTagMeta;
import com.gwtmobile.persistence.model.MNTTag;

public class MNTTagUpdatesController extends Controller {

    @Override
    public Navigation run() throws Exception {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        MNTTagMeta meta = MNTTagMeta.get();
        if (isGet()) {
            // after get the last sync time bet server and that client, check any update data in this server, then send to client,
            //  eg. return {"now":1322885578681,"updates":[]}
            Iterator<MNTTag> mntTagIterator =
                Datastore
                    .query(meta)
                    .filter(meta.lastChange.greaterThan(asLong("since")))
                    .asList()
                    .iterator();

            JSONArray arr = new JSONArray();

            while (mntTagIterator.hasNext()) {
                arr.put(meta.modelToJSON(mntTagIterator.next()));
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
                
                MNTTag mntTag = meta.JSONtoModel(arr.getJSONObject(i), timestamp);
                
                // handle removed stuffs at Client side ====================
                if (arr.getJSONObject(i).has("_removed")) {     
                    
                //      matching the Stuffs ID and get back this object
                    Key key = mntTag.getKey();
                    
                //      remove this object from DataStore
                    Datastore.delete(key);

                // any new stuffs update ===================================
                } else {                                        
                    mntTag.setSyncDirty(true);
                    Datastore.put(mntTag);
                    mntTag.setSyncDirty(false);
                }
                
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
