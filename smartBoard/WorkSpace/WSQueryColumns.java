package smartBoard.WorkSpace;

/*
 * Stores query column result values. 
 */
public class WSQueryColumns {

	
	/*
	 * Query result columns from
	 * 
	 * SELECT * FROM PROJECT WHERE USERNAME = "user_name" ORDER BY REFERENCENO 
	 * SELECT * FROM BASKET  WHERE USERNAME = "user_name" ORDER BY REFERENCENO
	 * SELECT * FROM TASK    WHERE USERNAME = "user_name" ORDER BY REFERENCENO
	 *
	 */
	
	// Generic header fields used in WorkSpace super class only.
	public static final int WS_NAME              = 1;
	public static final int WS_CREATE_DATE       = 2;
	public static final int WS_CREATE_tIME       = 3;
	public static final int WS_USERNAME          = 4;
	public static final int WS_REFERENCE_NO      = 5;
	public static final int WS_DELETED           = 6;
	public static final int WS_PARENT_NAME       = 7;
	
	// Project column index values
	public static final int PJT_NAME             = 1;
	public static final int PJT_CREATE_DATE      = 2;
	public static final int PJT_CREATE_TIME      = 3;
	public static final int PJT_USERNAME         = 4;
	public static final int PJT_REFERENCE_NO     = 5;
	public static final int PJT_DELETED          = 6;
	
	// Basket column index values
	public static final int BSK_NAME 			 = 1;
	public static final int BSK_CREATE_DATE 	 = 2;
	public static final int BSK_CREATE_TIME      = 3;
	public static final int BSK_USERNAME         = 4;
	public static final int BSK_REFERENCE_NO     = 5;
	public static final int BSK_DELETED          = 6;
	public static final int BSK_PARENT_NAME      = 7;
	
	// Task column index values.
	public static final int TSK_NAME             = 1;
	public static final int TSK_CREATE_DATE      = 2;
	public static final int TSK_CREATE_TIME      = 4;
	public static final int TSK_USERNAME         = 4;
	public static final int TSK_REFERENCE_NO     = 5;
	public static final int TSK_DELETED          = 6;
	public static final int TSK_PARENT_NAME      = 7;
	public static final int TSK_PROJECT_NAME     = 8;
	public static final int TSK_DESCRIPTION      = 9;
	public static final int TSK_SCHEDULED_DATE   = 10;
	public static final int TSK_COMPLETED_DATE   = 11;
	
}
