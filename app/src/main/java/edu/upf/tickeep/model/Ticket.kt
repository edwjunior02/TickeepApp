package edu.upf.tickeep.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable
import java.sql.Timestamp

data class Ticket(
    var id:String ?= null,
    var id2:Long ?= null,
    var dataFi: com.google.firebase.Timestamp? = null,
    var dataIni:com.google.firebase.Timestamp? =null,
    var factura:Boolean ?= null,
    var fav:Boolean ?= null,
    var important:Boolean ?= null,
    var place:String ?= null,
    var products: List<Product> ?= null
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readParcelable(com.google.firebase.Timestamp::class.java.classLoader),
        parcel.readParcelable(com.google.firebase.Timestamp::class.java.classLoader),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        TODO("products")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeValue(id2)
        parcel.writeParcelable(dataFi, flags)
        parcel.writeParcelable(dataIni, flags)
        parcel.writeValue(factura)
        parcel.writeValue(fav)
        parcel.writeValue(important)
        parcel.writeString(place)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Ticket> {
        override fun createFromParcel(parcel: Parcel): Ticket {
            return Ticket(parcel)
        }

        override fun newArray(size: Int): Array<Ticket?> {
            return arrayOfNulls(size)
        }
    }
}
