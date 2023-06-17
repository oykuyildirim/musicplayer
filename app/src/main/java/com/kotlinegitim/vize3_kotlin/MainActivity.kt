package com.kotlinegitim.vize3_kotlin

import com.kotlinegitim.vize3_kotlin.customadaptor.CustomExpandableListAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ExpandableListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kotlinegitim.vize3_kotlin.client.ApiClient
import com.kotlinegitim.vize3_kotlin.service.MockiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var Service : MockiService


    lateinit var expandableListView : ExpandableListView
    private lateinit var database: DatabaseReference

    lateinit var favorite : Button

    var music = mutableListOf<MusicCategory>()


    var expandableListDetail = LinkedHashMap<String, List<Item>>()
    private var titleList: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        expandableListView = findViewById(R.id.expendableList)
        database = Firebase.database.reference
        Service = ApiClient.getClient().create(MockiService::class.java)
        favorite = findViewById(R.id.favorites)

        favorite.setOnClickListener{

            var intent = Intent(this, Favorites:: class.java)
            startActivity(intent)
            finish()

        }


    }

    override fun onStart() {

        database.child("Musics").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if ( !snapshot.exists() ) {

                    Service.allMusic().enqueue(object : Callback<Music>{
                        override fun onResponse(
                            call: Call<Music>,
                            response: Response<Music>
                        ) {


                            val musics = response.body()


                            if (musics != null){



                                database.child("Musics").setValue(musics.musicCategories)

                                for (music in musics.musicCategories){

                                        expandableListDetail[music.baseTitle!!] = music.items!!

                                }

                                expandableLoader(musics.musicCategories)




                            }
                        }

                        override fun onFailure(call: Call<Music>, t: Throwable) {
                            println("Not yet implemented")
                        }

                    })

                }

                else{

                    database.child("Musics").addValueEventListener(object :
                        ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {

                            if ( snapshot.exists() ) {


                                snapshot.children.forEach {

                                    val musicList = it.getValue(MusicCategory::class.java)
                                    music.add(musicList as MusicCategory)

                                }
                            }

                            expandableListDetail.clear()

                            for(ms in music){

                                expandableListDetail[ms.baseTitle.toString()] = ms.items!!

                            }

                            expandableLoader(music)




                        }
                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG).show()
                        }
                    })

                }

            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG).show()
            }
        })
        super.onStart()
    }

    fun expandableLoader(music : List<MusicCategory>){

        if (expandableListView != null) {
            val listData = expandableListDetail

            titleList = ArrayList(listData.keys)
            val adapter = CustomExpandableListAdapter(this@MainActivity, titleList as ArrayList<String>, listData)
            expandableListView!!.setAdapter(adapter)
            adapter.notifyDataSetChanged()
            expandableListView!!.setOnGroupExpandListener { groupPosition ->

            }
            expandableListView!!.setOnGroupCollapseListener { groupPosition ->

            }
            expandableListView!!.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->



                val url = music[groupPosition].items!!.get(childPosition).url
                val title = music[groupPosition].items!!.get(childPosition).title

                val intent = Intent (this@MainActivity, MusicWindow:: class.java)
                intent.putExtra("url",url)
                intent.putExtra("title",title)
                startActivity(intent)
                finish()

                false
            }


        }


    }

}

