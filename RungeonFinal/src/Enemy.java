import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.net.URL;

public class Enemy {

	//attributes
	private int x, y, vx, vy, width, height, time, time2, ranX, xDisplacement, yDisplacement, health, spareInt, bulletW, bulletH, bulletXd, bulletYd; 
	private boolean alive, complexType, simpleType; 		
	private Image img; 			
	private AffineTransform tx = AffineTransform.getTranslateInstance(x, y);
	private String normal, damaged; 
	Bullet singleShot;
	Bullet[] complexShot = new Bullet[3];

	//constructors
	public Enemy(String fileName) {
		
		//attributes
		x = 200;
		y = 200;
		vx = 0;
		vy = 0;
		width = 10;
		height = 10;
		time = 0;
		ranX = 0;
		xDisplacement = 0;
		yDisplacement = 0;
		health = 10;
		complexType = false;
		simpleType = false;
		time2 = 0;
		normal = fileName;
		alive = true;
		
		//do not touch
		img = getImage(fileName);
		updateEnemy();
		
	}

	public Enemy(String fileName, String d, int posX, int posY, int newW, int newH, int xDis, int yDis, int h, int bulW, int bulH, int bulXd, int bulYd){
		
		//calls default constructor
		this(fileName); 
		
		//sets parameters
		x = posX;
		y = posY;
		width = newW;
		height = newH;
		xDisplacement = xDis;
		yDisplacement = yDis;
		health = h;
		damaged = d;
		bulletW = bulW;
		bulletH = bulH;
		bulletXd = bulXd;
		bulletYd = bulYd;
		
		//any time you alter x or y you must call updateEnemy()
		updateEnemy();
		
	}
	
	//methods
	
	public void paint(Graphics g) {
		
		//draw enemy
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(img, tx, null);
		
		//if slime type draw slime ball
		if(simpleType) {
			singleShot.paint(g);
		}
		
		//if fire type draw fire ball
		if(complexType){
			for(int i = 0; i < complexShot.length; i++){
				complexShot[i].paint(g);
			}			
		}
		
	}
	
	private void updateEnemy() {
		tx.setToTranslation(x, y);		
	}
	
