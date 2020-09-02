package discord;

import ch.obermuhlner.math.big.BigDecimalMath;
import ch.obermuhlner.math.big.DefaultBigDecimalMath;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.UserStatus;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import algorithms.Algorithms;
import algorithms.Conversion;

public class Commands implements MessageCreateListener {
	
	final String PREFIX = "c.";
	
	final EmbedBuilder NoSuchCommandError = new EmbedBuilder()
			.setAuthor("Invalid Command")
			.setDescription("Your command was not recognized.\nUse `c.help` for a list of commands.");
	final Color BLUE = new Color(0x00b0f4);
	final BigDecimal GAMMA = BigDecimalMath.toBigDecimal("0.577215664901532860606512090082402431"+
			"0421593359399235988057672348848677267776646709369470632917467495146314472498070"+
			"8248096050401448654283622417399764492353625350033374293733773767394279259525824"+
			"7094916008735203948165670853233151776611528621199501507984793745085705740029921"+
			"3547861466940296043254215190587755352673313992540129674205137541395491116851028"+
			"0798423487758720503843109399736137255306088933126760017247953783675927135157722"+
			"6102734929139407984301034177717780881549570661075010161916633401522789358679654"+
			"9725203621287922655595366962817638879272680132431010476505963703947394957638906"+
			"5729679296010090151251959509222435014093498712282479497471956469763185066761290"+
			"6381105182419744486783638086174945516989279230187739107294578155431600500218284"+
			"4096053772434203285478367015177394398700302370339518328690001558193988042707411"+
			"5422278197165230110735658339673487176504919418123000406546931429992977795693031"+
			"0050308630341856980323108369164002589297089098548682577736428825395492587362959"+
			"6133298574739302373438847070370284412920166417850248733379080562754998434590761"+
			"6431671031467107223700218107450444186647591348036690255324586254422253451813879"+
			"1243457350136129778227828814894590986384600629316947188714958752549236649352047"+
			"3243641097268276160877595088095126208404544477992299157248292516251278427659657"+
			"0832146102982146179519579590959227042089896279712553632179488737642106606070659"+
			"8256199010288075612519913751167821764361905705844078357350158005607745793421314"+
			"49885007864151716151945");
	final BigDecimal LOG2 = BigDecimalMath.toBigDecimal("0.693147180559945309417232121458176568075"+
			"50013436025525412068000949339362196969471560586332699641868754200148102057068573"+
			"368552023575813055703267075163507596193072757082837143519030703862389167347112335"+
			"011536449795523912047517268157493206515552473413952588295045300709532636664265410"+
			"423915781495204374043038550080194417064167151864471283996817178454695702627163106"+
			"454615025720740248163777338963855069526066834113727387372292895649354702576265209"+
			"885969320196505855476470330679365443254763274495125040606943814710468994650622016"+
			"772042452452961268794654619316517468139267250410380254625965686914419287160829380"+
			"317271436778265487756648508567407764845146443994046142260319309673540257444607030"+
			"809608504748663852313818167675143866747664789088143714198549423151997354880375165"+
			"861275352916610007105355824987941472950929311389715599820565439287170007218085761"+
			"025236889213244971389320378439353088774825970171559107088236836275898425891853530"+
			"243634214367061189236789192372314672321720534016492568727477823445353476481149418"+
			"642386776774406069562657379600867076257199184734022651462837904883062033061144630"+
			"073719489002743643965002580936519443041191150608094879306786515887090060520346842"+
			"973619384128965255653968602219412292420757432175748909770675268711581705113700915"+
			"894266547859596489065305846025866838294002283300538207400567705304678700184162404"+
			"418833232798386349001563121889560650553151272199398332030751408426091479001265168"+
			"243443893572472788205486271552741877243002489794540196187233980860831664811490930"+
			"667519339312890431641370681397776498176974868903887789991296503619270710889264105"+
			"230924783917373501229842420499568935992206602204654941510613");
	final BigDecimal PHI = BigDecimal.ONE.add(DefaultBigDecimalMath.sqrt(BigDecimal.valueOf(5))).divide(new BigDecimal(2));
	final BigDecimal DELTA = BigDecimalMath.toBigDecimal("4.66920160910299067185320382046620161725" + 
			"818557747576863274565134300413433021131473713868974402394801381716");
	final BigDecimal ALPHA = BigDecimalMath.toBigDecimal("2.50290787509589282228390287321821578638" + 
			"127137672714997733619205677923546317959020670329964974643383412959");
	final BigDecimal AVOGADRO = BigDecimal.valueOf(6.022_140_76e23);
	final BigDecimal PLANCK = BigDecimal.valueOf(6.626_070_15e-34);
	BigDecimal X = BigDecimal.ZERO;
	BigDecimal Y = BigDecimal.ZERO;
	BigDecimal Z = BigDecimal.ZERO;

