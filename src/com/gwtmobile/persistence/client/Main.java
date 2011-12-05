package com.gwtmobile.persistence.client;


import com.google.gwt.core.client.EntryPoint;
import com.gwtmobile.persistence.client.ui.MainDesktopView;
import com.gwtmobile.ui.client.page.Page;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Main implements EntryPoint {

    // create main core for MVP ======================================================
    //  create all essential objects for this Application: 
    public void onModuleLoad() {
        
        // implement ClientFactory ===================================================
        ClientFactoryImpl clientFactory = new ClientFactoryImpl();
        
        MainDesktopView mainDesktopView = new MainDesktopView(clientFactory);
        
        Page.load(mainDesktopView);
    }
}