package com.gwtmobile.persistence.controller;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;


import com.google.appengine.repackaged.org.json.JSONObject;
import com.gwtmobile.persistence.model.Item;
import com.gwtmobile.persistence.model.Item2MntTagTable;
import com.gwtmobile.persistence.model.MNTTag;
import com.gwtmobile.persistence.model.Project;
import com.gwtmobile.persistence.model.Tag;
import com.gwtmobile.persistence.model.Task;


public class ResetController extends Controller {

    @Override
    public Navigation run() throws Exception {
        
        // remove all old records ======================
        Datastore.delete(Datastore.query(Project.class).asKeyList());
        Datastore.delete(Datastore.query(Tag.class).asKeyList());
        Datastore.delete(Datastore.query(Task.class).asKeyList());
        Datastore.delete(Datastore.query(MNTTag.class).asKeyList());
        Datastore.delete(Datastore.query(Item.class).asKeyList());
        Datastore.delete(Datastore.query(Item2MntTagTable.class).asKeyList());
        
        
        /*Project project = new Project();
        project.setName("Main project");
        for (int i = 0; i < 25; i++) {
            Task task = new Task();
            task.setName("Task "+ i);
            task.setDone(false);
            task.getProjectRef().setModel(project);
            Datastore.put(task);
        }
        Datastore.put(project);*/

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new JSONObject().put("status", "ok").toString());

        return null;
    }
}