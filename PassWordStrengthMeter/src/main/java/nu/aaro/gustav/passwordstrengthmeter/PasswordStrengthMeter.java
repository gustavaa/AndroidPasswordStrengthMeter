package nu.aaro.gustav.passwordstrengthmeter;

/**
 * Created by gustavaaro on 2018-10-04.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by gustavaaro on 2016-11-09.
 */

public class PasswordStrengthMeter extends LinearLayout {

    // Meter preferences
    private boolean animateChanges = true;
    private boolean showStrengthLabel = true;
    private boolean showStrengthBar = true;
    private int animationDuration = 300;
    private float strengthBarHeight;
    private float textSize;

    private Context context;
    private TextView strengthLabel;
    private StrengthIndicatorView strengthIndicatorView;
    private TextWatcher textWatcher;
    private EditText input;

    public static final int ID_MODIFIER = 321;

    private static String TAG = "PasswordStrengthMeter";
    private PasswordStrengthCalculator passwordStrengthCalculator;

    // The default array with the defined strength display names and colors for the different levels in order
    private PasswordStrengthLevel[] strengthLevels = {
            new PasswordStrengthLevel("Too short", android.R.color.darker_gray),
            new PasswordStrengthLevel("Weak", android.R.color.holo_red_dark),
            new PasswordStrengthLevel("Fair", android.R.color.holo_orange_dark),
            new PasswordStrengthLevel("Good", android.R.color.holo_orange_light),
            new PasswordStrengthLevel("Strong", android.R.color.holo_blue_light),
            new PasswordStrengthLevel("Very strong", android.R.color.holo_green_dark)};


    /**
     * Constructor used when created programmatically
     *
     * @param context the context of the component
     */

    public PasswordStrengthMeter(Context context) {
        super(context);
        this.context = context;
        setId(View.generateViewId());
        strengthBarHeight = convertDpToPx(5);
        textSize = convertDpToPx(14);
        initStrengthMeter();
    }

    /**
     * Constructor used when created from xml.
     *
     * @param context the context of the component.
     * @param attrs   the AttributeSet with properties from the xml.
     */

    public PasswordStrengthMeter(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PasswordStrengthMeter,
                0, 0);

        try {
            showStrengthLabel = a.getBoolean(R.styleable.PasswordStrengthMeter_showStrengthLabel, true);
            showStrengthBar = a.getBoolean(R.styleable.PasswordStrengthMeter_showStrengthBar, true);
            animateChanges = a.getBoolean(R.styleable.PasswordStrengthMeter_animateChanges, true);
            animationDuration = a.getInt(R.styleable.PasswordStrengthMeter_animationDuration, animationDuration);
            strengthBarHeight = a.getDimension(R.styleable.PasswordStrengthMeter_strengthBarHeight, convertDpToPx(5));
            textSize = a.getDimension(R.styleable.PasswordStrengthMeter_labelTextSize, convertDpToPx(14));
        } finally {
            a.recycle();
        }

