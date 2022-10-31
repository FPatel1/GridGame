package lesson.gridGame;

public class Entity {

	public static int SYMBOL;
	private int xPos;
	private int yPos;
	private String voiceline;

	public Entity(int xPos, int yPos, String voiceline) {

		this.xPos = xPos;
		this.yPos = yPos;
		this.voiceline = voiceline;
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
