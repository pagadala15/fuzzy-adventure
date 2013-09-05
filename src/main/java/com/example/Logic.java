package com.example;


/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 7/31/13
 * Time: 12:51 PM
 */
public class Logic {

	/**
	 * This method takes long integer as input and confirms if this an Prime or not
	 * {@link class Logic, method isPrime()}
	 * @param number
	 * @return boolean
	 */
    public boolean isPrime(long number) {
    	//Checking if the number is less than 2, then number is not a prime
    	if( number < 2 ) return false;
    	// Checking if the number is a prime using mod operator
    	for (long i=2; i<number; i++)
    	{
    		if(number%i == 0) return false;
    	}
    	return true;
     }

    /**
     * This method is written for future implementation, currently this is unused method.
     * @param number
     * @return long 
     */
    public long nextPrimeFrom(long number) {
        int result = (int) number + 1;
        while (!isPrime(result)) result++;
        return result;
    }
}
