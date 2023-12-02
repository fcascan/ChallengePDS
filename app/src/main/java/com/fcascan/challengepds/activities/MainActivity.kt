package com.fcascan.challengepds.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fcascan.challengepds.R
import com.fcascan.challengepds.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val _TAG = "FCC#MainActivity"

    //View Elements:
    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //View Binding:
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }

}