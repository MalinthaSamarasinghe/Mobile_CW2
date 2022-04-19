package com.example.w1832133

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SearchMovie : AppCompatActivity() {

    private var searchBox: EditText? = null
    private var searchBtn: Button? = null
    private var resultsBox: TextView? = null
    private var saveBtn: Button? = null
    private var urlString: String? = null
    private var dbData: String? = null

    // Assigning the API key
    private var keyAPI: String = "e1156dbb"

    // Search values for each movies
    private var movieTitle: String? = null
    private var movieYear: Int? = null
    private var movieRated: String? = null
    private var movieReleased: String? = null
    private var movieRuntime: String? = null
    private var movieGenre: String? = null
    private var movieDirector: String? = null
    private var movieWriter: String? = null
    private var movieActors: String? = null
    private var moviePlot: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        // Always call the superclass first
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_movie)

        // inflating the 2 Buttons, EditText & TextView from XML
        searchBox = findViewById(R.id.movieSearchBox)
        searchBtn = findViewById(R.id.movieSearchBtn)
        resultsBox = findViewById(R.id.movieResultsBox)
        saveBtn = findViewById(R.id.movieSaveBtn)

        // set the click listeners for the searchBtn & call the retrieveMovie function
        searchBtn?.setOnClickListener {
            retrieveMovie()
        }

        // set the click listeners for the saveBtn & call the saveMovie function
        saveBtn?.setOnClickListener {
            saveMovie()
        }

        // https://developer.android.com/guide/components/activities/activity-lifecycle
        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            val movieDetails = savedInstanceState.getString("MovieDetails")
            val searchBoxData = savedInstanceState.getCharSequence("SearchBoxData")

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

    private fun retrieveMovie() {
        val movie = searchBox?.text.toString().trim()
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
                urlString = "https://www.omdbapi.com/?t=$movie&apikey=$keyAPI"
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
                dbData = parseJSON(stb)
            }
            // display the data
            resultsBox?.text = dbData
        }
    }

    // extracts the relevant info from the JSON returned by the Web Service
    private fun parseJSON(stb: StringBuilder): String {
        // extract the actual data
        val json = JSONObject(stb.toString())
        movieTitle = json["Title"] as String
        movieYear = (json["Year"] as String).toInt()
        movieRated = json["Rated"] as String
        movieReleased = json["Released"] as String
        movieRuntime = json["Runtime"] as String
        movieGenre = json["Genre"] as String
        movieDirector = json["Director"] as String
        movieWriter = json["Writer"] as String
        movieActors = json["Actors"] as String
        moviePlot = json["Plot"] as String

        return  "Title: " + movieTitle +
                "\nYear: " + movieYear +
                "\nRated: " + movieRated +
                "\nReleased: " + movieReleased +
                "\nRuntime: " + movieRuntime +
                "\nGenre: " + movieGenre +
                "\nDirector: " + movieDirector +
                "\nWriter: " + movieWriter +
                "\nActors: " + movieActors +
                "\n\nPlot: " + moviePlot
    }

    private fun saveMovie() {
        // Creating an instance of the database
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "MovieDB").build()
        // Creating an instance of the Dao object
        val movieDao = db.movieDao()

        // start the fetching of data in the background
        runBlocking {
            launch{
                // Saving the searched movie results in the DataBase
                val movies: List<Movie> = movieDao.getAll()
                val index = movies.size + 1
                val mov = Movie(index, movieTitle, movieYear, movieRated, movieReleased, movieRuntime, movieGenre, movieDirector, movieWriter, movieActors, moviePlot)

                // if retrieved movie is then added to the database
                if (movieTitle !== null){
                    Log.d("**** movieTitle:*** ", "Not Null" )
                    movieDao.insertMovies(mov)
                    Toast.makeText(applicationContext, "Movie Added To Database", Toast.LENGTH_SHORT).show()
                }else{
                    Log.d("**** movieTitle:*** ", "Null" )
                    Toast.makeText(applicationContext, "Please Retrieve Movie First", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}