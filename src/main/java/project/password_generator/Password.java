package project.password_generator;

import javafx.scene.control.CheckBox;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
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
    private String symbolString;

    //constructors
    public Password() {}
    public Password(int length, boolean hasUppers, boolean hasNumbers, int upperMax, int numberMax, String symbolString) {
        setLength(length);
        setHasUppers(hasUppers);
        setHasNumbers(hasNumbers);
        setUpperMax(upperMax);
        setNumberMax(numberMax);
        setSymbolString(symbolString);
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
            this.upperMax = upperMax;
        } else { this.upperMax = 0; }
    }

    public int getNumberMax() {return numberMax;}
    public void setNumberMax(int numberMax) {
        if(isUsingNumbers()){
            if(numberMax > getLength()) {
                throw new IllegalArgumentException("You want too many numbers.  Try again.");
            }
            this.numberMax = numberMax;
        } else { this.numberMax = 0; }
    }

    public String getSymbolString() {return symbolString;}
    public void setSymbolString(String symbolString) {
        this.symbolString = symbolString;
    }

    /**
     * @return
     * string of the user-desired symbols
     */
    public String captureSymbolString(CheckBox[] boxArray) {
        StringBuilder symbols = new StringBuilder();

        for(CheckBox checkBox : boxArray) {
            if(checkBox.isSelected()) {
                symbols.append(checkBox.getText());
            }
        }
        return symbols.toString();
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
