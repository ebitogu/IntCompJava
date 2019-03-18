import java.util.*;
import java.security.SecureRandom;


/***
	PROBLEM STATEMENT:

	So, I am currently working on a software and an integral part of the software is 
	lossless natural numbers compression.That is losslessly encoding a pair of numbers into another unique 
	natural number and be able to get back the pair if given the unique number. 

	This works flawlessly except that the pairing functions generates astronomically large numbers fairly
	quickly

	My current issue now is to figure out a way to compress this large numbers to smaller numbers and be 
	able to get back the large numbers given the small numbers.

	I could do this easily with hexadecimals, but they won't work well with what I currently have.

	So,let's have a practical example:

	Assuming I want to losslessly encode two natural numbers [43208,32485] into another unique natural 
	number, below is how it's currently done

	long pairables = {43208,32485};
	long encoded = pair(pairables);

	The encoded result = 1866996234L.

	You can find the logic of the pair and unpair functions below.

	Now assuming I want to now pair {1866996234L,65536}, Result would now be = 3485674937770313700L; //Now the number is too large for me to work with in java and I don't want to work with BigIntegers and BigDecimals

	My current task now is how to reduce large numbers like 1866996234 into another smaller number, 
	so I could instead use that smaller number and pair with other numbers incrementally
	but whenever I see the smaller number I should be able to know that it is a minified
	representation of a large numer, say 1866996234.

	So asssuming:
	    long expansion = 1866996234L;
		long reduction = 28488; // Whenever I see 28488 I should be able to get back to 1866996234 from it.


	I could create a mapping of small numbers to large numbers in an embedded local database, 
	but I currently do not have the computing power for that as it would certainly be time consuming,
	it would definitely span months to complete should I do that on my current development machine.


	Do you think you can help out or come up with a better approach of large number reduction other than 
	hexadecimals.

	I have exhausted StackOverFlow and Quora with no result.

	***/


public class IntCompression{

	/**
	Generate a random number between 0 and 65536(Exclusive upper bound)
	*/
    private int generateRandomNumber() {
        return new SecureRandom().nextInt(65536);
    }

     /**
    Losslessly encodes two natural numbers into a single unique number
    @return the encoded natural number.
    **/
	public long pair(long [] pairables) {
        long a = pairables[0];
        long b = pairables[1];
        long shell = Math.max(a, b);
        long step = Math.min(a, b);
        long flag = 0;
        if (step != b) {
            flag = 1;
        }
        double shellSquared = Math.pow(shell, 2);
        return (long) (shellSquared + (step * 2) + flag);
    }

 	/**
	Decodes a pair of encoded natural numbers
	@return the unpaired naturals.
	**/
    public long [] unpair(long paired) {
        long shell = (long) Math.floor(Math.sqrt(paired));
        long remainder = (long) (paired - Math.pow(shell, 2));
        long step = (long) Math.floor((double) remainder / 2);
        if (remainder % 2 == 0) {
            return new long[]{shell, step};
        } else {
            return new long[]{step, shell};
        }
    }

	/***
	Attempt to reduce large numbers using modular arithmetic.

	This serves a purpose but didn't work out well as we are gonna end up with two numbers the 
	[quotient and the remainder]. We need just one of them to work with but certainly 
	just one can't work without knowledge of the other.
	**/
	public long [] attemptMOD(long largeNumber){
		//Using a constant divisor of 65536
		long divisor = 65536;
		long quotient = (long)(largeNumber/divisor);
		long remainder = largeNumber % divisor;
		return new long[]{quotient,remainder};
	}


	public static void main(String...args){

		IntCompression intCompression = new IntCompression();

		long [] pairables = {intCompression.generateRandomNumber(),intCompression.generateRandomNumber()};
	    
	    long result = intCompression.pair(pairables);

	    System.out.println("Pairables\n" + Arrays.toString(pairables) + " Result=" + result);
	   
	    long [] unpairedResult = intCompression.unpair(result);
	    
	    System.out.println("UnPairedResult=" + Arrays.toString(unpairedResult));

	    System.out.println("==================REDUCTION ATTEMPTS================");

	    long [] modAttempt = intCompression.attemptMOD(result);
	    
	    System.out.println("Mod of " + result + " = " + Arrays.toString(modAttempt));

	}

}