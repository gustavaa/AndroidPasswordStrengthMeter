# Android Password Strength Meter

Password strength meter is an easy-to-implement and flexible password strength indicator for Android. It is fully customizable and features an animated strength indicator and a matching label. 

<img src="https://s2.gifyu.com/images/ezgif.com-gif-makerb90cdc0bcfb522cb.gif" width="650">

# Usage

**Project level build.gradle**
~~~~
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
~~~~

App level build.gradle

~~~~
dependencies {
    implementation 'se.gustav.aaro:PasswordStrengthMeter'
}
~~~~

# Examples

## XML

PasswordStrengthMeter can be initialized by defining it in a layput XML file, for example:
~~~~
    <se.aaro.gustav.passwordstrengthmeter.PasswordStrengthMeter 
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:id="@+id/passwordInputMeter"
        app:strengthBarHeight="5dp" 
        app:animateChanges="true"
        app:showStrengthLabel="true"
        app:showStrengthBar="true"
        app:animationDuration="300"
        app:labelTextSize="12sp"/>
~~~~

Then all you have to do is to set it up like this in your activity of fragment
~~~~
PasswordStrengthMeter meter = findViewById(R.id.passwordInputMeter);
meter.setEditText(passwordInputEditText);
~~~~

### XML properties
* **showStrengthBar:** Define whether or not the view should show the strength indicator bar. Default: true
* **strengthBarHeight:** The height of the strength indicator bar. Default: 5dp
* **animateChanges:** Define whether or not the bar should animate changes. Default: true
* **animationDuration:** The duration of the password strength level change animation in ms. Default: 300ms. 
* **showStrengthLabel:** Define whether or not the view should show a label which show the current strength level display name. Default: true
* **labelTextSize:** The label text size. Default: 12sp

## Java
PasswordStrengthMeter can also be initalized programatically:
~~~~
PasswordStrengthMeter meter = new PasswordStrengthMeter(this);
meter.setEditText(passwordInput);
linearLayout.addView(meter);

// And some customization can be done by: 
meter.setAnimationDuration(300); 
meter.setShowStrengthIndicator(true); 
meter.setShowStrengthLabel(true);
~~~~

# Customization
Apart from the basic cosmetic customization described above, PasswordStrengthMeter can also be customized in other aspects.

## Password strength calculation algorithm

PasswordStrengthMeter implements a default algorithm for pasword strength estimation, altough it is very basic and should not be considered the main contribution of this library. Instead, I recommend you to implement an algorithm that fits your system the best using the `PasswordStrengtCalculator`interface: 

~~~~
meter.setPasswordStrengthCalculator(new PasswordStrengthCalculator() {
        @Override
        public int calculatePasswordSecurityLevel(String password) {
            // Do some calculation and return an int corresponding to the "points" or "level" the user password got
            return points;
        }

        @Override
        public int getMinimumlength() {
            // Define the minimum length of a password. Anything below this should always yield a score of 0
            return 8;
        }

        @Override
        public boolean passwordAccepted(int level) {
            // Define whether or not the level is an accepted level or not. 
            return level > 3;
        }

        @Override
        public void onPassWordAccepted(String password) {
          // Called when the password entered meets your requirements of length and strength levels
        }
    });
~~~~

## Password strength levels

PasswordStrengthMeter has 5 (or 6 if you count level 0) default password strength levels. These are simply `PasswordStrengthLevel` that defines a diplay name and a color associated with the level. These are stored in an array, where the index corresponds to the numerical "level" or "score" from the password strength calculation algorithm. 

The default levels are defined as follows:
~~~~
PasswordStrengthLevel[] strengthLevels = {
    new PasswordStrengthLevel("Too short", android.R.color.darker_gray), // level 0
    new PasswordStrengthLevel("Weak", android.R.color.holo_red_dark), // level 1
    new PasswordStrengthLevel("Fair", android.R.color.holo_orange_dark), // level 2
    new PasswordStrengthLevel("Good", android.R.color.holo_orange_light), // level 3
    new PasswordStrengthLevel("Strong", android.R.color.holo_blue_light), // level 4
    new PasswordStrengthLevel("Very strong", android.R.color.holo_green_dark)}; // level 5
~~~~

These levels can easily be changed by simply creating a similar array and call `setStrengthLevels`:
~~~~
PasswordStrengthMeter meter = new PasswordStrengthMeter(this);
meter.setStrengthLevels(new PasswordStrengthLevel[]{
        new PasswordStrengthLevel("Level 0", android.R.color.white),
        new PasswordStrengthLevel("Level 1", android.R.color.holo_red_light),
        new PasswordStrengthLevel("Level 2", android.R.color.holo_orange_light),
        new PasswordStrengthLevel("Level 3", android.R.color.holo_green_light)});
~~~~

**NOTE** that you for the best result should provide the meter with a number of levels that is adapted for the strength esitmation algorithm used. I.e. if the algorithm yields a level between 0 and 5 (as the default implementation), you should provide the meter with a total of 6 strength levels. 









