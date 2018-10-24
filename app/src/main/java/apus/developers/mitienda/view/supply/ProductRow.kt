package apus.developers.mitienda.view.supply

import android.view.View
import apus.developers.mitienda.R
import apus.developers.mitienda.model.Product
import apus.developers.mitienda.model.Sale
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.product_row.view.*

class ProductRow(val product: Product, val parent: SupplyFragment, val fragment: SupplyFragment): Item<ViewHolder>() {
    val sale = Sale();
    override fun getLayout(): Int {
        return R.layout.product_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        sale.codeProduct = product.code
        sale.nameProduct = product.name
        val saleCount = parent.carSale.get(product.code)?.amount ?: 0
        viewHolder.itemView.amount_sale_text.setText(saleCount.toString())
        viewHolder.itemView.name_textview_product.text = String.format("%s x %s", product.name, product.amount.toString())
        var price = ""
        when (parent.action){
            1 -> {
                viewHolder.itemView.actions_layout.visibility = View.VISIBLE
                price = product.sale_price.toString()
            }
            2 -> {
                viewHolder.itemView.actions_layout.visibility = View.GONE
                price = product.cost.toString()
            }
        }

        viewHolder.itemView.detail_textview_latest_message.text = String.format("Cod: %s - $ %s", product.code, price)

        viewHolder.itemView.minus_button.setOnClickListener {
            if(sale.amount > 0){
                sale.amount--;
                viewHolder.itemView.amount_sale_text.setText(sale.amount.toString())
                fragment.addToSaleCar(sale)
            }
        }
        viewHolder.itemView.plus_button.setOnClickListener {
            if(sale.amount < product.amount){
                sale.amount++;
                viewHolder.itemView.amount_sale_text.setText(sale.amount.toString())
                fragment.addToSaleCar(sale)
            }
        }
    }
}