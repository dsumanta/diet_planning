package com.example.chatapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.IOException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class CalorieIntake : Fragment() {

    private lateinit var mySpinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calorie_intake, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mySpinner = view.findViewById(R.id.mySpinner)

        val options = arrayOf("Breakfast", "Lunch", "Dinner")

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mySpinner.adapter = adapter

       val fetch:Button=view.findViewById(R.id.fetch_button)
        fetch.setOnClickListener(){
            fetchApiData()
        }
    }
    private fun fetchApiData() {
        CoroutineScope(Dispatchers.IO).launch {
            val apiKey = "122b65e914df4116b8e55c2aa9c93fe7"
            val url = URL("https://api.spoonacular.com/recipes/findByNutrients?apiKey=122b65e914df4116b8e55c2aa9c93fe7&minCarbs=10")

            try {
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val responseCode = connection.responseCode

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val response = StringBuilder()
                    var line: String?

                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }

                    reader.close()

                    val responseData = response.toString()
                    println(responseData)
                    withContext(Dispatchers.Main) {
                        // Update UI with the data
                        Toast.makeText(requireContext(), responseData, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    println("Error: $responseCode")
                }

                connection.disconnect()
            } catch (e: IOException) {
                println("Error: ${e.message}")
            }
        }
    }

}
