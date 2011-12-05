/**
 * 
 */
package com.gwtmobile.persistence.client.ui;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
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
import com.gwtmobile.persistence.client.domain.MNTTag;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.Button;
import com.gwtmobile.ui.client.widgets.TextArea;

/**
 * Display the Main View 
 * 
 * @author yan
 *
 */
public class ResetDbDesktopView extends Page {

	interface ResetDbDesktopViewUiBinder extends UiBinder<HTMLPanel, ResetDbDesktopView> {}
	private static ResetDbDesktopViewUiBinder uiBinder = GWT.create(ResetDbDesktopViewUiBinder.class);
	
    @UiField Button resetServerDb;
    @UiField Button resetLocalDb;
    @UiField TextArea msg;
    
    @UiField Button testButton;
    
    static int cnt = 0;
	
	private ClientFactoryImpl clientFactory;

	/**
	 * prepare main UI of Tag Attach Items
	 */
	public ResetDbDesktopView(ClientFactoryImpl clientFactory) {

		// set the clientFactory for most of common objects
		this.clientFactory = clientFactory;
		
	    // init. this View, All composites must call initWidget() in their constructors.
		//	and must put at the end before setup all above
	    // However, any object(s) created for uiBinder is worked in initWidget(), so 
	    //	using these kind of objects after initWidget()
	    initWidget(uiBinder.createAndBindUi(this));

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
                
                Persistence.reset();                            // drop all tables
                
                Persistence.schemaSync(new Callback() {         // synchronized (activated) with the database and defined entity
                    
//                  @Override
                    public void onSuccess() {
                        msg.setText("reset local db completed");
                    }
                });
                
            }
            
    }
        
    @UiHandler("testButton")
    void onButtonTestButtonClicked(ClickEvent event) {
        String URL = GWT.getHostPageBaseURL() + "MarkAllDone";
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL);
        
        try {
            Request request = builder.sendRequest(null, new RequestCallback() {
                
                @Override
                public void onResponseReceived(Request request, Response response) {
                    Window.alert("Complete Testing Http !!");
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    Window.alert("Err in Testing Http !!");
                }
            });
            
          } catch (RequestException e) {
              Window.alert("Exception in Testing Http !!");
          }
    }
    
}
