package fr.satanche.titanche.command.werewolf;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import fr.satanche.titanche.MainApp;
import fr.satanche.titanche.command.core.Command;
import fr.satanche.titanche.command.core.Command.ExecutorType;
import fr.satanche.titanche.werewolf.game.GameManager;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public class CommandWerewolfCreatorBeforeGame {

	private GameManager gameManager;
	private final String UNDEFINED_NAME_GAME;
	private final String NEED_NAME_GAME;
	private final String NEED_NAME_ROLE;
	private final String NEED_NAME_VERSION;
	private final String NEED_NAME_PSEUDO;
	private final String ROLE_ADD;
	private final String ROLE_REMOVE;
	private final String GAME_CREATED;
	private final String VERSION_SET;
	private final String UNDEFINED_NAME_VERSION;
	private final String UNDEFINED_NAME_ROLE;
	private final String NOT_ROLE_IN_GAME;
	private final String PRINT_PLAYER_IN_GAME;
	private final String PRINT_ROLE_IN_GAME;
	
	public CommandWerewolfCreatorBeforeGame(Properties prop){
		this.UNDEFINED_NAME_GAME = prop.getProperty("UNDEFINED_NAME_GAME");
		this.NEED_NAME_GAME = prop.getProperty("NEED_NAME_GAME");
		this.NEED_NAME_ROLE = prop.getProperty("NEED_NAME_ROLE");
		this.NEED_NAME_VERSION = prop.getProperty("NEED_NAME_VERSION");
		this.NEED_NAME_PSEUDO = prop.getProperty("NEED_NAME_PSEUDO");
		this.ROLE_ADD = prop.getProperty("ROLE_ADD");
		this.ROLE_REMOVE = prop.getProperty("ROLE_REMOVE");
		this.GAME_CREATED = prop.getProperty("GAME_CREATED");
		this.VERSION_SET = prop.getProperty("VERSION_SET");
		this.UNDEFINED_NAME_VERSION = prop.getProperty("UNDEFINED_NAME_VERSION");
		this.UNDEFINED_NAME_ROLE = prop.getProperty("UNDEFINED_NAME_ROLE");
		this.NOT_ROLE_IN_GAME = prop.getProperty("NOT_ROLE_IN_GAME");
		this.PRINT_PLAYER_IN_GAME = prop.getProperty("PRINT_PLAYER_IN_GAME");
		this.PRINT_ROLE_IN_GAME = prop.getProperty("PRINT_ROLE_IN_GAME");
		this.gameManager = GameManager.INSTANCE;
	}
	
	
	@Command(name="werewolf createGame", description="Create a new game : 'werewolf createGame <game_name>'", type=ExecutorType.USER)
	public void createGame(User author, MessageChannel channel, String[] param){
		if(param.length < 2){
			channel.sendMessage(NEED_NAME_GAME).complete();
		}else {
			String gameName = param[1];
			if(gameManager.createNewGameBuilder(gameName,author)){
				channel.sendMessage(GAME_CREATED.replace("{0}",gameName)).complete();
			}else{
				channel.sendMessage("This name is already taken by another game. Please choose another name.").complete();
			}
		}
	}
	
	@Command(name="werewolf setGameVersion", description="Set the version of the game : 'werewolf setGameVersion <game_name> <version>", type=ExecutorType.USER)
	public void setVersion(User author, MessageChannel channel, String[] param){
		if(param.length < 2){
			channel.sendMessage(NEED_NAME_GAME).complete();
		}else if(param.length < 3){
			channel.sendMessage(NEED_NAME_VERSION).complete();
		}else {
			String gameName = param[1];
			String version = param[2];
			if(gameManager.setVersion(gameName, author, version.toUpperCase())){
				channel.sendMessage(VERSION_SET.replace("{0}", gameName).replace("{1}", version.toUpperCase())).complete();
			}else if(gameManager.isInLinkGameCreator(gameName) == null){
				channel.sendMessage(UNDEFINED_NAME_GAME).complete();
			}else{
				channel.sendMessage(UNDEFINED_NAME_VERSION).complete();
			}
		}
	}
	
	
	@Command(name="werewolf addRole", description="Add a role of the game : 'werewolf addRole <game_name> <role>", type=ExecutorType.USER)
	public void addRole(User author, MessageChannel channel, String[] param){
		if(param.length < 2){
			channel.sendMessage(NEED_NAME_GAME).complete();
		}else if(param.length < 3){
			channel.sendMessage(NEED_NAME_ROLE).complete();
		}else{
			String gameName = param[1];
			String role = param[2];
			if(gameManager.addRole(gameName, author, role)){
				channel.sendMessage(ROLE_ADD.replace("{0}", role)).complete();
			}else if(gameManager.isInLinkGameCreator(gameName) == null){
				channel.sendMessage(UNDEFINED_NAME_GAME).complete();
			}else{
				channel.sendMessage(UNDEFINED_NAME_ROLE).complete();
			}
		}
	}
	
	@Command(name="werewolf removeRole", description="Remove a role of the game : 'werewolf removeRole <game_name> <role>", type=ExecutorType.USER)
	public void removeRole(User author, MessageChannel channel, String[] param){
		if(param.length < 2){
			channel.sendMessage(NEED_NAME_GAME).complete();
		}else if(param.length < 3){
			channel.sendMessage(NEED_NAME_ROLE).complete();
		}else{
			String gameName = param[1];
			String role = param[2];
			if(gameManager.removeRole(gameName, author, role)){
				channel.sendMessage(ROLE_REMOVE.replace("{0}", role)).complete();
			}else if(gameManager.isInLinkGameCreator(gameName) == null){
				channel.sendMessage(UNDEFINED_NAME_GAME).complete();
			}else{
				channel.sendMessage(NOT_ROLE_IN_GAME).complete();
			}
		}
	}
	
	@Command(name="werewolf playerStatus", description="Check all the player of the game : 'werewolf playerStatus <game_name>'", type=ExecutorType.USER)
	public void playerStatus(User author, MessageChannel channel, String[] param){
		if(param.length < 2){
			channel.sendMessage(NEED_NAME_GAME).complete();
		}else{
			String gameName = param[1];
			String printing = gameManager.printPlayers(gameName, author);
			if(printing != null){
				channel.sendMessage(PRINT_PLAYER_IN_GAME.replace("{0}", gameName).replace("{1}", printing)).complete();
			}else{
				channel.sendMessage(UNDEFINED_NAME_GAME).complete();
			}
		}
	}
	
	@Command(name="werewolf roleStatus", description="Check all the role of the game : 'werewolf roleStatus <game_name>'", type=ExecutorType.USER)
	public void roleStatus(User author, MessageChannel channel, String[] param){
		String gameName = param[1];
		if(param.length < 2){
			channel.sendMessage(NEED_NAME_GAME).complete();
		}else{
			String printing = gameManager.printRoles(gameName, author);
			if(printing != null){
				channel.sendMessage(PRINT_ROLE_IN_GAME.replace("{0}", gameName).replace("{1}", printing)).complete();
			}else{
				channel.sendMessage(UNDEFINED_NAME_GAME).complete();
			}
		}
	}
}
