package project.password_generator;

import java.security.SecureRandom;
import java.util.Random;

public class Password {

    SecureRandom secRandom = new SecureRandom();
    Random random = new Random();

    final String NUMBERSTRING = "1234567890";
    final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    //input variables
    private int length;
    private boolean hasUppers;
    private boolean hasNumbers;
    private int upperMax;
    private int numberMax;

    //constructors
    public Password() {}
    public Password(int length, boolean hasUppers, boolean hasNumbers) {
        setLength(length);
        setHasUppers(hasUppers);
        setHasNumbers(hasNumbers);
    }
    public Password(int length, boolean hasUppers, boolean hasNumbers, int upperMax, int numberMax) {
        setLength(length);
        setHasUppers(hasUppers);
        setHasNumbers(hasNumbers);
        setUpperMax(upperMax);
        setNumberMax(numberMax);
    }

    //setter and getter
    public int getLength() {
        return length;
    }
    public void setLength(int length) {
        if(length < 4) {
            throw new IllegalArgumentException("Password length should be more than three (3).");
        }
        if(length > 30) {
            throw new IllegalArgumentException("Password length should be a maximum of thirty (30) letters.");
        }
        this.length = length;
    }

    public boolean isUsingUppers() {return hasUppers;}
    public void setHasUppers(boolean hasUppers) {
        this.hasUppers = hasUppers;
    }

    public boolean isUsingNumbers() {return hasNumbers;}
    public void setHasNumbers(boolean hasNumbers) {
        this.hasNumbers = hasNumbers;
    }

    public int getUpperMax() {return upperMax;}
    public void setUpperMax(int upperMax) {
        if(isUsingUppers()) {
            if(upperMax > getLength()){
                throw new IllegalArgumentException("You want too many uppercase letters.  Try again.");
            }
            if(upperMax < 1) {
                throw new IllegalArgumentException("Uppercase input can not be less than one (1).");
            }
            this.upperMax = upperMax;
        } else { this.upperMax = 0; }
    }

    public int getNumberMax() {return numberMax;}
    public void setNumberMax(int numberMax) {
        if(isUsingNumbers()){
            if(numberMax > getLength()) {
                throw new IllegalArgumentException("You want too many numbers.  Try again.");
            }
            if(numberMax < 1) {
                throw new IllegalArgumentException("Number input can not be less than one (1).");
            }
            this.numberMax = numberMax;
        } else { this.numberMax = 0; }
    }


    public String passwordCreator(int wantedLength, int upCount, int numCount, String symbolString) {
        StringBuilder pw = new StringBuilder();

        boolean repeat = true;
        while(repeat) {
            pw.delete(0, wantedLength);
            int numCounter = 0, upperCounter = 0, lowerCounter = 0, symbolCounter = 0;

            for(int i=0; i<wantedLength; i++) {
                int switchPick = secRandom.nextInt(3);  //randomize characterType
                switch(switchPick) {
                    case 0:
                        //SYMBOLs
                        if(symbolCounter < lowerCounter) {
                            if(!symbolString.isEmpty()) {
                                int res = random.nextInt(symbolString.length());
                                char symbol = symbolString.charAt(res);

                                pw.append(symbol);  //appending symbol
                                symbolCounter++;
                                break;
                            }
                        }
                    case 1:
                        //NUMBERs
                        if(hasNumbers){
                            if(numCounter < numCount && numCounter <= lowerCounter) {
                                int res = random.nextInt(NUMBERSTRING.length());
                                char number = NUMBERSTRING.charAt(res);

                                pw.append(number);  //appending number
                                numCounter++;
                                break;
                            }
                        }
                    case 2:
                        //UPPERs
                        if(hasUppers) {
                            if(upperCounter < upCount && upperCounter <= lowerCounter) {
                                int res = random.nextInt(UPPERCASE.length());
                                char upperLetter = UPPERCASE.charAt(res);

                                pw.append(upperLetter);  //appending Uppercase Letter
                                upperCounter++;
                                break;
                            }
                        }
                    default:
                        //LOWERs
                        if(symbolString.isEmpty()) {
                            int res = random.nextInt(LOWERCASE.length());
                            char lowerLetter = LOWERCASE.charAt(res);

                            pw.append(lowerLetter);
                            lowerCounter++;
                        } else {
                            //if symbols wanted, add possibility to add symbol instead
                            if(lowerCounter < (symbolCounter * 3)) {
                                int res = random.nextInt(LOWERCASE.length());
                                char lowerLetter = LOWERCASE.charAt(res);

                                pw.append(lowerLetter);
                                lowerCounter++;
                            } else {
                                int res = random.nextInt(symbolString.length());  //random int based on size of the symbolString
                                char symbol = symbolString.charAt(res);

                                pw.append(symbol);  //append a symbol
                                symbolCounter++;
                            }
                        }
                }
            }
            char securityCheck = pw.charAt(0);
            if(securityCheck == '_' || securityCheck == '.' || securityCheck == '%' || securityCheck == '$' || securityCheck == '#' || securityCheck == '@' || securityCheck == '<') {
                repeat = true;
            } else { repeat = false; }
        }
        //if out of while loop -- return password
        return pw.toString();
    }

    @Override
    public String toString() {
        return "length = " + length +
                ", hasUppers = " + hasUppers +
                ", hasNumbers = " + hasNumbers +
                ", upperMax = " + upperMax +
                ", numberMax = " + numberMax;
    }
}
