package com.spikeyduyphan.sidecryptobar

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CryptoArrayAdapter(context: Context?, resource: Int, objects: ArrayList<Coin>?) : ArrayAdapter<Coin>(context, resource, objects) {

    var resource : Int = resource
    var list : ArrayList<Coin> ? = objects
    var vi : LayoutInflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    // class that reuses views as list items scroll off
    private class ViewHolder {
        var coinTitleView : TextView ?= null
        var coinSymbolView : TextView ?= null
        var coinPriceView : TextView ?= null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var coin : Coin = getItem(position)
        val viewHolder : ViewHolder

        if (convertView == null) {
            viewHolder = ViewHolder()

        }

        return super.getView(position, convertView, parent)
    }

}