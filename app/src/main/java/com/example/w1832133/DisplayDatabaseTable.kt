package com.example.w1832133

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.room.Room
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DisplayDatabaseTable : AppCompatActivity() {
    private lateinit var tableData: TextView
    private val dbData: ArrayList<String> = ArrayList()
    private var result = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        // Always call the superclass first
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_database_table)

        // inflating the TextView from XML
        tableData = findViewById(R.id.tableView)

        // Calling displayMovieList() function & Displaying the movie list in the Database
        displayMovieList()
    }

    private fun displayMovieList(){
        // Creating an instance of the database
        // https://developer.android.com/reference/android/arch/persistence/room/Room#databasebuilder
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "MovieDB").build()
        // Creating an instance of the Dao object
        val movieDao = db.movieDao()

        runBlocking {
            launch {
                val movies: List<Movie> = movieDao.getAll()
                for (movie in movies) {
                    dbData.add("\nId: ${movie.id}\n\nTitle: ${movie.title}\n\nYear: ${movie.year}\n\nRated: ${movie.rated}\n\nReleased: ${movie.released}\n\nRuntime: ${movie.runtime}\n\nGenre: ${movie.genre}\n\nDirector: ${movie.director}\n\nWriter: ${movie.writer}\n\nActors: ${movie.actors}\n\nPlot: ${movie.plot}")
                }

                for (data in dbData) {
                    result += "$data\n _______________________________________________ \n"
                }

                // display the data
                tableData.text = result
            }
        }
    }
}