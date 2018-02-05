package com.spikeyduyphan.sidecryptobar

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.widget.ArrayAdapter

class CryptoArrayAdapter(context: Context?, resource: Int, objects: ArrayList<Coin>?) : ArrayAdapter<Coin>(context, resource, objects) {

    var resource : Int
    var list : ArrayList<Coin> ?
    var vi : LayoutInflater

    init {
        this.resource = resource
        this.list = objects
        this.vi = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
}