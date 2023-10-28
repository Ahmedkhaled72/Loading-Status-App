package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var widthSize = 0
    private var heightSize = 0
    private var textWidth = 0f

    private var textSize: Float = resources.getDimension(R.dimen.default_text_size)
    private var circleoffsetx = textSize / 2
    private var raduis = 10f

    //shapeColor
    private var maincolor   = ContextCompat.getColor(context, R.color.colorPrimary)
    private var loadbutton  = ContextCompat.getColor(context, R.color.colorPrimaryDark)
    private var circleColor = ContextCompat.getColor(context, R.color.colorAccent)

    //Progress
    private var progressW = 0f
    private var progressC = 0f


    private var buttonText:String = resources.getString(R.string.buttunTitle)


    private var valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

        when(new)
        {
            ButtonState.Loading -> {

                valueAnimator = ValueAnimator.ofFloat(0f, 100f)
                valueAnimator.setDuration(3000)

                valueAnimator.addUpdateListener {  animation ->
                    progressW  = animation.animatedValue as Float
                    progressC = animation.animatedValue as Float
                    invalidate()
                }

                valueAnimator.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                         buttonText = "DowenLoad"
                         progressW = 0f
                         progressC = 0f
                         valueAnimator.cancel()
                         invalidate()
                    }
                })

                valueAnimator.repeatMode = ValueAnimator.REVERSE
                valueAnimator.repeatCount = ValueAnimator.INFINITE
                invalidate()

            }

            ButtonState.Completed -> {
                valueAnimator.end()
                invalidate()
            }


        }

    }


    init {
            isClickable = true
    }

     //todo paint object
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }


    override fun performClick(): Boolean {
         super.performClick()
        if(buttonState == ButtonState.Completed)
        {
            buttonState = ButtonState.Loading
            valueAnimator.start()
        }

         return true
    }



    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        //Custom your Shape
        //Draw Main Rectangle
        paint.color = maincolor
        val rectangle: RectF = RectF(0f,0f,widthSize.toFloat(),heightSize.toFloat())
        canvas?.drawRect(rectangle, paint)

        //DrawTextInside canvas
        paint.color = Color.WHITE
        canvas?.drawText(buttonText, (widthSize/2).toFloat(), (heightSize/2).toFloat() , paint)


         if (buttonState == ButtonState.Loading) {
                  //Draw Rectangle With Animation
                 paint.color = loadbutton
                 val rectPross: RectF = RectF(0f, 0f, (width*(progressW /100)).toFloat(), heightSize.toFloat())
                 canvas?.drawRect(rectPross, paint)

                paint.color = circleColor
                val raduies:RectF = RectF((widthSize/2f) + 270,(heightSize/2f)-50,(widthSize/2f)+370,(heightSize/2f)+50)
                canvas?.drawArc(raduies, 0f, (progressC * 360f).toFloat() /100f,true, paint)

                //DrawTextInside canvas
                 paint.color = Color.WHITE
                 buttonText = "We are Dowenloading"
                 canvas?.drawText(buttonText, (widthSize/2).toFloat(), (heightSize/2).toFloat() , paint)



         }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    fun changeStateFun(btnState: ButtonState)
    {
        buttonState = btnState
    }

}