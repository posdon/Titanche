package fr.satanche.titanche.roleplay;

public class Game {

	private final int ID;
	private GameState GAME_STATE;
	
	public Game(int id) {
		this.ID = id;
		this.GAME_STATE = GameState.PREPARING;
	}
	
	public void start() {
		if(this.GAME_STATE.equals(GameState.PREPARING) || this.GAME_STATE.equals(GameState.STOPPED)) {
			this.GAME_STATE = GameState.RUNNING;
		}
	}
	
	public void stop() {
		if(this.GAME_STATE.equals(GameState.RUNNING)) {
			this.GAME_STATE = GameState.STOPPED;
		}
	}
	
	public void end() {
		if(this.GAME_STATE.equals(GameState.RUNNING)) {
			this.GAME_STATE = GameState.ENDED;
		}
	}
	
}
