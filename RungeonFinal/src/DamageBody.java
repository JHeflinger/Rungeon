import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.net.URL;

public class DamageBody {

	//attributes
	private int x, y; 
	private Image img; 			
	private AffineTransform tx = AffineTransform.getTranslateInstance(x, y);

	//constructors
	public DamageBody(String fileName) {
		
		//attributes
		x = 200;
		y = 200;
		
		//do not touch
		img = getImage(fileName);
		updateEnemy();
		
	}

	public DamageBody(String fileName, int posX, int posY){
		
		//calls default constructor
		this(fileName); 
		
		//sets parameters
		x = posX;
		y = posY;
		
		//any time you alter x or y you must call updateEnemy()
		updateEnemy();
		
	}
	
	//methods
	
	public void paint(Graphics g) {	
		//draw enemy
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(img, tx, null);
		
	}
	
	private void updateEnemy() {
		tx.setToTranslation(x, y);		
	}
	
	private Image getImage(String path) {
		Image tempImage = null;
		try {
			URL imageURL = DamageBody.class.getResource(path);
			tempImage = Toolkit.getDefaultToolkit().getImage(imageURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempImage;
	}
	
	//getters
	public int getX() {
		return x;	
	}
	
	public int getY(){
		return y;		
	}
	
	//setters
	public void setX(int x) {
		this.x = x;
		updateEnemy();		
	}

	public void setY(int y) {
		this.y = y;
		updateEnemy();
	}
	
}