	@Override
	public void onMessageCreate(MessageCreateEvent e) {
		
		if(e.getMessageAuthor().isBotOwner()) {
			if(e.getMessageContent().equals("c.restart")) {
				Discord.log("Restarted at " + Discord.DF.format(Date.from(Instant.now())));
				Discord.main(null);
			} else if(e.getMessageContent().equals("c.shutdown")) {
				Discord.log("Shutdown at " + Discord.DF.format(Date.from(Instant.now())));
				System.exit(0);
			}
		}
		
		if(!e.getMessageAuthor().isBotUser() && e.getMessageContent().toLowerCase().startsWith(PREFIX)) {
			
			Discord.getAPI().updateStatus(UserStatus.DO_NOT_DISTURB);
			
			String message = Discord.DF.format((Calendar.getInstance().getTime())) + ": " + e.getMessageAuthor() + " used " + e.getMessageContent() + " in " + e.getServer() + ", " + e.getChannel();
			EmbedBuilder logEmbed = new EmbedBuilder()
					.setDescription("```" + message + "```")
					.addInlineField("User", "<@" + e.getMessageAuthor().getIdAsString() + ">")
					.addInlineField("Channel", "<#" + e.getChannel().getIdAsString() + ">")
					.addField("Command", e.getMessageContent())
					.setColor(BLUE);
					
			CompletableFuture<Message> sentLogMessage = Discord.commandLog(message, logEmbed);
			
			String[] content = e.getMessageContent().substring(2).split(" +");
			EmbedBuilder embed = NoSuchCommandError;
			long time = -1;
			
			if(content.length > 0) switch (content[0]) {
			
				case "info":
					embed = new EmbedBuilder().setAuthor("Information")
						.setTitle("Μαθηματική Αριθμομηχανή")
						.setDescription("A Discord bot for mathematics calculations.")
						.addField("About", "Μαθηματική Αριθμομηχανή (Mathēmatikí Arithmomēchaní) is a Discord bot application developed by Nes370.\nShe is written in Java and runs on Java 8.\nShe uses:\n • [BtoBastian's Javacord library](https://github.com/BtoBastian/Javacord/tree/v_3) v3.0.4\n • [Eobermuhlner's Big Math library](https://github.com/eobermuhlner/big-math) v2.3.0.\nYou can read her source code on [GitHub](https://github.com/Nes370/Mathematiki-Arithmomechani)")
						.addField("Support Development", "\nIf you'd like to support the developer, consider making a one-time donation via [PayPal](https://paypal.me/nes370) or become a patron at [Patreon](https://www.patreon.com/nes370).")
						.addField("Contact", "If you wish to add this bot to your server, please contact <@237676240931258378> (Nes#9856) on Discord.")
						.addField("RunTime", Discord.getTimeString());
					 break;
					 
				case "help":
					embed = new EmbedBuilder().setAuthor("Help")
						.addInlineField("Info", "View information about this bot.")
						.addInlineField("Help", "View this list of commands.")
						.addInlineField("Functions", "View the list of functions, operators, constants and variables supported.")
						.addInlineField("Order", "View the logical order of operations.")
						.addInlineField("Solve", "Returns the result of a given expression. This function does not solve equations, just expressions.")
						.addInlineField("Primes", "Returns the prime factorization of a given expression, if it exists. Only works with whole numbers.")
						.addInlineField("Vertex", "Formats a given quadratic equation into vertex form.")
						.addInlineField("Roman", "Converts Arabic numerals to Roman numerals and vice-versa.")
						.addInlineField("Chinese", "Converts Arabic numerals to Chinese numerals and vice-versa.")
						.addInlineField("Japanese", "Converts Arabic numerals to Japanese numerals and vice-versa.")
						.addField("To Do List", "\n • Reminder function"
								+ "\n • Command logging"
								+ "\n • Paper and coin currency conversion"
								+ "\n • Number to plain English"
								+ "\n • Point-slope equation formatter"
								+ "\n • Taylor series"
								+ "\n • `ζ(n)`"
								+ "\n • Algebraic equations solving function."
								+ "\nDebug √3945240.5, cbrt(462252638930914036407598308067858564), c.primes(111) misbehavior.");
					break;
					
				case "functions":
					String x;
					if(BigDecimalMath.isLongValue(X))
						x = X.toPlainString();
					else x = toScientificString(format(X, 16));
					String y;
					if(BigDecimalMath.isLongValue(Y))
						y = Y.toPlainString();
					else y = toScientificString(format(Y, 16));
					String z;
					if(BigDecimalMath.isLongValue(Z))
						z = Z.toPlainString();
					else z = toScientificString(format(Z, 16));
					embed = new EmbedBuilder().setAuthor("Functions")
						.addField("Binary operators", "• `+` addition\n • `-` subtraction\n • `*` or `×` or `⋅` multiplication\n • `/` or `÷` division\n • `%` modulo\n • `^` exponentation")
						.addField("Unary operators", " • `-(x)` negation\n • `√(x)` square root, `∛(x)` cube root, `∜(x)` quad root\n • `(x)!` factorial")
						.addField("Functions", "Roots:\n • `sqrt(x)` square root\n • `cbrt(x)` cube root\n • `root(n, x)` nth root of x"
								+ "\nLogarithms:\n • `log(x)` or `ln(x)` natural logarithm\n • `log(b, x)` or `log_b(x)` logarithm to base b of x"
								+ "\nTrigonometry:\n • `sin(x)` sine, `asin(x)` arcsine, `sinh(x)` hyperbolic sine\n • `cos(x)` cosine, `acos(x)` arccosine, `cosh(x)` hyperbolic cosine\n • `tan(x)` tangent, `atan(x)` arctangent, `tanh(x)` hyperbolic tangent\n • `csc(x)` cosecant, `acsc(x)` arccosecant, `csch(x)` hyperbolic cosecant\n • `sec(x)` secant, `asec(x)` arcsecant, `sech(x)` hyperbolic secant\n • `cot(x)` cotangent, `acot(x)` arccotangent, `coth(x)` hyperbolic cotangent"
								+ "\nOther:\n • `abs(x)` absolute value\n • `round(x)` round half up\n • `random(x)` random value between 0 and x\n • `fib(n)` nth fibonacci number, `afib(f)` fibonacci inverse\n • `Γ(x)` or `gamma(x)` gamma function")
						.addField("Constants", " • `π` or `pi` Archimedes' constant\n • `n` or `avogadro` Avogadro's constant\n • `e` Euler's number\n • `α` or `alpha` Feigenbaum reduction parameter\n • `δ` or `delta` Feigenbaum bifurcation velocity\n • `φ` or `phi` the golden ratio\n • `γ` or `gamma` Euler-Mascheroni constant\n • `h` Planck's constant")
						.addField("Variables", "`x` " + x + "\n`y` " + y + "\n`z` " + z + "\n`setx(a)`, `sety(a)`, `setz(a)` set the variable's value to a.");
					break;
					
				case "order":
					embed = new EmbedBuilder().setAuthor("Order of Operations")
						.setDescription("This is the order in which the bot solves different elements in an expression.")
						.addField("Functions", "First values within function parentheses are resolved.\nThen functions are calculated with the resolved values as inputs.")
						.addField("Constants", "Constants are replaced with their corresponding values.")
						.addField("Parentheses", "First content between absolute value bars `|x|` are resolved from left to right.\nThen content between parenthesis pairs `(x)` and brackets `[x]` are resolved from left to right concurrently.")
						.addField("Factorials", "Factorials `(x)!` are resolved from left to right.")
						.addField("Exponentials", "First roots `√(x)` are resolved from left to right.\nThen exponents `a^b` are resolved from right to left.")
						.addField("Multiplication", "Multiplication `a*b`, division `a/b`, and modulo `a%b` are resolved from left to right concurrently.")
						.addField("Addition", "Addition `a+b` and subtraction `a-b` are resolved from left to right concurrently.");
					break;
					
				case "solve":
					if(content.length >= 2) {
						try {
							StringBuilder work = new StringBuilder(content[1]);
							if(content.length > 2)
								for(int i = 2; i < content.length; i++) {
									work.append(" " + content[i]);
								}
							long t = System.currentTimeMillis();
							BigDecimal result = solve(new String(work).replaceAll("\\s",""), t);
							time = System.currentTimeMillis() - t;
							work.append(" =\n");
							
							String description;
							try {
								description = new String(work) + result.toBigIntegerExact();
								if(description.length() > 1024)
									throw new ArithmeticException();
							} catch(ArithmeticException ae) {
								description = new String(work) + toScientificString(result.toString());
								if(description.length() > 1024)
									description = new String(work) + toScientificString(format(result, 16));
							}
							embed = new EmbedBuilder().setAuthor("Solve").setDescription(description);
							
						} catch(StringIndexOutOfBoundsException sioobe) {
							String cause = sioobe.getMessage();
							Discord.errorLog("StringIndexOutOfBoundsException", "Unmatched Grouping", cause, Arrays.toString(sioobe.getStackTrace()), logEmbed, sentLogMessage);
							if(cause != null) {
								cause = "\nReason: " + cause + ".";
								embed = new EmbedBuilder().setAuthor("Unmatched Grouping").setDescription("Your command has one or more unmatched groupings." + cause);
							} else embed = new EmbedBuilder().setAuthor("Unmatched Grouping").setDescription("Your command has one or more unmatched groupings.");
							
						} catch(NullPointerException npe) {
							String cause = npe.getMessage();
							Discord.errorLog("NullPointerException", "Missing Values", cause, Arrays.toString(npe.getStackTrace()), logEmbed, sentLogMessage);
							if(cause != null) {
								cause = "\nReason: " + cause + ".";
								embed = new EmbedBuilder().setAuthor("Missing Values").setDescription("Your command is missing one more expected values." + cause);
							} else embed = new EmbedBuilder().setAuthor("Missing Values").setDescription("Your command is missing one more expected values.");
							
						} catch(ArithmeticException ae) {
							String cause =  ae.getMessage();
							Discord.errorLog("ArithmeticException", "Incalculable Arithmetic", cause, Arrays.toString(ae.getStackTrace()), logEmbed, sentLogMessage);
							if(cause != null) {
								cause = "\nReason: " + cause + ".";
								embed = new EmbedBuilder().setAuthor("Incalculable Arithmetic").setDescription("The operation was interrupted because an incalculable exception was encountered." + cause);
							} else embed = new EmbedBuilder().setAuthor("Incalculable Arithmetic").setDescription("The operation was interrupted because an incalculable exception was encountered.");
							
						} catch(NumberFormatException nfe) {
							Discord.errorLog("NumberFormatException", "Invalid Parameter", nfe.getMessage(), Arrays.toString(nfe.getStackTrace()), logEmbed, sentLogMessage);
							embed = new EmbedBuilder().setAuthor("Invalid Parameter").setDescription("One or more of your parameters were incompatible with the requested command.");
							
						} catch(TimeoutException te) {
							String cause = te.getMessage();
							Discord.errorLog("TimeoutException", "Timeout", cause, Arrays.toString(te.getStackTrace()), logEmbed, sentLogMessage);
							if(cause != null) {
								cause = "\nReason: " + cause + ".";
								embed = new EmbedBuilder().setAuthor("Timeout").setDescription("Your request was aborted because it took too long to process." + cause);
							} else embed = new EmbedBuilder().setAuthor("Timeout").setDescription("Your request was aborted because it took too long to process.");
						}
					}
					break;

				case "primes":
					if(content.length >= 2) {
						try {
							StringBuilder work = new StringBuilder(e.getMessageContent().substring(PREFIX.length() + content[0].length()));
							long t = System.currentTimeMillis();
							work.append(" =\n" + factorizationToString(Algorithms.getPrimeFactorization(solve(work.toString().replaceAll("\\s",""), t).toBigIntegerExact())));
							time = System.currentTimeMillis() - t;
							embed = new EmbedBuilder().setAuthor("Prime Factors").setDescription(work.toString());
							
						} catch(StringIndexOutOfBoundsException sioobe) {
							String cause = sioobe.getMessage();
							Discord.errorLog("StringIndexOutOfBoundsException", "Unmatched Grouping", cause, Arrays.toString(sioobe.getStackTrace()), logEmbed, sentLogMessage);
							if(cause != null) {
								cause = "\nReason: " + cause + ".";
								embed =new EmbedBuilder().setAuthor("Unmatched Grouping").setDescription("Your command has one or more unmatched groupings." + cause);
							} else embed = new EmbedBuilder().setAuthor("Unmatched Grouping").setDescription("Your command has one or more unmatched groupings.");
				
						} catch(NullPointerException npe) {
							String cause = npe.getMessage();
							Discord.errorLog("NullPointerException", "Missing Values", cause, Arrays.toString(npe.getStackTrace()), logEmbed, sentLogMessage);
							if(cause != null) {
								cause = "\nReason: " + cause + ".";
								embed = new EmbedBuilder().setAuthor("Missing Values").setDescription("Your command is missing one more expected values." + cause);
							} else embed = new EmbedBuilder().setAuthor("Missing Values").setDescription("Your command is missing one more expected values.");
						
						} catch(ArithmeticException ae) {
							String cause =  ae.getMessage();
							Discord.errorLog("ArithmeticException", "Incalculable Arithmetic", cause, Arrays.toString(ae.getStackTrace()), logEmbed, sentLogMessage);
							if(cause != null) {
								cause = "\nReason: " + cause + ".";
								embed = new EmbedBuilder().setAuthor("Incalculable Arithmetic").setDescription("The operation was interrupted because an incalculable exception was encountered." + cause);
							} else embed = new EmbedBuilder().setAuthor("Incalculable Arithmetic").setDescription("The operation was interrupted because an incalculable exception was encountered.");
						
						} catch(NumberFormatException nfe) {
							Discord.errorLog("NumberFormatException", "Invalid Parameter", nfe.getMessage(), Arrays.toString(nfe.getStackTrace()), logEmbed, sentLogMessage);
							embed = new EmbedBuilder().setAuthor("Invalid Parameter").setDescription("One or more of your parameters were incompatible with the requested command.");
						
						} catch(TimeoutException te) {
							String cause = te.getMessage();
							Discord.errorLog("TimeoutException", "Timeout", cause, Arrays.toString(te.getStackTrace()), logEmbed, sentLogMessage);
							if(cause != null) {
								cause = "\nReason: " + cause + ".";
								embed = new EmbedBuilder().setAuthor("Timeout").setDescription("Your request was aborted because it took too long to process." + cause);
							} else embed = new EmbedBuilder().setAuthor("Timeout").setDescription("Your request was aborted because it took too long to process.");
						}
					}
					break;
				
				case "vertex":
					if(content.length >= 2) {
						StringBuilder work = new StringBuilder(e.getMessageContent().substring(content[0].length() + 3));
						embed = new EmbedBuilder().setAuthor("Vertex Form").setDescription(Conversion.getVertexFormFull(work.toString()));
					}
					break;
					
				case "roman":
					ROMAN: if(content.length >= 2) {
						try {
							int num = Integer.parseInt(content[1].replaceAll(",", ""));
							embed = new EmbedBuilder().setAuthor("Arabic to Roman Numerals").setDescription(content[1] + " = \n" + Conversion.intToRoman(num));
						
						} catch(NumberFormatException nfe) {
							String cause = nfe.getMessage();
							if(cause == null || !cause.contains("cannot")) {
								try {
									Double.parseDouble(content[1].replaceAll(",", ""));
									embed = new EmbedBuilder().setAuthor("Invalid Parameter").setDescription("One or more of your parameters were incompatible with the requested command.\nReason: Decimals cannot be represented in Roman Numerals.");
									break ROMAN;
								} catch(NumberFormatException nfe2) {}
								try {
									embed = new EmbedBuilder().setAuthor("Roman to Arabic Numerals").setDescription(content[1] + " = \n" + Conversion.romanToInt(content[1]));
								} catch(NumberFormatException nfe2) {
									String cause2 = nfe2.getMessage();
									Discord.errorLog("NumberFormatException", "Invalid Parameter", cause2, Arrays.toString(nfe2.getStackTrace()), logEmbed, sentLogMessage);
									if(cause2 != null)
										embed = new EmbedBuilder().setAuthor("Invalid Parameter").setDescription("One or more of your parameters were incompatible with the requested command.\nReason: " + cause2 + ".");
									else embed = new EmbedBuilder().setAuthor("Invalid Parameter").setDescription("One or more of your parameters were incompatible with the requested command.");
								}
							}
							else {
								Discord.errorLog("NumberFormatException", "Invalid Parameter", cause, Arrays.toString(nfe.getStackTrace()), logEmbed, sentLogMessage);
								embed = new EmbedBuilder().setAuthor("Invalid Parameter").setDescription("One or more of your parameters were incompatible with the requested command.\nReason: " + cause + ".");
							}
						}
					}
					break;
					
				case "japanese":
				case "chinese":
					CHINESE: if(content.length >= 2) {
						boolean japanese = content[0].equals("japanese");
						try {
							long num = Long.parseLong(content[1].replaceAll(",", ""));
							embed = new EmbedBuilder().setAuthor("Arabic to Chinese Numerals").setDescription(content[1] + " = \n" + Conversion.longToChinese(num, japanese));
							if(japanese)
								embed.setAuthor("Arabic to Japanese Numerals");
						} catch(NumberFormatException nfe) {
							String cause = nfe.getMessage();
							if(cause == null || !(cause.contains("cannot") || cause.contains("less than"))) {
								try {
									double value = Double.parseDouble(content[1].replaceAll(",", ""));
									if(value > Long.MAX_VALUE)
										embed = new EmbedBuilder().setAuthor("Invalid Parameter").setDescription("One or more of your parameters were incompatible with the requested command.\nReason: Numbers greater than 9,223,372,036,854,775,807 are not supported.");
									else if(value < Long.MIN_VALUE)
										embed = new EmbedBuilder().setAuthor("Invalid Parameter").setDescription("One or more of your parameters were incompatible with the requested command.\nReason: Numbers less than -9,223,372,036,854,775,808 are not supported.");
									else embed = new EmbedBuilder().setAuthor("Invalid Parameter").setDescription("One or more of your parameters were incompatible with the requested command.\nReason: Decimal conversion is not supported.");
									break CHINESE;
								} catch(NumberFormatException nfe2) {}
								try {
									embed = new EmbedBuilder().setAuthor("Chinese to Arabic Numerals").setDescription(content[1] + " = \n" + Conversion.chineseToLong(content[1]));
									if(japanese)
										embed.setAuthor("Japanese to Arabic Numerals");
								} catch(NumberFormatException nfe2) {
									String cause2 = nfe2.getMessage();
									Discord.errorLog("NumberFormatException", "Invalid Parameter", cause2, Arrays.toString(nfe2.getStackTrace()), logEmbed, sentLogMessage);
									if(cause2 != null)
										embed = new EmbedBuilder().setAuthor("Invalid Parameter").setDescription("One or more of your parameters were incompatible with the requested command.\nReason: " + cause2 + ".");
									else embed = new EmbedBuilder().setAuthor("Invalid Parameter").setDescription("One or more of your parameters were incompatible with the requested command.");
								}
							}
							else {
								Discord.errorLog("NumberFormatException", "Invalid Parameter", cause, Arrays.toString(nfe.getStackTrace()), logEmbed, sentLogMessage);
								embed = new EmbedBuilder().setAuthor("Invalid Parameter").setDescription("One or more of your parameters were incompatible with the requested command.\nReason: " + cause + ".");
							}
						}
					}
					break;
				
				case "english": //TODO
					break;
					
				case "remind":
					if(content.length >= 5) {
						long userID = e.getMessageAuthor().getId();
						long creationChannelID = e.getMessage().getChannel().getId();
						long creationStamp = e.getMessage().getCreationTimestamp().toEpochMilli();
						
						//destination
						long destination = -1;
						if(content[1].equals("me"))
							destination = 0;
						else if(content[1].equals("here"))
							destination = 1;
						else if(isNumeric(content[1]))
							//TODO handle errors
							destination = Long.parseLong(content[1]);
						else if(content[1].startsWith("<#"))
							//TODO handle errors
							destination = Long.parseLong(content[1].substring(2, content[1].length() - 1));
						else {
							//TODO handle errors
							destination = e.getServer().get().getChannelsByName(content[1]).get(0).getId();
						}
						
						//message
						String reminderMessage = new String();
						//if(e.getMessageContent().lastIndexOf("in") > 0)
						//TODO error logging
						reminderMessage = e.getMessageContent().substring(e.getMessageContent().indexOf(content[2]), e.getMessageContent().lastIndexOf("in"));
						
						//time
						long executionStamp = System.currentTimeMillis();
						String timeStr = e.getMessageContent().toLowerCase().substring(e.getMessageContent().lastIndexOf("in") + 3).trim();
						
						if(timeStr.contains("w")) {
							int weeks = Integer.parseInt(timeStr.substring(0, timeStr.indexOf("w")));
							timeStr = timeStr.substring(timeStr.indexOf("w") + 1);
							executionStamp += weeks * 604800000;
						}
						
						if(timeStr.contains("d")) {
							int days = Integer.parseInt(timeStr.substring(0, timeStr.indexOf("d")));
							timeStr = timeStr.substring(timeStr.indexOf("d") + 1);
							executionStamp += days * 86400000;
						}
						
						if(timeStr.contains("h")) {
							int hours = Integer.parseInt(timeStr.substring(0, timeStr.indexOf("h")));
							timeStr = timeStr.substring(timeStr.indexOf("h") + 1);
							executionStamp += hours * 3600000;
						}
						
						if(timeStr.contains("m")) {
							int minutes = Integer.parseInt(timeStr.substring(0, timeStr.indexOf("m")));
							timeStr = timeStr.substring(timeStr.indexOf("m") + 1);
							executionStamp += minutes * 60000;
						}
						
						if(timeStr.contains("s")) {
							int secs = Integer.parseInt(timeStr.substring(0, timeStr.indexOf("s")));
							timeStr = timeStr.substring(timeStr.indexOf("s") + 1);
							executionStamp += secs * 1000;
						}
						
						embed = Discord.scheduleReminder(destination, reminderMessage, executionStamp, userID, creationChannelID, creationStamp);
					}
					break;
				
				case "test":
					embed = new EmbedBuilder().setDescription("❘M\u0305❘  |M\u0305| │\u0305M\u0305│\u0305 │M\u0305│" + Conversion.numberToEnglish(BigDecimalMath.toBigDecimal(content[1])));
					break;
					
				default:
					if(content.length >= 1) {
						try {
							StringBuilder work = new StringBuilder(content[0]);
							for(int i = 1; i < content.length; i++)
								work.append(" " + content[i]);
							
							long t = System.currentTimeMillis();
							BigDecimal result = solve(new String(work).replaceAll("\\s",""), t);
							time = System.currentTimeMillis() - t;
							work.append(" =\n");
							
							String description;
							try {
								description = new String(work) + result.toBigIntegerExact();
								if(description.length() > 1024) {
									throw new ArithmeticException("Too long");
								}
							} catch(ArithmeticException ae) {
								description = new String(work) + toScientificString(result.toString());
								if(description.length() > 1024) {
									description = new String(work) + toScientificString(format(result, 16));
								}
							}
							embed = new EmbedBuilder().setAuthor("Solve").setDescription(description);
							
						} catch(StringIndexOutOfBoundsException sioobe) {
							String cause = sioobe.getMessage();
							Discord.errorLog("StringIndexOutOfBoundsException", "Unmatched Grouping", cause, Arrays.toString(sioobe.getStackTrace()), logEmbed, sentLogMessage);
							if(cause != null) {
								cause = "\nReason: " + cause + ".";
								embed =new EmbedBuilder().setAuthor("Unmatched Grouping").setDescription("Your command has one or more unmatched groupings." + cause);
							} else embed = new EmbedBuilder().setAuthor("Unmatched Grouping").setDescription("Your command has one or more unmatched groupings.");
						
						} catch(NullPointerException npe) {
							String cause = npe.getMessage();
							Discord.errorLog("NullPointerException", "Mising Values", cause, Arrays.toString(npe.getStackTrace()), logEmbed, sentLogMessage);
							if(cause != null) {
								cause = "\nReason: " + cause + ".";
								embed = new EmbedBuilder().setAuthor("Missing Values").setDescription("Your command is missing one more expected values." + cause);
							} else embed = new EmbedBuilder().setAuthor("Missing Values").setDescription("Your command is missing one more expected values.");
						
						} catch(ArithmeticException ae) {
							String cause =  ae.getMessage();
							Discord.errorLog("ArithmeticException", "Incalculable Arithmetic", cause, Arrays.toString(ae.getStackTrace()), logEmbed, sentLogMessage);
							if(cause != null) {
								cause = "\nReason: " + cause + ".";
								embed = new EmbedBuilder().setAuthor("Incalculable Arithmetic").setDescription("The operation was interrupted because an incalculable exception was encountered." + cause);
							} else embed = new EmbedBuilder().setAuthor("Incalculable Arithmetic").setDescription("The operation was interrupted because an incalculable exception was encountered.");
						
						} catch(NumberFormatException nfe) {
							Discord.errorLog("NumberFormatException", "Invalid Parameter", nfe.getMessage(), Arrays.toString(nfe.getStackTrace()), logEmbed, sentLogMessage);
							embed = new EmbedBuilder().setAuthor("Invalid Parameter").setDescription("One or more of your parameters were incompatible with the requested command.");
						
						} catch(TimeoutException te) {
							String cause = te.getMessage();
							Discord.errorLog("TimeoutException", "Timeout", cause, Arrays.toString(te.getStackTrace()), logEmbed, sentLogMessage);
							if(cause != null) {
								cause = "\nReason: " + cause + ".";
								embed = new EmbedBuilder().setAuthor("Timeout").setDescription("Your request was aborted because it took too long to process." + cause);
							} else embed = new EmbedBuilder().setAuthor("Timeout").setDescription("Your request was aborted because it took too long to process.");
						}
					}
					break;
				}
			
			if(time >= 0)
				e.getChannel().sendMessage(embed.setFooter("Response for " + e.getMessageAuthor().getDisplayName() + " found in " + time + " ms").setColor(BLUE));
			else e.getChannel().sendMessage(embed.setFooter("Response for " + e.getMessageAuthor().getDisplayName()).setColor(BLUE));
			
			Discord.getAPI().updateStatus(UserStatus.ONLINE);
		}
	}

