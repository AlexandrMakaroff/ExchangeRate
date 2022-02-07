package com.example.valute

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HelperAdapter(val name: ArrayList<String>, val value: ArrayList<String>, val context: Context) :
    RecyclerView.Adapter<HelperAdapter.MyViewClass>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HelperAdapter.MyViewClass {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return MyViewClass(view)
    }

    override fun getItemCount(): Int {
        return name.size
    }

    override fun onBindViewHolder(holder: MyViewClass, position: Int) {
        holder.name.text = name[position]
        holder.value.text = value[position]
    }

    class MyViewClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.name)
        var value: TextView = itemView.findViewById(R.id.value)

    }
}