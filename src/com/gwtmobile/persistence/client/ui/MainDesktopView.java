/**
 * 
 */
package com.gwtmobile.persistence.client.ui;



import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.gwtmobile.persistence.client.Callback;
import com.gwtmobile.persistence.client.ClientFactoryImpl;
import com.gwtmobile.persistence.client.CollectionCallback;
import com.gwtmobile.persistence.client.ConflictCallback;
import com.gwtmobile.persistence.client.Persistence;
import com.gwtmobile.persistence.client.ScalarCallback;
import com.gwtmobile.persistence.client.domain.Item;
import com.gwtmobile.persistence.client.domain.Item2MntTagTable;
import com.gwtmobile.persistence.client.domain.MNTTag;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.page.Transition;
import com.gwtmobile.ui.client.widgets.Button;
import com.gwtmobile.ui.client.widgets.DropDownItem;
import com.gwtmobile.ui.client.widgets.DropDownList;
import com.gwtmobile.ui.client.widgets.TextArea;
import com.gwtmobile.ui.client.widgets.TextBox;

/**
 * Display the Main View in Desktop originally, but now it should be display on mobile,
 * change the class name in future
 * 
 * @author yan
 *
 */
public class MainDesktopView extends Page {

	interface MainDesktopUiBinder extends UiBinder<HTMLPanel, MainDesktopView> {}
	private static MainDesktopUiBinder uiBinder = GWT.create(MainDesktopUiBinder.class);
	
	private MNTTag selectedTag;
	
	@UiField Button tagNew;
	@UiField Button tagDel;
	@UiField Button tagModify;
	@UiField Button tagAttachItems;
	@UiField TextBox tagNewName;
	@UiField TextBox tagModifiedName;
	@UiField DropDownList tagList;
	@UiField Button Sync2Server;
	
//	@UiField SimplePanel uploadFilePanel;
//	@UiField FlowPanel uploadFileImagePanel;
	
	private Item selectedItem;
	
	@UiField Button itemNew;
	@UiField Button itemDel;
	@UiField Button itemModify;
	@UiField Button itemAttachTags;
//	@UiField Button backupDb;
	@UiField TextArea itemNewName;
	@UiField TextArea itemModifiedName;
	@UiField DropDownList itemList;
	//@UiField CellList<MNTItemEntitiesProxy> itemList;

	private ClientFactoryImpl clientFactory;

	/**
	 * prepare main UI
	 */
	public MainDesktopView(ClientFactoryImpl clientFactory) {
		
		this.clientFactory = clientFactory;
		
	    // init. this View, All composites must call initWidget() in their constructors.
		//	and must put at the end before setup all above
	    initWidget(uiBinder.createAndBindUi(this));
	    
	    loadList();
	    
	}

    // set Event handler of ListBox ==========================================
	@UiHandler("tagList")
	public void onValueChangeTagList(ValueChangeEvent<String> e) {
		// display the selected Tag Name ==================
		String selectedTagName = e.getValue();
		loadSelectedTag(selectedTagName);				// it is async, so using RAM buffer to display selected Tag name
		tagModifiedName.setText(selectedTagName);
	}
    
    // set Event handler of ListBox ==========================================
	@UiHandler("itemList")
	public void onValueChangeItemList(ValueChangeEvent<String> e) {
		// display the selected Item Name ==================
		String selectedItemName = e.getValue();
		loadSelectedItem(selectedItemName);				// it is async, so using RAM buffer to display selected Item name
		itemModifiedName.setText(selectedItemName);
	}
    
	public void loadSelectedTag(final String selectedTagName) {
		
		if(clientFactory != null) {
			
					clientFactory.getDbconn().getTagEntity().findBy("Name", selectedTagName, new ScalarCallback<MNTTag>() {

						@Override
						public void onSuccess(MNTTag result) {
							selectedTag = result;
						}
						
					});
			
		}
	}
	
	public void loadSelectedItem(final String selectedItemName) {
		
		if(clientFactory != null) {
			
					clientFactory.getDbconn().getItemEntity().findBy("Name", selectedItemName, new ScalarCallback<Item>() {

						@Override
						public void onSuccess(Item result) {
							selectedItem = result;
						}
						
					});
		}
	}
	
