package com.devclass.apiheartbeat

import android.content.*
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import java.util.HashMap

class SQLite:ContentProvider() {
    companion object{

        val PROVIDER_NAME = "com.devclass.apiheartbeat.SQLite";
        val prov_URL = "content://"+ PROVIDER_NAME + "/";
        val CONTENT_URL = Uri.parse(prov_URL);

        val _ID = "_id";
        val NAME = "name";
        val METHOD = "method";
        val SCEHMA = "schema";
        val URL = "url";
        val PORT = "port";
        val ENDPOINT = "endpoint";

        private val SERVER_PROJECTION_MAP: HashMap<String, String>? = null;

        val SERVERS = 1;
        val SERVER_ID = 2;

        val DATABASE_NAME = "heartbeat";
        val SERVERS_TABLE_NAME = "servers";
        val DATABASE_VERSION = 1;
        val CREATE_DB_TABLE = "CREATE TABLE " + SERVERS_TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT,method TEXT NOT NULL,name TEXT NOT NULL,schema TEXT NOT NULL,url TEXT NOT NULL,port TEXT ,endpoint TEXT );";

        private var sUriMatcher = UriMatcher(UriMatcher.NO_MATCH);

        init{
            sUriMatcher.addURI(PROVIDER_NAME, "servers", SERVERS);
            sUriMatcher.addURI(PROVIDER_NAME, "server_id/#", SERVER_ID);
        }


        private var db: SQLiteDatabase? = null

        private class DatabaseHelper internal constructor(context: Context?) :
            SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
                override fun onCreate(db: SQLiteDatabase) {
                    db.execSQL(CREATE_DB_TABLE);
                }

                override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
                    db.execSQL("DROP TABLE IF EXISTS " + SERVERS_TABLE_NAME);
                    onCreate(db);
                }
        }
    }

    override fun onCreate(): Boolean {

        val context = context;
        val dbHelper = DatabaseHelper(context)
        db = dbHelper.writableDatabase;

        return db!=null;

    }

    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {
        println("####################### IN QUERY")
        val qb = SQLiteQueryBuilder();
        qb.tables = SERVERS_TABLE_NAME;

        println(p0);
        when (sUriMatcher.match(p0)) {
            SERVER_ID ->{
                qb.appendWhere(_ID + "=" + p0.pathSegments[1])
            }
            else -> {
                println("no")
            }
        }

        val sQLiteResolver = qb.query(db,null,p2,p3,null,null,null);
        sQLiteResolver.setNotificationUri(context!!.contentResolver, p0)

        return sQLiteResolver
    }

    override fun getType(p0: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        val last_row_id = db!!.insert(SERVERS_TABLE_NAME,"",p1);
        if(last_row_id>0){
            val _uri = ContentUris.withAppendedId(CONTENT_URL,last_row_id);
            context!!.contentResolver.notifyChange(_uri,null);
            return _uri;
        }
        throw SQLException("Failed to add record into ${p0}");
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        TODO("Not yet implemented")
    }



}


