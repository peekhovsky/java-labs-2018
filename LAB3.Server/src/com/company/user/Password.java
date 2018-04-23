package com.company.user;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Password {
	
	public static String getHash(String newPassword) {	 
			return hashingMD5(newPassword); 
	   }
	
	/**Here is string that is attached to the end of a password (for higher level of protection*/
	private static final String salt = "SaltPekhRost";
	   
	/**This method makes new hash-code of password using MD5 (attaching fix string and index in the end of password)
	 * * @param newPassword here is password that hashes*/
	static private String hashingMD5(String newPassword) {
		
	       String stringHashCode = null;
	       
	       //add salt with different indexes
	       newPassword = newPassword.concat(salt);

	       //hashing
	       try {
	           MessageDigest messageDigest = MessageDigest.getInstance("MD5");
	           messageDigest.update(newPassword.getBytes());

	           byte[] hashPassword = messageDigest.digest();

	           BigInteger bigInteger = new BigInteger(1, hashPassword);

	           stringHashCode = bigInteger.toString(16);

	       } catch (NoSuchAlgorithmException ex) {
	           System.out.println("Password -> setPassword -> NoSuchAlgorithmException");
	       }
	       return stringHashCode;
	   }


	   /**That method checks is that password valid. It compares hash-codes of checked password and valid password.
	    * @param newPassword password that you want to check*/
	   public static boolean isPasswordCorrect(String correctHashPassword, String newPassword){
	       return hashingMD5(newPassword).equals(correctHashPassword);
	   }
}
