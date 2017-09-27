package fr.satanche.titanche;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

import javax.security.auth.login.LoginException;

import fr.satanche.titanche.botListener.BotListener;
import fr.satanche.titanche.command.core.CommandFactory;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

public class MainApp implements Runnable {

	private JDA jda;
	private CommandFactory commandFactory;
	private boolean running;
	private Scanner scanner = new Scanner(System.in);
	private final String path_werewolfProperties = "./src/fr/satanche/titanche/properties/werewolf/werewolf.properties";
	
	
	public MainApp(String token) throws LoginException, IllegalArgumentException, RateLimitedException, InterruptedException, FileNotFoundException, IOException{
		
		Properties propWerewolf = new Properties();
		propWerewolf.load(new FileInputStream(path_werewolfProperties));
		commandFactory = new CommandFactory(this,propWerewolf);
		
		jda = new JDABuilder(AccountType.BOT).setToken(token).buildBlocking();
		jda.addEventListener(new BotListener(commandFactory));
		System.out.println("Bot connected.");
	}
	
	public JDA getJDA(){
		return jda;
	}
	
	public void setRunning(boolean running){
		this.running = running;
	}
	
	@Override
	public void run(){
		running = true;
		while(running){
			if(scanner.hasNextLine()) commandFactory.commandConsole(scanner.nextLine());
		}
		scanner.close();
		System.out.println("[Info] [Titanche] Bot is going to shutdown.");
		jda.shutdown();
		System.out.println("[Info] [Titanche] Bot disconnected.");
		System.exit(0);
	}
	
	public static void main(String[] args) {
		String tokenBot = "MzUwMzk3NDA5NzUyMjUyNDE3.DKFG0A.ORO4dE2fflF2e_yPrCDnbmN6sd4";
		try {
			MainApp main = new MainApp(tokenBot);
			new Thread(main, "bot").start();
		} catch (LoginException | IllegalArgumentException | RateLimitedException | InterruptedException | IOException e) {
			e.printStackTrace();
		}

	}

}
