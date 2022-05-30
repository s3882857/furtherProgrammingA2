package smartBoard.WorkSpace;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;

public class Project extends WorkSpace implements Comparable<Project> {

	private LinkedHashSet<Basket> baskets;

	private final static String TABLENAME = "PROJECT";
	
	
	/*
	 * Create a new Project item.
	 */
	public Project(String projectName) throws SQLException {

		super();

		this.baskets = new LinkedHashSet<Basket>();
		// this.name = projectName;
		super.setName(projectName);
		
	}

	/*
	 * Load a Project from database.
	 */
	public Project(ResultSet rs) throws SQLException {

		// super();
		
		readDB(rs);

		this.baskets = new LinkedHashSet<Basket>();

	}

	/*
	 * Write to the project table.
	 */
	public void writeDB() throws SQLException {

		if (!super.isRecordWritten()) {

			String scriptValue = "";
			String[] columnValues = super.getColumnValues();
			
			String name = getName();
			String userName = getUserName();
			
			
			String[] keyValues = {name,userName};
			String[] keyNames  = {KEYNAME,KEYNAME2};
			
			boolean recordExists = super.getConnection().existingRecord(TABLENAME, keyValues, keyNames);
			
			if (recordExists) {

				scriptValue = super.getSQLScriptor().buildUpdateScript(super.getConnection(), TABLENAME,
						keyValues, keyNames, columnValues);
						
				super.getConnection().executeUpdate(scriptValue);
						
			} else {
			
				scriptValue = super.getSQLScriptor().buildInsertScript(TABLENAME, columnValues);

				super.getConnection().executeInsert(scriptValue);
			
			}

			super.recordWritten();

		}

	}

	/*
	 * Read database data from the PROJECT table's result set
	 */
	public void readDB(ResultSet rs) throws SQLException {

		super.readDB(rs);

	}

	public int compareTo(Project workspace) {

		return super.getName().compareTo(workspace.getName());

	}

	/*
	 * Add a new Basket to the Project workspace
	 */
	public void add(WorkSpace workSpace) throws IllegalArgumentException {

		if (workSpace instanceof Basket) {

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

		for (Basket basket : this.baskets) {

			if (searchKey.equalsIgnoreCase(basket.getName()) && !basket.getDeleted()) {
				baskObj = basket;
				break;
			}
		}

		return baskObj;

	}

	/*
	 * Find Basket. Exact match search.
	 */
	public Basket findBasket(int index) {

		Basket baskObj = null;
		int indexCount = 0;
		
		for (Basket basket : this.baskets) {

			if(!basket.getDeleted()) {
			
				if (indexCount==index ) {
					baskObj = basket;
					break;
				}
				
				indexCount++;
				
			}
			
		}

		return baskObj;

	}

	
	/*
	 * delete project.
	 */
	public void deleteProject() throws SQLException {
		
		open();
		
		String[] keyNames = {KEYNAME,KEYNAME2};
		String[] keyValues = {getName(),getUserName()};
		
		super.getConnection().executeDelete(super.getSQLScriptor().buildDeleteScript(TABLENAME, keyNames, keyValues));
		
		close();
		
	}
	
	
	public int numberBaskets() {
		
		int numberBaskets = 0;
		
		for (Basket basket : this.baskets) {
			
			if(!basket.getDeleted()) {
				numberBaskets++;
			}
			
		}
		
		return numberBaskets;
		
	}
	/*
	 * Getters
	 */



	/*
	 * Get the list of baskets belonging to this project.
	 */
	public LinkedHashSet<Basket> getBaskets() {
		return this.baskets;
	}

	@Override
	public void open() throws SQLException {

		super.openSBConnection();

	}

	@Override
	public void close() throws SQLException {

		super.closeSBConnection();

	}
	
}
