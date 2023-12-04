package com.fcascan.challengepds.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fcascan.challengepds.adapters.EventsAdapter
import com.fcascan.challengepds.databinding.ActivityMainBinding
import com.fcascan.challengepds.repositories.MainRepository
import com.fcascan.challengepds.services.RetrofitService
import com.fcascan.challengepds.database.EventsDatabase
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private val _TAG = "FCC#MainActivity"

    //View Elements:
    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var recViewAdapter: EventsAdapter

    //ViewModels:
    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Initialize ViewModel:
        // Using by lazy so the database and the repository are only created when they're needed rather than when the application starts
        val database by lazy { EventsDatabase.getDatabase(this)}
        val eventsDAO by lazy { database.eventsDao() }
        val retrofitService = RetrofitService.getInstance()
        val mainRepository = MainRepository(retrofitService, eventsDAO)
        mainActivityViewModel = ViewModelProvider(this, MyViewModelFactory(mainRepository))[MainActivityViewModel::class.java]

        //Lifecycle Log:
        Log.d("$_TAG - onCreate", "Init")
        mainActivityViewModel.saveTimeStamp("${javaClass.simpleName} - ${Thread.currentThread().stackTrace[2].methodName}")

        //View Binding:
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        //Fill RecyclerView:
        mainActivityViewModel.refreshRecView()

        //Loading State Off:
        binding.progressBar.visibility = android.view.View.GONE
    }

    override fun onStart() {
        super.onStart()

        //Lifecycle Log:
        Log.d("$_TAG - onStart", "Init")
        mainActivityViewModel.saveTimeStamp("${javaClass.simpleName} - ${Thread.currentThread().stackTrace[2].methodName}")

        //Button Factorial:
        binding.btnFactorial.setOnClickListener {
            if (binding.editTextNumber.text.toString() == "") {
                binding.tvFactorialResult.text = "Please enter a number"
                return@setOnClickListener
            }
            val number = binding.editTextNumber.text.toString().toLong()
            if (number > 15) binding.tvFactorialResult.textSize = 28f
            else binding.tvFactorialResult.textSize = 40f
            if (number < 0)
                binding.tvFactorialResult.text = "Math Error"
            else if (number > 20)
                binding.tvFactorialResult.text = "Result out of Long range"
            else {
                val factorial = mainActivityViewModel.myFactorial(number)
                binding.tvFactorialResult.text = "$number!  =  $factorial"
            }
        }

        //Button Recursive Factorial:
        binding.btnFactorialV2.setOnClickListener {
            if (binding.editTextNumber.text.toString() == "") {
                binding.tvFactorialResult.text = "Please enter a number"
                return@setOnClickListener
            }
            val number = binding.editTextNumber.text.toString().toLong()
            if (number > 15) binding.tvFactorialResult.textSize = 28f
            else binding.tvFactorialResult.textSize = 40f
            if (number < 0)
                binding.tvFactorialResult.text = "Math Error"
            else if (number > 20)
                binding.tvFactorialResult.text = "Result out of Long range"
            else {
                val factorial = mainActivityViewModel.myRecursiveFactorial(number)
                binding.tvFactorialResult.text = "$number!  =  $factorial"
            }
        }

        //Button Time:
        binding.btnTime.setOnClickListener {
            mainActivityViewModel.getTime()
        }

        //Floating Action Button Trash Bin:
        binding.fabTrashBin.setOnClickListener {
            mainActivityViewModel.deleteAllEvents()
        }

        //Edit-Text Enter Pressed:
        binding.editTextNumber.setOnKeyListener { _, keyCode, event ->
            if (keyCode == android.view.KeyEvent.KEYCODE_ENTER && event.action == android.view.KeyEvent.ACTION_UP) {
                binding.btnFactorial.performClick()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        //LiveData:
        mainActivityViewModel.currentTime.observe(this) { timeDTO ->
            Log.d("$_TAG - onStart", "currentTime changed: $timeDTO")
            //TODO: Agregar entrada al recyclerview
            Snackbar.make(binding.root, "Current DateTime: ${timeDTO.currentDateTime}", Snackbar.LENGTH_LONG).show()
        }

        mainActivityViewModel.errorMessage.observe(this) { errorMessage ->
            Log.d("$_TAG - onStart", "errorMessage changed: $errorMessage")
            Snackbar.make(binding.root, "Error: $errorMessage", Snackbar.LENGTH_INDEFINITE).show()
            binding.progressBar.visibility = android.view.View.GONE
            binding.btnTime.isEnabled = true
        }

        mainActivityViewModel.loading.observe(this) { loading ->
            Log.d("$_TAG - onStart", "loading changed: $loading")
            if (loading) {
                binding.progressBar.visibility = android.view.View.VISIBLE
                binding.btnTime.isEnabled = false
            } else {
                binding.progressBar.visibility = android.view.View.GONE
                binding.btnTime.isEnabled = true
            }
        }

        mainActivityViewModel.recViewContent.observe(this) { eventsList ->
            Log.d("$_TAG - onStart", "eventsList changed: $eventsList")
            if (eventsList.isNullOrEmpty()) return@observe
            recViewAdapter = EventsAdapter(
                eventsList = eventsList
            )
            binding.recview.setHasFixedSize(false)
            binding.recview.layoutManager = LinearLayoutManager(this)
            binding.recview.adapter = recViewAdapter
        }
    }

    override fun onPause() {
        super.onPause()

        //Lifecycle Log:
        Log.d("$_TAG - onPause", "Init")
        mainActivityViewModel.saveTimeStamp("${javaClass.simpleName} - ${Thread.currentThread().stackTrace[2].methodName}")
    }

    override fun onResume() {
        super.onResume()

        //Lifecycle Log:
        Log.d("$_TAG - onResume", "Init")
        mainActivityViewModel.saveTimeStamp("${javaClass.simpleName} - ${Thread.currentThread().stackTrace[2].methodName}")
    }

    override fun onRestart() {
        super.onRestart()

        //Lifecycle Log:
        Log.d("$_TAG - onRestart", "Init")
        mainActivityViewModel.saveTimeStamp("${javaClass.simpleName} - ${Thread.currentThread().stackTrace[2].methodName}")
    }

    override fun onStop() {
        super.onStop()

        //Lifecycle Log:
        Log.d("$_TAG - onStop", "Init")
        mainActivityViewModel.saveTimeStamp("${javaClass.simpleName} - ${Thread.currentThread().stackTrace[2].methodName}")
    }

    override fun onDestroy() {
        super.onDestroy()

        //Lifecycle Log:
        Log.d("$_TAG - onDestroy", "Init")
        mainActivityViewModel.saveTimeStamp("${javaClass.simpleName} - ${Thread.currentThread().stackTrace[2].methodName}")

        //Remove Observers:
        mainActivityViewModel.currentTime.removeObservers(this)
        mainActivityViewModel.errorMessage.removeObservers(this)
        mainActivityViewModel.loading.removeObservers(this)
        mainActivityViewModel.recViewContent.removeObservers(this)

        finish()
    }
}