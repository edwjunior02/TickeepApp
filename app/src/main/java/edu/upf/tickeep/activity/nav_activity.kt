package edu.upf.tickeep.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.facebook.login.LoginManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import edu.upf.tickeep.R
import edu.upf.tickeep.fragments.expireds
import edu.upf.tickeep.fragments.favourites
import edu.upf.tickeep.fragments.home
import edu.upf.tickeep.fragments.importants
import edu.upf.tickeep.model.User
import kotlinx.android.synthetic.main.activity_nav.*
import kotlinx.android.synthetic.main.content_main.*


class nav_activity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{
    lateinit var txtuser: TextView
    lateinit var txtEmail: TextView
    lateinit var  navigationView: NavigationView
    lateinit var imgNav:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_nav)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar,
            R.string.open,
            R.string.close
        )
        toggle.isDrawerIndicatorEnabled = true
        drawer_layout.addDrawerListener(toggle)
        navigationView = findViewById(R.id.nav_menu)
        val header = navigationView.getHeaderView(0)


        toggle.syncState()

        nav_menu.setNavigationItemSelectedListener (this)

        changeFragment(home())
    }
    override fun onBackPressed() {
        val fm: FragmentManager = supportFragmentManager
        if (fm.backStackEntryCount > 0) {

            fm.popBackStack()
        } else {

            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.nav_menuoptions, menu)
        txtuser = findViewById(R.id.txt_userNav)
        txtEmail = findViewById(R.id.txt_emailNav)
        //imgNav = findViewById(R.id.img_navheader)
        //imgNav.setImageResource(R.mipmap.logo)

        val c = edu.upf.tickeep.utils.Constants()
        c.firebaseFirestore.collection("users").document(c.firebaseAuth.currentUser?.uid.toString()).get().addOnSuccessListener {
                document->
            var user = document.toObject<User>()
            txtEmail.text = user?.email.toString()
            txtuser.text = user?.user.toString()
        }
        return false
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawer_layout.closeDrawer(GravityCompat.START)
        when (item.itemId) {
            R.id.mytickets -> {
                changeFragment(home())
            }
            R.id.importants -> {
                changeFragment(importants())
            }
            R.id.favourites -> {
                changeFragment(favourites())
            }
            R.id.expireds -> {
                changeFragment(expireds())
            }
            R.id.logout -> {
                val prefs2 = getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE)
                val prov = prefs2.getString("prov",null)
                if(prov == "FACEBOOK"){
                    LoginManager.getInstance().logOut()

                }
                val prefs = getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE).edit()
                prefs.clear()
                prefs.apply()
                Firebase.auth.signOut()

                val intent = Intent(this, login::class.java)
                startActivity(intent)
                finish()
            }
        }
        return true
    }
    fun changeFragment(frag: Fragment){
        val fragment = supportFragmentManager.beginTransaction()
        fragment.replace(R.id.fragment_container,frag).commit()
    }
}