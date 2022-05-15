import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Vector;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.image.*;
import java.awt.geom.AffineTransform;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

public class Driver extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener {
	
	public static int life = 20;

	//screen attributes
	int screen_width = 1200;
	int screen_height = 800;
	
	//general variables
	int numCrashEnemies = 21;
	int numSimpleEnemies = 17;
	int numComplexEnemies = 9;
	boolean[] crashEnemySpawn = new boolean[numCrashEnemies];
	boolean[] simpleEnemySpawn = new boolean[numSimpleEnemies];
	boolean[] complexEnemySpawn = new boolean[numComplexEnemies];
	boolean[] healthOn = new boolean[life];
	boolean[] bossHealthOn = new boolean[240];
	boolean[] bossHealthOn2 = new boolean[240];
	boolean[] bossHealthOn3 = new boolean[240];
	int[] simpleEnemyDispersion = new int[numSimpleEnemies];
	int[] complexEnemyDispersion = new int[numComplexEnemies];
	boolean pickleSpawn = false;
	boolean dragonSpawn = false;
	boolean richardSpawn = false;
	boolean progress2 = false;
	boolean progress3 = false;
	boolean autofire = false;

	String src = new File("").getAbsolutePath() + "/src/"; // path to image
	Clip hop;
	
	//objects
	Player player = new Player("Player.gif", "playerwithcrossbowdamaged.gif", 0, 300, "arrow.gif");
	Background background1 = new Background("Field.gif", 0, 0);
	Background background2 = new Background("Field.gif", 1200, 0);
	Background background3 = new Background("Field.png", 0, 0);
	Background background4 = new Background("Field.png", 1200, 0);
	Background progressBar = new Background("ProgressBar.gif", 60, 628);
	Background deathScreen = new Background("youdied.gif", 0, 0);
	Background winScreen = new Background("youwinscreen.gif", 0, 0);
	Background winScreenStatic = new Background("youwin.png", 0, 0);
	Background death = new Background("death.png", 0, 0);
	Background[] healthHeart = new Background[life];
	Background[] barFluid = new Background[240];
	Background[] barFluid2 = new Background[240];
	Background[] barFluid3 = new Background[240];
	Enemy[] crashEnemy = new Enemy[numCrashEnemies];
	Enemy[] simpleEnemy = new Enemy[numSimpleEnemies];
	Enemy[] complexEnemy = new Enemy[numComplexEnemies];
	Boss pickle = new Boss("imperialpickle.gif", "imperialpickleDamaged.gif", 1400, 200, 356, 260, 188, 40, 120);
	Boss dragon = new Boss("dragon.gif", "dragondamaged.gif", 1400, 200, 100, 260, 272, 100, 120);	
	Boss richard = new Boss("richard.gif", "richarddamaged.gif", 1400, 200, 124, 176, 48, 64, 120);
	DamageBody crash = new DamageBody("tomatodamaged.gif", 1200, 800);
	DamageBody simple = new DamageBody("avacadodamaged.gif", 1200, 800);
	DamageBody complex = new DamageBody("carrotdamaged.gif", 1200, 800);
	DamageBody boss = new DamageBody("imperialpickledamage.gif", 1200, 800);
	DamageBody playerDamaged = new DamageBody("playerwithcrossbowdamaged.gif", 1200, 800);
	
	Sequencer sequencer;

	String lost = "";

