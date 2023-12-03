package com.fcascan.challengepds.activities

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fcascan.challengepds.dto.TimeDTO
import com.fcascan.challengepds.repositories.MainRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel constructor(private val mainRepository: MainRepository) : ViewModel() {
    private val _TAG = "FCC#MainActivityViewModel"

    //LiveData for the Views:
    private val _currentTime = MutableLiveData<TimeDTO>()
    val currentTime: LiveData<TimeDTO> get() = _currentTime

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    //Job and Exception Handler:
    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    //Public Methods:
    fun saveTimeStamp() {
        //TODO
//        getTime()
    }

    fun getTime() {
        Log.d("$_TAG - getTime", "Init")
        _loading.postValue(true)
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = mainRepository.getTime()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Log.d("$_TAG - getTime", "Response: ${response.body()}")
                    updateTime(response.body()!!)
                } else {
                    Log.d("$_TAG - getTime", "Response: ${response.errorBody()}")
                    onError("Error: ${response.errorBody()}")
                }
            }
        }
    }

    fun myFactorial(number: Long): Long {
        var result = 1L
        for (i in 1L..number) result *= i
        return result
    }

    fun myRecursiveFactorial(number: Long): Long {
        if (number == 0L) return 1
        return number * myRecursiveFactorial(number - 1)
    }

    //Private Methods:
    private fun updateTime(time: TimeDTO) {
        _currentTime.postValue(time)
        _loading.postValue(false)
    }

    private fun onError(message: String) {
        _errorMessage.postValue(message)
        _loading.postValue(false)
    }

    //Lifecycle Methods:
    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}