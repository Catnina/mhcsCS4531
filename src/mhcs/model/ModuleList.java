/*
 * Owner: Garrett Blythe
 * Original Date: 4/9
 * Amended by:		Date: 
 *   Garrett Blythe		4/9
 *   Garrett Blythe		4/10
 */

package mhcs.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

public class ModuleList {
	private HashMap<Integer, Module> moduleTable;
	
	//Constructor
	public ModuleList() {
		moduleTable = new HashMap<Integer, Module>();
	}
	
	//Add and Remove
	public boolean addModule(Module newModule) {
		boolean success;
		Integer idNum = newModule.getIdNumber();
		
		if(moduleTable.containsKey(idNum))
		{
			success = false;
		}
		else
		{
			moduleTable.put(idNum, newModule);
			success = true;
		}
		
		return success;
	}
	public void removeModule(Module target) {
		Integer idNum = target.getIdNumber();
		moduleTable.remove(idNum);
	}
	
	public void clearList() {
		moduleTable.clear();
	}
	
	//General Methods
	public Module getModuleByIdNumber(Integer idNum) {
		Module result;
		result = moduleTable.get(idNum);
		return result;
	}
	public void removeModuleByIdNumber(Integer idNum) {
		moduleTable.remove(idNum);
	}
	
	public boolean containsId(Integer idNum) {
		return moduleTable.containsKey(idNum);
	}
	
	public Vector<Module> getModules() {
		Vector<Module> output;
		Collection<Module> collect = moduleTable.values();
		output = new Vector<Module>(collect);
		return output;
		 		
	}
	
	public Set<Integer> getListOfModuleIds() {
		return moduleTable.keySet();
	}
	
	/**
	 * Creates a full JSON representation of all modules in the list for saving
	 * @return Full JSON list of modules
	 */
	public String toJSONString() {
		String output = "[";
		Collection<Module> modList = getModules();
		for(Module iterator : modList) {
			output += iterator.toJSONString();
			output += ",";
		}
		
		if(output.length() > 2) {
			output = output.substring(0, output.length() - 1);
		}
		output += "]";
		
		return output;
	}
}
