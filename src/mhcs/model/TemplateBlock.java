
package mhcs.model;

public class TemplateBlock implements Comparable<TemplateBlock> {
	private Integer weight;		//order with which it is handled
	private Integer relativeX;	// x in relation to the key block
	private Integer relativeY;	// y in relation to the key block
	private Module module;		// the module contained within the template block
	private String type;		// the preferred type of module in the block
	
	public TemplateBlock(Integer priority, Integer xCoord, Integer yCoord, String preferredType)
	{
		weight = priority;
		relativeX = xCoord;
		relativeY = yCoord;
		type = preferredType;
	}
	
	public Integer getPriority() {
		return weight;
	}
	public void setPriority(Integer priority) {
		weight = priority;
	}
	
	public Integer getXOffset() {
		return relativeX;
	}
	public void setXOffset(Integer offset) {
		relativeX = offset;
	}
	
	public Integer getYOffset() {
		return relativeY;
	}
	public void setYOffset(Integer offset) {
		relativeY = offset;
	}
	
	public String getPreferredModuleType() {
		return type;
	}
	
	public Module getContainedModule() {
		return module;
	}
	public void setContainedModule(Module newModule) {
		module = newModule;
	}
	
	public String toString() {
		String result = "";
		result = "Priority:" + weight + " X:" + relativeX + " Y:" + relativeY + " Pref:" + type + " ";
		result += module.toString();
		
		return result;
	}
	
	public int compareTo(TemplateBlock otherBlock) {
		return (weight - otherBlock.weight);
	}
}