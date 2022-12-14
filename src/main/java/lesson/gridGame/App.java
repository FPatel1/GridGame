package lesson.gridGame;

import java.io.*;

import java.util.concurrent.ThreadLocalRandom;
import java.util.*;

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
			return Integer.parseInt(res);

		default:
			System.out.println("Invald input!");
			App.mainMenu();
			break;
		}
		return -1;

	}

	private void pause(long time) {

		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void runGame() {

		System.out.println("Game has started");

		while (!gameFinished) {

			map.printMap();

			this.pause(500);

			this.checkDistance();

			System.out.println("Enter 'w' 'a' 's' or 'd' to move or q to save and quit!");

			this.pause(500);

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

				} else
					System.out.println("Type 'w' 'a' 's' or 'd'");

			} else
				System.out.println("Type 'w' 'a' 's' or 'd'");
			this.pause(500);
		}

	}

	public void generateEntity(int symbol) {

		int xSet = ThreadLocalRandom.current().nextInt(0, map.getRows());
		int ySet = ThreadLocalRandom.current().nextInt(0, map.getCols());

		if (map.addEntity(symbol, xSet, ySet)) {
			this.generateEntity(symbol);
			return;

		}

		if (symbol == App.PLAYER) {
			Entity toAdd = new Entity(xSet, ySet, "Voice");
			players.add(toAdd);
		}

		if (symbol == App.TREASURE) {
			Entity toAdd = new Entity(xSet, ySet, "Voice");
			treasures.add(toAdd);
		}
		if (symbol == App.ENEMY) {
			Entity toAdd = new Entity(xSet, ySet,
					Entity.lines[ThreadLocalRandom.current().nextInt(0, Entity.lines.length)]);
			enemies.add(toAdd);
		}
	}

	public Integer checkCollision(Entity e, int xMove, int yMove) {

		int x = e.getxPos() + xMove;
		int y = e.getyPos() + yMove;

		for (Entity enemy : enemies) {
			if (x == enemy.getxPos() && y == enemy.getyPos()) {
				System.out.println(enemy.getVoiceline());
				return App.ENEMY;

			}
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

		if (collision == App.TREASURE) {
			this.gameFinished = true;
			System.out.println("You have won");

			return true;
		}
		if (collision == App.ENEMY) {
			this.gameFinished = true;
			System.out.println("You have lost");

			this.pause(1000);
			System.out.print("But wait");
			this.pause(500);
			System.out.print(".");
			this.pause(500);
			System.out.print(".");
			this.pause(500);
			System.out.print(".\n");
			
			System.out.println("The enemies have a message for you!");
			this.pause(1000);

			for (Entity ent : enemies) {
				System.out.println(ent.getVoiceline());
				this.pause(500);
			}

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

				if (ens + 1 + 1 > (rows * cols)) {
					System.out.println("Not enough space");
					continue;
				}
				// scan.close();
				App app = new App(rows, cols, ens);
				app.runGame();

			} else if (res == 2) {
				App app = App.loadGame();
				app.runGame();
			} else if (res == 4)
				break;
			else {
				System.out.println("Not Valid!");
			}
		}
	}
}
