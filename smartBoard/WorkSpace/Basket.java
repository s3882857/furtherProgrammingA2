package smartBoard.WorkSpace;


import java.util.LinkedHashSet;

public class Basket extends WorkSpaceManager implements WorkSpaceHub, Comparable<Basket>{

	//private String name;
	
	private LinkedHashSet<Task> tasks;
	
	public Basket(String basketName) {
		
		super(basketName);
		this.tasks = new LinkedHashSet<Task>();
				
	}

	public int compareTo(Basket workspace) {
		
		return getName().compareTo(workspace.getName());
		
	}

	
	public Task findTask(String searchKey) {
		
		Task taskObj = null;
		
		for(Task task : this.tasks) {
			
			if(searchKey.equalsIgnoreCase(task.getName())) {
				taskObj = task;
				break;
			}
		}
	
		return taskObj;
		
	}

	
	/*
	 * Add a new Task to the Basket's WorkSpace. 
	 */
	public void add(WorkSpaceHub workSpace) throws IllegalArgumentException{
		
		if(workSpace instanceof Task) {
			
			this.tasks.add((Task) workSpace);
			
		} else {
			
			throw new IllegalArgumentException();
			
		}
	}
	
	/*
	 * Getters
	 */
	/*
	 * Get Basket Name.
	 */
	/*public String getName() {
		return this.name;
	}*/
	
	/*
	 * Get the list of tasks belonging to this Basket.
	 */
	public LinkedHashSet<Task> getTasks(){
		return this.tasks;				
	}
}
