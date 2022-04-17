package smartBoard.WorkSpace;

import utilities.ConsoleUtilities;

/*
 * Factory for creating a new instance of a WorkSpace item or task.
 */
public class WorkSpaceInitiator {

	private ConsoleUtilities consoleUtils;
	
	public WorkSpaceInitiator() {
		
		this.consoleUtils = new ConsoleUtilities();
		
	}
	
	public WorkSpaceHub createWorkSpaceItem(String itemType, String name) throws IllegalArgumentException{
		
		WorkSpaceHub hub = null;
		
		if(!this.consoleUtils.isStringFieldEmpty(name)) {
		
			switch(itemType) {
		
			case "Project":
				hub = new Project(name);
				break;
			case "Basket":
				hub = new Basket(name);
				break;
			case "Task":
				hub = new Task(name);
				break;
			default:
				throw new IllegalArgumentException();
			
			}
			
		} else {
			
			throw new IllegalArgumentException();
			
		}
		
		return hub;
		
	}
}
