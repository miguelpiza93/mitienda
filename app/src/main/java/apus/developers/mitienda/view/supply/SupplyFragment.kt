package apus.developers.mitienda.view.supply


import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apus.developers.mitienda.R
import apus.developers.mitienda.model.Product
import apus.developers.mitienda.model.Sale
import apus.developers.mitienda.view.home.HomeFragment
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_supply.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SupplyFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SupplyFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    val adapter = GroupAdapter<ViewHolder>()
    val products = HashMap<String,Product>()
    val carSale = HashMap<String,Sale>()
    var action: Int? = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        add_more_product_button.setOnClickListener { view ->
            when (action){
                1 -> {
                    Log.d(TAG, "Actualizar cantidades y registrar venta")
                    var addToCash = 0L
                    carSale.keys.forEach { codeProduct ->
                        val productRef = FirebaseDatabase.getInstance().getReference("/products/$codeProduct")
                        val product = products.get(codeProduct)
                        val sale = carSale.get(codeProduct)
                        product!!.amount = product.amount.minus(sale!!.amount)
                        addToCash += product.sale_price * sale.amount
                        productRef.setValue(product)
                            .addOnSuccessListener { item ->
                                Log.d(TAG, "Amount updated for product: ${productRef.key}")
                                sale.timestamp = System.currentTimeMillis()
                                sale.price = product.sale_price
                                val day = DateFormat.format("yyyy/MM/dd", sale.timestamp);
                                val saleRef = FirebaseDatabase.getInstance().getReference("/sales/$day/${sale.timestamp}")
                                saleRef.setValue(sale)
                                    .addOnSuccessListener {
                                        Log.d(TAG, "new sale registered: ${saleRef.key}")
                                        Snackbar.make(view, getString(R.string.new_sale_ok),
                                                Snackbar.LENGTH_LONG).show()
                                    }
                            }
                    }
                    carSale.clear()
                    addToCash += HomeFragment.CASH
                    val cashRef = FirebaseDatabase.getInstance().getReference("/cash")
                    cashRef.setValue(addToCash)
                }
                else -> {
                    val intent = Intent(context, AddProductActivity::class.java)
                    startActivity(intent)
                }
            }

        }

        recyclerview_supplies.adapter = adapter
        recyclerview_supplies.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        adapter.setOnItemClickListener { item, view ->
            Log.d(TAG, "Open product detail")
            //val intent = Intent(context, ChatLogActivity::class.java)
            //val row = item as LatestMessageRow
            //intent.putExtra(NewMessageActivity.USER_KEY, row.chatPartnerUser)
            //startActivity(intent)
        }

        var title = getString(R.string.store)

        when (action){
            1 -> {
                title = getString(R.string.new_sale)
            }
            2 -> {
                title = getString(R.string.new_supply)
            }
        }

        activity!!.title = title
        listenForProducts()
    }

    fun addToSaleCar(sale: Sale){
        if(sale.amount == 0){
            carSale.remove(sale.codeProduct)
        }
        else{
            carSale.put(sale.codeProduct, sale)
        }
    }

    private fun listenForProducts() {
        val ref = FirebaseDatabase.getInstance().getReference("/products")
        ref.addChildEventListener(object : ChildEventListener {

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val product = p0.getValue(Product::class.java) ?: return
                products[p0.key!!] = product
                refreshRecyclerView()
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val product = p0.getValue(Product::class.java) ?: return
                products[p0.key!!] = product
                refreshRecyclerView()
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }
        })
    }

    private fun refreshRecyclerView() {
        adapter.clear()
        products.values.forEach {
            adapter.add(ProductRow(it, action, this))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_supply, container, false)
    }


    companion object {

        val TAG = "SupplyFragment"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SupplyFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                SupplyFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
