import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.net.URL;

public class Player {

	//attributes
	private int x, y, vx, vy, width, height, time, spareInt; 
	private String str, normal, damaged;
	private boolean alive; 		
	private Image img; 			
	private AffineTransform tx = AffineTransform.getTranslateInstance(x, y);
	Bullet[] arrows = new Bullet[20];

	//constructors
	public Player(String fileName, String d) {		
	
		//attributes
		x = 200;
		y = 200;
		vx = 0;
		vy = 0;
		width = 52;
		height = 80;
		time = 0;
		str = "arrow.png";
		spareInt = 0;
		normal = fileName;
		damaged = d;
		
		//load arrows when character loads in
		for(int i = 0; i < arrows.length; i++) {
			arrows[i] = new Bullet(str, 0, 0, 0, 0);
		}
		//do not touch
		img = getImage(fileName);
		updatePlayer();
		
	}

	public Player(String fileName, String d, int posX, int posY, String s){
		
		//calls the first constructor above
		this(fileName, d); 
		
		//modifies x and y
		x = posX;
		y = posY;
		
		//set projectile 
		str = s;
		
		//any time you alter x or y you must call updatePlayer()
		updatePlayer();
		
	}
	
	//methods
	private void updatePlayer() {
		tx.setToTranslation(x, y);		
	}
	
	public void paint(Graphics g) {
		
		//draws player
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(img, tx, null);
		
		//draws arrows
		for(int i = 0; i < arrows.length; i++){	
			if(arrows[i].live){
				arrows[i].paint(g);			
			}			
		}
		
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
	
	public void shoot(){
		for(int i = 0; i < arrows.length; i++){
			if(arrows[i].live != true){
				arrows[i].live = true;
				arrows[i].setX(x + 100);
				arrows[i].setY(y + 60);
				arrows[i].setVx(10);
				break; 
			}
		}
	}
	
	public void reload(){		
		for(int i = 0; i < arrows.length; i++){			
			if(!arrows[i].live) {				
				arrows[i] = new Bullet(str, 0, 0, 0, 0);				
			}			
		}		
	}
	
	public void killArrows() {		
		for(int i = 0; i < arrows.length; i++) {			
			if(arrows[i].getX() > 1200 || arrows[i].getX() < 0) {				
				arrows[i].live = false;				
			}			
		}		
	}
	
	public void playerMovement(){	
		
		x += vx;
		y += vy;		
		updatePlayer();	
		
		//take damage img conversion
		if(alive){
			img = getImage(normal);
		}else{
			img = getImage(damaged);
		}
										
		//take damage register
		if(!alive){
			spareInt++;
			if(spareInt >= 6){
				spareInt = 0;
				alive = true;
			}
		}

	}
	
	public void timeMovement(){		
		time++;		
	}
	
	public void damage(Enemy e){	
		Rectangle enemy = new Rectangle(e.getX() + e.getXDisplacement(), e.getY() + e.getYDisplacement(), e.getW(), e.getH());
		Rectangle[] arrow = new Rectangle[arrows.length];		
		for(int i = 0; i < arrows.length; i++){			
			arrow[i] = new Rectangle(arrows[i].getX(), arrows[i].getY(), 48, 12);			
			if(arrow[i].intersects(enemy)){				
				arrows[i].setY(-100);
				e.setHealth(e.getHealth() - arrows[i].getDamage());			
				e.setAlive(false);
			}			
		}		
	}
	
	public void bossDamage(Boss b){	
		Rectangle boss = new Rectangle(b.getX() + b.getXDisplacement(), b.getY() + b.getYDisplacement(), b.getW(), b.getH());
		Rectangle[] arrow = new Rectangle[arrows.length];	
		for(int i = 0; i < arrows.length; i++){		
			arrow[i] = new Rectangle(arrows[i].getX(), arrows[i].getY(), 48, 12);		
			if(arrow[i].intersects(boss)){			
				arrows[i].setY(-100);
				b.setHealth(b.getHealth() - arrows[i].getDamage());
				b.setAlive(false);
			}	
		}	
	}
	
	public void borders(){
		
		//left border
		if(x < 0){
			x = 0;
		}
		
		//right border
		if(x > 1072){
			x = 1072;
		}
		
		//top border
		if(y < 50){
			y = 50;
		}
		
		//bottom border
		if(y > 550){
			y = 550;
		}
		
		updatePlayer();
		
	}
	
	//getters
	public int getX() {
		return x;	
	}
	
	public int getY(){
		return y;	
	}

	public void setX(int x) {
		this.x = x;
		updatePlayer();	
	}

	public void setY(int y) {
		this.y = y;
		updatePlayer();
	}
	
	public int whatTime(){		
		return time;		
	}
	
	public String getProjectile() {
		return str;
	}
	
	public int getW(){
		return width;
	}
	
	public int getH(){
		return height;
	}
	
	//setters
	public void setVy(int vy) {
		this.vy = vy;
	}
	
	public void setVx(int vx) {
		this.vx = vx;
	}

	public void setTime(int t){
		time = t;
	}
	
	public void setProjectile(String s) {
		str = s;
	}

	public void setAlive(boolean b){
		alive = b;
	}
}
