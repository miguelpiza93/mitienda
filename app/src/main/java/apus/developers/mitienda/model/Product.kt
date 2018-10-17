package apus.developers.mitienda.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Product(var code: String, var name: String, var cost: Float, var amount: Int, var sale_price: Int): Parcelable{
    constructor(): this("", "", 0f, 0, 0)
}