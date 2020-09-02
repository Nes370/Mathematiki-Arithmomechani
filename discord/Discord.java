package discord;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.channel.PrivateChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.entity.user.UserStatus;

public class Discord {

	private static DiscordApi api;
	private static String resourcePath = "C:/Users/Rie_f/eclipse-workspace/Euler/src/resources/";
	final static long bootStamp = System.currentTimeMillis();
	private static Timer timer;
	private static TextChannel logChannel;
	final public static DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	public static void main(String[] args) {
		
		if(args.length > 0) {
			resourcePath = args[0];
		}
		
		if(api != null)
			api.disconnect();
		api = new DiscordApiBuilder()
				.setToken(readText(resourcePath + "token.txt"))
				.login()
				.join();
		
		logChannel = api.getTextChannelById(731252481266286733L).get();
		
		log("Logged in at " + DF.format(Date.from(Instant.now())) + ".");
		
		api.updateActivity(ActivityType.LISTENING, "your every command");
		api.updateStatus(UserStatus.ONLINE);
		
		api.addMessageCreateListener(new Commands());
		
		timer = new Timer();
		//schedule tasks from file
	}
	
	/**
	 * Logs message in the console and the log channel.
	 * @param message
	 */
	public static CompletableFuture<Message> log(String message) {		
		System.out.println(message);
		return logChannel.sendMessage(new EmbedBuilder().setDescription(message).setColor(Color.GREEN));
	}
	
	/**
	 * Logs command usage in the console and the log channel.
	 * 
	 * @param message
	 * @param logEmbed
	 * @return
	 */
	public static CompletableFuture<Message> commandLog(String message, EmbedBuilder logEmbed) {
		System.out.println(message);
		return logChannel.sendMessage(logEmbed);
	}
	
	public static void errorLog(String exception, String error, String cause, String stackTrace, EmbedBuilder logEmbed, CompletableFuture<Message> sentLogMessage) {
		System.out.println(error);
		try {
			sentLogMessage.get().edit(logEmbed.addField("Stack Trace", "```Java\n" + stackTrace + "```")
				.addField("Error", exception + "\n" + error + ((cause == null) ? "" : "\n" + cause))
				.setColor(Color.RED));
		} catch (Exception e) {
			System.out.println("Failed to update log embed.");
			e.printStackTrace();
		}
	}

	/**
	 * Reads the first line from the specified file and returns it as a String.
	 * 
	 * @param filepath	The path to the file to be read
	 * @return			A String of the first line of the specified file.
	 */
	public static String readText(String filepath) {
		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), StandardCharsets.UTF_8));
			String content = in.readLine();
			in.close();
			return content;
		} catch(IOException e) {
			System.out.println("An error occured when attempting to read from " + filepath);
			e.printStackTrace();
		}
		return "";
	}
	
	public static String getTimeString() {
		long time = System.currentTimeMillis() - bootStamp;
		long days = time / 86400000;
		long hours = (time %= 86400000) / 3600000;
		long mins = (time %= 3600000) / 60000;
		long secs = (time %= 60000) / 1000;
		StringBuilder s = new StringBuilder();
		if(days > 0)
			s.append(days + "d ");
		if(hours > 0)
			s.append(hours + "h ");
		if(mins > 0)
			s.append(mins + "m ");
		if(secs > 0)
			s.append(secs + "s ");
		if(time % 1000 > 0)
			s.append(time % 1000 + "ms ");
		return s.toString();
	}
	
	public static DiscordApi getAPI() {
		return api;
	}
	
	public static EmbedBuilder scheduleReminder(long destination, String message, long executionStamp, long userID,
			long creationChannelID, long creationStamp) {
		
		TimerTask task = new TimerTask() {			
			@Override
			public void run() {
				if(destination == 0) 
					try {
						remindUser();
					} catch (Exception e) {
						//error message
						//redirect to creationChannel
					}
				else if(destination == 1)
					try {
						remindChannel(creationChannelID);
					} catch (Exception e) {
						//error message
						//redirect to user
					}
				else
					try {
						api.getTextChannelById(destination).get().sendMessage(message);
					} catch (Exception e) {
						//error message
						//redirect to user
					}
			}
			
			public void remindUser() throws InterruptedException, ExecutionException {
				User user = api.getUserById(userID).get();
				PrivateChannel channel = user.openPrivateChannel().get();
				channel.sendMessage(message);
			}
			
			public void remindChannel(long channelID) {
				TextChannel channel = api.getTextChannelById(channelID).get();
				channel.sendMessage("<@" + userID + "> " + message);
			}
		};
		
		timer.schedule(task, Date.from(Instant.ofEpochMilli(executionStamp)));
		
		return new EmbedBuilder().setDescription("Reminder scheduled to send in " + formatTime(executionStamp - System.currentTimeMillis()));
	}

	private static String formatTime(long l) {
		
		DecimalFormat df = new DecimalFormat("00");
		
		String result = "";
		if(l > 86_400_000) {
			result += (l / 86_400_000) + "d ";
			l %= 86_400_000;
		}
		result += df.format(l / 3_600_000) + ":";
		l %= 3_600_000;
		result += df.format(l / 60_000) + ":";
		l %= 60_000;
		result += df.format(l / 1000);
		return result;
	}
	
}