        initStrengthMeter();
    }

    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(HORIZONTAL);
    }

    /**
     * Initializes the view by defining components and laying them out
     */

    public void initStrengthMeter() {
        setOrientation(HORIZONTAL);
        setVerticalGravity(Gravity.CENTER_VERTICAL);
        this.setPasswordStrengthCalculator(defaultPassWordStrengthCalculator);

        if (showStrengthBar) {
            strengthIndicatorView = new StrengthIndicatorView(context);
            strengthIndicatorView.setPasswordStrengthLevels(strengthLevels);
            strengthIndicatorView.setAnimDuration(animationDuration);
            strengthIndicatorView.setAnimate(animateChanges);
            strengthIndicatorView.setHeight((int) strengthBarHeight);
            strengthIndicatorView.setId(View.generateViewId());
            addView(strengthIndicatorView);
        }

        if (showStrengthLabel) {
            strengthLabel = new TextView(context);
            strengthLabel.setGravity(Gravity.END);
            strengthLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
            addView(strengthLabel);
            strengthLabel.setMinWidth(getMaxWidth());
            strengthLabel.setText(strengthLevels[0].getDisplayName());
            strengthLabel.setTextColor(getResources().getColor(strengthLevels[0].getIndicatorColor()));
            strengthLabel.setId(View.generateViewId());
        }
        setLayoutParams();

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                refresh();
            }
        };
    }

    /**
     * Set animation duration for meter change animations
     * @param animationDuration
     */
    public void setAnimationDuration(int animationDuration) {
        this.animationDuration = animationDuration;
        strengthIndicatorView.setAnimDuration(animationDuration);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setLayoutParams();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) setLayoutParams();
    }



    private void refresh(){
        if (input != null) {
            int level = normalizeLevel(passwordStrengthCalculator.calculatePasswordSecurityLevel(input.getText().toString()));

            if(this.passwordStrengthCalculator.passwordAccepted(level))
                this.passwordStrengthCalculator.onPasswordAccepted(input.getText().toString());

            if (showStrengthBar) {
                strengthIndicatorView.setSecurityLevel(normalizeLevel(level), false);
            }

            if (showStrengthLabel) {
                strengthLabel.setText(strengthLevels[level].getDisplayName());
                strengthLabel.setTextColor(getResources().getColor(strengthLevels[level].getIndicatorColor()));
            }
        }
    }

    /**
     * Get the maximum width of all level display names
     * @return the maximum width of the displaynames
     */
    private int getMaxWidth(){
        int max = 0;
        strengthLabel.setMinWidth(0);
        for(PasswordStrengthLevel level : strengthLevels){
            strengthLabel.setText(level.getDisplayName());
            strengthLabel.measure(0,0);
            max = strengthLabel.getMeasuredWidth() > max ? strengthLabel.getMeasuredWidth() : max;
        }
        return max;
    }


    /**
     *
     * Safety methods that normalizes the calculated level to one that is defined. If the strength calculation
     * algorithm returns a number that correctly corresponds to the number of levels defined, this won't do anything.
     * @param level the non normalized strength level
     * @return normalized strength level
     */
    private int normalizeLevel(int level){
        if(level < 0){
            level = 0;
        }else if(level >= strengthLevels.length){
            level = strengthLevels.length-1;
        }
        return level;
    }

    /**
     * Calculates the width of the components
     */
    private void setLayoutParams() {
        int labelWidth = 0;
        int marginEnd = 0;
        int marginStart = 0;
        if (showStrengthLabel) {
            strengthLabel.measure(0, 0);
            labelWidth = strengthLabel.getMeasuredWidth();
            marginEnd = convertDpToPx(2);
            marginStart = convertDpToPx(10);
            LayoutParams labelparams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            labelparams.setMarginEnd(marginEnd);
            labelparams.setMarginStart(marginStart);
            strengthLabel.setLayoutParams(labelparams);
        }

        if (showStrengthBar) {
            int indicatorWidth = getWidth() - labelWidth - marginEnd - marginStart;
            if (indicatorWidth < 0) indicatorWidth = 0;
            LayoutParams indicatorParams = new LayoutParams(indicatorWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            indicatorParams.setMargins(0, convertDpToPx(8), 0, convertDpToPx(8));
            strengthIndicatorView.setLayoutParams(indicatorParams);
        }
    }

    /**
     * Sets the strength levels for the meter, defined by a color and a display name
     *
     * @param strengthLevels array of PasswordStrengthLevel
     */
    public void setStrengthLevels(PasswordStrengthLevel[] strengthLevels) {
        this.strengthLevels = strengthLevels;
        strengthIndicatorView.setPasswordStrengthLevels(strengthLevels);
        strengthIndicatorView.invalidate();
        strengthLabel.setMinWidth(getMaxWidth());
        refresh();
        invalidate();
    }

    /**
     * Sets whether or not the line indicator should be visible.
     *
     * @param showStrengthIndicator show strength indicator boolean.
     */

    public void setShowStrengthIndicator(boolean showStrengthIndicator) {
        showStrengthBar = showStrengthIndicator;
        if (showStrengthIndicator) strengthIndicatorView.setVisibility(VISIBLE);
        else strengthIndicatorView.setVisibility(GONE);
    }


    public void setEditText(EditText passwordInput) {
        passwordInput.addTextChangedListener(textWatcher);
        this.input = passwordInput;
    }

    /**
     * Sets whether or not the text indicator should be visible.
     *
     * @param showStrengthLabel show text indicator boolean.
     */

    public void setShowStrengthLabel(boolean showStrengthLabel) {
        this.showStrengthLabel = showStrengthLabel;
        if (showStrengthLabel) strengthLabel.setVisibility(VISIBLE);
        else strengthLabel.setVisibility(GONE);
    }


    /**
     * Sets the handler responsible for the strength-algorithm. The default value is
     * this class itself.
     *
     * @param passwordStrengthCalculator handler responsible for calculations and requirements.
     */

    public void setPasswordStrengthCalculator(PasswordStrengthCalculator passwordStrengthCalculator) {
        this.passwordStrengthCalculator = passwordStrengthCalculator;
    }



    /**
     * Default algorithm for calculating password strength, providing 5 basic strength levels
     * NOTE: This is a very basic algorithm and you are strongly advised to implement one customized to your needs
     */

    private PasswordStrengthCalculator defaultPassWordStrengthCalculator = new PasswordStrengthCalculator() {
        @Override
        public int calculatePasswordSecurityLevel(String password) {
            int level = 0;
            int minLength = getMinimumLength();

            // Checks if password meets minimum length
            if (password.length() < minLength) {
                return 0;
            } else {
                ++level;
            }

            // Give level for extra long password
            if (password.length() >= (minLength * (3.0 / 2))) ++level;

            boolean hasUppercase = !password.equals(password.toLowerCase());
            boolean hasLowercase = !password.equals(password.toUpperCase());

            // Give level for password containing both upper and lower case letters
            if (hasLowercase && hasUppercase) ++level;

            // Give level for password containing digits
            boolean hasNumber = password.matches(".*\\d.*");
            if (hasNumber) ++level;

            // Give level for password containing special characters
            boolean hasSpecial = password.matches(".*[!@#€£©§|≈$%^&*].*");
            if (hasSpecial) ++level;

            return level;
        }

        @Override
        public int getMinimumLength() {
            return 8;
        }

        @Override
        public boolean passwordAccepted(int level) {
            return level > 0;
        }

        @Override
        public void onPasswordAccepted(String password) {

        }
    };

    /**
     * Convinience method for calculating dp values to pixel values
     * @param dp the value in dp
     * @return the value in px
     */
    private int convertDpToPx(int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }


}