import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.net.URL;

public class Bullet {

	//attributes
	private int x, y, vx, vy, width, height, damage, xDis, yDis;
	public boolean live;
	private Image img;
	private AffineTransform tx = AffineTransform.getTranslateInstance(x, y);
	private boolean friendOrFoe; //friend = true, foe = false
	
	//constructors
	public Bullet(String fileName, int w, int h, int xD, int yD) {
		
		//attributes
		x = 1200; 
		y = 800;
		vx = 0;
		vy = 0;
		width = w;
		height = h;
		friendOrFoe = true;
		live = false;
		damage = 1;
		xDis = xD;
		yDis = yD;
		
		//do not touch
		img = getImage(fileName);
		updateBullet();
		
	}
	
	//methods
	public void paint(Graphics g) {
		
		//draws bullet
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(img, tx, null);
		
		//moves bullet based on velocity
		x+=vx;
		
		//update image
		updateBullet();
		
	}
	
	private void updateBullet() {
		tx.setToTranslation(x, y);		
	}
	
	private Image getImage(String path) {
		Image tempImage = null;
		try {
			URL imageURL = Boss.class.getResource(path);
			tempImage = Toolkit.getDefaultToolkit().getImage(imageURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempImage;
	}

	public void move() {
		x += vx;
		y += vy;
		updateBullet();		
	}

	public void collision(Player p){
		Rectangle player = new Rectangle(p.getX() + 40, p.getY() + 16, p.getW(), p.getH());
		Rectangle bullet = new Rectangle(x + xDis, y + yDis, width, height);
		if(player.intersects(bullet)){
			Driver.life -= damage;
			p.setAlive(false);
			x = 1200;
			y = 800;
			vx = 0;
			vy = 0;
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
	
	public int getDamage(){
		return damage;		
	}
	
	public boolean getFriendOrFoe() {
		return friendOrFoe;		
	}

	//setters
	public void setDamage(int d){
		damage = d;		
	}
	
	public void setVx(int newVx){
		vx = newVx;
	}
	
	public void setX(int newX){
		x = newX;
	}
	
	public void setY(int newY){
		y = newY;
	}

}
