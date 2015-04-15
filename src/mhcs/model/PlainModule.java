/*
 * Owner: Garrett Blythe
 * Original Date: 4/9
 * Amended by:		Date: 
 *   Garrett Blythe		4/9
 */

package mhcs.model;

public class PlainModule extends Module{
	private Integer neighborNorthId;
	private Module neighborNorth;
	private Integer neighborEastId;
	private Module neighborEast;
	private Integer neighborSouthId;
	private Module neighborSouth;
	private Integer neighborWestId;
	private Module neighborWest;
	private String type;
	
	//Constructors
	public PlainModule(int x, int y, int id, int turns, String cond) {
		super(x, y, id, turns, cond);
		
		type = "Plain";
	}
	public PlainModule(int x, int y, int id, int turns, String cond, Module north, Module east, Module south, Module west) {
		super(x, y, id, turns, cond);
		
		type = "Plain";
		
		neighborNorth = north;
		if(north != null) {
			neighborNorthId = north.getIdNumber();
		}
		neighborEast = east;
		if(east != null) {
			neighborEastId = east.getIdNumber();
		}
		neighborSouth = south;
		if(south != null) {
			neighborSouthId = south.getIdNumber();
		}
		neighborWest = west;
		if(west != null) {
			neighborWestId = west.getIdNumber();
		}
	}
	
	//toString
	public String toString() {
		String output;
		output = super.toString();
		output += " NORTH:" + neighborNorthId;
		output += " EAST:" + neighborEastId;
		output += " SOUTH:" + neighborSouthId;
		output += " WEST:" + neighborWestId;
		
		return output;
	}
		
	//Getters and Setters
	
	public Module getNorthNeighbor() {
		return neighborNorth;
	}
	public Integer getNorthNeighborId() {
		return neighborNorthId;
	}
	public void setNorthNeighbor(Module newNeighbor) {
		neighborNorth = newNeighbor;
		neighborNorthId = newNeighbor.getIdNumber();
	}
	
	public Module getEastNeighbor() {
		return neighborEast;
	}
	public Integer getEastNeighborId() {
		return neighborEastId;
	}
	public void setEastNeighbor(Module newNeighbor) {
		neighborEast = newNeighbor;
		neighborEastId = newNeighbor.getIdNumber();
	}
	
	public Module getSouthNeighbor() {
		return neighborSouth;
	}
	public Integer getSouthNeighborId() {
		return neighborSouthId;
	}
	public void setSouthNeighbor(Module newNeighbor) {
		neighborSouth = newNeighbor;
		neighborSouthId = newNeighbor.getIdNumber();
	}
	
	public Module getWestNeighbor() {
		return neighborWest;
	}
	public Integer getWestNeighborId() {
		return neighborWestId;
	}
	public void setWestNeighbor(Module newNeighbor) {
		neighborWest = newNeighbor;
		neighborWestId = newNeighbor.getIdNumber();
	}
	
	public String getType() {
		return type;
	}
}