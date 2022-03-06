package edu.upf.tickeep.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import edu.upf.tickeep.R
import edu.upf.tickeep.model.User


class login : AppCompatActivity() {
    lateinit var txtEmail: EditText
    lateinit var txtPasword: EditText
    lateinit var btnGoogle: Button
    lateinit var btnFacebook: Button
    lateinit var  buttonSignIn:Button
    lateinit var btnReg:Button
    lateinit var  btnTwitter:Button

    private val GOOGLE_SING_IN= 100
    private  val callBackManager = CallbackManager.Factory.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginaccount)
        buttonSignIn= findViewById(R.id.btn_singinLogin)
        btnReg = findViewById(R.id.btn_registerFromLogin)
        txtEmail=findViewById(R.id.edit_emailLogin)
        txtPasword = findViewById(R.id.edit_passwordLogin)
        btnGoogle = findViewById(R.id.btn_google)
        btnFacebook = findViewById(R.id.btn_facebook)
        btnTwitter = findViewById(R.id.btn_twitter)


        //APP PREFEFERENCES
        val prefs = getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE).edit()

        val c = edu.upf.tickeep.utils.Constants()
        btnReg.setOnClickListener {
            val intent = Intent(this, register::class.java)
            startActivity(intent)

        }
        buttonSignIn.setOnClickListener {
            if(!(txtEmail.text.isEmpty() && txtPasword.text.isEmpty())){
                c.firebaseAuth.signInWithEmailAndPassword(txtEmail.text.toString(), txtPasword.text.toString()).addOnCompleteListener {
                    if(it.isSuccessful){
                        prefs.putString("email", txtEmail.text.toString())
                        prefs.apply()
                        val intent = Intent(this, nav_activity::class.java)
                        startActivity(intent)
                        finish()

                    }else{
                        Toast.makeText(
                            applicationContext,
                            resources.getString(R.string.failLogin),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }else{
                Toast.makeText(this, R.string.emptyStringsError, Toast.LENGTH_SHORT).show()
            }

        }
        btnGoogle.setOnClickListener {
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1045681981837-sg07in8tb883a9tvmfnjg4e2n5hi68a2.apps.googleusercontent.com")
                .requestEmail()
                .requestProfile()
                .build()
            val googleClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent, GOOGLE_SING_IN)

        }
        btnFacebook.setOnClickListener { v->
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email","public_profile"))
            LoginManager.getInstance().registerCallback(callBackManager,
                object :FacebookCallback<LoginResult>{
                    override fun onSuccess(result: LoginResult?) {
                        result?.let {
                            val token= it.accessToken
                            val credential = FacebookAuthProvider.getCredential(token.token)

                            FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                                if(it.isSuccessful){
                                    val prefs = getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE).edit()
                                    prefs.putString("email", it.result?.user?.email)
                                    prefs.putString("prov", "FACEBOOK")
                                    prefs.apply()
                                    val userRef = FirebaseAuth.getInstance().currentUser
                                        ?.let { it1 -> FirebaseFirestore.getInstance().collection("users").document(it1.uid) }


                                    val user = User(
                                        it.result?.user?.email.toString(),
                                        it.result?.user?.displayName.toString(),
                                        it.result?.user?.phoneNumber.toString()
                                    )

                                    userRef?.set(user)

                                    setResult(RESULT_OK)

                                    val intent = Intent(v.context, nav_activity::class.java)
                                    startActivity(intent)
                                    finish()

                                }else{
                                    Toast.makeText(
                                        applicationContext,
                                        resources.getString(R.string.failLogin),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    }}

                    override fun onCancel() {

                    }

                    override fun onError(error: FacebookException?) {
                        Toast.makeText(
                            applicationContext,
                            resources.getString(R.string.error_facebook),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

            })
        }

        btnTwitter.setOnClickListener {
            val provider = OAuthProvider.newBuilder("twitter.com")
            val pendingResultTask: Task<AuthResult>? = FirebaseAuth.getInstance().getPendingAuthResult()
            if (pendingResultTask != null) {
                // There's something already here! Finish the sign-in for your user.
                pendingResultTask
                    .addOnSuccessListener(
                        OnSuccessListener<AuthResult?> {

                            val prefs = getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE).edit()
                            prefs.putString("email",it.user?.email)
                            prefs.putString("email", "TWITTER")
                            prefs.apply()
                            val userRef = FirebaseAuth.getInstance().currentUser
                                ?.let { it1 -> FirebaseFirestore.getInstance().collection("users").document(it1.uid) }


                            val user = User(
                                it.user?.email.toString(),
                                it.user?.displayName.toString(),
                                ""
                            )

                            userRef?.set(user)

                            setResult(RESULT_OK)
                            val intent = Intent(this, nav_activity::class.java)
                            startActivity(intent)
                            finish()
                        })
                    .addOnFailureListener(
                        OnFailureListener {
                            Toast.makeText(
                                applicationContext,
                                resources.getString(R.string.failLogin),
                                Toast.LENGTH_SHORT
                            ).show()
                        })
            } else {
                FirebaseAuth.getInstance()
                    .startActivityForSignInWithProvider( /* activity= */this, provider.build())
                    .addOnSuccessListener(
                        OnSuccessListener<AuthResult?> {

                                val prefs = getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE).edit()
                                prefs.putString("email", it.user?.email)
                                prefs.putString("email", "TWITTER")
                                prefs.apply()
                                val userRef = FirebaseAuth.getInstance().currentUser
                                    ?.let { it1 -> FirebaseFirestore.getInstance().collection("users").document(it1.uid) }


                                val user = User(
                                    it.user?.email.toString(),
                                    it.user?.displayName.toString(),
                                    ""
                                )

                                userRef?.set(user)

                                setResult(RESULT_OK)
                                val intent = Intent(this, nav_activity::class.java)
                                startActivity(intent)
                                finish()

                            })
                        }.addOnFailureListener(
                        OnFailureListener {
                            Toast.makeText(
                                applicationContext,
                                resources.getString(R.string.failLogin),
                                Toast.LENGTH_SHORT
                            ).show()
                        })
            }
        }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callBackManager.onActivityResult(requestCode,resultCode,data)
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == GOOGLE_SING_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
           try {
               val account = task.getResult(ApiException::class.java)
               if(account!=null){
                   val credential = GoogleAuthProvider.getCredential(account.idToken,null)

                   FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                       if(it.isSuccessful){
                           val prefs = getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE).edit()
                           prefs.putString("email",account.email)
                           prefs.putString("email", "GOOGLE")
                           prefs.apply()
                           val userRef = FirebaseAuth.getInstance().currentUser
                               ?.let { it1 -> FirebaseFirestore.getInstance().collection("users").document(it1.uid) }


                           val user = User(
                               account.email.toString(),
                               account.displayName.toString(),
                               ""
                           )

                           userRef?.set(user)

                           setResult(RESULT_OK)
                           val intent = Intent(this, nav_activity::class.java)
                           startActivity(intent)
                           finish()

                       }else{
                           Toast.makeText(
                               applicationContext,
                               resources.getString(R.string.failLogin),
                               Toast.LENGTH_SHORT
                           ).show()
                       }
                   }

               }
           }catch (e:ApiException){

           }
    }


}
}