package fr.satanche.titanche.werewolf;

import java.util.ArrayList;
import java.util.List;

import fr.satanche.titanche.werewolf.roles.Role;
import fr.satanche.titanche.werewolf.util.CauseOfDeath;
import fr.satanche.titanche.werewolf.util.Team;

public class Player {

	private String pseudo;
	private List<Team> teams;
	private Role role;
	private boolean isAlive;
	private CauseOfDeath causeOfDeath;
	private boolean canVote;
	
	public Player(String pseudo){
		this.pseudo = pseudo;
		teams = new ArrayList<Team>();
		role = null;
		isAlive = true;
		causeOfDeath = CauseOfDeath.NOT_DEAD;
	}
	
	public boolean setRole(Role role){
		if(this.role == null){
			this.role = role;
			for(Team team : role.getStartingTeams()){
				teams.add(team);
			}
			return true;
		}
		return false;
	}
	
	public boolean isAlive(){
		return this.isAlive;
	}
	
	public List<Team> getTeams(){
		return teams;
	}
	
	public String getPseudo(){
		return pseudo;
	}

	public Role getRole() {
		return role;
	}

	public CauseOfDeath getCauseOfDeath() {
		return causeOfDeath;
	}

	public boolean canVote() {
		return canVote;
	}
}
