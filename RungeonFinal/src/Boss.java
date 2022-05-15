import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.net.URL;

/* class to represent a Ship object in a game */
public class Boss {

	//attributes
	private int x, y, vx, vy, width, height, baseHealth, xDisplacement, yDisplacement, health, bossTime, spareInt; 
	private boolean alive, primed, wavePrimed, spareBool, spareBool2, fireBack, inviFrames; 	
	private int ranY;
	private double random;
	private Image img; 
	private String normal, damaged;
	private AffineTransform tx = AffineTransform.getTranslateInstance(x, y);
	Bullet attack;
	Bullet[] wave = new Bullet[10];
	boolean[] liveWave = new boolean[10];
	
	//constructors
	public Boss(String fileName) {
		
		//attributes
		x = 200;
		y = 200;
		vx = 0;
		vy = 0;
		width = 0;
		height = 0;
		xDisplacement = 0;
		yDisplacement = 0;
		health = 10;
		bossTime = 0;
		baseHealth = health;
		primed = false;
		wavePrimed = false;
		ranY = 400;
		spareBool = true;
		spareBool2 = false;
		fireBack = false;
		inviFrames = false;
		spareInt = 0;
		alive = true;
		
		//pre-load attacks
		attack = new Bullet("", 0, 0, 0, 0);
		for(int i = 0; i < wave.length; i++){
			wave[i] = new Bullet("", 0, 0, 0, 0);
		}
		
		//do not touch
		img = getImage(fileName);
		updateBoss();
		
	}

	public Boss(String fileName, String d, int posX, int posY, int newW, int newH, int xDis, int yDis, int h){
		
		//call default constructor
		this(fileName); 
		
		//set parameters
		x = posX;
		y = posY;
		width = newW;
		height = newH;
		xDisplacement = xDis;
		yDisplacement = yDis;
		health = h;
		baseHealth = h;
		normal = fileName;
		damaged = d;
		
		//any time you alter x or y you must call updateBoss()
		updateBoss();
		
	}
	
	//methods
	private void updateBoss() {
		tx.setToTranslation(x, y);
	}

