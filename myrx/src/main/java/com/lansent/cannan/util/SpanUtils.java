package com.lansent.cannan.util;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.text.Layout.Alignment;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.MaskFilterSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.graphics.BlurMaskFilter.Blur;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 16/12/13
 *     desc  : SpannableString相关工具类
 * </pre>
 */
public final class SpanUtils {

    private static final int DEFAULT_COLOR = 0xFEFFFFFF;

    public static final int ALIGN_BOTTOM = 0;
    public static final int ALIGN_BASELINE = 1;
    public static final int ALIGN_CENTER = 2;
    public static final int ALIGN_TOP = 3;

    @IntDef({ALIGN_BOTTOM, ALIGN_BASELINE, ALIGN_CENTER, ALIGN_TOP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Align {
    }

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private CharSequence mText;
    private int flag;
    private int foregroundColor;
    private int backgroundColor;
    private int lineHeight;
    private int alignLine;
    private int quoteColor;
    private int stripeWidth;
    private int quoteGapWidth;
    private int first;
    private int rest;
    private int bulletColor;
    private int bulletRadius;
    private int bulletGapWidth;
    private Bitmap iconMarginBitmap;
    private Drawable iconMarginDrawable;
    private Uri iconMarginUri;
    private int iconMarginResourceId;
    private int iconMarginGapWidth;
    private int alignIconMargin;
    private int fontSize;
    private boolean fontSizeIsDp;
    private float proportion;
    private float xProportion;
    private boolean isStrikethrough;
    private boolean isUnderline;
    private boolean isSuperscript;
    private boolean isSubscript;
    private boolean isBold;
    private boolean isItalic;
    private boolean isBoldItalic;
    private String fontFamily;
    private Typeface typeface;
    private Alignment alignment;
    private ClickableSpan clickSpan;
    private String url;
    private float blurRadius;
    private Blur style;
    private Shader shader;
    private Object[] spans;

    private static SpannableStringBuilder mBuilder;

    private int mType;
    private final int mTypeCharSequence = 0;
    private final int mTypeImage = 1;
    private final int mTypeSpace = 2;


    public SpanUtils() {
        mBuilder = new SpannableStringBuilder();
        mText = "";
        setDefault();
    }

    private void setDefault() {
        flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
        foregroundColor = DEFAULT_COLOR;
        backgroundColor = DEFAULT_COLOR;
        lineHeight = -1;
        quoteColor = DEFAULT_COLOR;
        first = -1;
        bulletColor = DEFAULT_COLOR;
        iconMarginBitmap = null;
        iconMarginDrawable = null;
        iconMarginUri = null;
        iconMarginResourceId = -1;
        iconMarginGapWidth = -1;
        fontSize = -1;
        proportion = -1;
        xProportion = -1;
        isStrikethrough = false;
        isUnderline = false;
        isSuperscript = false;
        isSubscript = false;
        isBold = false;
        isItalic = false;
        isBoldItalic = false;
        fontFamily = null;
        typeface = null;
        alignment = null;
        clickSpan = null;
        url = null;
        blurRadius = -1;
        shader = null;
        spans = null;
    }

    /**
     * 设置标识
     *
     * @param flag <ul>
     *             <li>{@link Spanned#SPAN_INCLUSIVE_EXCLUSIVE}</li>
     *             <li>{@link Spanned#SPAN_INCLUSIVE_INCLUSIVE}</li>
     *             <li>{@link Spanned#SPAN_EXCLUSIVE_EXCLUSIVE}</li>
     *             <li>{@link Spanned#SPAN_EXCLUSIVE_INCLUSIVE}</li>
     *             </ul>
     * @return {@link SpanUtils}
     */
    public SpanUtils setFlag(final int flag) {
        this.flag = flag;
        return this;
    }

    /**
     * 设置前景色
     *
     * @param color 前景色
     * @return {@link SpanUtils}
     */
    public SpanUtils setForegroundColor(  final int color) {
        this.foregroundColor = color;
        return this;
    }

    /**
     * 设置背景色
     *
     * @param color 背景色
     * @return {@link SpanUtils}
     */
    public SpanUtils setBackgroundColor(  final int color) {
        this.backgroundColor = color;
        return this;
    }

    /**
     * 设置引用线的颜色
     *
     * @param color 引用线的颜色
     * @return {@link SpanUtils}
     */
    public SpanUtils setQuoteColor(  final int color) {
        return setQuoteColor(color, 2, 2);
    }

    /**
     * 设置引用线的颜色
     *
     * @param color       引用线的颜色
     * @param stripeWidth 引用线线宽
     * @param gapWidth    引用线和文字间距
     * @return {@link SpanUtils}
     */
    public SpanUtils setQuoteColor(  final int color,   final int stripeWidth,  final int gapWidth) {
        this.quoteColor = color;
        this.stripeWidth = stripeWidth;
        this.quoteGapWidth = gapWidth;
        return this;
    }

    /**
     * 设置缩进
     *
     * @param first 首行缩进
     * @param rest  剩余行缩进
     * @return {@link SpanUtils}
     */
    public SpanUtils setLeadingMargin(  final int first, final int rest) {
        this.first = first;
        this.rest = rest;
        return this;
    }

    /**
     * 设置字体尺寸
     *
     * @param size 尺寸
     * @return {@link SpanUtils}
     */
    public SpanUtils setFontSize( final int size) {
        return setFontSize(size, false);
    }

    /**
     * 设置字体尺寸
     *
     * @param size 尺寸
     * @param isDp 是否使用dip
     * @return {@link SpanUtils}
     */
    public SpanUtils setFontSize( final int size, final boolean isDp) {
        this.fontSize = size;
        this.fontSizeIsDp = isDp;
        return this;
    }

    /**
     * 设置字体比例
     *
     * @param proportion 比例
     * @return {@link SpanUtils}
     */
    public SpanUtils setFontProportion( final float proportion) {
        this.proportion = proportion;
        return this;
    }

    /**
     * 设置字体横向比例
     *
     * @param proportion 比例
     * @return {@link SpanUtils}
     */
    public SpanUtils setFontXProportion(  final float proportion) {
        this.xProportion = proportion;
        return this;
    }

    /**
     * 设置删除线
     *
     * @return {@link SpanUtils}
     */
    public SpanUtils setStrikethrough() {
        this.isStrikethrough = true;
        return this;
    }

    /**
     * 设置下划线
     *
     * @return {@link SpanUtils}
     */
    public SpanUtils setUnderline() {
        this.isUnderline = true;
        return this;
    }

    /**
     * 设置上标
     *
     * @return {@link SpanUtils}
     */
    public SpanUtils setSuperscript() {
        this.isSuperscript = true;
        return this;
    }

    /**
     * 设置下标
     *
     * @return {@link SpanUtils}
     */
    public SpanUtils setSubscript() {
        this.isSubscript = true;
        return this;
    }

    /**
     * 设置粗体
     *
     * @return {@link SpanUtils}
     */
    public SpanUtils setBold() {
        isBold = true;
        return this;
    }

    /**
     * 设置斜体
     *
     * @return {@link SpanUtils}
     */
    public SpanUtils setItalic() {
        isItalic = true;
        return this;
    }

    /**
     * 设置粗斜体
     *
     * @return {@link SpanUtils}
     */
    public SpanUtils setBoldItalic() {
        isBoldItalic = true;
        return this;
    }

    /**
     * 设置字体系列
     *
     * @param fontFamily 字体系列
     *                   <ul>
     *                   <li>monospace</li>
     *                   <li>serif</li>
     *                   <li>sans-serif</li>
     *                   </ul>
     * @return {@link SpanUtils}
     */
    public SpanUtils setFontFamily(@NonNull final String fontFamily) {
        this.fontFamily = fontFamily;
        return this;
    }

    /**
     * 设置字体
     *
     * @param typeface 字体
     * @return {@link SpanUtils}
     */
    public SpanUtils setTypeface(@NonNull final Typeface typeface) {
        this.typeface = typeface;
        return this;
    }

    /**
     * 设置对齐
     *
     * @param alignment 对其方式
     *                  <ul>
     *                  <li>{@link Alignment#ALIGN_NORMAL}正常</li>
     *                  <li>{@link Alignment#ALIGN_OPPOSITE}相反</li>
     *                  <li>{@link Alignment#ALIGN_CENTER}居中</li>
     *                  </ul>
     * @return {@link SpanUtils}
     */
    public SpanUtils setAlign(@NonNull final Alignment alignment) {
        this.alignment = alignment;
        return this;
    }

    /**
     * 设置点击事件
     * <p>需添加view.setMovementMethod(LinkMovementMethod.getDefault())</p>
     *
     * @param clickSpan 点击事件
     * @return {@link SpanUtils}
     */
    public SpanUtils setClickSpan(@NonNull final ClickableSpan clickSpan) {
        this.clickSpan = clickSpan;
        return this;
    }

    /**
     * 设置超链接
     * <p>需添加view.setMovementMethod(LinkMovementMethod.getDefault())</p>
     *
     * @param url 超链接
     * @return {@link SpanUtils}
     */
    public SpanUtils setUrl(@NonNull final String url) {
        this.url = url;
        return this;
    }


    /**
     * 追加样式字符串
     *
     * @param text 样式字符串文本
     * @return {@link SpanUtils}
     */
    public SpanUtils append(@NonNull final CharSequence text) {
        apply(mTypeCharSequence);
        mText = text;
        return this;
    }

    /**
     * 追加一行
     *
     * @return {@link SpanUtils}
     */
    public SpanUtils appendLine() {
        apply(mTypeCharSequence);
        mText = LINE_SEPARATOR;
        return this;
    }

    /**
     * 追加一行样式字符串
     *
     * @return {@link SpanUtils}
     */
    public SpanUtils appendLine(@NonNull final CharSequence text) {
        apply(mTypeCharSequence);
        mText = text + LINE_SEPARATOR;
        return this;
    }


    private void apply(final int type) {
        applyLast();
        mType = type;
    }

    /**
     * 创建样式字符串
     *
     * @return 样式字符串
     */
    public SpannableStringBuilder create() {
        applyLast();
        return mBuilder;
    }

    /**
     * 设置上一次的样式
     */
    private void applyLast() {
        if (mType == mTypeCharSequence) {
            updateCharCharSequence();
        } else if (mType == mTypeImage) {
        } else if (mType == mTypeSpace) {
        }
        setDefault();
    }

    private void updateCharCharSequence() {
        if (mText.length() == 0) return;
        int start = mBuilder.length();
        mBuilder.append(mText);
        int end = mBuilder.length();
        if (foregroundColor != DEFAULT_COLOR) {
            mBuilder.setSpan(new ForegroundColorSpan(foregroundColor), start, end, flag);
        }
        if (backgroundColor != DEFAULT_COLOR) {
            mBuilder.setSpan(new BackgroundColorSpan(backgroundColor), start, end, flag);
        }
        if (first != -1) {
            mBuilder.setSpan(new LeadingMarginSpan.Standard(first, rest), start, end, flag);
        }
        if (quoteColor != DEFAULT_COLOR) {
        }
        if (bulletColor != DEFAULT_COLOR) {
        }
        if (iconMarginGapWidth != -1) {
            if (iconMarginBitmap != null) {
            } else if (iconMarginDrawable != null) {
            } else if (iconMarginUri != null) {
            } else if (iconMarginResourceId != -1) {
            }
        }
        if (fontSize != -1) {
            mBuilder.setSpan(new AbsoluteSizeSpan(fontSize, fontSizeIsDp), start, end, flag);
        }
        if (proportion != -1) {
            mBuilder.setSpan(new RelativeSizeSpan(proportion), start, end, flag);
        }
        if (xProportion != -1) {
            mBuilder.setSpan(new ScaleXSpan(xProportion), start, end, flag);
        }
        if (lineHeight != -1) {
        }
        if (isStrikethrough) {
            mBuilder.setSpan(new StrikethroughSpan(), start, end, flag);
        }
        if (isUnderline) {
            mBuilder.setSpan(new UnderlineSpan(), start, end, flag);
        }
        if (isSuperscript) {
            mBuilder.setSpan(new SuperscriptSpan(), start, end, flag);
        }
        if (isSubscript) {
            mBuilder.setSpan(new SubscriptSpan(), start, end, flag);
        }
        if (isBold) {
            mBuilder.setSpan(new StyleSpan(Typeface.BOLD), start, end, flag);
        }
        if (isItalic) {
            mBuilder.setSpan(new StyleSpan(Typeface.ITALIC), start, end, flag);
        }
        if (isBoldItalic) {
            mBuilder.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), start, end, flag);
        }
        if (fontFamily != null) {
            mBuilder.setSpan(new TypefaceSpan(fontFamily), start, end, flag);
        }
        if (typeface != null) {
        }
        if (alignment != null) {
            mBuilder.setSpan(new AlignmentSpan.Standard(alignment), start, end, flag);
        }
        if (clickSpan != null) {
            mBuilder.setSpan(clickSpan, start, end, flag);
        }
        if (url != null) {
            mBuilder.setSpan(new URLSpan(url), start, end, flag);
        }
        if (blurRadius != -1) {
            mBuilder.setSpan(new MaskFilterSpan(new BlurMaskFilter(blurRadius, style)), start, end, flag);
        }
        if (shader != null) {
        }
        if (spans != null) {
            for (Object span : spans) {
                mBuilder.setSpan(span, start, end, flag);
            }
        }
    }
}