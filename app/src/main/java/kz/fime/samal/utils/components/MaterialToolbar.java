package kz.fime.samal.utils.components;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION_CODES;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;

import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.MaterialShapeUtils;


import kz.fime.samal.R;

import static com.google.android.material.theme.overlay.MaterialThemeOverlay.wrap;
import static java.lang.Math.max;

public class MaterialToolbar extends Toolbar {

    private static final int DEF_STYLE_RES = R.style.Widget_MaterialComponents_Toolbar;

    @Nullable private Integer navigationIconTint;
    private boolean titleCentered;
    private boolean subtitleCentered;

    public MaterialToolbar(@NonNull Context context) {
        this(context, null);
    }

    public MaterialToolbar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.toolbarStyle);
    }

    public MaterialToolbar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(wrap(context, attrs, defStyleAttr, DEF_STYLE_RES), attrs, defStyleAttr);
        // Ensure we are using the correctly themed context rather than the context that was passed in.
        context = getContext();

        final TypedArray a =
                ThemeEnforcement.obtainStyledAttributes(
                        context, attrs, R.styleable.MaterialToolbar, defStyleAttr, DEF_STYLE_RES);

        if (a.hasValue(R.styleable.MaterialToolbar_navigationIconTint)) {
            setNavigationIconTint(a.getColor(R.styleable.MaterialToolbar_navigationIconTint, -1));
        }

        titleCentered = a.getBoolean(R.styleable.MaterialToolbar_titleCentered, false);
        subtitleCentered = a.getBoolean(R.styleable.MaterialToolbar_subtitleCentered, false);

        a.recycle();

        initBackground(context);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        maybeCenterTitleViews();
    }

    private void maybeCenterTitleViews() {
        if (!titleCentered && !subtitleCentered) {
            return;
        }

        TextView titleTextView = getTitleTextView(this);
        TextView subtitleTextView = getSubtitleTextView(this);
        if (titleTextView == null && subtitleTextView == null) {
            return;
        }

        Pair<Integer, Integer> titleBoundLimits =
                calculateTitleBoundLimits(titleTextView, subtitleTextView);

        if (titleCentered && titleTextView != null) {
            layoutTitleCenteredHorizontally(titleTextView, titleBoundLimits);
        }

        if (subtitleCentered && subtitleTextView != null) {
            layoutTitleCenteredHorizontally(subtitleTextView, titleBoundLimits);
        }
    }

    @Nullable
    private TextView getTitleTextView(@NonNull Toolbar toolbar) {
        return getTextView(toolbar, toolbar.getTitle());
    }

    @Nullable
    private TextView getSubtitleTextView(@NonNull Toolbar toolbar) {
        return getTextView(toolbar, toolbar.getSubtitle());
    }

    @Nullable
    private TextView getTextView(@NonNull Toolbar toolbar, CharSequence text) {
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View child = toolbar.getChildAt(i);
            if (child instanceof TextView) {
                TextView textView = (TextView) child;
                if (TextUtils.equals(textView.getText(), text)) {
                    return textView;
                }
            }
        }
        return null;
    }

    private Pair<Integer, Integer> calculateTitleBoundLimits(
            @Nullable TextView titleTextView, @Nullable TextView subtitleTextView) {
        int width = getMeasuredWidth();
        int midpoint = width / 2;
        int leftLimit = getPaddingLeft();
        int rightLimit = width - getPaddingRight();

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE && child != titleTextView && child != subtitleTextView) {
                if (child.getRight() < midpoint && child.getRight() > leftLimit) {
                    leftLimit = child.getRight();
                }
                if (child.getLeft() > midpoint && child.getLeft() < rightLimit) {
                    rightLimit = child.getLeft();
                }
            }
        }

        return new Pair<>(leftLimit, rightLimit);
    }

    private void layoutTitleCenteredHorizontally(
            View titleView, Pair<Integer, Integer> titleBoundLimits) {
        int width = getMeasuredWidth();
        int titleWidth = titleView.getMeasuredWidth();

        int titleLeft = width / 2 - titleWidth / 2;
        int titleRight = titleLeft + titleWidth;

        int leftOverlap = max(titleBoundLimits.first - titleLeft, 0);
        int rightOverlap = max(titleRight - titleBoundLimits.second, 0);
        int overlap = max(leftOverlap, rightOverlap);

        if (overlap > 0) {
            titleLeft += overlap;
            titleRight -= overlap;
            titleWidth = titleRight - titleLeft;
            titleView.measure(
                    MeasureSpec.makeMeasureSpec(titleWidth, MeasureSpec.EXACTLY),
                    titleView.getMeasuredHeightAndState());
        }

        titleView.layout(titleLeft, titleView.getTop(), titleRight, titleView.getBottom());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        MaterialShapeUtils.setParentAbsoluteElevation(this);
    }

    @RequiresApi(VERSION_CODES.LOLLIPOP)
    @Override
    public void setElevation(float elevation) {
        super.setElevation(elevation);

        MaterialShapeUtils.setElevation(this, elevation);
    }

    @Override
    public void setNavigationIcon(@Nullable Drawable drawable) {
        super.setNavigationIcon(maybeTintNavigationIcon(drawable));
    }

    /**
     * Sets the color of the toolbar's navigation icon.
     *
     * @see #setNavigationIcon
     */
    public void setNavigationIconTint(@ColorInt int navigationIconTint) {
        this.navigationIconTint = navigationIconTint;
        Drawable navigationIcon = getNavigationIcon();
        if (navigationIcon != null) {
            // Causes navigation icon to be tinted if needed.
            setNavigationIcon(navigationIcon);
        }
    }

    /**
     * Sets whether the title text corresponding to the {@link #setTitle(int)} method should be
     * centered horizontally within the toolbar.
     *
     * <p>Note: it is not recommended to use centered titles in conjunction with a nested custom view,
     * as there may be positioning and overlap issues.
     */
    public void setTitleCentered(boolean titleCentered) {
        if (this.titleCentered != titleCentered) {
            this.titleCentered = titleCentered;
            requestLayout();
        }
    }

    /**
     * Returns whether the title text corresponding to the {@link #setTitle(int)} method should be
     * centered horizontally within the toolbar.
     *
     * @see #setTitleCentered(boolean)
     */
    public boolean isTitleCentered() {
        return titleCentered;
    }

    /**
     * Sets whether the subtitle text corresponding to the {@link #setSubtitle(int)} method should be
     * centered horizontally within the toolbar.
     *
     * <p>Note: it is not recommended to use centered titles in conjunction with a nested custom view,
     * as there may be positioning and overlap issues.
     */
    public void setSubtitleCentered(boolean subtitleCentered) {
        if (this.subtitleCentered != subtitleCentered) {
            this.subtitleCentered = subtitleCentered;
            requestLayout();
        }
    }

    /**
     * Returns whether the subtitle text corresponding to the {@link #setSubtitle(int)} method should
     * be centered horizontally within the toolbar.
     *
     * @see #setSubtitleCentered(boolean)
     */
    public boolean isSubtitleCentered() {
        return subtitleCentered;
    }

    private void initBackground(Context context) {
        Drawable background = getBackground();
        if (background != null && !(background instanceof ColorDrawable)) {
            return;
        }
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable();
        int backgroundColor =
                background != null ? ((ColorDrawable) background).getColor() : Color.TRANSPARENT;
        materialShapeDrawable.setFillColor(ColorStateList.valueOf(backgroundColor));
        materialShapeDrawable.initializeElevationOverlay(context);
        materialShapeDrawable.setElevation(ViewCompat.getElevation(this));
        ViewCompat.setBackground(this, materialShapeDrawable);
    }

    @Nullable
    private Drawable maybeTintNavigationIcon(@Nullable Drawable navigationIcon) {
        if (navigationIcon != null && navigationIconTint != null) {
            Drawable wrappedNavigationIcon = DrawableCompat.wrap(navigationIcon);
            DrawableCompat.setTint(wrappedNavigationIcon, navigationIconTint);
            return wrappedNavigationIcon;
        } else {
            return navigationIcon;
        }
    }
}

