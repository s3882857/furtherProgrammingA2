package smartBoard.User;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/*
 * Store the profile photo of the User.
 * This is held statically. A single instance.
 */
public class Photo {

	private static Photo photo;
	private String filePhotoName;
	private BufferedImage profileImage;
	private File file; 
	private File storedFile;
		
	private Photo() {
				
		this.file = null;
		this.profileImage = null;
		this.filePhotoName = "";
		this.storedFile = null;
		
	}
			
	/*
	 *  Start writing profile photo image.
	 */
	public boolean startWriteProfilePhoto() {

		boolean writeOK = false;
		
		try {
			if(this.profileImage!=null) {
				
				writeOK = WriteProfilePhoto();
				
			} else {
			
				this.profileImage = getBufferedImage();
				writeOK = WriteProfilePhoto();
				
			}
		}
		catch(IOException ioe) {
			// Just catch the Exception. Allow method to just return false. 
		}
		
		return writeOK;
		
	}
	
	/*
	 * Write and resize the image file 
	 * Code source from https://mkyong.com/java/how-to-write-an-image-to-file-imageio/ 
	 * (mkyong.com, 2020)
	 * Also see readme.txt file for full references.
	 */
	private boolean WriteProfilePhoto() throws IOException {
		
		this.storedFile = new File("./" + User.getInstance().getUserName() + "_" + this.filePhotoName);
		
		Image resizeImage = this.profileImage.getScaledInstance(350,320 , Image.SCALE_DEFAULT);
		
		return ImageIO.write(convertToBufferedImage(resizeImage), "png", this.storedFile);
		
	}
	
	/*
	 * Convert the Image back into a BufferedImage
	 * Code source from https://mkyong.com/java/how-to-write-an-image-to-file-imageio/ 
	 * (mkyong.com, 2020)
	 * Also see readme.txt file for full references.
	 */
    private static BufferedImage convertToBufferedImage(Image img) {

        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics2D = bi.createGraphics();
        
        graphics2D.drawImage(img, 0, 0, null);
        graphics2D.dispose();

        return bi;
        
    }
    
	/*
	 * Getters
	 */
    
    /*
	 *  Read photo. If the file exists and the Image has not been built 
	 *  then build a new Image. There is only one version in existence.
	 */
	public BufferedImage getBufferedImage() throws IOException {
		
		if(this.profileImage==null) {
			
			this.file = new File("./" + this.filePhotoName);
					
			if(this.file.exists()) {
				
				this.profileImage = ImageIO.read(file);
		
			} else {
				
				throw new IOException("File " + this.filePhotoName + " does not exist.");
				
			}
			
		}
		
		return this.profileImage;
		
	}
	
	/*
	 * Get a static instance. Only need one instance of the profile image.
	 */
	public synchronized static Photo getInstance() {
		
		if(Photo.photo==null) {
			
			Photo.photo = new Photo();
			
		}
		
		return Photo.photo;
		
	}
	/*
	 * Get file photo name profile photo.
	 */
	public String getFilePhotoName() {
		return this.filePhotoName;
	}
	
	/*
	 * Setters
	 * 
	 */
	
	/*
	 * Set file photo name. Profile picture. By resetting or setting the 
	 * photo name this will force a rebuild of the BufferedImage. Check to 
	 * ensure that it has changed first.
	 */
	public void setFilePhotoName(String filePhotoName) {
		
		if(!filePhotoName.equals(this.filePhotoName)) {
			
			this.filePhotoName = filePhotoName;
		
			this.profileImage = null;
			
		}
		
	}
	
}
