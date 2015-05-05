
package mhcs.control;


import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Vector;

import mhcs.model.ModuleList;
import mhcs.model.Module;
import mhcs.model.TemplateBlock;
import mhcs.model.Configuration;

public class ConfigurationBuilder {
	private ModuleList modList;
	
	private Integer PLAIN = 2;
	private Integer DORMITORY = 2;
	private Integer SANITATION = 4;
	private Integer FOOD = 2;
	private Integer GYM = 2;
	private Integer CANTEEN = 10;
	private Integer POWER = 7;
	private Integer CONTROL = 7;
	private Integer AIRLOCK = 20;
	private Integer MEDICAL = 10;
	
	public ConfigurationBuilder(ModuleList list) {
		modList = list;
	}
	
	public Configuration buildCrossConfiguration() {
		Configuration config;
		PriorityQueue<TemplateBlock> templateQueue = buildCrossTemplate();
		ArrayList<TemplateBlock> template;
		
		templateQueue = buildSkeleton(templateQueue);
		
		template = fleshCross(templateQueue);
		
		config = new Configuration( averageX(), averageY(), template, qualityCalculator(36));
		
		return config;
	}
	//   To be added if time allows
	public Configuration buildCupConfiguration() {
		Configuration config;
		PriorityQueue<TemplateBlock> templateQueue = buildCupTemplate();
		ArrayList<TemplateBlock> template;
		
		templateQueue = buildSkeleton(templateQueue);
		
		template = fleshCup(templateQueue);
		
		config = new Configuration( averageX(), averageY(), template, qualityCalculator(34));
		
		return config;
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
	private PriorityQueue<TemplateBlock> buildSkeleton(PriorityQueue<TemplateBlock> buildQueue) {
		PriorityQueue<TemplateBlock> built = new PriorityQueue<TemplateBlock>();
		ArrayList<Module> hallList = getPlains();
		for(TemplateBlock current: buildQueue) {
			if(!hallList.isEmpty()) {
				current.setContainedModule(hallList.remove(1));
				built.add(current);
			}
		}
		return built;
	}

	private Integer qualityCalculator(Integer minConfigNum) { // Minimun Configuration Number - the number of modules required to provide a full configuration
		Integer quality = 100;
		Integer count = 0;
		
		// plain modules
		for(Integer i = 1; i <= 40; ++i) {
			if(modList.containsId(i)) {
				++count;
			}
		}
		if( count.intValue() < minConfigNum.intValue() ) {
			quality -= PLAIN * (minConfigNum - count);
		}
		
		// dormitory modules
		for(Integer i = 61; i <= 80; ++i) {
			if(!modList.containsId(i)) {
				quality -= DORMITORY;
			}
		}
		// sanitation modules
		for(Integer i = 91; i <= 100; ++i) {
			if(!modList.containsId(i)) {
				quality -= SANITATION;
			}
		}
		// food modules
		for(Integer i = 111; i <= 120; ++i) {
			if(!modList.containsId(i)) {
				quality -= FOOD;
			}
		}
		// gym modules
		for(Integer i = 131; i <= 134; ++i) {
			if(!modList.containsId(i)) {
				quality -= GYM;
			}
		}
		
		// canteen modules
		for(Integer i = 141; i <= 144; ++i) {
			if(!modList.containsId(i)) {
				quality -= CANTEEN;
			}
		}
		
		// power modules
		for(Integer i = 151; i <= 154; ++i) {
			if(!modList.containsId(i)) {
				quality -= POWER;
			}
		}
		
		// control modules
		for(Integer i = 161; i <= 164; ++i) {
			if(!modList.containsId(i)) {
				quality -= CONTROL;
			}
		}
		
		// airlock modules
		for(Integer i = 171; i <= 174; ++i) {
			if(!modList.containsId(i)) {
				quality -= AIRLOCK;
			}
		}
		// medical modules
		for(Integer i = 181; i <= 184; ++i) {
			if(!modList.containsId(i)) {
				quality -= MEDICAL;
			}
		}
		
		if( quality.intValue() < 0) {
			quality = 0;
		}
		
		return quality;
	}
	
	private ArrayList<TemplateBlock> fleshCup(PriorityQueue<TemplateBlock> templateQueue) {
		ArrayList<TemplateBlock> template = new ArrayList<TemplateBlock>();
		ArrayList<Module> used = new ArrayList<Module>();
		ArrayList<TemplateBlock> endcap = new ArrayList<TemplateBlock>();  //contains the current Airlock locations
		TemplateBlock current = null;
		TemplateBlock neighbor;
		Module addedModule;
		
		
		
		for(Integer i = 0; i < 2; ++i) {	
			if( !templateQueue.isEmpty()) {
				current = templateQueue.remove();	// 1 & 23 Airlock formation;
				template.add(current);
				
				addedModule = findType("Airlock", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()-(1-i), current.getYOffset()-i, addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
				addedModule = findType("Medical", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()+i, current.getYOffset()-(1-i), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
				addedModule = findType("Power", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()-1, current.getYOffset()+(1-i)-(i), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
				addedModule = findType("Control", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()+1, current.getYOffset()+(i)-(1-i), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
			}
			if( !templateQueue.isEmpty()) {
				current = templateQueue.remove();	// 2 & 24 blank
				template.add(current);
			}
			
			if( !templateQueue.isEmpty()) {
				current = templateQueue.remove();	// 3 & 25
				template.add(current);
				
				addedModule = findType("Dormitory", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()-1, current.getYOffset(), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
				addedModule = findType("Dormitory", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()+1, current.getYOffset(), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
			}
			
			if( !templateQueue.isEmpty()) {
				current = templateQueue.remove();	// 4 & 26
				template.add(current);
				
				addedModule = findType("Sanitation", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()-1, current.getYOffset(), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
				addedModule = findType("Sanitation", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()+1, current.getYOffset(), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
			}
			
			if( !templateQueue.isEmpty()) {
				current = templateQueue.remove();	// 5 & 27
				template.add(current);
				
				addedModule = findType("Dormitory", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()-1, current.getYOffset(), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
				addedModule = findType("Dormitory", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()+1, current.getYOffset(), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
			}
			
			if( !templateQueue.isEmpty()) {
				current = templateQueue.remove();	// 6 & 28
				template.add(current);
				
				addedModule = findType("Gym", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()-1, current.getYOffset(), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
				addedModule = findType("Dormitory", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()+1, current.getYOffset(), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
			}
			
			if( !templateQueue.isEmpty()) {
				current = templateQueue.remove();	// 7 & 29
				template.add(current);
				
				addedModule = findType("Sanitation", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()-1, current.getYOffset(), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
				addedModule = findType("Dormitory", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()+1, current.getYOffset(), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
			}
			
			if( !templateQueue.isEmpty()) {
				current = templateQueue.remove();	// 8 & 30
				template.add(current);
				
				addedModule = findType("Dormitory", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()-1, current.getYOffset(), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
				addedModule = findType("Dormitory", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()+1, current.getYOffset(), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
			}
			
			if( !templateQueue.isEmpty()) {
				current = templateQueue.remove();	// 9 & 31
				template.add(current);
				
				addedModule = findType("Dormitory", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()-1, current.getYOffset(), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
				addedModule = findType("Sanitation", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()+1, current.getYOffset(), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
			}
			
			if( !templateQueue.isEmpty()) {
				current = templateQueue.remove();	// 10 & 32
				template.add(current);
				
				addedModule = findType("Dormitory", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()-1, current.getYOffset(), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
				addedModule = findType("Sanitation", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()+1, current.getYOffset(), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
			}
			
			if( !templateQueue.isEmpty()) {
				current = templateQueue.remove();	// 11 & 33
				template.add(current);
				
				addedModule = findType("Gym", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()+1, current.getYOffset(), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
			}
			
			if( !templateQueue.isEmpty()) {
				current = templateQueue.remove();	// 12 & 34
				template.add(current);
				endcap.add(current);
			}
			
			if(i.equals(0)) {
				if( !templateQueue.isEmpty()) {
					current = templateQueue.remove();	// 13 blank
					template.add(current);
				}
				
				if( !templateQueue.isEmpty()) {
					current = templateQueue.remove();	// 14
					template.add(current);
					
					addedModule = findType("Food", used);
					if(addedModule != null) {
						neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset(), current.getYOffset()-1, addedModule.getType());
						neighbor.setContainedModule(addedModule);
						template.add(neighbor);
					}
					addedModule = findType("Canteen", used);
					if(addedModule != null) {
						neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset(), current.getYOffset()+1, addedModule.getType());
						neighbor.setContainedModule(addedModule);
						template.add(neighbor);
					}
				}
				
				if( !templateQueue.isEmpty()) {
					current = templateQueue.remove();	// 15
					template.add(current);
					
					addedModule = findType("Food", used);
					if(addedModule != null) {
						neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset(), current.getYOffset()-1, addedModule.getType());
						neighbor.setContainedModule(addedModule);
						template.add(neighbor);
					}
					addedModule = findType("Food", used);
					if(addedModule != null) {
						neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset(), current.getYOffset()+1, addedModule.getType());
						neighbor.setContainedModule(addedModule);
						template.add(neighbor);
					}
				}
				
				if( !templateQueue.isEmpty()) {
					current = templateQueue.remove();	// 16
					template.add(current);
					
					addedModule = findType("Canteen", used);
					if(addedModule != null) {
						neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset(), current.getYOffset()-1, addedModule.getType());
						neighbor.setContainedModule(addedModule);
						template.add(neighbor);
					}
					addedModule = findType("Food", used);
					if(addedModule != null) {
						neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset(), current.getYOffset()+1, addedModule.getType());
						neighbor.setContainedModule(addedModule);
						template.add(neighbor);
					}
				}
				
				if( !templateQueue.isEmpty()) {
					current = templateQueue.remove();	// 17
					template.add(current);
					
					addedModule = findType("Food", used);
					if(addedModule != null) {
						neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset(), current.getYOffset()-1, addedModule.getType());
						neighbor.setContainedModule(addedModule);
						template.add(neighbor);
					}
				}
				
				if( !templateQueue.isEmpty()) {
					current = templateQueue.remove();	// 18
					template.add(current);
					
					addedModule = findType("Food", used);
					if(addedModule != null) {
						neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset(), current.getYOffset()-1, addedModule.getType());
						neighbor.setContainedModule(addedModule);
						template.add(neighbor);
					}
					addedModule = findType("Food", used);
					if(addedModule != null) {
						neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset(), current.getYOffset()+1, addedModule.getType());
						neighbor.setContainedModule(addedModule);
						template.add(neighbor);
					}
				}
				
				if( !templateQueue.isEmpty()) {
					current = templateQueue.remove();	// 19
					template.add(current);
					
					addedModule = findType("Canteen", used);
					if(addedModule != null) {
						neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset(), current.getYOffset()-1, addedModule.getType());
						neighbor.setContainedModule(addedModule);
						template.add(neighbor);
					}
					addedModule = findType("Food", used);
					if(addedModule != null) {
						neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset(), current.getYOffset()+1, addedModule.getType());
						neighbor.setContainedModule(addedModule);
						template.add(neighbor);
					}
				}
				
				if( !templateQueue.isEmpty()) {
					current = templateQueue.remove();	// 20
					template.add(current);
					
					addedModule = findType("Food", used);
					if(addedModule != null) {
						neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset(), current.getYOffset()-1, addedModule.getType());
						neighbor.setContainedModule(addedModule);
						template.add(neighbor);
					}
					addedModule = findType("Canteen", used);
					if(addedModule != null) {
						neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset(), current.getYOffset()+1, addedModule.getType());
						neighbor.setContainedModule(addedModule);
						template.add(neighbor);
					}
				}
				
				if( !templateQueue.isEmpty()) {
					current = templateQueue.remove();	// 21
					template.add(current);
					
					addedModule = findType("Food", used);
					if(addedModule != null) {
						neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset(), current.getYOffset()-1, addedModule.getType());
						neighbor.setContainedModule(addedModule);
						template.add(neighbor);
					}
				}
				
				if( !templateQueue.isEmpty()) {
					current = templateQueue.remove();	// 22
					template.add(current);
					if( templateQueue.isEmpty() ) {
						endcap.add(current);
					}
							
				}
			}
		}
		for(Integer i = 0; i < 3 & !templateQueue.isEmpty(); i++) {
			current = templateQueue.remove();	// 35, 36, 37
			template.add(current);
		}
		if(current.getPriority() > 34) {
			replace(34, current, endcap);
		}
		
		for(Integer i = 0; i < 3 & !templateQueue.isEmpty(); i++) {
			current = templateQueue.remove();	// 38, 39, 40
			template.add(current);
		}
		if(current.getPriority() > 37) {
			replace(12, current, endcap);
		}

		for(TemplateBlock block: endcap) {
			addedModule = findType("Airlock", used);
			if(addedModule != null) {
				neighbor = new TemplateBlock(addedModule.getIdNumber(), block.getXOffset(), block.getYOffset()+1, addedModule.getType());
				neighbor.setContainedModule(addedModule);
				template.add(neighbor);
			}
			addedModule = findType("Power", used);
			if(addedModule != null) {
				neighbor = new TemplateBlock(addedModule.getIdNumber(), block.getXOffset()+1, block.getYOffset(), addedModule.getType());
				neighbor.setContainedModule(addedModule);
				template.add(neighbor);
			}
			addedModule = findType("Control", used);
			if(addedModule != null) {
				neighbor = new TemplateBlock(addedModule.getIdNumber(), block.getXOffset()-1, block.getYOffset()-1, addedModule.getType());
				neighbor.setContainedModule(addedModule);
				template.add(neighbor);
			}
			addedModule = findType("Medical", used);
			if(addedModule != null) {
				neighbor = new TemplateBlock(addedModule.getIdNumber(), block.getXOffset()-1, block.getYOffset(), addedModule.getType());
				neighbor.setContainedModule(addedModule);
				template.add(neighbor);
			}
		}
		
		return template;
	}
	private ArrayList<TemplateBlock> fleshCross(PriorityQueue<TemplateBlock> templateQueue) {
		ArrayList<TemplateBlock> template = new ArrayList<TemplateBlock>();
		ArrayList<Module> used = new ArrayList<Module>();
		ArrayList<TemplateBlock> endcap = new ArrayList<TemplateBlock>();  //contains the current Airlock locations.
		TemplateBlock current;
		TemplateBlock neighbor;
		Module addedModule;
		
		if( !templateQueue.isEmpty()) {
			current = templateQueue.remove();	// 1 blank
			template.add(current);
		}
		
		if( !templateQueue.isEmpty()) {
			current = templateQueue.remove();	// 2 blank
			template.add(current);
		}
		
		if( !templateQueue.isEmpty()) {
			current = templateQueue.remove();	// 3 
			template.add(current);
			addedModule = findType("Dormitory", used);
			if(addedModule != null) {
				neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()-1, current.getYOffset(), addedModule.getType());
				neighbor.setContainedModule(addedModule);
				template.add(neighbor);
			}
			addedModule = findType("Dormitory", used);
			if(addedModule != null) {
				neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()+1, current.getYOffset(), addedModule.getType());
				neighbor.setContainedModule(addedModule);
				template.add(neighbor);
			}
		}
		
		if( !templateQueue.isEmpty()) {
			current = templateQueue.remove();	// 4 blank
			template.add(current);
		}
		
		if( !templateQueue.isEmpty()) {
			current = templateQueue.remove();	// 5
			template.add(current);
			addedModule = findType("Canteen", used);
			if(addedModule != null) {
				neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset(), current.getYOffset()+1, addedModule.getType());
				neighbor.setContainedModule(addedModule);
				template.add(neighbor);
			}
			addedModule = findType("Food", used);
			if(addedModule != null) {
				neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset(), current.getYOffset()-1, addedModule.getType());
				neighbor.setContainedModule(addedModule);
				template.add(neighbor);
			}	
		}
		
		if( !templateQueue.isEmpty()) {
			current = templateQueue.remove();	// 6
			template.add(current);
			addedModule = findType("Sanitation", used);
			if(addedModule != null) {
				neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()-1, current.getYOffset(), addedModule.getType());
				neighbor.setContainedModule(addedModule);
				template.add(neighbor);
			}	
			addedModule = findType("Sanitation", used);
			if(addedModule != null) {
				neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()+1, current.getYOffset(), addedModule.getType());
				neighbor.setContainedModule(addedModule);
				template.add(neighbor);
			}
		}
		
		if( !templateQueue.isEmpty()) {
			current = templateQueue.remove();	// 7
			template.add(current);
			addedModule = findType("Gym", used);
			if(addedModule != null) {
				neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()-1, current.getYOffset(), addedModule.getType());
				neighbor.setContainedModule(addedModule);
				template.add(neighbor);
			}	
			addedModule = findType("Dormitory", used);
			if(addedModule != null) {
				neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()+1, current.getYOffset(), addedModule.getType());
				neighbor.setContainedModule(addedModule);
				template.add(neighbor);
			}
		}
		
		if( !templateQueue.isEmpty()) {
			current = templateQueue.remove();	// 8
			template.add(current);
			addedModule = findType("Dormitory", used);
			if(addedModule != null) {
				neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()-1, current.getYOffset(), addedModule.getType());
				neighbor.setContainedModule(addedModule);
				template.add(neighbor);
			}
			addedModule = findType("Dormitory", used);
			if(addedModule != null) {
				neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()+1, current.getYOffset(), addedModule.getType());
				neighbor.setContainedModule(addedModule);
				template.add(neighbor);
			}
		}
		
		if( !templateQueue.isEmpty()) {
			current = templateQueue.remove();	// 9
			template.add(current);
			addedModule = findType("Dormitory", used);
			if(addedModule != null) {
				neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()-1, current.getYOffset(), addedModule.getType());
				neighbor.setContainedModule(addedModule);
				template.add(neighbor);
			}
			addedModule = findType("Sanitation", used);
			if(addedModule != null) {
				neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()+1, current.getYOffset(), addedModule.getType());
				neighbor.setContainedModule(addedModule);
				template.add(neighbor);
			}	
		}
		
		if( !templateQueue.isEmpty()) {
			current = templateQueue.remove();	// 10
			template.add(current);
			addedModule = findType("Gym", used);
			if(addedModule != null) {
				neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()+1, current.getYOffset(), addedModule.getType());
				neighbor.setContainedModule(addedModule);
				template.add(neighbor);
			}
		}
		
		if( !templateQueue.isEmpty()) {
			current = templateQueue.remove();	// 11
			template.add(current);
			endcap.add(current);
		}
		
		if( !templateQueue.isEmpty()) {
			current = templateQueue.remove();	// 12
			template.add(current);
			addedModule = findType("Food", used);
			if(addedModule != null) {
				neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset(), current.getYOffset()+1, addedModule.getType());
				neighbor.setContainedModule(addedModule);
				template.add(neighbor);
			}
			addedModule = findType("Canteen", used);
			if(addedModule != null) {
				neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset(), current.getYOffset()-1, addedModule.getType());
				neighbor.setContainedModule(addedModule);
				template.add(neighbor);
			}	
		}
		
		if( !templateQueue.isEmpty()) {
			current = templateQueue.remove();	// 13
			template.add(current);
			addedModule = findType("Food", used);
			if(addedModule != null) {
				neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset(), current.getYOffset()+1, addedModule.getType());
				neighbor.setContainedModule(addedModule);
				template.add(neighbor);
			}
			addedModule = findType("Food", used);
			if(addedModule != null) {
				neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset(), current.getYOffset()-1, addedModule.getType());
				neighbor.setContainedModule(addedModule);
				template.add(neighbor);
			}	
		}
		
		if( !templateQueue.isEmpty()) {
			current = templateQueue.remove();	// 14
			template.add(current);
			addedModule = findType("Food", used);
			if(addedModule != null) {
				neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset(), current.getYOffset()+1, addedModule.getType());
				neighbor.setContainedModule(addedModule);
				template.add(neighbor);
			}	
			addedModule = findType("Food", used);
			if(addedModule != null) {
				neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset(), current.getYOffset()-1, addedModule.getType());
				neighbor.setContainedModule(addedModule);
				template.add(neighbor);
			}
		}
		
		if( !templateQueue.isEmpty()) {
			current = templateQueue.remove();	// 15
			template.add(current);
			addedModule = findType("Canteen", used);
			if(addedModule != null) {
				neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset(), current.getYOffset()+1, addedModule.getType());
				neighbor.setContainedModule(addedModule);
				template.add(neighbor);
			}
			addedModule = findType("Canteen", used);
			if(addedModule != null) {
				neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset(), current.getYOffset()-1, addedModule.getType());
				neighbor.setContainedModule(addedModule);
				template.add(neighbor);
			}
		}
		
		if( !templateQueue.isEmpty()) {
			current = templateQueue.remove();	// 16
			template.add(current);
			addedModule = findType("Food", used);
			if(addedModule != null) {
				neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset(), current.getYOffset()+1, addedModule.getType());
				neighbor.setContainedModule(addedModule);
				template.add(neighbor);
			}
			addedModule = findType("Food", used);
			if(addedModule != null) {
				neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset(), current.getYOffset()-1, addedModule.getType());
				neighbor.setContainedModule(addedModule);
				template.add(neighbor);
			}	
		}
		
		if( !templateQueue.isEmpty()) {
			current = templateQueue.remove();	// 17
			template.add(current);
			addedModule = findType("Food", used);
			if(addedModule != null) {
				neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset(), current.getYOffset()+1, addedModule.getType());
				neighbor.setContainedModule(addedModule);
				template.add(neighbor);
			}
			addedModule = findType("Food", used);
			if(addedModule != null) {
				neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset(), current.getYOffset()-1, addedModule.getType());
				neighbor.setContainedModule(addedModule);
				template.add(neighbor);
			}
		}
		
		if( !templateQueue.isEmpty()) {
			current = templateQueue.remove();	// 18
			template.add(current);
		}
		
		if( !templateQueue.isEmpty()) {
			current = templateQueue.remove();	// 19
			template.add(current);
			endcap.add(current);
		}
		
		for(Integer i = 0; i < 2; ++i) {
			if( !templateQueue.isEmpty()) {
				current = templateQueue.remove(); // 20 & 29 blank
				template.add(current);
			}
			
			if( !templateQueue.isEmpty()) {
				current = templateQueue.remove(); // 21 & 30
				template.add(current);
				addedModule = findType("Dormitory", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()+(1-i), current.getYOffset()+(i), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
				addedModule = findType("Dormitory", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()-(1-i), current.getYOffset()-(i), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
			}
			
			if( !templateQueue.isEmpty()) {
				current = templateQueue.remove(); // 22 & 31
				template.add(current);
				addedModule = findType("Sanitation", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()+(1-i), current.getYOffset()+(i), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
				addedModule = findType("Sanitation", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()-(1-i), current.getYOffset()-(i), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
			}
			
			if( !templateQueue.isEmpty()) {
				current = templateQueue.remove(); // 23 & 32
				template.add(current);
				addedModule = findType("Dormitory", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()+(1-i), current.getYOffset()+(i), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
				addedModule = findType("Dormitory", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()-(1-i), current.getYOffset()-(i), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
			}
			
			if( !templateQueue.isEmpty()) {
				current = templateQueue.remove(); // 24 & 33
				template.add(current);
				addedModule = findType("Dormitory", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()+(1-i), current.getYOffset()+(i), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
				addedModule = findType("Gym", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()-(1-i), current.getYOffset()-(i), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
			}
			
			if( !templateQueue.isEmpty()) {
				current = templateQueue.remove(); // 25 & 34
				template.add(current);
				addedModule = findType("Dormitory", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()+(1-i), current.getYOffset()+(i), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}	
				addedModule = findType("Sanitation", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()-(1-i), current.getYOffset()-(i), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
			}
			
			if(i == 0) { // if it is the first iteration of the loop 
				if( !templateQueue.isEmpty()) {
					current = templateQueue.remove(); // 26
					template.add(current);
					addedModule = findType("Dormitory", used);
					if(addedModule != null) {
						neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()+1, current.getYOffset(), addedModule.getType());
						neighbor.setContainedModule(addedModule);
						template.add(neighbor);
					}
					addedModule = findType("Dormitory", used);
					if(addedModule != null) {
						neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()-1, current.getYOffset(), addedModule.getType());
						neighbor.setContainedModule(addedModule);
						template.add(neighbor);
					}
				}
			
				if( !templateQueue.isEmpty()) {
					current = templateQueue.remove(); // 27
					template.add(current);
					addedModule = findType("Sanitation", used);
					if(addedModule != null) {
						neighbor = new TemplateBlock(addedModule.getIdNumber(), current.getXOffset()-1, current.getYOffset(), addedModule.getType());
						neighbor.setContainedModule(addedModule);
						template.add(neighbor);
					}
				}
				
				if( !templateQueue.isEmpty()) {
					current = templateQueue.remove(); // 28
					template.add(current);
					endcap.add(current);
				}
			}
		}
				
		if( !templateQueue.isEmpty()) {
			current = templateQueue.remove(); // 35 blank
			template.add(current);
		}
		
		if( !templateQueue.isEmpty()) {
			current = templateQueue.remove(); // 36
			template.add(current);
			endcap.add(current);
		}
		
		if( !templateQueue.isEmpty()) {
			current = templateQueue.remove(); // 37
			template.add(current);
			replace(11, current, endcap);
		}
		
		if( !templateQueue.isEmpty()) {
			current = templateQueue.remove(); // 38
			template.add(current);
			replace(28, current, endcap);
		}
		
		if( !templateQueue.isEmpty()) {
			current = templateQueue.remove(); // 39
			template.add(current);
			replace(36, current, endcap);
		}
		
		if( !templateQueue.isEmpty()) {
			current = templateQueue.remove(); // 40
			template.add(current);
			replace(39, current, endcap);
		}
		
		for(TemplateBlock block: endcap) {
			Integer id = block.getPriority();
			if(id.equals(11) || id.equals(37)) {
				addedModule = findType("Airlock", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), block.getXOffset(), block.getYOffset()+1, addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
				addedModule = findType("Power", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), block.getXOffset()+1, block.getYOffset(), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
				addedModule = findType("Control", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), block.getXOffset()-1, block.getYOffset()-1, addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
				addedModule = findType("Medical", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), block.getXOffset()-1, block.getYOffset(), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
			} else if(id.equals(19)) {
				addedModule = findType("Airlock", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), block.getXOffset()+1, block.getYOffset(), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
				addedModule = findType("Power", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), block.getXOffset(), block.getYOffset()-1, addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
				addedModule = findType("Control", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), block.getXOffset()-1, block.getYOffset()+1, addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
				addedModule = findType("Medical", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), block.getXOffset(), block.getYOffset()+1, addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
			} else if(id.equals(28) || id.equals(38)) {
				addedModule = findType("Airlock", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), block.getXOffset(), block.getYOffset()-1, addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
				addedModule = findType("Power", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), block.getXOffset()-1, block.getYOffset(), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
				addedModule = findType("Control", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), block.getXOffset()+1, block.getYOffset()+1, addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
				addedModule = findType("Medical", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), block.getXOffset()+1, block.getYOffset(), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
			} else if( id.equals(36) || id.equals(39) || id.equals(40) ) {
				addedModule = findType("Airlock", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), block.getXOffset()-1, block.getYOffset(), addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
				addedModule = findType("Power", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), block.getXOffset(), block.getYOffset()+1, addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
				addedModule = findType("Control", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), block.getXOffset()+1, block.getYOffset()-1, addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
				addedModule = findType("Medical", used);
				if(addedModule != null) {
					neighbor = new TemplateBlock(addedModule.getIdNumber(), block.getXOffset(), block.getYOffset()-1, addedModule.getType());
					neighbor.setContainedModule(addedModule);
					template.add(neighbor);
				}
			}
		}
		
		return template;
	}
	
	private void replace(Integer target, TemplateBlock replacement, ArrayList<TemplateBlock> list) {
		for(TemplateBlock block: list) {
			if(block.getPriority().equals(target) ) {
				list.remove(block);
				list.add(replacement);
			}
		}
	}
	
	private Module findType(String type, ArrayList<Module> used) {
		Module result = null;
		Module inQuestion;
		boolean found = false;
		Vector<Module> moduleVector = modList.getModules();
		for(Integer index = 0; index < moduleVector.size() && !found; ++index) {
			inQuestion = moduleVector.elementAt(index);
			if( inQuestion.getType().equals(type) ) {
				if( !used.contains(inQuestion) ) {
					if(inQuestion.getCondition().equals("Usable") || inQuestion.getCondition().equals("undamaged")){ 
						used.add(inQuestion);
						result = inQuestion;
						found = true;
					} else {
						used.add(inQuestion);
					}
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
		nextBlock = new TemplateBlock(12,3,0,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(13,4,0,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(14,5,0,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(15,6,0,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(16,7,0,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(17,8,0,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(18,9,0,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(19,10,0,plain);	// Complete partial configuration - 19
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(20,0,-1,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(21,0,-2,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(22,0,-3,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(23,0,-4,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(24,0,-5,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(25,0,-6,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(26,0,-7,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(27,0,-8,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(28,0,-9,plain);	// Complete Partial Configuration - 28
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(29,-1,0,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(30,-2,0,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(31,-3,0,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(32,-4,0,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(33,-5,0,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(34,-6,0,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(35,-7,0,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(36,-8,0,plain);		// Minimum Complete Configuration - 36
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(37,0,9,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(38,0,-10,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(39,-9,0,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(40,-10,0,plain);
		templateQueue.add(nextBlock);
		
		return templateQueue;
	}
	private PriorityQueue<TemplateBlock> buildCupTemplate() {
		PriorityQueue<TemplateBlock> templateQueue = new PriorityQueue<TemplateBlock>();
		String plain = "Plain";
		
		TemplateBlock nextBlock = new TemplateBlock(1,0,0,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(2,0,1,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(3,0,2,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(4,0,3,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(5,0,4,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(6,0,5,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(7,0,6,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(8,0,7,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(9,0,8,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(10,0,9,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(11,0,10,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(12,0,11,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(13,1,0,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(14,2,0,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(15,3,0,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(16,4,0,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(17,5,0,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(18,6,0,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(19,7,0,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(20,8,0,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(21,9,0,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(22,10,0,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(23,11,0,plain);	// Complete Partial Configuration - 22
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(24,11,1,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(25,11,2,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(26,11,3,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(27,11,4,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(28,11,5,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(29,11,6,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(30,11,7,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(31,11,8,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(32,11,9,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(33,11,10,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(34,11,11,plain);		// Minimum Complete Configuration - 34
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(35,11,12,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(36,11,13,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(37,11,14,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(38,0,12,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(39,0,13,plain);
		templateQueue.add(nextBlock);
		nextBlock = new TemplateBlock(40,0,14,plain);
		templateQueue.add(nextBlock);
		
		return templateQueue;
	}
}