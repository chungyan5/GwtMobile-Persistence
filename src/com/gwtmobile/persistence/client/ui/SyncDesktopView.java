/**
 * 
 */
package com.gwtmobile.persistence.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.gwtmobile.persistence.client.Callback;
import com.gwtmobile.persistence.client.ClientFactoryImpl;
import com.gwtmobile.persistence.client.ConflictCallback;
import com.gwtmobile.persistence.client.Persistence;
import com.gwtmobile.persistence.client.ScalarCallback;
import com.gwtmobile.persistence.client.domain.Item;
import com.gwtmobile.persistence.client.domain.Item2MntTagTable;
import com.gwtmobile.persistence.client.domain.MNTTag;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.page.Transition;
import com.gwtmobile.ui.client.widgets.Button;
import com.gwtmobile.ui.client.widgets.TextArea;


/**
 * Display the Main View 
 * 
 * @author yan
 *
 */
public class SyncDesktopView extends Page {

	interface SyncDesktopUiBinder extends UiBinder<HTMLPanel, SyncDesktopView> {}
	private static SyncDesktopUiBinder uiBinder = GWT.create(SyncDesktopUiBinder.class);
	
    @UiField Button sync;
    @UiField Button syncFrClient2Server;
    @UiField TextArea msg;
    
	private ClientFactoryImpl clientFactory;

    private boolean conflictFlag = false;
    
    private MainDesktopView mainDesktopView;
    
	/**
	 * prepare main UI of Tag Attach Items
	 */
	public SyncDesktopView(ClientFactoryImpl clientFactory, MainDesktopView mainDesktopView) {

		// set the clientFactory for most of common objects
		this.clientFactory = clientFactory;
		
		this.mainDesktopView = mainDesktopView;
		
	    // init. this View, All composites must call initWidget() in their constructors.
		//	and must put at the end before setup all above
	    // However, any object(s) created for uiBinder is worked in initWidget(), so 
	    //	using these kind of objects after initWidget()
	    initWidget(uiBinder.createAndBindUi(this));

	}
	