	private boolean isNumeric(String str) { 
		try {  
			Long.parseLong(str);  
			return true;
		} catch(NumberFormatException e){  
			return false;  
		}  
	}

	private BigDecimal solve(String s, long time) throws StringIndexOutOfBoundsException, NullPointerException, TimeoutException {
		
		if(System.currentTimeMillis() - time > 10000)
			throw new TimeoutException("Expression took more than 10 seconds to calculate");
		
		if(s.length() == 0)
			throw new NullPointerException(); //is missing value
		
		//System.out.println(System.currentTimeMillis() - time + ": Start " + s);
		
		if(s.contains("[") || s.contains("]")) {
			if(!s.contains("["))
				throw new StringIndexOutOfBoundsException("Unmatched brackets, missing start bracket");
			if(!s.contains("]"))
				throw new StringIndexOutOfBoundsException("Unmatched brackets, missing end bracket");
			s = s.replaceAll("\\[", "(").replaceAll("\\]", ")");
		}
		
		//resolve functions
		while(s.contains("sqrt(")) {
			int startIndex = s.indexOf("sqrt("); 
			int endIndex = findEndParenthesis(s, startIndex + 4);
			if(startIndex == 0) {
				if(endIndex == s.length() - 1)
					return DefaultBigDecimalMath.root(solve(s.substring(5, endIndex), time), BigDecimal.valueOf(2));
				else s = "(" + DefaultBigDecimalMath.root(solve(s.substring(5, endIndex), time), BigDecimal.valueOf(2)).toPlainString() + ")" + s.substring(endIndex + 1);	
			} else if(endIndex == s.length() - 1)
				s = s.substring(0, startIndex) + "(" +  DefaultBigDecimalMath.root(solve(s.substring(startIndex + 5, endIndex), time), BigDecimal.valueOf(2)).toPlainString() + ")";	
			 else s = s.substring(0, startIndex) + "(" + DefaultBigDecimalMath.root(solve(s.substring(startIndex + 5, endIndex), time), BigDecimal.valueOf(2)).toPlainString() + ")" + s.substring(endIndex + 1);
		}
		
		while(s.contains("cbrt(")) {
			int startIndex = s.indexOf("cbrt("); 
			int endIndex = findEndParenthesis(s, startIndex + 4);
			if(startIndex == 0) {
				if(endIndex == s.length() - 1)
					return DefaultBigDecimalMath.root(solve(s.substring(5, endIndex), time), BigDecimal.valueOf(3));
				else s = "(" + DefaultBigDecimalMath.root(solve(s.substring(5, endIndex), time), BigDecimal.valueOf(3)).toPlainString() + ")" + s.substring(endIndex + 1);
			} else if(endIndex == s.length() - 1)
				s = s.substring(0, startIndex) + "(" + DefaultBigDecimalMath.root(solve(s.substring(startIndex + 5, endIndex), time), BigDecimal.valueOf(3)).toPlainString() + ")";	
			 else s = s.substring(0, startIndex) + "(" + DefaultBigDecimalMath.root(solve(s.substring(startIndex + 5, endIndex), time), BigDecimal.valueOf(3)).toPlainString() + ")" + s.substring(endIndex + 1);
		}
		
		while(s.contains("root(")) {
			int startIndex = s.indexOf("root("), nIndex = -1, endIndex = -1;
			int count = 1;
			for(int i = startIndex + 5; i < s.length(); i++) {
				if(s.charAt(i) == '(')
					count++;
				else if(s.charAt(i) == ')') {
					count--;
					if(count == 0) {
						endIndex = i;
						break;
					}
				} else if(s.charAt(i) == ',' && count == 1) {
					nIndex = i;
				}
			}
			if(startIndex == 0) {
				if(endIndex == s.length() - 1) {
					return DefaultBigDecimalMath.root(solve(s.substring(nIndex + 1, endIndex), time), solve(s.substring(5, nIndex), time));
				} else s = "(" + DefaultBigDecimalMath.root(solve(s.substring(nIndex + 1, endIndex), time), solve(s.substring(5, nIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
			} else if(endIndex == s.length() - 1) {
				s = s.substring(0, startIndex) + "(" + DefaultBigDecimalMath.root(solve(s.substring(nIndex + 1, endIndex), time), solve(s.substring(startIndex + 5, nIndex), time)).toPlainString() + ")";
			} else s = s.substring(0, startIndex) + "(" + DefaultBigDecimalMath.root(solve(s.substring(nIndex + 1, endIndex), time), solve(s.substring(startIndex + 5, nIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
		}
		
		if(s.contains("log(2)"))
			s = s.replaceAll("log(2)", "(" + LOG2 + ")");
		
		while(s.contains("ln(")) {
			int startIndex = s.indexOf("ln(");
			int endIndex = findEndParenthesis(s, startIndex + 2);
			if(startIndex == 0) {
				if(endIndex == s.length() - 1)
					return DefaultBigDecimalMath.log(solve(s.substring(3, endIndex), time));
				else s = "(" + DefaultBigDecimalMath.log(solve(s.substring(3, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
			} else if(endIndex == s.length() - 1)
				s = s.substring(0, startIndex) + "(" + DefaultBigDecimalMath.log(solve(s.substring(startIndex + 3, endIndex), time)).toPlainString() + ")";
			else s = s.substring(0, startIndex) + "(" + DefaultBigDecimalMath.log(solve(s.substring(startIndex + 3, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
		}
		
		while(s.contains("log(")) {
			int startIndex = s.indexOf("log("), endIndex = -1, bIndex = -1;
			int count = 1;
			for(int i = startIndex + 4; i < s.length(); i++) {
				if(s.charAt(i) == '(')
					count++;
				else if(s.charAt(i) == ')') {
					count--;
					if(count == 0) {
						endIndex = i;
						break;
					}
				} else if(s.charAt(i) == ',' && count == 1) {
					bIndex = i;
				}
			}
			if(startIndex == 0) {
				if(endIndex == s.length() - 1) {
					if(bIndex == -1) {
						return DefaultBigDecimalMath.log(solve(s.substring(4, endIndex), time));
					} else {
						return log(solve(s.substring(4, bIndex), time), solve(s.substring(bIndex + 1, endIndex), time));
					}
				} else if(bIndex == -1) {
					s = "(" + DefaultBigDecimalMath.log(solve(s.substring(4, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
				} else {
					s = "(" + log(solve(s.substring(4, bIndex), time), solve(s.substring(bIndex + 1, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
				}
			} else if(endIndex == s.length() - 1) {
				if(bIndex == -1) {
					s = s.substring(0, startIndex) + "(" + DefaultBigDecimalMath.log(solve(s.substring(startIndex + 4, endIndex), time)).toPlainString() + ")";
				} else {
					s = s.substring(0, startIndex) + "(" + log(solve(s.substring(startIndex + 4, endIndex), time), solve(s.substring(bIndex + 1, endIndex), time)).toPlainString() + ")";
				}
			} else if(bIndex == -1) {
				s = s.substring(0, startIndex) + "(" + DefaultBigDecimalMath.log(solve(s.substring(startIndex + 4, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
			} else {
				s = s.substring(0, startIndex) + "(" + log(solve(s.substring(startIndex + 4, endIndex), time), solve(s.substring(bIndex + 1, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
			}
		}
		
		while(s.contains("log_")) {
			int startIndex = s.indexOf("log_"), endIndex = -1, bIndex = s.substring(startIndex).indexOf('(') + startIndex;
			if(bIndex == -1)
				throw new NullPointerException();
			endIndex = findEndParenthesis(s, bIndex);
			if(endIndex == -1)
				throw new NullPointerException();
			if(startIndex == 0) {
				if(endIndex == s.length() - 1) {
					return log(solve(s.substring(4, bIndex), time), solve(s.substring(bIndex + 1, endIndex), time));
				} else s = "(" + log(solve(s.substring(4, bIndex), time), solve(s.substring(bIndex + 1, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
			} else if(endIndex == s.length() - 1) {
				s = s.substring(0, startIndex) + "(" + log(solve(s.substring(startIndex + 4, bIndex), time), solve(s.substring(bIndex + 1, endIndex), time)).toPlainString() + ")";
			} else s = s.substring(0, startIndex) + "(" + log(solve(s.substring(startIndex + 4, bIndex), time), solve(s.substring(bIndex + 1, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
		}
		
		while(s.contains("afib(")) {
			int index = s.indexOf("afib(");
			int endIndex = findEndParenthesis(s, index + 4);
			if(index == 0) {
				if(endIndex == s.length() - 1) {
					return fibonacciInverse(solve(s.substring(5, endIndex), time));
				} else s = "(" + fibonacciInverse(solve(s.substring(5, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
			} else if(endIndex == s.length() - 1) {
				s = s.substring(0, index) + "(" + fibonacciInverse(solve(s.substring(index + 5, endIndex), time)).toPlainString() + ")";
			} else s = s.substring(0, index) + "(" + fibonacciInverse(solve(s.substring(index + 5, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
		}
		
		while(s.contains("fib(")) {
			int index = s.indexOf("fib(");
			int endIndex = findEndParenthesis(s, index + 3);
			if(index == 0) {
				if(endIndex == s.length() - 1) {
					return fibonacci(solve(s.substring(4, endIndex), time));
				} else s = "(" + fibonacci(solve(s.substring(4, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
			} else if(endIndex == s.length() - 1) {
				s = s.substring(0, index) + "(" + fibonacci(solve(s.substring(index + 4, endIndex), time)).toPlainString() + ")";
			} else s = s.substring(0, index) + "(" + fibonacci(solve(s.substring(index + 4, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
		}
		
		
		if(s.contains("gamma("))
			s = s.replaceAll("gamma\\(", "Γ(");
		
		while(s.contains("Γ(")){
			int index = s.indexOf("Γ(");
			int endIndex = findEndParenthesis(s, index + 1);
			if(index == 0) {
				if(endIndex == s.length() - 1) {
					return DefaultBigDecimalMath.gamma(solve(s.substring(2, endIndex), time));
				} else s = "(" + DefaultBigDecimalMath.gamma(solve(s.substring(2, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
			} else if(endIndex == s.length() - 1) {
				s = s.substring(0, index) + "(" + DefaultBigDecimalMath.gamma(solve(s.substring(index + 2, endIndex), time)).toPlainString() + ")";
			} else s = s.substring(0, index) + "(" + DefaultBigDecimalMath.gamma(solve(s.substring(index + 2, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
		}
		
		//inverse trig
		while(s.contains("asin(")) {
			int index = s.indexOf("asin(");
			int endIndex = findEndParenthesis(s, index + 4);
			if(index == 0) {
				if(endIndex == s.length() - 1) {
					return DefaultBigDecimalMath.asin(solve(s.substring(5, endIndex), time));
				} else s = "(" + DefaultBigDecimalMath.asin(solve(s.substring(5, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
			} else if(endIndex == s.length() - 1) {
				s = s.substring(0, index) + "(" + DefaultBigDecimalMath.asin(solve(s.substring(index + 5, endIndex), time)).toPlainString() + ")";
			} else s = s.substring(0, index) + "(" + DefaultBigDecimalMath.asin(solve(s.substring(index + 5, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
		}
		while(s.contains("acos(")) {
			int index = s.indexOf("acos(");
			int endIndex = findEndParenthesis(s, index + 4);
			if(index == 0) {
				if(endIndex == s.length() - 1) {
					return DefaultBigDecimalMath.acos(solve(s.substring(5, endIndex), time));
				} else s = "(" + DefaultBigDecimalMath.acos(solve(s.substring(5, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
			} else if(endIndex == s.length() - 1) {
				s = s.substring(0, index) + "(" + DefaultBigDecimalMath.acos(solve(s.substring(index + 5, endIndex), time)).toPlainString() + ")";
			} else s = s.substring(0, index) + "(" + DefaultBigDecimalMath.acos(solve(s.substring(index + 5, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
		}
		while(s.contains("atan(")) {
			int index = s.indexOf("atan(");
			int endIndex = findEndParenthesis(s, index + 4);
			if(index == 0) {
				if(endIndex == s.length() - 1) {
					return DefaultBigDecimalMath.atan(solve(s.substring(5, endIndex), time));
				} else s = "(" + DefaultBigDecimalMath.atan(solve(s.substring(5, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
			} else if(endIndex == s.length() - 1) {
				s = s.substring(0, index) + "(" + DefaultBigDecimalMath.atan(solve(s.substring(index + 5, endIndex), time)).toPlainString() + ")";
			} else s = s.substring(0, index) + "(" + DefaultBigDecimalMath.atan(solve(s.substring(index + 5, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
		}
		while(s.contains("acsc(")) {
			int index = s.indexOf("acsc(");
			int endIndex = findEndParenthesis(s, index + 4);
			if(index == 0) {
				if(endIndex == s.length() - 1) {
					return acsc(solve(s.substring(5, endIndex), time));
				} else s = "(" + acsc(solve(s.substring(5, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
			} else if(endIndex == s.length() - 1) {
				s = s.substring(0, index) + "(" + acsc(solve(s.substring(index + 5, endIndex), time)).toPlainString() + ")";
			} else s = s.substring(0, index) + "(" + acsc(solve(s.substring(index + 5, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
		}
		while(s.contains("asec(")) {
			int index = s.indexOf("asec(");
			int endIndex = findEndParenthesis(s, index + 4);
			if(index == 0) {
				if(endIndex == s.length() - 1) {
					return asec(solve(s.substring(5, endIndex), time));
				} else s = "(" + asec(solve(s.substring(5, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
			} else if(endIndex == s.length() - 1) {
				s = s.substring(0, index) + "(" + asec(solve(s.substring(index + 5, endIndex), time)).toPlainString() + ")";
			} else s = s.substring(0, index) + "(" + asec(solve(s.substring(index + 5, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
		}
		while(s.contains("acot(")) {
			int index = s.indexOf("acot(");
			int endIndex = findEndParenthesis(s, index + 4);
			if(index == 0) {
				if(endIndex == s.length() - 1) {
					return acot(solve(s.substring(5, endIndex), time));
				} else s = "(" + acot(solve(s.substring(5, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
			} else if(endIndex == s.length() - 1) {
				s = s.substring(0, index) + "(" + acot(solve(s.substring(index + 5, endIndex), time)).toPlainString() + ")";
			} else s = s.substring(0, index) + "(" + acot(solve(s.substring(index + 5, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
		}
		
		//normal trig
		while(s.contains("sin(")) {
			int index = s.indexOf("sin(");
			int endIndex = findEndParenthesis(s, index + 3);
			if(index == 0) {
				if(endIndex == s.length() - 1) {
					return DefaultBigDecimalMath.sin(solve(s.substring(4, endIndex), time));
				} else s = "(" + DefaultBigDecimalMath.sin(solve(s.substring(4, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
			} else if(endIndex == s.length() - 1) {
				s = s.substring(0, index) + "(" + DefaultBigDecimalMath.sin(solve(s.substring(index + 4, endIndex), time)).toPlainString() + ")";
			} else s = s.substring(0, index) + "(" + DefaultBigDecimalMath.sin(solve(s.substring(index + 4, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
		}
		while(s.contains("cos(")) {
			int index = s.indexOf("cos(");
			int endIndex = findEndParenthesis(s, index + 3);
			if(index == 0) {
				if(endIndex == s.length() - 1) {
					return DefaultBigDecimalMath.cos(solve(s.substring(4, endIndex), time));
				} else s = "(" + DefaultBigDecimalMath.cos(solve(s.substring(4, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
			} else if(endIndex == s.length() - 1) {
				s = s.substring(0, index) + "(" + DefaultBigDecimalMath.cos(solve(s.substring(index + 4, endIndex), time)).toPlainString() + ")";
			} else s = s.substring(0, index) + "(" + DefaultBigDecimalMath.cos(solve(s.substring(index + 4, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
		}
		while(s.contains("tan(")) {
			int index = s.indexOf("tan(");
			int endIndex = findEndParenthesis(s, index + 3);
			if(index == 0) {
				if(endIndex == s.length() - 1) {
					return DefaultBigDecimalMath.tan(solve(s.substring(4, endIndex), time));
				} else s = "(" + DefaultBigDecimalMath.tan(solve(s.substring(4, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
			} else if(endIndex == s.length() - 1) {
				s = s.substring(0, index) + "(" + DefaultBigDecimalMath.tan(solve(s.substring(index + 4, endIndex), time)).toPlainString() + ")";
			} else s = s.substring(0, index) + "(" + DefaultBigDecimalMath.tan(solve(s.substring(index + 4, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
		}
		while(s.contains("csc(")) {
			int index = s.indexOf("csc(");
			int endIndex = findEndParenthesis(s, index + 3);
			if(index == 0) {
				if(endIndex == s.length() - 1) {
					return csc(solve(s.substring(4, endIndex), time));
				} else s = "(" + csc(solve(s.substring(4, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
			} else if(endIndex == s.length() - 1) {
				s = s.substring(0, index) + "(" + csc(solve(s.substring(index + 4, endIndex), time)).toPlainString() + ")";
			} else s = s.substring(0, index) + "(" + csc(solve(s.substring(index + 4, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
		}
		while(s.contains("sec(")) {
			int index = s.indexOf("sec(");
			int endIndex = findEndParenthesis(s, index + 3);
			if(index == 0) {
				if(endIndex == s.length() - 1) {
					return sec(solve(s.substring(4, endIndex), time));
				} else s = "(" + sec(solve(s.substring(4, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
			} else if(endIndex == s.length() - 1) {
				s = s.substring(0, index) + "(" + sec(solve(s.substring(index + 4, endIndex), time)).toPlainString() + ")";
			} else s = s.substring(0, index) + "(" + sec(solve(s.substring(index + 4, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
		}
		while(s.contains("cot(")) {
			int index = s.indexOf("cot(");
			int endIndex = findEndParenthesis(s, index + 3);
			if(index == 0) {
				if(endIndex == s.length() - 1) {
					return cot(solve(s.substring(4, endIndex), time));
				} else s = "(" + cot(solve(s.substring(4, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
			} else if(endIndex == s.length() - 1) {
				s = s.substring(0, index) + "(" + cot(solve(s.substring(index + 4, endIndex), time)).toPlainString() + ")";
			} else s = s.substring(0, index) + "(" + cot(solve(s.substring(index + 4, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
		}
		
		//hyperbolic trig
		while(s.contains("sinh(")) {
			int index = s.indexOf("sinh(");
			int endIndex = findEndParenthesis(s, index + 4);
			if(index == 0) {
				if(endIndex == s.length() - 1) {
					return DefaultBigDecimalMath.sinh(solve(s.substring(5, endIndex), time));
				} else s = "(" + DefaultBigDecimalMath.sinh(solve(s.substring(5, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
			} else if(endIndex == s.length() - 1) {
				s = s.substring(0, index) + "(" + DefaultBigDecimalMath.sinh(solve(s.substring(index + 5, endIndex), time)).toPlainString() + ")";
			} else s = s.substring(0, index) + "(" + DefaultBigDecimalMath.sinh(solve(s.substring(index + 5, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
		}
		while(s.contains("cosh(")) {
			int index = s.indexOf("cosh(");
			int endIndex = findEndParenthesis(s, index + 4);
			if(index == 0) {
				if(endIndex == s.length() - 1) {
					return DefaultBigDecimalMath.cosh(solve(s.substring(5, endIndex), time));
				} else s = "(" + DefaultBigDecimalMath.cosh(solve(s.substring(5, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
			} else if(endIndex == s.length() - 1) {
				s = s.substring(0, index) + "(" + DefaultBigDecimalMath.cosh(solve(s.substring(index + 5, endIndex), time)).toPlainString() + ")";
			} else s = s.substring(0, index) + "(" + DefaultBigDecimalMath.cosh(solve(s.substring(index + 5, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
		}
		while(s.contains("tanh(")) {
			int index = s.indexOf("tanh(");
			int endIndex = findEndParenthesis(s, index + 4);
			if(index == 0) {
				if(endIndex == s.length() - 1) {
					return DefaultBigDecimalMath.tanh(solve(s.substring(5, endIndex), time));
				} else s = "(" + DefaultBigDecimalMath.tanh(solve(s.substring(5, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
			} else if(endIndex == s.length() - 1) {
				s = s.substring(0, index) + "(" + DefaultBigDecimalMath.tanh(solve(s.substring(index + 5, endIndex), time)).toPlainString() + ")";
			} else s = s.substring(0, index) + "(" + DefaultBigDecimalMath.tanh(solve(s.substring(index + 5, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
		}
		while(s.contains("csch(")) {
			int index = s.indexOf("csch(");
			int endIndex = findEndParenthesis(s, index + 4);
			if(index == 0) {
				if(endIndex == s.length() - 1) {
					return csch(solve(s.substring(5, endIndex), time));
				} else s = "(" + csch(solve(s.substring(5, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
			} else if(endIndex == s.length() - 1) {
				s = s.substring(0, index) + "(" + csch(solve(s.substring(index + 5, endIndex), time)).toPlainString() + ")";
			} else s = s.substring(0, index) + "(" + csch(solve(s.substring(index + 5, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
		}
		while(s.contains("sech(")) {
			int index = s.indexOf("sech(");
			int endIndex = findEndParenthesis(s, index + 4);
			if(index == 0) {
				if(endIndex == s.length() - 1) {
					return sech(solve(s.substring(5, endIndex), time));
				} else s = "(" + sech(solve(s.substring(5, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
			} else if(endIndex == s.length() - 1) {
				s = s.substring(0, index) + "(" + sech(solve(s.substring(index + 5, endIndex), time)).toPlainString() + ")";
			} else s = s.substring(0, index) + "(" + sech(solve(s.substring(index + 5, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
		}
		while(s.contains("coth(")) {
			int index = s.indexOf("coth(");
			int endIndex = findEndParenthesis(s, index + 4);
			if(index == 0) {
				if(endIndex == s.length() - 1) {
					return coth(solve(s.substring(5, endIndex), time));
				} else s = "(" + coth(solve(s.substring(5, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
			} else if(endIndex == s.length() - 1) {
				s = s.substring(0, index) + "(" + coth(solve(s.substring(index + 5, endIndex), time)).toPlainString() + ")";
			} else s = s.substring(0, index) + "(" + coth(solve(s.substring(index + 5, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
		}
		
		while(s.contains("abs(")) {
			int index = s.indexOf("abs(");
			int endIndex = findEndParenthesis(s, index + 3);
			if(index == 0) {
				if(endIndex == s.length() - 1) {
					return absolute(solve(s.substring(4, endIndex), time));
				} else s = "(" + absolute(solve(s.substring(4, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
			} else if(endIndex == s.length() - 1) {
				s = s.substring(0, index) + "(" + absolute(solve(s.substring(index + 4, endIndex), time)).toPlainString() + ")";
			} else s = s.substring(0, index) + "(" + absolute(solve(s.substring(index + 4, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
		}
		
		while(s.contains("round(")) {
			int index = s.indexOf("round(");
			int endIndex = findEndParenthesis(s, index + 5);
			if(index == 0) {
				if(endIndex == s.length() - 1) {
					return round(solve(s.substring(6, endIndex), time));
				} else s = "(" + round(solve(s.substring(6, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
			} else if(endIndex == s.length() - 1) {
				s = s.substring(0, index) + "(" + round(solve(s.substring(index + 6, endIndex), time)).toPlainString() + ")";
			} else s = s.substring(0, index) + "(" + round(solve(s.substring(index + 6, endIndex), time)).toPlainString() + ")" + s.substring(endIndex + 1);
		}
		
		while(s.contains("random(")) {
			int start = s.indexOf("random(");
			int end = findEndParenthesis(s, start + 6);
			if(start == 0) {
				if(end == s.length() - 1) {
					return random(solve(s.substring(7, end), time));
				} else s = "(" + random(solve(s.substring(7, end), time)).toPlainString() + ")" + s.substring(end + 1);
			} else if(end == s.length() - 1) {
				s = s.substring(0, start) + "(" + random(solve(s.substring(start + 7, end), time)).toPlainString() + ")";
			} else s = s.substring(0, start) + "(" + random(solve(s.substring(start + 7, end), time)).toPlainString() + ")" + s.substring(end + 1);
		}
		
		while(s.contains("setx(")) {
			int start = s.indexOf("setx(");
			int end = findEndParenthesis(s, start + 4);
			if(start == 0) {
				if(end == s.length() - 1) {
					return X = solve(s.substring(5, end), time);
				} else s = "(" + (X = solve(s.substring(5, end), time)) + ")" + s.substring(end + 1);
			} else if(end == s.length() - 1) {
				s = s.substring(0, start) + "(" + (X = solve(s.substring(start + 5, end), time)).toPlainString() + ")";
			} else s = s.substring(0, start) + "(" + (X = solve(s.substring(start + 5, end), time)).toPlainString() + ")" + s.substring(end + 1);
		}
		
		while(s.contains("sety(")) {
			int start = s.indexOf("sety(");
			int end = findEndParenthesis(s, start + 4);
			if(start == 0) {
				if(end == s.length() - 1) {
					return Y = solve(s.substring(5, end), time);
				} else s = "(" + (Y = solve(s.substring(5, end), time)) + ")" + s.substring(end + 1);
			} else if(end == s.length() - 1) {
				s = s.substring(0, start) + "(" + (Y = solve(s.substring(start + 5, end), time)).toPlainString() + ")";
			} else s = s.substring(0, start) + "(" + (Y = solve(s.substring(start + 5, end), time)).toPlainString() + ")" + s.substring(end + 1);
		}
		
		while(s.contains("setz")) {
			int start = s.indexOf("setz(");
			int end = findEndParenthesis(s, start + 4);
			if(start == 0) {
				if(end == s.length() - 1) {
					return Z = solve(s.substring(5, end), time);
				} else s = "(" + (Z = solve(s.substring(5, end), time)) + ")" + s.substring(end + 1);
			} else if(end == s.length() - 1) {
				s = s.substring(0, start) + "(" + (Z = solve(s.substring(start + 5, end), time)).toPlainString() + ")";
			} else s = s.substring(0, start) + "(" + (Z = solve(s.substring(start + 5, end), time)).toPlainString() + ")" + s.substring(end + 1);
		}
		
		//System.out.println(System.currentTimeMillis() - time + ": Functions resolved " + s);
		
		while(Conversion.containsHanScript(s)) {
			int start = 0;
			for(int i = 0; i < s.length();) {
				int index = i;
				int codepoint = s.codePointAt(i);
				i += Character.charCount(codepoint);
				if(Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HAN) {
					start = index;
					break;
				}
			}
			
			int end = s.length();
			for(int i = start; i < s.length();) {
				int index = i;
				int codepoint = s.codePointAt(i);
				i += Character.charCount(codepoint);
				if(Character.UnicodeScript.of(codepoint) != Character.UnicodeScript.HAN) {
					end = index;
					break;
				}
			}
			
			if(start == 0) {
				if(end == s.length()) {
					return BigDecimal.valueOf(Conversion.chineseToLong(s));
				} else s = "(" + Conversion.chineseToLong(s.substring(0, end)) + ")" + s.substring(end);
			} else if(end == s.length()) {
				s = s.substring(0, start) + "(" + Conversion.chineseToLong(s.substring(start)) + ")";
			} else s = s.substring(0, start) + "(" + Conversion.chineseToLong(s.substring(start, end)) + ")" + s.substring(end);
		}
		
		//replace constants
		s = s.toLowerCase();
		if(s.contains("δ"))
			s = s.replaceAll("δ", "(" + DELTA.toPlainString() + ")");
		if(s.contains("delta"))
			s = s.replaceAll("delta", "(" + DELTA.toPlainString() + ")");
		if(s.contains("α"))
			s = s.replaceAll("α", "(" + ALPHA.toPlainString() + ")");
		if(s.contains("alpha"))
			s = s.replaceAll("alpha", "(" + ALPHA.toPlainString() + ")");
		if(s.contains("γ"))
			s = s.replaceAll("γ", "(" + GAMMA.toPlainString() + ")");
		if(s.contains("gamma"))
			s = s.replaceAll("gamma", "(" + GAMMA.toPlainString() + ")");
		if(s.contains("e"))
			s = s.replaceAll("e", "(" + DefaultBigDecimalMath.e().toPlainString() + ")");
		if(s.contains("π"))
			s = s.replaceAll("π", "(" + DefaultBigDecimalMath.pi().toPlainString() + ")");
		if(s.contains("pi"))
			s = s.replaceAll("pi", "(" + DefaultBigDecimalMath.pi().toPlainString() + ")");
		if(s.contains("φ"))
			s = s.replaceAll("φ", "(" + PHI.toPlainString() + ")");
		if(s.contains("phi"))
			s = s.replaceAll("phi", "(" + PHI.toPlainString() + ")");
		if(s.contains("n"))
			s = s.replaceAll("n", "(" + AVOGADRO.toPlainString() + ")");
		if(s.contains("h"))
			s = s.replaceAll("h", "(" + PLANCK.toPlainString() + ")");
		
		//replace variables
		if(s.contains("x"))
			s = s.replaceAll("x", "(" + X.toPlainString() + ")");
		if(s.contains("y"))
			s = s.replaceAll("y", "(" + Y.toPlainString() + ")");
		if(s.contains("z"))
			s = s.replaceAll("z", "(" + Z.toPlainString() + ")");
		
		//System.out.println(System.currentTimeMillis() - time + ": Constants substituted " + s);
		
		while(s.contains("|")) {
			int start = s.indexOf("|"), end = -1;
			int pCount = 0;
			for(int i = start + 1; i < s.length(); i++) {
				char c = s.charAt(i);
				if(pCount == 0 && c == '|')
					end = i;
				else if(c == '(')
					pCount++;
				else if(c == ')')
					pCount--;
			}
			if(end == -1)
				throw new StringIndexOutOfBoundsException("Unmatched absolute value bars");
			if(start == 0) {
				if(end == s.length() - 1) {
					return absolute(solve(s.substring(1, end), time));
				} else s = "(" + absolute(solve(s.substring(1, end), time)).toPlainString() + ")" + s.substring(end + 1);
			} else if(end == s.length() - 1) {
				s = s.substring(0, start) + "(" + absolute(solve(s.substring(start + 1, end), time)).toPlainString() + ")";
			} else s = s.substring(0, start) + "(" + absolute(solve(s.substring(start + 1, end), time)).toPlainString() + ")" + s.substring(end + 1);
		}
		
		//solve parentheses
		while(s.contains("(") || s.contains(")")) {
			int start = s.indexOf('('), end = -1;
			if(start == -1)
				throw new StringIndexOutOfBoundsException("Unmatched parentheses, missing start brace"); //is missing a start brace
			int openCount = 0;
			for(int i = start + 1; i < s.length(); i++) {
				if(s.charAt(i) == ')') {
					if(openCount > 0)
						openCount--;
					else {
						end = i;
						break;
					}
				} else if(s.charAt(i) == '(')
					openCount++;
			}
			if(end == -1)
				throw new StringIndexOutOfBoundsException("Unmatched parentheses, missing end brace"); //is missing an end brace
			
			//nothing to left
			if(start == 0) { 
				if(end == s.length() - 1) //nothing to right
					return solve(s.substring(start + 1, end), time);
				else if(s.charAt(end + 1) == '^' || s.charAt(end + 1) == '!') //power or factorial to right
					s = "{" + solve(s.substring(start + 1, end), time).toPlainString() + "}" + s.substring(end + 1);
				else if(Character.isDigit(s.charAt(end + 1))) //number to right
					s = solve(s.substring(start + 1, end), time).toPlainString() + "*" + s.substring(end + 1);
				else s = solve(s.substring(start + 1, end), time).toPlainString() + s.substring(end + 1); //something to right
			
			//nothing to right
			} else if(end == s.length() - 1) {
				if(Character.isDigit(s.charAt(start - 1))) //number to left
					s = s.substring(0, start) + "*" + solve(s.substring(start + 1, end), time).toPlainString();
				else if(s.charAt(start - 1) == '√' || s.charAt(start - 1) == '∛' || s.charAt(start - 1) == '∜')
					s = s.substring(0, start) + "{" + solve(s.substring(start + 1, end), time).toPlainString() + "}";
				else s = s.substring(0, start) + solve(s.substring(start + 1, end), time).toPlainString(); //nothing to right
				
			//number to left
			} else if(Character.isDigit(s.charAt(start - 1))) {
				if(s.charAt(end + 1) == '^' || s.charAt(end + 1) == '!') //power or factorial to right
					s = s.substring(0, start) + "*{" + solve(s.substring(start + 1, end), time).toPlainString() + "}" + s.substring(end + 1);
				else if(Character.isDigit(s.charAt(end + 1))) //number to right
					 s = s.substring(0, start) + "*" + solve(s.substring(start + 1, end), time).toPlainString() + "*" + s.substring(end + 1);
				else s = s.substring(0, start) + "*" + solve(s.substring(start + 1, end), time).toPlainString() + s.substring(end + 1); //number to left
				
			//power to right
			} else if(s.charAt(end + 1) == '^' || s.charAt(end + 1) == '!')
				s = s.substring(0, start) + "{" + solve(s.substring(start + 1, end), time).toPlainString() + "}" + s.substring(end + 1); //power to right
			
			else if(Character.isDigit(s.charAt(end + 1))) //number on right
				s = s.substring(0, start) + solve(s.substring(start + 1, end), time).toPlainString() + "*" + s.substring(end + 1);
			
			//something to left and right
			else s = s.substring(0, start) + solve(s.substring(start + 1, end), time).toPlainString() + s.substring(end + 1);
			
		}
		
		//System.out.println(System.currentTimeMillis() - time + ": Parentheses resolved " + s);
		
		while(s.contains("!")) { //only valid for non-negative integers
			int index = s.indexOf('!');
			if(index < 1)
				throw new NullPointerException("For (x)!, value x could not be found");
			else if(Character.isDigit(s.charAt(index - 1))) {
				int startIndex = -1;
				for(int i = index - 1; i >= 0; i--) {
					char c = s.charAt(i);
					if(!(Character.isDigit(c) || c == '.')) {
						startIndex = i;
						break;
					}
				}
				if(startIndex + 1 == index)
					throw new NullPointerException("For (x)!, value x could not be found");
				if(s.length() - 1 > index) { //something to right
					if(startIndex == -1) { //nothing to left
						return solve("(" + DefaultBigDecimalMath.factorial(BigDecimalMath.toBigDecimal(s.substring(0, index))).toPlainString() + ")" + s.substring(index + 1), time);
					} else {
						return solve(s.substring(0, startIndex + 1) + "(" + DefaultBigDecimalMath.factorial(BigDecimalMath.toBigDecimal(s.substring(startIndex + 1, index))).toPlainString() + ")" + s.substring(index + 1), time); //something to left
					}
				} else if(startIndex == -1) { //nothing to left
					return DefaultBigDecimalMath.factorial(BigDecimalMath.toBigDecimal(s.substring(0, index)));
				} else {
					return solve(s.substring(0, startIndex + 1) + "(" + DefaultBigDecimalMath.factorial(BigDecimalMath.toBigDecimal(s.substring(startIndex + 1, index))).toPlainString() + ")", time); //something to left
				}
					
			} else if(s.charAt(index - 1) == '}') {
				int count = 1;
				int startIndex = -1;
				for(int i = index - 2; i >= 0; i--) {
					if(s.charAt(i) == '}')
						count++;
					else if(s.charAt(i) == '{') {
						count--;
						if(count == 0) {
							startIndex = i;
						}
					}
				}
				if(startIndex == -1)
					throw new StringIndexOutOfBoundsException("Unmatched braces");
				if(startIndex == 0) { //nothing to left
					if(index == s.length() - 1) { //nothing to right
						return DefaultBigDecimalMath.factorial(solve(s.substring(startIndex + 1, index - 1), time));
					} else return solve("(" + DefaultBigDecimalMath.factorial(solve(s.substring(startIndex + 1, index - 1), time)).toPlainString() + ")" + s.substring(index + 1), time); //something to right
				} else if(index == s.length() - 1) { //nothing to right
					return solve(s.substring(0, startIndex) + "(" + DefaultBigDecimalMath.factorial(solve(s.substring(startIndex + 1, index - 1), time)).toPlainString() + ")", time);
				} else return solve(s.substring(0, startIndex) + "(" + DefaultBigDecimalMath.factorial(solve(s.substring(startIndex + 1, index - 1), time)).toPlainString() + ")" + s.substring(index + 1), time);
			} else throw new NullPointerException("For (x)!, value x could not be found");
		}

		while(s.contains("√") || s.contains("∛") || s.contains("∜")) {
			int[] operatorIndicies = {s.indexOf("√"), s.indexOf("∛"), s.indexOf("∜")};
			int operation = smallestGreaterThanOne(operatorIndicies);
			
			int index = operatorIndicies[operation];//;
			if(Character.isDigit(s.charAt(index + 1))) {
				int i = index + 2;
				for(; i < s.length(); i++) {
					char c = s.charAt(i);
					if(!Character.isDigit(c) && c != '.')
						break;
				}
				if(index == 0) {
					if(i == s.length() - 1)
						return DefaultBigDecimalMath.root(solve(s.substring(1, i), time), BigDecimal.valueOf(operation + 2));
					else return solve("(" + DefaultBigDecimalMath.root(solve(s.substring(index + 1, i), time), BigDecimal.valueOf(operation + 2)) + ")" + s.substring(i), time);
				} else if(i == s.length() - 1)
					return solve(s.substring(0, index) + "(" + DefaultBigDecimalMath.root(solve(s.substring(index + 1), time), BigDecimal.valueOf(operation + 2)).toPlainString() + ")", time);
				else return solve(s.substring(0, index) + "(" + DefaultBigDecimalMath.root(solve(s.substring(index + 1, i), time), BigDecimal.valueOf(operation + 2)).toPlainString() + ")" + s.substring(i), time);
			} else if(s.charAt(index + 1) == '(') {
				int count = 1;
				int endIndex = -1;
				for(int i = index + 2; i < s.length(); i++)
					if(s.charAt(i) == '(')
						count++;
					else if(s.charAt(i) == ')') {
						count--;
						if(count == 0) {
							endIndex = i;
							break;
						}
					} 
				if(endIndex == -1)
					throw new StringIndexOutOfBoundsException("Unmatched parentheses");
				if(index == 0) {
					if(endIndex == s.length() - 2)
						return DefaultBigDecimalMath.root(solve(s.substring(2, endIndex), time), BigDecimal.valueOf(operation + 2));
					else return solve("(" + DefaultBigDecimalMath.root(solve(s.substring(index + 2, endIndex), time), BigDecimal.valueOf(operation + 2)).toPlainString() + ")" + s.substring(endIndex + 1), time);
				} else if(endIndex == s.length() - 1)
					return solve(s.substring(0, index) + "(" + DefaultBigDecimalMath.root(solve(s.substring(index + 2, endIndex), time), BigDecimal.valueOf(operation + 2)).toPlainString() + ")", time);
				else return solve(s.substring(0, index) + "(" + DefaultBigDecimalMath.root(solve(s.substring(index + 2, endIndex), time), BigDecimal.valueOf(operation + 2)).toPlainString() + ")" + s.substring(endIndex + 1), time);
			} else if(s.charAt(index + 1) == '{') {
				int count = 1;
				int endIndex = -1;
				for(int i = index + 2; i < s.length(); i++)
					if(s.charAt(i) == '{')
						count++;
					else if(s.charAt(i) == '}') {
						count--;
						if(count == 0) {
							endIndex = i;
							break;
						}
					} 
				if(endIndex == -1)
					throw new StringIndexOutOfBoundsException("Unmatched braces");
				if(index == 0) {
					if(endIndex == s.length() - 2)
						return DefaultBigDecimalMath.root(solve(s.substring(2, endIndex), time), BigDecimal.valueOf(operation + 2));
					else return solve("(" + DefaultBigDecimalMath.root(solve(s.substring(index + 2, endIndex), time), BigDecimal.valueOf(operation + 2)).toPlainString() + ")" + s.substring(endIndex + 1), time);
				} else if(endIndex == s.length() - 1)
					return solve(s.substring(0, index) + "(" + DefaultBigDecimalMath.root(solve(s.substring(index + 2, endIndex), time), BigDecimal.valueOf(operation + 2)).toPlainString() + ")", time);
				else return solve(s.substring(0, index) + "(" + DefaultBigDecimalMath.root(solve(s.substring(index + 2, endIndex), time), BigDecimal.valueOf(operation + 2)).toPlainString() + ")" + s.substring(endIndex + 1), time);
			} else if(s.charAt(index + 1) == '-')
				throw new ArithmeticException("Illegal root(x) for x < 0");
			else throw new NullPointerException("For √(x), value x could not be found");
		}
		
		//solve exponents
		while(s.contains("^")) {
			int operatorIndex = s.lastIndexOf('^');
			if(operatorIndex == 0)
				throw new NullPointerException("For a^b, value a could not be found"); // is missing parameter
			else if(operatorIndex == s.length() - 1)
				throw new NullPointerException("For a^b, value b could not be found");
				
			int leftIndex = 0, rightIndex = s.length() - 1;
			BigDecimal a = null, b = null;
				
			//parse the left parameter
			if(s.charAt(operatorIndex - 1) == '}') {
				String left = s.substring(0, operatorIndex);
				if(!left.contains("{"))
					throw new StringIndexOutOfBoundsException("Unmatched braces");
				leftIndex = left.lastIndexOf('{');
				a = BigDecimalMath.toBigDecimal(s.substring(leftIndex + 1, operatorIndex - 1));
			} else {
				String[] entities = s.substring(0, operatorIndex).split("(?<=[-+*×/÷%^])|(?=[-+*×/÷%^])");
				leftIndex = operatorIndex - entities[entities.length - 1].length();
				a = BigDecimalMath.toBigDecimal(entities[entities.length - 1]);
			}
				
			boolean integer = true;
			int c = 0;
			try {
				String right = s.substring(operatorIndex + 1).split("(?=[-+*×/÷%^])")[0];
				rightIndex = operatorIndex + right.length();
				b = BigDecimalMath.toBigDecimal(right);
				if(b.compareTo(BigDecimal.ZERO) <= 0)
					throw new ArithmeticException("java.math.BigDecimal.pow(int exponent) does not support negative exponents");
				c = b.toBigIntegerExact().intValueExact();
			} catch(ArithmeticException ae) {
				integer = false;
			}
				
			if(integer) {
				if(c == 0)
					throw new NullPointerException("For a^b, value b could not be found");
			} else if(b == null)
				throw new NullPointerException("For a^b, value b could not be found");
			
				
			if(a.compareTo(BigDecimal.ZERO) == 0 && b.compareTo(BigDecimal.ZERO) == 0) {
				throw new ArithmeticException("0^0 is indeterminate");
			}
				
					// parse the right parameter
			if(leftIndex == 0) {
				if(rightIndex == s.length() - 1) {
					if(integer) {
						return a.pow(c);
					} else {
						return DefaultBigDecimalMath.pow(a, b);
					}
				} else if(integer) {
					s = a.pow(c).toPlainString() + s.substring(rightIndex + 1);
				} else {
					s = DefaultBigDecimalMath.pow(a, b).toPlainString() + s.substring(rightIndex + 1);
				}
			} else if(rightIndex == s.length() - 1) {
				if(integer) {
					s = s.substring(0, leftIndex) + a.pow(c).toPlainString();
				} else {
					s = s.substring(0, leftIndex) + DefaultBigDecimalMath.pow(a, b).toPlainString();
				}
			} else if(integer) {
				s = s.substring(0, leftIndex) + a.pow(c).toPlainString() + s.substring(rightIndex + 1);
			} else {
				s = s.substring(0, leftIndex) + DefaultBigDecimalMath.pow(a, b).toPlainString() + s.substring(rightIndex + 1);
			}
		}
		
		//System.out.println(System.currentTimeMillis() - time + ": Exponents resolved " + s);
		
		
		if(s.contains("+-"))
			s = s.replaceAll("\\+-", "-");
		if(s.contains("-+"))
			s = s.replaceAll("-\\+", "-");
		if(s.contains("--"))
			s = s.replaceAll("--", "\\+");
		if(s.contains("++"))
			s = s.replaceAll("\\++", "\\+");
		
		if(s.contains("×"))
			s = s.replaceAll("×", "*");
		if(s.contains("⋅"))
			s = s.replaceAll("⋅", "*");
		if(s.contains("÷"))
			s = s.replaceAll("÷", "/");
		
		//work left to right solving multiplication and division
		while(s.contains("*") || s.contains("/") || s.contains("%")) {
			String[] entities = s.split("(?<=[-+*/%])|(?=[-+*/%])");
			boolean twoForward = false;
			int i = 0;
			for(; i < entities.length; i++)
				if(entities[i].equals("*")) {
					
					if(i == 0)
						throw new NullPointerException("For a*b, value a could not be found");
					else if(i == entities.length - 1)
						throw new NullPointerException("For a*b, value b could not be found");
					
					if(i < entities.length - 2) {
						if(entities[i + 1].equals("-")) {
							entities[i] = BigDecimalMath.toBigDecimal(entities[i - 1]).multiply(BigDecimalMath.toBigDecimal("-" + entities[i + 2])).toPlainString();
							twoForward = true;
						} else if(entities[i + 1].equals("+")) {
							entities[i] = BigDecimalMath.toBigDecimal(entities[i - 1]).multiply(BigDecimalMath.toBigDecimal(entities[i + 2])).toPlainString();
							twoForward = true;
						} else entities[i] = BigDecimalMath.toBigDecimal(entities[i - 1]).multiply(BigDecimalMath.toBigDecimal(entities[i + 1])).toPlainString();
					} else entities[i] = BigDecimalMath.toBigDecimal(entities[i - 1]).multiply(BigDecimalMath.toBigDecimal(entities[i + 1])).toPlainString();
					break;
				} else if(entities[i].equals("/")) {
					if(i == 0)
						throw new NullPointerException("For a/b, value a could not be found");
					else if(i == entities.length - 1)
						throw new NullPointerException("For a/b, value b could not be found");
					
					if(i < entities.length - 2) {
						if(entities[i + 1].equals("-")) {
							entities[i] = BigDecimalMath.toBigDecimal(entities[i - 1]).divide(BigDecimalMath.toBigDecimal("-" + entities[i + 2]), MathContext.DECIMAL128).toPlainString();
							twoForward = true;
						} else if(entities[i + 1].equals("+")) {
							entities[i] = BigDecimalMath.toBigDecimal(entities[i - 1]).divide(BigDecimalMath.toBigDecimal(entities[i + 2]), MathContext.DECIMAL128).toPlainString();
							twoForward = true;
						} else entities[i] = BigDecimalMath.toBigDecimal(entities[i - 1]).divide(BigDecimalMath.toBigDecimal(entities[i + 1]), MathContext.DECIMAL128).toPlainString();
					} else entities[i] = BigDecimalMath.toBigDecimal(entities[i - 1]).divide(BigDecimalMath.toBigDecimal(entities[i + 1]), MathContext.DECIMAL128).toPlainString();
					break;
				} else if(entities[i].equals("%")) {
					if(i == 0)
						throw new NullPointerException("For a%b, value a could not be found");
					else if(i == entities.length - 1)
						throw new NullPointerException("For a%b, value b could not be found");
					
					if(i < entities.length - 2) {
						if(entities[i + 1].equals("-")) {
							entities[i] = BigDecimalMath.toBigDecimal(entities[i - 1]).remainder(BigDecimalMath.toBigDecimal("-" + entities[i + 2])).toPlainString();
							twoForward = true;
						} else if(entities[i + 1].equals("+")) {
							entities[i] = BigDecimalMath.toBigDecimal(entities[i - 1]).remainder(BigDecimalMath.toBigDecimal(entities[i + 2])).toPlainString();
							twoForward = true;
						}
					} else entities[i] = BigDecimalMath.toBigDecimal(entities[i - 1]).remainder(BigDecimalMath.toBigDecimal(entities[i + 1])).toPlainString();
					break;
				}
			StringBuilder newS = new StringBuilder();
			for(int x = 0; x < entities.length; x++)
				if(x + 1 != i && x - 1 != i && (!twoForward || x - 2 != i))
					newS.append(entities[x]);
			s = newS.toString();
		}
		
		//System.out.println(System.currentTimeMillis() - time + ": Multiplication resolved " + s);
		
		//work left to right solving addition and subtraction
		
		A: while(s.contains("+") || s.contains("-")) {
			if(s.contains("+-"))
				s = s.replaceAll("\\+-", "-");
			else if(s.contains("-+"))
				s = s.replaceAll("-\\+", "-");
			else if(s.contains("--"))
				s = s.replaceAll("--", "\\+");
			else if(s.contains("++"))
				s = s.replaceAll("\\++", "\\+");
			
			else {
				String[] entities = s.split("(?<=[-+])|(?=[-+])");
				for(int i = 0; i < entities.length; i++) {
					
					if(entities[i].equals("+")) {
						if(i == 0) {
							entities[i] = ""; //remove + prefix
							break;
						} else if(i == entities.length - 1)
							throw new NullPointerException("For a+b, value b could not be found");
						
						if(i > 1 && entities[i - 2].equals("-")) {
								entities[i] = BigDecimalMath.toBigDecimal("-" + entities[i - 1]).add(BigDecimalMath.toBigDecimal(entities[i + 1])).toPlainString();
								entities[i - 2] = entities[i - 1] = entities[i + 1] = "";
							} else {
								entities[i] = BigDecimalMath.toBigDecimal(entities[i - 1]).add(BigDecimalMath.toBigDecimal(entities[i + 1])).toPlainString();
								entities[i - 1] = entities[i + 1] = "";
							}
						break;
						
					} else if(entities[i].equals("-"))
						if(i > 0) {
							if(i == entities.length - 1)
								throw new NullPointerException("For a-b, value b could not be found");
	
							if(i > 1 && entities[i - 2].equals("-")) {
								entities[i] = BigDecimalMath.toBigDecimal("-" + entities[i - 1]).subtract(BigDecimalMath.toBigDecimal(entities[i + 1])).toPlainString();
								entities[i - 2] = entities[i - 1] = entities[i + 1] = "";
							} else {
								entities[i] = BigDecimalMath.toBigDecimal(entities[i - 1]).subtract(BigDecimalMath.toBigDecimal(entities[i + 1])).toPlainString();
								entities[i - 1] = entities[i + 1] = "";
							}
							break;
						} else if(entities.length == 2)
							break A;
				}
				StringBuilder newS = new StringBuilder();
				for(int i = 0; i < entities.length; i++)
					newS.append(entities[i]);
				s = newS.toString();
			}
		}
		
		//System.out.println(System.currentTimeMillis() - time + ": Addition resolved " + s);
		
		return BigDecimalMath.toBigDecimal(s);
	}
	
	private BigDecimal random(BigDecimal x) {
		return x.multiply(BigDecimal.valueOf(Math.random()));
	}

	private BigDecimal round(BigDecimal x) {
		return new BigDecimal(x.add(BigDecimal.valueOf(0.5)).toBigInteger());
	}

	private BigDecimal absolute(BigDecimal x) {
		if(x.compareTo(BigDecimal.ZERO) < 0)
			return x.negate();
		return x;
	}

	private BigDecimal csc(BigDecimal x) {
		return DefaultBigDecimalMath.reciprocal(DefaultBigDecimalMath.sin(x));
	}
	
	private BigDecimal sec(BigDecimal x) {
		return DefaultBigDecimalMath.reciprocal(DefaultBigDecimalMath.cos(x));
	}
	
	private BigDecimal cot(BigDecimal x) {
		return DefaultBigDecimalMath.reciprocal(DefaultBigDecimalMath.tan(x));
	}
	
	private BigDecimal acsc(BigDecimal x) {
		return DefaultBigDecimalMath.asin(DefaultBigDecimalMath.reciprocal(x));
	}

	private BigDecimal asec(BigDecimal x) {
		return DefaultBigDecimalMath.acos(DefaultBigDecimalMath.reciprocal(x));
	}
	
	private BigDecimal acot(BigDecimal x) {
		if(x.compareTo(BigDecimal.ZERO) == 0)
			return DefaultBigDecimalMath.pi().divide(BigDecimal.valueOf(2), DefaultBigDecimalMath.currentMathContext());
		else if(x.compareTo(BigDecimal.ZERO) > 0) {
			return DefaultBigDecimalMath.pi().divide(BigDecimal.valueOf(2), DefaultBigDecimalMath.currentMathContext()).subtract(DefaultBigDecimalMath.atan(x));
		} else return DefaultBigDecimalMath.pi().divide(BigDecimal.valueOf(2), DefaultBigDecimalMath.currentMathContext()).negate().subtract(DefaultBigDecimalMath.atan(x));
	}
	
	private BigDecimal csch(BigDecimal x) {
		return DefaultBigDecimalMath.reciprocal(DefaultBigDecimalMath.sinh(x));
	}
	
	private BigDecimal sech(BigDecimal x) {
		return DefaultBigDecimalMath.reciprocal(DefaultBigDecimalMath.cosh(x));
	}
	
	private BigDecimal coth(BigDecimal x) {
		return DefaultBigDecimalMath.reciprocal(DefaultBigDecimalMath.tanh(x));
	}
	
	private int smallestGreaterThanOne(int[] array) {
		int smallest = Integer.MAX_VALUE;
		int result = 0;
		for(int i = 0; i < array.length; i++)
			if(array[i] > -1 && array[i] < smallest) {
				smallest = array[i];
				result = i;
			}
		return result;
	}

	private BigDecimal fibonacci(BigDecimal n) throws TimeoutException {
		try {
			BigInteger a = n.toBigIntegerExact();
			if(a.compareTo(BigInteger.valueOf(-10000000)) < 0)
				throw new TimeoutException("Restricted fib(n) for n < -10000000: n = " + a);
			if(a.compareTo(BigInteger.valueOf(10000000)) > 0)
				throw new TimeoutException("Restricted fib(n) for n > 10000000: n = " + a);
			if(a.compareTo(BigInteger.ZERO) < 0) {
				if(a.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO) ) {
					if(a.compareTo(BigInteger.valueOf(-400)) >= 0)
						return new BigDecimal(Algorithms.getNthFibonacciNumberC(-n.longValue())).negate();
					else return new BigDecimal(Algorithms.getNthFibonacciNumberF((int) -n.longValue())).negate();
				} else {
					if(a.compareTo(BigInteger.valueOf(-400)) >= 0)
						return new BigDecimal(Algorithms.getNthFibonacciNumberC(-n.longValue()));
					else return new BigDecimal(Algorithms.getNthFibonacciNumberF((int) -n.longValue()));
				}
			}
			if(a.compareTo(BigInteger.valueOf(400)) <= 0)
				return new BigDecimal(Algorithms.getNthFibonacciNumberC(n.longValue()));
			else return new BigDecimal(Algorithms.getNthFibonacciNumberF((int) n.longValue()));
		} catch(ArithmeticException ae) {
			if(n.compareTo(BigDecimal.ZERO) < 0)
				throw new ArithmeticException("Illegal fib(n) for non-integer n < 0: n = " + n);
			if(n.compareTo(BigDecimal.valueOf(-10000000)) < 0)
				throw new TimeoutException("Restricted fib(n) for n < -10000000: n = " + n);
			if(n.compareTo(BigDecimal.valueOf(10000000)) > 0)
				throw new TimeoutException("Restricted fib(n) for n > 10000000: n = " + n);
			return Algorithms.getNthFibonacciNumberG(n, PHI);
		}
	}

	private BigDecimal fibonacciInverse(BigDecimal fib) {
		if(fib.compareTo(BigDecimal.ONE) < 0)
			throw new ArithmeticException("Illegal afib(f) for f < 1: f = " + fib);
		if(BigDecimalMath.isLongValue(fib) && Algorithms.isFibonacci(fib.longValue())) {
			return round(log(PHI, fib.multiply(DefaultBigDecimalMath.sqrt(BigDecimal.valueOf(5)))));
		}
		try {
			if(Algorithms.isFibonacci(fib.toBigIntegerExact()))
				return round(log(PHI, fib.multiply(DefaultBigDecimalMath.sqrt(BigDecimal.valueOf(5)))));
		} catch(ArithmeticException ae) {}
		return log(PHI, fib.multiply(DefaultBigDecimalMath.sqrt(BigDecimal.valueOf(5))));
	}

	
//	private static String format(BigDecimal x) {
//	    NumberFormat formatter = new DecimalFormat("0.0E0");
//	    formatter.setRoundingMode(RoundingMode.HALF_UP);
//	    formatter.setMinimumFractionDigits((x.scale() > 0) ? x.precision() : x.scale());
//	    return formatter.format(x);
//	}
	
	private static String format(BigDecimal x, int scale) {
		NumberFormat formatter = new DecimalFormat("0.0E0");
		formatter.setRoundingMode(RoundingMode.HALF_UP);
		formatter.setMinimumFractionDigits(scale);
		return formatter.format(x);
	}
	
	private static String toScientificString(String s) {
		int index = s.indexOf('E');
		if(index > 0) {
			if(s.charAt(index + 1)  == '-')
				return s.substring(0, index) + " × 10^(" + s.substring(index + 1) + ")";
			else return s.substring(0, index) + " × 10^" + s.substring(index + 1);
		} else return s;
	}
	
	private static BigDecimal log(BigDecimal b, BigDecimal x) {
		try {
			BigInteger base = b.toBigIntegerExact();
			if(base.compareTo(BigInteger.valueOf(2)) == 0)
				return DefaultBigDecimalMath.log2(x);
			else if(base.compareTo(BigInteger.TEN) == 0)
				return DefaultBigDecimalMath.log10(x);
		} catch(ArithmeticException ae) {
			if(!ae.getMessage().equals("Rounding necessary"))
				throw ae;
		}
		return log(b, x, DefaultBigDecimalMath.currentMathContext());
	}
	
	private static BigDecimal log(BigDecimal b, BigDecimal x, MathContext mc) {
		return DefaultBigDecimalMath.log(x).divide(DefaultBigDecimalMath.log(b), mc);
	}
	
	public static String factorizationToString(HashMap<BigInteger, BigInteger> factors) {
		boolean first = true;
		Map<BigInteger, BigInteger> map = factors;
		Object[] keys = map.keySet().toArray();
		Arrays.sort(keys);
		StringBuilder s = new StringBuilder();
		for(Object key : keys) {
			if(!first) {
				s.append(" × ");
			} else first = false;
			s.append(key);
			if(!map.get(key).equals(BigInteger.ONE))
				s.append("^" + map.get(key));
		}
		return s.toString();
	}
	
	public static int findEndParenthesis(String s, int start) {
		int count = 1;
		for(int i = start + 1; i < s.length(); i++)
			if(s.charAt(i) == '(')
				count++;
			else if(s.charAt(i) == ')') {
				count--;
				if(count == 0)
					return i;
			}
		return -1;
	}
}
