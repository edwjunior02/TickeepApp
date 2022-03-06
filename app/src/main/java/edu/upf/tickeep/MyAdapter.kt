package edu.upf.tickeep



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import edu.upf.tickeep.fragments.ticketFragment
import edu.upf.tickeep.model.Ticket
import edu.upf.tickeep.utils.Constants
import java.text.SimpleDateFormat
import java.util.*


class MyAdapter(private val ticketsList:ArrayList<Ticket>):RecyclerView.Adapter<MyAdapter.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.ticketsview, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val c = edu.upf.tickeep.utils.Constants()

        val currentItem = ticketsList[position]
        holder.btnImportant.setOnClickListener {
            if(currentItem.important == true){
                holder.btnImportant.setImageResource(R.drawable.ic_baseline_notification_important_24)
                currentItem.important = false
                var id = currentItem.id.toString()
                c.firebaseFirestore.collection("users").document(c.firebaseAuth.currentUser?.uid.toString())
                    .collection("documents").document(currentItem.id.toString()).update("important",false)
            }else{
                holder.btnImportant.setImageResource(R.drawable.ic_baseline_notification_important_24_yellow)
                currentItem.important = true
                var id = currentItem.id.toString()
                c.firebaseFirestore.collection("users").document(c.firebaseAuth.currentUser?.uid.toString())
                    .collection("documents").document(currentItem.id.toString()).update("important",true)
            }

        }
        holder.btnFavourite.setOnClickListener {
            if(currentItem.fav == true){
                holder.btnFavourite.setImageResource(R.drawable.ic_favorite)
                currentItem.fav = false
                var id = currentItem.id.toString()
                c.firebaseFirestore.collection("users").document(c.firebaseAuth.currentUser?.uid.toString())
                    .collection("documents").document(currentItem.id.toString()).update("fav",false)
            }else{
                holder.btnFavourite.setImageResource(R.drawable.ic_favorite_red)
                currentItem.fav = true
                var id = currentItem.id.toString()
                c.firebaseFirestore.collection("users").document(c.firebaseAuth.currentUser?.uid.toString())
                    .collection("documents").document(currentItem.id.toString()).update("fav",true)
            }

        }


        val milliseconds = currentItem.dataFi!!.seconds * 1000 + currentItem.dataFi!!.nanoseconds / 1000000
        val sdf = SimpleDateFormat("MM/dd/yyyy")
        val netDate = Date(milliseconds)
        val date = sdf.format(netDate).toString()

        holder.txtName.text = currentItem.place
        holder.txtDate.text = date

        if(currentItem.fav == true){

            holder.btnFavourite.setImageResource(R.drawable.ic_favorite_red)
        }else{
            holder.btnFavourite.setImageResource(R.drawable.ic_favorite)
        }
        if(currentItem.important == true){
            holder.btnImportant.setImageResource(R.drawable.ic_baseline_notification_important_24_yellow)

        }else{
            holder.btnImportant.setImageResource(R.drawable.ic_baseline_notification_important_24)
        }
        if(currentItem.factura == true){
            holder.txtDesc.text = "Bill"
        }else{
            holder.txtDesc.text = "Ticket"
        }

        if(currentItem.dataFi!! > Timestamp.now()){
            holder.txtStatus.text = "Available"
            holder.imgStatus.setImageResource(R.drawable.status_green)
        }else{
            holder.txtStatus.text = "Not available"
            holder.imgStatus.setImageResource(R.drawable.status_red)
        }
        holder.itemView.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {

                val activity = v!!.context as AppCompatActivity

                val fragmentWithObject = ticketFragment().apply {
                    arguments = Bundle().apply { putParcelable("item", currentItem) }
                }
                val fragment = activity.supportFragmentManager.beginTransaction().add(fragmentWithObject, "detail") // Add this transaction to the back stack (name is an optional name for this back stack state, or null).
                    .addToBackStack(null)
                fragment.replace(R.id.fragment_container,fragmentWithObject).commit()
            }
        })


    }

    override fun getItemCount(): Int {
        return ticketsList.size
    }
    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val txtName:TextView = itemView.findViewById(R.id.txt_ticketname)
        val txtDate:TextView = itemView.findViewById(R.id.txtEdit_ticketdate)
        val txtDesc:TextView = itemView.findViewById(R.id.txtEdit_ticketdescription)
        val txtStatus:TextView = itemView.findViewById(R.id.txtEdit_ticketstatus)
        val imgStatus:ImageView = itemView.findViewById(R.id.img_status)
        val btnFavourite:ImageButton = itemView.findViewById(R.id.imgBtn_favourite)
        val btnImportant:ImageButton = itemView.findViewById(R.id.imgBtn_important)





    }
}