	public void paint(Graphics g) {
		
		//paint attack if primed
		if(primed){
			attack.paint(g);
		}
		
		//paint wave if live
		if(wavePrimed) {
			for(int i = 0; i < wave.length; i++) {
				wave[i].paint(g);
			}
		}
		
		//paint boss
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(img, tx, null);
		
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
	
	public void loadPeirceSlash(){	
		attack = new Bullet("peirceslash.gif", 272, 68, 24, 104);	
		attack.setDamage(2);
		primed = true;
	}
	
	public void loadDragonFire(){	
		attack = new Bullet("Dragonfire.gif", 272, 68, 24, 20);	
		attack.setDamage(3);
		primed = true;
	}
	
	public void loadFlameThrower(){	
		attack = new Bullet("Flamethrower.gif", 1200, 136, 0, 244);	
		attack.setDamage(1);
		primed = true;
	}
	
	public void loadGroundSmash(){	
		attack = new Bullet("groundwave.gif", 160, 160, 64, 64);
		attack.setDamage(3);
		primed = true;
	}
	
	public void loadWave() {
		for(int i = 0; i < wave.length; i++) {
			wave[i] = new Bullet("peirceslash.gif", 272, 68, 24, 104);
			wave[i].setDamage(2);
			liveWave[i] = true;
			wavePrimed = true;
		}
	}
	
	public void loadLaser() {
		for(int i = 0; i < wave.length; i++) {
			wave[i] = new Bullet("heroine.gif", 80, 28, 12, 8);
			wave[i].setDamage(3);
			liveWave[i] = true;
			wavePrimed = true;
		}
	}
	
	public void useLaser() {
		for(int i = 0; i < wave.length; i++) {
			if(liveWave[i]) {
				wave[i].setX(x);
				wave[i].setY(y + 100);
				wave[i].setVx(-20);
				liveWave[i] = false;
				break;
			}
		}
	}
	
	public void useWave() {
		for(int i = 0; i < wave.length; i++) {
			if(liveWave[i]) {
				wave[i].setX(1200);
				wave[i].setY(ranY);
				wave[i].setVx(-10);
				liveWave[i] = false;
				break;
			}
		}
	}
	
	public void usePeirceSlash(){
		attack.setX(x - 35);
		attack.setY(y + 60);
		attack.setVx(-10);
	}

	public void useDragonFire(){
		attack.setX(x - 35);
		attack.setY(y + 120);
		attack.setVx(-10);
	}
	
	public void useFlameThrower(){
		attack.setX(x - 1000);
		attack.setY(y - 20);
		attack.setVx(0);
	}
	
	public void useGroundWave(){
		
		attack.setX(x - 184);
		attack.setY(y);
		attack.setVx(-10);
		
	}
	
	public void pickleAI(int time, Background a, Background b, Background c, Background d, Player p) {
		
		//move in accordance with velocity
		y += vy;
		x += vx;
		
		//if alive
		if(health > 0){
			
			//at one minute in (when boss spawns)
			if(time > 3600) {
				
				//move towards the player until reaches visibility
				if(x > 800){
					x -= 3;
					loadWave();
				}
				
				//start moving up and down
				if(time == 4000){
					vy = 2;
				}
				
				//once in visibility
				if(x <= 800){
					
					//collision
					attack.collision(p);
					
					//boss time in which attacks revolve around
					bossTime++;
	
					//if health is above half
					if(health >= baseHealth/2){
						
						//move up and down the screen
						if(y == 0){
							vy = 2;
						}else if(y == 600){
							vy = -2;
						}
						
						//either attack with fire ball or ground smash every 2 seconds
						if(bossTime == 120 && Math.random()*10 >= 5){
							loadPeirceSlash();
							usePeirceSlash();
							bossTime = 0;					
						}else if(bossTime == 120){						
							loadGroundSmash();
							useGroundWave();
							bossTime = 0;						
						}
					
					//if health is below half - phase 2
					}else{
						
						//randomize y value for spawning the wave
						ranYValue();
						
						//move towards the middle of the screen for phase 2
						if(y < 200){
							vy = 1;
							bossTime = -1;
							loadWave();
						}else if(y > 200){
							vy = -1;
							bossTime = -1;
							loadWave();
						}else{
							vy = 0;
						}
						
						//phase 2 attacks
						if(y == 200){							
							
							//collision
							for(int i = 0; i < wave.length; i++){
								wave[i].collision(p);
							}
							
							for(int i = 0; i < wave.length; i++) {
								if(wave[i].getX() < -400) {
									liveWave[i] = true;
								}
							}
							
							//shoot wave every half second
							if(bossTime%30 == 0) {
								useWave();
							}
							
						}
						
					}
					
				}
			
			}
						
		}else /*if(health == 0)*/{
			
			//if dead go to "grave yard" and make dungeon
			x = 1200;
			y = 800;
			vx = 0;
			vy = 0;
			a.setEnvironment(true);
			b.setEnvironment(true);
			c.setEnvironment(true);
			d.setEnvironment(true);
			
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
		
		//update all positions
		updateBoss();
				
	}
	
	public void dragonAI(int time, Background a, Background b, Background c, Background d, Player p) {
		
		//move in accordance with velocity
		y += vy;
		x += vx;
		
		//collision
		attack.collision(p);
		
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
		
		//if alive
		if(health > 0){
			
			//at one minute in (when boss spawns)
			if(time > 3600) {
				
				//move towards the player until reaches visibility
				if(x > 800){
					x -= 3;
				}
				
				//start moving up and down
				if(time == 4000){
					vy = 2;
				}
				
				//once in visibility
				if(x <= 800){
					
					//boss time in which attacks revolve around
					bossTime++;
	
					//if health is above half
					if(health >= baseHealth/2){
						
						//move up and down the screen
						if(y == 0){
							vy = 2;
						}else if(y == 300){
							vy = -2;
						}
						
						//attack with dragonfire every two seconds
						if(bossTime == 120){
							loadDragonFire();
							useDragonFire();
							bossTime = 0;					
						}
					
					//if health is below half
					}else{
						
						//variable that decides if phase 2 goes up or down
						random = Math.random()*10;
						
						//move towards the middle of the screen for phase 2
						if(y < 100 && spareBool){
							vy = 1;
							bossTime = -1;
						}else if(y > 100 && spareBool){
							vy = -1;
							bossTime = -1;
						}else if(y == 100 && spareBool){
							vy = 0;
							bossTime = 0;
							spareBool = false;
						}
						
						//phase 2 attacks
						if(y == 100){	
														
							if(bossTime == 0) {
								loadFlameThrower();
							}
							
							if(bossTime == 180 && 5 > random){
								vy = -8;
								y = 99;
								spareBool2 = true;
								System.out.println("up");
							}else if(bossTime == 180 && 5 <= random) {
								vy = 8;
								y = 101;
								System.out.println("down");
								spareBool2 = true;
							}
											
						}
						
						//once hits the edge slowly retreat back
						if(y <= 0) {
							spareBool = true;
							spareBool2 = false;
							loadFlameThrower();
						}else if(y >= 380) {
							spareBool = true;
							spareBool2 = false;
							loadFlameThrower();
						}
						
						//if attacking, use flamethrower
						if(spareBool2) {
							useFlameThrower();
						}
						
					}
					
				}
			
			}
						
		}else /*if(health == 0)*/{
			
			//if dead go to "grave yard" and make dungeon
			x = 1200;
			y = 800;
			vx = 0;
			vy = 0;
			a.setNextEnvironment(true);
			b.setNextEnvironment(true);
			c.setNextEnvironment(true);
			d.setNextEnvironment(true);
			
		}
		
		//update all positions
		updateBoss();
				
	}
	
	public void richardAI(int time, int playerY, Player p) {
		
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
		
		//collision
		Rectangle player = new Rectangle(p.getX() + 40, p.getY() + 16, p.getW(), p.getH());
		Rectangle enemy = new Rectangle(x + xDisplacement, y + yDisplacement, width, height);
		bossTime++;
		if(player.intersects(enemy) && !inviFrames){
			bossTime = 0;
			Driver.life -= 2;
			inviFrames = true;
			p.setAlive(false);
		}
		if(bossTime >= 90){
			inviFrames = false;
		}
		for(int i = 0; i < wave.length; i++){
			wave[i].collision(p);
		}
		
		x += vx;
		y += vy;
		
		if(health > 0){
			
			if(time >= 3600){
				
				if(health > baseHealth/2){
				
					if(x >= 800){
						x--;
					}
				
					if(x == 800){
						spareBool2 = true;
						vy = -5;
						loadLaser();
					}
				
					if(x >= 800){
						vx = 0;
						spareBool = true;
					}
				
					if(spareBool2){
					
						if(y <= 0){
							vy = 5;
						}else if(y >= 516){
							vy = -5;
						}
					
						if(Math.random()*100 > 99.5 && spareBool){
							vy = 0;
							vx = -20;
							spareBool = false;
						}
					
						if(x <= 0){
							vy = 0;
							vx = 5;
						}
					
					}
				
				}else{
					
					vx = 0;
					vy = 0;
					
					if(x < 800){
						x += 5;
					}
					
					if(x >= 800){
					
						//bullets become live again if outside screen (recall them)
						for(int i = 0; i < wave.length; i++) {
							if(wave[i].getX() < -100) {
								liveWave[i] = true;
							}
						}
				
						//shoot bullets when player shoots
						if(fireBack){
							useLaser();
							fireBack = false;
						}
					
						//follow player
						if(y < playerY - 100){
							y += 5;
						}else if(y > playerY - 100){
							y -= 5;
						}
						
					}
				
				}
				
			}
			
		}else{
			x = 1200;
			y = 1200;
			vy = 0;
			vx = 0;
		}
		
		updateBoss();
		
	}
	
	public void ranYValue() {
			ranY = (int)((Math.random()*522)+22);
	}
	
	//getters
	public int getX() {		
		return x;	
	}
	
	public int getY(){		
		return y;		
	}

	public int getW(){
		return width;
	}
	
	public int getH(){
		return height;
	}
	
	public int getXDisplacement(){
		return xDisplacement;
	}
	
	public int getYDisplacement(){
		return yDisplacement;
	}
	
	public int getHealth(){
		return health;
	}
	
	public int getRanY() {
		return ranY;
	}
	
	public boolean getFireBack(){
		return fireBack;
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
		updateBoss();
		
	}

	public void setY(int y) {
		
		this.y = y;
		updateBoss();
	}

	public void setHealth(int h){
		health = h;
	}
	
	public void setFireBack(boolean b){
		fireBack = b;
	}
	
	public void setAlive(boolean b){
		alive = b;
	}
	
}
