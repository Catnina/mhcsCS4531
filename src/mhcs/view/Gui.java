package mhcs.view;


/**
 * creates the graphical user interface. 
 * Event Bus handles adding and removing modules. 
 * 
 * @author Janna Madden
 *
 */
import com.google.gwt.core.client.EntryPoint;

import mhcs.view.ModuleMap;
import mhcs.model.ModuleList;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;


public class Gui{
	   public void onModuleLoad() {	
		   final PasswordTextBox ptb = new PasswordTextBox();
		   final Label tb = new Label("Enter Password:");
		   VerticalPanel panel = new VerticalPanel();
		   final Button enter = new Button("Enter");
		    panel.add(tb);
		    panel.add(ptb);
		    panel.add(enter);
		    RootPanel.get().add(panel);
		    enter.addClickHandler(new ClickHandler() {
		    	public void onClick(ClickEvent event) { 
		    	String s = ptb.getText();
		    	if (s.equals("trickypassword")) {
		    	tb.setText("Redirecting to Main Page");
		    	makeMain();
		    	}
		    	else {
		    		tb.setText("Please Re-Enter Password");
		    		ptb.setText("");
		    		}
		    	}
		    });
	   }
	   
	   
	   private void makeMain(){
		   RootPanel.get().clear();	
		   VerticalPanel panel = new VerticalPanel();
		   FlowPanel southPanel = new FlowPanel();
		   //buttons in south area
		   Button addModule= new Button("Add Module", new ClickHandler() {
            public void onClick(ClickEvent event) {
            	addModuleMethod();
            }
            });
		   southPanel.add(addModule);
		   RootPanel.get().add(panel);
		   Button removeModule = new Button ("Remove/Edit Module", new ClickHandler() {
	        public void onClick(ClickEvent event) {
	         //remove/edit modules window
		    }
		    });
           southPanel.add(removeModule);
	       Button getConfigs= new Button("Get Configurations", new ClickHandler() {
             public void onClick(ClickEvent event) {
              //get configurations
		      }
		   });
           southPanel.add(getConfigs);
           //configurations possible in north segment
           Label configPoss = new Label("minimum configuration NOT possible");
           panel.add(configPoss);
		   //map in center area
		   //panel.add(ModuleMap.renderMap(ModuleList.getModules()));
           panel.add(southPanel);
           //10 day alert in north panel??	
           RootPanel.get().add(panel);
	   }
	   
	   private void addModule(ClickHandler clickHandler) {
		// TODO Auto-generated method stub
		
	}


	private void getConfigs(ClickHandler clickHandler) {
		// TODO Auto-generated method stub
		
	}


	private void removeModule(ClickHandler clickHandler) {
		// TODO Auto-generated method stub
		
	}


	private void addModuleMethod(){
		   //TO BE DONE IN THIS CLASS:
		   //disable add, remove and getConfigs buttons

		   //click outside to close
		   //
		   
		 //this section of code makes the elements for within the addModule popup window
		   	  VerticalPanel panel1 = new VerticalPanel();
		   	  //actually want to use list boxes for dropdowns suggest doesn't return current selection
		      PopupPanel addModulePopUp = new PopupPanel();
		      Label configNumber = new Label("Module Identification Number");
		      TextBox configNumberInput = new TextBox();   
		      Label coordinates = new Label("Coordinates");
		      TextBox xCoordinate = new TextBox();
		      xCoordinate.setText("X Coordinate");
		      TextBox yCoordinate = new TextBox();	  
		      yCoordinate.setText("Y Coordinate");
		      Label condition = new Label("Condition");
		      ListBox conditionSuggest = new ListBox();
		      conditionSuggest.addItem("Usable");
		      conditionSuggest.addItem("Usable After Repair");
		      conditionSuggest.addItem("Unusable");
		      Label orientation = new Label("Orientation");
		      ListBox orientationSuggest = new ListBox();
		      orientationSuggest.addItem("None");
		      orientationSuggest.addItem("One Rotation");
		      orientationSuggest.addItem("Two Rotations");
		      orientationSuggest.addItem("Three Rotations");
		      Button addModuleButton = new Button("Add Module", new ClickHandler() {
		          public void onClick(ClickEvent event) {
			            //send to event bus
		        	  	//close window
			          }
			        });
		      
		      //add items to panel1
		      panel1.add(configNumber);
		      panel1.add(configNumberInput);
		      panel1.add(coordinates);
		      panel1.add(xCoordinate);
		      panel1.add(yCoordinate);
		      panel1.add(condition);
		      panel1.add(conditionSuggest);
		      panel1.add(orientation);
		      panel1.add(orientationSuggest);
		      panel1.add(addModuleButton);

		      addModulePopUp.add(panel1);
		      RootPanel.get().add(addModulePopUp);
	   }
}
		   
		 /**  


	      
	      //JPopupMenu removeModulePopUp = new JPopupMenu();
	      //JPopupMenu configurationAlert = new JPopupMenu();
	      //JPopupMenu tenDayAlert = new JPopupMenu();
	      
	     // Panel panel_1 = new Panel();
	     // panel.add(panel_1, BorderLayout.SOUTH);
	     // panel_1.setLayout(new GridLayout(1, 0, 0, 0));
	      
	      //map in center area
	      //panel.add(ModuleMap.renderMap(ModuleList.getModules()));
	      

	      //buttons in south area
	      Button addModule = new Button("Add Module", new ClickHandler() {
	          public void onClick(ClickEvent event) {
	            //disable add, remove and get configuration buttons  
	          }
	        });
	      panel.addSouth(addModule, 0);
	      Button removeModule = new Button("Edit/Remove Module", new ClickHandler() {
	          public void onClick(ClickEvent event) {
	            Window.alert("How high?");
	          }
	        });
	      panel.addSouth(removeModule, 0);
	      Button getConfigs = new Button("Get Configurations", new ClickHandler() {
	          public void onClick(ClickEvent event) {
	            Window.alert("How high?");
	          }
	        });
	      panel.addSouth(getConfigs, 0);
	      
	      
	      //configurations possible in north segment
	      Label configPoss = new Label("minimum configuration NOT possible");
	      panel.addNorth(configPoss, 0);
	      
	      //10 day alert in north panel		
	}
}

*/