	public void paint(Graphics g) {

		super.paintComponent(g);
		
		//paint backgrounds first
		background3.paint(g);
		background4.paint(g);
		background1.paint(g);
		background2.paint(g);
		
		//paint player if alive
		if(life > 0) {
			player.paint(g);	
		}
		
		//=======================complexEnemy SPAWNS==================================
		
		//wave 1: 17 seconds in
		if(player.whatTime() > 1020){
			for(int i = 0; i < 1; i++){
				complexEnemy[i].paint(g);
				complexEnemySpawn[i] = true;
			}
		}	
		
		//wave 2: 25 seconds in		
		if(player.whatTime() > 1500){
			for(int i = 1; i < 3; i++){
				complexEnemy[i].paint(g);
				complexEnemySpawn[i] = true;
			}
		}
		
		//wave 3: 35 seconds in		
		if(player.whatTime() > 2100){
			for(int i = 3; i < 4; i++){
				complexEnemy[i].paint(g);
				complexEnemySpawn[i] = true;
			}
		}
		
		//wave 4: 36 seconds in	
		if(player.whatTime() > 2160){
			for(int i = 4; i < 5; i++){
				complexEnemy[i].paint(g);
				complexEnemySpawn[i] = true;
			}
		}
		
		//wave 5: 37 seconds in		
		if(player.whatTime() > 2220){
			for(int i = 5; i < 6; i++){
				complexEnemy[i].paint(g);
				complexEnemySpawn[i] = true;
			}
		}
		
		//wave 6: 48 seconds in		
		if(player.whatTime() > 2880){
			for(int i = 6; i < 8; i++){
				complexEnemy[i].paint(g);
				complexEnemySpawn[i] = true;
			}
		}
		
		//wave 7: 55 seconds in		
		if(player.whatTime() > 3300){
			for(int i = 8; i < 9; i++){
				complexEnemy[i].paint(g);
				complexEnemySpawn[i] = true;
			}
		}

		//=======================complexEnemy SPAWNS END==================================
		
		//=======================simpleEnemy SPAWNS==================================
		
		//wave 1: 10 seconds in	
		if(player.whatTime() > 600){
			for(int i = 0; i < 1; i++){
				simpleEnemy[i].paint(g);
				simpleEnemySpawn[i] = true;
			}
		}		
		
		//wave 2: 16 seconds in		
		if(player.whatTime() > 960){
			for(int i = 1; i < 3; i++){
				simpleEnemy[i].paint(g);
				simpleEnemySpawn[i] = true;
			}
		}
		
		//wave 3: 10 seconds in		
		if(player.whatTime() > 1200){
			for(int i = 3; i < 4; i++){
				simpleEnemy[i].paint(g);
				simpleEnemySpawn[i] = true;
			}
		}
		
		//wave 4: 10 seconds in		
		if(player.whatTime() > 1500){
			for(int i = 4; i < 5; i++){
				simpleEnemy[i].paint(g);
				simpleEnemySpawn[i] = true;
			}
		}
		
		//wave 5: 10 seconds in		
		if(player.whatTime() > 1560){
			for(int i = 5; i < 6; i++){
				simpleEnemy[i].paint(g);
				simpleEnemySpawn[i] = true;
			}
		}
		
		//wave 6: 10 seconds in	
		if(player.whatTime() > 1620){
			for(int i = 6; i < 7; i++){
				simpleEnemy[i].paint(g);
				simpleEnemySpawn[i] = true;
			}
		}
		
		//wave 7: 10 seconds in		
		if(player.whatTime() > 2100){
			for(int i = 7; i < 10; i++){
				simpleEnemy[i].paint(g);
				simpleEnemySpawn[i] = true;
			}
		}
		
		//wave 8: 10 seconds in	
		if(player.whatTime() > 2700){
			for(int i = 10; i < 13; i++){
				simpleEnemy[i].paint(g);
				simpleEnemySpawn[i] = true;
			}
		}
		
		//wave 9: 10 seconds in	
		if(player.whatTime() > 3000){
			for(int i = 13; i < 17; i++){
				simpleEnemy[i].paint(g);
				simpleEnemySpawn[i] = true;
			}
		}
		//=======================simpleEnemy SPAWNS END==================================
		
		//=======================crashEnemy SPAWNS==================================
		
		//wave 1: 5 seconds in		
			if(player.whatTime() > 300){
				for(int i = 0; i < 1; i++){
					crashEnemy[i].paint(g);
					crashEnemySpawn[i] = true;
				}
			}
			
		//wave 2: 8 seconds in			
			if(player.whatTime() > 480){
				for(int i = 1; i < 3; i++){
					crashEnemy[i].paint(g);
					crashEnemySpawn[i] = true;
				}
			}
			
		//wave 3: 15 seconds in			
			if(player.whatTime() > 900){
				for(int i = 3; i < 6; i++){
					crashEnemy[i].paint(g);
					crashEnemySpawn[i] = true;
				}
			}
			
		//wave 4: 22 seconds in		
			if(player.whatTime() > 1320){
				for(int i = 6; i < 7; i++){
					crashEnemy[i].paint(g);
					crashEnemySpawn[i] = true;
				}
			}
		
		//wave 5: 28 seconds in			
			if(player.whatTime() > 1680){
				for(int i = 7; i < 9; i++){
					crashEnemy[i].paint(g);
					crashEnemySpawn[i] = true;
				}
			}
			
		//wave 6: 30 seconds in			
			if(player.whatTime() > 1800){
				for(int i = 9; i < 10; i++){
					crashEnemy[i].paint(g);
					crashEnemySpawn[i] = true;
				}
			}
			
		//wave 7: 31 seconds in		
			if(player.whatTime() > 1860){
				for(int i = 10; i < 11; i++){
					crashEnemy[i].paint(g);
					crashEnemySpawn[i] = true;
				}
			}
			
		//wave 8: 32 seconds in			
			if(player.whatTime() > 1920){
				for(int i = 11; i < 13; i++){
					crashEnemy[i].paint(g);
					crashEnemySpawn[i] = true;
				}
			}
			
		//wave 9: 33 seconds in			
			if(player.whatTime() > 1980){
				for(int i = 13; i < 14; i++){
					crashEnemy[i].paint(g);
					crashEnemySpawn[i] = true;
				}
			}
			
		//wave 10: 40 seconds in			
			if(player.whatTime() > 2400){
				for(int i = 14; i < 16; i++){
					crashEnemy[i].paint(g);
					crashEnemySpawn[i] = true;
				}
			}
			
		//wave 11: 41 seconds in		
			if(player.whatTime() > 2460){
				for(int i = 16; i < 17; i++){
					crashEnemy[i].paint(g);
					crashEnemySpawn[i] = true;
				}
			}
			
		//wave 12: 50 seconds in		
			if(player.whatTime() > 3000){
				for(int i = 17; i < 18; i++){
					crashEnemy[i].paint(g);
					crashEnemySpawn[i] = true;
				}
			}
		
		//wave 13: 51 seconds in		
			if(player.whatTime() > 3060){
				for(int i = 18; i < 19; i++){
					crashEnemy[i].paint(g);
					crashEnemySpawn[i] = true;
				}
			}
			
		//wave 13: 51 seconds in		
			if(player.whatTime() > 3300){
				for(int i = 19; i < 21; i++){
					crashEnemy[i].paint(g);
					crashEnemySpawn[i] = true;
				}
			}
			
		//=======================crashEnemy SPAWNS END==================================

		//======================BOSS SPAWNS=====================================
			
		//pickle
		if(player.whatTime() > 3600 && pickle.getHealth() > 0){
			pickle.paint(g);
			pickleSpawn = true;
		}
		
		//dragon
		if(player.whatTime() > 3600 && pickle.getHealth() < 0 && dragon.getHealth() > 0){
			dragon.paint(g);
			dragonSpawn = true;
		}
		
		//richard
		if(player.whatTime() > 3600 && pickle.getHealth() < 0 && dragon.getHealth() < 0 && richard.getHealth() > 0){
			richard.paint(g);
			richardSpawn = true;
		}
		
		//======================BOSS SPAWNS END=====================================
		
		//======================PLAYER UI===========================================
		
		//progress bar
		progressBar.paint(g);
		
		//bar fluid
		for(int i = 0; i < barFluid.length; i++){		
			
			//first bar fluid
			if(player.whatTime() > (i+1)*15){
				barFluid[i].setY(720);
				barFluid[i].setX((i*4) + 120);
				if(bossHealthOn[i]){
					barFluid[i].paint(g);
				}
			}	
			
			//second bar fluid
			if(progress2){
				if(player.whatTime() > (i+1)*15){
					barFluid2[i].setY(720);
					barFluid2[i].setX((i*4) + 120);
					if(bossHealthOn2[i]){
						barFluid2[i].paint(g);
					}
				}
			}
			
			//second bar fluid
			if(progress3){
				if(player.whatTime() > (i+1)*15){
					barFluid3[i].setY(720);
					barFluid3[i].setX((i*4) + 120);
					if(bossHealthOn3[i]){
						barFluid3[i].paint(g);
					}
				}
			}
			
		}
		
		//player health
		
		//row 1
		for(int i = 0; i < healthHeart.length/2; i++) {
			if(healthOn[i]) {
				healthHeart[i].paint(g);
			}
		}
		
		//row 1
		for(int i = healthHeart.length/2; i < healthHeart.length; i++) {
			if(healthOn[i]) {
				healthHeart[i].paint(g);
			}
		}
		
		//=========================Death AHAHAH or win======================================
		
		if(life <= 0){
			death.paint(g);
			deathScreen.paint(g);
		}
		
		if(richard.getHealth() <= 0){
			winScreenStatic.paint(g);
			winScreen.paint(g);
		}
	
	}

