package main.java.com.explorer;

import org.jetbrains.annotations.Nullable;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.Vector;


/**Use this class to protect your program.
 * If you want to create protected password using hashing you can do it using this class
 * @author Rostislav Pekhovksy*/
public class Password {

    /**Here you should store passwords that is valid (User is defined by index of string)*/
    private Vector<String> hashPasswords;
    /**Here is string that is attached in the end of a password (for higher level of protection*/
    private final String salt = "SaltPekhRost";

    /**This constructor uses location with a list of passwords just to create complete base
     * @param location text file with password hash-codes location */
    public Password(String location) throws IOException {
        hashPasswords = new Vector<>();
        readPassword(location);
    }

    /**This constructor just creates new empty prototype of this class*/
    public Password() {
        hashPasswords = new Vector<>();
    }

    /**Use this method to add new passwords to password base*
     * @param index index of passport in password list
     * @param newPassword here write down your new password
     */
    public void addPassword(int index, String newPassword) {

        String hashPassword = hashingMD5(index, newPassword);
        if (hashPassword != null) {
            hashPasswords.add(index, hashPassword);
        }
    }

    /**This method makes new hash-code of password using MD5 (attaching fix string and index in the end of password)
     * @param index index of password (attached in the end)
     * @param newPassword here is password that hashes*/
    private String hashingMD5(int index, String newPassword) {

        String stringHashCode = null;
        //add salt with different indexes
        newPassword = newPassword.concat(salt.concat(String.valueOf(index)));

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
     * @param index valid password index
     * @param newPassword password that you want to check*/
    public boolean isPasswordCorrect(int index, String newPassword){
        return hashingMD5(index, newPassword).equals(hashPasswords.get(index));
    }

    /**Method read password hash-codes from existing file in computer.
     * @param location location where this file is in a disk*/
    private void readPassword(String location) throws IOException {

        FileReader fileReader = new FileReader(location);
        Scanner scanner = new Scanner(fileReader);

        while (scanner.hasNextLine()) {
            hashPasswords.add(scanner.nextLine());
        }
        scanner.close();
        fileReader.close();
    }
    /**This method writes down passwords that is in base to file.
     * @param location location where this file will be in a disk*/
    public void writePassword(String location) throws IOException {

        FileWriter fileWriter = new FileWriter(location);

        for(String password : hashPasswords) {
            fileWriter.write(password);
            fileWriter.write('\n');
        }
        fileWriter.close();
    }

    /**Gets password hash by index. Returns null if it is wrong index.
     * @param index index (order) of password in vector
     * @return password hash, zero if index is wrong*/
    @Nullable
    public String getHashPassword(int index){
        try {
            return hashPasswords.get(index);
        } catch (Exception ex) {
            return null;
        }
    }

}
