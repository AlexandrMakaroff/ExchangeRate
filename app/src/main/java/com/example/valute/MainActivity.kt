package com.example.valute

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

private const val KEY_NAME = "NAME"
private const val KEY_VALUTE = "VALUTE"

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    var name = ArrayList<String>()
    var valute = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val updateButton = findViewById<Button>(R.id.update)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)

        if (savedInstanceState == null) {
            Toast.makeText(this@MainActivity, "Загружено", Toast.LENGTH_SHORT).show()
            GetURLDATA().execute("https://www.cbr-xml-daily.ru/daily_json.js")
        }

        updateButton.setOnClickListener {
            GetURLDATA().execute("https://www.cbr-xml-daily.ru/daily_json.js")
            Toast.makeText(this@MainActivity, "Обновлено", Toast.LENGTH_SHORT).show()
        }
    }

    inner class GetURLDATA: AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
//            Toast.makeText(this@MainActivity, "Ожидайте...", Toast.LENGTH_SHORT).show()
        }

        override fun doInBackground(vararg params: String?): String {
            var connector: HttpURLConnection? = null
            val reader: BufferedReader? = null

            try {
                val url = URL(params[0])

                connector = url.openConnection() as HttpURLConnection
                connector.connect()
                val stream: InputStream = connector.inputStream
                return stream.bufferedReader().use { it.readText() }
            } catch (e: MalformedURLException) {
                e.printStackTrace()
                return "MalformedURLException"
            } catch (e: IOException) {
                e.printStackTrace()
                return "IOException"
            } catch (e: Exception) {
                e.printStackTrace()
                return "Exception"
            } finally {
                connector?.disconnect()
                reader?.close()
            }
        }

        @SuppressLint("SetTextI18n")
        override fun onPostExecute(result: String) {
            super.onPostExecute(result)

            try {
                val json = JSONObject(result)
                val str: String = json.getJSONObject("Valute").toString()
                val jsonObject = JSONObject(str.trim())
                val keys: Iterator<String> = jsonObject.keys()
                while (keys.hasNext()) {
                    val key = keys.next()
                    if (jsonObject.get(key) is JSONObject) {
                        Log.d("MY_TAG", (jsonObject.get(key) as JSONObject).getString("Name"))
                        val data = (jsonObject.get(key) as JSONObject)
                        name.add(data.getString("Name") + ":")
                        valute.add(data.getDouble("Value").toString() + " руб.")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            startAdapter()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putStringArrayList(KEY_NAME, name)
        outState.putStringArrayList(KEY_VALUTE, valute)
    }

    private fun startAdapter() {
        val helperAdapter = HelperAdapter(name, valute, this@MainActivity)
        recyclerView.adapter = helperAdapter
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        name = savedInstanceState.getStringArrayList(KEY_NAME) as ArrayList<String>
        valute = savedInstanceState.getStringArrayList(KEY_VALUTE) as ArrayList<String>
        startAdapter()

        Toast.makeText(this@MainActivity, "Перевернуто", Toast.LENGTH_SHORT).show()
    }
}