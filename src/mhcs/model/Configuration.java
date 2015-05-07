
package mhcs.model;

import java.util.ArrayList;
import java.util.Vector;

import mhcs.model.ModuleMaker;
import mhcs.model.ModuleList;

public class Configuration {
	private Integer absoluteX;
	private Integer absoluteY;
	private ArrayList<TemplateBlock> list;
	private Integer quality;
	private Integer OFFSET = 12;
	
	public Configuration(Integer x, Integer y, ArrayList<TemplateBlock> key, Integer configurationQuality) {
		absoluteX = x;
		absoluteY = y;
		list = key;
		quality = configurationQuality;
	}
	
	public Integer getX() {
		return absoluteX;
	}
	public void setX(Integer x) {
		absoluteX = x;
	}
	
	public Integer getY() {
		return absoluteY;
	}
	public void setY(Integer y) {
		absoluteY = y;
	}
	
	public ArrayList<TemplateBlock> getTemplate() {
		return list;
	}
	protected void setTemplate(ArrayList<TemplateBlock> newKey) {
		list = newKey;
	}
	public Vector<Module> getConfiguration() {
		ModuleList modList = new ModuleList();
		ModuleMaker maker = new ModuleMaker(modList);
		for(TemplateBlock block: list) {
			Module module = block.getContainedModule();
			maker.createModule(module.getIdNumber(), ( absoluteX + block.getXOffset() ), ( absoluteY + block.getYOffset() ), 0, "Usable");
		}
		
		return modList.getModules();
	}
	
	/**
	 * Gets a module list for easy use in module map
	 * @return ModuleList of the configuration
	 */
	public ModuleList getConfigModList() {
		ModuleList modList = new ModuleList();
		ModuleMaker maker = new ModuleMaker(modList);
		for(TemplateBlock block: list) {
			Module module = block.getContainedModule();
			maker.createModule(module.getIdNumber(), ( absoluteX + block.getXOffset() ), ( absoluteY + block.getYOffset() ), 0, "Usable");
		}
		
		return modList;
		
	}
	
	public Integer getQuality() {
		return quality;
	}
	protected void setQuality(Integer newQuality) {
		quality = newQuality;
	}
	
	public boolean moveCenterOfGravity(Integer x, Integer y) {
		boolean result = false;
		if (x > OFFSET && x < (100 - OFFSET) && y > OFFSET && y < (100 - OFFSET)) {
			Integer oldX = averageX();
			Integer oldY = averageY();
			
			absoluteX += x - oldX;
			absoluteY += y - oldY;
			
			result = true;
		}
		
		return result;
	}
	
	private Integer averageX() {
		Integer sum = 0;
		Integer count = 0;
		for(TemplateBlock template: list) {
			sum += template.getXOffset();
			++count;
		}
		
		return (sum / count);
	}
	private Integer averageY() {
		Integer sum = 0;
		Integer count = 0;
		for(TemplateBlock template: list) {
			sum += template.getYOffset();
			++count;
		}
		
		return (sum / count);
	}
	
	public String toString() {
		String result = "";
		result = "ConfigX:" + absoluteX + " ConfigY:" + absoluteY + " ";
		
		for( TemplateBlock unit: list ) {
			result += unit.toString() + " ";
		}
		
		return result;
	}
}