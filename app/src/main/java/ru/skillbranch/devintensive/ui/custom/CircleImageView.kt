package ru.skillbranch.devintensive.ui.custom

import android.annotation.SuppressLint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.AttributeSet
import androidx.annotation.*
import androidx.core.content.ContextCompat.getColor
import ru.skillbranch.devintensive.R
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt
import android.graphics.Paint.Align
import androidx.appcompat.widget.AppCompatImageView

class CircleImageView @JvmOverloads constructor(context: Context, @Nullable attrs: AttributeSet? = null) :
    AppCompatImageView(context, attrs) {

    companion object {
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
        private const val DEFAULT_BORDER_WIDTH = 2
        private const val DEF_PRESS_HIGHLIGHT_COLOR = 0x32000000
    }

    private var mBitmapShader: Shader? = null
    private val mShaderMatrix: Matrix

    private val mBitmapDrawBounds: RectF
    private val mStrokeBounds: RectF

    private var mBitmap: Bitmap? = null

    private val mBitmapPaint: Paint
    private val mStrokePaint: Paint
    private val mPressedPaint: Paint

    private val mInitialized: Boolean
    private var mPressed: Boolean = false
    private var mHighlightEnable: Boolean = false

    var isHighlightEnable: Boolean
        get() = mHighlightEnable
        set(enable) {
            mHighlightEnable = enable
            invalidate()
        }

    var highlightColor: Int
        @ColorInt
        get() = mPressedPaint.color
        set(@ColorInt color) {
            mPressedPaint.color = color
            invalidate()
        }

    @ColorInt
    fun getBorderColor(): Int = mStrokePaint.color

    fun setBorderColor(@ColorRes colorId: Int) {
        mStrokePaint.color = getColor(context, colorId)
        invalidate()
    }

    fun setBorderColor(hex: String) {
        mStrokePaint.color = Color.parseColor(hex)
        invalidate()
    }

    @Dimension
    fun getBorderWidth(): Int = mStrokePaint.strokeWidth.toInt()

    fun setBorderWidth(@Dimension dp: Int) {
        mStrokePaint.strokeWidth = dp.toFloat()
        invalidate()
    }

    init {

        var borderColor = DEFAULT_BORDER_COLOR
        var borderWidth = DEFAULT_BORDER_WIDTH.toFloat()
        var highlightEnable = true
        var highlightColor = DEF_PRESS_HIGHLIGHT_COLOR

        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, 0, 0)

            borderColor =
                a.getColor(R.styleable.CircleImageView_cv_borderColor, DEFAULT_BORDER_COLOR)
            borderWidth =
                a.getDimensionPixelSize(
                    R.styleable.CircleImageView_cv_borderWidth,
                    DEFAULT_BORDER_WIDTH
                ).toFloat()
            highlightEnable = a.getBoolean(R.styleable.CircleImageView_highlightEnable, true)
            highlightColor =
                a.getColor(R.styleable.CircleImageView_highlightColor, DEF_PRESS_HIGHLIGHT_COLOR)

            a.recycle()
        }

        mShaderMatrix = Matrix()
        mBitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mStrokeBounds = RectF()
        mBitmapDrawBounds = RectF()
        mStrokePaint.color = borderColor
        mStrokePaint.style = Paint.Style.STROKE
        mStrokePaint.strokeWidth = borderWidth

        mPressedPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPressedPaint.color = highlightColor
        mPressedPaint.style = Paint.Style.FILL

        mHighlightEnable = highlightEnable
        mInitialized = true

        setupBitmap()
    }

    override fun setImageResource(@DrawableRes resId: Int) {
        super.setImageResource(resId)
        setupBitmap()
    }

    override fun setImageDrawable(@Nullable drawable: Drawable?) {
        super.setImageDrawable(drawable)
        setupBitmap()
    }

    override fun setImageBitmap(@Nullable bm: Bitmap) {
        super.setImageBitmap(bm)
        setupBitmap()
    }

    override fun setImageURI(@Nullable uri: Uri?) {
        super.setImageURI(uri)
        setupBitmap()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val halfStrokeWidth = mStrokePaint.strokeWidth / 2f
        updateCircleDrawBounds(mBitmapDrawBounds)
        mStrokeBounds.set(mBitmapDrawBounds)
        mStrokeBounds.inset(halfStrokeWidth, halfStrokeWidth)

        updateBitmapSize()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        var processed = false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!isInCircle(event.x, event.y)) {
                    return false
                }
                processed = true
                mPressed = true
                invalidate()
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                processed = true
                mPressed = false
                invalidate()
                if (!isInCircle(event.x, event.y)) {
                    return false
                }
            }
        }
        return super.onTouchEvent(event) || processed
    }

    override fun onDraw(canvas: Canvas) {
        drawBitmap(canvas)
        drawStroke(canvas)
        drawHighlight(canvas)
    }

    protected fun drawHighlight(canvas: Canvas) {
        if (mHighlightEnable && mPressed) {
            canvas.drawOval(mBitmapDrawBounds, mPressedPaint)
        }
    }

    protected fun drawStroke(canvas: Canvas) {
        if (mStrokePaint.strokeWidth > 0f) {
            canvas.drawOval(mStrokeBounds, mStrokePaint)
        }
    }

    protected fun drawBitmap(canvas: Canvas) {
        canvas.drawOval(mBitmapDrawBounds, mBitmapPaint)
    }

    protected fun updateCircleDrawBounds(bounds: RectF) {
        val contentWidth = (width - paddingLeft - paddingRight).toFloat()
        val contentHeight = (height - paddingTop - paddingBottom).toFloat()

        var left = paddingLeft.toFloat()
        var top = paddingTop.toFloat()
        if (contentWidth > contentHeight) {
            left += (contentWidth - contentHeight) / 2f
        } else {
            top += (contentHeight - contentWidth) / 2f
        }

        val diameter = min(contentWidth, contentHeight)
        bounds.set(left, top, left + diameter, top + diameter)
    }

    private fun setupBitmap() {
        if (!mInitialized) {
            return
        }
        mBitmap = getBitmapFromDrawable(drawable)
        if (mBitmap == null) {
            return
        }

        mBitmapShader = BitmapShader(mBitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        mBitmapPaint.shader = mBitmapShader

        updateBitmapSize()
    }

    private fun updateBitmapSize() {
        if (mBitmap == null) return

        val dx: Float
        val dy: Float
        val scale: Float

        // scale up/down with respect to this view size and maintain aspect ratio
        // translate bitmap position with dx/dy to the center of the image
        if (mBitmap!!.width < mBitmap!!.height) {
            scale = mBitmapDrawBounds.width() / mBitmap!!.width.toFloat()
            dx = mBitmapDrawBounds.left
            dy =
                mBitmapDrawBounds.top - mBitmap!!.height * scale / 2f + mBitmapDrawBounds.width() / 2f
        } else {
            scale = mBitmapDrawBounds.height() / mBitmap!!.height.toFloat()
            dx =
                mBitmapDrawBounds.left - mBitmap!!.width * scale / 2f + mBitmapDrawBounds.width() / 2f
            dy = mBitmapDrawBounds.top
        }
        mShaderMatrix.setScale(scale, scale)
        mShaderMatrix.postTranslate(dx, dy)
        mBitmapShader!!.setLocalMatrix(mShaderMatrix)
    }

    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return null
        }

        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    private fun isInCircle(x: Float, y: Float): Boolean {
        // find the distance between center of the view and x,y point
        val distance = sqrt(
            (mBitmapDrawBounds.centerX() - x).toDouble().pow(2.0) + (mBitmapDrawBounds.centerY() - y).toDouble().pow(
                2.0
            )
        )
        return distance <= mBitmapDrawBounds.width() / 2
    }

    class InitialsDrawable(color: Int, private val text: String) : ColorDrawable(color) {

        private val defaultWidth = 120
        private val defaultHeight = 120
        private var paint: Paint = Paint()

        init {
            paint.color = Color.WHITE
            paint.textSize = 55f
            paint.isAntiAlias = true
            paint.isFakeBoldText = true
            paint.setShadowLayer(6f, 0f, 0f, Color.WHITE)
            paint.style = Paint.Style.FILL
            paint.textAlign = Align.CENTER
        }

        override fun draw(canvas: Canvas) {
            super.draw(canvas)
            val xPos = bounds.width() / 2f
            val yPos = (bounds.height() / 2f - (paint.descent() + paint.ascent()) / 2f)

            canvas.drawText(text, xPos, yPos, paint)
        }

        override fun setAlpha(alpha: Int) {
            paint.alpha = alpha
        }

        override fun getOpacity(): Int {
            return PixelFormat.TRANSLUCENT
        }

        override fun setColorFilter(cf: ColorFilter) {
            paint.colorFilter = cf
        }

        override fun getIntrinsicWidth(): Int = defaultWidth

        override fun getIntrinsicHeight(): Int = defaultHeight
    }
}
