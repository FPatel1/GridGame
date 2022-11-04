package lesson.gridGame;

import java.io.*;
import java.util.*;

//is it still possible to spawn in the same spot as the treasure.
//what is the grid is too small to spawn everything.
//visual representation of grid
//monster voicelines
//multiple treasure
//tests

public class App implements Serializable {

	public static final Integer EMPTY_SQUARE = 0;
	public static final Integer PLAYER = 1;
	public static final Integer TREASURE = 2;
	public static final Integer ENEMY = 3;

	private Map map;

	private ArrayList<Entity> players = new ArrayList<>();
	private ArrayList<Entity> enemies = new ArrayList<>();
	private ArrayList<Entity> treasures = new ArrayList<>();

	private boolean gameFinished = false;

	public App(int numRows, int numCols, int numEnemies) {

		map = new Map(numRows, numCols);
		this.generateEntity(App.TREASURE);
		this.generateEntity(App.PLAYER);
		

		for (int i = 0; i < numEnemies; i++)
			this.generateEntity(App.ENEMY);
	}

	public void saveGame() {
		
		String filename = "saves/game.ser";
		
		try {
			FileOutputStream file = new FileOutputStream(filename);
			ObjectOutputStream out = new ObjectOutputStream(file);
			out.writeObject(this);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static App loadGame() {
		
		String filename = "saves/game.ser";
		try {
			FileInputStream file = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(file);
			
			App loadedApp = (App) in.readObject();
			
			return loadedApp;
//			this.map = loadedApp.map;
//			this.players = loadedApp.players;
//			this.enemies = loadedApp.enemies;
//			this.treasures = loadedApp.treasures;
//			this.gameFinished = loadedApp.gameFinished;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}

	public static int mainMenu() {

//		1 - Start Game
//		 * 2 - Load Game
//		 * 3 - Save Game
//		 * 4 - Quit
//		 *

		System.out.println("***************************");

		System.out.println("Please enter a digit");
		System.out.println("1 - Start Game");
		System.out.println("2 - Load Game");
		System.out.println("4 - Quit");

		System.out.println("***************************");

		Scanner scan = new Scanner(System.in);
		String res = scan.nextLine();

		switch (res) {

		case "1":
		case "2":
		case "4":
			// scan.close();
			return Integer.parseInt(res);

		default:
			System.out.println("Invald input!");
			App.mainMenu();
			break;
		}
		// scan.close();
		return -1;

	}

	public void runGame() {

		System.out.println("Game has started");

		while (!gameFinished) {

			map.printMap();

			int playerX = players.get(0).getxPos();
			int playerY = players.get(0).getyPos();

			System.out.println("Player is currently at X:" + playerX + " Y:" + playerY);

			this.checkDistance();

			System.out.println("Enter 'w' 'a' 's' or 'd' to move or q to save and quit!");

			Scanner scan = new Scanner(System.in);
			String read = scan.nextLine();

			if (read.toLowerCase().length() == 1) {
				if (read.toLowerCase().charAt(0) == 'w')
					this.moveEntity(players.get(0), -1, 0);
				else if (read.toLowerCase().charAt(0) == 'a')
					this.moveEntity(players.get(0), 0, -1);
				else if (read.toLowerCase().charAt(0) == 's')
					this.moveEntity(players.get(0), 1, 0);
				else if (read.toLowerCase().charAt(0) == 'd')
					this.moveEntity(players.get(0), 0, 1);
				else if (read.toLowerCase().charAt(0) == 'q') {
					this.saveGame();
					break;
					
				}
				else
					System.out.println("Type 'w' 'a' 's' or 'd'");

			} else
				System.out.println("Type 'w' 'a' 's' or 'd'");

		}

	}

	public void generateEntity(int symbol) {

		Random rand = new Random();

		int xSet = rand.nextInt(map.getRows());
		int ySet = rand.nextInt(map.getCols());
		System.out.println(xSet + " " + ySet);
		if (map.addEntity(symbol, xSet, ySet))
			this.generateEntity(symbol);

		Entity toAdd = new Entity(xSet, ySet, "Voice");

		if (symbol == App.PLAYER) {

			players.add(toAdd);

		}

		if (symbol == App.TREASURE) {
			treasures.add(toAdd);
		}
		if (symbol == App.ENEMY) {
			enemies.add(toAdd);
		}
	}

	public Integer checkCollision(Entity e, int xMove, int yMove) {

		int x = e.getxPos() + xMove;
		int y = e.getyPos() + yMove;

		for (Entity enemy : enemies) {
			if (x == enemy.getxPos() && y == enemy.getyPos())
				return App.ENEMY;
		}

		for (Entity enemy : treasures) {
			if (x == enemy.getxPos() && y == enemy.getyPos())
				return App.TREASURE;
		}

		return App.EMPTY_SQUARE;

	}

	public void checkDistance() {

		int xDiff = Math.abs(treasures.get(0).getxPos() - players.get(0).getxPos());

		int yDiff = Math.abs(treasures.get(0).getyPos() - players.get(0).getyPos());

		System.out.println("You are " + (xDiff + yDiff) + " moves away!");

	}

//	public void printEverything() {
//
//		System.out.println("Player x:" + entities.get(PLAYER).getxPos());
//		System.out.println("Player y:" + entities.get(PLAYER).getyPos());
//		System.out.println("numRows:" + map.getRows());
//		System.out.println("numCols:" + map.getCols());
//
//		System.out.println("TreasureX:" + treasureX);
//		System.out.println("TreasureY:" + treasureY);
//	}

	public boolean moveEntity(Entity e, int x, int y) {

		int entX = e.getxPos();
		int entY = e.getyPos();

		if (!(entX + x >= 0 && entX + x < map.getRows())) {
			System.out.println("WHOOOOPS, you tried to move off the grid!");
			return false;

		}

		if (!(entY + y >= 0 && entY + y < map.getCols())) {
			System.out.println("WHOOOOPS, you tried to move off the grid!");
			return false;
		}

		Integer collision = checkCollision(e, x, y);

		// empty square, eneemy, treasure

		if (collision == App.TREASURE) {
			this.gameFinished = true;
			System.out.println("You have won");

			return true;
		}
		if (collision == App.ENEMY) {
			this.gameFinished = true;
			System.out.println("You have lost");
			return true;
		}

		if (collision == App.PLAYER) {

			System.out.println("WHOOOOPS, leave the other player alone!");
			return false;

		}

		map.updateSquare(e.getxPos(), e.getyPos(), App.EMPTY_SQUARE);
		e.setxPos(entX + x);
		e.setyPos(entY + y);
		map.updateSquare(e.getxPos(), e.getyPos(), App.PLAYER);

		return true;

	}

	public static void main(String[] args) {

		while (true) {

			int res = App.mainMenu();

			if (res == 1) {
				
				Scanner scan = new Scanner(System.in);
				System.out.println("Hello, what is your name");
				System.out.println("Welcome, " + scan.nextLine());

				System.out.println("How many rows would you like?");
				int rows = scan.nextInt();

				System.out.println("How many columns would you like?");
				int cols = scan.nextInt();

				System.out.println("How many enemies would you like?");
				int ens = scan.nextInt();

				// scan.close();
				App app = new App(rows, cols, ens);
				app.runGame();

			}else if (res == 2) {
				App app = App.loadGame();
				app.runGame();
			} 
			else if (res == 4)
				break;
			else {
				System.out.println("Working on this!");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