	private Image getImage(String path) {
		Image tempImage = null;
		try {
			URL imageURL = Enemy.class.getResource(path);
			tempImage = Toolkit.getDefaultToolkit().getImage(imageURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempImage;
	}
	
	public void useComplexShot() {
		for(int i = 0; i < complexShot.length; i++) {	
			if(!complexShot[i].live) {
				complexShot[i].setX(x);
				complexShot[i].setY(y + 60);
				complexShot[i].setVx(-10);
				complexShot[i].live = true;
				break;
			}			
		}		
	}
	
	public void useSingleShot() {
			singleShot.setX(x);
			singleShot.setY(y + 64);
			singleShot.setVx(-10);
	}
	
	public void loadComplexShot(String fileName) {
		for(int i = 0; i < complexShot.length; i++){			
			complexShot[i] = new Bullet(fileName, bulletW, bulletH, bulletXd, bulletYd);
			complexShot[i].setX(-100);
			complexShot[i].setY(-100);
			complexType = true;			
		}		
	}

	public void loadSingleShot(String fileName) {
		singleShot = new Bullet(fileName, bulletW, bulletH, bulletXd, bulletYd);
		singleShot.setX(-100);
		singleShot.setY(-100);
		simpleType = true;
	}
	
	public void crashEnemyAI(int playerY, Player p){
		
		//collision
		Rectangle player = new Rectangle(p.getX() + 40, p.getY() + 16, p.getW(), p.getH());
		Rectangle enemy = new Rectangle(x + xDisplacement, y + yDisplacement, width, height);
		if(player.intersects(enemy)){
			Driver.life -= 2;
			x = 1200;
			y = 1200;
			vx = 0;
			vy = 0;
			health = 0;
			p.setAlive(false);
		}
		
		//if alive
		if(health > 0){
			
			//go towards player
			if(y < playerY){
				y += 3;
			}else if(y > playerY){
				y -= 3;
			}	
			x -= 12;
					
		//if dead
		}else{
			//go to "grave yard"
			x = 1200;
			y = 800;		
		}
		
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
		
		//update
		updateEnemy();
		
	}
	
	public void simpleEnemyAI(Player player){
		
		//collision
		singleShot.collision(player);
		
		//if alive
		if(health > 0){
			
			//move in accordance with velocity
			y+=vy;
			x+=vx;
		
			//go to screen
			if(x > ranX){
				x -= 5;			
			}

			//if at battle position
			if(x <= ranX){
				
				//move time
				time++;
				
				//go up and down
				if(y < 0){
					y = 1;
					vy = 5;
				}else if(y > 580){
					y = 579;
					vy = -5;
				}else if(vy == 0){
					vy = 5;
				}
				
				//every 2 seconds shoot
				if(time > 120) {
					useSingleShot();
					time = 0;
				}
			
			}
		
		//if dead
		}else{
			
			//move to "grave yard"
			x = -100;
			y = -100;
			
		}
		
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
		
		//update
		updateEnemy();
		
	}
	
	public void complexEnemyAI(int playerY, Player player){
		
		//collision
		for(int i = 0; i < complexShot.length; i++){
			complexShot[i].collision(player);
		}
		
		//if alive
		if(health > 0){
		
			//time movement
			time++;
			time2++;
			
			//time marker variable
			int z = 0;
			
			//store x position variable
			int store = 600;
		
			//go to battle position
			if(time < ranX){
				x -= 5;
			}
		
			//store the original x position
			if(time == ranX){
				store = x;
			}
		
			//store multiples of 2 seconds 2 seconds later
			if((time+120)%120 == 0){
				z = time+120;
			}
		
			//move towards player every two seconds
			if(time > ranX && time < z && y != playerY){	
				if(y < playerY){
					vy = 3;
				}else if(y > playerY){
					vy = -3;
				}	
			}
		
			//move around the original x position
			if(time > ranX){
			
				if(x <= store){
					vx = 3;
				}else if(x > store+300){
					vx = -3;
				}
			
			}
			
			//shoot three shots occasionally
			if(time2 > ranX){
				
				if(time2 == ranX + 60){
					useComplexShot();
				}else if(time2 == ranX + 70){
					useComplexShot();
				}else if(time2 == ranX + 80){
					useComplexShot();
					time2 = ranX;
					
					//say that all fire balls have been shot 
					for(int i = 0; i < complexShot.length; i++){
						complexShot[i].live = false;
					}
					
				}
				
			}
		
			//x and y move in accordance with velocity
			y += vy;
			x += vx;
		
		//if dead
		}else{
			
			//move to "grave yard"
			x = 1200;
			y = 800;
			
		}
		
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
		
		//update
		updateEnemy();
		
	}
	
	//getters
	public int getX() {
		return x;	
	}
	
	public int getY(){
		return y;		
	}
	
	public int getHealth(){
		return health;		
	}
	
	public int getXDisplacement(){
		return xDisplacement;		
	}
	
	public int getYDisplacement(){
		return yDisplacement;	
	}
	
	public int getW(){
		return width;	
	}
	
	public int getH(){
		return height;		
	}
	
	public boolean getAlive(){
		return alive;
	}
	
	//setters
	public void setVx(int vx) {
		vy = 0;
		this.vx = vx;	
	}

	public void setVy(int vy) {	
		vx=0;
		this.vy = vy;	
	}

	public void setX(int x) {
		this.x = x;
		updateEnemy();		
	}

	public void setY(int y) {
		this.y = y;
		updateEnemy();
	}
	
	public void setHealth(int h){
		health = h;		
	}

	public void setRanX(int newX){
		ranX = newX;		
	}

	public void setAlive(boolean b){
		alive = b;
	}
	
}
