package algorithms;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;

import ch.obermuhlner.math.big.BigDecimalMath;

public class Conversion {
	
	public static String numberToEnglish(BigDecimal num) {
		
		if(num.compareTo(BigDecimal.ZERO) == 0)
			return "Zero";
		
		StringBuilder result = new StringBuilder();
		
		if(num.compareTo(BigDecimal.ZERO) < 0) {
			result.append("Negative ");
			num = num.negate();
		}
		
		BigInteger integral = BigDecimalMath.integralPart(num).toBigInteger();
		BigDecimal fractional = BigDecimalMath.fractionalPart(num);
		
		boolean hasIntegral;
		if(hasIntegral = integral.compareTo(BigInteger.ZERO) > 0) {
			String whole = integral.toString();
			int length = whole.length();
			for(int i = 0; i < length; i++) {
				//find whole nuber
				char c = whole.charAt(length - i);			//[9876543210] index
				if(i % 3 != 1) { //ones or hundreds place	//[0210210210] index % 3
					if(c == '1') result.append("one ");
					else if(c == '2') result.append("two ");
					else if(c == '3') result.append("three ");
					else if(c == '4') result.append("four ");
					else if(c == '5') result.append("five ");
					else if(c == '6') result.append("six ");
					else if(c == '7') result.append("seven ");
					else if(c == '8') result.append("eight ");
					else if(c == '9') result.append("nine ");
				} else { //tens place
					if(c == '1') {
						char d = whole.charAt(length - ++i);
						if(d == '1') result.append("eleven ");
						else if(d == '2') result.append("twelve ");
						else if(d == '3') result.append("thirteen ");
						else if(d == '4') result.append("fourteen ");
						else if(d == '5') result.append("fifteen ");
						else if(d == '6') result.append("sixteen ");
						else if(d == '7') result.append("seventeen ");
						else if(d == '8') result.append("eighteen ");
						else if(d == '9') result.append("nineteen ");
						else //if(d == '0') 
							result.append("ten ");
					}
					else if(c == '2') result.append("twenty ");
					else if(c == '3') result.append("thirty ");
					else if(c == '4') result.append("forty ");
					else if(c == '5') result.append("fifty ");
					else if(c == '6') result.append("sixty ");
					else if(c == '7') result.append("seventy ");
					else if(c == '8') result.append("eighty ");
					else if(c == '9') result.append("ninety ");
				}
				if(i % 3 == 2) { //hundreds 
					if(c != '0') result.append("hundred ");
				} else if(i % 3 == 0 && i > 0) {
					if(c != '0' || (length - i - 1 > 0 && whole.charAt(length - i - 1) != '0') 
							|| (length - i - 2 > 0 && whole.charAt(length - i - 2) != '0')) {
						if(i == 4)
							result.append("thousand ");
						else if(i == 7)
							result.append("million ");
						else if(i == 10)
							result.append("billion ");
						else if(i == 13)
							result.append("trillion ");
						else if(i == 16)
							result.append("quadrillion ");
						else if(i == 19)
							result.append("quintillion ");
						else if(i == 22)
							result.append("sextillion ");
						else if(i == 25)
							result.append("septillion ");
						else if(i == 28)
							result.append("octillion ");
						else if(i == 31)
							result.append("nonillion ");
						else if(i == 34)
							result.append("decillion ");
						else if(i == 37)
							result.append("undecillion ");
						else if(i == 40)
							result.append("duodecillion ");
						else if(i == 43)
							result.append("tredecillion ");
						else if(i == 46)
							result.append("quattuordecillion ");
						else if(i == 49)
							result.append("quindecillion ");
						else if(i == 52)
							result.append("sedecillion ");
						else if(i == 55)
							result.append("septendecillion ");
						else if(i == 58)
							result.append("octodecillion ");
						else if(i == 61)
							result.append("novendecillion ");
						else if(i == 64)
							result.append("vigintillion ");
						else if(i == 67)
							result.append("unvigintillion ");
						else if(i == 70)
							result.append("duovigintillion ");
						else if(i == 73)
							result.append("tresvigintillion ");
						else if(i == 76)
							result.append("quattuorvigintillion ");
						else if(i == 79)
							result.append("quinvigintillion ");
						else if(i == 82)
							result.append("sesvigintillion ");
						else if(i == 85)
							result.append("septemvigintillion ");
						else if(i == 88)
							result.append("octovigintillion ");
						else if(i == 91)
							result.append("novemvigintillion ");
						else if(i == 94)
							result.append("trigintillion ");
						else if(i == 97)
							result.append("untrigintillion ");
						else if(i == 100)
							result.append("duotrigintilllion ");
						else if(i == 103)
							result.append("trestrigintillion ");
						else if(i == 106)
							result.append("quattuortrigintillion ");
						else if(i == 109)
							result.append("quinquatrigintillion ");
						else if(i == 112)
							result.append("sestrigintillion ");
						else if(i == 115)
							result.append("septentrigintillion ");
						else if(i == 118)
							result.append("octotrigintillion ");
						else if(i == 121)
							result.append("noventrigintillion ");
						else if(i == 124)
							result.append("quadragintillion ");
						else if(i == 127)
							result.append("unquadragintillion ");
						else if(i == 130)
							result.append("duoquadragintilllion ");
						else if(i == 133)
							result.append("tresquadragintillion ");
						else if(i == 136)
							result.append("quattuorquadragintillion ");
						else if(i == 139)
							result.append("quinquaquadragintillion ");
						else if(i == 142)
							result.append("sesquadragintillion ");
						else if(i == 145)
							result.append("septenquadragintillion ");
						else if(i == 148)
							result.append("octoquadragintillion ");
						else if(i == 151)
							result.append("novenquadragintillion ");
						else if(i == 304)
							result.append("centillion ");
					}
				}
			}
		}
		
		if(fractional.compareTo(BigDecimal.ZERO) > 0) {
			if(hasIntegral)
				result.append(" and ");
			//find decimal expansion
		}
		
		//System.out.println(num);
		System.out.println(integral);
		System.out.println(fractional);
		
		return null;
	}
	
