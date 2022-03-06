package edu.upf.tickeep.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import edu.upf.tickeep.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        Thread.sleep(2000)
        setTheme(R.style.Theme_Tickeep)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonSignup: Button = findViewById(R.id.btn_signup)
        val buttonSignIn: Button = findViewById(R.id.btn_signin)

        session()
        buttonSignup.setOnClickListener {
            val intent = Intent(this, register::class.java)
            startActivity(intent)
            finish()
        }
        buttonSignIn.setOnClickListener {
            val intent = Intent(this, login::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun session() {
        val prefs = getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email",null)

        if(email != null){

            val intent = Intent(this, nav_activity::class.java)
            startActivity(intent)
            finish()
        }
    }

}