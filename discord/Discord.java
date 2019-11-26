package discord;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.user.UserStatus;

public class Discord {

	private static DiscordApi api;
	private static String resourcePath = "C:/Users/Rie_f/eclipse-workspace/Euler/src/resources/";
	final static long bootStamp = System.currentTimeMillis();
	
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
		
		System.out.println("Logged in.");
				
		api.updateActivity(ActivityType.LISTENING, "your every command");
		api.updateStatus(UserStatus.IDLE);
		
		api.addMessageCreateListener(new Commands());
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
			s.append(days + " d ");
		if(hours > 0)
			s.append(hours + " h ");
		if(mins > 0)
			s.append(mins + " m ");
		if(secs > 0)
			s.append(secs + " s ");
		if(time % 1000 > 0)
			s.append(time % 1000 + " ms ");
		return s.toString();
	}
	
}
