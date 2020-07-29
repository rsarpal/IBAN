/*
* Author - Rishu Sarpal
* Date - 16/10/2018
* Environment - IntelliJ on MacOS X
* Description - Generates Finnish AccountNumber and an valid corresponding IBAN number
* Package - com.rsarpal.finIBAN
* Constructor - IBAN(String country) - parameters two letter country prefix . Supports only 'FI'
* Methods - String finAccGenerator() - returns a valid finnish bank account number
*         - String ibanGenerator() - returns a valid Finnish IBAN bank account number

 * */

package com.rsarpal.IBAN;

import java.math.BigInteger;


public class IBAN {

    String countryPrefix;
    String numericalCountryPrefix;
    String ibanNumber;
    int checksum;
    BigInteger divisor;

    public IBAN(String country){

        countryPrefix=country;
        numericalCountryPrefix=countryCodeToInt();
        ibanNumber=null;
        //branchNumber=null;
        //accountNumber=null;
        divisor= new BigInteger("97");

    }

    String countryCodeToInt(){
        String numPrefix="";
        int intval;
        for (int i=0;i<2;i++) {
             intval =(int)countryPrefix.charAt(i);
            numPrefix += intval - 55;
        }
        return numPrefix;
    }

    // Generates Finnish bank account number
    public String finAccGenerator(){
        String branchNumber,accountNumber;

        branchNumber= String.valueOf(Math.round(Math.random() * 1E5 + 100000));
        accountNumber=  String.valueOf(Math.round(1E6*Math.random()+ 1000000));
        //System.out.println( branchNumber + " " +  accountNumber + " " + numericalCountryPrefix);

        //add checksum to accountnumber
        accountNumber= branchNumber + accountNumber + addFinAccChecksumMod10((branchNumber+accountNumber).toCharArray());
        //FI66 39390051041309
        //accountNumber= "3939005104130" + addChecksumMod10(("3939005104130").toCharArray());

        //System.out.println("accountNumber " + accountNumber);
        return accountNumber;
    }

    // Generates IBAN for a Finnish bank account number
    public String ibanGenerator(String generatedAccountNumber){

        String strChecksum="";

        //int mod = (new BigInteger(branchNumber + "0000" + accountNumber + numericalCountryPrefix +"00").mod(divisor)).intValue();
        int mod = (new BigInteger( generatedAccountNumber + numericalCountryPrefix +"00").mod(divisor)).intValue();
        //int mod = (new BigInteger("39390051041309151800").mod(divisor)).intValue();

        //System.out.println("mod " + mod);
        checksum= 98- mod;

        //padding with leading 0
        if (checksum<10) strChecksum = "0";

        ibanNumber= countryPrefix + strChecksum + String.valueOf(checksum) + generatedAccountNumber;

    return ibanNumber;

    }

    //add account number checksum (https://en.wikipedia.org/wiki/Luhn_algorithm)
    int addFinAccChecksumMod10(char[] account) {

        /* https://en.wikipedia.org/wiki/Luhn_algorithm

           account - 1 5 9  0 3 0 0 0 0 0 0 7 7 6
           weights - 2 1 2  1 2 1 2 1 2 1 2 1 2
           multiple- 2 5 18 0 6 0 0 0 0 0 0 0 14
           result  - 2 5 9  0 6 0 0 0 0 0 0 7  5 = 34 → 6*/


        int  weightings[] = {2,1,2,1,2,1,2,1,2,1,2,1,2};

        int total, nDigit;
        int calcuatedCheckDigit;

        total=0;

        for (int i=0;i<13;i++) {
           nDigit=Character.getNumericValue((account[i])) * weightings [i];
           //System.out.println("nDigit " + nDigit + "=" + account[i] + "* " +  weightings [i]);
            //If the result of this doubling operation is greater than 9 (e.g., 8 × 2 = 16), then add the digits of the product (e.g., 16: 1 + 6 = 7, 18: 1 + 8 = 9)
            // or, alternatively, the same result can be found by subtracting 9 from the product (e.g., 16: 16 − 9 = 7, 18: 18 − 9 = 9).
           if (nDigit>9){
                nDigit=nDigit-9;
           }
           total = total + nDigit;

        }
        //System.out.println("total*9 " + total*9);

        calcuatedCheckDigit = (total*9)%10;

        //System.out.println("calcuatedCheckDigit " + calcuatedCheckDigit);



      return calcuatedCheckDigit;

    }


    public static void main(String[] args) {

        IBAN newIban = new IBAN("FI");


        for (int i = 0; i < 100; i++)
            System.out.println(newIban.ibanGenerator(newIban.finAccGenerator()));
    }


}
