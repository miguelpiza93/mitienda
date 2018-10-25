package apus.developers.mitienda.view.sale

import android.text.format.DateFormat
import apus.developers.mitienda.R
import apus.developers.mitienda.model.Sale
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.sale_row.view.*

class SaleRow(private val sale: Sale): Item<ViewHolder>() {

    override fun getLayout(): Int {
        return R.layout.sale_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val date = DateFormat.format("dd/MM/yyyy hh:mm AA", sale.timestamp)
        viewHolder.itemView.detail_textview_sale.text = date
        viewHolder.itemView.name_textview_sale.text = String.format("%s x %s", sale.nameProduct, sale.amount.toString())
    }
}