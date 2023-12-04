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
            withContext(Dispatchers.Main) {
                if (allEvents != null) {
                    Log.d("$_TAG - refreshRecView", "getAllEvents(): $allEvents")
                    val recViewContent = mutableListOf<EventsAdapter.EventObject>()
                    allEvents.forEach {
                        recViewContent.add(EventsAdapter.EventObject(it!!.name, it.timeStamp))
                    }
                    addRecViewDummies(recViewContent, 0)
                    updateRecViewContent(recViewContent)
                } else {
                    Log.d("$_TAG - refreshRecView", "getAllEvents(): returned null or empty")
                }
            }
        }
    }
    fun saveTimeStamp(name: String) {
        Log.d("$_TAG - saveTimeStamp", "Init")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = mainRepository.getTime()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Log.d("$_TAG - saveTimeStamp", "Response: ${response.body()}")
                        val time = response.body()!!
                        val event = EventEntity(
                            0L,
                            name,
                            time.currentDateTime
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

    private suspend fun insertEvent(event: EventEntity) {
        withContext(Dispatchers.IO) {
            mainRepository.insertEvent(event)
        }
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

    fun deleteAllEvents() {
        Log.d("$_TAG - deleteAllEvents", "Init")
        job = CoroutineScope(Dispatchers.IO).launch {
            mainRepository.deleteAllEvents()
            withContext(Dispatchers.Main) {
                refreshRecView()    //TODO() - esta linea no funciona
            }
        }
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

    private fun updateRecViewContent(events: MutableList<EventsAdapter.EventObject>) {
        _recViewContent.postValue(events)
        _loading.postValue(false)
    }

    private fun addRecViewDummies(content: MutableList<EventsAdapter.EventObject>, count: Int) {
        for (i in 1..count)
            content.add(EventsAdapter.EventObject("", ""))
    }

    //Lifecycle Methods:
    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}