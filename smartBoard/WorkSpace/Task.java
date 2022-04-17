package smartBoard.WorkSpace;

import java.util.LinkedHashSet;

public class Task extends WorkSpaceManager implements WorkSpaceHub, Comparable<Task> {

	//private String name;
	private String description;
	
	private LinkedHashSet<CheckList> checkLists;
	
	public Task(String taskName, String description ) {
			
		super(taskName);
		this.description = description;
		this.checkLists = new LinkedHashSet<CheckList>();
				
	}
	
	public Task(String taskName) {
			
		super(taskName);
		this.description = "";
		this.checkLists = new LinkedHashSet<CheckList>();
		
	}
	
	/*
	 * Add a CheckList item to the Task.
	 */
	public void add(WorkSpaceHub checkList) throws IllegalArgumentException {
		
		if (checkList instanceof CheckList) {
			
			this.checkLists.add((CheckList)checkList);
			
		} else {
			
			throw new IllegalArgumentException();
		}
		
	}
	
		
	/*
	 * Getters
	 */
			
	/*
	 * Get task description.
	 */
	public String getDescription() {
		return this.description;
	}
	
	/*
	 * Get task name.
	 */
	/*public String getName() {
		return this.name;
	}*/
	


	public int compareTo(Task workspace) {
	
		return getName().compareTo(workspace.getName());
	
	}

	/*
	 * Setters	
	 */
	
	/*
	 * Set description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
