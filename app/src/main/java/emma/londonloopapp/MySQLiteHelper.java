package emma.londonloopapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emma on 30/04/2015.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "loop.db";

    // Table Names
    private static final String TABLE_NODE = "Node";
    private static final String TABLE_SECTION = "Section";
    private static final String TABLE_WALK = "Walk";

    // NODES Table - column names
    private static final String KEY_NODE_ID = "NodeId";
    private static final String KEY_NAME = "Name";
    private static final String KEY_LATITUDE = "Latitude";
    private static final String KEY_LONGITUDE = "Longitude";

    // SECTION Table - column names
    private static final String KEY_SECTION_ID = "SectionID";
    private static final String KEY_DESCRIPTION = "Description";
    private static final String KEY_LENGTH = "Length";
    private static final String KEY_IMAGE = "Image";
    private static final String KEY_START_NODE = "StartNode";
    private static final String KEY_END_NODE = "EndNode";

    // Table Create Statements
    // Node table create statement
    private static final String CREATE_TABLE_NODE = "CREATE TABLE "
            + TABLE_NODE + "(" + KEY_NODE_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME   + " TEXT," + KEY_LATITUDE + " REAL," + KEY_LONGITUDE
            + " REAL" + ")";

    // Tag table create statement
    private static final String CREATE_TABLE_SECTION = "CREATE TABLE " + TABLE_SECTION
            + "(" + KEY_SECTION_ID + " INTEGER PRIMARY KEY,"
            + KEY_START_NODE + " INTEGER,"
            + KEY_END_NODE + " INTEGER,"
            + KEY_DESCRIPTION + " TEXT,"
            + KEY_LENGTH + " REAL,"
            + KEY_IMAGE + " INTEGER" + ")";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_NODE);
        db.execSQL(CREATE_TABLE_SECTION);
    }

    public void clearDB(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NODE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SECTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NODE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SECTION);

        // create new tables
        onCreate(db);
    }

    //Creating a Node
    public void createNode(NodeItem nodeItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NODE_ID, nodeItem.getId());
        values.put(KEY_NAME, nodeItem.getName());
        values.put(KEY_LATITUDE, nodeItem.getLatitude());
        values.put(KEY_LONGITUDE, nodeItem.getLongitude());

        // insert row
        db.insert(TABLE_NODE, null, values);

    }

    public void createSection(SectionItem sectionItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SECTION_ID, sectionItem.getId());
        values.put(KEY_START_NODE, sectionItem.getStartNode().getId());
        values.put(KEY_END_NODE, sectionItem.getEndNode().getId());
        values.put(KEY_DESCRIPTION, sectionItem.getDescription());
        values.put(KEY_LENGTH, sectionItem.getMiles());
        values.put(KEY_IMAGE, sectionItem.getIcon());
        // insert row
        db.insert(TABLE_SECTION, null, values);

    }

    //Get a Node
    public NodeItem getNode(long node_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_NODE + " WHERE "
                + KEY_NODE_ID + " = " + node_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        NodeItem nodeItem = new NodeItem();
        nodeItem.setId(c.getLong(c.getColumnIndex(KEY_NODE_ID)));
        nodeItem.setName((c.getString(c.getColumnIndex(KEY_NAME))));
        nodeItem.setLatitude(c.getDouble(c.getColumnIndex(KEY_LATITUDE)));
        nodeItem.setLongitude(c.getDouble(c.getColumnIndex(KEY_LONGITUDE)));
        return nodeItem;
    }

    //Get a Section
    public SectionItem getSection(long section_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_SECTION + " WHERE "
                + KEY_SECTION_ID + " = " + section_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        SectionItem sectionItem = new SectionItem();
        sectionItem.setId(c.getLong(c.getColumnIndex(KEY_SECTION_ID)));
        sectionItem.setStartNode(getNode(c.getLong(c.getColumnIndex(KEY_START_NODE))));
        sectionItem.setEndNode(getNode(c.getLong(c.getColumnIndex(KEY_END_NODE))));
        sectionItem.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
        sectionItem.setMiles(c.getDouble(c.getColumnIndex(KEY_LENGTH)));
        sectionItem.setIcon(c.getInt((c.getColumnIndex(KEY_IMAGE))));
        return sectionItem;
    }

    //Get all Nodes
    public List<NodeItem> getAllNodes() {
        List<NodeItem> nodes = new ArrayList<NodeItem>();
        String selectQuery = "SELECT  * FROM " + TABLE_NODE;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                NodeItem nodeItem = new NodeItem();
                nodeItem.setId(c.getLong(c.getColumnIndex(KEY_NODE_ID)));
                nodeItem.setName((c.getString(c.getColumnIndex(KEY_NAME))));
                nodeItem.setLatitude(c.getDouble(c.getColumnIndex(KEY_LATITUDE)));
                nodeItem.setLongitude(c.getDouble(c.getColumnIndex(KEY_LONGITUDE)));

                // adding to node list
                nodes.add(nodeItem);
            } while (c.moveToNext());
        }

        return nodes;
    }

    //Get all Sections
    public List<SectionItem> getAllSections() {
        List<SectionItem> sections = new ArrayList<SectionItem>();
        String selectQuery = "SELECT  * FROM " + TABLE_SECTION;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                SectionItem sectionItem = new SectionItem();
                sectionItem.setId(c.getLong(c.getColumnIndex(KEY_SECTION_ID)));
                sectionItem.setStartNode(getNode(c.getLong(c.getColumnIndex(KEY_START_NODE))));
                sectionItem.setEndNode(getNode(c.getLong (c.getColumnIndex(KEY_END_NODE))));
                sectionItem.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
                sectionItem.setMiles(c.getDouble(c.getColumnIndex(KEY_LENGTH)));
                sectionItem.setIcon(c.getInt(c.getColumnIndex(KEY_IMAGE)));

                // adding to node list
                sections.add(sectionItem);
            } while (c.moveToNext());
        }

        return sections;
    }

    //Updating a node
    public int updateNode(NodeItem nodeItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, nodeItem.getName());
        values.put(KEY_LATITUDE, nodeItem.getLatitude());
        values.put(KEY_LONGITUDE, nodeItem.getLongitude());

        // updating row
        return db.update(TABLE_NODE, values, KEY_NODE_ID + " = ?",
                new String[] { String.valueOf(nodeItem.getId()) });
    }

    //Updating a node
    public int updateSection(SectionItem sectionItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_START_NODE, sectionItem.getStartNode().getId());
        values.put(KEY_END_NODE, sectionItem.getEndNode().getId());
        values.put(KEY_DESCRIPTION, sectionItem.getDescription());
        values.put(KEY_LENGTH, sectionItem.getMiles());
        values.put(KEY_IMAGE, sectionItem.getIcon());

        // updating row
        return db.update(TABLE_SECTION, values, KEY_SECTION_ID + " = ?",
                new String[] { String.valueOf(sectionItem.getId()) });
    }

    //Deleting a node
    public void deleteNode(long node_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NODE, KEY_NODE_ID + " = ?",
                new String[] { String.valueOf(node_id) });
    }

    //Deleting a section
    public void deleteSection(long section_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SECTION, KEY_SECTION_ID + " = ?",
                new String[] { String.valueOf(section_id) });
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
