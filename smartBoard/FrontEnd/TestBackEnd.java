package smartBoard.FrontEnd;

import smartBoard.BackEnd;
import smartBoard.User.SmartBoardLoginException;

public class TestBackEnd {

	private BackEnd backEnd;
	
	public static void main(String[] args) throws SmartBoardLoginException{
		
	//	try {
			
			TestBackEnd tb = new TestBackEnd();
	//	}
		//catch()
			
		System.exit(0);
		
	}
	
	public TestBackEnd() throws SmartBoardLoginException {
		
		this.backEnd = new BackEnd();
		
	}

}
