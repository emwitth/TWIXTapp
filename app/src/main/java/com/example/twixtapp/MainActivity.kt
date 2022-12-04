package com.example.twixtapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.twixtapp.databinding.ActivityMainBinding
import java.io.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // view model instance
        viewModel = ViewModelProvider(this)[GameViewModel::class.java]

        // read file
        var path = filesDir.canonicalPath
        for(file in filesDir.listFiles()) {
            Log.v("rootbeer", file.name)
            if(file.name == "savedGame.txt") {
                path = file.canonicalPath
            }
        }
        try {
            Log.v("rootbeer", "reading")
            val fileReader = FileReader(path)
            Log.v("rootbeer", fileReader.toString())
//            Log.v("rootbeer", fileReader.readText())
            for(line in fileReader.readLines())
                Log.v("rootbeer", line)


        } catch(e: Exception) {
            Log.v("rootbeer",e.toString())
            e.printStackTrace()
        }
    }

    override fun onPause() {
        super.onPause()
        var path = filesDir.canonicalPath
        for(file in filesDir.listFiles()) {
            Log.v("rootbeer", file.name)
            if(file.name == "savedGame.txt") {
                path = file.canonicalPath
            }
        }
        if(path == filesDir.canonicalPath) {
            path = "$path/savedGame.txt"
        }
        if(!viewModel.hasBlackWon && !viewModel.hasRedWon) {
            try {
                val fileWriter = FileWriter(path)
                val currentBoard = viewModel.formatForFile()
                for(line in currentBoard) {
                    fileWriter.append(line+"\n")
                }
                fileWriter.close();
            } catch (e: Exception) {
                Log.v("rootbeer",e.toString())
                e.printStackTrace()
            }
        }
        else {
            try {
                val fileWriter = FileWriter(path)
                fileWriter.append("start new game\n")
                fileWriter.close();
            } catch (e: Exception) {
                Log.v("rootbeer",e.toString())
                e.printStackTrace()
            }
        }
    }
}