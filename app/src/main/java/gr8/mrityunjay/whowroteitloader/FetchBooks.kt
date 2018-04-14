package gr8.mrityunjay.whowroteitloader

import android.os.AsyncTask
import android.widget.TextView
import org.json.JSONObject

class FetchBooks():AsyncTask<String, Void, String>() {
    private lateinit var mAuthorTextView: TextView
    private lateinit var mTitleTextView: TextView

    public constructor(authorTextView: TextView, titleTextView: TextView) : this() {
        this.mAuthorTextView = authorTextView
        this.mTitleTextView = titleTextView
    }

    override fun doInBackground(vararg params: String?): String {
        return NetworkUtils.getBookInfo(params[0])
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        try {
            val jsonObject = JSONObject(result)
            val itemsArray = jsonObject.getJSONArray("items")

            for (i in 0 until itemsArray.length()) {
                val book: JSONObject = itemsArray.getJSONObject(i)

                lateinit var title: String
                lateinit var author: String

                val volumeInfo: JSONObject = book.getJSONObject("volumeInfo")

                try {
                    title = volumeInfo.getString("title")
                    author = volumeInfo.getString("authors")
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                try {
                    mTitleTextView.text = title
                    mAuthorTextView.text = author
                    return
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            mTitleTextView.text = "No Results Found"
            mAuthorTextView.text = ""

        } catch (e: Exception){
            mTitleTextView.text = "No Results Found"
            mAuthorTextView.text = ""
            e.printStackTrace()
        }
    }
}