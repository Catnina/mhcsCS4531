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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;


public class Gui{
	   public void onModuleLoad() {	      
	      DockLayoutPanel panel = new DockLayoutPanel(Unit.EM);
	      Grid panel1 = new Grid(4,3);
	      
	      //this section of code makes the elements for within the addModule popup window
	      MultiWordSuggestOracle conditionOracle = new MultiWordSuggestOracle();
	      conditionOracle.add("Usable");
	      conditionOracle.add("Usable After Repair");
	      conditionOracle.add("Unusable");
		  MultiWordSuggestOracle orientationOracle = new MultiWordSuggestOracle();
		  orientationOracle.add("None");
		  orientationOracle.add("One Rotation");
		  orientationOracle.add("Two Rotations");
		  orientationOracle.add("Three Rotations");
	      PopupPanel addModulePopUp = new PopupPanel();
	      Label configNumber = new Label("Configuration Number");
	      panel1.add(configNumber);
	      TextBox configNumberInput = new TextBox();   
	      panel1.add(configNumberInput);
	      Label coordinates = new Label("Coordinates");
	      panel1.add(coordinates);
	      TextBox xCoordinate = new TextBox();
	      panel.add(xCoordinate);
	      TextBox yCoordinate = new TextBox();	  
	      panel.add(yCoordinate);
	      Label condition = new Label("Condition");
	      panel.add(condition);
	      SuggestBox conditionSuggest = new SuggestBox(conditionOracle);
	      panel.add(conditionSuggest);
	      Label orientation = new Label("Orientation");
	      panel.add(orientation);
	      SuggestBox orientationSuggest = new SuggestBox(orientationOracle);
	      panel.add(orientationSuggest);
	      Button addModuleButton = new Button("Add Module", new ClickHandler() {
	          public void onClick(ClickEvent event) {
		            //Window.alert("How high?");
		          }
		        });
	      panel.add(addModuleButton);
	      addModulePopUp.add(panel1);


	      
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