	// New Tag handling
	@UiHandler("tagNew")
	void onButtonTagNewClicked(ClickEvent event) {
		
		if (clientFactory != null) {
			
			// does it already exist? =============
					
					clientFactory.getDbconn().getTagEntity().all().filter("Name", "=", tagNewName.getText()).count(new ScalarCallback<Integer>() {					
						@Override
						public void onSuccess(Integer result) {
							if (result.intValue()==0) {
								
			// create new =========================
								MNTTag tag = clientFactory.getDbconn().getTagEntity().newInstance();
								tag.setName(tagNewName.getText());
								Persistence.flush(new Callback() {			// flush to database
				
									@Override
									public void onSuccess() {
										tagNewName.setText("");
										
										// after adding a new Tag, List the existing Tags ====================
										loadListTag();
										
            // try sync, otherwise keep it is dirty=========================
										
									}
								});
								
							} else {
								Window.alert("Tag '" + tagNewName.getText() + "' is already exist!");
								tagNewName.setText("");
							}
							
						}
					});
			
		}
	}
	
	@UiHandler("tagDel")
	void onButtonTagDelClicked(ClickEvent event) {
		
//		let user to confirm ========			
		if ( Window.confirm("Pls Confirm to remove '" + selectedTag.getName() + "'") && (clientFactory != null) ) {
			
//			update database ============================
			Persistence.remove(selectedTag);
			
            if (clientFactory != null) {             // update the relationship table
                
                String a = selectedTag.getId();
                
                clientFactory.getDbconn().getItem2MntTagTableEntity().all()
                    .filter("mNTTag_Items", "=", selectedTag.getId())
                            .list(new CollectionCallback<Item2MntTagTable>() {

                    @Override
                    public void onSuccess(Item2MntTagTable[] results) {
                        
                        for (int i=0;i<results.length;i++)
                            Persistence.remove(results[i]);
                    }
                    
                });
                
            }
            Persistence.flush();
			
//			Window.alert("Tag '" + result.getName() + "' has been removed!");
			tagModifiedName.setText("");
			
			// after adding a new Tag, List the existing Tags ====================
			loadListTag();
			
		}
	}
	
	@UiHandler("tagModify")
	void onButtonTagModifyClicked(ClickEvent event) {
		
		if (clientFactory != null) {
			
//			get selected Tag and rd modify name ========		
			selectedTag.setName(tagModifiedName.getText());
			
//			update database ============================
			Persistence.flush(new Callback() {			// flush to database
				
//				@Override
				public void onSuccess() {
//										Window.alert("This Tag name modified from '" + selectedMNTTagName + "' to '" + tagModifiedName.getText() + "'!");
					tagModifiedName.setText("");
										
					// after adding a new Tag, List the existing Tags ====================
					loadListTag();
				}
			});
		}
	}
	
	/**
	 * display all Tags at the UI
	 * 
	 * Todo: consider using RequestFactory howto in tx. delta change only to reduce the comm. data amt 
	 */
	public void loadListTag() {

		if(clientFactory != null) {
			
					clientFactory.getDbconn().getTagEntity().all().order("Name", true).list(new CollectionCallback<MNTTag>() {
							@Override
							public void onSuccess(MNTTag[] results) {
								tagList.getListBox().clear();
								for (int i=0;i<results.length;i++) {
									DropDownItem ddi = new DropDownItem();
									ddi.setText(results[i].getName());
									tagList.add(ddi);
								}
							}
						});
					
		}
	}
	
