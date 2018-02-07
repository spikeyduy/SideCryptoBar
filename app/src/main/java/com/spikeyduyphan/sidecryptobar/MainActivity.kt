package com.spikeyduyphan.sidecryptobar

import android.app.Activity
import android.app.SearchManager
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.util.Log
import android.widget.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
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
    private var cryptoArray : JSONArray ?= null
    private var urlString : String ?= null
    private var actualURLString : URL ?= null

    private var searchViewList : ListView ?= null
    private var coinArrayList : ArrayList<Coin> ?= null
    private var cryptoArrayAdapter : CryptoArrayAdapter ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        urlString = getString(R.string.apiSite)
        actualURLString = URL(urlString)

        // create the array for all the coins
        val feedTask = RetrieveFeedTask()
        cryptoArray = feedTask.execute(actualURLString).get() // jsonarray that is returned by the feedTask
        // this is the working array we want to be working on? OR should we convert into arraylist then work with that?
        val finCryptoArray = cryptoArray

        // get search intent, verify, and get query
        // TODO POP UP VIEW OF LIST OF COINS ON SEARCH TYPE
//        setDefaultKeyMode(Activity.DEFAULT_KEYS_SEARCH_LOCAL)
//        val intent : Intent = getIntent()
//        if (Intent.ACTION_SEARCH.equals(intent.action)) {
//            val query = intent.getStringExtra(SearchManager.QUERY)
//            searchCoins(query)
//        }


        searchView1 = findViewById(R.id.firstSearch)
        searchView2 = findViewById(R.id.secondSearch)
        switchButton = findViewById(R.id.switchCurrency)
        editText = findViewById(R.id.edit_query)
        convertButton = findViewById(R.id.convertButton)
        finalTextView = findViewById(R.id.convertTextView)

        // this is getting and populating a listview with all the coins
        // TODO work on getting a listview of the coins then work from that array
//        searchViewList = findViewById(R.id.search1ListView) // the list that shows all the coins
//        cryptoArrayAdapter = CryptoArrayAdapter(this, android.R.layout.simple_list_item_1, coinArrayList)
//        searchViewList!!.adapter = cryptoArrayAdapter
//        convertJSONToArray(cryptoArray!!)
//        cryptoArrayAdapter!!.notifyDataSetChanged()

        // TODO convert button listener
//        val cConvertButton = convertButton
//        if (cConvertButton != null) {
//            cConvertButton.setOnClickListener{
//                // just need the code after the button is pressed
//
//            }
//        }

        

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

    fun convertJSONToArray(coinArray : JSONArray) {
        // this converts the JSONArray into a more managable arraylist
        for (i in 0..(coinArray.length())) {
            var coin : JSONObject = coinArray.getJSONObject(i) // get one coin's data
            coinArrayList!!.add(
                    Coin(coin.getString("name"),
                            coin.getString("symbol"),
                            coin.getString("price_usd")))
        }
    }

    fun searchCoins(name: String) {
        // this is used in the text boxes and should be used when typing in textview
        val feedTask = RetrieveFeedTask()
        cryptoArray = feedTask.execute(actualURLString).get() // jsonarray that is returned by the feedTask
        val finCryptoArray = cryptoArray
        if (finCryptoArray != null) {
            for (t in 0..(finCryptoArray.length() - 1)) {
                if (finCryptoArray.getJSONObject(t).getString("id") == name) {
                    testView!!.text  = finCryptoArray.getJSONObject(t).getString("id")
                    Log.i("SEARCHCOINS", "FOUND THE RIGHT COIN")
                }
            }
        }
    }
}
