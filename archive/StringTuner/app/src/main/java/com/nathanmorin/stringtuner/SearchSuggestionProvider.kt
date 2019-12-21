package com.nathanmorin.stringtuner

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.app.SearchManager
import android.content.UriMatcher
import android.database.MatrixCursor
import android.provider.BaseColumns



class SearchSuggestionProvider : ContentProvider() {

    public val AUTHORITY = "com.nathanmorin.stringtuner.SearchSuggestionProvider"
    private val SEARCH_SUGGEST_COLUMNS = arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1, SearchManager.SUGGEST_COLUMN_INTENT_DATA)
    private var uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    private var SEARCH_SUGGEST = 1



    override fun query(uri: Uri?, p1: Array<out String>?, p2: String?, p3: Array<out String>?, p4: String?): Cursor {

        val query = uri?.lastPathSegment
        val instruments = getInstruments(context).filter { it.matchesSearch(query) }

        val mc = MatrixCursor(SEARCH_SUGGEST_COLUMNS,instruments.size)

        instruments.forEach{
            mc.addRow(arrayOf(it.id.toString(),it.name,it.id.toString()))
        }

        return mc
    }

    override fun onCreate(): Boolean {
        uriMatcher.addURI(AUTHORITY,SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST)
        uriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH_SUGGEST)
        return true
    }


    override fun getType(uri: Uri): String {
        when (uriMatcher.match(uri)) {
            SEARCH_SUGGEST -> return SearchManager.SUGGEST_MIME_TYPE
            else -> throw IllegalArgumentException("Unknown URL $uri")
        }
    }

    override fun insert(p0: Uri?, p1: ContentValues?): Uri {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(p0: Uri?, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(p0: Uri?, p1: String?, p2: Array<out String>?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}