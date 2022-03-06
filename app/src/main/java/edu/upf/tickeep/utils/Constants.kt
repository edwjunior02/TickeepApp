package edu.upf.tickeep.utils

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.upf.tickeep.model.User


class Constants {

    @SuppressLint("StaticFieldLeak")
    val firebaseFirestore:FirebaseFirestore = FirebaseFirestore.getInstance()
    val firebaseAuth:FirebaseAuth = FirebaseAuth.getInstance()




}
