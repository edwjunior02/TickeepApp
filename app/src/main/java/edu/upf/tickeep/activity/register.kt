package edu.upf.tickeep.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import edu.upf.tickeep.R
import edu.upf.tickeep.model.User

class register : AppCompatActivity() {
    lateinit var txtUser: EditText
    lateinit var txtEmail: EditText
    lateinit var txtPhone: EditText
    lateinit var txtPasword: EditText
    lateinit var txtRepeatedPassword: EditText
    private var userRef: DocumentReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registeraccount)
        val buttonSignIn: Button = findViewById(R.id.btn_loginReg)
        val buttonSignUp: Button = findViewById(R.id.btn_registerAccept)
         txtUser = findViewById(R.id.edit_user)
         txtEmail = findViewById(R.id.edit_email)
         txtPhone = findViewById(R.id.edit_phone)
         txtPasword = findViewById(R.id.edit_password)
         txtRepeatedPassword = findViewById(R.id.edit_repeatPassword)
        buttonSignIn.setOnClickListener {
            val intent = Intent(this, login::class.java)
            startActivity(intent)
        }
        buttonSignUp.setOnClickListener {
            if(camposLlenos() && txtPasword.text.toString().equals(txtRepeatedPassword.text.toString())){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(txtEmail.text.toString(), txtPasword.text.toString()).addOnCompleteListener {
                if(it.isSuccessful){

                    val c = edu.upf.tickeep.utils.Constants()
                    //CREACION USUARIO+SUBIR DATOS BDDD
                    userRef = c.firebaseAuth.currentUser
                        ?.let { it1 -> c.firebaseFirestore.collection("users").document(it1.uid) }


                    val user = User(
                        txtEmail.text.toString(),
                        txtUser.text.toString(),
                        txtPhone.text.toString(),
                    )

                    userRef?.set(user)

                    setResult(RESULT_OK)
                    finish()
                }else{
                   showAlert()
                }
            }


            } else if (!txtPasword.text.toString().equals(txtRepeatedPassword.text.toString())) {
                Toast.makeText(this, R.string.notEqualPassReg, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, R.string.emptyStringsError, Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun camposLlenos(): Boolean {
        return !(isEmpty(txtUser) || isEmpty(txtEmail) || isEmpty(txtPhone) || isEmpty(txtPasword) ||
                isEmpty(txtRepeatedPassword))
    }

    private fun isEmpty(editText: EditText): Boolean {
        return editText.text.toString().trim { it <= ' ' }.isEmpty()
    }
    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("There was an error creating the user")
        builder.setPositiveButton("Accept",null)
        val dialog:AlertDialog=builder.create()
        dialog.show()
    }
}