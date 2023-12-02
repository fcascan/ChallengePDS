package com.fcascan.challengepds.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.fcascan.challengepds.R
import com.fcascan.challengepds.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private val _TAG = "FCC#MainActivity"

    //View Elements:
    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Lifecycle Log:
        Log.d(_TAG, "onCreate - TimeStamp: ${System.currentTimeMillis()}")
        //TODO almacenar en Room este lifecycle log

        //View Binding:
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        //Loading State Off:
        binding.progressBar.visibility = android.view.View.GONE

        //Button Factorial:
        binding.btnFactorial.setOnClickListener {
            if (binding.editTextNumber.text.toString() == "") {
                binding.tvFactorialResult.text = "Please enter a number"
                return@setOnClickListener
            }
            val number = binding.editTextNumber.text.toString().toLong()
            if (number < 0)
                binding.tvFactorialResult.text = "Math Error"
            else {
                val factorial = myFactorial(number)
                binding.tvFactorialResult.text = "$number!  =  $factorial"
                //TODO considerar el caso de que el número sea muy grande
            }
        }

        //Button Recursive Factorial:
        binding.btnFactorialV2.setOnClickListener {
            if (binding.editTextNumber.text.toString() == "") {
                binding.tvFactorialResult.text = "Please enter a number"
                return@setOnClickListener
            }
            val number = binding.editTextNumber.text.toString().toLong()
            if (number < 0)
                binding.tvFactorialResult.text = "Math Error"
            else {
                val factorial = myRecursiveFactorial(number)
                binding.tvFactorialResult.text = "$number!  =  $factorial"
                //TODO considerar el caso de que el número sea muy grande
            }
        }

        //Button Time:
        binding.btnTime.setOnClickListener {
            val time = System.currentTimeMillis()
            Snackbar.make(binding.root, "Time: $time", Snackbar.LENGTH_LONG).show()
            //TODO esta logica tiene que ser mediante una pegada a una API
        }

        //Edit Text Enter Pressed:
        binding.editTextNumber.setOnKeyListener { _, keyCode, event ->
            if (keyCode == android.view.KeyEvent.KEYCODE_ENTER && event.action == android.view.KeyEvent.ACTION_UP) {
                binding.btnFactorial.performClick()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        //Lifecycle Log:
        Log.d(_TAG, "onCreate - TimeStamp: ${System.currentTimeMillis()}")
        //TODO almacenar en Room este lifecycle log

//        sharedViewModel.screenState.removeObservers(this)
        finish()
    }

    fun myFactorial(number: Long): Long {
        var result = 1L
        for (i in 1L..number) {
            result *= i
        }
        return result
    }

    fun myRecursiveFactorial(number: Long): Long {
        if (number == 0L) return 1
        return number * myRecursiveFactorial(number - 1)
    }
}