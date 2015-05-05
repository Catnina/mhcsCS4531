/*
 * Owner: Garrett Blythe
 * Original Date: 4/9
 * Amended by:		Date: 
 *   Garrett Blythe		4/9
 */
// It may be that this module belongs in the controller package rather than the model package
package mhcs.model;

public class ModuleMaker {
	private ModuleList list;
	private Integer X_SIZE = 100;
	private Integer Y_SIZE = 50;
	private boolean clear[][] = new boolean[X_SIZE][Y_SIZE];
	
	//Constructor
	public ModuleMaker(ModuleList givenList)
	{
		list = givenList;
		reset();
	}
	
	//General Methods
	public boolean createModule(Integer idNumber, Integer xCoordinate, Integer yCoordinate, Integer turnsToUpright, String condition) {
		int id = idNumber.intValue();
		boolean result = true;
		Module newModule = null;
		if(clear[xCoordinate-1][yCoordinate-1]) {
			if(id >= 1 && id <= 40) {
				newModule = new PlainModule(xCoordinate, yCoordinate, idNumber, turnsToUpright, condition);
				list.addModule(newModule);
			} else if( (id >= 61 && id <= 80) || (id >= 91 && id <= 100) || (id >= 111 && id <= 120) || (id >= 131 && id <= 134) || (id >= 141 && id <= 144) || (id >= 151 && id <= 154) || (id >= 161 && id <= 164) || (id >= 171 && id <= 174) || (id >=181 && id <= 184) ) {
				newModule = new StandardModule(xCoordinate, yCoordinate, idNumber, turnsToUpright, condition);
				list.addModule(newModule);
			} else {
				result = false;
			}
		} else {
			result = false;
		}
		
		return result;
			
	}
	
	public boolean editModule(Integer oldId, Integer idNumber, Integer xCoordinate, Integer yCoordinate, Integer turnsToUpright, String condition) {
		Module target = list.getModuleByIdNumber(oldId);
		clear[target.getXCoordinate()-1][target.getYCoordinate()-1] = true;
		list.removeModule(target);
		return createModule(idNumber, xCoordinate, yCoordinate, turnsToUpright, condition);
	}
	
	public void clear() {
		list.clearList();
		reset();
	}
	
	private void reset() {
		for(Integer i = 0; i < X_SIZE; ++i) {
			for(Integer j = 0; j < Y_SIZE; ++j) {
				clear[i][j] = true;
			}
		}
	}
}
