import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.net.URL;

public class Background {

	//attributes
	private int x, y, vx, vy, width, height;
	private Image img;
	private AffineTransform tx = AffineTransform.getTranslateInstance(x, y);
	private boolean isEnvironment, isNextEnvironment;
	
	//constructors
	public Background(String fileName) {
		
		//attributes
		x = 0; 
		y = 0;
		vx = 0;
		vy = 0;
		width = 0;
		height = 0;
		isEnvironment = false;
		isNextEnvironment = false;
		
		//do not touch
		img = getImage(fileName);
		updateBackground();
		
	}

	public Background(String fileName, int xPos, int yPos) {
		
		//call default constructor
		this(fileName);
		
		//modify x and y
		x = xPos;
		y = yPos;
		
		//update
		updateBackground();
		
	}
	
	//methods
	public void paint(Graphics g) {	
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(img, tx, null);
	}
	
	private void updateBackground() {
		tx.setToTranslation(x, y);		
	}
	
	private Image getImage(String path) {
		Image tempImage = null;
		try {
			URL imageURL = Player.class.getResource(path);
			tempImage = Toolkit.getDefaultToolkit().getImage(imageURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempImage;
	}
	
	public void backgroundMover(){
		
		//move background to the left
		x -= 5;
		updateBackground();
		
		//if out of visibility move it back to 1200
		if(x + 1200 <= 0){
			x = 1200;
			updateBackground();		
		}
		
	}
	
	public void changeEnvrionment(String fileName, boolean b){
		if(b){
			img = getImage(fileName);
			updateBackground();
		}
	}
	
	//getters
	public int getX() {
		return x;		
	}
	
	public int getY() {
		return y;		
	}
	
	public int getVx() {
		return vx;		
	}
	
	public int getVy() {
		return vy;		
	}

	public boolean getEnvironment(){
		return isEnvironment;
	}
	
	public boolean getNextEnvironment(){
		return isNextEnvironment;
	}
	
	public boolean getVoid(){
		return isEnvironment;
	}
	
	//setters
	public void setVx(int newVx){
		vx = newVx;
	}
	
	public void setX(int newX){
		x = newX;
		updateBackground();
	}
	
	public void setY(int newY){
		y = newY;
		updateBackground();
	}

	public void setEnvironment(boolean d){
		isEnvironment = d;
	}
	
	public void setNextEnvironment(boolean d){
		isNextEnvironment = d;
	}
	
}
