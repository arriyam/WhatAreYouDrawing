package com.arriyam.whatareyoudrawing.game

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.arriyam.whatareyoudrawing.R
import com.arriyam.whatareyoudrawing.dataclass.PaintCoordinates
import com.arriyam.whatareyoudrawing.sketch.GuessingView
import com.arriyam.whatareyoudrawing.socket.SocketHandler
import com.google.gson.GsonBuilder

class GuessingActivity : AppCompatActivity() {



    lateinit var guessingViews: GuessingView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_guesser)


        guessingViews=findViewById<GuessingView>(R.id.canvas)
        val button = findViewById<Button>(R.id.button)


//      Socket connection for this activity
        val nSocket= SocketHandler.getSocket();
//
//
//        First paint coordinate received from server
        nSocket.on("mouseBeginA") { args ->
            if (args[0] != null) {
                val mouse = args[0]
//                Log.v("Hen",mouse)
                runOnUiThread {
                    val gson= GsonBuilder().create()
                    val homefeed=gson.fromJson(mouse.toString(),PaintCoordinates::class.java)
                    mouseAndroidStart(homefeed)
                }
            }
        }

//      Paint coordinates between the start and end received from server
        nSocket.on("mouseA") { args ->
            if (args[0] != null) {
                val mouse = args[0]
//                Log.v("Hen",mouse)
                runOnUiThread {
                    val gson= GsonBuilder().create()
                    val homefeed=gson.fromJson(mouse.toString(),PaintCoordinates::class.java)
                    mouseAndroidMiddle(homefeed)
                }
            }
        }

        nSocket.on("mouseEndedA") { args ->
            if (args[0] != null) {
                val mouse = args[0]
//                Log.v("Hen",mouse)
                runOnUiThread {
                    val gson= GsonBuilder().create()
                    val homefeed=gson.fromJson(mouse.toString(),PaintCoordinates::class.java)
//                    mouseAndroidEnd(mouse)
                }
            }
        }


//        Server asked the server to clear Canvas
        nSocket.on("clear") {
            clearCanvas()
        }

        button.setOnClickListener {
            clearCanvas()
            nSocket.emit("clear")
//            nSocket.emit("release")
        }


    }

    fun clearCanvas(){
        guessingViews.clearCanvas()
    }

    private fun mouseAndroidStart(mouse: PaintCoordinates){
        guessingViews.mouseAndroidStart(mouse)
    }

    private fun mouseAndroidMiddle(mouse: PaintCoordinates){
        guessingViews.mouseAndroidMiddle(mouse)
    }

//    private fun mouseAndroidEnd(){
//        guessingViews.mouseAndroidEnd()
//    }


}