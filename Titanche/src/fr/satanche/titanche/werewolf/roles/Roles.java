package fr.satanche.titanche.werewolf.roles;

import java.util.ArrayList;
import java.util.List;

import fr.satanche.titanche.werewolf.Version;
import fr.satanche.titanche.werewolf.util.Team;

public enum Roles {

	/* ***************************** */
	/*						    	 */
	/*   First generation of cards   */
	/*							     */
	/* ***************************** */
	VILLAGER(new Team[]{Team.VILLAGER}, new Version[]{Version.CLASSIC}),
	LITTLE_GIRL(new Team[]{Team.VILLAGER}, new Version[]{Version.CLASSIC}),
	HUNTER(new Team[]{Team.VILLAGER}, new Version[]{Version.CLASSIC}),
	SEER(new Team[]{Team.VILLAGER}, new Version[]{Version.CLASSIC}),
	CUPID(new Team[]{Team.VILLAGER}, new Version[]{Version.CLASSIC}),
	THIEF(new Team[]{Team.VILLAGER}, new Version[]{Version.CLASSIC}),
	WITCH(new Team[]{Team.VILLAGER}, new Version[]{Version.CLASSIC}),
	WEREWOLF(new Team[]{Team.WEREWOLF}, new Version[]{Version.CLASSIC});
	
	private List<Version> versions;
	private List<Team> startingTeams;
	
	Roles(Team[] teams, Version[] versions){
		startingTeams = new ArrayList<Team>();
		this.versions = new ArrayList<Version>();
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
}
