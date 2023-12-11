package com.fcascan.challengepds.activities

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fcascan.challengepds.adapters.EventsAdapter
import com.fcascan.challengepds.dto.TimeDTO
import com.fcascan.challengepds.entities.EventEntity
import com.fcascan.challengepds.repositories.MainRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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

    private val _recViewContent = MutableLiveData<MutableList<EventsAdapter.EventObject>?>()
    val recViewContent: LiveData<MutableList<EventsAdapter.EventObject>?> get() = _recViewContent


    //Job and Exception Handler:
    private var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }


    //Public Methods:
    fun refreshRecView() {
        Log.d("$_TAG - refreshRecView", "Init")
        job = CoroutineScope(Dispatchers.IO).launch {
            val allEvents =  mainRepository.getAllEvents()
            Log.d("$_TAG - refreshRecView", "getAllEvents(): $allEvents")
            val recViewContent = mutableListOf<EventsAdapter.EventObject>()
            if (!allEvents.isNullOrEmpty()) {
                allEvents.forEach {
                    recViewContent.add(EventsAdapter.EventObject(it!!.name, it.timeStamp))
                }
            }
            updateRecViewContent(recViewContent)
        }
    }
    fun saveTimeStamp(name: String) {
        Log.d("$_TAG - saveTimeStamp", "Init")
        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = mainRepository.getTime()
                withContext(Dispatchers.IO) {
                    if (response.isSuccessful) {
                        Log.d("$_TAG - saveTimeStamp", "Response: ${response.body()}")
                        val time = response.body()!!
                        val event = EventEntity(
                            0L,
                            name,
                            time.dateTime
                        )
                        insertEvent(event)
                    } else {
                        Log.d("$_TAG - saveTimeStamp", "Response: ${response.errorBody()}")
                        onError("Error: ${response.errorBody()}")
                    }
                }
            } catch (e: Exception) {
                Log.e("$_TAG - saveTimeStamp", "Error: $e")
                onError("Error: $e")
            }
        }
    }

    fun getCurrentTime() {
        Log.d("$_TAG - getCurrentTime", "Init")
        _loading.postValue(true)
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = mainRepository.getTime()
            if (response.isSuccessful && response.body() != null) {
                Log.d("$_TAG - getCurrentTime", "Response: ${response.body()}")
                updateTime(response.body()!!)
            } else {
                Log.d("$_TAG - getCurrentTime", "Response: ${response.errorBody()}")
                onError("Error: ${response.errorBody()}")
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

    fun deleteButtonClicked() {
        Log.d("$_TAG - deleteButtonClicked", "Trash Can Clicked")
        job = CoroutineScope(Dispatchers.IO).launch {
            deleteAllEvents()
        }
    }


    //Private Methods:
    private fun insertEvent(event: EventEntity) {
        Log.d("$_TAG - insertEvent", "Init")
        mainRepository.insertEvent(event)
        refreshRecView()
    }

    private suspend fun deleteAllEvents() {
        Log.d("$_TAG - deleteAllEvents", "Init")
        mainRepository.deleteAllEvents()
        refreshRecView()
    }

    private fun updateTime(time: TimeDTO) {
        _currentTime.postValue(time)
        _loading.postValue(false)
    }

    private fun onError(message: String) {
        _errorMessage.postValue(message)
        _loading.postValue(false)
    }

    private fun updateRecViewContent(events: MutableList<EventsAdapter.EventObject>) {
        _recViewContent.postValue(events)
        _loading.postValue(false)
    }


    //Lifecycle Methods:
    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}