	@UiHandler("itemNew")
	void onButtonItemNewClicked(ClickEvent event) {
		
		if (clientFactory != null) {
			
			// does it already exist? =============
					
					clientFactory.getDbconn().getTagEntity().all().filter("Name", "=", itemNewName.getText()).count(new ScalarCallback<Integer>() {					
						@Override
						public void onSuccess(Integer result) {
							if (result.intValue()==0) {
								
			// create new =========================
								Item item = clientFactory.getDbconn().getItemEntity().newInstance();
								item.setName(itemNewName.getText());
								
								Persistence.flush(new Callback() {			// flush to database
				
									@Override
									public void onSuccess() {
									    
									    
									    
									    
									    
									    
//										Window.alert("A new Item '" + itemNewName.getText() + "' is added!");
										itemNewName.setText("");
										
										// after adding a new Tag, List the existing Tags ====================
										
//										Persistence.reset();				// drop the table
										
										// after adding a new Item, List the existing Items ====================
										loadListItem();
									}
								});
								
							} else {
								Window.alert("Item '" + itemNewName.getText() + "' is already exist!");
								itemNewName.setText("");
							}
							
						}
					});
			
		}
		
	}
	
	@UiHandler("itemDel")
	void onButtonItemDelClicked(ClickEvent event) {
		
//		let user to confirm ========			
		if ( Window.confirm("Pls Confirm to remove '" + selectedItem.getName() + "'") && (clientFactory != null) ) {
			
//			update database ============================
			Persistence.remove(selectedItem);
			
            if (clientFactory != null) {             // update the relationship table
                
                
                clientFactory.getDbconn().getItem2MntTagTableEntity().all()
                    .filter("item_MNTTags", "=", selectedItem.getId())
                            .list(new CollectionCallback<Item2MntTagTable>() {

                    @Override
                    public void onSuccess(Item2MntTagTable[] results) {
                        
                        for (int i=0;i<results.length;i++)
                            Persistence.remove(results[i]);
                    }
                    
                });
                
            }
            Persistence.flush();
			
//			Window.alert("Item '" + result.getName() + "' has been removed!");
			itemModifiedName.setText("");
			
			// List the existing Items ====================
			loadListItem();
			
		}
	}
	
	@UiHandler("itemModify")
	void onButtonItemModifyClicked(ClickEvent event) {
		
		if (clientFactory != null) {
			
//			get selected Tag and rd modify name ========

			selectedItem.setName(itemModifiedName.getText());
			
//			update database ============================
			Persistence.flush(new Callback() {			// flush to database
				
//				@Override
				public void onSuccess() {
//										Window.alert("This Item name modified from '" + selectedMNTItemName + "' to '" + itemModifiedName.getText() + "'!");
					itemModifiedName.setText("");
					
//										Persistence.reset();				// drop the table
					
					// List the existing Items ====================
					loadListItem();
				}
			});
								
		}
	}
	
	public void loadListItem() {

		if(clientFactory != null) {
			
					clientFactory.getDbconn().getItemEntity().all().order("Name", true).list(new CollectionCallback<Item>() {
							@Override
							public void onSuccess(Item[] results) {
								itemList.getListBox().clear();
								for (int i=0;i<results.length;i++) {
									DropDownItem ddi = new DropDownItem();
									ddi.setText(results[i].getName());
									itemList.add(ddi);
								}
								
							}
						});
					
		}
	}
	
	public void loadList() {
		loadListTag();
		loadListItem();
	}
	
	@UiHandler("itemAttachTags")
	void onButtonItemAttachTagsClicked(ClickEvent event) {
		
		Page ItemAttachTagsDesktopView = new ItemAttachTagsDesktopView(clientFactory, selectedItem);
		this.goTo(ItemAttachTagsDesktopView);
	}

	@UiHandler("tagAttachItems")
	void onButtonTagAttachItemsClicked(ClickEvent event) {
		
		Page TagAttachItemsDesktopView = new TagAttachItemsDesktopView(clientFactory, selectedTag);
		this.goTo(TagAttachItemsDesktopView);
	}

//	@UiHandler("backupDb")
//	void onButtonTagBackupDbClicked(ClickEvent event) {
		/*UrlBuilder urlBuilder = new UrlBuilder();
		urlBuilder.setHost(Window.Location.getHost());
		urlBuilder.setPath("/cloud.db");
		Window.open(urlBuilder.buildString(), "DB backup", "");*/		
//		Window.alert("under construction !!");
//	}
	
    @UiHandler("Sync2Server")
    void sync2ServerClicked(ClickEvent event) {
        
        Page SyncDesktopView = new SyncDesktopView(clientFactory, this);
        this.goTo(SyncDesktopView);
        
    }
    
}
