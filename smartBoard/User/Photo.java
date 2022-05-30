package smartBoard.User;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import utilities.FXUtilities;


/*
 * Store the profile photo of the User.
 * This is held statically. A single instance.
 */
public class Photo {

	//private static Photo photo;
	
	private Image image;
	private File originalFile; 
	private File storedFile;
	private final String DIR = "Users"; 
	private String fileName;
	private FXUtilities util;
	
	/*
	 * Instantiate a blank photo profile object.
	 */
	public Photo() {
				
		this.originalFile = null;
		this.image = null;
		this.util = new FXUtilities();		
						
	}
	
	/*
	 * Instantiate the photo class with the image details and the user it relates too.
	 */
	public Photo(String fileImageName, String userName) throws IOException {
		
		this.originalFile = null;
		this.image = null;
		this.util = new FXUtilities();		
		
		setNewImage(fileImageName,userName);
	
	}
		
	/*
	 * Write the image file to a standard file location to protect against the  
	 * file being removed from its original location.
	 */
	public boolean writeImage(String userName) throws IOException {
		
		boolean writeOK = false;
		
		if(!this.util.isStringFieldEmpty(userName)) {
			
			this.storedFile = null;
			this.storedFile = new File(this.DIR + "//" + userName + "_" + this.fileName);
			
			BufferedImage bi = SwingFXUtils.fromFXImage(this.image,null);
			
			ImageIO.write(bi, this.storedFile.getName().split("\\.")[1], this.storedFile);
			
			writeOK = true;
			
		}
		
		return writeOK;
		
	}
	
	/*
	 * Read the image from storage location.
	 * 
	 * Should only be one file in existence. Need a generic way to find it
	 * without having to store the original filename in the database.
	 * Use the user name as a prefix to the original filename.
	 * Search using the user name.
	 */
	public boolean readImage(String userName) throws IOException {
		
		String[] fileList;
		String[] files = null;
		
		this.storedFile = new File(this.DIR + "\\" );
		File fileFound = null;
		
		fileList = this.storedFile.list();
		
		for(String file : fileList) {
			
			if(!this.util.isStringFieldEmpty(file)) {
				
				files = file.split("_");
				
				if(files[0].equals(userName)) {
					
					file = this.storedFile.getAbsolutePath() + "\\" + file;
					fileFound = new File(file);
					
					break;
				}
				
			}
			
		}
		
		return setImage(fileFound);
		
	}
	
	/*
	 * Remove any old images relating to this userName. Cleanup
	 */
	public void removeImage(String userName) {
		
		String[] fileList;
		
		this.storedFile = new File(this.DIR + "\\" );
		String[] files = null;
		
		fileList = this.storedFile.list();
		
		for (String fileName : fileList) {
			
			files = fileName.split("_");
	
			if(files[0].equals(userName)) {
				
				fileName = this.storedFile.getAbsolutePath() + "\\" + fileName;
							
			}
			
		}
		
	}
	
	/*
     *  Get the stored image.
	 */
	public Image getImage() throws IOException {
		
		if(this.image==null) {
			readImage(User.getInstance().getUserName());
		}
		
		return this.image;
		
	}
	
	/*
	 * Setters
	 */
	
	/*
	 * Set file photo name. Profile picture. 
	 */
	public void setNewImage(String fileImageName, String userName) throws IOException {
		
		removeImage(userName);
		
		this.originalFile = new File(fileImageName);
		this.fileName = this.originalFile.getName();
			
		
		setImage(this.originalFile);
		writeImage(userName);
						
	}
	
	/*
	 * Set the image from a File object.
	 */
	private boolean setImage(File file) throws IOException {
		
		boolean imageOK = false;
		
		if(file!=null) {
		
			if(file.exists()) {
				
				FileInputStream fis = new FileInputStream(file);
				
				this.image = new Image(fis);
				
				fis.close();
								
				imageOK = true;
				
			}
			
		}
		
		return imageOK;
		
	}
}