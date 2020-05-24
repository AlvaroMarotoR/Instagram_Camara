package com.example.insta.activities

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View


class DrawingView(c: Context): View(c) {


   //Creamos el Paint
    var mPaint: Paint
    var mBitMapPaint : Paint
    private lateinit var mCanvas: Canvas
    var mPath: Path
    lateinit var mBitmap: Bitmap



    init{


        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.isDither = true
        mPaint.color = 0xFFFF0000.toInt()
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeJoin = Paint.Join.ROUND
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.strokeWidth = 20f

        mPath = Path()
        mBitMapPaint = Paint()
        mBitMapPaint.color = Color.RED


    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mBitmap)
    }

    override fun draw(canvas: Canvas) {
        // TODO Auto-generated method stub
        super.draw(canvas)
        canvas.drawBitmap(mBitmap,0f,0f,mBitMapPaint)
        canvas.drawPath(mPath, mPaint)
    }

    private var mX: Float = 0f
    private var mY: Float = 0f
    private val TOUCH_TOLERANCE = 4f


    private fun touch_start(x: Float, y: Float) {
        //mPath.reset();
        mPath.moveTo(x, y)
        mX = x
        mY = y


    }

    private fun touch_move(x: Float, y: Float) {
        val dx = Math.abs(x - mX)
        val dy = Math.abs(y - mY)
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)
            mX = x
            mY = y
        }
    }

    private fun touch_up() {
        mPath.lineTo(mX, mY) // commit the path to our offscreen
        mCanvas.drawPath(mPath, mPaint)
        mPath.reset()
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touch_start(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                touch_move(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                touch_up()
                invalidate()
            }
        }
        return true
    }






}
