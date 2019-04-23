package com.alidevs.instaapp.utils

interface AuthenticationListener {
    fun onTokenReceived(auth_token: String)
}