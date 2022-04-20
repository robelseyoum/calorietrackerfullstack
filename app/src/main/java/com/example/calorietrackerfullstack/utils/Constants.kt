package com.example.calorietrackerfullstack.utils
class Constants {

    companion object{

        const val BASE_URL = "https://3458-82-36-96-41.ngrok.io" //TODO need to change later //terminal - ngrok http 8080
        const val IMAGE_BASE_URL = "$BASE_URL/images/l/"

        const val TAG: String = "AppDebug"

        const val NETWORK_TIMEOUT = 6000L
        const val CACHE_TIMEOUT = 2000L
        const val TESTING_NETWORK_DELAY = 0L // fake network delay for testing
        const val TESTING_CACHE_DELAY = 0L // fake cache delay for testing

        const val GALLERY_REQUEST_CODE = 201
        const val PERMISSIONS_REQUEST_READ_STORAGE: Int = 301
        const val BASE_URL_RANDOM_USER = "https://randomuser.me/"
        const val ERROR_UNKNOWN = "Unknown error"
        const val NETWORK_ERROR= "IOException"
        const val NETWORK_ERROR_UNKNOWN = "Unknown network error"
        const val NETWORK_ERROR_TIMEOUT = "Network timeout"


        ///API com.example.calorietrackerfullstack.utils.Constants
        const val F_COLLECTION_USER = "Users"
        const val F_FIELD_EMAIL = "Email"
        const val F_FIELD_IS_ADMIN = "isAdmin"
        const val IS_ADMIN = "isAdmin"

    }
}