package lesson.gridGame;

import java.io.*;

public class Player extends Entity {

	public Player(int xPos, int yPos, String voiceline) {

		super(xPos, yPos, voiceline);

		Player.SYMBOL = 1;

	}

	public boolean move(int xPos, int yPos) {

		// validation here

		this.setxPos(xPos);
		this.setyPos(yPos);

		return true;

	}

}
