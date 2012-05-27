package eu.uberdust.mobileclient.datasource;

import java.util.ArrayList;
import java.util.List;

import eu.uberdust.mobileclient.model.Server;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
/**
 * SQLite database. Contains various preferences
 * @author Prevezanos Ioannis
 * @version 1.0
 *
 */
public class ServerDatabaseHandler extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "ServersManager";
 
    // Contacts table name
    private static final String TABLE_SERVERS = "servers";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_URL = "url";
    private static final String KEY_TESTBED = "testbed";
 
    public ServerDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_SERVERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_URL + " TEXT," + KEY_TESTBED + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVERS);
 
        // Create tables again
        onCreate(db);
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    // Adding new server
    public void addServer(Server server) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, server.getName()); // Server Name
        values.put(KEY_URL, server.getUrl()); // Server Url
 
        // Inserting Row
        db.insert(TABLE_SERVERS, null, values);
        db.close(); // Closing database connection
    }
    
    public void addServer(Server server, int testbed) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, server.getName()); // Server Name
        values.put(KEY_URL, server.getUrl()); // Server Url
        values.put(KEY_TESTBED, String.valueOf(testbed)); // Default Testbed for server
 
        // Inserting Row
        db.insert(TABLE_SERVERS, null, values);
        db.close(); // Closing database connection
    }
 
    // Getting single server
    public Server getServer(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_SERVERS, new String[] { KEY_ID,
                KEY_NAME, KEY_URL }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        Server server = new Server(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        cursor.close();
        // return server
        return server;
    }
 
    public int getDefaultTestbed(int id)
    {
    	SQLiteDatabase db = this.getReadableDatabase();
    
    	Cursor cursor = db.query(TABLE_SERVERS, new String[] { KEY_TESTBED }, KEY_ID + "=?",
            new String[] { String.valueOf(id) }, null, null, null, null);
    	if (cursor != null)
    		cursor.moveToFirst();

    	int testbed = Integer.parseInt(cursor.getString(0));
    	cursor.close();
    	return testbed;
    
    }
    public int setDefaultTestbed(int id, int testbed)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(KEY_TESTBED, Integer.toString(testbed));
       
        // updating row
        return db.update(TABLE_SERVERS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }
    
    // Getting All Servers
    public List<Server> getAllServers() {
        List<Server> serverList = new ArrayList<Server>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SERVERS;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Server server = new Server();
                server.setId(Integer.parseInt(cursor.getString(0)));
                server.setName(cursor.getString(1));
                server.setUrl(cursor.getString(2));
                // Adding server to list
                serverList.add(server);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return servers list
        return serverList;
    }
 
    // Updating single contact
    public int updateServer(Server server) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, server.getName());
        values.put(KEY_URL, server.getUrl());
 
        // updating row
        return db.update(TABLE_SERVERS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(server.getId()) });
    }
 
    // Deleting single server
    public void deleteServer(Server server) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SERVERS, KEY_ID + " = ?",
                new String[] { String.valueOf(server.getId()) });
        db.close();
    }
 
    // Getting servers Count
    public int getServersCount() {
        String countQuery = "SELECT  * FROM " + TABLE_SERVERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
 
        // return count
        return count;
    }
}