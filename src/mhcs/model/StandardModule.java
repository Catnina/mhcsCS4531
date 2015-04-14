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
	
	//Constructors
	public StandardModule(int x, int y, int id, int turns, String cond) {
		super(x, y, id, turns, cond);
	}
	
	public StandardModule(int x, int y, int id, int turns, String cond, Module nextTo) {
		super(x, y, id, turns, cond);
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
}