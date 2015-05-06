package mhcs.control;

import java.util.Collection;

import mhcs.model.Module;
import mhcs.model.ModuleList;
import mhcs.model.PlainModule;
import mhcs.model.StandardModule;

/**
 * counts number of modules of each type and if a minimum configruation is possible
 * 
 * @author Janna Madden
 *
 */

public class counters{
	//initialize each counter to zero
	private Integer plain = 0;
	private Integer airlock = 0;
	private Integer hosp = 0;
	private Integer dorm = 0;
	private Integer food = 0;
	private Integer sanit = 0;
	private Integer power = 0;
	private Integer control = 0;
	private Integer gym = 0;
	private Integer canteen =0;
	
	public counters(ModuleList mList){
		//iterate through list; count occurance of each module type
		Collection<Module> vMods = mList.getModules();
		Module mod = null;
		for(Module iterator : vMods){
			mod = iterator;
			Integer id = mod.getIdNumber();
			//if the moudule falls between these number, a module is added to the type of module for example, plain
			if(id >= 1 && id <= 40) 
				plain++;
			else if(id >= 61 && id <= 80)
				dorm++;
			else if(id >= 91 && id <= 100)
				sanit++;
			else if(id >= 111 && id <= 120)
				food++;
			else if(id >= 131 && id <= 134)
				gym++;
			else if(id >= 141 && id <= 144)
				canteen++;
			else if(id >= 151 && id <= 154)
				power++;
			else if (id >= 161 && id <= 164)
				control++;
			else if (id >= 171 && id <= 174)
				airlock++;
			else if (id >=181 && id <= 184)
				hosp++;
		}
	}
	public boolean minConfigPossible(){
		boolean toReturn = false; 
		if (plain >=3){ //if we have at least three plains, 1 airlock, 1 dorm, 1 food and water storage, 
			if (airlock >=1){ //one sanitation, one power, 1 control and 1 canteen module, a minumum configuration is possible
				if (dorm >=1){
					if (food >=1){
						if (sanit >=1){
							if (power >=1){
								if (control >=1){
									if (canteen >=1)
										toReturn = true;
								}
							}
						}
					}
				}
				
			}
		}
		return toReturn; //otherwise returns toReturn which is initialized to false. 
	}
}