	/**
	 * Converts a given Roman numeral into its integer form.
	 * 
	 * @param s
	 * @return integer
	 */
	public static int romanToInt(String s) {
		s = s.replaceAll(" +", "").toUpperCase();
		if(!s.matches("^[\\u0305CDILMXVX]+$"))
			throw new NumberFormatException("Contains non-Roman numeral characters");
        int result = 0;
        if(s.contains("\u0305")) {
        	result += romanToInt(s.substring(0, s.lastIndexOf('\u0305')).replaceAll("\u0305", "")) * 1000;
        	s = s.substring(s.lastIndexOf('\u0305') + 1);
        }
        while(s.startsWith("M")) {
            result += 1000;
            s = s.substring(1);
        }
        if(s.startsWith("CM")) {
            result += 900;
            s = s.substring(2);
        }
        while(s.startsWith("D")) {
            result += 500;
            s = s.substring(1);
        }
        if(s.startsWith("CD")) {
            result += 400;
            s = s.substring(2);
        }
        while(s.startsWith("C")) {
            result += 100;
            s = s.substring(1);
        }
        if(s.startsWith("XC")) {
            result += 90;
            s = s.substring(2);
        }
        while(s.startsWith("L")) {
            result += 50;
            s = s.substring(1);
        }
        if(s.startsWith("XL")) {
            result += 40;
            s = s.substring(2);
        }
        while(s.startsWith("X")) {
            result += 10;
            s = s.substring(1);
        }
        if(s.startsWith("IX"))
            return result + 9;
        if(s.startsWith("V")) {
            result += 5;
            s = s.substring(1);
        } 
        if(s.startsWith("IV"))
            return result + 4;
        while(s.startsWith("I")) {
            result += 1;
            s = s.substring(1);
        }    
        return result;
    }
	