	public void update() {
		
		//dragon damage body
		if(dragonSpawn){
			boss = new DamageBody("dragonDamaged", 1200, 800);
		}
		
		//autofire function                                                           
		if(autofire) {
			if(player.whatTime()%5 == 0) {
				player.shoot();
			}
		}
		
		//set hearts to life
		for(int i = 0; i < healthOn.length; i++) {
			if(life >= i + 1) {
				healthOn[i] = true;
			}else {
				healthOn[i] = false;
			}
		}
		
		//set bar to boss health
		for(int i = 0; i < bossHealthOn.length; i += 2){
			if(pickle.getHealth() >= i/2){
				bossHealthOn[i] = true;
				bossHealthOn[i + 1] = true;
			}else{
				bossHealthOn[i] = false;
				bossHealthOn[i + 1] = false;
			}
		}
		
		for(int i = 0; i < bossHealthOn.length; i += 2){
			if(dragon.getHealth() >= i/2){
				bossHealthOn2[i] = true;
				bossHealthOn2[i + 1] = true;
			}else{
				bossHealthOn2[i] = false;
				bossHealthOn2[i + 1] = false;
			}
		}
		
		for(int i = 0; i < bossHealthOn.length; i += 2){
			if(richard.getHealth() >= i/2){
				bossHealthOn3[i] = true;
				bossHealthOn3[i + 1] = true;
			}else{
				bossHealthOn3[i] = false;
				bossHealthOn3[i + 1] = false;
			}
		}
		
		//background is moving until 1 minute and 10 seconds in
		background3.backgroundMover();
		background4.backgroundMover();
		background1.backgroundMover();
		background2.backgroundMover();
		
		//background changes to dungeon
		if(background1.getX() == 1200 && pickle.getHealth() <= 0){
			background1.changeEnvrionment("Dungeon.gif", background1.getEnvironment());
			background1.setEnvironment(false);
		}
		if(background2.getX() == 1200 && pickle.getHealth() <= 0){
			background2.changeEnvrionment("Dungeon.gif", background2.getEnvironment());
			background2.setEnvironment(false);
		}
		if(background3.getX() == 1200 && pickle.getHealth() <= 0){
			background3.changeEnvrionment("Dungeon.png", background3.getEnvironment());
			background3.setEnvironment(false);
		}
		if(background4.getX() == 1200 && pickle.getHealth() <= 0){
			background4.changeEnvrionment("Dungeon.png", background4.getEnvironment());
			background4.setEnvironment(false);
		}
		
		//background changes to space
		if(background1.getX() == 1200 && dragon.getHealth() <= 0){
			background1.changeEnvrionment("space1.png", background1.getNextEnvironment());
			background1.setNextEnvironment(false);
		}
		if(background2.getX() == 1200 && dragon.getHealth() <= 0){
			background2.changeEnvrionment("space2.png", background1.getNextEnvironment());
			background1.setNextEnvironment(false);
		}
		if(background3.getX() == 1200 && dragon.getHealth() <= 0){
			background3.changeEnvrionment("space1.png", background1.getNextEnvironment());
			background1.setNextEnvironment(false);
		}
		if(background4.getX() == 1200 && dragon.getHealth() <= 0){
			background4.changeEnvrionment("space2.png", background1.getNextEnvironment());
			background1.setNextEnvironment(false);
		}
		
		//construct dungeon
		if(pickle.getHealth() == 0){
			
			//damage body setter
			crash = new DamageBody("ghouldamaged.gif", 1200, 800);
			simple = new DamageBody("slimedamaged.gif", 1200, 800);
			complex = new DamageBody("beholderdamaged.gif", 1200, 800);
			playerDamaged = new DamageBody("level2playersworddamaged.gif", 1200, 800);
			
			//new progress bar
			progressBar = new Background("ProgressBar2.gif", 60, 628);
			progress2 = true;

			//boss health setter
			for(int i = 0; i < bossHealthOn.length; i++){
				bossHealthOn2[i] = true;
			}
			
			//evolve player
			player = new Player("Player2.gif", "level2playersworddamaged.gif", player.getX(), player.getY(), "playerlsash.gif");
			player.reload();
			
			//reset time
			player.setTime(0);
			
			//ghoul construction
			for(int i = 0; i < crashEnemy.length; i++){
				crashEnemySpawn[i] = false;
				crashEnemy[i] = new Enemy("ghoul.gif", "ghouldamaged.gif", 1200, (int)(Math.random()*600+100), 60, 60, 20, 24, 3, 5, 5, 5, 5);
			}
			
			//slime construction
			for(int i = 0; i < simpleEnemy.length; i++){
				simpleEnemySpawn[i] = false;
				simpleEnemy[i] = new Enemy("slime.gif", "slimedamaged.gif", 1300, (int)(Math.random()*600+100), 40, 60, 20, 36, 4, 16, 12, 4, 0);
				simpleEnemyDispersion[i] = (int)(Math.random()*600+500);
				simpleEnemy[i].loadSingleShot("slimeshot.gif");
			}
			
			//beholder construction
			for(int i = 0; i < complexEnemy.length; i++){
				complexEnemySpawn[i] = false;
				complexEnemy[i] = new Enemy("beholder.gif", "beholderdamaged.gif", 1200, (int)(Math.random()*600+100), 40, 160, 40, 30, 12, 20, 20, 4, 4);
				complexEnemyDispersion[i] = (int)(Math.random()*120+60);
				complexEnemy[i].loadComplexShot("fireball.gif");
			}
			
			//insure that this happens once
			pickle.setHealth(-1);
		}
		
		//construct space
		if(dragon.getHealth() == 0){
					
			//damage body setter
			crash = new DamageBody("davedamaged.gif", 1200, 800);
			simple = new DamageBody("rudydamaged.gif", 1200, 800);
			complex = new DamageBody("adamdamaged.gif", 1200, 800);
			boss = new DamageBody("richardDamaged", 1200, 800);
			playerDamaged = new DamageBody("player3damaged.gif", 1200, 800);
			
			//new progress bar
			progressBar = new Background("ProgressBar3.gif", 60, 628);
			progress3 = true;
			
			//boss health setter
			for(int i = 0; i < bossHealthOn.length; i++){
				bossHealthOn3[i] = true;
			}
			
			//reset time
			player.setTime(0);
					
			//evolve player
			player = new Player("Player3.gif", "player3damaged.gif", player.getX(), player.getY(), "playerfire.gif");
			player.reload();
			
			//dave construction
			for(int i = 0; i < crashEnemy.length; i++){
				crashEnemySpawn[i] = false;
				crashEnemy[i] = new Enemy("dave.gif", "davedamaged.gif", 1200, (int)(Math.random()*600+100), 60, 60, 20, 24, 4, 5, 5, 5, 5);
			}
					
			//rudy construction
			for(int i = 0; i < simpleEnemy.length; i++){
				simpleEnemySpawn[i] = false;
				simpleEnemy[i] = new Enemy("rudy.gif", "rudydamaged.gif", 1300, (int)(Math.random()*600+100), 40, 60, 20, 36, 5, 60, 24, 4, 8);
				simpleEnemyDispersion[i] = (int)(Math.random()*600+500);
				simpleEnemy[i].loadSingleShot("yin.gif");
			}
					
			//adam construction
			for(int i = 0; i < complexEnemy.length; i++){
				complexEnemySpawn[i] = false;
				complexEnemy[i] = new Enemy("adam.gif", "adamdamaged.gif", 1200, (int)(Math.random()*600+100), 40, 160, 40, 30, 12, 24, 24, 8, 8);
				complexEnemyDispersion[i] = (int)(Math.random()*120+60);
				complexEnemy[i].loadComplexShot("orb.gif");
			}
					
			//insure that this happens once
			dragon.setHealth(-1);
		}
		
		//PLAYER
		if(life > 0) {
		
			//player movement method and borders
			player.playerMovement();
			player.borders();
		
			//player projectile deactivation
			player.killArrows();
		
			//time in accordance with the player
			player.timeMovement();
		
		}
		
		//boss spawning
		if(pickleSpawn){
			pickle.pickleAI(player.whatTime(), background1, background2, background3, background4, player);
			player.bossDamage(pickle);
		}
		
		if(dragonSpawn){
			dragon.dragonAI(player.whatTime(), background1, background2, background3, background4, player);
			player.bossDamage(dragon);
		}
		
		if(richardSpawn){
			richard.richardAI(player.whatTime(), player.getY(), player);
			player.bossDamage(richard);
		}
		
		//crashEnemys spawn in accordance with when their spawn == true
		for(int i = 0; i < crashEnemySpawn.length; i++){
			if(crashEnemySpawn[i] == true){
				crashEnemy[i].crashEnemyAI(player.getY(), player);
			}
			player.damage(crashEnemy[i]);
		}
		
		//simpleEnemys spawn in accordance with when their spawn == true
		for(int i = 0; i < simpleEnemy.length; i++){
			simpleEnemy[i].setRanX(simpleEnemyDispersion[i]);
			if(simpleEnemySpawn[i] == true){
				simpleEnemy[i].simpleEnemyAI(player);
			}
			player.damage(simpleEnemy[i]);
		}
		
		//complexEnemys spawn in accordance with when their spawn == true
		for(int i = 0; i < complexEnemy.length; i++){
			complexEnemy[i].setRanX(complexEnemyDispersion[i]);
			if(complexEnemySpawn[i] == true){
				complexEnemy[i].complexEnemyAI(player.getY(), player);
			}
			player.damage(complexEnemy[i]);
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		update();
		repaint();
	}

	public static void main(String[] arg) {
		Driver d = new Driver();
	}

	public Driver() {

		JFrame f = new JFrame();
		f.setTitle("RUNgeon");
		f.setSize(screen_width, screen_height);
		f.setResizable(false);
		f.addKeyListener(this);
		f.addMouseListener(this);
		f.addMouseMotionListener(this);
		
		//instantiate variables
		
		//crashEnemys spawn randomly across the y axis out of screen view
		for(int i = 0; i < crashEnemy.length; i++){
			crashEnemy[i] = new Enemy("tomato.gif", "tomatodamaged.gif", 1200, (int)(Math.random()*600+100), 60, 60, 20, 24, 2, 5, 5, 5, 5);
		}
		
		//simpleEnemys spawn randomly across the y axis out of the screen view, x dispersion array is instantiated, and cannon is loaded
		for(int i = 0; i < simpleEnemy.length; i++){
			simpleEnemy[i] = new Enemy("Avocado.gif", "avacadodamaged.gif", 1300, (int)(Math.random()*600+100), 40, 60, 20, 36, 3, 20, 20, 4, 4);
			simpleEnemyDispersion[i] = (int)(Math.random()*600+500);
			simpleEnemy[i].loadSingleShot("cannonball.gif");
		}
		
		//complexEnemys spawn randomly across the y axis out of the screen view
		for(int i = 0; i < complexEnemy.length; i++){
			complexEnemy[i] = new Enemy("carrot.gif", "carrotdamaged.gif", 1200, (int)(Math.random()*600+100), 40, 80, 40, 30, 10, 16, 12, 4, 0);
			complexEnemyDispersion[i] = (int)(Math.random()*120+60);
			complexEnemy[i].loadComplexShot("bullet.gif");
		}
		
		//barFluid instantiator
		for(int i = 0; i < barFluid.length; i++){
			barFluid[i] = new Background("barFluid.png", 1200, 800);
			barFluid2[i] = new Background("barFluid.png", 1200, 800);
			barFluid3[i] = new Background("barFluid.png", 1200, 800);
		}
		
		//player hearts instantiator
		for(int i = 0; i < healthHeart.length/2; i++){
			healthHeart[i] = new Background("FullHeart.png", (i+1)*44 - 36, 8);
		}
		
		for(int i = healthHeart.length/2; i < healthHeart.length; i++) {
			healthHeart[i] = new Background("FullHeart.png", (i+1)*44 - 476, 48);
		}
	
		//boss health instantiator
		for(int i = 0; i < bossHealthOn.length; i++){
			bossHealthOn[i] = true;
			bossHealthOn2[i] = false;
			bossHealthOn3[i] = false;
		}
		
		// Obtains the default Sequencer connected to a default device.
		try {
			sequencer = MidiSystem.getSequencer();
			// Opens the device, indicating that it should now acquire any
			// system resources it requires and become operational.
			sequencer.open();

			// create a stream from a file
			
			InputStream is = new BufferedInputStream(new FileInputStream(
					new File("Thelazysong.mid").getAbsoluteFile()));

			// Sets the current sequence on which the sequencer operates.
			// The stream must point to MIDI file data.
			sequencer.setSequence(is);

			// Starts playback of the MIDI data in the currently loaded
			// sequence.
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		f.add(this);
		t = new Timer(16, this);
		t.start();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		
	}

	Timer t;

	@Override
	public void keyPressed(KeyEvent e) {
		
		if(e.getKeyCode() == 65){			//if key A is pressed			
			player.setVx(-10);
		}else if(e.getKeyCode() == 68){		// if key D is pressed			
			player.setVx(10);			
		}else if(e.getKeyCode() == 87){		//if key W is pressed			
			player.setVy(-10);			
		}else if(e.getKeyCode() == 83){		//if key S is pressed			
			player.setVy(10);			
		}
		
		//shoot on space
		if(e.getKeyCode() == 32){			
			player.shoot();
			richard.setFireBack(true);
		}
		
		//reload on r
		if(e.getKeyCode() == 82){		
			player.reload();			
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {

		if(e.getKeyCode() == 65){			//if key A is released		
			player.setVx(0);
		}else if(e.getKeyCode() == 68){		// if key D is released			
			player.setVx(0);			
		}else if(e.getKeyCode() == 87){		//if key W is released			
			player.setVy(0);			
		}else if(e.getKeyCode() == 83){		//if key S is released			
			player.setVy(0);			
		}
		
		//autofire
		if(e.getKeyCode() == 17 && !autofire) {
			autofire = true;
		}else if(e.getKeyCode() == 17 && autofire) {
			autofire = false;
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
	
	public void reset() {}
	
	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent m) {

	}

	@Override
	public void mouseMoved(MouseEvent m) {}

}
