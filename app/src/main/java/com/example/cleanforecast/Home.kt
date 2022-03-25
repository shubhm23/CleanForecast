package com.example.cleanforecast

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import kotlinx.coroutines.*
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class Home : AppCompatActivity() {
    var iconUrl : String? = "https://openweathermap.org/img/wn/50d@2x.png"
    var CITY: String? = null
    val API: String = BuildConfig.apiKey

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CITY = intent.getStringExtra("Data").toString()
       weatherTask().execute()
       Glide.with(this).load(iconUrl).into(findViewById<ImageView>(R.id.iconImg))
        Log.d(TAG, "This is the current value of URL = $iconUrl")
//        GlobalScope.launch ( Dispatchers.IO ) {
//            val answer1 = launch { weatherTask().execute() }
//            Log.d(TAG, "This is the 3rd value of URL = $answer1.await()")
//            val answer2 = launch { weatherIcon()}
//            answer1.join()
//            answer2.join()
//        }

//        val handler = Handler(Looper.getMainLooper())
//        handler.postDelayed({
//            // do something after 1000ms
//            Glide.with(this).load(iconUrl).into(findViewById<ImageView>(R.id.iconImg))
//        }, 8000L)

//        Glide.with(this).load(weatherTask().iconUrl).into(findViewById<ImageView>(R.id.iconImg))
    }
//    suspend fun weatherIcon() {
//        delay(3000L)
//        Log.d(TAG, "This is the 3rd value of URL = $iconUrl")
//    }
    inner class weatherTask(): AsyncTask<String, Void, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
            findViewById<TextView>(R.id.errorText).visibility = View.GONE
        }

        override fun doInBackground(vararg p0: String?): String? {
            var response: String?
            try{
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API").readText(
                    Charsets.UTF_8
                )
            }catch (e: Exception){
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try{
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)

                val updatedAt:Long = jsonObj.getLong("dt")
                val updatedAtText = "Updated at: "+ SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
                    Date(updatedAt*1000)
                )
                val temp = main.getString("temp")+"°C"
                val tempMin = "Min Temp: " + main.getString("temp_min")+"°C"
                val tempMax = "Max Temp: " + main.getString("temp_max")+"°C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")

                val sunrise:Long = sys.getLong("sunrise")
                val sunset:Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescription = weather.getString("description")
                val icon = weather.getString("icon")
                iconUrl = "http://openweathermap.org/img/wn/"+icon+"@2x.png"
                val address = jsonObj.getString("name")+", "+sys.getString("country")
                Log.d(TAG, "This is the next current value of URL = $iconUrl")
                /*Data Plotting*/

                findViewById<TextView>(R.id.address).text = address
                findViewById<TextView>(R.id.updatedAt).text =  updatedAtText
                findViewById<TextView>(R.id.status).text = weatherDescription.capitalize()
                findViewById<TextView>(R.id.temp).text = temp
                findViewById<TextView>(R.id.tempMin).text = tempMin
                findViewById<TextView>(R.id.tempMax).text = tempMax
                findViewById<TextView>(R.id.sunrise).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise*1000))
                findViewById<TextView>(R.id.sunset).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset*1000))
                findViewById<TextView>(R.id.wind).text = windSpeed
                findViewById<TextView>(R.id.pressure).text = pressure
                findViewById<TextView>(R.id.humidity).text = humidity

                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE

            }catch (e: Exception) {
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
            }
        }
    }

}