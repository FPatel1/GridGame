package lesson.gridGame;

import java.util.*;

public class App {

	public static final Integer EMPTY_SQUARE = 0;
	public static final Integer PLAYER = 1;
	public static final Integer TREASURE = 2;
	public static final Integer ENEMY = 3;

	private Map map;

	private HashMap<Integer, Entity> entities = new HashMap<>();

	private int treasureX;
	private int treasureY;

	private int numEnemies = 0;

	private boolean winStatus = false;

	public App(int numRows, int numCols, int numEnemies) {

		map = new Map(numRows, numCols);
		this.numEnemies = numEnemies;

		this.generateEntity(App.PLAYER);
		this.generateEntity(App.TREASURE);
	}

	public void runGame() {

		System.out.println("Game has started");

		while (!winStatus) {

			//System.out.println("Treasure: " + treasureX + "  " + treasureY);

			int playerX = entities.get(App.PLAYER).getxPos();
			int playerY = entities.get(App.PLAYER).getyPos();

			System.out.println("Player is currently at X:" + playerX + " Y:" + playerY);

			this.checkDistance();

			System.out.println("Enter 'w' 'a' 's' or 'd' to move");

			Scanner scan = new Scanner(System.in);
			String read = scan.nextLine();

			if (read.toLowerCase().length() == 1) {
				if (read.toLowerCase().charAt(0) == 'w')
					this.movePlayer(0, 1);
				else if (read.toLowerCase().charAt(0) == 'a')
					this.movePlayer(-1, 0);
				else if (read.toLowerCase().charAt(0) == 's')
					this.movePlayer(0, -1);
				else if (read.toLowerCase().charAt(0) == 'd')
					this.movePlayer(1, 0);
				else
					System.out.println("Type 'w' 'a' 's' or 'd'");

			} else
				System.out.println("Type 'w' 'a' 's' or 'd'");

		}
		System.out.println("Congratulations! You have found the treasure");

	}

	public void generateEntity(int symbol) {

		Random rand = new Random();

		int xSet = rand.nextInt(map.getRows());
		int ySet = rand.nextInt(map.getCols());

		if (!map.addEntity(symbol, xSet, ySet))
			this.generateEntity(symbol);

		if (symbol == this.TREASURE) {
			treasureX = xSet;
			treasureY = ySet;
		}

		Entity play;
		if (symbol == App.PLAYER) {
			play = new Player(xSet, ySet, "Voice");
			entities.put(App.PLAYER, play);
		}
	}

	public boolean checkWin() {

		if (entities.get(App.PLAYER).getxPos() == treasureX && entities.get(App.PLAYER).getyPos() == treasureY)
			return true;

		return false;
	}

	public void checkDistance() {

		int xDiff = Math.abs(treasureX - entities.get(App.PLAYER).getxPos());

		int yDiff = Math.abs(treasureY - entities.get(App.PLAYER).getyPos());

		System.out.println("You are " + (xDiff + yDiff) + " moves away!");

	}

	public void printEverything() {

		System.out.println("Player x:" + entities.get(PLAYER).getxPos());
		System.out.println("Player y:" + entities.get(PLAYER).getyPos());
		System.out.println("numRows:" + map.getRows());
		System.out.println("numCols:" + map.getCols());

		System.out.println("TreasureX:" + treasureX);
		System.out.println("TreasureY:" + treasureY);
	}

	public boolean movePlayer(int x, int y) {

		// printEverything();

		int playerX = entities.get(App.PLAYER).getxPos();
		int playerY = entities.get(App.PLAYER).getyPos();

		if (!(playerX + x >= 0 && playerX + x < map.getRows()))
			return false;

		if (!(playerY + y >= 0 && playerY + y < map.getCols()))
			return false;

		entities.get(App.PLAYER).setxPos(entities.get(App.PLAYER).getxPos() + x);
		entities.get(App.PLAYER).setyPos(entities.get(App.PLAYER).getyPos() + y);

		if (checkWin()) {
			this.winStatus = true;
		} else {
			map.updateSquare(entities.get(App.PLAYER).getxPos(), entities.get(App.PLAYER).getyPos(), App.PLAYER);
		}

		return true;
	}

	public static void main(String[] args) {

		App app = new App(4, 4, 0);
		app.runGame();

	}
}
