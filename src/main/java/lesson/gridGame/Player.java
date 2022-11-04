package lesson.gridGame;

import java.io.*;

public class Player extends Entity {
	
	private String name;

	public Player(int xPos, int yPos, String voiceline, String name) {

		super(xPos, yPos, voiceline);
		this.name = name;
		Player.SYMBOL = 1;

	}

	public boolean move(int xPos, int yPos) {

		// validation here

		this.setxPos(xPos);
		this.setyPos(yPos);

		return true;

	}

	public String getName() {
		return this.name;
	}
	
}
