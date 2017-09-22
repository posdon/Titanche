package fr.satanche.titanche.werewolf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import fr.satanche.titanche.werewolf.roles.Role;
import fr.satanche.titanche.werewolf.util.Team;
import fr.satanche.titanche.werewolf.util.Version;

public class Game {

	private List<Player> players;
	private List<Role> roles;
	private List<Team> winningTeam;
	private boolean canVote;
	private Map<Player,Player> voteMap;
	private boolean running;
	
	public Game(Version version, List<Player> players, List<Role> roles){
		this.players = players;
		this.roles = roles;
		this.running = false;
		this.winningTeam = new ArrayList<Team>();
		this.voteMap = new HashMap<Player,Player>();
		this.canVote = false;
	}
	
	/**
	 * Give the cards to players
	 * @return if there is the good amount of cards for the number of player or if the game already started.
	 */
	public boolean start(){
		if(!running){
			Random rdm = new Random();
			int nbCard = (this.roles.contains(Role.THIEF)) ? this.players.size()+2 : this.players.size();
			if(this.roles.size() == nbCard){
				this.running = true;
				List<Role> temp = new ArrayList<Role>(this.roles);
				for(Player player : players) player.setRole(temp.remove(rdm.nextInt(temp.size())));
				return true;
			}		
		}
		return false;
	}
	
	/**
	 * Set the value to winnerTeam if there is a winner
	 * @return true if there is a winner
	 */
	public boolean verifIfWinner(){
		List<Team> currentTeams = new ArrayList<Team>();
		boolean verifNoSurvivor = true;
		for(Player player : this.players){
			if(player.isAlive()){
				if(currentTeams.isEmpty()){
					currentTeams.addAll(player.getTeams());
					verifNoSurvivor = false;
				}else{
					for(Team currTeam : currentTeams){
						if(player.getTeams().contains(currTeam)){
							currentTeams.remove(currTeam);
						}
					}
				}
			}
		}
		if(verifNoSurvivor){
			winningTeam.add(Team.EQUALITY);
			return true;
		}else if(!currentTeams.isEmpty()){
			winningTeam.addAll(currentTeams);
			return true;
		}
		return false;
	}
	
	
	/**
	 * Set the vote of a player
	 * @param voter : Player who will vote
	 * @param votee : Player getting voted
	 * @return true if player can vote.
	 */
	public boolean setVote(Player voter, Player votee){
		if(canVote && voter.canVote()){
			voteMap.put(voter, votee);
			return true;
		}
		return false;
	}
	
	/**
	 * Able player to vote.
	 */
	public void startVote(){
		canVote = true;
	}
	
	/**
	 * Disable player to vote.
	 * @return the number of vote for each Player
	 */
	public Map<Player,Integer> getVoteResult(){
		Map<Player,Integer> result = new HashMap<Player,Integer>();
		for(Player player : this.players){
			result.put(player,0);
		}
		for(Player player : voteMap.keySet()){
			Player votee = voteMap.get(player);
			result.put(votee, result.get(votee)+1);
		}
		voteMap.clear();
		return result;
	}
}
