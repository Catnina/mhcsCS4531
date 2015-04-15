/*
 * Owner: Garrett Blythe
 * Original Date: 4/9
 * Amended by:		Date: 
 *   Garrett Blythe		4/9
 */

package mhcs.model;

public class StandardModule extends Module{
	private Integer neighborId;
	private Module neighbor;
	private String type;
	
	//Constructors
	public StandardModule(int x, int y, int id, int turns, String cond) {
		super(x, y, id, turns, cond);
		setType();
	}
	
	public StandardModule(int x, int y, int id, int turns, String cond, Module nextTo) {
		super(x, y, id, turns, cond);
		setType();
		neighbor = nextTo;
		neighborId = nextTo.getIdNumber();
	}
	
	//toString
	public String toString() {
		String output;
		output = super.toString();
		output += " NEXT_TO:" + neighborId;
		return output;
	}
	
	// Getters and Setters
	public Module getNeighbor() {
		return neighbor;
	}
	public Integer getNeighborId() {
		return neighborId;
	}
	public void setNeighbor(Module newNeighbor) {
		neighbor = newNeighbor;
		neighborId = newNeighbor.getIdNumber();
	}
	
	public String getType() {
		return type;
	}
	private void setType() {
		Integer selfId = super.getIdNumber();
		int id = selfId.intValue();
		if (id >= 61 && id <= 80) {
			type = "Dormitory";
		} else if (id >= 91 && id <= 100) {
			type = "Sanitation";
		} else if (id >= 111 && id <= 120) {
			type = "Food";
		} else if (id >= 131 && id <= 134) {
			type = "Gym";
		} else if (id >= 141 && id <= 144) {
			type = "Canteen";
		} else if (id >= 151 && id <= 154) {
			type = "Power";
		} else if (id >= 161 && id <= 164) {
			type = "Control";
		} else if (id >= 171 && id <= 174) {
			type = "Airlock";
		} else if (id >= 181 && id <= 184) {
			type = "Medical";
		} else {
			type = "ERROR - No valid module type";
		}
	}
}