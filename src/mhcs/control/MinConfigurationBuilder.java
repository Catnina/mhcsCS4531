package mhcs.control;


import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Vector;

import mhcs.model.ModuleList;
import mhcs.model.Module;
import mhcs.model.TemplateBlock;
import mhcs.model.Configuration;

public class MinConfigurationBuilder {
	private ModuleList modList;
	
	public MinConfigurationBuilder(ModuleList list) {
		modList = list;
	}
	
	//************************************
	// build min configuation one
	//************************************
	
	public Configuration buildMinConfigOne() {
		Module temp;
		Configuration returnConfig = null;
		ArrayList<Module> plainList = getPlains();
		if (plainList.size()>=3){
			ArrayList<TemplateBlock> key = new ArrayList<TemplateBlock>();
			TemplateBlock plain1 = new TemplateBlock(1, 0, 0, "Plain");
			plain1.setContainedModule(plainList.get(0));
			key.add(plain1);
			TemplateBlock plain2 = new TemplateBlock(1, -1, 0, "Plain");
			plain2.setContainedModule(plainList.get(1));
			key.add(plain2);
			TemplateBlock plain3 = new TemplateBlock(1, 1, 0, "Plain");
			plain3.setContainedModule(plainList.get(2));
			key.add(plain3);
			TemplateBlock airlock = new TemplateBlock(2,2,0,"Airlock");
			temp = findType("Airlock");
			if(temp != null){
				airlock.setContainedModule(temp);
				key.add(airlock);
			}
			TemplateBlock canteen = new TemplateBlock(4,-1,-1, "Canteen");
			temp = findType("Canteen");
			if(temp != null){
				canteen.setContainedModule(temp);
				key.add(canteen);
			}			
			TemplateBlock control = new TemplateBlock(5,0,-1, "Control");
			temp = findType("Control");
			if(temp != null){
				control.setContainedModule(temp);
				key.add(control);
			}			
			TemplateBlock dormitory = new TemplateBlock(6,1,-1, "Dormitory");
			temp = findType("Dormitory");
			if(temp != null){
				dormitory.setContainedModule(temp);
				key.add(dormitory);
			}
			TemplateBlock food = new TemplateBlock(7,-1,1,"Food");
			temp = findType("Food");
			if(temp != null){
				food.setContainedModule(temp);
				key.add(food);
			}
			TemplateBlock power = new TemplateBlock(8,0,1,"Power");
			temp = findType("Power");
			if(temp != null){
				power.setContainedModule(temp);
				key.add(power);
			}
			TemplateBlock sanitation = new TemplateBlock(9,1,1,"Sanitation");
			temp = findType("Sanitation");
			if(temp != null){
				sanitation.setContainedModule(temp);
				key.add(sanitation);
			}
			
			returnConfig = new Configuration(averageX(), averageY(), key, 66);
		}
		return returnConfig;
	}
	
	

	//************************************
	// build min configuation two
	//************************************
	
	public Configuration buildMinConfigTwo() {
		Module temp;
		Configuration returnConfig = null;
		ArrayList<Module> plainList = getPlains();
		if (plainList.size()>=3){
			ArrayList<TemplateBlock> key = new ArrayList<TemplateBlock>();
			TemplateBlock plain1 = new TemplateBlock(1, 0, 0, "Plain");
			plain1.setContainedModule(plainList.get(0));
			key.add(plain1);
			TemplateBlock plain2 = new TemplateBlock(1, 1, 0, "Plain");
			plain2.setContainedModule(plainList.get(1));
			key.add(plain2);
			TemplateBlock plain3 = new TemplateBlock(1, 0, -1, "Plain");
			plain3.setContainedModule(plainList.get(2));
			key.add(plain3);
			TemplateBlock airlock = new TemplateBlock(2,0,-2,"Airlock");
			temp = findType("Airlock");
			if(temp != null){
				airlock.setContainedModule(temp);
				key.add(airlock);
			}
			TemplateBlock canteen = new TemplateBlock(4,-1,0, "Canteen");
			temp = findType("Canteen");
			if(temp != null){
				canteen.setContainedModule(temp);
				key.add(canteen);
			}			
			TemplateBlock control = new TemplateBlock(5,0,1, "Control");
			temp = findType("Control");
			if(temp != null){
				control.setContainedModule(temp);
				key.add(control);
			}			
			TemplateBlock dormitory = new TemplateBlock(6,1,-1, "Dormitory");
			temp = findType("Dormitory");
			if(temp != null){
				dormitory.setContainedModule(temp);
				key.add(dormitory);
			}
			TemplateBlock food = new TemplateBlock(7,-1,-1,"Food");
			temp = findType("Food");
			if(temp != null){
				food.setContainedModule(temp);
				key.add(food);
			}
			TemplateBlock power = new TemplateBlock(8,1,1,"Power");
			temp = findType("Power");
			if(temp != null){
				power.setContainedModule(temp);
				key.add(power);
			}
			TemplateBlock sanitation = new TemplateBlock(9,2,0,"Sanitation");
			temp = findType("Sanitation");
			if(temp != null){
				sanitation.setContainedModule(temp);
				key.add(sanitation);
			}
			
			returnConfig = new Configuration(averageX(), averageY(), key, 78);
		}
		return returnConfig;
	}
	
	
	
	private ArrayList<Module> getPlains() {
		ArrayList<Module> result = new ArrayList<Module>();
		for(Integer idNum : modList.getListOfModuleIds()) {
			if(idNum <= 40 && idNum >= 1) {
				Module plainMod = modList.getModuleByIdNumber(idNum);
				if(plainMod.getCondition().equals("Usable") || plainMod.getCondition().equals("undamaged")) {
					result.add(plainMod);
				}
			}
		}
		return result;
	}
	
	
	
	
	private Module findType(String type) {
		Module result = null;
		Module inQuestion;
		boolean found = false;
		Vector<Module> moduleVector = modList.getModules();
		for(Integer index = 0; index < moduleVector.size() && !found; ++index) {
			inQuestion = moduleVector.elementAt(index);
			if( inQuestion.getType().equals(type) ) {
				if(inQuestion.getCondition().equals("Usable") || inQuestion.getCondition().equals("undamaged")){ 
					result = inQuestion;
					found = true;
				} 
			}
		}
		return result;
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
}