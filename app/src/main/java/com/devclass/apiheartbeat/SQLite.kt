package com.devclass.apiheartbeat

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import java.util.HashMap

class SQLite:ContentProvider() {
    companion object{
        val PROVIDER_NAME = "com.devclass.apiheartbeat.SQLite";
        val URL = "content://"+ PROVIDER_NAME + "/servers";
        val CONTENT_URL = Uri.parse(URL);
        val _ID = "_id";
        val NAME = "name";
        val GRADE = "grade";
        private val SERVER_PROJECTION_MAP: HashMap<String, String>? = null;
        val STUDENTS = 1;
        val STUDENT_ID = 2;
        val uriMatcher : UriMatcher? = null;
        val DATABASE_NAME = "College";
        val STUDENT_TABLE_NAME = "students";
        val DATABSE_VERSION = 1;

        val CREATE_DB_TABLE = "CREATE TABLE " + STUDENT_TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, grade TEXT NOT NULL);"

    }

    override fun onCreate(): Boolean {
        TODO("Not yet implemented")
    }

    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {
        TODO("Not yet implemented")
    }

    override fun getType(p0: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

}


