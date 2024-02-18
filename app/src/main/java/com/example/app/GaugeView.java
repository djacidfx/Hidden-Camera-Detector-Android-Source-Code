package com.example.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.view.ViewCompat;

public class GaugeView extends View {
    public static final float BOTTOM = 1.0f;
    public static final float CENTER = 0.5f;
    public static final float INNER_RIM_BORDER_WIDTH = 0.005f;
    public static final float INNER_RIM_WIDTH = 0.06f;
    public static final float LEFT = 0.0f;
    public static final float NEEDLE_HEIGHT = 0.28f;
    public static final float NEEDLE_WIDTH = 0.035f;
    public static final float OUTER_BORDER_WIDTH = 0.04f;
    public static final float OUTER_RIM_WIDTH = 0.05f;
    public static final float OUTER_SHADOW_WIDTH = 0.03f;
    public static final float RIGHT = 1.0f;
    public static final int SCALE_DIVISIONS = 10;
    public static final float SCALE_END_VALUE = 100.0f;
    public static final float SCALE_POSITION = 0.025f;
    public static final float SCALE_START_ANGLE = 30.0f;
    public static final float SCALE_START_VALUE = 0.0f;
    public static final int SCALE_SUBDIVISIONS = 5;
    public static final boolean SHOW_INNER_RIM = true;
    public static final boolean SHOW_NEEDLE = true;
    public static final boolean SHOW_OUTER_BORDER = true;
    public static final boolean SHOW_OUTER_RIM = true;
    public static final boolean SHOW_OUTER_SHADOW = true;
    public static final boolean SHOW_RANGES = true;
    public static final boolean SHOW_SCALE = false;
    public static final boolean SHOW_TEXT = false;
    public static final int SIZE = 300;
    public static final int TEXT_UNIT_COLOR = -1;
    public static final float TEXT_UNIT_SIZE = 0.1f;
    public static final int TEXT_VALUE_COLOR = -1;
    public static final float TEXT_VALUE_SIZE = 0.3f;
    public static final float TOP = 0.0f;
    private Bitmap mBackground;
    private Paint mBackgroundPaint;
    private float mCurrentValue;
    private float mDivisionValue;
    private int mDivisions;
    private Paint mFaceBorderPaint;
    private Paint mFacePaint;
    private RectF mFaceRect;
    private Paint mFaceShadowPaint;
    private Paint mInnerRimBorderDarkPaint;
    private Paint mInnerRimBorderLightPaint;
    private RectF mInnerRimBorderRect;
    private float mInnerRimBorderWidth;
    private Paint mInnerRimPaint;
    private RectF mInnerRimRect;
    private float mInnerRimWidth;
    private float mNeedleAcceleration;
    private float mNeedleHeight;
    private boolean mNeedleInitialized;
    private long mNeedleLastMoved;
    private Paint mNeedleLeftPaint;
    private Path mNeedleLeftPath;
    private Paint mNeedleRightPaint;
    private Path mNeedleRightPath;
    private Paint mNeedleScrewBorderPaint;
    private Paint mNeedleScrewPaint;
    private float mNeedleVelocity;
    private float mNeedleWidth;
    private Paint mOuterBorderPaint;
    private RectF mOuterBorderRect;
    private float mOuterBorderWidth;
    private Paint mOuterRimPaint;
    private RectF mOuterRimRect;
    private float mOuterRimWidth;
    private Paint mOuterShadowPaint;
    private RectF mOuterShadowRect;
    private float mOuterShadowWidth;
    private int[] mRangeColors;
    private Paint[] mRangePaints;
    private float[] mRangeValues;
    private float mScaleEndAngle;
    private float mScaleEndValue;
    private float mScalePosition;
    private RectF mScaleRect;
    private float mScaleRotation;
    private float mScaleStartAngle;
    private float mScaleStartValue;
    private boolean mShowInnerRim;
    private boolean mShowNeedle;
    private boolean mShowOuterBorder;
    private boolean mShowOuterRim;
    private boolean mShowOuterShadow;
    private boolean mShowRanges;
    private boolean mShowScale;
    private boolean mShowText;
    private float mSubdivisionAngle;
    private float mSubdivisionValue;
    private int mSubdivisions;
    private float mTargetValue;
    private int mTextShadowColor;
    private String mTextUnit;
    private int mTextUnitColor;
    private Paint mTextUnitPaint;
    private float mTextUnitSize;
    private String mTextValue;
    private int mTextValueColor;
    private Paint mTextValuePaint;
    private float mTextValueSize;
    public static final int[] OUTER_SHADOW_COLORS = {Color.argb(40, 255, 254, 187), Color.argb(20, 255, 247, 219), Color.argb(5, 255, 255, 255)};
    public static final float[] OUTER_SHADOW_POS = {0.9f, 0.95f, 0.99f};
    public static final float[] RANGE_VALUES = {16.0f, 25.0f, 40.0f, 100.0f};
    public static final int[] RANGE_COLORS = {Color.rgb(231, 32, 43), Color.rgb(232, 111, 33), Color.rgb(232, 231, 33), Color.rgb(27, 202, 33)};
    public static final int TEXT_SHADOW_COLOR = Color.argb(100, 0, 0, 0);

