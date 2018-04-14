package gr8.mrityunjay.whowroteitloader

import android.net.Uri
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class NetworkUtils{
    companion object {
        private val LOG_TAG = NetworkUtils::class.simpleName
        private val BOOK_BASE_URL = "https://www.googleapis.com/books/v1/volumes?" // Base URI for the Books API
        private val QUERY_PARAM = "q" // Parameter for the search string
        private val MAX_RESULTS = "maxResults" // Parameter that limits search results
        private val PRINT_TYPE = "printType"   // Parameter to filter by print type
        private val ERROR_RETURN = "error return"

        fun getBookInfo(queryString: String?): String{
            lateinit var urlConnection: HttpURLConnection
            lateinit var reader: BufferedReader
            lateinit var resultString: String

            try {
                val builtURI: Uri = Uri.parse(BOOK_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, queryString)
                        .appendQueryParameter(MAX_RESULTS, "10")
                        .appendQueryParameter(PRINT_TYPE, "books")
                        .build()

                val requestURL = URL(builtURI.toString())

                urlConnection = requestURL.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"
                urlConnection.connect()

                val inputStream = urlConnection.inputStream
                val buffer = StringBuffer()

                reader = BufferedReader(InputStreamReader(inputStream))

                while (true){
                    val line = reader.readLine() ?: break
                    buffer.append(line + '\n')
                }

                resultString = buffer.toString()
            } catch (e: Exception){
                e.printStackTrace()
            } finally {
                urlConnection.disconnect()
                reader.close()
                Log.d(LOG_TAG, resultString)
                return resultString
            }
        }

    }


}