	void sync() {
	    
        if (clientFactory != null) {
            
            // sync Tag ============================
            clientFactory.getDbconn().getTagEntity().syncAll(new ConflictCallback() {

                @Override
//                public void onSuccess(JSONArray resultJson) {
                public void onSuccess(String[] resultStrArray) {
                    Window.alert("Conflict in Sync Tag 1 !!");
                    conflictFlag = true;
                    handleConflictScreen(resultStrArray[0] + " " + resultStrArray[1], null);
                }
            },
            
            new Callback() {

                public void onSuccess() {
                    
                    if (conflictFlag==false) clientFactory.getDbconn().getTagEntity().syncAll(new ConflictCallback() {

                        @Override
//                      public void onSuccess(JSONArray resultJson) {
                        public void onSuccess(String[] resultStrArray) {
                            Window.alert("Conflict in Sync Tag 2 !!");
                            conflictFlag = true;
                            handleConflictScreen(resultStrArray[0] + " " + resultStrArray[1], null);
                        }
                    }, 
                    
                    new Callback() {

                        public void onSuccess() {
                            Window.alert("Sync Tag completed !!");
                            
            // sync Item ============================
                            if (conflictFlag==false) clientFactory.getDbconn().getItemEntity().syncAll(new ConflictCallback() {

                                    @Override
//                                  public void onSuccess(JSONArray resultJson) {
                                    public void onSuccess(String[] resultStrArray) {
                                        Window.alert("Conflict in Sync Item 1 !!");
                                        conflictFlag = true;
                                        handleConflictScreen(resultStrArray[0] + " " + resultStrArray[1], null);
                                    }
                                },
                                
                                new Callback() {

                                    public void onSuccess() {
                                        
                                        if (conflictFlag==false) clientFactory.getDbconn().getItemEntity().syncAll(new ConflictCallback() {

                                            @Override
//                                              public void onSuccess(JSONArray resultJson) {
                                            public void onSuccess(String[] resultStrArray) {
                                                Window.alert("Conflict in Sync Item 2 !!");
                                                conflictFlag = true;
                                                handleConflictScreen(resultStrArray[0] + " " + resultStrArray[1], null);
                                            }
                                        }, 
                                        
                                        new Callback() {

                                            public void onSuccess() {
                                                Window.alert("Sync Item completed !!");
                                                
            // sync Tag Item relationship ============================
                                                if (conflictFlag==false) clientFactory.getDbconn().getItem2MntTagTableEntity().syncAll(new ConflictCallback() {

                                                    @Override
//                                                  public void onSuccess(JSONArray resultJson) {
                                                    public void onSuccess(String[] resultStrArray) {
                                                        Window.alert("Conflict in Sync Item2MntTagTable 1 !!");
                                                        conflictFlag = true;
                                                        handleConflictScreen(resultStrArray[0] + " " + resultStrArray[1], null);
                                                    }
                                                },
                                                
                                                new Callback() {

                                                    public void onSuccess() {
                                                        
                                                        if (conflictFlag==false) clientFactory.getDbconn().getItem2MntTagTableEntity().syncAll(new ConflictCallback() {

                                                            @Override
//                                                          public void onSuccess(JSONArray resultJson) {
                                                            public void onSuccess(String[] resultStrArray) {
                                                                Window.alert("Conflict in Sync Item2MntTagTable 2 !!");
                                                                conflictFlag = true;
                                                                handleConflictScreen(resultStrArray[0] + " " + resultStrArray[1], null);
                                                            }
                                                        }, 
                                                        
                                                        new Callback() {

                                                            public void onSuccess() {
                                                                Window.alert("Sync Item-to-MntTag Table completed !!");
                                                                
                                                                mainDesktopView.loadList();
                                                            }
                                                            
                                                        });
                                                    }
                                                    
                                                });

                                            }
                                            
                                        });
                                    }
                                    
                                });
                            
                        }
                        
                    });
                }
                
            });
            
        }
	}
	
    @UiHandler("sync")
    void onButtonResetServerDbClicked(ClickEvent event) {
        sync();
    }
        
    @UiHandler("syncFrClient2Server")
    void onButtonResetLocalDbClicked(ClickEvent event) {

        // touch all local data inside db ===============================
        if (clientFactory != null) {
        
        //  touch Tag
            clientFactory.getDbconn().getTagEntity().all().forEach(new ScalarCallback<MNTTag>() {
                @Override
                public void onSuccess(MNTTag result) {
                    String orig = result.getName();
                    result.setName(orig+" ");
                    Persistence.flush();
                    result.setName(orig);
                    Persistence.flush();
                    
                }

            });
            
        //  touch Item
            clientFactory.getDbconn().getItemEntity().all().forEach(new ScalarCallback<Item>() {
                @Override
                public void onSuccess(Item result) {
                    String orig = result.getName();
                    result.setName(orig+" ");
                    Persistence.flush();
                    result.setName(orig);
                    Persistence.flush();
                }

            });
            
        //  touch Match table
            clientFactory.getDbconn().getItem2MntTagTableEntity().all().forEach(new ScalarCallback<Item2MntTagTable>() {
                @Override
                public void onSuccess(Item2MntTagTable result) {
                    String orig = result.getItem_MNTTags();
                    result.setItem_MNTTags(orig+" ");
                    Persistence.flush();
                    result.setItem_MNTTags(orig);
                    Persistence.flush();
                    
                }

            });
            
        }
        
        // sync to server ===============================================
        sync();
            
    }
        
    void handleConflictScreen(String conflicts, String updatesToPush) {
        Page ConflictDesktopView = new ConflictDesktopView(clientFactory, conflicts, updatesToPush);
        this.goTo(ConflictDesktopView, Transition.POP);
    }    
    
}
