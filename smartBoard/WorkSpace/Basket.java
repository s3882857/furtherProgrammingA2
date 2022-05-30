package smartBoard.WorkSpace;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class Basket extends WorkSpace implements Comparable<Basket> {

	private final static String TABLENAME = "BASKET";

	private LinkedHashSet<Task> tasks;

	/*
	 * Create a new Basket item
	 */
	public Basket(String basketName, String projectName) {

		super();
		this.tasks = new LinkedHashSet<Task>();
		
		super.setName(basketName);
		super.setParentName(projectName);
		
	}

	/*
	 * Load basket item from database.
	 */
	public Basket(ResultSet rs) throws SQLException {

		this.tasks = new LinkedHashSet<Task>();

		readDB(rs);
	
	}

	public int compareTo(Basket workspace) {

		return super.getName().compareTo(workspace.getName());

	}

	public void readDB(ResultSet rs) throws SQLException {

		super.readDB(rs);

		// Cannot do this in the super class as only a basket or task will have this.
		super.setParentName(rs.getString(WSQueryColumns.WS_PARENT_NAME));

	}

	public Task findTask(String searchKey) {

		Task taskObj = null;

		for (Task task : this.tasks) {

			if (searchKey.equalsIgnoreCase(task.getName()) && !task.getDeleted()) {
				taskObj = task;
				break;
			}
		}

		return taskObj;

	}

	/*
	 * Add a new Task to the Basket's WorkSpace.
	 */
	public void add(WorkSpace workSpace) throws IllegalArgumentException {

		if (workSpace instanceof Task) {

			this.tasks.add((Task) workSpace);

		} else {

			throw new IllegalArgumentException();

		}
	}

	/*
	 * Getters
	 */

	/*
	 * Get column values.
	 */
	public String[] getColumnValues(String projectName) {

		String[] columnValues = super.getColumnValues();
		
		columnValues[6] = projectName;

		return columnValues;

	}

	/*
	 * Write basket to Database.
	 */
	public void writeDB() throws SQLException {

		if (!super.isRecordWritten()) {

			String scriptValue = "";
			String[] columnValues = getColumnValues(super.getParentName());

			String[] keyValues = {getName(),getUserName(),getParentName()};
			String[] keyNames  = {KEYNAME,KEYNAME2,KEYNAME3};
			
			boolean recordExists = super.getConnection().existingRecord(TABLENAME, keyValues, keyNames);

			if (recordExists) {

				scriptValue = super.getSQLScriptor().buildUpdateScript(super.getConnection(), TABLENAME, keyValues,
						keyNames, columnValues);
				super.getConnection().executeUpdate(scriptValue);
				
			} else {

				scriptValue = super.getSQLScriptor().buildInsertScript(TABLENAME, columnValues);
				super.getConnection().executeInsert(scriptValue);

			}

			super.recordWritten();

		}

	}

	public int numberTasks() {
		
		int numberTasks = 0;
		
		for (@SuppressWarnings("unused") Task task : this.tasks) {
			
			numberTasks++;
						
		}
		
		return numberTasks;
		
	}
	
	/*
	 * delete basket.
	 */
	public void deleteBasket() throws SQLException {

		open();
		
		String[] keyNames = {KEYNAME,KEYNAME2,KEYNAME3};
		String[] keyValues = {getName(),getUserName(),getParentName()};
		
		super.getConnection().executeDelete(super.getSQLScriptor().buildDeleteScript(TABLENAME, keyNames, keyValues));
		
		close();
		
	}
	
	/*
	 * Get the list of tasks belonging to this Basket.
	 */
	public LinkedHashSet<Task> getTasks() {
		return this.tasks;
	}

	@Override
	public void open() throws SQLException {

		super.openSBConnection();

	}

	@Override
	public void close() throws SQLException {

		super.closeSBConnection();

	}
	
	/*
	 * remove a task.
	 */
	public void removeTask(Task task) {
		
		this.tasks.remove(task);
		
	}
	
	/*
	 * Switch/swap tasks.
	 */
	public void switchTask(Task task1, Task task2) {
		
		this.tasks.remove(task1);
		this.tasks.remove(task2);
		
		
	}
	
	/*
	 * Set task list to a new list.
	 */
	public void setTaskList(ArrayList<Task> newList) {
		
		this.tasks = new LinkedHashSet<Task>();
		this.tasks.addAll(newList);
		
	}
	
	public void reName(String newName) {
		
		for(Task task : this.tasks) {
			
			task.setParentName(newName);
			
		}
		
		super.setName(newName);
		
	}
}
