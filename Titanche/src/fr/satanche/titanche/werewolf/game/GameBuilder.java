package fr.satanche.titanche.werewolf.game;

import java.util.ArrayList;
import java.util.List;

import fr.satanche.titanche.werewolf.player.Player;
import fr.satanche.titanche.werewolf.roles.Role;
import fr.satanche.titanche.werewolf.util.Version;

public class GameBuilder {

	private List<Player> players;
	private List<Role> roles;
	private Version version;
	
	public GameBuilder(){
		version = Version.CLASSIC;
		players = new ArrayList<Player>();
		roles = new ArrayList<Role>();
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
		if(!role.isUnic()||!roles.contains(role)) roles.add(role);
		return this;
	}
	
	public GameBuilder removeRole(Role role){
		roles.remove(role);
		return this;
	}
	
	public GameBuilder setVersion(Version version){
		this.version = version;
		return this;
	}
	
	public Game build(){
		Game result = new Game(version, players, roles);
		roles.clear();
		players.clear();
		version = Version.CLASSIC;
		return result;
	}
}
