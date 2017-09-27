package fr.satanche.titanche.werewolf.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
				return ((GameBuilder) currGameInterface).addRole(Role.getRole(role));
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
				return ((GameBuilder) currGameInterface).removeRole(Role.getRole(role));
			}
		}
		return false;
	} 
	
	/**
	 * Get the list of current roles
	 * @param name of the building game
	 * @param user creator of the game
	 * @return if the game exists, the list of the current roles. If it doesn't return null
	 */
	public List<Role> getRoles(String name, User user){
		GameInterface currGameInterface = isInLinkGameCreator(name,user);
		if(currGameInterface != null){
			if(currGameInterface instanceof GameBuilder){
				return ((GameBuilder) currGameInterface).getRoles();
			}
		}
		return null;
	}
	/**
	 * Print all the detail of the roles of the game
	 * @param name of the gameInterface
	 * @param user calling the function
	 * @return null if we don't have any game, the detail of the role instead
	 */
	public String printRoles(String name, User user){
		List<Role> rolesList = getRoles(name,user);
		if(rolesList == null){
			return null;
		}else{
			Map<Role,Integer> counterRole = new HashMap<Role,Integer>();
			for(Role currRole : rolesList){
				if(counterRole.containsKey(currRole)){
					counterRole.put(currRole, counterRole.get(currRole)+1);
				}else{
					counterRole.put(currRole, 1);
				}
			}
			String result = "";
			for(Role currRole : counterRole.keySet()){
				result += currRole.toString()+" : "+counterRole.get(currRole)+"\n";
			}
			return result;
		}
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
				return ((GameBuilder) currGameInterface).setVersion(Version.getVersion(version));
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
				return ((GameBuilder) currGameInterface).addPlayer(currPlayer);
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
					boolean result = ((GameBuilder) currGameInterface).removePlayer(currPlayer);
					if(!isActivePlayer(currPlayer)) linkPlayerUser.remove(currPlayer);
					return result;
				}
			}
		}
		return false;
	}
	
	/**
	 * Get the list of player in the given GameInterface
	 * @param name of the GameInterface
	 * @return all the player subscribe to this gameInterface
	 */
	public List<Player> getPlayers(String name){
		GameInterface currGameInterface = isInLinkGameCreator(name);
		if(currGameInterface != null){
			if(currGameInterface instanceof GameBuilder){
				return ((GameBuilder) currGameInterface).getPlayers();
			}
		}
		return null;
	}

	/**
	 * Print all the detail of the players of the game
	 * @param name of the gameInterface
	 * @param user calling the function
	 * @return null if we don't have any game, the detail of the player instead
	 */
	public String printPlayers(String name, User user){
		GameInterface currGameInterface = isInLinkGameCreator(name,user);
		List<Player> playerList = getPlayers(name);
		if(playerList == null){
			// We don't have any game with that name
			return null;
		}else{
			String result = "";
			if(currGameInterface == null || playerList.contains(user) || currGameInterface instanceof GameBuilder){
				// The user isn't the game master or is part of the player or the game hasn't started yet.
				for(Player player : playerList) result += player.getPseudo()+"\n";
			}else{
				for(Player player : playerList) {
					result += player.getPseudo();
					result += (!player.isAlive())?" (DEAD)":"";
					result +=" : "+player.getRole().toString()+"\n";					
				}
			}
			return result;
		}
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
