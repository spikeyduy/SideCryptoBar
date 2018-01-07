package com.spikeyduyphan.sidecryptobar

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import com.spikeyduyphan.sidecryptobar.R.id.placeholder
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private var testTextView : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testTextView = findViewById(R.id.placeholder)

        val getAPI = RetrieveFeedTask().execute()

    }

    // REST web service call to get data from coinmarketcap API
    inner class RetrieveFeedTask : AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg p0: Void?): String? {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            try {
                // create a connection
                val siteURL  = URL("https://api.coinmarketcap.com/v1/ticker/?limit=200")
                val urlConn = siteURL.openConnection() as HttpURLConnection
                urlConn.connect()
                // if there the connection is successful
                try {
                    // reads the urlConn as a string
                    val bufferedReader = urlConn.inputStream.bufferedReader()
                    val stringBuilder = StringBuilder("")
                    // each line from the json object
                    var line : String?
                    do {
                        line = bufferedReader.readLine()
                        stringBuilder.append(line).append("\n")
                        if (line === null)
                            break
                    } while (true)
                    bufferedReader.close()
                    return stringBuilder.toString()
                } catch (e : Exception) {
                    throw e
                } finally {
                    urlConn.disconnect()
                }
            } catch (e : Exception) {
                Log.e("NETWORK","Couldn't connect to the website: " + e)
                return null
            }
        }

        override fun onPostExecute(result: String?) {
            if (result == null) {
                // should return an error message
                testTextView!!.text = "Error with website"
            } else {
                testTextView!!.text = result
            }
        }

    }

}
