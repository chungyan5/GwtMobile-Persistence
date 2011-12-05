/**
 * 
 */
package com.gwtmobile.persistence.client.ui;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.gwtmobile.persistence.client.Callback;
import com.gwtmobile.persistence.client.ClientFactoryImpl;
import com.gwtmobile.persistence.client.Persistence;
import com.gwtmobile.persistence.client.PersistenceSync;
import com.gwtmobile.persistence.client.ScalarCallback;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.Button;
import com.gwtmobile.ui.client.widgets.TextArea;

/**
 * Display the Main View 
 * 
 * @author yan
 *
 */
public class ConflictDesktopView extends Page {

	interface ConflictViewUiBinder extends UiBinder<HTMLPanel, ConflictDesktopView> {}
	private static ConflictViewUiBinder uiBinder = GWT.create(ConflictViewUiBinder.class);
	
    @UiField Button resetServerDb;
    @UiField Button resetLocalDb;
    @UiField TextArea msg;
    
    static int cnt = 0;
	
	private ClientFactoryImpl clientFactory;

	/**
	 * prepare main UI of Tag Attach Items
	 */
	public ConflictDesktopView(ClientFactoryImpl clientFactory, String conflicts, String updatesToPush) {

		// set the clientFactory for most of common objects
		this.clientFactory = clientFactory;
		
	    // init. this View, All composites must call initWidget() in their constructors.
		//	and must put at the end before setup all above
	    // However, any object(s) created for uiBinder is worked in initWidget(), so 
	    //	using these kind of objects after initWidget()
	    initWidget(uiBinder.createAndBindUi(this));

        msg.setText("conflicts: " + conflicts + " updatesToPush: " + updatesToPush);
	}
	
    @UiHandler("resetServerDb")
    void onButtonResetServerDbClicked(ClickEvent event) {

//      let user to confirm ========            
            if ( Window.confirm("Pls Confirm to reset Server Database") ) {
                
                PersistenceSync.getJSON("/reset", new ScalarCallback<String>() {

                    public void onSuccess(String result) {
                        msg.setText("reset server db completed: " + result + " " + cnt++);
                    }

                });

            }
            
    }
        
    @UiHandler("resetLocalDb")
    void onButtonResetLocalDbClicked(ClickEvent event) {

//      let user to confirm ========            
            if ( Window.confirm("Pls Confirm to reset Local Database") ) {
                
                Persistence.reset();                // drop all tables
                
                msg.setText("reset local db completed");
            }
            
    }
        
}
