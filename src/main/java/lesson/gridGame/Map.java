package lesson.gridGame;

import java.util.Arrays;


import java.io.*;

public class Map implements Serializable{

	/**
	 * 
	 * 0 = empty square 1 = player 2 = treasure
	 * 
	 */

	private int[][] gameMap;
	private int numRows;
	private int numCols;

	public Map(int numRows, int numCols) {
		this.numRows = numRows;
		this.numCols = numCols;

		this.generateMap();

	}

	public void printMap() {
		
		for(int i = 0; i < numCols; i++) 
			System.out.print("----");
		
		System.out.print("\n");
		

		for (int row = 0; row < numRows; row++) {
			System.out.print("|  ");
			for (int col = 0; col < numCols; col++) {		
				
				if(gameMap[row][col] == 1)
					System.out.print("P  ");
//				else if (gameMap[row][col] == 2)
//					System.out.print("T  ");
//				else if (gameMap[row][col] == 3)
//					System.out.print("E  ");
				else
					System.out.print("•  ");
				
			}
			System.out.print("|\n");
		}
		
		for(int i = 0; i < numCols; i++) 
			System.out.print("----");

		System.out.print("\n");

		// if walk into entity display that entity

	}

	public void updateSquare(int xPos, int yPos, int symbol) {
		gameMap[xPos][yPos] = symbol;
	}

	public boolean addEntity(int symbol, int xPos, int yPos) {

		if (gameMap[xPos][yPos] != App.EMPTY_SQUARE)
			return true;

		gameMap[xPos][yPos] = symbol;
		return false;

	}

	public void generateMap() {

		this.gameMap = new int[numRows][numCols];

		for (int x = 0; x < numRows; x++) {
			Arrays.fill(gameMap[x], 0);
		}
	}

	public int getRows() {
		return this.numRows;
	}

	public int getCols() {
		return this.numCols;
	}

}
