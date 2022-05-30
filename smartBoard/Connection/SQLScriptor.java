package smartBoard.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;

import utilities.FXUtilities;

/*
 * Builds SQL statements. Helps prevents basic syntax errors. Allow for future database
 * platform changes.
 */
public class SQLScriptor {

	private FXUtilities util;
	private String script;
		
	public SQLScriptor() {
	
		this.util = new FXUtilities();
		this.script = "";
			
	}
	
	/*
	 * Build the Insert SQL statement. Helps prevents basic syntax errors.
	 */
	public String buildInsertScript(String tableName, String[] values) {

		this.script = "INSERT INTO ";

		this.script += tableName.toUpperCase();
		this.script += " VALUES(";

		for (String value : values) {

			/*
			 * I don't like this either. Cannot think of alternative way of
			 * dealing with intentionally blank fields. So stuck with this for now.
			 */
			if(!value.equals("<skip>")) {
				
				this.script += "'" + value + "',";
				
			}

		}

		this.script = this.script.substring(0, this.script.length() - 1);

		this.script += ")";
		
		return this.script;

	}
	
	/*
	 * Build the update script SQL statement. Helps prevents basic syntax errors.
	 * This uses MetaTableData column names. See SQLMetaData class.
	 */
	public String buildUpdateScript(SBConnectionManager connection, String tableName, String[] keys, String[] keyNames,  String[] colValues) throws SQLException  {

		
		String[] colNames;
		
		try {
			
			connection.createSBConnection();
			connection.createSBStatement();
			
			SQLMetaData sqlMeta = SQLMetaData.getInstance();
			
			colNames = sqlMeta.getMetaData(tableName);
			
			if(colNames == null) {
			
				ResultSet rs = connection.executeQuery(buildMetaDataScript(tableName));
				sqlMeta.setMetaData(tableName, rs);
				colNames = sqlMeta.getMetaData(tableName);
				
			}
						
			this.script = "UPDATE " + tableName.toUpperCase() + " SET "; 
			
			int i = 0;
			
			for(String value : colValues) {
				
				/*
				 * I don't like this either. Cannot think of alternative way of
				 * dealing with intentionally blank fields. So stuck with this for now.
				 */
				if(!value.equals("<skip>")) {
							
					this.script += colNames[i] + " = '" + value + "', ";
					
				} 
				
				i++;
										
			}
			
			this.script = this.script.substring(0, this.script.length() - 2);
			
			this.script += " WHERE ";
			
			/*String key= "";
			String keyName = "";
			
			for(int j = 0; j<keyNames.length ; j++) {
				
				key = keys[j];
				keyName = keyNames[j];
				
				script += keyName + " = '" + key + "'";
				
				if(j+1!=keyNames.length) {
					
					script += " AND ";
					
				}
				
			}*/
			
			this.script += addKeyNames(keyNames, keys);
							
		}
		catch(SQLException sqle) {
			
			throw new SQLException("Unable to build table column values");
			
		}
		
		return script;
		
	}
	
	/*
	 * Build a basic select query. Helps prevents basic syntax errors. 
	 */
	public String buildBasicSelectQuery(String tableName, String[] whereName, String[] whereValue, String orderBy) {
		
		this.script = "SELECT * FROM " + tableName.toUpperCase() + " WHERE "; 
		
		/*if(!this.util.isStringFieldEmpty(whereName) && !this.util.isStringFieldEmpty(whereValue)) {
			
			this.script += " WHERE " + whereName + " = '" + whereValue + "'";
			
		}*/
		
		this.script += addKeyNames(whereName, whereValue);
		
		if(!this.util.isStringFieldEmpty(orderBy)) {
			
			this.script += " ORDER BY " + orderBy;
		}
						
		return this.script;
		
	}
	
	/*
	 * Build a basic Delete script.
	 */
	public String buildDeleteScript(String tableName, String[] keyNames, String[] keys) {
		
		this.script = "DELETE FROM " + tableName + " WHERE ";
		this.script += addKeyNames(keyNames, keys);
		       
		return script;
		
	}
	
	public String buildRecordExists(String tableName, String[] keyNames, String[] keys) {
		
		this.script = "SELECT EXISTS(SELECT 1 FROM "+ tableName + " WHERE ";

	/*	String keyName = "", keyValue = "";
		
		for(int i = 0; i<keyNames.length; i++) {
			
			keyName = keyNames[i];
			keyValue = keys[i];
			
			script += keyName;
			script += " = '" + keyValue + "'";
			
			if(i+1!=keyNames.length) {
				
				script += " AND ";
				
			}
		}*/
		
		this.script += addKeyNames(keyNames, keys);
		
		this.script += ")";
		
		//System.out.println("Record Exists " + script);
		
		return script;
		
	}
	
	public String buildMetaDataScript(String tableName) {
		
		return "SELECT * FROM " + tableName;
		
	}
	
	public String addKeyNames(String[] keyNames, String[] keys) {
		
		String key= "", keyName = "", script = "";
		
		for(int j = 0; j<keyNames.length ; j++) {
			
			key = keys[j];
			keyName = keyNames[j];
			
			script += keyName + " = '" + key + "'";
			
			if(j+1!=keyNames.length) {
				
				script += " AND ";
				
			}
			
		}
		
		return script;
	}
	
}
