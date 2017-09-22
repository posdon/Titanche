package fr.satanche.titanche.werewolf.util;

import java.util.ArrayList;
import java.util.List;

public enum CauseOfDeath {

	/* ***************************** */
	/*						    	 */
	/*   First generation of cards   */
	/*							     */
	/* ***************************** */
	NOT_DEAD(new Version[]{Version.CLASSIC}),
	VILLAGERS(new Version[]{Version.CLASSIC}),
	WITCH(new Version[]{Version.CLASSIC}),
	WEREWOLF(new Version[]{Version.CLASSIC}),
	HUNTER(new Version[]{Version.CLASSIC}),
	LOVER(new Version[]{Version.CLASSIC});
	
	CauseOfDeath(Version... versions){
		this.versions = new ArrayList<Version>();
		for(Version version : versions)
			this.versions.add(version);
	}
	
	private List<Version> versions;
	
	public List<Version> getVersions(){
		return versions;
	}
}
