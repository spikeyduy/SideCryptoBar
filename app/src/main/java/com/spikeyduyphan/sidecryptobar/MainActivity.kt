package com.spikeyduyphan.sidecryptobar

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.View
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
    private var editText : EditText ?= null // amount of coin1 by user input
    private var convertButton : Button ?= null
    private var finalTextView : TextView ?= null // answer to conversion
    private var cryptoArray : JSONArray ?= null // the array received when query from coinmarketcap
    private var urlString : String ?= null // the string form of url
    private var actualURLString : URL ?= null // url format of url

    private var searchViewList : ListView ?= null
    private var coinArrayList : ArrayList<Coin> ?= null
    private var cryptoArrayAdapter : ArrayAdapter<Coin> ?= null

    private var firstcoin : Coin ?= null
    private var secondCoin : Coin ?= null
    private var coinAmount : Int = 0

    private var counterCoin : String = "First"


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


        searchView1 = findViewById(R.id.firstSearch)
        searchView2 = findViewById(R.id.secondSearch)
        switchButton = findViewById(R.id.switchCurrency)
        editText = findViewById(R.id.edit_query)
        convertButton = findViewById(R.id.convertButton)
        finalTextView = findViewById(R.id.convertTextView)

        // this is getting and populating a listview with all the coins
        searchViewList = findViewById(R.id.coinListView) // the list that shows all the coins
        coinArrayList = convertJSONToArray(finCryptoArray!!)
//        cryptoArrayAdapter = CryptoArrayAdapter(this, coinArrayList) // coinArrayList must not be empty
        cryptoArrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, coinArrayList)
        searchViewList!!.adapter = cryptoArrayAdapter

//        searchView1!!.setOnQueryTextListener(SearchView.OnQueryTextListener)

        // set up the searchviews so they send the data through intents
        val searchManager : SearchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView1!!.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView1!!.setIconifiedByDefault(false)
        searchView2!!.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView2!!.setIconifiedByDefault(false)

        // these two listeners let app know which is the current searchView 
        searchView1!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText : String) : Boolean {
                Log.i("COUNTERcount","onTextChangeFIRST")
                counterCoin = "First"
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

        })

        searchView2!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText : String) : Boolean {
                Log.i("COUNTERcount","onTextChangeSECOND")
                counterCoin = "Second"
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

        })
        
//        editText!!.setOnClickListener(object : View.OnClickListener {
//
//        })

        // get search intent, verify, and get query
        // TODO POP UP VIEW OF LIST OF COINS ON SEARCH TYPE
//        setDefaultKeyMode(Activity.DEFAULT_KEYS_SEARCH_LOCAL)
//        val intent : Intent = intent
//        if (Intent.ACTION_SEARCH == intent!!.action) {
//            val query = intent.getStringExtra(SearchManager.QUERY)
//            if ((counter % 2) != 0) {
//                secondCoin = searchCoins(query, coinArrayList!!)
//                Toast.makeText(this, "Setting CoinS: " + secondCoin!!.name, Toast.LENGTH_SHORT).show()
//                Log.i("COUNTER","Counter: " + counter)
//                counter += 1
//            } else if((counter % 2) == 0) {
//                firstcoin = searchCoins(query,coinArrayList!!)
//                Toast.makeText(this, "Setting Coin: " + firstcoin!!.name, Toast.LENGTH_SHORT).show()
//                counter += 1
//                Log.i("COUNTERcount","Counter: " + counter)
//            }
//        }
//        if (onSearchRequested()) {
////            Toast.makeText(this, "SEARCH BEGINNING",Toast.LENGTH_LONG).show()
//        }

        // TODO convert button listener
//        val cConvertButton = convertButton
//        cConvertButton!!.setOnClickListener{
//            // get all the inputs
//
//            finalTextView!!.text  = coinConverter(firstcoin!!.priceUSD.toInt(), secondCoin!!.priceUSD.toInt(), coinAmount ).toString()
//        }

    }

    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//        setIntent(intent)
        if (Intent.ACTION_SEARCH == intent!!.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
//            Log.i("COUNTERcount", "This is the count before:  " + counter)
//            Log.i("COUNTERcount", "Trying to find tag: " + intent.)
            if (counterCoin == "First") {
                firstcoin = searchCoins(query, coinArrayList!!)
                Toast.makeText(this, "Setting CoinS: " + firstcoin!!.name, Toast.LENGTH_SHORT).show()
//                Log.i("COUNTERcount","Counter: " + counterCoin)
            }
            if (counterCoin == "Second") {
                secondCoin = searchCoins(query,coinArrayList!!)
                Toast.makeText(this, "Setting Coin: " + secondCoin!!.name, Toast.LENGTH_SHORT).show()
//                Log.i("COUNTERcount","CounterELSE: " + counter)
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
//            if (result != null) {
////                convertJSONArrayList(result) // populate cryptoList
//                // need to refresh the cryptoArrayAdapter
////                Log.i("FINISHED","this is the result" + result.getJSONObject(0).toString())
//            }
        }
    }

    private fun convertJSONToArray(coinArray : JSONArray) : ArrayList<Coin> {
        // this converts the JSONArray into a more managable arraylist
        val coinArrayList : ArrayList<Coin>  = ArrayList()
        for (i in 0..(coinArray.length() - 1)) {
            val coin : JSONObject = coinArray.getJSONObject(i) // get one coin's data
//            Log.i("CONVERTER","this is the current coin: " + coin)
            coinArrayList.add(
                    Coin(coin.getString("name"),
                            coin.getString("symbol"),
                            coin.getString("price_usd")))
//            Log.i("CONVERTER","This is the current coinArrayList: " + coinArrayList[i].toString())
        }
        return coinArrayList
    }

    // should it return the coin symbol?
    private fun searchCoins(name : String, coinList : ArrayList<Coin>) : Coin {
        // this is used in the text boxes and should be used when typing in textview
//        val capName = name.capitalize()
//        Log.i("SEARCHCOINS", "This is the desired coin: " + capName)
        var returnCoin : Coin = Coin("blank", "blk", "0")
        for (t in 0..(coinList.size-1)) {
            if (coinList[t].ticker.contains(name, true)) {
                //                testView!!.text  = coinList[t].name
                //                Log.i("SEARCHCOINS", "FOUND THE RIGHT COIN")
                //                Toast.makeText(this, "FOUND " + coinList[t].name, Toast.LENGTH_LONG).show()
                //                searchView1!!.setQuery(coinList[t].ticker, false)
                returnCoin = coinList[t]
                break
            }
        }
        return returnCoin
    }

    // this method takes two coins and converts how much coin2 a user is able to get with x amount of coin1
    fun coinConverter(coin1 : Int, coin2 : Int, amount : Int): Int {
        return (coin1 * amount)/coin2
    }
}
