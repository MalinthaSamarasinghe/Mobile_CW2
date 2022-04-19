package com.example.w1832133

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.room.Room
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SearchActor : AppCompatActivity() {

    private var searchBox: EditText? = null
    private var searchBtn: Button? = null
    private var resultsBox: TextView? = null

    private lateinit var movieTitle: String

    override fun onCreate(savedInstanceState: Bundle?) {
        // Always call the superclass first
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_actor)

        // inflating the Button, EditText & TextView from XML
        searchBox = findViewById(R.id.actorSearchBox)
        searchBtn = findViewById(R.id.actorSearchBtn)
        resultsBox = findViewById(R.id.actorResultsBox)

        // set the click listeners for the searchBtn & call the getActor function
        searchBtn?.setOnClickListener {
            getActor()
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

    private fun getActor() {
        // Creating an instance of the database
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "MovieDB").build()
        // Creating an instance of the Dao object
        val movieDao = db.movieDao()

        // Getting the actor's name
        val actor = searchBox!!.text.toString().trim()
        Log.d("**** actor:*** ", "" + actor)

        // Checking if user entered value or not in the search box
        if (actor  == "") {
            Toast.makeText(applicationContext, "Enter The Actor's Name First", Toast.LENGTH_SHORT).show()
            resultsBox?.text = ""
            return
        }

        // start the fetching of data in the background
        runBlocking {
            launch{
                val movies =  movieDao.getSpecificMovieTitle(actor)
                // Emptying the results box for every search
                movieTitle = ""
                Log.d("**** movieTitle:*** ", "" + movieTitle)

                if (movies.isEmpty()){
                    Log.d("**** List<Movie>:*** ", "Empty List" )
                    Toast.makeText(applicationContext, "There Are No Related Movies", Toast.LENGTH_SHORT).show()
                    resultsBox?.text = ""
                } else {
                    for ( movie in movies){
                        Log.d("**** List<Movie>:*** ", "Not Empty List" )
                        movieTitle += "${movie.title.toString()}\n"

                    }
                    // display the data
                    resultsBox?.text = movieTitle
                }
            }
        }
    }
}