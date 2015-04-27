
package mhcs.model;

import java.util.ArrayList;
import java.util.Vector;

import mhcs.model.ModuleMaker;
import mhcs.model.ModuleList;

public class Configuration {
	private Integer absoluteX;
	private Integer absoluteY;
	private ArrayList<TemplateBlock> list;
	
	public Configuration(Integer x, Integer y, ArrayList<TemplateBlock> key) {
		absoluteX = x;
		absoluteY = y;
		list = key;
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
	
	public String toString() {
		String result = "";
		result = "ConfigX:" + absoluteX + " ConfigY:" + absoluteY + " ";
		
		for( TemplateBlock unit: list ) {
			result += unit.toString() + " ";
		}
		
		return result;
	}
}