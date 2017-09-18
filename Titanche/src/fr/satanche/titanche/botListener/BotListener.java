package fr.satanche.titanche.botListener;


import fr.satanche.titanche.command.core.CommandFactory;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;

public class BotListener implements EventListener {

	private final CommandFactory commandFactory;
	
	public BotListener(CommandFactory commandFactory){
		this.commandFactory = commandFactory;
	}
	
	@Override
	public void onEvent(Event event){
		if(event instanceof MessageReceivedEvent) onMessage((MessageReceivedEvent) event);
	}
	
	public void onMessage(MessageReceivedEvent event){
		if(event.getAuthor().equals(event.getJDA().getSelfUser())) return;
		String message = event.getMessage().getContent();
		if(message.startsWith(commandFactory.getTag())){
			message = message.replaceFirst(commandFactory.getTag(), "");
			commandFactory.commandUser(event.getAuthor(), message, event.getMessage());
		}
		
	}
}
