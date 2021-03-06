package algorithms;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import ch.obermuhlner.math.big.DefaultBigDecimalMath;

public class Algorithms {
	//TODO Drake Equation Denominator
	//TODO Sieve of Eratosthenes, solve for nth Prime
	
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
		//System.out.println(n);
		Map<BigInteger, BigInteger> factorization = new HashMap<BigInteger, BigInteger>();
		if(n.compareTo(BigInteger.ZERO) < 0) {
			factorization.put(BigInteger.ONE.negate(), BigInteger.ONE);
			n = n.negate();
		} 
		if(n.compareTo(BigInteger.ONE) < 1)
			factorization.put(n, BigInteger.ONE);
		BigInteger max = BigInteger.valueOf(100_000_000);
		for(BigInteger i = BigInteger.valueOf(2); n.compareTo(i) > -1; i = i.add(BigInteger.ONE)) {
			if(n.mod(i).equals(BigInteger.ZERO)) {
				//System.out.println(i + "	" + n);
				if(!factorization.containsKey(i))
					factorization.put(i, BigInteger.ONE);
				else factorization.put(i, factorization.get(i).add(BigInteger.ONE));
				n = n.divide(i);
				i = i.subtract(BigInteger.ONE);
			} else if(i.compareTo(max) >= 0) {
				throw new TimeoutException("Restricted prime factor x^n for x > 100,000,000");
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
	 * If 5 * a^2 ± 4 is square, it is a Fibonacci number.
	 * 
	 * @param n
	 * @return is n a Fibonacci number?
	 */
	public static boolean isFibonacci(long n) {
		long x = 5 * (long) Math.pow(n, 2);
		return isSquare(x + 4) || isSquare(x - 4);
	}
	
	/**
	 * If 5 * a^2 ± 4 is square, it is a Fibonacci number.
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
	 * Phi = (√5 + 1) / 2; Fib(n) = (Phi^n - (-Phi)^(-n)) / √5
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
	 * Phi = (√5 + 1) / 2; Fib(n) = (Phi^n - (-Phi)^(-n)) / √5
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
	 * Phi = (√5 + 1) / 2; Fib(n) = (Phi^n - (-Phi)^(-n)) / √5
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
