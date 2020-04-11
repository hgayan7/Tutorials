package com.mercury.retrofitkotlintutorial.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.mercury.retrofitkotlintutorial.Utils.RetrofitRepository
import kotlinx.coroutines.Dispatchers

class MarsViewModel : ViewModel() {

    private val retrofitRepository : RetrofitRepository = RetrofitRepository()

    val data = liveData(Dispatchers.IO) {
        val marsData = retrofitRepository.getData()
        emit(marsData)
    }
}