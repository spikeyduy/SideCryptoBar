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

    private var testView : TextView ?= null
    private var searchView1 : SearchView ?= null
    private var searchView2 : SearchView ?= null
    private var switchButton : ImageButton ?= null
    private var editText : EditText ?= null
    private var convertButton : Button ?= null
    private var finalTextView : TextView ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val urlString = getString(R.string.apiSite)
        val URLurlString = URL(urlString)
//        val feedTask = RetrieveFeedTask()
//        val cryptoArray : JSONArray = feedTask.execute(URLurlString).get() // jsonarray that is returned by the feedTask

        searchView1 = findViewById(R.id.firstSearch)
        searchView2 = findViewById(R.id.secondSearch)
        switchButton = findViewById(R.id.switchCurrency)
        editText = findViewById(R.id.edit_query)
        convertButton = findViewById(R.id.convertButton)
        finalTextView = findViewById(R.id.convertTextView)
        testView = findViewById(R.id.placeholder) // test  placeholder, should display the first ticker aka BTC

        val cConvertButton = convertButton
        if (cConvertButton != null) {
            cConvertButton.setOnClickListener{
                // just need the code after the button is pressed
                val feedTask = RetrieveFeedTask()
                val cryptoArray : JSONArray = feedTask.execute(URLurlString).get() // jsonarray that is returned by the feedTask
            }
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
                // EX this is how to iterate through the jsonarray when searching for ticker
//                for (t in 0..(result.length() - 1)) {
//                    if (result.getJSONObject(t).getString("id") == "qtum") {
//                        textViewer.text = result.getJSONObject(t).getString("id")
////                    } else {
////                        Log.i("CRYPTO","TICKER: " + result.getJSONObject(t).getString("id"))
//                    }
//                }
//                textViewer.text = result.optJSONObject(0).getString("id")
            }
        }
    }

    fun searchCoins(name: String, coinArr: JSONArray) {
        // this is used in the text boxes and should be used when typing in textview
        for (t in 0..(coinArr.length() - 1)) {
            if (coinArr.getJSONObject(t).getString("id") == name) {
                testView!!.text  = coinArr.getJSONObject(t).getString("id")
            }
        }
    }
}
