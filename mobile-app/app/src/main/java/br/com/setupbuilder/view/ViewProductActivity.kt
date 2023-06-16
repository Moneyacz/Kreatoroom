package br.com.setupbuilder.view

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import br.com.setupbuilder.R
import br.com.setupbuilder.controller.PartController
import br.com.setupbuilder.controller.SetupController
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.set_setup_dialog.view.*
import kotlinx.android.synthetic.main.view_product_activity.*
import java.lang.ClassCastException

class ViewProductActivity: AppCompatActivity() {
    var url:String=""
    var infos:String = "Characteristic: "
    var value:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_product_activity)
        val cSetup = SetupController()
        val controller = PartController()


        controller.listPartsByAsin(intent.getStringExtra("name").toString()).addOnSuccessListener { response ->
            for (el in response){
                url = el.data.get("url").toString()
                product_price.setText("Rp. " + el.data.get("price").toString())
                id_product.setText(el.data.get("name").toString())
                val keys = el.data.keys

                for(key in keys){
                    if(key != "img" && key != "url" && key != "price" && key != "name" && key != "product") {
                        value = el.data.get(key).toString()
                        infos += key + ": " + value + "\n"
                    }
                }
                caracteristicas.text = infos
                Picasso.get()
                    .load( el.data.get("img").toString())
                    .into(product_image);
                addPart.setOnClickListener {
                    if(!intent.getStringExtra("setup").isNullOrBlank()){
                        cSetup.addPart(el.get("product").toString() , el.get("asin").toString(),el.get("price").toString().toDouble() ,intent.getStringExtra("setup").toString(), this)
                    }else{
                        val mDialogView = LayoutInflater.from(this).inflate(R.layout.set_setup_dialog, null)
                        //AlertDialogBuilder
                        val mBuilder = AlertDialog.Builder(this)
                            .setView(mDialogView)
                        val  mAlertDialog = mBuilder.show()

                        val adapter : ArrayAdapter<String> =  ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

                        mDialogView.setup_list.adapter = adapter

                        cSetup.listSetupsByTime("inc").addOnSuccessListener {
                            documents->
                                for(document in documents){
                                    adapter.add(document.data.get("name").toString())
                                }
                        }

                        mDialogView.setup_list.setOnItemClickListener { adapterView, view, i, l ->
                            mAlertDialog.dismiss()
                            cSetup.addPart(el.get("product").toString() , el.get("asin").toString(),el.get("price").toString().toDouble(), adapter.getItem(i).toString(), this)
                        }
                    }
                }
            }

        }

    }

    fun getUrlFromIntent(view: View) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}