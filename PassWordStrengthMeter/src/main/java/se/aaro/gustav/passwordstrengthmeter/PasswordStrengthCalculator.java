package se.aaro.gustav.passwordstrengthmeter;

/**
 * Created by gustavaaro on 2018-10-04.
 */


/**
 * Created by gustavaaro on 2016-11-09.
 */

public interface PasswordStrengthCalculator {

    /**
     * Calculates the current password strength.
     * @param password the password entered by the user
     * @return an int in the range [0-N], where n is the number of defined security levels.
     */
    int calculatePasswordSecurityLevel(String password);


    /**
     * Defines the minimum length of a password
     * @return An integer value which represents the minimum length of a password.
     */
    int getMinimumLength();


    /**
     * Defines what minimum security level is required for an accepted password.
     * @param level the current security level of the password.
     */

    boolean passwordAccepted(int level);

    /**
     * Is called when the password is accepted.
     * @param password the current password.
     */
    void onPasswordAccepted(String password);

}