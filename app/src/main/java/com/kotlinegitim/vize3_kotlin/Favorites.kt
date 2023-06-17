package com.kotlinegitim.vize3_kotlin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference

class Favorites : AppCompatActivity() {

    lateinit var favorites : ListView
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        favorites = findViewById(R.id.myfavorites)


        val sharedPreferences = getSharedPreferences("Favorites", Context.MODE_PRIVATE)
        val sh2 = getSharedPreferences("FavoritesURL", MODE_PRIVATE)

        val sharedPreferenceIds = sharedPreferences.all.map { it.key }

        println("keys ${sharedPreferenceIds}")


        val adaptor = ArrayAdapter(this,android.R.layout.simple_list_item_1,sharedPreferenceIds)

        favorites.adapter = adaptor

        adaptor.notifyDataSetChanged()


        favorites.onItemClickListener = object : AdapterView.OnItemClickListener {

            override fun onItemClick(parent: AdapterView<*>, view: View,
                                     position: Int, id: Long) {

                val name = sharedPreferenceIds[position]

                val urlName = name+"URL"

                val urlSong = sh2.getString(urlName, "")

                println(urlSong)



                val intent = Intent(this@Favorites, MusicWindow::class.java)
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                intent.putExtra("url", urlSong)
                intent.putExtra("title", name)
                startActivity(intent)

                finish()

                //İkinci Yol favorilerden music activitesini açmak için
               /* database = Firebase.database.reference

                database.child("Musics").addValueEventListener(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        if (snapshot.exists()) {


                            snapshot.children.forEach {

                                val musics = it.getValue(MusicCategory::class.java)
                                for (music in musics!!.items!!) {

                                    if (music.title == name) {

                                        val intent = Intent(this@Favorites, MusicWindow::class.java)
                                        intent.putExtra("url", music.url)
                                        intent.putExtra("title", music.title)
                                        startActivity(intent)
                                    }
                                }

                            }
                        }


                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG)
                            .show()
                    }
                })*/


            }
        }


    }
    override fun onBackPressed() {


        var intent = Intent(this@Favorites, MainActivity::class.java)
        startActivity(intent)
        finish()

    }

}
