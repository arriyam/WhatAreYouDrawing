package com.arriyam.whatareyoudrawing.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.arriyam.whatareyoudrawing.R
import com.arriyam.whatareyoudrawing.dataclass.PaintCoordinates
import com.arriyam.whatareyoudrawing.dataclass.User
import com.arriyam.whatareyoudrawing.game.GuessingActivity
import com.arriyam.whatareyoudrawing.socket.SocketHandler
import com.google.gson.GsonBuilder

class LoginActivity: AppCompatActivity() {

    //    lateinit var listData:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //        Connecting to the socket using SocketHandler Object
        SocketHandler.setSocket()
        //      Socket connection for this activity
        val nSocket= SocketHandler.getSocket();

        nSocket.connect()

        val editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        val btnJoin = findViewById<Button>(R.id.buttonJoin)



        btnJoin.setOnClickListener {

            if (nSocket.connected()) {
                val username=editTextUsername.text.toString()
                if (username!="") {
                    var bob = User(username,-1)
                    val gson= GsonBuilder().create()
                    val data2Json=gson.toJson(bob, User::class.java)
                    nSocket.emit("join", data2Json)

                    val intent= Intent(this, UsernameActivity::class.java)
                    intent.putExtra("Username_Data", username)

                    startActivity(intent)
                }
                else{
                    Toast.makeText(this,"Username can not be blank.", Toast.LENGTH_SHORT).show()
                }


            } else {
                Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show()
            }

            nSocket.on("gameState") { args ->
                if (args[0] != null) {
                    val gameState = args[0] as Int
                    runOnUiThread {

                        val intent= Intent(this, GuessingActivity::class.java)
                        startActivity(intent)

                    }
                }
            }

        }

    }
}