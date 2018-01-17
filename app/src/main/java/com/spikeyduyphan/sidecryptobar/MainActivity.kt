package com.spikeyduyphan.sidecryptobar

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.support.v7.widget.SearchView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.w3c.dom.Text
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // create arrayadapter to bind to cryptolist
        val searchView1 = findViewById<SearchView>(R.id.firstSearch)
        val searchView2 = findViewById<SearchView>(R.id.secondSearch)
        val switchButton = findViewById<ImageButton>(R.id.switchCurrency)
        val editText = findViewById<EditText>(R.id.edit_query)
        val convertButton = findViewById<Button>(R.id.convertButton)
        val finalTextView = findViewById<TextView>(R.id.convertTextView)
        val testView = findViewById<TextView>(R.id.placeholder) // test  placeholder, should display the first ticker aka BTC

        convertButton.setOnClickListener{
            // just need the code after the button is pressed
            val urlString = getString(R.string.apiSite)
            val URLurlString = URL(urlString)
            val feedTask = RetrieveFeedTask()
            feedTask.execute(URLurlString)
        }

    }

    // REST web service call to get data from coinmarketcap API
    inner class RetrieveFeedTask : AsyncTask<URL, Void, JSONArray>() {
        override fun doInBackground(vararg p0: URL?): JSONArray? {
            var conn : HttpURLConnection? = null
            try {
                conn = p0[0]!!.openConnection() as HttpURLConnection
                val response = conn.responseCode
                Log.i("ASYNC","Response Code: " + response)
                // if there is a successful connection, return the json object
                if (response == HttpURLConnection.HTTP_OK) {
                    val strBuilder = StringBuilder()
                    try {
                        val buffReader = BufferedReader(InputStreamReader(conn.inputStream))
                        // line is will take line by line the returned result until there is none left
                        var line : String?
                        do {
                            line = buffReader.readLine()
                            strBuilder.append(line)
                            if (line === null) {
                                break
                            }
                        } while (true)
                    } catch (e : JSONException) {
                        Log.e("WEB","Website not returning correct result")
                    }
                    return JSONArray(strBuilder.toString())
                } else {
                    Log.e("WEB","Website connection not ok")
                }
            } catch (e : Exception) {
                Log.e("ASYNC","Different Error: " + e.toString())
            } finally {
                conn!!.disconnect()
            }
            return null
        }

         override fun onPostExecute(result: JSONArray?) {
            // need to convert the jsonObject into an array list
            if (result != null) {
//                convertJSONArrayList(result) // populate cryptoList
                // need to refresh the cryptoArrayAdapter
                Log.i("FINISHED","this is the result" + result.getJSONObject(0).toString())
                val textViewer = findViewById<TextView>(R.id.placeholder)
                for (t in 0..(result.length() - 1)) {
                    if (result.getJSONObject(t).getString("id") == "qtum") {
                        textViewer.text = result.getJSONObject(t).getString("id")
                    } else {
                        Log.i("CRYPTO","TICKER: " + result.getJSONObject(t).getString("id"))
                    }
                }
//                textViewer.text = result.optJSONObject(0).getString("id")
            }
        }
    }

    // should not need this because result is already a JSONArray
//    // convert JSONObject into an arrayList
//    // do we need this? We should just be able to return the JSONObject and take whatever info we need from that
//    private fun convertJSONArrayList(obj : JSONObject) {
//        cryptoList.clear() // make sure the list is clear and updated each time
//        try {
//            // this should try to get the object after the 'ticker'
//            // may have to change this depending on what result comes out of the API
//            var jArray : JSONArray = obj.getJSONArray("ticker")
//            // convert the gathered objects into Crypto Class
//            // this may be hard coded if we are only taking a certain number of tickers into account
//            (0..(jArray.length() - 1))
//                    .map { jArray.getJSONObject(it) }
//                    .forEach { cryptoList.add(Crypto(it.getString("id"), it.getString("symbol"), it.getString("price_usd"))) }
//        } catch (e : JSONException) {
//            Log.e("JSON","JSON ERROR")
//        }
//    }

}
