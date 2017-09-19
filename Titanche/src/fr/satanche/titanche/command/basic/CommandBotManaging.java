package fr.satanche.titanche.command.basic;

import fr.satanche.titanche.MainApp;
import fr.satanche.titanche.command.core.Command;
import fr.satanche.titanche.command.core.Command.ExecutorType;
import net.dv8tion.jda.core.entities.MessageChannel;

public class CommandBotManaging {

	private MainApp mainRef;
	
	public CommandBotManaging(MainApp mainRef){
		this.mainRef = mainRef;
	}
	
	@Command(name="stop", type=ExecutorType.CONSOLE)
	private void stop(){
		mainRef.setRunning(false);
	}
	
	@Command(name="help", type=ExecutorType.USER)
	private void help(MessageChannel channel){
		channel.sendMessage("This is an info.").queue();
	}
}
