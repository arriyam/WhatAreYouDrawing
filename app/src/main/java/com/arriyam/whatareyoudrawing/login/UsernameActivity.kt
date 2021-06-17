package com.arriyam.whatareyoudrawing.login

import android.R
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.arriyam.whatareyoudrawing.game.GuessingActivity
import com.arriyam.whatareyoudrawing.socket.SocketHandler

class UsernameActivity : AppCompatActivity() {

//    private lateinit var listView:ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(com.arriyam.whatareyoudrawing.R.layout.activity_username)


//      Socket connection for this activity
        val nSocket= SocketHandler.getSocket();

        val textViewUserCount = findViewById<TextView>(com.arriyam.whatareyoudrawing.R.id.textViewUserCount)
        val textViewName= findViewById<TextView>(com.arriyam.whatareyoudrawing.R.id.textViewName)



//        Data from LoginActivity
        val username = intent.getStringExtra("Username_Data")


//      Switches to Sketching activity once timer is done


        nSocket.on("gameState") { args ->
            if (args[0] != null) {
                val gameState = args[0] as Int
                runOnUiThread {

                    val intent= Intent(this, GuessingActivity::class.java)
                    startActivity(intent)

                }
            }
        }



//        Displays number of Players connected.
        nSocket.on("numberConnected") { args ->
            if (args[0] != null) {
                val data = args[0] as Int


                runOnUiThread {
                    textViewUserCount.text="Players in lobby: "+data.toString()
                }
            }
        }

        textViewName.text = "Hello, " + username


    }
}