package fr.satanche.titanche.werewolf.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import fr.satanche.titanche.werewolf.player.Player;
import fr.satanche.titanche.werewolf.roles.Role;
import fr.satanche.titanche.werewolf.util.CauseOfDeath;
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
	
	public boolean hasPlayer(Player player){
		return players.contains(player);
	}
	
	/**
	 * Give the cards to players
	 * @return if there is the good amount of cards for the number of player or if the game already started.
	 */
	public boolean startGame(){
		if(!running){
			int nbCard = (this.roles.contains(Role.THIEF)) ? this.players.size()+2 : this.players.size();
			if(this.roles.size() == nbCard){
				this.running = true;
				Random rdm = new Random();
				List<Role> temp = new ArrayList<Role>(this.roles);
				for(Player player : this.players) player.setRole(temp.remove(rdm.nextInt(temp.size())));
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
	
	/* **********************************************
	 *					Role functions				*
	 ********************************************** */
	
	/**
	 * Allow the player with role to vote
	 * @param role
	 */
	public void startVoteFor(Role role){
		for(Player player : this.players)
			if(player.isAlive() && player.getRole() == role)
				player.setCanVote(true);				
	}
	
	public void stopVoteForAll(){
		for(Player player : this.players) player.setCanVote(false);
	}
	
	/**
	 * Set the lovers from cupid
	 * @param voter : cupid
	 * @param firstLover
	 * @param secondLover
	 * @return
	 */
	public boolean setCupidVote(Player voter, Player firstLover, Player secondLover){
		if(voter.canVote()){
			if(firstLover.isAlive() && secondLover.isAlive() && firstLover != secondLover){
				firstLover.addTeam(Team.LOVERS);
				secondLover.addTeam(Team.LOVERS);
				voter.setCanVote(false);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Make the survivor survive thanks to the witch magical potion.
	 * @param voter
	 * @param survivor
	 * @return
	 */
	public boolean setWitchVoteCure(Player voter, Player survivor){
		if(voter.canVote() && survivor.isAlive()){
			survivor.setCauseOfDeath(CauseOfDeath.NOT_DEAD);
			voter.setCanVote(false);
			return true;
		}
		return false;
	}
	
	/**
	 * Make the condamned succomb from the witch magical poison.
	 * @param voter
	 * @param condamned
	 * @return
	 */
	public boolean setWitchVotePoison(Player voter, Player condamned){
		if(voter.canVote() && condamned.isAlive()){
			voter.setCanVote(false);
			condamned.setCauseOfDeath(CauseOfDeath.WITCH);
			return true;
		}
		return false;
	}
	
	/**
	 * Make the voter see the role of the votee.
	 * @param voter
	 * @param votee
	 * @return
	 */
	public Role seerVote(Player voter, Player votee){
		if(voter.canVote()){
			if(votee.isAlive()){
				voter.setCanVote(false);
				return votee.getRole();
			}
		}
		return null;
	}
	
	/**
	 * Make the thief choose one of the remaining roles.
	 * @param voter
	 * @param role
	 * @return
	 */
	public boolean thiefVote(Player voter, Role role){
		if(voter.canVote()){
			voter.setCanVote(false);
			voter.setRole(role);
			return true;
		}
		return false;
	}
	
	/**
	 * Make the hunter instant-kill the condamned
	 * @param voter
	 * @param condamned
	 * @return
	 */
	public boolean hunterVote(Player voter, Player condamned){
		if(voter.canVote()){
			voter.setCanVote(false);
			return condamned.kill(CauseOfDeath.HUNTER);
		}
		return false;
	}
	
	/* **********************************************
	 *				All Vote functions				*
	 ********************************************** */
	
	/**
 	 * Set the vote of a player
	 * @param voter : Player who will vote
	 * @param votee : Player getting voted
	 * @return true if player can vote.
	 */
 	public boolean setGroupVote(Player voter, Player votee){
		if(canVote && voter.canVote()){
			voteMap.put(voter, votee);
			return true;
		}
		return false;
	}
	
	/**
	 * Able player to vote.
	 */
	public void startVillagerVote(){
		canVote = true;
		for(Player player : this.players){
			if(player.isAlive()){
				player.setCanVote(true);
			}
		}
	}
	
	/**
	 * Able werewolf player to vote.
	 */
	public void startWerewolfVote(){
		canVote = true;
		for(Player player : this.players){
			if(player.isAlive() && player.getTeams().contains(Team.WEREWOLF)){
				player.setCanVote(true);
			}
		}
	}
	
	/**
	 * Disable player to vote.
	 * @return the number of vote for each Player
	 */
	public Map<Player,Integer> getGroupVoteResult(){
		canVote = false;
		Map<Player,Integer> result = new HashMap<Player,Integer>();
		for(Player player : this.players){
			result.put(player,0);
			player.setCanVote(false);
		}
		for(Player player : voteMap.keySet()){
			Player votee = voteMap.get(player);
			result.put(votee, result.get(votee)+1);
		}
		voteMap.clear();
		return result;
	}
}