	/**
	 * Converts a given integer into its Roman Numeral form.
	 * Only works as intended up to the value 5,000,000.
	 * 
	 * @param num
	 * @return Roman Numeral
	 */
	public static String intToRoman(int num) {
		if(num >= 5000000)
			throw new NumberFormatException("Values 5000000 or greater cannot be represented in Roman numerals");
		if(num <= 0)
			throw new NumberFormatException("Values 0 or less cannot be represented in Roman numerals");
		StringBuilder result = new StringBuilder();
		if(num >= 5000) {
			int a = num / 5000 * 5;
			result.append(intToRoman(a).replace("", "\u0305").substring(1));
			num -= a * 1000;
		}
		while(num >= 1000) {
		    result.append('M');
		    num -= 1000;
		} 
		if(num >= 900) {
		    result.append("CM");
		    num -= 900;
		}
		if(num >= 500) {
		    result.append('D');
		    num -= 500;
		}
		if(num >= 400) {
		    result.append("CD");
		    num -= 400;
		}
		while(num >= 100) {
		    result.append('C');
		    num -= 100;
		}
		if(num >= 90) {
		    result.append("XC");
		    num -= 90;
		}
		if(num >= 50) {
		    result.append('L');
		    num -= 50;
		}
		if(num >= 40) {
		    result.append("XL");
		    num -= 40;
		}
		while(num >= 10) {
		    result.append('X');
		    num -= 10;
		}
		if(num == 9)
		    return new String(result.append("IX"));
		if(num >= 5) {
		    result.append('V');
		    num -= 5;
		}
		if(num == 4)
		    return new String(result.append("IV"));
		while(num > 0) {
		    result.append('I');
		    num--;
		}
		return new String(result);
	}
	
	/**
	 * Determines if a value contains Chinese characters.
	 * 
	 * @param s
	 * @return boolean
	 */
	public static boolean containsHanScript(String s) {
	    return s.codePoints().anyMatch(
	            codepoint ->
	            Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HAN);
	}
	
