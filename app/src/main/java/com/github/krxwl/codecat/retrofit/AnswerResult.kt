package com.github.krxwl.codecat.retrofit

data class AnswerResult(val output: String,
                   val statusCode: Int,
                   val memory: String,
                   val cpuTime: String,
                   val compilationStatus: Int?,
                   val projectKey: String?) {
}