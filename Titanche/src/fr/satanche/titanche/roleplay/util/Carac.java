package fr.satanche.titanche.roleplay.util;

import java.util.HashMap;
import java.util.Map;


public class Carac {

	private Map<String, Integer> carac;
	
	public Carac(Map<String, Integer> carac) {
		this.carac = carac;
	}

	/**
	 * Print each carac :
	 * 		"name: value\n"
	 */
	public String toString() {
		String result = "";
		for(String it : this.carac.keySet()) result += it+": "+this.carac.get(it)+"\n";
		return result;
	}
	
	/**
	 * Add the given carac to this one. Return false if a given carac isn't present
	 * @param carac
	 * @return
	 */
	public boolean add(Carac carac) {
		if(this.carac.size() != carac.carac.size()) return false;
		Map<String, Integer> tempMap = new HashMap<String, Integer>();
		for(String key : this.carac.keySet()) {
			if(!carac.carac.containsKey(key)) return false;
			tempMap.put(key, carac.carac.get(key)+this.carac.get(key));
		}
		this.carac = tempMap;
		return true;
	}
	
	/**
	 * Return if this is sup or equals than the given carac 
	 * @param carac
	 * @return
	 */
	public boolean sup(Carac carac) {
		if(this.carac.size() != carac.carac.size()) return false;
		for(String key : this.carac.keySet()) {
			if(!carac.carac.containsKey(key)) return false;
			if(carac.carac.get(key) > this.carac.get(key)) return false;
		}
		return true;
	}
	
	public int get(String name) {
		return carac.get(name);
	}
}
