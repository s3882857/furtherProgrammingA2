package smartBoard.WorkSpace;

import utilities.FXUtilities;

/*
 * Factory for creating a new instance of a WorkSpace item or task.
 */
public class WorkSpaceInitiator {

	private FXUtilities utils;
	
	public WorkSpaceInitiator() {
		
		this.utils = new FXUtilities();
		
	}
	
	public WorkSpace createWorkSpaceItem(String itemType, String name) throws IllegalArgumentException{
		
		WorkSpace hub = null;
		
		if(!this.utils.isStringFieldEmpty(name)) {
		
			switch(itemType) {
		
			case "Project":
		//		hub = new Project(name);
				break;
			case "Basket":
			//	hub = new Basket(name);
				break;
			case "Task":
				//hub = new Task(name);
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
