package lesson.gridGame;

import java.io.*;

public class Entity implements Serializable {

	public static int SYMBOL;
	private int xPos;
	private int yPos;
	private String voiceline;

	public static final String[] lines = { "We've been trying to reach you about your cars extended warranty",
			"Call an ambulance, but not for me", "Enemy.VoiceLine.Load()", "You're on the wrong side of the town buddy",
			"This class isn't big enough for the two of us buster" };

	public Entity(int xPos, int yPos, String voiceline) {

		this.xPos = xPos;
		this.yPos = yPos;
		this.voiceline = voiceline;
	}

	public boolean equals(Object another) {

		if (another instanceof Entity) {
			if (this.xPos == ((Entity) another).getxPos() && ((Entity) another).getyPos() == this.yPos)
				return true;
		}
		return false;
	}

	public int getxPos() {
		return this.xPos;
	}

	public int getyPos() {
		return this.yPos;
	}

	public String getVoiceline() {
		return this.voiceline;
	}

	public void setxPos(int xPos) {
		this.xPos = xPos;
	}

	public void setyPos(int yPos) {
		this.yPos = yPos;
	}

}
