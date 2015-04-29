package mhcs.view;

import java.util.Collection;

import mhcs.model.Module;
import mhcs.model.ModuleList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;

/**
 * Creates a map of all the module positions on the map.
 * After a module has been added or removed, call renderMap(ModuleList) to update the map.
 * 
 * @author Ryan Ostroot
 *
 */
public class ModuleMap {
	private Grid map;
	static private int height = 50;
	static private int width = 100;
	
	public ModuleMap() {
		map = new Grid(height, width);
		map.addStyleName("background");
		for(int row = 0; row < height ; row++) {
			for(int column = 0; column < width; column++) {
				map.getCellFormatter().setStyleName(row, column, "tableCell");
			}
		}
	}
	
	/**
	 * Builds the grid representation of the module map.
	 * @param list List of all the modules
	 * @return The GWT grid representation of the module map
	 */
	public Grid renderMap(ModuleList list) {
		Collection<Module> modList = list.getModules();
		Module mod = null;
		//int modId;
		
		for(Module iterator : modList) {
			mod = iterator;
			ModuleImage img = new ModuleImage(mod);
			img.setPixelSize(50, 50);
    	    img.addClickHandler(new ClickHandler() {
    	    	  public void onClick(ClickEvent event) {
    	    		  ModuleImage temp = (ModuleImage)event.getSource();
    	    		  ModuleInfoPopup p = new ModuleInfoPopup(temp.getMod());
    	    		  p.setPopupPosition(event.getClientX(), event.getClientY());
    	    		  p.show();
    	    	  }
    	    });
			
			map.setWidget(50 - mod.getYCoordinate(), mod.getXCoordinate() - 1, img);
		}
		return map;
	}
	
	public void setMapElement(int rows, int columns, Image img) {
		map.setWidget(rows, columns, img);
	}
}