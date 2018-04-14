package gr8.mrityunjay.whowroteitloader

import android.content.Context
import android.support.v4.content.AsyncTaskLoader

class BookLoader(context: Context): AsyncTaskLoader<String>(context) {

    private lateinit var mQueryString: String

    override fun onStartLoading() {
        super.onStartLoading()
        forceLoad()
    }

    constructor(context: Context, queryString: String) : this(context) {
        mQueryString = queryString
    }

    override fun loadInBackground(): String? {
        return NetworkUtils.getBookInfo(mQueryString)
    }
}