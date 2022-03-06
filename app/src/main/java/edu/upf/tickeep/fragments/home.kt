package edu.upf.tickeep.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import edu.upf.tickeep.MyAdapter
import edu.upf.tickeep.R
import edu.upf.tickeep.model.Ticket


class home : Fragment() {
    private lateinit var dbref:DocumentReference
    private lateinit var ticketRecyclerView: RecyclerView
    private  lateinit var  ticketArrayList: ArrayList<Ticket>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        var alert= AlertDialog.Builder(view.context)
        alert.setMessage("After adding the ticket to favorites/important you must refresh the screen through the menu for the changes to take effect.")
            .setCancelable(false).setPositiveButton("Okey"){
                    a,b ->a.cancel()
            }
        val dialog = alert.create()
        alert.setTitle("Important")
        alert.show()
        ticketRecyclerView= view.findViewById(R.id.ticketsList)
        ticketRecyclerView.layoutManager = LinearLayoutManager(view.context)
        ticketRecyclerView.setHasFixedSize(true)
        ticketArrayList = arrayListOf<Ticket>()
        getTicketData()
        return view
    }

    private fun getTicketData() {
        val c = edu.upf.tickeep.utils.Constants()
       c.firebaseFirestore.collection("users").document(c.firebaseAuth.currentUser?.uid.toString())
            .collection("documents").get().addOnSuccessListener {
                documents->
                for (document in documents){
                    val ticket = document.toObject<Ticket>()
                    ticketArrayList.add(ticket)
                }
               ticketRecyclerView.adapter = MyAdapter(ticketArrayList)
            }
    }


}