package com.spikeyduyphan.sidecryptobar

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CryptoArrayAdapter(context: Context?, objects: ArrayList<Coin>?) : ArrayAdapter<Coin>(context, -1, objects) {

    var list : ArrayList<Coin> ? = objects

    // class that reuses views as list items scroll off
    private class ViewHolder {
        var coinTitleView : TextView ?= null
        var coinSymbolView : TextView ?= null
        var coinPriceView : TextView ?= null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // get coin object from list
        var coin : Coin = getItem(position)

        var viewHold : ViewHolder ?= null // references the list item's views

        // check for usable viewholder that has been recycled, else make a new viewholder
        if (convertView == null) { // no reusable viewholder, so create a new one
            viewHold = ViewHolder()
            var inflater : LayoutInflater = LayoutInflater.from(context)
            var newConvertView = inflater.inflate(R.layout.list_coin, parent, false)
            viewHold.coinTitleView = newConvertView.findViewById(R.id.coinTitleView)
            viewHold.coinSymbolView = newConvertView.findViewById(R.id.coinSymbolView)
            viewHold.coinPriceView = newConvertView.findViewById(R.id.coinPriceView)
            newConvertView.tag = viewHold
        } else { // reuse existing viewholder
            viewHold = convertView.tag as ViewHolder?
        }

        // set the coin data onto the list
        viewHold!!.coinTitleView!!.text = coin.name
        viewHold.coinSymbolView!!.text = coin.ticker
        viewHold.coinPriceView!!.text = coin.priceUSD

//        return super.getView(position, convertView, parent)
        return convertView!!
    }

}