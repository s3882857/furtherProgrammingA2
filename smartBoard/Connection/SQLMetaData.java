package smartBoard.Connection;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * Stores meta table data relating to each table within the database.
 * Stores a singleton version of itself so the setMetaData method 
 * only needs to be executed once for the duration of the program.
 * This is done to reduce the number of database requests to obtain the 
 * column names of a table. Enabling the removal of hard-coded column names
 * utilized within SQLScriptor class. 
 * So far only storing Column names.
 */
public class SQLMetaData {
	
	private static SQLMetaData sqlMetaData;

	private ArrayList<MetaTableStorage> arrList;
	
	private SQLMetaData() {
		
		this.arrList = new ArrayList<MetaTableStorage>();
		
	}
	
	/*
	 * Get a singleton of this so the table column names are only 
	 * loaded once for the duration of the program.
	 */
	public synchronized static SQLMetaData getInstance() {
		
		if(SQLMetaData.sqlMetaData==null) {
			SQLMetaData.sqlMetaData = new SQLMetaData();
		}
		
		return SQLMetaData.sqlMetaData;
		
	}
	
	/*
	 * Get Meta Data
	 */
	public String[] getMetaData(String tableName) {

		MetaTableStorage storage = find(tableName);
		String[] colNames = null;		
		
		if(storage!=null) {
			colNames = storage.getColumnNames();
		}
		
		return colNames;
	
	}
	
	/*
	 * Set the Meta Data
	 */
	public void setMetaData(String tableName, ResultSet rs) throws SQLException {
		
		MetaTableStorage storage = new MetaTableStorage();
					
		storage = new MetaTableStorage();
		storage.setTableName(tableName);
		
		storage.setColumnNames(getTableColumnNames(rs));
		this.arrList.add(storage);
		
	}
	
	
	/*
	 * Get all the column names for a given database table 
	 */
	private String[] getTableColumnNames(ResultSet rs) throws SQLException {
		
		ResultSetMetaData rsmd = rs.getMetaData();
			
		int colMax = rsmd.getColumnCount();
		
		String[] colNames = new String[colMax];
		
		for(int i = 1; i < colMax + 1 ; i++) {
			
			colNames[i-1] = rsmd.getColumnName(i);
							
		}
		
		return colNames;
	}
	
	/*
	 * Find the MetaTableStorage class for a given database table. 
	 */
	private MetaTableStorage find(String tableName) {
		
		MetaTableStorage returnObj = null;
		
		for(MetaTableStorage storage : this.arrList) {
			
			if(storage.getTableName().equalsIgnoreCase(tableName)) {
				returnObj = storage;
				break;
			}
		}
		
		return returnObj;
		
	}
	
	
	/*
	 * Class that holds the Meta Table data relating to a given database table.
	 * Placed here as it relates specifically to the above class. Should not and cannot 
	 * be used anywhere else.
	 */
	class MetaTableStorage {
		
		
		public MetaTableStorage() {
			this.tableName = "";
			this.colNames = null;
		}
		private String tableName;
		private String[] colNames;
		
		public void setTableName(String tableName) {
			this.tableName = tableName;
		}
		
		public String getTableName() {
			return this.tableName;
		}
		
		public void setColumnNames(String[] colNames) {
			this.colNames = colNames;
		}
		
		public String[] getColumnNames() {
			return this.colNames;
		}
		
	}
}
