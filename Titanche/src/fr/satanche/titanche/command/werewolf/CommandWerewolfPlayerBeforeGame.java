package fr.satanche.titanche.command.werewolf;

import java.util.Properties;

import fr.satanche.titanche.command.core.Command;
import fr.satanche.titanche.command.core.Command.ExecutorType;
import fr.satanche.titanche.werewolf.game.GameBuilder;
import fr.satanche.titanche.werewolf.game.GameManager;
import fr.satanche.titanche.werewolf.player.Player;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public class CommandWerewolfPlayerBeforeGame {
	private GameManager gameManager;
	
	private final String PSEUDO_ALREADY_EXIST;
	private final String PLAYER_ADD;
	private final String PLAYER_REMOVE;
	private final String NEED_NAME_GAME;
	private final String UNDEFINED_NAME_GAME;
	private final String ALREADY_PARTICIPATING;
	private final String UNDEFINED_PLAYER;
	private final String ALREADY_PARTICIPATING_RUNNING_GAME;
	
	public CommandWerewolfPlayerBeforeGame(Properties prop){
		this.NEED_NAME_GAME = prop.getProperty("NEED_NAME_GAME");
		this.PSEUDO_ALREADY_EXIST = prop.getProperty("PSEUDO_ALREADY_EXIST");
		this.PLAYER_ADD = prop.getProperty("PLAYER_ADD");
		this.PLAYER_REMOVE = prop.getProperty("PLAYER_REMOVE");
		this.UNDEFINED_NAME_GAME = prop.getProperty("UNDEFINED_NAME_GAME");
		this.ALREADY_PARTICIPATING = prop.getProperty("ALREADY_PARTICIPATING");
		this.UNDEFINED_PLAYER = prop.getProperty("UNDEFINED_PLAYER");
		this.ALREADY_PARTICIPATING_RUNNING_GAME = prop.getProperty("ALREADY_PARTICIPATING_RUNNING_GAME");
		this.gameManager = GameManager.INSTANCE;
		
	}
	
	@Command(name="werewolf joinGame", description="Add the player to the current game : 'werewolf joinGame <game_name>'", type=ExecutorType.USER)
	public void joinGame(User author, MessageChannel channel, String[] param){
		if(param.length < 2){
			channel.sendMessage(NEED_NAME_GAME).complete();
		}else{
			String gameName = param[1];
			String pseudo = author.getName();
			//String pseudo = (param.length < 3) ? author.getName():param[2];
			if(gameManager.getPlayer(pseudo) != null && gameManager.getPlayer(author) != gameManager.getPlayer(pseudo)){
				channel.sendMessage(PSEUDO_ALREADY_EXIST).complete();
			}else if(gameManager.addPlayer(gameName, author, pseudo)){
				channel.sendMessage(PLAYER_ADD.replace("{0}", gameName)).complete();
			}else{
				GameBuilder currGameBuilder = gameManager.getGameBuilder(gameName);
				if(currGameBuilder == null){
					channel.sendMessage(UNDEFINED_NAME_GAME).complete();
				}else if(currGameBuilder.hasPlayer(gameManager.getPlayer(pseudo))){
					channel.sendMessage(ALREADY_PARTICIPATING).complete();
				}else{
					channel.sendMessage(ALREADY_PARTICIPATING_RUNNING_GAME).complete();
				}
			}
		}
	}
	
	@Command(name="werewolf leaveGame", description="Remove the player to the current game : 'werewolf joinGame <game_name>'", type=ExecutorType.USER)
	public void leaveGame(User author, MessageChannel channel, String[] param){
		if(param.length < 2){
			channel.sendMessage(NEED_NAME_GAME).complete();
		}else{
			String gameName = param[1];
			Player currPlayer = gameManager.getPlayer(author);
			if(currPlayer == null){
				channel.sendMessage(UNDEFINED_PLAYER).complete();
			}else if(gameManager.removePlayer(gameName, author, currPlayer.getPseudo())){
				channel.sendMessage(PLAYER_REMOVE.replace("{0}", gameName)).complete();
			}else{
				channel.sendMessage(UNDEFINED_NAME_GAME).complete();
			}
		}
	}
}
