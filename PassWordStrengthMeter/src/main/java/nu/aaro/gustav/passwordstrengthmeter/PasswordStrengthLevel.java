package nu.aaro.gustav.passwordstrengthmeter;

/**
 * Created by gustavaaro on 2018-10-04.
 */

public class PasswordStrengthLevel {

    private int indicatorColor;
    private String displayName;

    public PasswordStrengthLevel(String displayName, int indicatorColor){
        this.indicatorColor = indicatorColor;
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getIndicatorColor() {
        return indicatorColor;
    }
}
