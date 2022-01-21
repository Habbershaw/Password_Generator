package project.password_generator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class PasswordController implements Initializable {

    @FXML
    private Label errorString;
    @FXML
    private TextField displayString, maxNumCount, maxUpperCount, passwordLength;
    @FXML
    private CheckBox and, apostrophe, asteric, atSign, bSlash, backQuote, cBracket, cCurly, cParenthesis, carat, colon,
            comma, dbQuote, dollarSign, equals, exclamation, fSlash, greaterThan, hasNumbers, hasUppers, hashTag,
            lessThan, minus, oBracket, oCurly, oParenthesis, percent, period, pipe, plus, question, semiColon, tilde, underScore;


    private Password thePassword;
    private CheckBox[] boxArray;

    //on start..
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("APP STARTED ... ");

        //instantiating password Obj and pre-populate
        thePassword = new Password(5, true, true, 1, 1 );
        passwordLength.setText(String.valueOf(thePassword.getLength()));
        hasUppers.setSelected(thePassword.isUsingUppers());
        hasNumbers.setSelected(thePassword.isUsingNumbers());
        maxUpperCount.setText(String.valueOf(thePassword.getUpperMax()));
        maxNumCount.setText(String.valueOf(thePassword.getNumberMax()));

        //instantiating 1 box-array and populate.
        boxArray = new CheckBox[32];
        captureUserSymbols();
    }


    @FXML
    void generateButton(ActionEvent event) {
        displayString.setText("");
        errorString.setText("");

        String symbolInstance = symbolStringCreator(boxArray);

        //set if app is use uppers and/or numbers in calculation
        thePassword.setHasUppers(hasUppers.isSelected());
        thePassword.setHasNumbers(hasNumbers.isSelected());

        //method to call respective setters and return number
        try {
            int length = captureNumber(passwordLength);
            int upCount = captureNumber(maxUpperCount);
            int numCount = captureNumber(maxNumCount);

            String result = thePassword.passwordCreator(length, upCount, numCount, symbolInstance);
            displayString.setText(result);

            //DEBUGGING PRINT LINES
            System.out.printf("SYMBOL_STRING:   %s%n", symbolInstance);
            System.out.printf("PASSWORD_OBJ:   %s%n", thePassword.toString());
            System.out.printf("RETURN:   %s%n%n", result);

        } catch (Exception ex) {
            errorString.setText(ex.getMessage());
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

    private void captureUserSymbols() {
        /*
            - to be called on start to populate the boxArray
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

    /**
     * @param userInput
     * used to filter out strings before calling appropriate setters.
     *
     * @exception NumberFormatException
     * thrown if input is not found to be a number.
     * */
    private int captureNumber(TextField userInput) {
        String inputType = userInput.getId();
        switch(inputType) {
            case "passwordLength":
                if(userInput.getText().isEmpty()) {
                    userInput.setText("0");
                }
                try {
                    int len = Integer.parseInt(userInput.getText());
                    thePassword.setLength(len);
                } catch(NumberFormatException nfe) {
                    throw new NumberFormatException("Password length must be a number.");
                }
                break;

            case "maxUpperCount":
                if(userInput.getText().isEmpty() && thePassword.isUsingUppers()) {
                    userInput.setText("1");
                }
                if(userInput.getText().isEmpty() && !thePassword.isUsingUppers()) {
                    userInput.setText("0");
                }
                try {
                    int res = Integer.parseInt(userInput.getText());
                    thePassword.setUpperMax(res);
                } catch(NumberFormatException nfe) {
                    throw new NumberFormatException("Uppercase limit must be a number.");
                }
                break;

            case "maxNumCount":

                if(userInput.getText().isEmpty() && thePassword.isUsingNumbers()) {
                    userInput.setText("1");
                }
                if(userInput.getText().isEmpty() && !thePassword.isUsingNumbers()) {
                    userInput.setText("0");
                }
                try {
                    int res = Integer.parseInt(userInput.getText());
                    thePassword.setNumberMax(res);
                } catch(NumberFormatException fne) {
                    throw new NumberFormatException("Number limit must be a number.");
                }
                break;
        }
        return Integer.parseInt(userInput.getText());
    }

    /**
     * @return
     * String of user-desired symbols
    * */
    private String symbolStringCreator(CheckBox[] theBoxArray) {

        StringBuilder symbols = new StringBuilder();

        for(CheckBox checkBox : theBoxArray) {
            if(checkBox.isSelected()) {
                symbols.append(checkBox.getText());
            }
        }

        return symbols.toString();
    }
}
