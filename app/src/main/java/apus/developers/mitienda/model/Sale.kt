package apus.developers.mitienda.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Sale(var timestamp: Long, var price: Int, var codeProduct: String, var nameProduct: String, var amount: Int) : Parcelable {
    constructor() : this(0L, 0, "", "", 0)
}