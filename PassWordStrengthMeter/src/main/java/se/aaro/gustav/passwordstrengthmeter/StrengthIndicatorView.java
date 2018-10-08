package se.aaro.gustav.passwordstrengthmeter;


import android.animation.ArgbEvaluator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by gustavaaro on 2018-10-04.
 */

class StrengthIndicatorView extends View {
    private int height;
    private int currentLineWidth;
    private int correctLineWidth;
    private int currentColor;
    private int correctColor;
    private Paint linePaint = new Paint();
    private Paint backgroundPaint = new Paint();
    private int securityLevel;

    private static String TAG = "StrengthIndicatorView";
    private boolean animate;
    private int animDuration;

    //Colors used for the different security levels
    private PasswordStrengthLevel[] levels;

    /**
     * Constructor
     * @param context the context of the component.
     */

    public StrengthIndicatorView(Context context) {
        super(context);
        securityLevel = 0;
    }

    public void setPasswordStrengthLevels(PasswordStrengthLevel[] levels) {
        backgroundPaint.setColor(getResources().getColor(levels[0].getIndicatorColor()));
        currentColor = getResources().getColor(levels[0].getIndicatorColor());
        this.levels = levels;
        invalidate();
        refresh();
    }

    /**
     * Default constructor
     * @param context the context of the component.
     * @param attrs AttributeSet received from the XML.
     */
    public StrengthIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        securityLevel = 0;
    }

    public void setAnimDuration(int animDuration) {
        this.animDuration = animDuration;
    }

    public void setAnimate(boolean animate) {
        this.animate = animate;
    }

    /**
     * Sets the view's height
     * @param height the height
     */

    public void setHeight(int height){
        this.height = height;
    }

    public void refresh(){
        int duration = animate ? animDuration : 0;
        PropertyValuesHolder colorProp = PropertyValuesHolder.ofInt("color",currentColor,correctColor);
        PropertyValuesHolder widthProperty = PropertyValuesHolder.ofInt("width",currentLineWidth,correctLineWidth);

        ValueAnimator colorAnim = new ValueAnimator();
        colorAnim.setDuration(duration);
        colorAnim.setValues(colorProp,widthProperty);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setInterpolator(new AccelerateDecelerateInterpolator()
        );
        colorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentColor = (int) animation.getAnimatedValue("color");
                currentLineWidth = (int) animation.getAnimatedValue("width");
                invalidate();
            }
        });
        colorAnim.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(changed) setSecurityLevel(securityLevel, true);
    }

    /**
     * Sets the current security level
     * @param level The calculated security level.
     */

    public void setSecurityLevel(int level, boolean force){
        if(force || this.securityLevel != level){
            this.securityLevel = level;
            correctLineWidth = (getMeasuredWidth()/(levels.length-1))*level;
            correctColor = getResources().getColor(levels[level].getIndicatorColor());
            refresh();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        linePaint.setStrokeWidth(height);
        linePaint.setColor(currentColor);
        backgroundPaint.setStrokeWidth(height);
        canvas.drawLine(0, height/2, getWidth(), height/2, backgroundPaint);
        canvas.drawLine(0,height/2, currentLineWidth,height/2,linePaint);
    }

}

