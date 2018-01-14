package com.spikeyduyphan.sidecryptobar

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.TextView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    var cryptoList = ArrayList<Crypto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // set listener for convert button, when pressed, should display result

    }

    // REST web service call to get data from coinmarketcap API
    class RetrieveFeedTask : AsyncTask<URL, Void, JSONObject>() {
        override fun doInBackground(vararg p0: URL?): JSONObject? {
            var conn : HttpURLConnection? = null
            try {
                conn = p0[0]!!.openConnection() as HttpURLConnection
                val response = conn.responseCode
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
                    } catch (e : IOException) {
                        Log.e("WEB","Website not returning correct result")
                    }
                    return JSONObject(strBuilder.toString())
                } else {
                    Log.e("WEB","Website connection not ok")
                }
            } catch (e : Exception) {
                Log.e("ASYNC","Cannot open a connection")
            } finally {
                conn!!.disconnect()
            }
            return null
        }

        override fun onPostExecute(result: JSONObject?) {
            // need to convert the jsonObject into an array list

        }
    }

    // convert JSONObject into an arrayList
    private fun convertJSONArrayList(obj : JSONObject) {
        cryptoList.clear() // make sure the list is clear and updated each time
        try {
            var jArray : JSONArray = obj.getJSONArray("{")
            // convert the gathered objects into Crypto Class
            // this may be hard coded if we are only taking a certain number of tickers into account
            // TODO fix the array
//            for (i in jArray.) {
//
//            }
        } catch (e : JSONException) {
            Log.e("JSON","JSON ERROR")
        }
    }
}
