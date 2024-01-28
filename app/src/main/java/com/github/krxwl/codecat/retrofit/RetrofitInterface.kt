package com.github.krxwl.codecat.retrofit

import android.database.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface RetrofitInterface {
    @POST("v1/execute")
    @JvmSuppressWildcards
    suspend fun executeProgram(@Body answerData: AnswerData): AnswerResult
}