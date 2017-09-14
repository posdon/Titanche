package fr.satanche.titanche.roleplay.util;

import java.util.List;

public class Action {

	private String beforeText;
	private String successText;
	private String failText;
	private Carac testCarac;
	private Carac successCarac;
	private Carac failCarac;
	private List<State> requiredStates;
	private List<State> forbiddenStates;
	private List<State> successStates;
	private List<State> failStates;
	
	public Action(String beforeText, String successText, String failText, Carac testCarac, Carac successCarac,
			Carac failCarac, List<State> requiredStates, List<State> forbiddenStates, List<State> successStates,
			List<State> failStates) {
		this.beforeText = beforeText;
		this.successText = successText;
		this.failText = failText;
		this.testCarac = testCarac;
		this.successCarac = successCarac;
		this.failCarac = failCarac;
		this.requiredStates = requiredStates;
		this.forbiddenStates = forbiddenStates;
		this.successStates = successStates;
		this.failStates = failStates;
	}

	
	public String getBeforeText() {
		return beforeText;
	}
	
	public String getResultText(boolean success) {
		return (success)?successText:failText;
	}
	
	public boolean test(Carac carac) {
		return carac.sup(testCarac);
	}
	
	public Carac getResultCarac(boolean success) {
		return (success)?successCarac:failCarac;
	}
	
	public boolean isEligible(List<State> states) {
		for(State currState : states) {
			if(forbiddenStates.contains(currState)) return false;
		}
		for(State requiredState : requiredStates) {
			if(!states.contains(requiredState)) return false;
		}
		return true;
	}
	
	public List<State> getResultStates(boolean success) {
		return (success)?successStates:failStates;
	}
}
