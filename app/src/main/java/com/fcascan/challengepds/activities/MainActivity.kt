package com.fcascan.challengepds.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.fcascan.challengepds.databinding.ActivityMainBinding
import com.fcascan.challengepds.repositories.MainRepository
import com.fcascan.challengepds.services.RetrofitService
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private val _TAG = "FCC#MainActivity"

    //View Elements:
    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!

    //ViewModels:
    lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Initialize ViewModel:
        val retrofitService = RetrofitService.getInstance()
        val mainRepository = MainRepository(retrofitService)
        mainActivityViewModel = ViewModelProvider(this, MyViewModelFactory(mainRepository))[MainActivityViewModel::class.java]

        //Lifecycle Log:
        Log.d("$_TAG - onCreate", "TimeStamp: ${System.currentTimeMillis()}")
        mainActivityViewModel.saveTimeStamp()

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
                val factorial = mainActivityViewModel.myFactorial(number)
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
                val factorial = mainActivityViewModel.myRecursiveFactorial(number)
                binding.tvFactorialResult.text = "$number!  =  $factorial"
                //TODO considerar el caso de que el número sea muy grande
            }
        }

        //Button Time:
        binding.btnTime.setOnClickListener {
            mainActivityViewModel.getTime()
        }

        //Edit Text Enter Pressed:
        binding.editTextNumber.setOnKeyListener { _, keyCode, event ->
            if (keyCode == android.view.KeyEvent.KEYCODE_ENTER && event.action == android.view.KeyEvent.ACTION_UP) {
                binding.btnFactorial.performClick()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        //LiveData:
        mainActivityViewModel.currentTime.observe(this) { timeDTO ->
            Log.d("$_TAG - onCreate", "currentTime changed: $timeDTO")
            Snackbar.make(binding.root, "Current DateTime: ${timeDTO.currentDateTime}", Snackbar.LENGTH_LONG).show()
        }

        mainActivityViewModel.errorMessage.observe(this) { errorMessage ->
            Log.d("$_TAG - onCreate", "errorMessage changed: $errorMessage")
            Snackbar.make(binding.root, "Error: $errorMessage", Snackbar.LENGTH_LONG).show()
        }

        mainActivityViewModel.loading.observe(this) { loading ->
            Log.d("$_TAG - onCreate", "loading changed: $loading")
            if (loading)
                binding.progressBar.visibility = android.view.View.VISIBLE
            else
                binding.progressBar.visibility = android.view.View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        //Lifecycle Log:
        Log.d("$_TAG - onDestroy", "TimeStamp: ${System.currentTimeMillis()}")
        mainActivityViewModel.saveTimeStamp()

        //Remove Observers:
        mainActivityViewModel.currentTime.removeObservers(this)

        finish()
    }
}