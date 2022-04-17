package smartBoard.WorkSpace;


import java.util.LinkedHashSet;

public class Project extends WorkSpaceManager implements WorkSpaceHub, Comparable<Project> {

	private LinkedHashSet<Basket> baskets;
	
	//private String name;

	
	public Project(String projectName) {
			
		super(projectName);
		
		this.baskets = new LinkedHashSet<Basket>();
				
	}

	public int compareTo(Project workspace) {
		
		return getName().compareTo(workspace.getName());
		
	}
	
	/*
	 * Add a new Basket to the Project workspace 
	 */
	public void add(WorkSpaceHub workSpace) throws IllegalArgumentException {
		
		if(workSpace instanceof Basket) {
			
			this.baskets.add((Basket) workSpace);
			
		} else {
			
			throw new IllegalArgumentException();
			
		}
		
	}
	
	/*
	 * Find Basket. Exact match search.
	 */
	public Basket findBasket(String searchKey) {
		
		Basket baskObj = null;
		
		for(Basket basket : this.baskets) {
			
			if(searchKey.equalsIgnoreCase(basket.getName())) {
				baskObj = basket;
				break;
			}
		}
		
		return baskObj;
		
	}
	
	/*
	 * Getters
	 */
	
	/*
	 * get the project name
	 */
	/*public String getName() {
		return this.name;
	}*/

	/*
	 * Get the list of baskets belonging to this project.
	 */
	public LinkedHashSet<Basket> getBaskets(){
		return this.baskets;
	}
}
