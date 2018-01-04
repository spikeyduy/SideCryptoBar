package com.spikeyduyphan.sidecryptobar

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import org.w3c.dom.Text
import java.net.URL

class MainActivity : AppCompatActivity() {

    private var testTextView : TextView? = null
    private val siteURL  = URL("https://api.coinmarketcap.com/v1/ticker/?limit=200")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testTextView = findViewById(R.id.placeholder)

    }

    class RetrieveFeedTask : AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg p0: Void?): String {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
        
    }

}
