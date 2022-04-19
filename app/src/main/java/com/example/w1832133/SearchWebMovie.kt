package com.example.w1832133

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SearchWebMovie : AppCompatActivity() {

    private var searchBox: EditText? = null
    private var searchBtn: Button? = null
    private var resultsBox: TextView? = null

    private var urlString: String? = null
    private var dbData: String? = null
    private var  movieTitles: ArrayList<String> = ArrayList()

    // Assigning the API key
    private var keyAPI: String = "e1156dbb"

    override fun onCreate(savedInstanceState: Bundle?) {
        // Always call the superclass first
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_web_movie)

        // retrieve the Button, EditText & TextView
        searchBox = findViewById(R.id.webSearchBox)
        searchBtn = findViewById(R.id.webSearchBtn)
        resultsBox = findViewById(R.id.webResultsBox)

        // set the click listeners for the searchBtn & call the getWebMovie function
        searchBtn?.setOnClickListener {
            getWebMovie()
        }

        // https://developer.android.com/guide/components/activities/activity-lifecycle
        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            val movieDetails = savedInstanceState.getCharSequence("MovieDetails")
            val searchBoxData = savedInstanceState.getString("SearchBoxData")

            resultsBox?.text = movieDetails
            searchBox?.setText(searchBoxData)

            Log.d("**** resultsBox:*** ", "" + movieDetails)
            Log.d("**** searchBox:*** ", "" + searchBoxData)
        }
    }

    // https://developer.android.com/guide/components/activities/activity-lifecycle#save-simple,-lightweight-ui-state-using-onsaveinstancestate
    // invoked when the activity may be temporarily destroyed, save the instance state here
    override fun onSaveInstanceState(outState: Bundle) {
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState)
        outState.putString("MovieDetails", resultsBox?.text.toString())
        outState.putCharSequence("SearchBoxData", (searchBox?.text.toString()))
    }

    private fun getWebMovie() {
        val movie = searchBox!!.text.toString().trim()
        Log.d("**** movie:*** ", "" + movie)

        // Checking if user entered value or not in the search box
        if (movie  == "") {
            Toast.makeText(applicationContext, "Enter The Movie's Name First", Toast.LENGTH_SHORT).show()
            resultsBox?.text = ""
            return
        }

        // collecting all the JSON string
        val stb = StringBuilder("")

        // start the fetching of data in the background
        runBlocking {
            // run the code of the coroutine in a new thread
            withContext(Dispatchers.IO){
                // Setting up the fetching URL for the web service
                urlString = "https://www.omdbapi.com/?s=${movie}&apikey=${keyAPI}"
                val url = URL(urlString)
                val con: HttpURLConnection = url.openConnection() as HttpURLConnection
                val bf: BufferedReader

                try {
                    bf = BufferedReader(InputStreamReader(con.inputStream))
                }catch (e: IOException){
                    e.printStackTrace()
                    return@withContext
                }

                // read all lines JSON info  in a string builder
                var line = bf.readLine()
                while (line != null){
                    stb.append(line)
                    line = bf.readLine()
                }

                // Picking up all the data
                for (movieList in parseJSON(stb)){
                    dbData += movieList + "\n"
                }
            }
            // update the textview with the movie's details entered by the user
            resultsBox?.setText(dbData)
        }
    }

    // extracts the relevant info from the JSON returned by the Web Service
    private fun parseJSON(stb: StringBuilder): ArrayList<String> {
        // extract the actual data
        val json = JSONObject(stb.toString())
        val sArray = json.getJSONArray("Search")

        for (i in 0 until sArray.length()) {
            val movies = sArray.getJSONObject(i)
            val movieTitle = movies["Title"] as String
            movieTitles.add(movieTitle)
        }
        return movieTitles
    }
}