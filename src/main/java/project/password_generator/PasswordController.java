package project.password_generator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.security.SecureRandom;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class PasswordController implements Initializable {

    @FXML
    private Label errorString;
    @FXML
    private TextField displayString, passwordLength ;
    @FXML
    private Spinner<Integer> maxNumCount, maxUpperCount;
    @FXML
    private CheckBox and, apostrophe, asteric, atSign, bSlash, backQuote, cBracket, cCurly, cParenthesis, carat, colon,
            comma, dbQuote, dollarSign, equals, exclamation, fSlash, greaterThan, includeNumbers, includeUppercase, hashTag,
            lessThan, minus, oBracket, oCurly, oParenthesis, percent, period, pipe, plus, question, semiColon, tilde, underScore;



    private Password tempPassword = new Password();
    SecureRandom secRandom = new SecureRandom();
    Random random = new Random();
    private CheckBox[] boxArray;


    //on start..
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("APP STARTED ... ");
        tempPassword.setLength(8);
        tempPassword.setHasUppers(true);
        tempPassword.setUpperMax(1);
        tempPassword.setHasNumbers(true);
        tempPassword.setNumberMax(1);

        //instantiating 1 box-array -- populate.
        boxArray = new CheckBox[32];
        populateBoxArray();

        //Text-Formatter to be used on text-fields whose input must only be a number.
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            //this regex also accounting for empty spacing
            if(newText.matches("([0-9][0-9]*)?")) {
                return change;
            } else {
                try {
                    if(change.getControl().getId().equals("passwordLength")) {
                        displayString.setText("");
                        errorString.setText("Password length can not contain letters.");
                    }
                } catch(Exception ex) {
                    displayString.setText("");
                    errorString.setText("Spinners can only handle numbers.");
                }
            }
            return null;
        };
        //declare integer-only TextField(s)
        passwordLength.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), tempPassword.getLength(), integerFilter));

        /*
            - configure spinners to accept realistic resolutions using SpinnerValueFactory
            - ( minValue==0 // maxValue==30 // startValue==0 // incrementValue==2 )
        */
        SpinnerValueFactory<Integer> numSpinnerFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, tempPassword.getLength(), tempPassword.getNumberMax(), 2);
        SpinnerValueFactory<Integer> uppSpinnerFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, tempPassword.getLength(), tempPassword.getUpperMax(), 2);
        maxNumCount.setValueFactory(numSpinnerFactory);
        maxUpperCount.setValueFactory(uppSpinnerFactory);

        TextField numSpinnerTextField = maxNumCount.getEditor();
        TextField uppSpinnerTextField = maxUpperCount.getEditor();

        //add integerFilter to manNumCount and maxUpperCount with the newly created text-fields.
        numSpinnerTextField.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), tempPassword.getNumberMax(), integerFilter));
        uppSpinnerTextField.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), tempPassword.getUpperMax(), integerFilter));

        //pre-populate Upper and Number inputs
        includeNumbers.setSelected(tempPassword.isUsingNumbers());
        includeUppercase.setSelected(tempPassword.isUsingUppers());
    }

    @FXML
    void generateButton(ActionEvent event) {
        displayString.setText("");
        errorString.setText("");
        String symbolString = tempPassword.captureSymbolString(boxArray);

        try {
            //populate object variables
            tempPassword.setLength(Integer.parseInt(passwordLength.getText()));
            tempPassword.setHasUppers(includeUppercase.isSelected());
            tempPassword.setHasNumbers(includeNumbers.isSelected());
            tempPassword.setUpperMax(maxUpperCount.getValue());
            tempPassword.setNumberMax(maxNumCount.getValue());
            tempPassword.setSymbolString(symbolString);

            //generate password.
            String thePassword = passwordCreator();
            displayString.setText(thePassword);
        } catch(Exception ex) {
            displayString.setText("");
            errorString.setText(ex.getMessage());
        }

        //QoL to inputs
        if(tempPassword.getUpperMax() == 0){
            includeUppercase.setSelected(false);
        }
        if(tempPassword.getNumberMax() == 0) {
            includeNumbers.setSelected(false);
        }
    }

    @FXML
    void deselectAllButton(ActionEvent event) {
        //loop through the CheckBox[] -- set everything to false
        for(CheckBox checkBox : boxArray) {
            checkBox.setSelected(false);
        }
    }
    @FXML
    void selectAllButton(ActionEvent event) {
        //loop through the CheckBox[] -- set everything to true
        for(CheckBox checkBox : boxArray) {
            checkBox.setSelected(true);
        }
    }

    /**
     * @return
     * a string object generated using a StringBuilder.
     */
    private String passwordCreator() {
        String numberString = "1234567890";
        String lowerString = "abcdefghijklmnopqrstuvwxyz";
        String upperString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder pw = new StringBuilder();

        boolean repeat = true;
        while(repeat) {
            pw.delete(0, tempPassword.getLength());
            int numCounter = 0, upperCounter = 0, lowerCounter = 0, symbolCounter = 0;

            for(int i=0; i<tempPassword.getLength(); i++) {
                int switchPick = secRandom.nextInt(3);  //randomize characterType
                switch(switchPick) {
                    case 0:
                        //SYMBOLs
                        if(symbolCounter < lowerCounter) {
                            if(!tempPassword.getSymbolString().isEmpty()) {
                                int res = random.nextInt(tempPassword.getSymbolString().length());
                                char symbol = tempPassword.getSymbolString().charAt(res);

                                pw.append(symbol);  //appending symbol
                                symbolCounter++;
                                break;
                            }
                        }
                    case 1:
                        //NUMBERs
                        if(tempPassword.isUsingNumbers()){
                            if(numCounter < tempPassword.getNumberMax() && numCounter <= lowerCounter) {
                                int res = random.nextInt(numberString.length());
                                char number = numberString.charAt(res);

                                pw.append(number);  //appending number
                                numCounter++;
                                break;
                            }
                        }
                    case 2:
                        //UPPERs
                        if(tempPassword.isUsingUppers()) {
                            if(upperCounter < tempPassword.getUpperMax() && upperCounter <= lowerCounter) {
                                int res = random.nextInt(upperString.length());
                                char upperLetter = upperString.charAt(res);

                                pw.append(upperLetter);  //appending Uppercase Letter
                                upperCounter++;
                                break;
                            }
                        }
                    default:
                        //LOWERs
                        if(lowerCounter == 0) {
                            int res = random.nextInt(lowerString.length());
                            char lowerLetter = lowerString.charAt(res);

                            pw.append(lowerLetter);  //append lowerLetter
                            lowerCounter++;
                            break;
                        }
                        if(symbolCounter < (lowerCounter * 2.5)){
                            int res = random.nextInt(lowerString.length());
                            char lowerLetter = lowerString.charAt(res);

                            pw.append(lowerLetter);  //append lowerLetter
                            lowerCounter++;
                        } else {
                            int res = random.nextInt(tempPassword.getSymbolString().length());
                            char symbol = tempPassword.getSymbolString().charAt(res);

                            pw.append(symbol);  //append a symbol
                            symbolCounter++;
                        }
                }
            }
            //extra security can easily go here...
            repeat = false;
        }
        return pw.toString();
    }


    private void populateBoxArray() {
        /*
            - to be called on start to populate boxArray
        */
        boxArray[0] = tilde;
        boxArray[1] = backQuote;
        boxArray[2] = exclamation;
        boxArray[3] = atSign;
        boxArray[4] = hashTag;
        boxArray[5] = dollarSign;
        boxArray[6] = percent;
        boxArray[7] = carat;
        boxArray[8] = and;
        boxArray[9] = asteric;
        boxArray[10] = oParenthesis;
        boxArray[11] = cParenthesis;
        boxArray[12] = underScore;
        boxArray[13] = minus;
        boxArray[14] = plus;
        boxArray[15] = equals;
        boxArray[16] = cBracket;
        boxArray[17] = oBracket;
        boxArray[18] = cCurly;
        boxArray[19] = oCurly;
        boxArray[20] = greaterThan;
        boxArray[21] = lessThan;
        boxArray[22] = question;
        boxArray[23] = comma;
        boxArray[24] = colon;
        boxArray[25] = semiColon;
        boxArray[26] = period;
        boxArray[27] = pipe;
        boxArray[28] = dbQuote;
        boxArray[29] = bSlash;
        boxArray[30] = fSlash;
        boxArray[31] = apostrophe;
    }  //method to return checkBox variables as checkboxArray
}
