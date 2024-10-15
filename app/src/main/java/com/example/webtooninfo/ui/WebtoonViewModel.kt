package com.example.webtooninfo.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.webtooninfo.data.WebtoonData
import com.example.webtooninfo.data.WebtoonDatabase
import com.example.webtooninfo.data.WebtoonLocal
import com.example.webtooninfo.data.toLocal
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WebtoonViewModel(application: Application): AndroidViewModel(application){
    private lateinit var progressJob: Job
    private val db = WebtoonDatabase.getDatabase(getApplication())
    private val webtoonDao = db.webtoonDao()

    private val _fetchLocal = MutableStateFlow(false)
    val fetchLocal: MutableStateFlow<Boolean> get() = _fetchLocal
    fun setFetchLocal(fetchLocal: Boolean){
        _fetchLocal.value = fetchLocal
    }
    fun fetchWebtoon() {
        viewModelScope.launch {
            _favoriteWebtoons.value = webtoonDao.getWebtoon()
        }
    }
    fun checkFavorite(webtoonData: WebtoonData){
        viewModelScope.launch {
            val webtoon = webtoonDao.getWebtoonByTitle(webtoonData.title)
            if(webtoon != null){
                _fetchLocal.value = true
                setChosenWebtoonLocal(webtoon)
                Log.e("isFavorite","Title: ${_chosenWebtoonLocal.value?.author}")
            }
            else{
                _fetchLocal.value = false
                setChosenWebtoon(webtoonData)
                Log.e("isNotFavorite","Title: ${_chosenWebtoon.value?.author}")
            }
        }
    }
    fun saveWebtoon(data: WebtoonData){
        viewModelScope.launch {
            val webtoonLocal = data.toLocal()
            webtoonDao.insertWebtoon(webtoonLocal)
        }
    }

    fun deleteWebtoon(data: WebtoonLocal){
        viewModelScope.launch {
            webtoonDao.deleteWebtoon(data)
        }
    }
    private val _progress = MutableStateFlow(0.00f)
    val progress: MutableStateFlow<Float> get() = _progress

    private val _showDialog = MutableStateFlow(false)
    val showDialog: MutableStateFlow<Boolean> get() = _showDialog
    fun setDialogVisible(showDialog: Boolean){
        _showDialog.value = showDialog
    }

    fun saveRating(title: String, newRating: Float) {
        val myRef = database.getReference("webtoons")

        myRef.orderByChild("title").equalTo(title).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (webtoonSnapshot in dataSnapshot.children) {
                    webtoonSnapshot.ref.child("rating").setValue(newRating)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("FirebaseError", databaseError.message)
            }
        })
    }


    private val _chosenWebtoon = MutableStateFlow<WebtoonData?>(null)
    val chosenWebtoon: MutableStateFlow<WebtoonData?> get() = _chosenWebtoon
    fun setChosenWebtoon(webtoon: WebtoonData){
        _chosenWebtoon.value = webtoon
    }

    private val _chosenWebtoonLocal = MutableStateFlow<WebtoonLocal?>(null)
    val chosenWebtoonLocal: MutableStateFlow<WebtoonLocal?> get() = _chosenWebtoonLocal
    fun setChosenWebtoonLocal(webtoon: WebtoonLocal){
        _chosenWebtoonLocal.value = webtoon
    }

    private val _isLoaded = MutableStateFlow(false)
    val isLoaded: MutableStateFlow<Boolean> get() = _isLoaded
    fun setLoaded(isLoaded: Boolean){
        _isLoaded.value = isLoaded
    }
    private val _options = MutableStateFlow<List<String>>(mutableListOf())
    val options: MutableStateFlow<List<String>> get() = _options

    private val _favoriteWebtoons = MutableStateFlow<List<WebtoonLocal>>(mutableListOf())
    val favoriteWebtoons: MutableStateFlow<List<WebtoonLocal>> get() = _favoriteWebtoons

    private val _webtoonsDownloaded = MutableStateFlow<List<WebtoonData>>(mutableListOf())
    val webtoonsDownloaded: MutableStateFlow<List<WebtoonData>> get() = _webtoonsDownloaded
    fun addPlace(webtoon: WebtoonData){
        val currentWebtoons = _webtoonsDownloaded.value.toMutableList()
        currentWebtoons.add(webtoon)
        _webtoonsDownloaded.value = currentWebtoons.toList()
    }

    private val database = Firebase.database("https://webtoon-info-default-rtdb.firebaseio.com/")

    private fun getDataFromFirebase(){
        startProgress()
        val myRef = database.getReference("webtoons")
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                _webtoonsDownloaded.value = emptyList()
                for (webtoonSnapshot in dataSnapshot.children) {
                    val webtoon = webtoonSnapshot.getValue(WebtoonData::class.java)
                    webtoon?.let { addPlace(it) }
                }
                options.value = webtoonsDownloaded.value.map { it.title }
                setLoaded(true)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    fun startProgress(){
        progressJob = viewModelScope.launch {
            while(_progress.value <=1.00f){
                _progress.value += 0.01f
                delay(10)
            }
        }
    }

    init {
        getDataFromFirebase()
    }
}