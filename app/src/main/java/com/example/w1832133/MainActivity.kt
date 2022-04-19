package com.example.w1832133

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.room.Room
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private lateinit var addToDbBtn: Button
    private lateinit var searchMovieBtn: Button
    private lateinit var searchActorBtn: Button
    private lateinit var searchFromWebBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        // Always call the superclass first
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // inflating the 4 Buttons from XML
        addToDbBtn = findViewById(R.id.addToDbBtn)
        searchMovieBtn = findViewById(R.id.searchMovieBtn)
        searchActorBtn = findViewById(R.id.searchActorBtn)
        searchFromWebBtn = findViewById(R.id.searchFromWebBtn)

        // set the click listeners for the addToDbBtn & call the addMovies function
        addToDbBtn.setOnClickListener{
            addMovies()
        }

        // set the click listeners for the searchMovieBtn & call the displaySearchMoviePage function
        searchMovieBtn.setOnClickListener{
            displaySearchMoviePage()
        }

        // set the click listeners for the searchActorBtn & call the displaySearchActorPage function
        searchActorBtn.setOnClickListener {
            displaySearchActorPage()
        }

        // set the click listeners for the searchFromWebBtn & call the displaySearchWebMoviePage function
        searchFromWebBtn.setOnClickListener {
            displaySearchWebMoviePage()
        }

    }

    private fun addMovies() {
        // Creating an instance of the database
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "MovieDB").build()
        // Creating an instance of the Dao object
        val movieDao = db.movieDao()

        runBlocking {
            launch {
                val movie1 = Movie(
                    1,
                    "The Shawshank Redemption",
                    1994,
                    "R",
                    "14 Oct 1994",
                    "142 min",
                    "Drama",
                    "Frank Darabont",
                    "Stephen King, Frank Darabont",
                    "Tim Robbins, Morgan Freeman, Bob Gunton",
                    "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency."
                )

                val movie2 = Movie(
                    2,
                    "Batman: The Dark Knight Returns, Part 1",
                    2012,
                    "PG-13",
                    "25 Sep 2012",
                    "76 min",
                    "Animation, Action, Crime, Drama, Thriller",
                    "Jay Oliva",
                    "Bob Kane (character created by: Batman), Frank Miller (comic book), Klaus Janson (comic book), Bob Goodman",
                    "Peter Weller, Ariel Winter, David Selby, Wade Williams",
                    "Batman has not been seen for ten years. A new breed of criminal ravages Gotham City, forcing 55-year-old Bruce Wayne back into the cape and cowl. But, does he still have what it takes to fight crime in a new era?"
                )

                val movie3 = Movie(
                    3,
                    "The Lord of the Rings: The Return of the King",
                    2003,
                    "PG-13",
                    "17 Dec 2003",
                    "201 min",
                    "Action, Adventure, Drama",
                    "Peter Jackson",
                    "J.R.R. Tolkien, Fran Walsh, Philippa Boyens",
                    "Elijah Wood, Viggo Mortensen, Ian McKellen",
                    "Gandalf and Aragorn lead the World of Men against Sauron's army to draw his gaze from Frodo and Sam as they approach Mount Doom with the One Ring."
                )

                val movie4 = Movie(
                    4,
                    "Inception",
                    2010,
                    "PG-13",
                    "16 Jul 2010",
                    "148 min",
                    "Action, Adventure, Sci-Fi",
                    "Christopher Nolan",
                    "Christopher Nolan",
                    "Leonardo DiCaprio, Joseph Gordon-Levitt, Elliot Page",
                    "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O., but his tragic past may doom the project and his team to disaster."
                )

                val movie5 = Movie(
                    5,
                    "The Matrix",
                    1999,
                    "R",
                    "31 Mar 1999",
                    "136 min",
                    "Action, Sci-Fi",
                    "Lana Wachowski, Lilly Wachowski",
                    "Lana Wachowski, Lilly Wachowski",
                    "Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss",
                    "When a beautiful stranger leads computer hacker Neo to a forbidding underworld, he discovers the shocking truth--the life he knows is the elaborate deception of an evil cyber-intelligence."
                )

                // Populating Database
                movieDao.insertMovies(movie1, movie2, movie3, movie4, movie5)
                // Toast.makeText(applicationContext, "Movies Added to Database", Toast.LENGTH_SHORT).show()

                // Calling displayDatabaseTablePage() function & Displaying Database table
                displayDatabaseTablePage()
            }
        }
    }

    // Displaying the DisplayDatabaseTable Page that movie data saved in the Database
    private fun displayDatabaseTablePage() {
        val contactIntent = Intent(this, DisplayDatabaseTable::class.java)
        startActivity(contactIntent)
    }

    // Displaying the SearchMovie Page
    private fun displaySearchMoviePage() {
        val contactIntent = Intent(this, SearchMovie::class.java)
        startActivity(contactIntent)
    }

    // Displaying SearchActor Page
    private fun displaySearchActorPage(){
        val contactIntent = Intent(this, SearchActor::class.java)
        startActivity(contactIntent)
    }

    // Displaying SearchWebMovie Page
    private fun displaySearchWebMoviePage(){
        val contactIntent = Intent(this, SearchWebMovie::class.java)
        startActivity(contactIntent)
    }

}