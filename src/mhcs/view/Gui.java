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
import mhcs.control.weather;

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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.dom.client.Style.Unit;


public class Gui{
	   public void onModuleLoad() {	
		   loginPage();
	   }
	   
	   private void loginPage(){
		   RootPanel.get().clear();
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
		    	if (s.equals("mhcs")) {
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
		   DockLayoutPanel panel = new DockLayoutPanel(Unit.EM);
		   // p.addNorth(new HTML("north"), 2);
		   // p.addSouth(new HTML("south"), 2);
		   // p.addEast(new HTML("east"), 2);
		   // p.addWest(new HTML("west"), 2);
		   // p.add(new HTML("center"));

		    // Attach the LayoutPanel to the RootLayoutPanel. The latter will listen for
		    // resize events on the window to ensure that its children are informed of
		    // possible size changes.
		    
		   HorizontalPanel southPanel = new HorizontalPanel();
		   HorizontalPanel northPanel = new HorizontalPanel();
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
           Button logoutButton= new Button("Log Out", new ClickHandler() {
               public void onClick(ClickEvent event) {
                loginPage();
  		      }
  		   });
           Label configPoss = new Label("minimum configuration NOT possible");
           //add listener; check that configuration is/isn't possible
           northPanel.add(configPoss);
           northPanel.add(logoutButton);
           panel.addNorth(northPanel,5);
		   //map in center area
           VerticalPanel weatherPanel = new VerticalPanel();
           weatherPanel = makeWeatherMethod();
           VerticalPanel tenPanel = new VerticalPanel();
           tenPanel = makeTenMethod();
           StackLayoutPanel stackPanel = new StackLayoutPanel(Unit.EM);
           stackPanel.add(weatherPanel, new HTML("Weather"),4);
           stackPanel.add(tenPanel, new HTML("10-Day Alert"), 4);
           //centerPanel.add(ModuleMap.renderMap(ModuleList.getModules()));
           panel.addEast(stackPanel,25);
           panel.addSouth(southPanel,5);
           
           RootLayoutPanel rp = RootLayoutPanel.get();
		    rp.add(panel);
	   }
	   
	   //I don't know if I need these methods...
	   private void addModule(ClickHandler clickHandler) {
		 //TODO Auto-generated Method stub
	}
	   private void getConfigs(ClickHandler clickHandler) {
		// TODO Auto-generated method stub	
	}
	   private void removeModule(ClickHandler clickHandler) {
		// TODO Auto-generated method stub	
	}


	private void addModuleMethod(){
		RootPanel.get().clear();		   
		 //this section of code makes the elements for adding a module
		   	  VerticalPanel panel1 = new VerticalPanel();
		   	  //actually want to use list boxes for dropdowns suggest doesn't return current selection
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
		        	  	//able to add? (boolean)
		        	  	//show 
		        	  	makeMain();	
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

		      //addModulePopUp.add(panel1);
		      RootPanel.get().add(panel1);
	   }
	
	private void removeModuleMethod(){
		//TODO Janna
	}
	
	private void getConfigurations(){
	//TODO	Janna
	}
	
	private VerticalPanel makeWeatherMethod(){
		VerticalPanel tempPanel = new VerticalPanel();
		ScrollPanel sp = weather.getResponse();
		tempPanel.add(sp);
		return tempPanel;
	}
	
	private VerticalPanel makeTenMethod(){
		VerticalPanel tempPanel = new VerticalPanel();
		//TODO 
		return tempPanel;
	}
}
