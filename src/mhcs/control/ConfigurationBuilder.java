
package mhcs.control;


import java.util.PriorityQueue;
import java.util.ArrayList;

import mhcs.model.ModuleList;
import mhcs.model.Module;
import mhcs.model.TemplateBlock;
import mhcs.model.Configuration;

public class ConfigurationBuilder {
	private ModuleList modList;
	
	public ConfigurationBuilder(ModuleList list) {
		modList = list;
	}
	
	public Configuration buildCrossConfiguration() {
		Configuration config;
		ArrayList<Module> usedModules = new ArrayList<Module>();
		PriorityQueue<TemplateBlock> templateQueue = buildCrossTemplate();
		ArrayList<TemplateBlock> template;
		
		
		return config;
	}
	/*   To be added if time allows
	   public Configuration buildCupConfiguration() {
		Configuration config;
		ArrayList<Module> usedModules = new ArrayList<Module>();
		PriorityQueue<TemplateBlock> templateQueue;
		ArrayList<TemplateBlock> template;
		
		
		return config;
	}*/
	
	private ArrayList<Module> getPlains() {
		ArrayList<Module> result = new ArrayList<Module>();
		for(Integer idNum : modList.getListOfModuleIds()) {
			if(idNum <= 40 && idNum >= 1) {
				Module plainMod = modList.getModuleByIdNumber(idNum);
				result.add(plainMod);
			}
		}
		return result;
	}
	private ArrayList<TemplateBlock> buildSkeleton(PriorityQueue<TemplateBlock> buildQueue, ArrayList<Module> used) {
		ArrayList<TemplateBlock> built = new ArrayList<TemplateBlock>();
		ArrayList<Module> hallList = getPlains();
		for(TemplateBlock current: buildQueue) {
			if(!hallList.isEmpty()) {
				current.setContainedModule(hallList.remove(1));
				built.add(current);
			}
		}
		return built;
	}
	
	private Integer averageX() {
		Integer sum = 0;
		Integer count = 0;
		for(Module module: modList.getModules()) {
			sum += module.getXCoordinate();
			++count;
		}
		
		return (sum / count);
	}
	private Integer averageY() {
		Integer sum = 0;
		Integer count = 0;
		for(Module module: modList.getModules()) {
			sum += module.getYCoordinate();
			++count;
		}
		
		return (sum / count);
	}
	
	private PriorityQueue<TemplateBlock> buildCrossTemplate() {
		PriorityQueue<TemplateBlock> templateQueue = new PriorityQueue<TemplateBlock>();
		String plain = "Plain";
		
		TemplateBlock nextBlock = new TemplateBlock(1,0,0,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(2,0,1,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(3,0,2,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(4,1,0,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(5,2,0,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(6,0,3,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(7,0,4,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(8,0,5,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(9,0,6,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(10,0,7,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(11,0,8,plain);
		templateQueue.add(nextBlock);
	}
}