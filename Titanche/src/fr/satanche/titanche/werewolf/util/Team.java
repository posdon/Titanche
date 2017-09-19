package fr.satanche.titanche.werewolf.util;

import java.util.ArrayList;
import java.util.List;

import fr.satanche.titanche.werewolf.Version;

public enum Team {
	VILLAGER(Version.CLASSIC), 
	WEREWOLF(Version.CLASSIC), 
	LOVERS(Version.CLASSIC);
	
	Team(Version... versions){
		this.versions = new ArrayList<Version>();
		for(Version version : versions)
			this.versions.add(version);
	}
	
	private List<Version> versions;
	
	public List<Version> getVersions(){
		return versions;
	}
}
