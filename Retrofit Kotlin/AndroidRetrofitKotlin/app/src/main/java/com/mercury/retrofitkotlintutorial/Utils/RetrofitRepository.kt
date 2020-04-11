package com.mercury.retrofitkotlintutorial.Utils

class RetrofitRepository {
    var retrofitClient : RetrofitService = RetrofitClient().retrofitService
    suspend fun getData() = retrofitClient.getMarsData()
}
