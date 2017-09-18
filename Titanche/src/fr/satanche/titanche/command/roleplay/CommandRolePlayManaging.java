package fr.satanche.titanche.command.roleplay;

import fr.satanche.titanche.command.core.Command;
import fr.satanche.titanche.command.core.Command.ExecutorType;
import net.dv8tion.jda.core.entities.MessageChannel;

public class CommandRolePlayManaging {

	public CommandRolePlayManaging() {
		
	}
	
	@Command(name="roleplay game create", type=ExecutorType.USER)
	public void createRolePlay(MessageChannel channel){
		System.out.println("roleplay game create");
	}
	
	@Command(name="roleplay game save", type=ExecutorType.USER)
	public void saveRolePlay(){
		System.out.println("roleplay game save");
	}
	
	@Command(name="roleplay game start", type=ExecutorType.USER)
	public void startRolePlay(){
		System.out.println("roleplay game start");
	}
	
	@Command(name="roleplay game join", type=ExecutorType.USER)
	public void joinRolePlay(){
		System.out.println("roleplay game join");
	}
	
	@Command(name="roleplay game leave", type=ExecutorType.USER)
	public void leaveRolePlay(){
		System.out.println("roleplay game leave");
	}
	
	@Command(name="roleplay game delete", type=ExecutorType.USER)
	public void deleteRolePlay(){
		
	}
}
