package fr.satanche.titanche.werewolf.util;

public enum Version {
	CLASSIC;
	
	public static Version getVersion(String name){
		if(name.equals("CLASSIC")){
			return Version.CLASSIC;
		}else{
			return null;
		}
	}
}
