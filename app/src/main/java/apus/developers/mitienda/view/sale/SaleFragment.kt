package apus.developers.mitienda.view.sale


import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.*
import apus.developers.mitienda.R
import android.support.v7.widget.DividerItemDecoration
import android.text.format.DateFormat
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import apus.developers.mitienda.model.Product
import apus.developers.mitienda.model.Sale
import apus.developers.mitienda.view.MainActivity
import apus.developers.mitienda.view.supply.ProductRow
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_sale.*
import kotlinx.android.synthetic.main.fragment_supply.*
import kotlinx.android.synthetic.main.notification_template_lines_media.view.*
import java.nio.file.Files.size




// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SaleFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SaleFragment : Fragment(), AdapterView.OnItemSelectedListener{

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    val adapter = GroupAdapter<ViewHolder>()
    val sales = ArrayList<Sale>()
    var filter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sale, container, false)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var format = ""
        when(position){
            0 -> {
                format = "yyyy/MM/dd"
                filter = 2
            }
            1 -> {
                format = "yyyy/MM"
                filter = 1
            }
            2 -> {
                format = "yyyy"
                filter = 0
            }
        }
        val query = DateFormat.format(format, System.currentTimeMillis())
        listenForSales(query.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)

        val item = menu.findItem(R.id.spinner)
        val spinner = item.getActionView() as Spinner

        spinner.onItemSelectedListener = this
        val adapter = ArrayAdapter.createFromResource(context,
                R.array.spinner_list_item_array, R.layout.spinner_layout)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerview_sales.adapter = adapter
        recyclerview_sales.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    private fun listenForSales(query: String) {
        val ref = FirebaseDatabase.getInstance().getReference("${MainActivity.environment}/sales/$query")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                read(p0)
            }
        })
    }

    private fun read(p0: DataSnapshot){
        sales.clear()
        when(filter){
            0 -> {
                p0.children.forEach { month ->
                    month.children.forEach { day ->
                        day.children.forEach { sal ->
                            val sale = sal.getValue(Sale::class.java)!!
                            sales.add(0, sale)
                        }
                    }
                }
            }
            1 -> {
                p0.children.forEach { month ->
                    month.children.forEach { day ->
                        val sale = day.getValue(Sale::class.java)!!
                        sales.add(0, sale)
                    }
                }
            }
            2 -> {
                p0.children.forEach { day ->
                    val sale = day.getValue(Sale::class.java)!!
                    sales.add(0, sale)
                }
            }
            3 -> {
                val sale = p0.getValue(Sale::class.java)!!
                sales.add(0, sale)
            }
        }
        refreshRecyclerView()
    }

    private fun refreshRecyclerView() {
        adapter.clear()
        sales.forEach {
            adapter.add(SaleRow(it))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.spinner -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SaleFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                SaleFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
