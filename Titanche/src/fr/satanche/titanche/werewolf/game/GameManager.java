package fr.satanche.titanche.werewolf.game;

import java.util.HashMap;
import java.util.Map;

import fr.satanche.titanche.werewolf.player.Player;
import fr.satanche.titanche.werewolf.roles.Role;
import fr.satanche.titanche.werewolf.util.Version;
import net.dv8tion.jda.core.entities.User;

public class GameManager {

	private static Map<Player,User> linkUserPlayer;
	private static Map<String, GameBuilder> linkUserGame;
	private static Map<String, Game> runningGame;
	public static final GameManager INSTANCE = new GameManager();
	
	private GameManager(){
		linkUserPlayer = new HashMap<Player,User>();
		linkUserGame = new HashMap<String, GameBuilder>();
		runningGame = new HashMap<String,Game>();
	}
	
	/* *******************
	 * Player management *
	 * *******************/
	
	/**
	 * Create a new player
	 * @param user that will be associated to the player
	 * @param pseudo of the player
	 * @return if the pseudo is free or not
	 */
	public static boolean newPlayer(User user, String pseudo){
		Player currPlayer = new Player(pseudo);
		if(! linkUserPlayer.containsKey(currPlayer)){
			linkUserPlayer.put(new Player(pseudo),user);
			return true;
		}
		return false;
	}
	
	/**
	 * Remove the player
	 * @param pseudo of the player
	 */
	public static void removePlayer(String pseudo){
		Player currPlayer = new Player(pseudo);
		linkUserPlayer.remove(currPlayer);
	}	
	
	/**
	 * Remove the player
	 * @param player to remove
	 */
	public static void removePlayer(Player player){
		linkUserPlayer.remove(player);
	}

	/* *******************
	 *  Game management  *
	 * *******************/
	/**
	 * Create a new GameBuilder
	 * @param name name of the GameBuilder
	 */
	public static boolean createNewGameBuilder(String name){
		if(!linkUserGame.containsKey(name) && !runningGame.containsKey(name)){
			linkUserGame.put(name, new GameBuilder());
			return true;
		}
		return false;
	}
	
	/**
	 * Verify if the player is in any Game or GameBuilder
	 * @param player
	 * @return
	 */
	public static boolean removeIfLastOccurence(Player player){
		for(GameBuilder gameBuilder : linkUserGame.values()){
			if(gameBuilder.hasPlayer(player)){
				return false;
			}
		}
		for(Game game : runningGame.values()){
			if(game.hasPlayer(player)){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Add the player from the given GameBuilder, and create the link between the user and the player if it is the first occurrence
	 * @param user calling the action
	 * @param name of the game
	 * @param player to remove
	 * @return if the add correctly happen
	 */
	public static boolean addPlayerOfGame(User user, String name, String player){
		Player currPlayer = new Player(player);
		if(linkUserGame.get(name) != null){
			if(linkUserPlayer.get(currPlayer) != user){
				newPlayer(user,player);
			}
			linkUserGame.get(name).addPlayer(currPlayer);
			return true;
		}
		return false;
	}
	
	/**
	 * Remove the player from the given GameBuilder, and remove the link between the user and the player if it is the last occurrence
	 * @param user calling the action
	 * @param name of the game
	 * @param player to remove
	 * @return if the remove correctly happen
	 */
	public static boolean removePlayerOfGame(User user, String name, String player){
		Player currPlayer = new Player(player);
		if(linkUserGame.get(name) != null){
			linkUserGame.get(name).removePlayer(currPlayer);
			if(removeIfLastOccurence(currPlayer)) 
				linkUserPlayer.remove(currPlayer);
			return true;
		}
		return false;
	}
	
	
	/**
	 * Add role to the given game
	 * @param name of the game
	 * @param role to remove
	 * @return if the remove correctly happen
	 */
	public static boolean addRoleOfGame(String name, String role){
		Role currRole = Role.getRole(role);
		if(currRole != null && linkUserGame.get(name) != null){
			linkUserGame.get(name).addRole(currRole);
			return true;
		}
		return false;
	}
	
	
	/**
	 * Remove role to the given game
	 * @param name of the game
	 * @param role to remove
	 * @return if the remove correctly happen
	 */
	public static boolean removeRoleOfGame(String name, String role){
		Role currRole = Role.getRole(role);
		if(currRole != null && linkUserGame.get(name) != null){
			linkUserGame.get(name).removeRole(currRole);
			return true;
		}
		return false;
	}
	
	
	/**
	 * Set the version of the given game
	 * @param name of the game
	 * @param version of the game
	 * @return if the version is correct
	 */
	public static boolean setVersionOfGame(String name, String version){
		Version result = Version.getVersion(version);
		if(result != null && linkUserGame.get(name) != null){
			linkUserGame.get(name).setVersion(result);
			return true;
		}
		return false;
	}
	
	
	/**
	 * Launch the game
	 * @param name of the game
	 * @return if the game has correctly started
	 */
	public static boolean build(String name){
		Game result = linkUserGame.get(name).build();
		if(result.startGame()){
			runningGame.put(name, result);
			linkUserGame.remove(name);
			return true;
		}
		return false;
	}
}
