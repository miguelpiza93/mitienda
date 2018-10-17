package apus.developers.mitienda.view.supply

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import apus.developers.mitienda.R
import apus.developers.mitienda.model.Product
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_add_product.*

class AddProductActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        val TAG = "AddProductActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        add_product_add_button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val product = Product()
        product.code = code_product_input.text.toString()
        product.name = name_product_input.text.toString()
        product.amount = amount_product_input.text.toString().toInt()
        val total = cost_product_input.text.toString().toFloat()
        product.cost = total / product.amount.toFloat()
        product.sale_price = sale_price_input.text.toString().toInt()
        addProduct(product)
    }

    fun addProduct(product: Product){
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this@AddProductActivity) { instanceIdResult ->
            val productsRef = FirebaseDatabase.getInstance().getReference("/products/${product.code}")
            productsRef.setValue(product).addOnSuccessListener {
                Log.d(TAG, "Saved out product")
                code_product_input.text.clear()
                name_product_input.text.clear()
                amount_product_input.text.clear()
                cost_product_input.text.clear()
            }
        }
    }
}