	/**
	 * Converts a given String of Chinese numerals to Arabic Numerals
	 * 
	 * @param s
	 * @return
	 */
	public static long chineseToLong(String s) {
		s = s.replaceAll(" +", "");
		if(s.contains("〇"))
			s = s.replaceAll("〇", "零");
		if(s.contains("壱"))
			s = s.replaceAll("壱", "一");
		if(s.contains("壹"))
			s = s.replaceAll("壹", "一");
		if(s.contains("两"))
			s = s.replaceAll("两", "二");
		if(s.contains("弐"))
			s = s.replaceAll("弐", "二");
		if(s.contains("贰"))
			s = s.replaceAll("贰", "二");
		if(s.contains("貳"))
			s = s.replaceAll("貳", "二");
		if(s.contains("兩"))
			s = s.replaceAll("兩", "二");
		if(s.contains("参"))
			s = s.replaceAll("参", "三");
		if(s.contains("叁"))
			s = s.replaceAll("叁", "三");
		if(s.contains("參"))
			s = s.replaceAll("參", "三");
		if(s.contains("肆"))
			s = s.replaceAll("肆", "四");
		if(s.contains("伍"))
			s = s.replaceAll("伍", "五");
		if(s.contains("陆"))
			s = s.replaceAll("陆", "六");
		if(s.contains("陸"))
			s = s.replaceAll("陸", "六");
		if(s.contains("柒"))
			s = s.replaceAll("柒", "七");
		if(s.contains("漆"))
			s = s.replaceAll("漆", "七");
		if(s.contains("捌"))
			s = s.replaceAll("捌", "八");
		if(s.contains("玖"))
			s = s.replaceAll("玖", "九");
		if(s.contains("拾"))
			s = s.replaceAll("拾", "十");
		if(s.contains("佰"))
			s = s.replaceAll("佰", "百");
		if(s.contains("阡"))
			s = s.replaceAll("阡", "千");
		if(s.contains("仟"))
			s = s.replaceAll("仟", "千");
		if(s.contains("萬"))
			s = s.replaceAll("萬", "万");
		if(s.contains("亿"))
			s = s.replaceAll("亿", "億");
		if(!s.matches("^[負零一二三四五六七八九十百千万億兆京]+$"))
			throw new NumberFormatException("Contains non-Chinese numeral characters");
		if(s.equals("零"))
			return 0;
		else if(s.contains("零"))
			s = s.replaceAll("零", "");
		boolean negative = false;
		if(s.contains("負")) {
			negative = true;
			s = s.replaceAll("負", "");
		}
		long num = 0;
		if(s.contains("京")) {
			int index = s.indexOf("京");
			if(index == 0) {
				num += 10_000_000_000_000_000L;
			} else {
				num += chineseToLong(s.substring(0, index)) * 10_000_000_000_000_000L;
			}
			s = s.substring(index + 1);
		}
		if(s.contains("兆")) {
			int index = s.indexOf("兆");
			if(index == 0) {
				num += 1_000_000_000_000L;
			} else {
				num += chineseToLong(s.substring(0, index)) * 1_000_000_000_000L;
			}
			s = s.substring(index + 1);
		}
		if(s.contains("億")) {
			int index = s.indexOf("億");
			if(index == 0) {
				num += 100_000_000;
			} else {
				num += chineseToLong(s.substring(0, index)) * 100_000_000;
			}
			s = s.substring(index + 1);
		}
		if(s.contains("万")) {
			int index = s.indexOf("万");
			if(index == 0) {
				num += 10_000;
			} else {
				num += chineseToLong(s.substring(0, index)) * 10_000;
			}
			s = s.substring(index + 1);
		}
		if(s.contains("千")) {
			int index = s.indexOf("千");
			if(index == 0) {
				num += 1000;
			} else {
				num += chineseToLong(s.substring(0, index)) * 1000;
			}
			s = s.substring(index + 1);
		}
		if(s.contains("百")) {
			int index = s.indexOf("百");
			if(index == 0) {
				num += 100;
			} else {
				num += chineseToLong(s.substring(0, index)) * 100;
			}
			s = s.substring(index + 1);
		}
		if(s.contains("十")) {
			int index = s.indexOf("十");
			if(index == 0) {
				num += 10;
			} else {
				num += chineseToLong(s.substring(0, index)) * 10;
			}
			s = s.substring(index + 1);
		}
		if(s.equals("九")) {
			if(negative)
				return -(num + 9);
			return num + 9;
		} else if(s.equals("八")) {
			if(negative)
				return -(num + 8);
			return num + 8;
		} else if(s.equals("七")) {
			if(negative)
				return -(num + 7);
			return num + 7;
		} else if(s.equals("六")) {
			if(negative)
				return -(num + 6);
			return num + 6;
		} else if(s.equals("五")) {
			if(negative)
				return -(num + 5);
			return num + 5;
		} else if(s.equals("四")) {
			if(negative)
				return -(num + 4);
			return num + 4;
		} else if(s.equals("三")) {
			if(negative)
				return -(num + 3);
			return num + 3;
		} else if(s.equals("二")) {
			if(negative)
				return -(num + 2);
			return num + 2;
		} else if(s.equals("一")) {
			if(negative)
				return -(num + 1);
			return num + 1;
		} else {
			if(negative)
				return -num;
			return num;
		}
	}
	
