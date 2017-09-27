package fr.satanche.titanche.command.core;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import fr.satanche.titanche.MainApp;
import fr.satanche.titanche.command.basic.CommandBotManaging;
import fr.satanche.titanche.command.core.Command.ExecutorType;
import fr.satanche.titanche.command.roleplay.CommandRolePlayCreation;
import fr.satanche.titanche.command.roleplay.CommandRolePlayManaging;
import fr.satanche.titanche.command.roleplay.CommandRolePlayPlaying;
import fr.satanche.titanche.command.werewolf.CommandWerewolfCreatorBeforeGame;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class CommandFactory {

	private final Map<String, SimpleCommand> commands = new HashMap<String, SimpleCommand>();
	private final String tag = "";
	private MainApp mainRef;
	
	public CommandFactory(MainApp mainRef, Properties propWerewolf) {
		this.mainRef = mainRef;
		registerCommand(new CommandBotManaging(mainRef));
		registerCommands(new CommandRolePlayCreation(), new CommandRolePlayManaging(), new CommandRolePlayPlaying());
		registerCommand(new CommandWerewolfCreatorBeforeGame(propWerewolf));
    }
   
    public String getTag() {
        return tag;
    }
   
    public Collection<SimpleCommand> getCommands(){
        return commands.values();
    }
   
    public void registerCommands(Object...objects){
        for(Object object : objects) registerCommand(object);
    }
   
    public void registerCommand(Object object){
        for(Method method : object.getClass().getDeclaredMethods()){
            if(method.isAnnotationPresent(Command.class)){
                Command command = method.getAnnotation(Command.class);
                method.setAccessible(true);
                SimpleCommand simpleCommand = new SimpleCommand(command.name(), command.description(), command.type(), object, method);
                if(commands.containsKey(command.name()))
                	throw new Error("[Warning] [Titanche] : Another command exist with the same name than "+command.name());
                commands.put(command.name(), simpleCommand);
            }
        }
    }
   
    public void commandConsole(String command){
        Object[] object = getCommand(command);
        if(object[0] == null || ((SimpleCommand)object[0]).getExecutorType() == ExecutorType.USER){
            System.out.println("Commande inconnue.");
            return;
        }
        try{
            execute(((SimpleCommand)object[0]), command, (String[])object[1], null);
        }catch(Exception exception){
            System.out.println("La methode "+((SimpleCommand)object[0]).getMethod().getName()+" n'est pas correctement initialisé.");
            System.out.println(exception.toString());
        }
    }
   
    public boolean commandUser(User user, String command, Message message){
        Object[] object = getCommand(command);
        if(object[0] == null || ((SimpleCommand)object[0]).getExecutorType() == ExecutorType.CONSOLE) return false;
        try{
            execute(((SimpleCommand)object[0]), command,(String[])object[1], message);
        }catch(Exception exception){
            System.out.println("La methode "+((SimpleCommand)object[0]).getMethod().getName()+" n'est pas correctement initialisé.");
           exception.printStackTrace();
        }
        return true;
    }
   
    private Object[] getCommand(String command){
    	for(String currKey : commands.keySet()){
    		if(command.startsWith(currKey)){
    			SimpleCommand simpleCommand = commands.get(currKey);
    			String[] args = command.replaceFirst(currKey, "").split(" ");
    			return new Object[]{simpleCommand, args};
       		}  		    		
    	}
    	return new Object[]{null,command.split(" ")};
    }
   
    private void execute(SimpleCommand simpleCommand, String command, String[] args, Message message) throws Exception{
        Parameter[] parameters = simpleCommand.getMethod().getParameters();
        Object[] objects = new Object[parameters.length];
        for(int i = 0; i < parameters.length; i++){
            if(parameters[i].getType() == String[].class) objects[i] = args;
            else if(parameters[i].getType() == User.class) objects[i] = message == null ? null : message.getAuthor();
            else if(parameters[i].getType() == TextChannel.class) objects[i] = message == null ? null : message.getTextChannel();
            else if(parameters[i].getType() == PrivateChannel.class) objects[i] = message == null ? null : message.getPrivateChannel();
            else if(parameters[i].getType() == Guild.class) objects[i] = message == null ? null : message.getGuild();
            else if(parameters[i].getType() == String.class) objects[i] = command;
            else if(parameters[i].getType() == Message.class) objects[i] = message;
            else if(parameters[i].getType() == JDA.class) objects[i] = this.mainRef.getJDA();
            else if(parameters[i].getType() == MessageChannel.class) objects[i] = message.getChannel();
        }
        simpleCommand.getMethod().invoke(simpleCommand.getObject(), objects);
    }
	
}
