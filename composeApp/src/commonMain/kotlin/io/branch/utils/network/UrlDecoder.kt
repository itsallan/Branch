package io.branch.utils.network

expect object UrlDecoder {
    fun decode(value: String, charset: String = "UTF-8"): String
}