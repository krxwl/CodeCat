package com.github.krxwl.codecat.retrofit

data class AnswerData(val clientId: String,
                 val clientSecret: String,
                 val script: String,
                 val stdin: String,
                 val language: String,
                 val versionIndex: String) {
}