package com.github.krxwl.codecat.customspinner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class CustomAdapter(context: Context, customItemList: ArrayList<CustomItem>) : ArrayAdapter<CustomItem>(context, 0, customItemList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (convertView == null) {
            val newConvertView = LayoutInflater.from(context).inflate(com.github.krxwl.codecat.R.layout.custom_spinner_layout, parent, false)
            val item = getItem(position)
            val nameTextView: TextView = newConvertView.findViewById(com.github.krxwl.codecat.R.id.language_name_spinner) as TextView
            val imageView: ImageView? = newConvertView.findViewById(com.github.krxwl.codecat.R.id.language_picture)

            if (item != null) {
                imageView?.setImageBitmap(item.getCustomSpinnerItemImage())
                nameTextView.text = item.getCustomSpinnerItemName()
            }
            return newConvertView
        }
        val item = getItem(position)
        val nameTextView: TextView = convertView.findViewById(com.github.krxwl.codecat.R.id.language_name_spinner) as TextView
        val imageView: ImageView? = convertView.findViewById(com.github.krxwl.codecat.R.id.language_picture)

        if (item != null) {
            imageView?.setImageBitmap(item.getCustomSpinnerItemImage())
            nameTextView.text = item.getCustomSpinnerItemName()
        }
        return convertView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (convertView == null) {
            val newConvertView = LayoutInflater.from(context).inflate(com.github.krxwl.codecat.R.layout.custom_dropdown_layout, parent, false)
            val item = getItem(position)
            val nameTextView: TextView = newConvertView.findViewById(com.github.krxwl.codecat.R.id.language_name_spinner) as TextView
            val imageView: ImageView? = newConvertView.findViewById(com.github.krxwl.codecat.R.id.language_picture)

            if (item != null) {
                imageView?.setImageBitmap(item.getCustomSpinnerItemImage())
                nameTextView.text = item.getCustomSpinnerItemName()
            }
            return newConvertView
        }
        val item = getItem(position)
        val nameTextView: TextView = convertView.findViewById(com.github.krxwl.codecat.R.id.language_name_spinner) as TextView
        val imageView: ImageView? = convertView.findViewById(com.github.krxwl.codecat.R.id.language_picture)

        if (item != null) {
            imageView?.setImageBitmap(item.getCustomSpinnerItemImage())
            nameTextView.text = item.getCustomSpinnerItemName()
        }
        return convertView
    }
}