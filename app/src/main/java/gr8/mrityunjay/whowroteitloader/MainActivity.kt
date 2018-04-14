package gr8.mrityunjay.whowroteitloader

import android.content.Context
import android.content.Loader
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import gr8.mrityunjay.whowroteitloader.BookLoader
import gr8.mrityunjay.whowroteitloader.R
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<String> {
    override fun onCreateLoader(id: Int, args: Bundle?): android.support.v4.content.Loader<String> {
        return BookLoader(this, args!!.getString(getString(R.string.query_string)))
    }

    override fun onLoadFinished(loader: android.support.v4.content.Loader<String>, data: String?) {
        try {
            val jsonObject = JSONObject(data)
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

    override fun onLoaderReset(loader: android.support.v4.content.Loader<String>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private lateinit var mEditText: EditText
    private lateinit var mAuthorTextView: TextView
    private lateinit var mTitleTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mEditText = bookInput
        mAuthorTextView = authorText
        mTitleTextView = titleText

        if (supportLoaderManager.getLoader<String>(0)!=null){
            supportLoaderManager.initLoader(0, null, this)
        }
    }



    fun searchBooks(v: View){
        val queryString = mEditText.text.toString()
        val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)


        val connMgr: ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo = connMgr.activeNetworkInfo

        val queryBundle = Bundle()
        queryBundle.putString(getString(R.string.query_string), queryString)

        if (networkInfo.isConnected and queryString.isNotEmpty()){
            mTitleTextView.text = getString(R.string.title_textview_loading)
            mAuthorTextView.text = ""
            supportLoaderManager.restartLoader(0, queryBundle, this)
        } else {
            if (queryString.isEmpty()){
                mAuthorTextView.text = ""
                mTitleTextView.text = getString(R.string.query_hint)
            } else {
                mAuthorTextView.text = ""
                mTitleTextView.text = getString(R.string.network_hint)
            }
        }
    }
}