	/**
	 * Converts a given Arabic Numeral into Simplified Chinese, or Japanese.
	 * 
	 * @param num
	 * @param japanese
	 * @return
	 */
	public static String longToChinese(long num, boolean japanese) {
		if(num == 0)
			return "零";
		StringBuilder s = new StringBuilder();
		if(num < 0) {
			if(num == Long.MIN_VALUE)
				throw new NumberFormatException("Numbers less than -9,223,372,036,854,775,807 are not supported");
			s.append('負');
			num = -num;
		}
		if(num >= 10_000_000_000_000_000L) {
			int count = (int) (num / 10_000_000_000_000_000L);
			num %= 10_000_000_000_000_000L;
			if(japanese && count == 1)
				s.append('京');
			else s.append(longToChinese(count, japanese) + "京");
		}
		if(num >= 1_000_000_000_000L) {
			int count = (int) (num / 1_000_000_000_000L);
			num %= 1_000_000_000_000L;
			if(japanese && count == 1)
				s.append('兆');
			else s.append(longToChinese(count, japanese) + "兆");
		} else if(!japanese && s.toString().endsWith("京"))
			s.append("零");
		if(num >= 100_000_000) {
			int count = (int) (num / 100_000_000);
			num %= 100_000_000;
			if(japanese && count == 1)
				s.append('億');
			else if(japanese)
				s.append(longToChinese(count, japanese) + "億");
			else s.append(longToChinese(count, japanese) + "亿");
		} else if(!japanese && s.toString().endsWith("兆"))
			s.append("零");
		if(num >= 10_000) {
			int count = (int) (num / 10_000);
			num %= 10_000;
			if(japanese && count == 1)
				s.append('万');
			else s.append(longToChinese(count, japanese) + "万");
		} else if(!japanese && s.toString().endsWith("兆"))
			s.append("零");
		if(num >= 1000) {
			int count = (int) (num / 1000);
			num %= 1000;
			if(japanese && count == 1)
				s.append('千');
			else s.append(longToChinese(count, japanese) + "千");
		} else if(!japanese && s.toString().endsWith("万"))
			s.append("零");
		if(num >= 100) {
			int count = (int) (num / 100);
			num %= 100;
			if(japanese && count == 1)
				s.append('百');
			else s.append(longToChinese(count, japanese) + "百");
		} else if(!japanese && s.toString().endsWith("兆"))
			s.append("零");
		if(num >= 10) {
			int count = (int) (num / 10);
			num %= 10;
			if(count == 1)
				s.append('十');
			else s.append(longToChinese(count, japanese) + "十");
		} else if(!japanese && s.toString().endsWith("百"))
			s.append("零");
		if(num == 9)
			return s.append('九').toString();
		else if(num == 8)
			return s.append('八').toString();
		else if(num == 7)
			return s.append('七').toString();
		else if(num == 6)
			return s.append('六').toString();
		else if(num == 5)
			return s.append('五').toString();
		else if(num == 4)
			return s.append('四').toString();
		else if(num == 3)
			return s.append('三').toString();
		else if(num == 2)
			return s.append('二').toString();
		else if(num == 1) 
			return s.append('一').toString();
		else {
			String str = s.toString();
			if(str.length() > 1 && str.charAt(str.length() - 1) == '零')
				return str.substring(0, str.length() - 1);
			return str;
		}
	}
	
