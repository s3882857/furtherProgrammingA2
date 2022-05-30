package smartBoard.Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * Connection Manager class
 * Handles all connection requests to the Smart-Board database.
 */
public class SBConnectionManager {

	private Connection connection;
	private Statement statement;

	private static final String connectDriver = "jdbc";
	private static final String connectDB = "sqlite";
	private static final String connectDBName = "SmartBoard.db";
	private int timeOut;
	private String nameDBConnection;
	private int verbose;
	private SQLScriptor scripts;
	boolean executedOK = true;

	public SBConnectionManager() {

		this.connection = null;
		this.nameDBConnection = SBConnectionManager.connectDriver + ":" + SBConnectionManager.connectDB + ":" + SBConnectionManager.connectDBName;
		this.timeOut = 30;
		this.verbose = 0;
		this.scripts = new SQLScriptor();

	}

	/*
	 * Get a connection from the SmartBoard database (SqLite3 at present)
	 * Synchronized to prevent potential thread clashes.
	 */
	public synchronized void createSBConnection() throws SQLException {

		// Make sure any previous connection was not left open.
		if(this.connection != null) {
			try {
				this.close();
			}
			catch(SQLException donotReturn) {}
		}
		
		this.connection = null;
		boolean executedOK = true;

		try {

			// create a new database connection
			this.connection = DriverManager.getConnection(this.nameDBConnection);
				

		} catch (SQLException sqle) {

			// if the error message is "out of memory",
			// it probably means no database file is found
			//System.err.println(e.getMessage());
			executedOK = false;
			checkErrorPrint(sqle);
			throw new SQLException(sqle);
			
		} finally {

			try {

				if (this.connection != null && !executedOK ) {
					this.connection.close();
				}

			} catch (SQLException sqle) {
				// connection close failed.
				//System.err.println(e.getMessage());
				//connectionOK = false;
				checkErrorPrint(sqle);
				throw new SQLException(sqle);
				
			}

		}

		//return connectionOK;

	}

	/*
	 * Create SmartBoard statement.
	 */
	public boolean createSBStatement() throws SQLException {
		
		this.executedOK = true;
		this.statement = null;
				
		try {
			
			this.statement = connection.createStatement();
			this.statement.setQueryTimeout(this.timeOut); // set timeout to 30 sec.
			
		}
		catch(SQLException sqle) {
			checkErrorPrint(sqle);
			this.executedOK = false;
		}
		finally {
			
			try {

				if (this.connection != null && !this.executedOK ) {
					this.connection.close();
				}

			} catch (SQLException sqle) {
				// connection close failed.
				//System.err.println(e.getMessage());
				//connectionOK = false;
				checkErrorPrint(sqle);
				throw new SQLException(sqle);
				
			}
		}
		
		return this.executedOK;
		
	}
	
	
	
	/*
	 * The same methods are used here this will allow for any future separation  
	 * of the code, cater for any potential future divergences.
	 *  
	 * Execute insert record. Add new record.
	 */
	public void executeInsert(String script) throws SQLException{

		this.executedOK = true;
		
		try {

			//this.statement.executeUpdate(script);
			this.executeUpdate(script);

		} catch (SQLException updateException) {
			checkErrorPrint(updateException);
			this.executedOK = false;
			throw new SQLException(updateException);
			//System.out.println(updateException.getMessage());
		} 
		finally {
			
			try {

				if (this.connection != null && !this.executedOK ) {
					this.connection.close();
				}

			} catch (SQLException sqle) {
				// connection close failed.
				//System.err.println(e.getMessage());
				//connectionOK = false;
				checkErrorPrint(sqle);
				throw new SQLException(sqle);
				
			}
		}

		
	}

	/*
	 * Execute Update record. Update an existing record. 
	 */
	public void executeUpdate(String script) throws SQLException {

		this.executedOK = true;
		
		try {

			this.statement.executeUpdate(script);

		} catch (SQLException updateException) {
			checkErrorPrint(updateException);
			this.executedOK = false;
			//System.out.println(updateException.getMessage());
			throw new SQLException(updateException);
			
		}
		finally {
			
			try {

				if (this.connection != null && !this.executedOK ) {
					this.connection.close();
				}

			} catch (SQLException sqle) {
				// connection close failed.
				//System.err.println(e.getMessage());
				//connectionOK = false;
				checkErrorPrint(sqle);
				throw new SQLException(sqle);
				
			}
		}

				
	}
	
