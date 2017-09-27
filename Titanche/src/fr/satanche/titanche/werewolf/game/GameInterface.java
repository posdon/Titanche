package fr.satanche.titanche.werewolf.game;

import java.util.List;

import fr.satanche.titanche.werewolf.player.Player;
import fr.satanche.titanche.werewolf.roles.Role;

public interface GameInterface {
	public String getName();
	public List<Player> getPlayers();
	public List<Role> getRoles();
	public boolean hasPlayer(Player player);
}
