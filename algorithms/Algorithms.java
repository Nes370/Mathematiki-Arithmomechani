package algorithms;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import ch.obermuhlner.math.big.DefaultBigDecimalMath;

public class Algorithms {
	//TODO Drake Equation Denominator
	//TODO Sieve of Eratosthenes, solve for nth Prime
	
	public static String longToChinese(long num) {
		
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
	 * Determines if a given number is a palindrome.
	 * 
	 * @param x
	 * @return is a palindrome
	 */
	public static boolean isPalindrome(int x) {
        if(x < 0)
            return false;
        if(x < 10)
            return true;
        if(x % 10 == 0)
            return false;
        int y = 0;
        while(x > y) {
            y = y * 10 + x % 10;
            x /= 10;
        }
        return x == y || x == y / 10;
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
	 * @param quadratic
	 * @return
	 */
	public static String getVertexFormFull(String equation) {
		System.out.println(equation);
		String[] entities = equation.replaceAll(" +", "").replaceAll("\\+-", "-").replaceAll("-\\+", "-").replaceAll("--", "+").replaceAll("\\++", "+").split("(?<=[-+*/%=])|(?=[-+*/%=])");
		equation = "";
		for(String entity : entities)
		    equation += entity + " ";
		equation = equation.substring(0, equation.length() - 1);
		System.out.println(equation);
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
	
	/**
	 * Estimates the Number of Civilizations in the Universe.
	 * 
	 * @return an estimated number of civilizations
	 */
	public static BigInteger drakeEquation() {
		BigDecimal R_star, //Rate of Star Formation in a galaxy
			f_p, //fraction of stars with planets
			n_e, //number of planets per star with life-suitable environments
			f_e, //fraction of suitable planets that have life
			f_i, //fraction of planets with life that have an emergent intelligent species
			f_c, //fraction of intelligent species that release signs of existence into space
			L; //length of time the intelligent species signals
		R_star = BigDecimal.valueOf((Math.random() * 0.77 + 0.68) * 2); //Estimates: 0.68-1.45 M is created each year, and 2 stars created per M
		f_p = BigDecimal.ONE;
		n_e = BigDecimal.valueOf((Math.random() * 39 + 11) / 100); //Estimates: 40 billion earth-like planets in habitable zones, 11 billion of which have sun-like stars, of 100 billion in universe.
		f_e = BigDecimal.valueOf(Math.random()); //Conflicting estimates
		f_i = BigDecimal.valueOf(Math.random()); //Conflicting estimates
		f_c = BigDecimal.valueOf(Math.random()); //Conflicting estimates
		L = BigDecimal.valueOf(Math.random() * 1000); //Conflicting estimates, 500 to an indefinite number of years is hypothesized
		return R_star.multiply(f_p).multiply(n_e).multiply(f_e).multiply(f_i).multiply(f_c).multiply(L).toBigInteger();
	}
	
	/**
	 * Converts string input into a mapped factorization.
	 * 
	 * @param str
	 * @return map containing factorization
	 */
	public static HashMap<Integer, Integer> getFactorization(String str) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		String[] factors = str.replace('*', ' ').replace('x', ' ').split(" +");
		int index;
		for(String factor : factors)
			if((index = factor.indexOf('^')) > 0)
				map.put(Integer.parseInt(factor.substring(0, index)), Integer.parseInt(factor.substring(index + 1)));
			else map.put(Integer.parseInt(factor), 1);
		return (HashMap<Integer, Integer>) map;
	}
	
	/**
	 * Calculates the product of a factorization and returns it as a BigInteger object.
	 * @param a
	 * @return product of all factors
	 */
	public static BigInteger getProductB(HashMap<Integer, Integer> a) {
		Map<Integer, Integer> map = a;
		BigInteger product = BigInteger.ONE;
		for(Map.Entry<Integer, Integer> entry : map.entrySet())
			product = product.multiply(BigInteger.valueOf(entry.getKey()).pow(entry.getValue()));
		return product;
	}
	
	/**
	 * Calculates the product of a factorization and returns it as a long value.
	 * 
	 * @param a
	 * @return product of all factors
	 */
	public static long getProductA(HashMap<Integer, Integer> a) {
		Map<Integer, Integer> map = a;
		long product = 1;
		for(Map.Entry<Integer, Integer> entry : map.entrySet())
			product *= Math.pow(entry.getKey(), entry.getValue());
		return product;
	}
	
	/**
	 * Finds the least common multiple of two factorizations.
	 * 
	 * @param a
	 * @param b
	 * @return map containing the prime factorization of the LCM
	 */
	public static HashMap<Integer, Integer> leastCommonMultiple(HashMap<Integer, Integer> a, HashMap<Integer, Integer> b) {
		Map<Integer, Integer> x, y;
		x = a;
		y = b;
		for(Map.Entry<Integer, Integer> entry : x.entrySet()) {
			if(y.containsKey(entry.getKey()) && y.get(entry.getKey()) > entry.getValue()) {
				entry.setValue(y.get(entry.getKey()));
			}
		}
		for(Map.Entry<Integer, Integer> entry : y.entrySet()) {
			if(!x.containsKey(entry.getKey())) {
				x.put(entry.getKey(), entry.getValue());
			}
		}
		if(x.containsKey(1) && x.size() > 1)
			x.remove(1);
		return (HashMap<Integer, Integer>) x;
	}
	
	/**
	 * Finds all prime factors of a given number.
	 * 
	 * @param n
	 * @return map containing all primes and their corresponding exponents
	 * @throws TimeoutException 
	 */
	public static HashMap<Integer, Integer> getPrimeFactorization(long n) {
		Map<Integer, Integer> factorization = new HashMap<Integer, Integer>();
		if(n <= 1)
			factorization.put((int) n, 1);
		for(int i = 2; i <= n; i++) {
			if(n % i == 0) {
				if(!factorization.containsKey(i))
					factorization.put(i, 1);
				else factorization.put(i, factorization.get(i) + 1);
				n /= i--;
			}
		}
		return (HashMap<Integer, Integer>) factorization;
	}
	/**
	 * Finds all prime factors of a given number.
	 * 
	 * @param n
	 * @return map containing all primes and their corresponding exponents
	 * @throws TimeoutException 
	 */
	public static HashMap<BigInteger, BigInteger> getPrimeFactorization(BigInteger n) throws TimeoutException {
		Map<BigInteger, BigInteger> factorization = new HashMap<BigInteger, BigInteger>();
		if(n.compareTo(BigInteger.ZERO) < 1) {
			factorization.put(BigInteger.ONE.negate(), BigInteger.ONE);
			n = n.negate();
		} 
		if(n.compareTo(BigInteger.ONE) < 1)
			factorization.put(n, BigInteger.ONE);
		BigInteger max = BigInteger.valueOf(50000000);
		for(BigInteger i = BigInteger.valueOf(2); n.compareTo(i) > -1; i = i.add(BigInteger.ONE)) {
			if(n.mod(i).equals(BigInteger.ZERO)) {
				if(!factorization.containsKey(i))
					factorization.put(i, BigInteger.ONE);
				else factorization.put(i, factorization.get(i).add(BigInteger.ONE));
				n = n.divide(i);
				i = i.subtract(BigInteger.ONE);
			} else if(i.compareTo(max) >= 0) {
				throw new TimeoutException();
			}
		}
		return (HashMap<BigInteger, BigInteger>) factorization;
		
	}

	/**
	 * @param n
	 * @return the largest prime factor of n
	 */
	public static long largestPrimeFactor(long n) {
		if(n < 2)
			return n;
		for(long i = 2; i < n; i++)
			if(n % i == 0)
				n /= i--;
		return n;
	}

	/**
	 * Uses a recursive Euclidian algorithm to find the GCD.
	 * 
	 * @param a
	 * @param b
	 * @return greatest common divisor of a and b
	 */
	public static long greatestCommonDivisor(long a, long b) {
		if(b == 0)
			return a;
		return greatestCommonDivisor(b, a % b);
	}

	/**
	 * Uses the GCD to determine the LCM.
	 * 
	 * @param a
	 * @param b
	 * @return least common multiple of a and b
	 */
	public static long leastCommonMultiple(long a, long b) {
		return a / greatestCommonDivisor(a, b) * b;
	}

	/**
	 * @param n
	 * @return is n even
	 */
	public static boolean isEven(long n) {
		return n % 2 == 0;
	}

	/**
	 * @param n, multiple candidate
	 * @param a
	 * @return is n a multiple of a
	 */
	public static boolean isMultiple(long n, long a) {
		return n % a == 0;
	}

	/**
	 * @param n, divisor candidate
	 * @param a
	 * @return is n a divisor of a
	 */
	public static boolean isDivisor(long n, long a) {
		return a % n == 0;
	}

	/**
	 * @param n
	 * @return is n a perfect square
	 */
	public static boolean isSquare(double n) {
		double sq = Math.sqrt(n);
		return sq - Math.floor(sq) == 0;
	}
	
	/**
	 * @param n
	 * @return is n a perfect square
	 */
	public static boolean isSquare(BigInteger n) {
		BigDecimal sq = DefaultBigDecimalMath.sqrt(new BigDecimal(n));
		return sq.subtract(new BigDecimal(sq.toBigInteger())).compareTo(BigDecimal.ZERO) == 0;
	} 

	/**
	 * If 5 * a^2 } 4 is square, it is a Fibonacci number.
	 * 
	 * @param n
	 * @return is n a Fibonacci number?
	 */
	public static boolean isFibonacci(long n) {
		long x = 5 * (long) Math.pow(n, 2);
		return isSquare(x + 4) || isSquare(x - 4);
	}
	
	/**
	 * If 5 * a^2 } 4 is square, it is a Fibonacci number.
	 * 
	 * @param n
	 * @return is n a Fibonacci number?
	 */
	public static boolean isFibonacci(BigInteger n) {
		BigInteger x = n.pow(2).multiply(BigInteger.valueOf(5));
		return isSquare(x.add(BigInteger.valueOf(4))) || isSquare(x.subtract(BigInteger.valueOf(4)));
	}

	/**
	 * If a is greater than 1 and indivisible, it is prime.
	 * 
	 * @param n
	 * @return is n a prime number?
	 */
	public static boolean isPrime(long n) {
		if(n < 0) n = -n;
		if(n == 1 || n == 0)
			return false;
		int max = (int) Math.sqrt(n) + 1;
		for(long i = 2; i < max; i++)
			if(n % i == 0)
				return false;
		return true;
	}

	/**
	 * Method A uses the orthodox summation pattern to find the nth value. For n >
	 * 91, the nth number is too large to be represented, causing an overflow.
	 * 
	 * @param n
	 * @return the nth Fibonacci number, starting from 0 and 1.
	 */
	public static long getNthFibonacciNumberA(long n) {
		if(n <= 1)
			return n;
		long a = 0, b = 1, c, x = 1;
		while(x < n) {
			c = a + b;
			a = b;
			b = c;
			x++;
		}
		return b;
	}

	/**
	 * Method B uses Binet's Formula to approximate the nth value.
	 * 
	 * Phi = (ã5 + 1) / 2; Fib(n) = (Phi^n - (-Phi)^(-n)) / ã5
	 * 
	 * For n > 69, the approximation becomes too inaccurate to return the correct
	 * value.
	 * 
	 * @param n
	 * @return the nth Fibonacci number, starting from 0 and 1.
	 */
	public static long getNthFibonacciNumberB(long n) {
		if(n <= 1)
			return n;
		double phi = (1 + Math.sqrt(5)) / 2;
		double inversePhi = (Math.sqrt(5) - 1) / 2;
		return (long) (((Math.pow(phi, n) - Math.pow(inversePhi, n)) / Math.sqrt(5) + 0.5));
	}

	/**
	 * Method C uses the orthodox summation pattern to find the nth value. It can
	 * precisely calculate very large numbers, but it is very slow.
	 * 
	 * @param n
	 * @return the nth Fibonacci number, starting from 0 and 1.
	 */
	public static BigInteger getNthFibonacciNumberC(long n) {
		if(n <= 1)
			return BigInteger.valueOf(n);
		BigInteger a = BigInteger.valueOf(0), b = BigInteger.valueOf(1), c;
		long x = 1;
		while(x < n) {
			c = a.add(b);
			a = b;
			b = c;
			x++;
		}
		return b;
	}

	/**
	 * Method D uses Binet's formula to approximate the nth value.
	 * 
	 * Phi = (ã5 + 1) / 2; Fib(n) = (Phi^n - (-Phi)^(-n)) / ã5
	 * 
	 * For n > 146, the approximation becomes too inaccurate to return the correct
	 * value.
	 * 
	 * @param n
	 * @return the nth Fibonacci number, starting from 0 and 1.
	 */
	public static BigInteger getNthFibonacciNumberD(int n) {
		if(n <= 1)
			return BigInteger.valueOf(n);
		BigDecimal five = new BigDecimal(5);
		BigDecimal two = new BigDecimal(2);
		BigDecimal x = new BigDecimal(Math.sqrt(five.doubleValue()));
		BigDecimal sqrt_five = x
				.add(new BigDecimal(five.subtract(x.multiply(x)).doubleValue() / (x.doubleValue() * 2.0)));

		BigDecimal phi = BigDecimal.ONE.add(sqrt_five).divide(two);
		BigDecimal inversePhi = sqrt_five.subtract(BigDecimal.ONE).divide(two);

		try {
			return (phi.pow(n).subtract(inversePhi.pow(n))).divide(sqrt_five).add(BigDecimal.valueOf(0.5))
					.toBigInteger();
		} catch(ArithmeticException ae) {
			return (phi.pow(n).subtract(inversePhi.pow(n))).divide(sqrt_five, MathContext.DECIMAL128)
					.add(BigDecimal.valueOf(0.5)).toBigInteger();
		}
	}

	/**
	 * Method E uses Dijkstra's formulae to find the nth value.
	 * 
	 * n = 2i; F(2i) = F(i)^2 + F(i + 1)^2; F(2i + 1) = (2 * F(i) + F(i + 1)) * F(i
	 * + 1); F(2i - 1) = (2 * F(i + 1) - F(i)) * F(i);
	 * 
	 * @param n
	 * @return the nth Fibonacci number, starting from 0 and 1.
	 */
	public static BigInteger getNthFibonacciNumberE(long n) {
		if(n <= 1)
			return BigInteger.valueOf(n);
		if(n % 2 == 0) {
			BigInteger b = getNthFibonacciNumberE(n / 2);
			return (BigInteger.valueOf(2).multiply(getNthFibonacciNumberE(n / 2 - 1)).add(b)).multiply(b);
		} else {
			return getNthFibonacciNumberE(n / 2).pow(2).add(getNthFibonacciNumberE(n / 2 + 1).pow(2));
		}
	}

	/**
	 * Method F uses Dijkstra's formulae to find the nth value, but first indexes
	 * all of the necessary calculations to eliminate redundancies.
	 * 
	 * Is less efficient than Method C for n < 400, but is more efficient for n >
	 * 400.
	 * 
	 * n = 2i; 
	 * F(2i) = F(i)^2 + F(i + 1)^2; 
	 * F(2i + 1) = (2 * F(i) + F(i + 1)) * F(i + 1); 
	 * F(2i - 1) = (2 * F(i + 1) - F(i)) * F(i);
	 * 
	 * @param n
	 * @return the nth Fibonacci number, starting from 0 and 1.
	 */
	public static BigInteger getNthFibonacciNumberF(int n) {
		if(n <= 1)
			return BigInteger.valueOf(n);
		Map<Integer, BigInteger> index = new HashMap<Integer, BigInteger>();
		index.put(n, null);
		for(int i = n; i > 0;) {
			if(i == 1) {
				index.put(0, BigInteger.ZERO);
				index.put(1, BigInteger.ONE);
			} else if(i % 2 == 0) { // for n = 2i
				index.put(i / 2, null); // find i and i - 1
				index.put(i / 2 - 1, null);
			} else { // n = 2i - 1
				index.put(i / 2 + 1, null); // find i + 1 and i
				index.put(i / 2, null);
			}
			int next = 0;
			for(Map.Entry<Integer, BigInteger> entry : index.entrySet()) {
				int key = entry.getKey();
				if(key < i && key > next)
					next = key;
			}
			i = next;
		}
		for(int i = 2; i <= n; i++) {
			if(index.containsKey(i)) {
				if(i % 2 == 0) {
					BigInteger a = index.get(i / 2 - 1);
					BigInteger b = index.get(i / 2);
					index.put(i, BigInteger.valueOf(2).multiply(a).add(b).multiply(b));
				} else {
					BigInteger a = index.get(i / 2);
					BigInteger b = index.get(i / 2 + 1);
					index.put(i, a.pow(2).add(b.pow(2)));
				}
			}
		}
		return index.get(n);
	}
	
	/**
	 * Method D uses Binet's formula to approximate the nth value.
	 * 
	 * Phi = (ã5 + 1) / 2; Fib(n) = (Phi^n - (-Phi)^(-n)) / ã5
	 * 
	 * For n > 146, the approximation becomes too inaccurate to return the correct
	 * value.
	 * 
	 * @param n
	 * @return the nth Fibonacci number, starting from 0 and 1.
	 */
	public static BigDecimal getNthFibonacciNumberG(BigDecimal n, BigDecimal phi) {
		//if(n.compareTo(BigInteger.ONE) <= 1)
		//	return BigInteger.valueOf(n);
		BigDecimal five = new BigDecimal(5);
		BigDecimal two = new BigDecimal(2);
		BigDecimal x = DefaultBigDecimalMath.sqrt(five);
		BigDecimal sqrt_five = x.add(DefaultBigDecimalMath.divide(five.subtract(x.multiply(x)), (x.multiply(two))));
		

		//BigDecimal phi = BigDecimal.ONE.add(sqrt_five).divide(two);
		BigDecimal inversePhi = sqrt_five.subtract(BigDecimal.ONE).divide(two);

		try {
			return new BigDecimal(
					DefaultBigDecimalMath.divide(DefaultBigDecimalMath.pow(phi, n).subtract(DefaultBigDecimalMath.pow(inversePhi, n)), sqrt_five)
					.add(BigDecimal.valueOf(0.5)).toBigIntegerExact());
		} catch(ArithmeticException ae) {
			return DefaultBigDecimalMath.divide(DefaultBigDecimalMath.pow(phi, n).subtract(DefaultBigDecimalMath.pow(inversePhi, n)), sqrt_five)
					.add(BigDecimal.valueOf(0.5));
		}
	}
}