	/*
	 * Execute Query.
	 */
	public ResultSet executeQuery(String script) throws SQLException {

		this.executedOK = true;
		
		ResultSet rs = null;

		try {

			rs = this.statement.executeQuery(script);
			
		} catch (SQLException rse) {
			//System.out.println(rse.getMessage());
			checkErrorPrint(rse);
			this.executedOK = false;
			throw new SQLException(rse);
			
		}
		finally {
			
			try {

				if (this.connection != null && !this.executedOK ) {
					this.connection.close();
				}

			} catch (SQLException sqle) {
				// connection close failed.
				//System.err.println(e.getMessage());
				//connectionOK = false;
				checkErrorPrint(sqle);
				throw new SQLException(sqle);
				
			}
		}

		return rs;

	}
	
	/*
	 * Execute a delete script.
	 */
	public void executeDelete(String script) throws SQLException {
		
		this.executedOK = true;
		try {
			
			this.statement.execute(script);
		}
		catch (SQLException sqle) {
			this.executedOK = false;
			checkErrorPrint(sqle);
			throw new SQLException(sqle);
			
		}
		finally {
			
			try {

				if (this.connection != null && !this.executedOK ) {
					this.connection.close();
				}

			} catch (SQLException sqle) {
				// connection close failed.
				//System.err.println(e.getMessage());
				//connectionOK = false;
				checkErrorPrint(sqle);
				throw new SQLException(sqle);
				
			}
		}
	}
	
	/*
	 * Check if the record already exists in the database.
	 */
	public boolean existingRecord(String tableName, String[] keys, String[] keyNames) throws SQLException {
		
		int numberRecords = 0;
		boolean recordExists = true;
		this.executedOK = true;
				
		String script = this.scripts.buildRecordExists(tableName, keyNames, keys);
		
		try {
			
			ResultSet rs = this.executeQuery(script);
			
			while(rs.next()) {
				numberRecords = rs.getInt(1);
			}
			
			if(numberRecords==1) {
				recordExists = true;
			} else {
				recordExists = false;
			}
			
		}
		catch(SQLException noRecordFound) {
			recordExists = false;
			//System.out.println(noRecordFound.getMessage());
			checkErrorPrint(noRecordFound);
			this.executedOK = false;
			
			throw new SQLException(noRecordFound);
			
		}
		finally {
			
			try {

				if (this.connection != null && !this.executedOK ) {
					this.connection.close();
				}

			} catch (SQLException sqle) {
				// connection close failed.
				//System.err.println(e.getMessage());
				//connectionOK = false;
				checkErrorPrint(sqle);
				throw new SQLException(sqle);
				
			}
		}
		
		
		return recordExists;
		
	}

	/*
	 * Close the connection.
	 */
	public void close() throws SQLException {

		
		try {

			this.connection.close();
		

		} catch (SQLException closeException) {
		
			checkErrorPrint(closeException);
			throw new SQLException(closeException);
			
		}
	}

	/*
	 * Check verbose set to print errors.
	 */
	public void checkErrorPrint(Exception e) {
		
		if(this.verbose==1) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		
	}
	/*
	 * 
	 * Set timeout period in seconds. Default is 30 seconds.
	 */
	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	/*
	 * Set the name of the database to connect to. Only use if you want to connect to
	 * another database name. Currently the default is connectDBName = "SmartBoard.db"
	 */
	public void setDBName(String nameDB) {
		this.nameDBConnection = SBConnectionManager.connectDriver + ":" + SBConnectionManager.connectDB + ":" + nameDB;
	}

	/*
	 * Get DB Connection string.
	 */
	public String getDBConnectionString() {
		return this.nameDBConnection;
	}

	/*
	 * Get timeout in seconds.
	 */
	public int getTimeout() {
		return this.timeOut;
	}
	
	/*
	 * Set verbose. Prints all errors to console.
	 */
	public void setDebugOn() {
		this.verbose = 1;
	}
	
	/*
	 * turn debug off.
	 */
	public void setDebugOff() {
		this.verbose = 0;
	}
}
