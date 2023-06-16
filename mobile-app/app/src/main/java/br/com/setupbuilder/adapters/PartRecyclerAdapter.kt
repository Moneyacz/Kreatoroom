package br.com.setupbuilder.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.setupbuilder.R
import br.com.setupbuilder.view.ViewPartActivity


class PartRecyclerAdapter() :  RecyclerView.Adapter<PartRecyclerAdapter.ViewHolder>() {
    // Nama Komponen
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


    inner class ViewHolder(ItemView:View) : RecyclerView.ViewHolder(ItemView) {
        var title : TextView
        init{
            title = itemView.findViewById(R.id.partTitle2)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.partview2_model, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = ItemTitles[position]
        holder.title.setOnClickListener {
            val intent = Intent(it.context, ViewPartActivity::class.java)
            intent.putExtra("id", position)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return ItemTitles.size
    }

}