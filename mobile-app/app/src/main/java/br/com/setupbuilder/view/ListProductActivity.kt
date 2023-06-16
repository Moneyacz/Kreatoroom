package br.com.setupbuilder.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.setupbuilder.R
import br.com.setupbuilder.adapters.SetupRecyclerAdapter
import br.com.setupbuilder.controller.PartController
import br.com.setupbuilder.controller.SetupController
import kotlinx.android.synthetic.main.list_product_activity.*
import kotlinx.android.synthetic.main.part_filter_dialog.view.*

class ListProductActivity: AppCompatActivity(){
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<SetupRecyclerAdapter.ViewHolder>? = null
    val controller = PartController()
    val setupController = SetupController()
    val names = ArrayList<String>()
    val prices = ArrayList<String>()
    val imgs = ArrayList<String>()
    val id = ArrayList<String>()
    var setupName:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_product_activity)

        if(intent.getStringExtra("setup")!==null){
            setupName = intent.getStringExtra("setup").toString()
        }
        layoutManager = LinearLayoutManager(this)
        list_product.layoutManager = layoutManager
        adapter = SetupRecyclerAdapter(arrayListOf(), arrayListOf(), arrayListOf(), arrayListOf(), setupName)

        list_product.adapter = adapter

        var setup:String? = null
        if(intent.getStringExtra("setup")!= null) {
            setup = intent.getStringExtra("setup")
        }

        val part = intent.getStringExtra("part").toString()
        search_part.addTextChangedListener{
            search()
        }


        controller.listPartsByTerm(part, intent.getStringExtra("order").toString()).addOnSuccessListener { response ->
            for (element in response) {

                names.add(element.data.get("name").toString())
                prices.add(element.data.get("price").toString())
                imgs.add(element.data.get("img").toString())
                id.add(element.data.get("asin").toString())
            }

            adapter = SetupRecyclerAdapter(names, prices, imgs, id, setupName)
            list_product.adapter = adapter

            if(intent.getStringExtra("search") != null && intent.getStringExtra("search") != "") {
                search_part.setText(intent.getStringExtra("search"))
                search()
            }
            else
                search_part.setText("")



        }
        part_filter.setOnClickListener{
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.part_filter_dialog, null)
            val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
            val mAlertDialog = mBuilder.show()


            mDialogView.price_desc_part.setOnClickListener {
                mAlertDialog.dismiss()
                val intent = Intent(this, ListProductActivity::class.java)
                intent.putExtra("order", "desc")
                intent.putExtra("part", part)
                intent.putExtra("search", search_part.text.toString())
                startActivity(intent);
            }
            mDialogView.price_inc_part.setOnClickListener {
                mAlertDialog.dismiss()
                val intent = Intent(this, ListProductActivity::class.java)
                intent.putExtra("order", "asc")
                intent.putExtra("part", part)
                intent.putExtra("search", search_part.text.toString())
                startActivity(intent);
            }
        }

    }

    public fun search(){
        if(intent.getStringExtra("setup")!==null){
            setupName = intent.getStringExtra("setup").toString()
        }
        val namesS = ArrayList<String>()
        val pricesS = ArrayList<String>()
        val imgsS = ArrayList<String>()
        val idS = ArrayList<String>()
        val text = search_part.text.toString()
        if(text == "")
            adapter = SetupRecyclerAdapter(names, prices, imgs, id, setupName)
        else{
            for (i in 0..names.size-1){
                if (names.get(i).toUpperCase().contains(text.toUpperCase())){
                    namesS.add(names.get(i))
                    pricesS.add(prices.get(i))
                    imgsS.add(imgs.get(i))
                    idS.add(id.get(i))
                }
            }
            adapter = SetupRecyclerAdapter(namesS, pricesS, imgsS, idS, intent.getStringExtra("setup").toString())

        }
        list_product.adapter = adapter

    }

}