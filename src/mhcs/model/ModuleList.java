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
	
	//General Methods
	public Module getModuleByIdNumber(Integer idNum) {
		Module result;
		result = moduleTable.get(idNum);
		return result;
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
}
