package br.com.setupbuilder.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import br.com.setupbuilder.R
import br.com.setupbuilder.controller.PartController
import br.com.setupbuilder.controller.SetupController
import br.com.setupbuilder.view.ListProductActivity
import br.com.setupbuilder.view.ViewProductActivity
import br.com.setupbuilder.view.ViewSetupActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_exclusion.view.*


class ComponentRecyclerAdapter(val setupName: String) :  RecyclerView.Adapter<ComponentRecyclerAdapter.ViewHolder>() {
    private val ItemTitles = arrayOf(
        "CPU",
        "Motherboard",
        "RAM",
        "GPU",
        "Storage (HDD/SSD)",
        "Monitor",
        "Keyboard",
        "Mouse",
        "Headset",
        "PSU",
        "Case"
    )
    private val keys = arrayOf(
        "CPU",
        "Motherboard",
        "RAM",
        "GPU",
        "Storage (HDD/SSD)",
        "Monitor",
        "Keyboard",
        "Mouse",
        "Headset",
        "PSU",
        "Case"
    )

    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var title : TextView
        var name : TextView
        var add : TextView
        var card : ConstraintLayout
        var photo : ImageView
        var price : TextView
        var delete : View
        init{
            card = itemView.findViewById(R.id.compCard)
            title = itemView.findViewById(R.id.partTitle)
            photo = itemView.findViewById(R.id.compImage)
            add = itemView.findViewById(R.id.partName)
            name = itemView.findViewById(R.id.compTitle)
            price = itemView.findViewById(R.id.compPrice)
            delete = itemView.findViewById(R.id.compDelete)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.partview1_model, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var add = "Tambahkan "
        holder.title.text = add + ItemTitles[position]
        val cSetup = SetupController()
        val cPart = PartController()



        cSetup.listSetupsByTime("inc").addOnSuccessListener { documents->
                for(document in documents){

                    if(document.data.get(keys[position])!== null && setupName.equals(
                            document.data.get(
                                "name"
                            ).toString()
                        )){
                        cPart.listPartsByAsin(document.data.get(keys[position]).toString()).addOnSuccessListener { documents->
                                for(document in documents){
                                    holder.add.visibility = View.GONE
                                    holder.photo.visibility = View.VISIBLE
                                    holder.name.visibility = View.VISIBLE
                                    holder.price.visibility = View.VISIBLE
                                    holder.delete.visibility=View.VISIBLE
                                    holder.title.text = ItemTitles[position]
                                    holder.name.text =  document.data.get("name").toString()
                                    holder.price.text = "Rp. " + document.data.get("price").toString()
                                    Picasso.get()
                                        .load(document.data.get("img").toString())
                                        .placeholder(R.drawable.round_square)
                                        .into(holder.photo);

                                    holder.delete.setOnClickListener {
                                        val mDialogView = LayoutInflater.from(it.context)
                                            .inflate(R.layout.dialog_exclusion, null)

                                        val mBuilder = AlertDialog.Builder(it.context)
                                            .setView(mDialogView)

                                        val mAlertDialog = mBuilder.show()

                                        mDialogView.exclusion_title.setText("Hapus " + ItemTitles[position])
                                        mDialogView.exclusion_text.setText("Apakah Anda yakin ingin menghapus komponen ini? Anda dapat menambahkannya lagi dengan menambahkannya ulang dari daftar produk.")
                                        mDialogView.confirm_dialog.setOnClickListener {
                                            cSetup.deletePart(
                                                document.data.get("product").toString(), document.data.get("price").toString().toDouble() ,setupName
                                            )
                                            holder.title.text = add + ItemTitles[position]
                                            holder.add.visibility = View.VISIBLE
                                            holder.photo.visibility = View.GONE
                                            holder.name.visibility = View.GONE
                                            holder.price.visibility = View.GONE
                                            holder.delete.visibility=View.GONE
                                            val refresh =
                                                Intent(it.context, ViewSetupActivity::class.java)
                                            refresh.putExtra("name", setupName)
                                            refresh.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                            it.context.startActivity(refresh)

                                            mAlertDialog.dismiss()
                                        }
                                        mDialogView.cancel_dialog_exclusion.setOnClickListener {
                                            mAlertDialog.dismiss()
                                        }

                                    }
                                    holder.card.setOnClickListener {

                                            val intent = Intent(it.context, ViewProductActivity::class.java)
                                            intent.putExtra("name",  document.data.get("asin").toString())
                                            it.context.startActivity(intent)


                                    }
                                }


                        }

                    }else{
                        holder.add.visibility = View.VISIBLE
                        holder.photo.visibility = View.GONE
                        holder.name.visibility = View.GONE
                        holder.price.visibility = View.GONE
                        holder.delete.visibility=View.GONE
                        holder.card.setOnClickListener {

                            val intent = Intent(it.context, ListProductActivity::class.java)
                            intent.putExtra("part", keys[position])
                            intent.putExtra("setup", setupName)
                            it.context.startActivity(intent)

                        }
                    }
                }
        }



    }

    override fun getItemCount(): Int {
        return ItemTitles.size
    }

}