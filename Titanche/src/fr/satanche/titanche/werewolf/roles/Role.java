package fr.satanche.titanche.werewolf.roles;

import java.util.ArrayList;
import java.util.List;

import fr.satanche.titanche.werewolf.util.Team;
import fr.satanche.titanche.werewolf.util.Version;

public enum Role {

	/* ***************************** */
	/*						    	 */
	/*   First generation of cards   */
	/*							     */
	/* ***************************** */
	VILLAGER(new Team[]{Team.VILLAGER}, new Version[]{Version.CLASSIC},false),
	LITTLE_GIRL(new Team[]{Team.VILLAGER}, new Version[]{Version.CLASSIC},false),
	HUNTER(new Team[]{Team.VILLAGER}, new Version[]{Version.CLASSIC},false),
	SEER(new Team[]{Team.VILLAGER}, new Version[]{Version.CLASSIC},true),
	CUPID(new Team[]{Team.VILLAGER}, new Version[]{Version.CLASSIC},true),
	THIEF(new Team[]{Team.VILLAGER}, new Version[]{Version.CLASSIC},true),
	WITCH(new Team[]{Team.VILLAGER}, new Version[]{Version.CLASSIC},true),
	WEREWOLF(new Team[]{Team.WEREWOLF}, new Version[]{Version.CLASSIC},false);
	
	private List<Version> versions;
	private List<Team> startingTeams;
	private boolean unic;
	
	Role(Team[] teams, Version[] versions, boolean unic){
		startingTeams = new ArrayList<Team>();
		this.versions = new ArrayList<Version>();
		this.unic = unic;
		for(Version version : versions)
			this.versions.add(version);
		for(Team team : teams)
			if(this.versions.containsAll(team.getVersions()))
				startingTeams.add(team);
	}
	
	public List<Team> getStartingTeams(){
		return startingTeams;
	}
	
	public List<Version> getAvailableVersion(){
		return versions;
	}
	
	public boolean isUnic(){
		return unic;
	}
}
