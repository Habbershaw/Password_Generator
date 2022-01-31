package project.password_generator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
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


    private Password thePassword;
    private CheckBox[] boxArray;


    //on start..
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("APP STARTED ... ");

        //instantiating 1 box-array -- populate.
        boxArray = new CheckBox[32];
        captureUserSymbols();

        //Text-Formatter to be used on text-fields whose input must only be a number.
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if(newText.matches("([0-9][0-9]*)?")) {
                return change;
            } else {
                try {
                    if(change.getControl().getId().equals("passwordLength")) {
                        errorString.setText("Password length can not contain letters.");
                    }
                } catch(Exception ex) {
                    errorString.setText("Spinners can only handle numbers.");
                }
            }
            return null;
        };
        //declare integer-only TextField
        passwordLength.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));

        /*
            - configure spinners to accept realistic resolutions using SpinnerValueFactory
            - ( minValue==0 // maxValue==30 // startValue==0 // incrementValue==2 )
        */
        SpinnerValueFactory<Integer> numSpinnerFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 30, 0, 2);
        SpinnerValueFactory<Integer> uppSpinnerFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 30, 0, 2);
        maxNumCount.setValueFactory(numSpinnerFactory);
        maxUpperCount.setValueFactory(uppSpinnerFactory);

        TextField numSpinnerTextField = maxNumCount.getEditor();
        TextField uppSpinnerTextField = maxUpperCount.getEditor();

        //add integerFilter to manNumCount and maxUpperCount with the newly created text-fields.
        numSpinnerTextField.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
        uppSpinnerTextField.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
    }

    @FXML
    void generateButton(ActionEvent event) {
        displayString.setText("");
        errorString.setText("");

        //populate object variables
        int length = Integer.parseInt(passwordLength.getText());
        boolean hasUppers = includeUppercase.isSelected();
        boolean hasNumbers = includeNumbers.isSelected();
        int upperMax = maxUpperCount.getValue();
        int numberMax = maxNumCount.getValue();
        String symbolString = symbolStringCreator(boxArray);

        //instantiate new Password object and call function to generate password.

        //debug print lines
        System.out.println("Length of.. " + length);
        System.out.printf("hasUppers is %s with %d%n", hasUppers, upperMax);
        System.out.printf("hasNumbers is %s with %d%n", hasNumbers, numberMax);
        System.out.printf("symbols... %n%s%n", symbolString);
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
     * @return
     * string of user-desired symbols
     */
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
