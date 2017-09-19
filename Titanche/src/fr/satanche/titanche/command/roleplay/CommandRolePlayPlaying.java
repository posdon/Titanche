package fr.satanche.titanche.command.roleplay;

import fr.satanche.titanche.command.core.Command;
import fr.satanche.titanche.command.core.Command.ExecutorType;

public class CommandRolePlayPlaying {

	public CommandRolePlayPlaying(){
		
	}

	@Command(name="roleplay join", type=ExecutorType.USER)
	public void join(){
		System.out.println("roleplay join command");
	}
	
	@Command(name="roleplay move to", type=ExecutorType.USER)
	public void moveTo(){
		
	}
	
	@Command(name="roleplay choose", type=ExecutorType.USER)
	public void choose(){
		System.out.println("roleplay choose");
	}
	
}
