package edu.upf.tickeep.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.Timestamp
import edu.upf.tickeep.R
import edu.upf.tickeep.model.Ticket


class ticketFragment : Fragment() {

    private lateinit var  myDialog: Dialog
    private lateinit var model: Ticket
    private lateinit var txtStatus: TextView
    private lateinit var txtTitle: TextView
    private lateinit var txtId: TextView
    private lateinit var txtProducts: TextView
    private lateinit var txtTotal: TextView
    private lateinit var txtTotalPaid: TextView
    private lateinit var txtTotalChange: TextView
    private lateinit var txtProdPrice: TextView
    private lateinit var imgStatus: ImageView
    private lateinit var btnQR: Button



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflatedView =inflater.inflate(R.layout.fragment_ticket, container, false)
        txtStatus = inflatedView.findViewById(R.id.txt_statusFragTic)
        txtTitle = inflatedView.findViewById(R.id.txt_titleFragTic)
        txtId = inflatedView.findViewById(R.id.txt_idFragTic)
        txtProducts = inflatedView.findViewById(R.id.txt_prodFragTic)
        txtTotal = inflatedView.findViewById(R.id.txt_totalPriceFragTic)
        txtTotalPaid = inflatedView.findViewById(R.id.txt_creditCardPrice)
        txtTotalChange = inflatedView.findViewById(R.id.txt_return)
        txtProdPrice = inflatedView.findViewById(R.id.txt_prodPriceFragTic)
        imgStatus = inflatedView.findViewById(R.id.img_statusFragTic)
        btnQR = inflatedView.findViewById(R.id.btn_qrFragTic)

        myDialog = Dialog(inflatedView.context)

        btnQR.setOnClickListener {
            showPopUp_img()
        }


        val bundle = this.arguments
        if (bundle != null) {
            model = bundle.getParcelable<Parcelable>("item") as Ticket
        }
        if(model.dataFi!! > Timestamp.now()){
            txtStatus.text = "Avaiable"
            imgStatus.setImageResource(R.drawable.status_green)
        }else{
            txtStatus.text = "Not avaiable"
            imgStatus.setImageResource(R.drawable.status_red)
        }
        txtTitle.text = model.place.toString()
        txtId.text = model.id2.toString()
        var total: Float? = 0.0.toFloat()
        for (i in model.products!!) {
            txtProdPrice.append( String.format("%.2f",(i.price!!.toFloat() *  i.quantity!!.toFloat()) )+"€")
            txtProdPrice.append("\n")
            txtProducts.append(i.product)
            txtProducts.append("\n")
            total = (total!! +(i.price!! *  i.quantity!!)).toFloat()
        }
        val totalText=String.format("%.2f",total )+"€"
        txtTotal.text = totalText
        val totalPaid = total!! *1.3
        val totalPaidText= String.format("%.2f",totalPaid)+"€"
        txtTotalPaid.text  = totalPaidText
        val totalReturn = totalPaid-total
        val totalReturnText = String.format("%.2f",totalReturn )+"€"
        txtTotalChange.text = totalReturnText

        // Inflate the layout for this fragment
        return inflatedView
    }
    fun showPopUp_img() {
        val txtClose: TextView

        myDialog?.setContentView(R.layout.popup_qr)
        txtClose = myDialog!!.findViewById(R.id.txtClose_img)


        txtClose.setOnClickListener { myDialog.dismiss() }
        myDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog.show()
    }


}