	/**
	 * Takes in a quadratic equation in the form of:
	 * 
	 * 	y = ax^2 + bx + c
	 * 
	 * And returns it in the form of:
	 * 
	 * 	y = a(x - h)^2 + k
	 * 
	 * @param quadratic
	 * @return
	 */
	public static String getVertexForm(String equation) {
		//f(x) = ax^2 + bx + c
		String[] terms = equation.split(" +");
		double a = 0, b = 0, c = 0, y = 0;
		int equal = 0;
		for(int i = 0; i < terms.length; i++)
			if(terms[i].equals("=")) {
				equal = i;
				break;
			}
		int unflip = 1;
		for(int i = 0; i < terms.length; i++) {
			int flip = 1;
			if(i < equal)
				flip = -1;
			if(unflip != 1) {
				flip *= unflip;
				unflip = 1;
			}
			if(terms[i].contains("+") || terms[i].contains("="));
			else if(terms[i].equals("-"))
				unflip = -unflip;
			else if(terms[i].contains("y")) {
				if(!terms[i].startsWith("y")) {
					if(terms[i].startsWith("-")) {
						if(terms[i].length() == 2)
							y -= -flip;
						else y -= -flip * Long.parseLong(terms[i].substring(1, terms[i].indexOf("y")));
					} else y += -flip * Long.parseLong(terms[i].substring(0, terms[i].indexOf("y")));
				} else y += -flip;
			} else if(terms[i].contains("x^2")) {
				if(!terms[i].startsWith("x^2")) {
					if(terms[i].startsWith("-"))
						a -= flip * Long.parseLong(terms[i].substring(1, terms[i].indexOf("x^2")));
					else a += flip * Long.parseLong(terms[i].substring(0, terms[i].indexOf("x^2")));
				} else a += flip;
			} else if(terms[i].contains("x")) {
				if(!terms[i].startsWith("x")) {
					if(terms[i].startsWith("-"))
						b -= flip * Long.parseLong(terms[i].substring(1, terms[i].indexOf("x")));
					else b += flip * Long.parseLong(terms[i].substring(0, terms[i].indexOf("x")));
				} else b += flip;
			} else {
				if(terms[i].startsWith("-"))
					c -= flip * Long.parseLong(terms[i].substring(1));
				else c += flip * Long.parseLong(terms[i]);
			}
		}
		
		DecimalFormat df = new DecimalFormat("#.####");
		//System.out.println(y + "y = " + a + "x^2 + " + b + "x + " + c);
		if(y != 1)
			if(y == -1)
				System.out.print("-");
			else System.out.print(df.format(y));
		System.out.print("y = ");
		if(a != 1)
			if(a == -1)
				System.out.print("-");
			else System.out.print(df.format(a));
		System.out.print("x^2 ");
		if(b < 0)
			System.out.print("- ");
		else System.out.print("+ ");
		if(!(Math.abs(b) == 1.0))
			System.out.print(df.format(Math.abs(b)));
		System.out.print("x ");
		if(c < 0)
			System.out.print("- ");
		else System.out.print("+ ");
		System.out.println(df.format(Math.abs(c)));
		
		c = -c;
		//System.out.println(y + "y + " + c + " = " + a + "x^2 + " + b + "x");
		if(y != 1)
			if(y == -1)
				System.out.print("-");
			else System.out.print(df.format(y));
		System.out.print("y ");
		if(c < 0)
			System.out.print("- ");
		else System.out.print("+ ");
		System.out.print(df.format(Math.abs(c)) + " = ");
		if(a != 1)
			if(a == -1)
				System.out.print("-");
			else System.out.print(df.format(a));
		System.out.print("x^2 ");
		if(b < 0)
			System.out.print("- ");
		else System.out.print("+ ");
		if(!(Math.abs(b) == 1.0))
			System.out.print(df.format(Math.abs(b)));
		System.out.println("x");
		
		double d = b * 1.0 / a;
		//System.out.println(y + "y + " + c + " = " + a + "(x^2 + " + d + ")");
		if(y != 1)
			if(y == -1)
				System.out.print("-");
			else System.out.print(df.format(y));
		System.out.print("y ");
		if(c < 0)
			System.out.print("- ");
		else System.out.print("+ ");
		System.out.print(df.format(Math.abs(c)) + " = ");
		if(a < 0)
			System.out.print("-");
		if(!(Math.abs(a) == 1.0))
			System.out.print(df.format(Math.abs(a)));
		System.out.print("(x^2 ");
		if(d < 0)
			System.out.print("- ");
		else System.out.print("+ ");
		System.out.println(df.format(Math.abs(d)) + "x)");
		
		double square = Math.pow(b / (2.0 * a), 2);
		//System.out.println(y + "y + " + c + " + " + a + " * " + square + " = " + a + "(x^2 + " + d + " + " + square + ")");
		if(y != 1)
			if(y == -1)
				System.out.print("-");
			else System.out.print(df.format(y));
		System.out.print("y ");
		if(c < 0)
			System.out.print("- ");
		else System.out.print("+ ");
		System.out.print(df.format(Math.abs(c)) + " ");
		if(a < 0)
			System.out.print("- ");
		else System.out.print("+ ");
		if(!(Math.abs(a) == 1.0))
			System.out.print(df.format(Math.abs(a)) + "(" + df.format(square) + ") = ");
		if(a < 0)
			System.out.print("-");
		if(!(Math.abs(a) == 1.0))
			System.out.print(df.format(Math.abs(a)));
		System.out.print("(x^2 ");
		if(d < 0)
			System.out.print("- ");
		else System.out.print("+ ");
		System.out.print(df.format(Math.abs(d)) + "x ");
		if(square < 0)
			System.out.print("- ");
		else System.out.print("+ ");
		System.out.println(df.format(Math.abs(square)) + ")");
		
		double h = d / 2, k = -(c + a * square);
		String result = "";
		if(y != 1.0) {
			a /= y;
			k /= y;
			y /= y;
		}
		//System.out.println(y + "y = " + a + "(x + " + h + ")^2 + " + k);
		if(y != 1)
			if(y == -1)
				result += "-";
			else result += df.format(y);
		result += "y = ";
		if(a < 0)
			result += "-";
		if(!(Math.abs(a) == 1.0))
			result += df.format(Math.abs(a));
		result += "(x ";
		if(h < 0)
			result += "- ";
		else result += "+ ";
		result += df.format(Math.abs(h)) + ")^2 ";
		if(k < 0)
			result += "- ";
		else result += "+ ";
		result += df.format(Math.abs(k));
		
		return result;
	}
	
