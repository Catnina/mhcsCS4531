package mhcs.view;

import mhcs.model.Module;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

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
		Grid infoGrid = new Grid(1,6);
		Label type = new Label("Type: " + mod.getType());
		Label idLabel = new Label("Module ID Nuber: " + mod.getIdNumber());
		Label xCoord = new Label("X-Coordinate: " + mod.getXCoordinate());
		Label yCoord = new Label("Y-Coordinate: " + mod.getYCoordinate());
		Label cond = new Label("Condition: " + mod.getCondition());
		Label turns = new Label("Orientation: " + mod.getOrientation());
		
		infoGrid.setWidget(1, 1, type);
		infoGrid.setWidget(1, 2, idLabel);
		infoGrid.setWidget(1, 3, xCoord);
		infoGrid.setWidget(1, 4, yCoord);
		infoGrid.setWidget(1, 5, cond);
		infoGrid.setWidget(1, 6, turns);
		
		super.setWidget(infoGrid);
	}
}
