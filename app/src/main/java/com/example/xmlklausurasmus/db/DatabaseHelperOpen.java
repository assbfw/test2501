package com.example.xmlklausurasmus.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class DatabaseHelperOpen extends SQLiteOpenHelper {
    // TODO hier gehts mit dem DB-Handler weiter
    private final Context context;
    private SQLiteDatabase database;

    // database
    private static final String DB_NAME = "mydatabase.db";
    private static final int DB_VERSION = 1;
    private static String dbPath = "";

    //tables
    private static final String TABLE_USER = "tbl_user";

    // table USER
    private static final String USER_COLUMN_ID = "_id";
    private static final String USER_COLUMN_NAME = "username";
    private static final String USER_COLUMN_PW = "passwort";
    private static final String USER_COLUMN_ERSTELLTAM = "erstelltam";
    private static final String USER_COLUMN_AKTIV = "aktiv";

    public DatabaseHelperOpen(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        dbPath = context.getDatabasePath(DB_NAME).getPath();
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        database = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        database = db;
    }

    @Override
    public synchronized void close() {
        if (database != null)
            database.close();
        super.close();
    }

    //Open the database, so we can query it
    public boolean openDataBase() {
        String mPath = dbPath + DB_NAME;
        database = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return database != null;
    }

    public void createDataBase() {
        //If the database does not exist, copy it from the assets.
        boolean dbExists = checkDataBase();
        if (!dbExists) {
            this.getReadableDatabase();
            this.close();
            //Copy the database from assests
            copyDataBase();
        }
    }

    //Check that the database exists here: /data/data/your package/databases/DbName -  /data/user/0/your package/databases/
    private boolean checkDataBase() {
        return new File(dbPath).exists();
    }

    //Copy the database from assets
    private void copyDataBase() {
        FileOutputStream output = null;
        try {
            InputStream is = context.getAssets().open("databases/" + DB_NAME);
            output = new FileOutputStream(dbPath);
            byte[] mBuffer = new byte[1024];
            int mLength;
            while ((mLength = is.read(mBuffer)) > 0) {
                output.write(mBuffer, 0, mLength);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.flush();
                    output.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * write new user in database
     *
     * @param user new User
     * @return true|false
     */
    public boolean insertNewUser(User user) {

        SQLiteDatabase database = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(USER_COLUMN_NAME, user.getUsername());
        values.put(USER_COLUMN_PW, user.getPw());
        String dateTime = user.getErstelltAm().get(Calendar.DAY_OF_MONTH)
                + "-" + user.getErstelltAm().get(Calendar.MONTH)
                + "-" + user.getErstelltAm().get(Calendar.YEAR);
        values.put(USER_COLUMN_ERSTELLTAM, dateTime);
        values.put(USER_COLUMN_AKTIV, user.getAktiv());

        // Insert the new row and returning the primary key value of the new row
        long newRowId = database.insert(TABLE_USER, null, values);

        return newRowId != -1;
    }

    /**
     * update new user in database
     *
     * @param user new User
     * @return true|false
     */
    public boolean updateUser(User user) {
        SQLiteDatabase database = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(USER_COLUMN_NAME, user.getUsername());
        values.put(USER_COLUMN_PW, user.getPw());
        String dateTime = user.getErstelltAm().get(Calendar.DAY_OF_MONTH)
                + "-" + user.getErstelltAm().get(Calendar.MONTH)
                + "-" + user.getErstelltAm().get(Calendar.YEAR);
        values.put(USER_COLUMN_ERSTELLTAM, dateTime);
        values.put(USER_COLUMN_AKTIV, user.getAktiv());

        // Insert the new row and returning the primary key value of the new row
        long newRowId = database.update(TABLE_USER, values, "username = '" + user.getUsername() + "'", null);

        return newRowId != -1;
    }


    /**
     * delete user in database
     *
     * @param user delete User
     * @return true|false
     */
    public boolean deleteUser(User user) {
        SQLiteDatabase database = getWritableDatabase();

        // Insert the new row and returning the primary key value of the new row
        long newRowId = database.delete(TABLE_USER, "username=?", new String[]{user.getUsername()} );

        return newRowId != -1;
    }
    /**
     * get user by username
     *
     * @return object of user
     * @throws ParseException couldnt pass date and time
     */
    public User getUserByUsername(String username, String tabelle) throws ParseException {
                    this.database = getReadableDatabase();
                    User user = null;

                    String sql = "SELECT * FROM " + tabelle + " WHERE username = " + "'" + username +"'";
                    Cursor cursorUser = database.rawQuery(sql, null);

                    if (cursorUser.moveToFirst()) {
                        do {
                            //String userName = cursorUser.getString(1);
                            String pw = cursorUser.getString(2);
                            String dateTime = cursorUser.getString(3);
                            dateTime = dateTime.replace(".","-");
                            SimpleDateFormat sdf =
                                    new SimpleDateFormat("dd-MM-yyyy", Locale.GERMANY);
                sdf.parse(dateTime);
                Calendar cal = sdf.getCalendar();
                cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+1);
                int aktiv = cursorUser.getInt(4);

                user = new User(username, pw, cal, aktiv);
            } while (cursorUser.moveToNext());
        }

        cursorUser.close();
        return user;
    }


    /**
     * get all user entries from db
     *
     * @return list of all users
     * @throws ParseException couldnt pass date and time
     */
    public ArrayList<User> getAllUsers() throws ParseException {
        this.database = getReadableDatabase();
        ArrayList<User> userList = new ArrayList<>();

        String sql = "SELECT * FROM " + TABLE_USER;
        Cursor cursorUser = database.rawQuery(sql, null);

        if (cursorUser.moveToFirst()) {
            do {
                String username = cursorUser.getString(1);
                String pw = cursorUser.getString(2);
                String dateTime = cursorUser.getString(3);
                dateTime = dateTime.replace(".","-");
                SimpleDateFormat sdf =
                        new SimpleDateFormat("dd-MM-yyyy", Locale.GERMANY);
                sdf.parse(dateTime);
                Calendar cal = sdf.getCalendar();
                cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+1);
                int aktiv = cursorUser.getInt(4);
                User user = new User(username, pw, cal, aktiv);

                userList.add(user);

            } while (cursorUser.moveToNext());
        }

        cursorUser.close();
        return userList;
    }

    /**
     * delete all entries from shopping list
     * @return  true | false
     */
    public boolean deleteAllEntries() {
        long newRowId = database.delete(TABLE_USER, null, null );
        return newRowId != -1;
    }
}