	/**
	 * Takes in a quadratic equation in the form of:
	 * 
	 * 	y = ax^2 + bx + c
	 * 
	 * And returns it in the form of:
	 * 
	 * 	y = a(x - h)^2 + k
	 * 
	 * Along with all intermediate steps.
	 * 
	 * @param quadratic
	 * @return
	 */
	public static String getVertexFormFull(String equation) {
		//System.out.println(equation);
		String[] entities = equation.replaceAll(" +", "").replaceAll("\\+-", "-").replaceAll("-\\+", "-").replaceAll("--", "+").replaceAll("\\++", "+").split("(?<=[-+*/%=])|(?=[-+*/%=])");
		equation = "";
		for(String entity : entities)
		    equation += entity + " ";
		equation = equation.substring(0, equation.length() - 1);
		//System.out.println(equation);
		StringBuilder work = new StringBuilder(equation + "\n");
		//f(x) = ax^2 + bx + c
		String[] terms = equation.split(" +");
		double a = 0, b = 0, c = 0, y = 0;
		int equal = 0;
		for(int i = 0; i < terms.length; i++)
			if(terms[i].equals("=")) {
				equal = i;
				break;
			}
		int unflip = 1;
		for(int i = 0; i < terms.length; i++) {
			int flip = 1;
			if(i < equal)
				flip = -1;
			if(unflip != 1) {
				flip *= unflip;
				unflip = 1;
			}
			if(terms[i].contains("+") || terms[i].contains("="));
			else if(terms[i].equals("-"))
				unflip = -unflip;
			else if(terms[i].contains("y")) {
				if(!terms[i].startsWith("y")) {
					if(terms[i].startsWith("-")) {
						if(terms[i].length() == 2)
							y -= -flip;
						else y -= -flip * Long.parseLong(terms[i].substring(1, terms[i].indexOf("y")));
					} else y += -flip * Long.parseLong(terms[i].substring(0, terms[i].indexOf("y")));
				} else y += -flip;
			} else if(terms[i].contains("x^2")) {
				if(!terms[i].startsWith("x^2")) {
					if(terms[i].startsWith("-"))
						a -= flip * Long.parseLong(terms[i].substring(1, terms[i].indexOf("x^2")));
					else a += flip * Long.parseLong(terms[i].substring(0, terms[i].indexOf("x^2")));
				} else a += flip;
			} else if(terms[i].contains("x")) {
				if(!terms[i].startsWith("x")) {
					if(terms[i].startsWith("-"))
						b -= flip * Long.parseLong(terms[i].substring(1, terms[i].indexOf("x")));
					else b += flip * Long.parseLong(terms[i].substring(0, terms[i].indexOf("x")));
				} else b += flip;
			} else {
				if(terms[i].startsWith("-"))
					c -= flip * Long.parseLong(terms[i].substring(1));
				else c += flip * Long.parseLong(terms[i]);
			}
		}
		
		DecimalFormat df = new DecimalFormat("#.####");
		//System.out.println(y + "y = " + a + "x^2 + " + b + "x + " + c);
		if(y != 1)
			if(y == -1)
				work.append("-");
			else work.append(df.format(y));
		work.append("y = ");
		if(a != 1)
			if(a == -1)
				work.append("-");
			else work.append(df.format(a));
		work.append("x^2 ");
		if(b < 0)
			work.append("- ");
		else work.append("+ ");
		if(!(Math.abs(b) == 1.0))
			work.append(df.format(Math.abs(b)));
		work.append("x ");
		if(c < 0)
			work.append("- ");
		else work.append("+ ");
		work.append(df.format(Math.abs(c)) + "\n");
		
		c = -c;
		//System.out.println(y + "y + " + c + " = " + a + "x^2 + " + b + "x");
		if(y != 1)
			if(y == -1)
				work.append("-");
			else work.append(df.format(y));
		work.append("y ");
		if(c < 0)
			work.append("- ");
		else work.append("+ ");
		work.append(df.format(Math.abs(c)) + " = ");
		if(a != 1)
			if(a == -1)
				work.append("-");
			else work.append(df.format(a));
		work.append("x^2 ");
		if(b < 0)
			work.append("- ");
		else work.append("+ ");
		if(!(Math.abs(b) == 1.0))
			work.append(df.format(Math.abs(b)));
		work.append("x\n");
		
		double d = b * 1.0 / a;
		//System.out.println(y + "y + " + c + " = " + a + "(x^2 + " + d + ")");
		if(y != 1)
			if(y == -1)
				work.append("-");
			else work.append(df.format(y));
		work.append("y ");
		if(c < 0)
			work.append("- ");
		else work.append("+ ");
		work.append(df.format(Math.abs(c)) + " = ");
		if(a < 0)
			work.append("-");
		if(!(Math.abs(a) == 1.0))
			work.append(df.format(Math.abs(a)));
		work.append("(x^2 ");
		if(d < 0)
			work.append("- ");
		else work.append("+ ");
		work.append(df.format(Math.abs(d)) + "x)\n");
		
		double square = Math.pow(b / (2.0 * a), 2);
		//System.out.println(y + "y + " + c + " + " + a + " * " + square + " = " + a + "(x^2 + " + d + " + " + square + ")");
		if(y != 1)
			if(y == -1)
				work.append("-");
			else work.append(df.format(y));
		work.append("y ");
		if(c < 0)
			work.append("- ");
		else work.append("+ ");
		work.append(df.format(Math.abs(c)) + " ");
		if(a < 0)
			work.append("- ");
		else work.append("+ ");
		if(!(Math.abs(a) == 1.0))
			work.append(df.format(Math.abs(a)) + "(" + df.format(square) + ") = ");
		if(a < 0)
			work.append("-");
		if(!(Math.abs(a) == 1.0))
			work.append(df.format(Math.abs(a)));
		work.append("(x^2 ");
		if(d < 0)
			work.append("- ");
		else work.append("+ ");
		work.append(df.format(Math.abs(d)) + "x ");
		if(square < 0)
			work.append("- ");
		else work.append("+ ");
		work.append(df.format(Math.abs(square)) + ")\n");
		
		double h = d / 2, k = -(c + a * square);
		String result = "";
		if(y != 1.0) {
			a /= y;
			k /= y;
			y /= y;
		}
		//System.out.println(y + "y = " + a + "(x + " + h + ")^2 + " + k);
		if(y != 1)
			if(y == -1)
				result += "-";
			else result += df.format(y);
		result += "y = ";
		if(a < 0)
			result += "-";
		if(!(Math.abs(a) == 1.0))
			result += df.format(Math.abs(a));
		result += "(x ";
		if(h < 0)
			result += "- ";
		else result += "+ ";
		result += df.format(Math.abs(h)) + ")^2 ";
		if(k < 0)
			result += "- ";
		else result += "+ ";
		result += df.format(Math.abs(k));
		
		return work.toString() + result;
	}
	
}
