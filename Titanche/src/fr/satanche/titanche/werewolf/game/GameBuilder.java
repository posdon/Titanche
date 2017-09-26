package fr.satanche.titanche.werewolf.game;

import java.util.ArrayList;
import java.util.List;

import fr.satanche.titanche.werewolf.player.Player;
import fr.satanche.titanche.werewolf.roles.Role;
import fr.satanche.titanche.werewolf.util.Version;

public class GameBuilder implements GameInterface {

	private String name;
	private List<Player> players;
	private List<Role> roles;
	private Version version;
	
	public GameBuilder(String name){
		this.name = name;
		version = Version.CLASSIC;
		players = new ArrayList<Player>();
		roles = new ArrayList<Role>();
	}
	
	public String getName(){
		return name;
	}
	
	public GameBuilder addPlayer(Player player){
		if(!players.contains(player)) players.add(player);
		return this;
	}
	
	public GameBuilder removePlayer(Player player){
		players.remove(player);
		return this;
	}
	
	public GameBuilder addRole(Role role){
		if(!role.isUnic()||!roles.contains(role)) 
			if(role.getAvailableVersion().contains(this.version))
					roles.add(role);
		return this;
	}
	
	public GameBuilder removeRole(Role role){
		roles.remove(role);
		return this;
	}
	
	public GameBuilder setVersion(Version version){
		this.version = version;
		for(Role role : roles){
			if(!role.getAvailableVersion().contains(version)){
				roles.remove(role);
			}
		}
		return this;
	}
	
	public boolean hasPlayer(Player player){
		return players.contains(player);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameBuilder other = (GameBuilder) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public Game build(){
		Game result = new Game(name, version, players, roles);
		roles.clear();
		players.clear();
		version = Version.CLASSIC;
		return result;
	}
}
