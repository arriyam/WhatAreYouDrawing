package com.arriyam.whatareyoudrawing.sketch

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.arriyam.whatareyoudrawing.dataclass.CanvasSize
import com.arriyam.whatareyoudrawing.dataclass.ObjectConverter
import com.arriyam.whatareyoudrawing.dataclass.PaintCoordinates
import com.arriyam.whatareyoudrawing.socket.SocketHandler
import com.google.gson.GsonBuilder

class GuessingView(context: Context, attrs: AttributeSet?) :
    View(context, attrs) {
    lateinit var width:Integer
    lateinit var height:Integer
    private lateinit var mBitmap: Bitmap
    private lateinit var mCanvas: Canvas
    private lateinit var mPath: Path
    private lateinit var mPaint: Paint
    private var mX = 0f
    private var mY = 0f
    var px= 0f
    var py= 0f

    companion object {
        private const val TOLERANCE = 5f
    }

    init {
        mPath = Path()
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.color = Color.BLUE
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeJoin = Paint.Join.ROUND
        mPaint.strokeWidth = 7f
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        val displayHeight= CanvasSize.getCanvasHeight()
        val displayWidth=CanvasSize.getCanvasWidth()
        val nSocket= SocketHandler.getSocket();
        when (event.action) {

//            Where pointer first clicks
            MotionEvent.ACTION_DOWN -> {
                startTouch(x, y)
                val bob= PaintCoordinates(x,y,px,py,displayWidth,displayHeight)
                val gson= GsonBuilder().create()
                val data2Json=gson.toJson(bob,PaintCoordinates::class.java)
                nSocket.emit("mouseBegin",data2Json)
                px=x
                py=y
                invalidate()
            }
//            Holding on to pointer
            MotionEvent.ACTION_MOVE -> {
//              width first second height

                val bob= PaintCoordinates(x,y,px,py,displayWidth,displayHeight)

                val gson= GsonBuilder().create()
                val data2Json=gson.toJson(bob,PaintCoordinates::class.java)
                nSocket.emit("mouse",data2Json)
                px=x
                py=y
                moveTouch(x, y)
                invalidate()
            }
//            Last point the pointer is holding
            MotionEvent.ACTION_UP -> {
                val bob= PaintCoordinates(x,y,px,py,displayWidth,displayHeight)
                val gson= GsonBuilder().create()
                val data2Json=gson.toJson(bob,PaintCoordinates::class.java)
                nSocket.emit("mouseEnded",data2Json)
//                upTouch()
                invalidate()
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(mPath, mPaint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mBitmap)
    }

    private fun startTouch(x: Float, y: Float) {
        mPath.moveTo(x, y)
        mX = x
        mY = y
    }

    private fun moveTouch(x: Float, y: Float) {
        val dx = Math.abs(x - mX)
        val dy = Math.abs(y - mY)
        if (dx >= TOLERANCE || dy >= TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)
            mX = x
            mY = y
        }
    }


    fun mouseAndroidStart(mouse:PaintCoordinates) {
        val mouseAndroid= ObjectConverter.convertPaintCoordinates(mouse)
        Log.i("bob","hi")
        startTouch(mouseAndroid.x, mouseAndroid.y)
        invalidate()
    }
    fun mouseAndroidMiddle(mouse:PaintCoordinates) {
        val mouseAndroid= ObjectConverter.convertPaintCoordinates(mouse)
        Log.i("ad",mouseAndroid.toString())
        moveTouch(mouseAndroid.x, mouseAndroid.y)
        invalidate()
    }
    fun clearCanvas() {
        mPath.reset()
        invalidate()
    }
}

