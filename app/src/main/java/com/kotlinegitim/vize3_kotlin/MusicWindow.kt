package com.kotlinegitim.vize3_kotlin

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MusicWindow : AppCompatActivity() {

    lateinit var mediaPlayer: MediaPlayer
    lateinit var volSeekBar: SeekBar
    lateinit var pauseBtn : Button
    lateinit var startBtn : Button
    lateinit var favoriteBtn : Button
    lateinit var musicName : TextView

   // private val sharedPrefFile = "musicplayer"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_window)


        volSeekBar = findViewById(R.id.volSeekBar)
        pauseBtn = findViewById(R.id.pause)
        startBtn = findViewById(R.id.start)
        favoriteBtn = findViewById(R.id.favorite)
        musicName = findViewById(R.id.musictitle)



        var title = intent.getStringExtra("title")
        musicName.text = title

        checkFavorite(title.toString())



        val url = intent.getStringExtra("url").toString().replace("http://","https://")

        favoriteBtn.setOnClickListener{


            musicFavoriteControl(title.toString(), url)

        }



        println("url ${url}")
        mediaPlayer = MediaPlayer.create(this, Uri.parse(url))
        mediaPlayer.start()

        val lastVol = getVolumeFromPreferences()

        println("vol ${lastVol}")


        mediaPlayer.setVolume(lastVol*100/100, lastVol*100/100)
        volSeekBar.progress =(lastVol*100).toInt()

        volSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                Log.d("Int", p1.toString())
                Log.d("Boolean", p2.toString())

                val f1 = p1.toFloat() / 100
                mediaPlayer.setVolume(f1, f1)

                saveVolumeToPreferences(f1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

        pauseBtn.setOnClickListener{



            mediaPlayer.pause()

        }

        startBtn.setOnClickListener{

            mediaPlayer.start()
        }



    }

    override fun onBackPressed() {


        val f1_temp = getVolumeFromPreferences()

        saveVolumeToPreferences(f1_temp)

        mediaPlayer.stop()

        var intent = Intent(this@MusicWindow, MainActivity::class.java)
        startActivity(intent)
        finish()

    }

    override fun onDestroy() {
        val f1_temp = getVolumeFromPreferences()
        saveVolumeToPreferences(f1_temp)
        super.onDestroy()
    }

    override fun onStop() {
        val f1_temp = getVolumeFromPreferences()

        saveVolumeToPreferences(f1_temp)
        super.onStop()
    }

    fun saveVolumeToPreferences(f1:Float){

        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()
        myEdit.putFloat("volume",f1)
        myEdit.apply()


    }

    fun getVolumeFromPreferences() : Float{

        val sh = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val vol = sh.getFloat("volume", 0.5F)

        return vol

    }

    fun musicFavoriteControl(title : String, url: String){

        val sh = getSharedPreferences("Favorites", MODE_PRIVATE)
        val sh2 = getSharedPreferences("FavoritesURL", MODE_PRIVATE)
        val IsFavorite = sh.getString(title, "false")

        val songURL = title+"URL"

        if (IsFavorite == "false"){
            val myEdit = sh.edit()
            myEdit.putString(title,"true")
            myEdit.apply()
            val myEdit2 = sh2.edit()
            myEdit2.putString(songURL,url)
            myEdit2.apply()
            favoriteBtn.setBackgroundResource(R.drawable.yesfavorite_btn)

        }

        else{

            val myEdit = sh.edit()
            myEdit.remove(title)
            myEdit.apply()
            val myEdit2 = sh2.edit()
            myEdit2.remove(songURL)
            myEdit2.apply()
            favoriteBtn.setBackgroundResource(R.drawable.nofavorite_btn)



        }


    }

    fun checkFavorite(title : String){

        val sh = getSharedPreferences("Favorites", MODE_PRIVATE)
        val IsFavorite = sh.getString(title, "false")

        if (IsFavorite == "true"){

            favoriteBtn.setBackgroundResource(R.drawable.yesfavorite_btn)

        }

        else{

            favoriteBtn.setBackgroundResource(R.drawable.nofavorite_btn)

        }


    }
}