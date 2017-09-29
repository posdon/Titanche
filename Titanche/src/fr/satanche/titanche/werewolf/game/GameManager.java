package fr.satanche.titanche.werewolf.game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.satanche.titanche.werewolf.player.Player;
import fr.satanche.titanche.werewolf.roles.Role;
import fr.satanche.titanche.werewolf.util.Version;
import net.dv8tion.jda.core.entities.User;

public class GameManager {

	private Map<GameBuilder,User> linkGameCreator;
	private Map<Game,User> linkRunningGame;
	private Map<User,Player> linkPlayerUser;
	public static final GameManager INSTANCE = new GameManager();
	
	private GameManager(){
		linkGameCreator = new HashMap<GameBuilder,User>();
		linkRunningGame = new HashMap<Game,User>();
		linkPlayerUser = new HashMap<User,Player>();
	}
	
	
	/* **********************
	 * 						*
	 *	Building management	*
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
			if(getGame(name) == null && getGameBuilder(name) == null) {
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
		GameBuilder currGameBuilder = getGameBuilder(name,user);
		if(currGameBuilder != null){
			return currGameBuilder.addRole(Role.getRole(role));
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
		GameBuilder currGameBuilder = getGameBuilder(name,user);
		if(currGameBuilder != null){
			return currGameBuilder.removeRole(Role.getRole(role));
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
		GameBuilder currGameBuilder = getGameBuilder(name,user);
		if(currGameBuilder != null){
			return currGameBuilder.setVersion(Version.getVersion(version));
		}
		return false;
	}
	
	/**
	 * Get the list of current roles
	 * @param name of the building game
	 * @param user creator of the game
	 * @return if the game exists, the list of the current roles. If it doesn't return null
	 */
	public List<Role> getRoles(String name){
		GameInterface currGameInterface = getGameBuilder(name);
		if(currGameInterface == null)
			currGameInterface = getGame(name);
		if(currGameInterface != null){
			return currGameInterface.getRoles();
		}
		return null;
	}
	
	/**
	 * Create and start the Game object
	 * @param name of the game
	 * @param user creator of the game
	 * @return if the game has started correctly
	 */
	public boolean startGame(String name, User user){
		GameBuilder currGameBuilder = getGameBuilder(name, user);
		if(currGameBuilder != null){
			Game currGame = currGameBuilder.build();
			if(currGame.startGame()){
				linkRunningGame.put(currGame, user);
				linkGameCreator.remove(currGameBuilder);
				return true;
			}
		}
		return false;
	}
		
	/**
	 * Add a player to the given game
	 * @param name of the GameInterface (GameBuilder)
	 * @param user adding a player
	 * @param pseudo of the player
	 * @return if the player has been correctly added.
	 */
	public boolean addPlayer(String name, User user, String pseudo){
		GameBuilder currGameBuilder = getGameBuilder(name);
		if(currGameBuilder != null){
			Player currPlayer = getPlayer(pseudo);
			if(currPlayer == null){
				currPlayer = new Player(pseudo);
				linkPlayerUser.put(user,currPlayer);
			}
			for(Game currGame : linkRunningGame.keySet()){
				if(currGame.hasPlayer(currPlayer)) return false;
			}
			return currGameBuilder.addPlayer(currPlayer);
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
		GameBuilder currGameBuilder = getGameBuilder(name);
		if(currGameBuilder != null){
			Player currPlayer = getPlayer(pseudo);
			if(currPlayer != null){
				boolean result = currGameBuilder.removePlayer(currPlayer);
				if(!isActivePlayer(currPlayer)){
					for(User currUser : linkPlayerUser.keySet()){
						if(currPlayer.equals(linkPlayerUser.get(currUser)))
							linkPlayerUser.remove(currUser);
					}
				}
				return result;
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
		GameInterface currGameInterface = getGameBuilder(name);
		if(currGameInterface == null)
			currGameInterface = getGame(name);
		if(currGameInterface != null){
			return currGameInterface.getPlayers();
		}
		return null;
	}
	
	/* **********************
	 * 						*
	 *	Playing management	*
	 * 						*
	 * **********************/
	
		
	
	/* **********************
	 * 						*
	 *	   Utils function	*
	 * 						*
	 * **********************/
	
	public String printGamePlayers(String name, User user){
		String result = "";
		List<Player> playerList = getPlayers(name);
		if(getGameBuilder(name) != null){
			for(Player player : playerList) result += player.getPseudo()+"\n";
		}else if(getGame(name) != null){
			for(Player player : playerList){
				result += player.getPseudo();
				if(!player.isAlive())
					result += (linkRunningGame.get(getGame(name)).equals(user))? " (Killed by "+player.getCauseOfDeath().toString()+")":" (DEAD)";
				if(linkRunningGame.get(getGame(name)).equals(user)){
					result += " : "+player.getRole().toString();
				}
				result += "\n";
			}
		}
		return result;
	}
	
	public String printGameRoles(String name, User user){
		String result = "";
		GameInterface gameInterface = getGameBuilder(name,user);
		if(gameInterface == null)
			gameInterface = getGame(name,user);
		if(gameInterface != null){
			List<Role> roleList = getRoles(name);
			for(Role role : roleList){
				result += role.toString()+"\n";
			}
		}
		return result;
	}

	public GameBuilder getGameBuilder(String name){
		for(GameBuilder gameBuilder : linkGameCreator.keySet()){
			if(gameBuilder.getName().equals(name)){
				return gameBuilder;
			}
		}
		return null;
	}
	
	public GameBuilder getGameBuilder(String name, User user){
		for(GameBuilder gameBuilder : linkGameCreator.keySet()){
			if(gameBuilder.getName().equals(name) && linkGameCreator.get(gameBuilder).equals(user)){
				return gameBuilder;
			}
		}
		return null;
	}
	
	public Game getGame(String name){
		for(Game game : linkRunningGame.keySet()){
			if(game.getName().equals(name)){
				return game;
			}
		}
		return null;
	}
	
	public Game getGame(String name, User user){
		for(Game game : linkRunningGame.keySet()){
			if(game.getName().equals(name) && linkGameCreator.get(game).equals(user)){
				return game;
			}
		}
		return null;
	}
	
	public Player getPlayer(String pseudo){
		for(User user : linkPlayerUser.keySet()){
			Player currPlayer = linkPlayerUser.get(user); 
			if(currPlayer.getPseudo().equals(pseudo)) return currPlayer;
		}
		return null;
	}
	
	public Player getPlayer(User user){
		return linkPlayerUser.get(user);
	}
	
	public boolean isActivePlayer(Player player){
		for(GameBuilder gameBuilder : linkGameCreator.keySet()){
			if(gameBuilder.hasPlayer(player)){
				return true;
			}
		}
		for(Game game : linkRunningGame.keySet()){
			if(game.hasPlayer(player)) return true;
		}
		return false;
	}
	
}
