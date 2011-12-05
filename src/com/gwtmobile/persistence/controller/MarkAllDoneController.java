package com.gwtmobile.persistence.controller;

import java.util.Date;
import java.util.Iterator;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;


import com.google.appengine.repackaged.org.json.JSONArray;
import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.appengine.repackaged.org.json.JSONStringer;
import com.gwtmobile.persistence.meta.ItemMeta;
import com.gwtmobile.persistence.model.Item;
import com.gwtmobile.persistence.model.Task;


public class MarkAllDoneController extends Controller {

    @Override
    public Navigation run() throws Exception {

        ItemMeta meta = ItemMeta.get();
        
        // adding a new Item =================
        Item item = new Item();
        item.setName("Create fr Server 1");
        Datastore.put(item);
        
        // modify the existing Item =================
        /*Iterator<Item> itemIterator =
            Datastore
                .query(meta)
                .asList()
                .iterator();

//        while (itemIterator.hasNext()) {
            Item item = (Item) itemIterator.next();
            item.setName(item.getName()+" Server");
            Datastore.put(item);
//        }*/

        /*Iterator<Task> iterator = Datastore.query(Task.class).asList().iterator();
        while(iterator.hasNext()){
            Task task = (Task) iterator.next(); 
            task.setDone(true);
            Datastore.put(task);
        }*/
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new JSONObject().put("status", "ok").toString());
        
        return null;
    }
}
