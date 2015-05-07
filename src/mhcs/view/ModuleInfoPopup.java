package mhcs.view;

import mhcs.model.Module;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Creates a Popup that displays a given module's info
 * @author Ryan
 *
 */
public class ModuleInfoPopup extends PopupPanel {
	
	/**
	 * ModuleInfoPopup constructor
	 * @param mod Takes a module that the user wants info about
	 */
	public ModuleInfoPopup(Module mod) {
		super(true);

		TabLayoutPanel tabs = new TabLayoutPanel(1.5, Unit.EM);
		
		Grid infoGrid = new Grid(6, 1);
		
		Label type = new Label("Type: " + mod.getType());
		Label idLabel = new Label("Module ID Nuber: " + mod.getIdNumber());
		Label xCoord = new Label("X-Coordinate: " + mod.getXCoordinate());
		Label yCoord = new Label("Y-Coordinate: " + mod.getYCoordinate());
		Label cond = new Label("Condition: " + mod.getCondition());
		Label turns = new Label("Orientation: " + mod.getOrientation());
		
		infoGrid.setWidget(0, 0, type);
		infoGrid.setWidget(1, 0, idLabel);
		infoGrid.setWidget(2, 0, xCoord);
		infoGrid.setWidget(3, 0, yCoord);
		infoGrid.setWidget(4, 0, cond);
		infoGrid.setWidget(5, 0, turns);
		
		tabs.add(infoGrid, "Module Info");
		/*
		VerticalPanel edit = new VerticalPanel();
		edit.add(new Label("Edit Module (ID: " + mod.getIdNumber() + "):"));
		TextBox xBox = new TextBox();
		xBox.setText("" + mod.getXCoordinate());
		edit.add(new Label("X Coordinate:"));
		edit.add(xBox);
		TextBox yBox = new TextBox();
		yBox.setText("" + mod.getXCoordinate());
		edit.add(new Label("Y Coordinate:"));
		edit.add(yBox);
		
		
		
//		tabs.add(new Label("Type: " + mod.getType() + 
//				"\nModule ID Nuber: " + mod.getIdNumber() +
//				"\nX-Coordinate: " + mod.getXCoordinate() +
//				"\nY-Coordinate: " + mod.getYCoordinate() +
//				"\nCondition: " + mod.getCondition() +
//				"Orientation: " + mod.getOrientation()), "Module Info");
		
		tabs.add(edit, "Edit Module");
		*/
		tabs.setSize("200px", "350px");
		
		//Label modInfo = new Label(info);
		
		
		//add(type);
		add(tabs);
		
		
		
		
	}
}