    private int getDefaultDimension() {
        return SIZE;
    }

    public GaugeView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mNeedleLastMoved = -1L;
        readAttrs(context, attributeSet, i);
        init();
    }

    public GaugeView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public GaugeView(Context context) {
        this(context, null, 0);
    }

    public static void drawTextOnCanvasWithMagnifier(Canvas canvas, String str, float f, float f2, Paint paint) {
        if (Build.VERSION.SDK_INT <= 15) {
            canvas.drawText(str, f, f2, paint);
            return;
        }
        float strokeWidth = paint.getStrokeWidth();
        float textSize = paint.getTextSize();
        canvas.save();
        canvas.scale(0.001f, 0.001f);
        paint.setTextSize(textSize * 1000.0f);
        paint.setStrokeWidth(strokeWidth * 1000.0f);
        canvas.drawText(str, f * 1000.0f, f2 * 1000.0f, paint);
        canvas.restore();
        paint.setTextSize(textSize);
        paint.setStrokeWidth(strokeWidth);
    }

    private void readAttrs(Context context, AttributeSet attributeSet, int i) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.GaugeView, i, 0);
        this.mShowOuterShadow = obtainStyledAttributes.getBoolean(19, true);
        this.mShowOuterBorder = obtainStyledAttributes.getBoolean(17, true);
        this.mShowOuterRim = obtainStyledAttributes.getBoolean(18, true);
        this.mShowInnerRim = obtainStyledAttributes.getBoolean(15, true);
        this.mShowNeedle = obtainStyledAttributes.getBoolean(16, true);
        this.mShowScale = obtainStyledAttributes.getBoolean(21, false);
        this.mShowRanges = obtainStyledAttributes.getBoolean(20, true);
        this.mShowText = obtainStyledAttributes.getBoolean(22, false);
        this.mOuterShadowWidth = this.mShowOuterShadow ? obtainStyledAttributes.getFloat(7, 0.03f) : 0.0f;
        this.mOuterBorderWidth = this.mShowOuterBorder ? obtainStyledAttributes.getFloat(5, 0.04f) : 0.0f;
        this.mOuterRimWidth = this.mShowOuterRim ? obtainStyledAttributes.getFloat(6, 0.05f) : 0.0f;
        this.mInnerRimWidth = this.mShowInnerRim ? obtainStyledAttributes.getFloat(2, 0.06f) : 0.0f;
        this.mInnerRimBorderWidth = this.mShowInnerRim ? obtainStyledAttributes.getFloat(1, 0.005f) : 0.0f;
        this.mNeedleWidth = obtainStyledAttributes.getFloat(4, 0.035f);
        this.mNeedleHeight = obtainStyledAttributes.getFloat(3, 0.28f);
        this.mScalePosition = (this.mShowScale || this.mShowRanges) ? obtainStyledAttributes.getFloat(12, 0.025f) : 0.0f;
        this.mScaleStartValue = obtainStyledAttributes.getFloat(14, 0.0f);
        this.mScaleEndValue = obtainStyledAttributes.getFloat(11, 100.0f);
        float f = obtainStyledAttributes.getFloat(13, 30.0f);
        this.mScaleStartAngle = f;
        this.mScaleEndAngle = obtainStyledAttributes.getFloat(10, 360.0f - f);
        this.mDivisions = obtainStyledAttributes.getInteger(0, 10);
        this.mSubdivisions = obtainStyledAttributes.getInteger(23, 5);
        if (this.mShowRanges) {
            this.mTextShadowColor = obtainStyledAttributes.getColor(24, TEXT_SHADOW_COLOR);
            readRanges(obtainStyledAttributes.getTextArray(9), obtainStyledAttributes.getTextArray(8));
        }
        if (this.mShowText) {
            int resourceId = obtainStyledAttributes.getResourceId(28, 0);
            String string = obtainStyledAttributes.getString(28);
            String str = "";
            if (resourceId > 0) {
                string = context.getString(resourceId);
            } else if (string == null) {
                string = "";
            }
            this.mTextValue = string;
            int resourceId2 = obtainStyledAttributes.getResourceId(25, 0);
            String string2 = obtainStyledAttributes.getString(25);
            if (resourceId2 > 0) {
                str = context.getString(resourceId2);
            } else if (string2 != null) {
                str = string2;
            }
            this.mTextUnit = str;
            this.mTextValueColor = obtainStyledAttributes.getColor(29, -1);
            this.mTextUnitColor = obtainStyledAttributes.getColor(26, -1);
            this.mTextShadowColor = obtainStyledAttributes.getColor(24, TEXT_SHADOW_COLOR);
            this.mTextValueSize = obtainStyledAttributes.getFloat(30, 0.3f);
            this.mTextUnitSize = obtainStyledAttributes.getFloat(27, 0.1f);
        }
        obtainStyledAttributes.recycle();
    }

    private void readRanges(CharSequence[] charSequenceArr, CharSequence[] charSequenceArr2) {
        int length;
        int length2;
        if (charSequenceArr == null) {
            length = RANGE_VALUES.length;
        } else {
            length = charSequenceArr.length;
        }
        if (charSequenceArr2 == null) {
            length2 = RANGE_COLORS.length;
        } else {
            length2 = charSequenceArr2.length;
        }
        if (length != length2) {
            throw new IllegalArgumentException("The ranges and colors arrays must have the same length.");
        }
        if (charSequenceArr != null) {
            this.mRangeValues = new float[length];
            for (int i = 0; i < length; i++) {
                this.mRangeValues[i] = Float.parseFloat(charSequenceArr[i].toString());
            }
        } else {
            this.mRangeValues = RANGE_VALUES;
        }
        if (charSequenceArr2 != null) {
            this.mRangeColors = new int[length];
            for (int i2 = 0; i2 < length; i2++) {
                this.mRangeColors[i2] = Color.parseColor(charSequenceArr2[i2].toString());
            }
            return;
        }
        this.mRangeColors = RANGE_COLORS;
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= 11) {
            setLayerType(1, null);
        }
        initDrawingRects();
        initDrawingTools();
        if (this.mShowRanges) {
            initScale();
        }
    }

    public void initDrawingRects() {
        this.mOuterShadowRect = new RectF(0.0f, 0.0f, 1.0f, 1.0f);
        this.mOuterBorderRect = new RectF(this.mOuterShadowRect.left + this.mOuterShadowWidth, this.mOuterShadowRect.top + this.mOuterShadowWidth, this.mOuterShadowRect.right - this.mOuterShadowWidth, this.mOuterShadowRect.bottom - this.mOuterShadowWidth);
        this.mOuterRimRect = new RectF(this.mOuterBorderRect.left + this.mOuterBorderWidth, this.mOuterBorderRect.top + this.mOuterBorderWidth, this.mOuterBorderRect.right - this.mOuterBorderWidth, this.mOuterBorderRect.bottom - this.mOuterBorderWidth);
        this.mInnerRimRect = new RectF(this.mOuterRimRect.left + this.mOuterRimWidth, this.mOuterRimRect.top + this.mOuterRimWidth, this.mOuterRimRect.right - this.mOuterRimWidth, this.mOuterRimRect.bottom - this.mOuterRimWidth);
        this.mInnerRimBorderRect = new RectF(this.mInnerRimRect.left + this.mInnerRimBorderWidth, this.mInnerRimRect.top + this.mInnerRimBorderWidth, this.mInnerRimRect.right - this.mInnerRimBorderWidth, this.mInnerRimRect.bottom - this.mInnerRimBorderWidth);
        this.mFaceRect = new RectF(this.mInnerRimRect.left + this.mInnerRimWidth, this.mInnerRimRect.top + this.mInnerRimWidth, this.mInnerRimRect.right - this.mInnerRimWidth, this.mInnerRimRect.bottom - this.mInnerRimWidth);
        this.mScaleRect = new RectF(this.mFaceRect.left + this.mScalePosition, this.mFaceRect.top + this.mScalePosition, this.mFaceRect.right - this.mScalePosition, this.mFaceRect.bottom - this.mScalePosition);
    }

    private void initDrawingTools() {
        Paint paint = new Paint();
        this.mBackgroundPaint = paint;
        paint.setFilterBitmap(true);
        if (this.mShowOuterShadow) {
            this.mOuterShadowPaint = getDefaultOuterShadowPaint();
        }
        if (this.mShowOuterBorder) {
            this.mOuterBorderPaint = getDefaultOuterBorderPaint();
        }
        if (this.mShowOuterRim) {
            this.mOuterRimPaint = getDefaultOuterRimPaint();
        }
        if (this.mShowInnerRim) {
            this.mInnerRimPaint = getDefaultInnerRimPaint();
            this.mInnerRimBorderLightPaint = getDefaultInnerRimBorderLightPaint();
            this.mInnerRimBorderDarkPaint = getDefaultInnerRimBorderDarkPaint();
        }
        if (this.mShowRanges) {
            setDefaultScaleRangePaints();
        }
        if (this.mShowNeedle) {
            setDefaultNeedlePaths();
            this.mNeedleLeftPaint = getDefaultNeedleLeftPaint();
            this.mNeedleRightPaint = getDefaultNeedleRightPaint();
            this.mNeedleScrewPaint = getDefaultNeedleScrewPaint();
            this.mNeedleScrewBorderPaint = getDefaultNeedleScrewBorderPaint();
        }
        if (this.mShowText) {
            this.mTextValuePaint = getDefaultTextValuePaint();
            this.mTextUnitPaint = getDefaultTextUnitPaint();
        }
        this.mFacePaint = getDefaultFacePaint();
        this.mFaceBorderPaint = getDefaultFaceBorderPaint();
        this.mFaceShadowPaint = getDefaultFaceShadowPaint();
    }

    public Paint getDefaultOuterShadowPaint() {
        Paint paint = new Paint(1);
        paint.setStyle(Paint.Style.FILL);
        paint.setShader(new RadialGradient(0.5f, 0.5f, this.mOuterShadowRect.width() / 2.0f, OUTER_SHADOW_COLORS, OUTER_SHADOW_POS, Shader.TileMode.MIRROR));
        return paint;
    }

    private Paint getDefaultOuterBorderPaint() {
        Paint paint = new Paint(1);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.argb(245, 0, 0, 0));
        return paint;
    }

    public Paint getDefaultOuterRimPaint() {
        LinearGradient linearGradient = new LinearGradient(this.mOuterRimRect.left, this.mOuterRimRect.top, this.mOuterRimRect.left, this.mOuterRimRect.bottom, Color.rgb(255, 255, 255), Color.rgb(84, 90, 100), Shader.TileMode.REPEAT);
        Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.light_alu);
        BitmapShader bitmapShader = new BitmapShader(decodeResource, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        Matrix matrix = new Matrix();
        matrix.setScale(1.0f / decodeResource.getWidth(), 1.0f / decodeResource.getHeight());
        bitmapShader.setLocalMatrix(matrix);
        Paint paint = new Paint(1);
        paint.setShader(new ComposeShader(linearGradient, bitmapShader, PorterDuff.Mode.MULTIPLY));
        paint.setFilterBitmap(true);
        return paint;
    }

    private Paint getDefaultInnerRimPaint() {
        Paint paint = new Paint(1);
        paint.setShader(new LinearGradient(this.mInnerRimRect.left, this.mInnerRimRect.top, this.mInnerRimRect.left, this.mInnerRimRect.bottom, new int[]{Color.argb(100, 68, 73, 80), Color.argb(255, 91, 97, 105), Color.argb(255, 178, 180, 183), Color.argb(255, (int) 188, (int) 188, 190), Color.argb(100, 84, 90, 100), Color.argb(255, 137, 137, 137)}, new float[]{0.0f, 0.1f, 0.2f, 0.4f, 0.8f, 1.0f}, Shader.TileMode.CLAMP));
        return paint;
    }

    private Paint getDefaultInnerRimBorderLightPaint() {
        Paint paint = new Paint(1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.argb(100, 255, 255, 255));
        paint.setStrokeWidth(0.005f);
        return paint;
    }

    private Paint getDefaultInnerRimBorderDarkPaint() {
        Paint paint = new Paint(1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.argb(100, 255, 193, 7));
        paint.setStrokeWidth(0.005f);
        return paint;
    }

    public Paint getDefaultFacePaint() {
        Paint paint = new Paint(1);
        paint.setShader(new RadialGradient(0.5f, 0.5f, this.mFaceRect.width() / 2.0f, new int[]{Color.rgb(50, 132, 206), Color.rgb(36, 89, 162), Color.rgb(255, 193, 7)}, new float[]{0.5f, 0.96f, 0.99f}, Shader.TileMode.MIRROR));
        return paint;
    }

    public Paint getDefaultFaceBorderPaint() {
        Paint paint = new Paint(1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.argb(100, 255, 193, 7));
        paint.setStrokeWidth(0.005f);
        return paint;
    }

    public Paint getDefaultFaceShadowPaint() {
        Paint paint = new Paint(1);
        paint.setShader(new RadialGradient(0.5f, 0.5f, this.mFaceRect.width() / 2.0f, new int[]{Color.argb(60, 40, 96, 170), Color.argb(100, 255, 193, 7), Color.argb(120, 0, 0, 0), Color.argb(140, 0, 0, 0)}, new float[]{0.6f, 0.85f, 0.96f, 0.99f}, Shader.TileMode.MIRROR));
        return paint;
    }

    public void setDefaultNeedlePaths() {
        Path path = new Path();
        this.mNeedleLeftPath = path;
        path.moveTo(0.5f, 0.5f);
        this.mNeedleLeftPath.lineTo(0.5f - this.mNeedleWidth, 0.5f);
        this.mNeedleLeftPath.lineTo(0.5f, 0.5f - this.mNeedleHeight);
        this.mNeedleLeftPath.lineTo(0.5f, 0.5f);
        this.mNeedleLeftPath.lineTo(0.5f - this.mNeedleWidth, 0.5f);
        Path path2 = new Path();
        this.mNeedleRightPath = path2;
        path2.moveTo(0.5f, 0.5f);
        this.mNeedleRightPath.lineTo(this.mNeedleWidth + 0.5f, 0.5f);
        this.mNeedleRightPath.lineTo(0.5f, 0.5f - this.mNeedleHeight);
        this.mNeedleRightPath.lineTo(0.5f, 0.5f);
        this.mNeedleRightPath.lineTo(this.mNeedleWidth + 0.5f, 0.5f);
    }

    public Paint getDefaultNeedleLeftPaint() {
        Paint paint = new Paint(1);
        paint.setColor(Color.rgb(176, 10, 19));
        return paint;
    }

    public Paint getDefaultNeedleRightPaint() {
        Paint paint = new Paint(1);
        paint.setColor(Color.rgb(252, 18, 30));
        paint.setShadowLayer(0.01f, 0.005f, -0.005f, Color.argb(127, 0, 0, 0));
        return paint;
    }

    public Paint getDefaultNeedleScrewPaint() {
        Paint paint = new Paint(1);
        paint.setShader(new RadialGradient(0.5f, 0.5f, 0.07f, new int[]{Color.rgb(171, 171, 171), -1}, new float[]{0.05f, 0.9f}, Shader.TileMode.MIRROR));
        return paint;
    }

    public Paint getDefaultNeedleScrewBorderPaint() {
        Paint paint = new Paint(1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.argb(100, 81, 84, 89));
        paint.setStrokeWidth(0.005f);
        return paint;
    }

    public void setDefaultScaleRangePaints() {
        int length = this.mRangeValues.length;
        this.mRangePaints = new Paint[length];
        for (int i = 0; i < length; i++) {
            this.mRangePaints[i] = new Paint(65);
            this.mRangePaints[i].setColor(this.mRangeColors[i]);
            this.mRangePaints[i].setStyle(Paint.Style.STROKE);
            this.mRangePaints[i].setStrokeWidth(0.005f);
            this.mRangePaints[i].setTextSize(0.05f);
            this.mRangePaints[i].setTypeface(Typeface.SANS_SERIF);
            this.mRangePaints[i].setTextAlign(Paint.Align.CENTER);
            this.mRangePaints[i].setShadowLayer(0.005f, 0.002f, 0.002f, this.mTextShadowColor);
        }
    }

    public Paint getDefaultTextValuePaint() {
        Paint paint = new Paint(65);
        paint.setColor(this.mTextValueColor);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(0.005f);
        paint.setTextSize(this.mTextValueSize);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.SANS_SERIF);
        paint.setShadowLayer(0.01f, 0.002f, 0.002f, this.mTextShadowColor);
        return paint;
    }

    public Paint getDefaultTextUnitPaint() {
        Paint paint = new Paint(65);
        paint.setColor(this.mTextUnitColor);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(0.005f);
        paint.setTextSize(this.mTextUnitSize);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setShadowLayer(0.01f, 0.002f, 0.002f, this.mTextShadowColor);
        return paint;
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        Bundle bundle = (Bundle) parcelable;
        super.onRestoreInstanceState(bundle.getParcelable("superState"));
        this.mNeedleInitialized = bundle.getBoolean("needleInitialized");
        this.mNeedleVelocity = bundle.getFloat("needleVelocity");
        this.mNeedleAcceleration = bundle.getFloat("needleAcceleration");
        this.mNeedleLastMoved = bundle.getLong("needleLastMoved");
        this.mCurrentValue = bundle.getFloat("currentValue");
        this.mTargetValue = bundle.getFloat("targetValue");
    }

    private void initScale() {
        float f = this.mScaleStartAngle;
        this.mScaleRotation = (180.0f + f) % 360.0f;
        float f2 = this.mScaleEndValue - this.mScaleStartValue;
        int i = this.mDivisions;
        float f3 = f2 / i;
        this.mDivisionValue = f3;
        int i2 = this.mSubdivisions;
        this.mSubdivisionValue = f3 / i2;
        this.mSubdivisionAngle = (this.mScaleEndAngle - f) / (i * i2);
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        Parcelable onSaveInstanceState = super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", onSaveInstanceState);
        bundle.putBoolean("needleInitialized", this.mNeedleInitialized);
        bundle.putFloat("needleVelocity", this.mNeedleVelocity);
        bundle.putFloat("needleAcceleration", this.mNeedleAcceleration);
        bundle.putLong("needleLastMoved", this.mNeedleLastMoved);
        bundle.putFloat("currentValue", this.mCurrentValue);
        bundle.putFloat("targetValue", this.mTargetValue);
        return bundle;
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int mode = View.MeasureSpec.getMode(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        setMeasuredDimension(chooseDimension(mode, View.MeasureSpec.getSize(i)), chooseDimension(mode2, View.MeasureSpec.getSize(i2)));
    }

    private int chooseDimension(int i, int i2) {
        return (i == Integer.MIN_VALUE || i == 1073741824) ? i2 : getDefaultDimension();
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        drawGauge();
    }

    private void drawGauge() {
        Bitmap bitmap = this.mBackground;
        if (bitmap != null) {
            bitmap.recycle();
        }
        this.mBackground = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(this.mBackground);
        float min = Math.min(getWidth(), getHeight());
        canvas.scale(min, min);
        canvas.translate(min == ((float) getHeight()) ? ((getWidth() - min) / 2.0f) / min : 0.0f, min == ((float) getWidth()) ? ((getHeight() - min) / 2.0f) / min : 0.0f);
        drawRim(canvas);
        drawFace(canvas);
        if (this.mShowRanges) {
            drawScale(canvas);
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);
        float min = Math.min(getWidth(), getHeight());
        canvas.scale(min, min);
        canvas.translate(min == ((float) getHeight()) ? ((getWidth() - min) / 2.0f) / min : 0.0f, min == ((float) getWidth()) ? ((getHeight() - min) / 2.0f) / min : 0.0f);
        if (this.mShowNeedle) {
            drawNeedle(canvas);
        }
        if (this.mShowText) {
            drawText(canvas);
        }
        computeCurrentValue();
    }

    private void drawBackground(Canvas canvas) {
        Bitmap bitmap = this.mBackground;
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, this.mBackgroundPaint);
        }
    }

    private void drawRim(Canvas canvas) {
        if (this.mShowOuterShadow) {
            canvas.drawOval(this.mOuterShadowRect, this.mOuterShadowPaint);
        }
        if (this.mShowOuterBorder) {
            canvas.drawOval(this.mOuterBorderRect, this.mOuterBorderPaint);
        }
        if (this.mShowOuterRim) {
            canvas.drawOval(this.mOuterRimRect, this.mOuterRimPaint);
        }
        if (this.mShowInnerRim) {
            canvas.drawOval(this.mInnerRimRect, this.mInnerRimPaint);
            canvas.drawOval(this.mInnerRimRect, this.mInnerRimBorderLightPaint);
            canvas.drawOval(this.mInnerRimBorderRect, this.mInnerRimBorderDarkPaint);
        }
    }

    private void drawFace(Canvas canvas) {
        canvas.drawOval(this.mFaceRect, this.mFacePaint);
        canvas.drawOval(this.mFaceRect, this.mFaceBorderPaint);
        canvas.drawOval(this.mFaceRect, this.mFaceShadowPaint);
    }

    private void drawText(Canvas canvas) {
        String valueString = !TextUtils.isEmpty(this.mTextValue) ? this.mTextValue : valueString(this.mCurrentValue);
        float measureText = this.mTextValuePaint.measureText(valueString);
        canvas.drawText(valueString, 0.5f - ((!TextUtils.isEmpty(this.mTextUnit) ? this.mTextUnitPaint.measureText(this.mTextUnit) : 0.0f) / 2.0f), 0.6f, this.mTextValuePaint);
        if (TextUtils.isEmpty(this.mTextUnit)) {
            return;
        }
        canvas.drawText(this.mTextUnit, (measureText / 2.0f) + 0.5f + 0.03f, 0.5f, this.mTextUnitPaint);
    }

    private void drawScale(Canvas canvas) {
        float f;
        canvas.save();
        canvas.rotate(this.mScaleRotation, 0.5f, 0.5f);
        int i = (this.mDivisions * this.mSubdivisions) + 1;
        int i2 = 0;
        while (i2 < i) {
            float f2 = this.mScaleRect.top;
            float f3 = f2 + 0.015f;
            float f4 = f2 + 0.045f;
            float valueForTick = getValueForTick(i2);
            Paint rangePaint = getRangePaint(valueForTick);
            float f5 = valueForTick % this.mDivisionValue;
            int i3 = i;
            if (Math.abs(f5 - 0.0f) < 0.001d || Math.abs(f5 - this.mDivisionValue) < 0.001d) {
                canvas.drawLine(0.5f, f2, 0.5f, f4, rangePaint);
                f = 0.5f;
                drawTextOnCanvasWithMagnifier(canvas, valueString(valueForTick), 0.5f, f4 + 0.045f, rangePaint);
            } else {
                canvas.drawLine(0.5f, f2, 0.5f, f3, rangePaint);
                f = 0.5f;
            }
            canvas.rotate(this.mSubdivisionAngle, f, f);
            i2++;
            i = i3;
        }
        canvas.restore();
    }

    private String valueString(float f) {
        return String.format("%d", Integer.valueOf((int) f));
    }

    private float getValueForTick(int i) {
        return this.mScaleStartValue + (i * (this.mDivisionValue / this.mSubdivisions));
    }

    private Paint getRangePaint(float f) {
        int length = this.mRangeValues.length;
        int i = 0;
        while (true) {
            int i2 = length - 1;
            if (i < i2) {
                if (f < this.mRangeValues[i]) {
                    return this.mRangePaints[i];
                }
                i++;
            } else if (f <= this.mRangeValues[i2]) {
                return this.mRangePaints[i2];
            } else {
                throw new IllegalArgumentException("Value " + f + " out of range!");
            }
        }
    }

    private void drawNeedle(Canvas canvas) {
        if (this.mNeedleInitialized) {
            float angleForValue = getAngleForValue(this.mCurrentValue);
            canvas.save();
            canvas.rotate(angleForValue, 0.5f, 0.5f);
            setNeedleShadowPosition(angleForValue);
            canvas.drawPath(this.mNeedleLeftPath, this.mNeedleLeftPaint);
            canvas.drawPath(this.mNeedleRightPath, this.mNeedleRightPaint);
            canvas.restore();
            canvas.drawCircle(0.5f, 0.5f, 0.04f, this.mNeedleScrewPaint);
            canvas.drawCircle(0.5f, 0.5f, 0.04f, this.mNeedleScrewBorderPaint);
        }
    }

    private void setNeedleShadowPosition(float f) {
        if (f > 180.0f && f < 360.0f) {
            this.mNeedleRightPaint.setShadowLayer(0.0f, 0.0f, 0.0f, ViewCompat.MEASURED_STATE_MASK);
            this.mNeedleLeftPaint.setShadowLayer(0.01f, -0.005f, 0.005f, Color.argb(127, 0, 0, 0));
            return;
        }
        this.mNeedleLeftPaint.setShadowLayer(0.0f, 0.0f, 0.0f, ViewCompat.MEASURED_STATE_MASK);
        this.mNeedleRightPaint.setShadowLayer(0.01f, 0.005f, -0.005f, Color.argb(127, 0, 0, 0));
    }

    private float getAngleForValue(float f) {
        return (this.mScaleRotation + (((f - this.mScaleStartValue) / this.mSubdivisionValue) * this.mSubdivisionAngle)) % 360.0f;
    }

    private void computeCurrentValue() {
        if (Math.abs(this.mCurrentValue - this.mTargetValue) <= 0.01f) {
            return;
        }
        if (-1 != this.mNeedleLastMoved) {
            float currentTimeMillis = ((float) (System.currentTimeMillis() - this.mNeedleLastMoved)) / 1000.0f;
            float signum = Math.signum(this.mNeedleVelocity);
            if (Math.abs(this.mNeedleVelocity) < 90.0f) {
                this.mNeedleAcceleration = (this.mTargetValue - this.mCurrentValue) * 5.0f;
            } else {
                this.mNeedleAcceleration = 0.0f;
            }
            float f = this.mTargetValue;
            float f2 = this.mCurrentValue;
            float f3 = (f - f2) * 5.0f;
            this.mNeedleAcceleration = f3;
            float f4 = this.mNeedleVelocity;
            float f5 = f2 + (f4 * currentTimeMillis);
            this.mCurrentValue = f5;
            this.mNeedleVelocity = f4 + (f3 * currentTimeMillis);
            if ((f - f5) * signum < signum * 0.01f) {
                this.mCurrentValue = f;
                this.mNeedleVelocity = 0.0f;
                this.mNeedleAcceleration = 0.0f;
                this.mNeedleLastMoved = -1L;
            } else {
                this.mNeedleLastMoved = System.currentTimeMillis();
            }
            invalidate();
            return;
        }
        this.mNeedleLastMoved = System.currentTimeMillis();
        computeCurrentValue();
    }

    public void setTargetValue(float f) {
        if (this.mShowScale || this.mShowRanges) {
            float f2 = this.mScaleStartValue;
            if (f < f2) {
                this.mTargetValue = f2;
            } else {
                float f3 = this.mScaleEndValue;
                if (f > f3) {
                    this.mTargetValue = f3;
                } else {
                    this.mTargetValue = f;
                }
            }
        } else {
            this.mTargetValue = f;
        }
        this.mNeedleInitialized = true;
        invalidate();
    }
}
