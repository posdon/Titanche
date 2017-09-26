package fr.satanche.titanche.werewolf.game;

import java.util.HashMap;
import java.util.Map;

import fr.satanche.titanche.werewolf.player.Player;
import fr.satanche.titanche.werewolf.roles.Role;
import fr.satanche.titanche.werewolf.util.Version;
import net.dv8tion.jda.core.entities.User;

public class GameManager {

	private Map<GameInterface,User> linkGameCreator;
	private Map<Player,User> linkPlayerUser;
	public static final GameManager INSTANCE = new GameManager();
	
	private GameManager(){
		linkGameCreator = new HashMap<GameInterface,User>();
		linkPlayerUser = new HashMap<Player,User>();
	}
	
	
	
	/* **********************
	 * 						*
	 *	Creator management	*
	 * 						*
	 * **********************/
	
	/**
	 * Create a new GameInterface (GameBuilder)
	 * @param name of the gameInterface
	 * @param user creator of the gameInterface
	 * @return if the name is valid (not empty or already used)
	 */
	public boolean createNewGameBuilder(String name, User user){
		if(!name.equals("")){
			GameInterface currGameInterface = isInLinkGameCreator(name);
			if(currGameInterface == null) {
				linkGameCreator.put(new GameBuilder(name),user);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Add a role to the given GameBuilder
	 * @param name of the gameBuilder
	 * @param user creator of the gameBuilder
	 * @param role to add
	 * @return if the role has been correctly added
	 */
	public boolean addRole(String name, User user, String role){
		GameInterface currGameInterface = isInLinkGameCreator(name,user);
		if(currGameInterface != null){
			if(currGameInterface instanceof GameBuilder){
				((GameBuilder) currGameInterface).addRole(Role.getRole(role));
				return true;
			}
		}
		return false;
	}
	
	/** Remove a role to the given GameBuilder
	 * @param name of the gameBuilder
	 * @param user creator of the gameBuilder
	 * @param role to remove
	 * @return if the role has been correctly removed
	 */
	public boolean removeRole(String name, User user, String role){
		GameInterface currGameInterface = isInLinkGameCreator(name,user);
		if(currGameInterface != null){
			if(currGameInterface instanceof GameBuilder){
				((GameBuilder) currGameInterface).removeRole(Role.getRole(role));
			}
		}
		return false;
	} 
	
	/** Set the version of the given GameBuilder
	 * @param name of the gameBuilder
	 * @param user creator of the gameBuilder
	 * @param version to set
	 * @return if the version has been correctly set
	 */
	public boolean setVersion(String name, User user, String version){
		GameInterface currGameInterface = isInLinkGameCreator(name,user);
		if(currGameInterface != null){
			if(currGameInterface instanceof GameBuilder){
				((GameBuilder) currGameInterface).setVersion(Version.getVersion(version));
			}
		}
		return false;
	}
	
	/**
	 * Create and start the Game object
	 * @param name of the game
	 * @param user creator of the game
	 * @return if the game has started correctly
	 */
	public boolean startGame(String name, User user){
		GameInterface currGameInterface = isInLinkGameCreator(name, user);
		if(currGameInterface != null){
			Game currGame = ((GameBuilder) currGameInterface).build();
			if(currGame.startGame()){
				linkGameCreator.put(currGame, user);
				linkGameCreator.remove(currGameInterface);
				return true;
			}
		}
		return false;
	}
	
	/* **********************
	 * 						*
	 *	 Player management	*
	 * 						*
	 * **********************/	
	
	/**
	 * Add a player to the given game
	 * @param name of the GameInterface (GameBuilder)
	 * @param user adding a player
	 * @param pseudo of the player
	 * @return if the player has been correctly added.
	 */
	public boolean addPlayer(String name, User user, String pseudo){
		GameInterface currGameInterface = isInLinkGameCreator(name);
		if(currGameInterface != null){
			if(currGameInterface instanceof GameBuilder){
				Player currPlayer = isExistantPlayer(pseudo);
				if(currPlayer == null){
					currPlayer = new Player(pseudo);
					linkPlayerUser.put(currPlayer, user);
				}
				((GameBuilder) currGameInterface).addPlayer(currPlayer);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Remove a player from the given game
	 * @param name of the gamebuilder
	 * @param user removing his player
	 * @param pseudo of the player
	 * @return if the player has been correctly removed
	 */
	public boolean removePlayer(String name, User user, String pseudo){
		GameInterface currGameInterface = isInLinkGameCreator(name);
		if(currGameInterface != null){
			if(currGameInterface instanceof GameBuilder){
				Player currPlayer = isExistantPlayer(pseudo);
				if(currPlayer != null){
					((GameBuilder) currGameInterface).removePlayer(currPlayer);
					if(!isActivePlayer(currPlayer)) linkPlayerUser.remove(currPlayer);
					return true;
				}
			}
		}
		return false;
	}
	
	/* **********************
	 * 						*
	 *	   Utils function	*
	 * 						*
	 * **********************/
	
	public GameInterface isInLinkGameCreator(String name){
		for(GameInterface gameInterface : linkGameCreator.keySet()){
			if(gameInterface.getName().equals(name)){
				return gameInterface;
			}
		}
		return null;
	}
	
	public GameInterface isInLinkGameCreator(String name, User user){
		for(GameInterface gameInterface : linkGameCreator.keySet()){
			if(gameInterface.getName().equals(name) && linkGameCreator.get(gameInterface).equals(user)){
				return gameInterface;
			}
		}
		return null;
	}
	
	public Player isExistantPlayer(String pseudo){
		for(Player player : linkPlayerUser.keySet()){
			if(player.getPseudo().equals(pseudo)){
				return player;
			}
		}
		return null;
	}
	
	public boolean isActivePlayer(Player player){
		for(GameInterface gameInterface : linkGameCreator.keySet()){
			if(gameInterface.hasPlayer(player)){
				return true;
			}
		}
		return false;
	}
	
}
