package sds.com.adx;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.text.format.DateFormat;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {
	final String TAG = "DatabaseHandler";
	// Database Version
	private static final int DATABASE_VERSION = 945;

	private final int ADX_THRESHOLD = 30;

	// Database Name
	private static final String DATABASE_NAME = "quotesManager";    

	// Quotes table name 
	private static final String TABLE_TICKS = "ticks";
	private static final String TABLE_QUOTES = "quotes";
	private static final String TABLE_INDEXES = "indexes";
	private static final String TABLE_POSITIONS = "positions";
	private static final String TABLE_POTENTIALS = "potentials"; 
	private static final String TABLE_CLOSED_HIST = "close_hist";
	private static final String TABLE_LASTPOTUPDATE = "last_pot_update";
	private static final String TABLE_LASTQUOTEUPDATE = "last_quote_update";
	private static final String TABLE_RAW_QUOTES = "raw_quotes";
	private static final String TABLE_RAW_STREAM = "raw_stream";

	private static final String KEY_ID = "id";
	private static final String KEY_DATE = "date";
	private static final String KEY_STARTID = "startid";
	private static final String KEY_SYMBOL = "symbol";
	private static final String KEY_DIRECTION = "direction";
	private static final String KEY_LOW = "low";
	private static final String KEY_HIGH = "high";
	private static final String KEY_CLOSE = "close";
	private static final String KEY_ADX = "adx";
	private static final String KEY_TOP = "top";
	private static final String KEY_BOTTOM = "bottom";
	private static final String KEY_CURRENT = "current";
	private static final String KEY_TR14 = "tr14";
	private static final String KEY_DM14_PLUS = "dm14plus";
	private static final String KEY_DM14_MINUS = "dm14minus";
	private static final String KEY_DI14_PLUS = "di14plus";
	private static final String KEY_DI14_MINUS = "di14minus";
	private static final String KEY_STOP = "stop";
	private static final String KEY_LIMIT = "limiter";
	private static final String KEY_PPP = "ppp";
	private static final String KEY_PROFIT = "profit";
//	private static final String KEY_PULLBACK = "pullback";
	private static final String KEY_TRIGGER = "trigger";
	private static final String KEY_DAYSACTIVE = "daysactive";
	private static final String KEY_TICK = "tick";
	private static final String KEY_INDEX = "indexName";
	private static final String KEY_STATUS = "status";
//	private static final String KEY_LAST_UPDATED = "last_updated";
	private static final String KEY_12_HIGH = "twelve_high";
	private static final String KEY_12_LOW = "twelve_low";
	private static final String KEY_HOT = "hot";
	private static final String KEY_TRIGGERED = "triggered";
	private static final String KEY_DATE_CLOSED = "closed_date";
    private static final String KEY_DATE_OPENED = "opened_date";

    private static final String KEY_CLOSED = "closed";
	private static final String KEY_OPENED = "opened";

	private static final String KEY_STREAM_DATA = "stream_data";

	private final int ID = 0;
	private final int SYMBOL = 1;
	private final int DATE = 2;
	private final int HIGH = 3;
	private final int BOTTOM = 3;
	private final int LOW = 4;
	private final int TOP = 4;
	private final int CLOSE = 5;
	private final int ADX = 6;
	private final int TR14 = 7;
	private final int DM14_PLUS = 8;
	private final int DM14_MINUS = 9;
	private final int DI14_PLUS = 10;
	private final int DI14_MINUS = 11;
	
	private final int TICK = 1;
	private final int INDEX = 2;
	private final int LAST_CLOSE_DATE = 3;

	private ArrayList<String> cartList = new ArrayList<String>();

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {

		String CREATE_LASTPOTUPDATE_TABLE = "CREATE TABLE " + TABLE_LASTPOTUPDATE + "("
				+ KEY_DATE + "  TEXT" + ")";
		db.execSQL(CREATE_LASTPOTUPDATE_TABLE);
//		Log.d("DB", CREATE_LASTPOTUPDATE_TABLE);
		
		String CREATE_LASTQUOTEUPDATE_TABLE = "CREATE TABLE " + TABLE_LASTQUOTEUPDATE + "("
				+ KEY_DATE + "  TEXT" + ")";
		db.execSQL(CREATE_LASTQUOTEUPDATE_TABLE);
//		Log.d("DB", CREATE_LASTQUOTEUPDATE_TABLE);
		
		String CREATE_TICKS_TABLE = "CREATE TABLE " + TABLE_TICKS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TICK + " TEXT,"
				+ KEY_INDEX + " TEXT," + KEY_12_HIGH + " FLOAT," + KEY_12_LOW + " FLOAT" + ")";
		db.execSQL(CREATE_TICKS_TABLE);
		Log.d("DB", CREATE_TICKS_TABLE);

		String CREATE_QUOTES_TABLE = "CREATE TABLE " + TABLE_QUOTES + "("
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_SYMBOL + " TEXT,"
				+ KEY_DATE + " DATETIME," + KEY_HIGH + " FLOAT," + KEY_LOW
				+ " FLOAT," + KEY_CLOSE + " FLOAT," + KEY_ADX + " FLOAT,"
				+ KEY_TR14 + " FLOAT, " + KEY_DM14_PLUS + " FLOAT, "
				+ KEY_DM14_MINUS + " FLOAT, " + KEY_DI14_PLUS + " FLOAT, "
//				+ KEY_DI14_MINUS + " FLOAT" + " ,PRIMARY KEY (symbol, DATE))"; 
				+ KEY_DI14_MINUS + " FLOAT" + ")";

		db.execSQL(CREATE_QUOTES_TABLE);

		String CREATE_QUOTES_INDEX = "CREATE INDEX QUOTES_INDEX ON QUOTES (symbol)";
		db.execSQL(CREATE_QUOTES_INDEX);
		
		String CREATE_QUOTES_INDEX_DATE = "CREATE INDEX QUOTES_INDEX_DATE ON QUOTES (date)";
		db.execSQL(CREATE_QUOTES_INDEX_DATE);
		
		String CREATE_QUOTES_INDEX_ID = "CREATE INDEX QUOTES_INDEX_ID ON QUOTES (id)";
		db.execSQL(CREATE_QUOTES_INDEX_ID);
		
		String CREATE_QUOTES_INDEX_DATE_2 = "CREATE INDEX QUOTES_INDEX_DATE_2 ON QUOTES (date,symbol)";
		db.execSQL(CREATE_QUOTES_INDEX_DATE_2);
		
		String CREATE_TICKS_INDEX = "CREATE INDEX TICKS_INDEX ON QUOTES (symbol)";
		db.execSQL(CREATE_TICKS_INDEX);

		db.execSQL("CREATE TABLE if not exists checks(id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ KEY_DATE + " TEXT)");

		String CREATE_POSITIONS_TABLE = "CREATE TABLE " + TABLE_POSITIONS
				+ "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_SYMBOL + " TEXT," + KEY_DIRECTION + " TEXT,"
				+ KEY_STOP + " FLOAT," + KEY_LIMIT + " FLOAT," + KEY_PPP + " INTEGER," 
				+ KEY_CURRENT + " FLOAT," + KEY_PROFIT + " INTEGER, " + KEY_STATUS + " TEXT," 
				+ KEY_TRIGGERED + " FLOAT,"
				+ KEY_DATE_OPENED + " TEXT,"
				+ KEY_DAYSACTIVE + " INTEGER" + ")";
//		Log.d(TAG, CREATE_POSITIONS_TABLE);
		db.execSQL(CREATE_POSITIONS_TABLE);
		
//		String CREATE_POTENTIALS_TABLE = "CREATE TABLE " + TABLE_POTENTIALS
//				+ "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_SYMBOL
//				+ " TEXT," + KEY_DIRECTION + " TEXT," + KEY_STARTID + " INTEGER," + KEY_CURRENT + " INTEGER," + KEY_TRIGGER + " INTEGER," 
//			    + " INTEGER," + KEY_DAYSACTIVE + " INTEGER" + ")";
//		db.execSQL(CREATE_POTENTIALS_TABLE);
		
		String CREATE_POTENTIALS_TABLE = "CREATE TABLE " + TABLE_POTENTIALS
		+ "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
		+ KEY_SYMBOL + " TEXT," 
		+ KEY_DIRECTION + " TEXT," 
		+ KEY_STARTID + " INTEGER," 
		+ KEY_CURRENT + " FLOAT," 
		+ KEY_TRIGGER + " FLOAT," 
	    + KEY_DAYSACTIVE + " INTEGER," 
		+ KEY_HOT + " INTEGER,"
		+ KEY_HIGH + " FLOAT,"
		+ KEY_STOP + " FLOAT,"
		+ KEY_LIMIT + " FLOAT,"
		+ KEY_PPP + " FLOAT,"
		+ KEY_LOW + " FLOAT" + ")";
		Log.d(TAG,CREATE_POTENTIALS_TABLE);
		db.execSQL(CREATE_POTENTIALS_TABLE);

		
		String CREATE_INDEXES_TABLE = "CREATE TABLE " + TABLE_INDEXES
				+ "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_INDEX
				+ " TEXT," + KEY_DATE + " DATETIME" + ")";
		db.execSQL(CREATE_INDEXES_TABLE);
		
		String CREATE_CLOSED_HIST_TABLE = "CREATE TABLE " + TABLE_CLOSED_HIST
				+ "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_SYMBOL + " TEXT,"
                + KEY_DIRECTION + " TEXT,"
				+ KEY_PROFIT + " INTEGER, "
                + KEY_TRIGGERED + " FLOAT,"
                + KEY_DATE_OPENED + " TEXT,"
                + KEY_DATE_CLOSED + " TEXT" + ")";

//		Log.d(TAG,CREATE_CLOSED_HIST_TABLE);
		db.execSQL(CREATE_CLOSED_HIST_TABLE);

		String CREATE_RAW_QUOTES_TABLE = "CREATE TABLE " + TABLE_RAW_QUOTES + "("
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_SYMBOL + " TEXT,"
				+ KEY_DATE + " DATETIME," + KEY_HIGH + " FLOAT," + KEY_LOW
				+ " FLOAT," + KEY_CLOSE + " FLOAT" + ")";

		db.execSQL(CREATE_RAW_QUOTES_TABLE);

		String CREATE_RAW_STREAM_TABLE = "CREATE TABLE " + TABLE_RAW_STREAM + "("
				+ KEY_SYMBOL + " TEXT, " + KEY_DATE + " TEXT, " + KEY_STREAM_DATA + "  TEXT" + ")";
		db.execSQL(CREATE_RAW_STREAM_TABLE);

 		//db.close();
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUOTES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_INDEXES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSITIONS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_POTENTIALS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLOSED_HIST);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LASTPOTUPDATE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LASTQUOTEUPDATE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RAW_QUOTES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RAW_STREAM);



//		Log.d(TAG,"DROPPING ALL TABLES");
		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */
	void addTick(Tick tick) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TICK, tick.getTick()); // tick Name
		values.put(KEY_INDEX, tick.getIndex());
//		values.put(KEY_LAST_UPDATED, tick.getLastCloseDate());
		values.put(KEY_12_HIGH, tick.getTwoMonthHigh());
		values.put(KEY_12_LOW, tick.getTwoMonthLow());

		try {
		       db.beginTransaction();
		       db.insert(TABLE_TICKS, null, values);
		       db.setTransactionSuccessful();
		       }
		   finally {
		       db.endTransaction();
				//db.close();
		       }
	}

	Tick getTick(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

//		String CREATE_TICKS_TABLE = "CREATE TABLE " + TABLE_TICKS + "("
//				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_TICK + " TEXT,"
//				+ KEY_INDEX + " TEXT," + KEY_LAST_CLOSE_DATE + " DATETIME" + ")";

		Cursor cursor = db.query(TABLE_TICKS, new String[] { KEY_ID,
				KEY_TICK, KEY_INDEX, KEY_12_HIGH, KEY_12_LOW }, KEY_ID
				+ "=?", new String[] { String.valueOf(id) }, null, null, null,
				null);
		if (cursor != null)
			cursor.moveToFirst();

		final int ID = 0;
		final int TICK = 1;
		final int INDEX = 2;
//		final int LAST_UPDATED = 3;
		final int HIGH = 3;
		final int LOW = 4;


		Tick tick = new Tick(Integer.parseInt(cursor
				.getString(ID)), cursor.getString(TICK), cursor.getString(INDEX), cursor.getDouble(HIGH),cursor.getDouble(LOW));

		cursor.close();
		 //db.close();

		return tick;
	}

	Tick getTick(String symbol) {
		SQLiteDatabase db = this.getReadableDatabase();

//		String CREATE_TICKS_TABLE = "CREATE TABLE " + TABLE_TICKS + "("
//				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_TICK + " TEXT,"
//				+ KEY_INDEX + " TEXT," + KEY_LAST_CLOSE_DATE + " DATETIME" + ")";

		Cursor cursor = db.query(TABLE_TICKS, new String[] { KEY_ID,
				KEY_TICK, KEY_INDEX, KEY_12_HIGH, KEY_12_LOW }, KEY_TICK
				+ "=?", new String[] { String.valueOf(symbol) }, null, null, null,
				null);
		if (cursor != null)
			cursor.moveToFirst();

		final int ID = 0;
		final int TICK = 1;
		final int INDEX = 2;
//		final int LAST_UPDATED = 3;
		final int HIGH = 3;
		final int LOW = 4;


		Tick tick = new Tick(Integer.parseInt(cursor
				.getString(ID)), cursor.getString(TICK), cursor.getString(INDEX),cursor.getDouble(HIGH),cursor.getDouble(LOW));

		cursor.close();
		//db.close();

		return tick;
	}


	// Getting All Ticks
	public ArrayList<Tick> getAllTicks() {
		ArrayList<Tick> tickList = new ArrayList<Tick>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_TICKS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		final int ID = 0;
		final int TICK = 1;
		final int INDEX = 2;
//		final int LAST_UPDATED = 3;
		final int HIGH = 3;
		final int LOW = 4;

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Tick tick = new Tick(0, "", "",0,0);
//				tick.setID(Integer.parseInt(cursor.getString(ID)));
				tick.setId(cursor.getInt(ID));
				tick.setTick(cursor.getString(TICK));
				tick.setIndex(cursor.getString(INDEX));
//				tick.setLastCloseDate(cursor.getString(LAST_UPDATED));
				tick.setTwelveMonthHigh(cursor.getInt(HIGH));
				tick.setTwelveMonthLow(cursor.getInt(LOW));


				tickList.add(tick);
			} while (cursor.moveToNext());
		}
		cursor.close();
		 //db.close();

		return tickList;
	}

	public void updateTick(Tick tick) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

//		Log.d(TAG,"About to update High with: " + tick.getTwoMonthHigh());

		values.put(KEY_TICK, tick.getTick());
		//values.put(KEY_INDEX,  tick.getIndex());
//		values.put(KEY_LAST_UPDATED, tick.getLastCloseDate());
		values.put(KEY_12_HIGH, tick.getTwoMonthHigh());
		values.put(KEY_12_LOW, tick.getTwoMonthLow());

		try {
		       db.beginTransaction();
		       db.update(TABLE_TICKS, values, KEY_TICK + " = ?",new String[] { String.valueOf(tick.getTick() ) });
		       db.setTransactionSuccessful();
		       }
		   finally {
		       db.endTransaction();
			//db.close();

		}
	}

	// Deleting single tick
	public void deleteTick(Tick tick) {
		SQLiteDatabase db = this.getWritableDatabase();

		db.delete(TABLE_POTENTIALS, KEY_SYMBOL + " = ?",
				new String[] { String.valueOf(tick.getTick()) });
        //db.close();
	}

	void addPotential(Potential potential) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_SYMBOL, potential.getSymbol()); // potential Name
		values.put(KEY_STARTID, potential.getStartId());
		values.put(KEY_DIRECTION,  potential.getDirection());
		values.put(KEY_CURRENT, potential.getCurrent());
		values.put(KEY_TRIGGER, potential.getTrigger()); // potential Phone
		values.put(KEY_DAYSACTIVE, potential.getDaysActive());
		values.put(KEY_HIGH, potential.getHigh());

		values.put(KEY_STOP, potential.getStop());
		values.put(KEY_LIMIT, potential.getLimit());
		values.put(KEY_PPP, potential.getPpp());
		values.put(KEY_LOW, potential.getLow());



		try {
		       db.beginTransaction();
		       db.insert(TABLE_POTENTIALS, null, values);
		       db.setTransactionSuccessful();
		       }
		   finally {
		       db.endTransaction();
		       }

	}


	// Getting All Potentials
	public List<Potential> getAllPotentials() {
		List<Potential> potentialList = new ArrayList<Potential>();
		String selectQuery = "SELECT  * FROM " + TABLE_POTENTIALS + " ORDER BY DAYSACTIVE DESC";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		final int ID = 0;
		final int SYMBOL = 1;
		final int DIRECTION = 2;
		final int STARTID = 3;
		final int CURRENT = 4;
		final int TRIGGER = 5;
		final int DAYSACTIVE= 6;
		final int HOT = 7;
		final int HIGH = 8;
		final int STOP = 9;
		final int LIMIT = 10;
		final int PPP = 11;
		final int LOW = 12;


		if (cursor.moveToFirst()) {
			do {
				Potential potential = new Potential(0, "", "", 0, 0, 0, 0, 0, 0, 0, 0, 0,0);
				// potential.setID(Integer.parseInt(cursor.getString(ID)));
				potential.setId(cursor.getInt(ID));
				potential.setSymbol(cursor.getString(SYMBOL));
				potential.setDirection(cursor.getString(DIRECTION));
				potential.setStartId(cursor.getInt(STARTID));
				potential.setCurrent(cursor.getDouble(CURRENT));
				potential.setTrigger(cursor.getDouble(TRIGGER));
				potential.setDaysActive(cursor.getInt(DAYSACTIVE));
				potential.setHot(cursor.getInt(HOT));
				potential.setHigh(cursor.getDouble(HIGH));
				potential.setStop(cursor.getDouble(STOP));
				potential.setLimit(cursor.getDouble(LIMIT));
				potential.setPpp(cursor.getDouble(PPP));
				potential.setLow(cursor.getDouble(LOW));
				potentialList.add(potential);
			} while (cursor.moveToNext());
		}
		cursor.close();
 //db.close();

		return potentialList;
	}

	// Updating single potential
	public void updatePotential(Potential potential) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(KEY_CURRENT, potential.getCurrent());
		values.put(KEY_TRIGGER,  potential.getTrigger());
		values.put(KEY_DIRECTION, potential.getDirection());
		values.put(KEY_DAYSACTIVE, potential.getDaysActive());
		values.put(KEY_HOT, potential.getHot());
		values.put(KEY_STOP, potential.getStop());
		values.put(KEY_LIMIT, potential.getLimit());
		values.put(KEY_PPP, potential.getPpp());




		try {
			db.beginTransaction();
			db.update(TABLE_POTENTIALS, values, KEY_SYMBOL + " = ?",new String[] { String.valueOf(potential.getSymbol() ) });

			db.setTransactionSuccessful();
//			Log.d(TAG,"Updated potential with stop: " + potential.getSymbol() + " and " + potential.getStop());
//			showAllPotentials();
		}
		finally {
			db.endTransaction();
			//db.close();

		}

//		db.update(TABLE_POTENTIALS, values, KEY_SYMBOL + " = ?",new String[] { String.valueOf(potential.getSymbol() ) });


	}

	public void deleteAllPotential() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from " + TABLE_POTENTIALS);

		//db.close();

	}

	public void deletePotential(Potential potential) {
		SQLiteDatabase db = this.getWritableDatabase();

		db.delete(TABLE_POTENTIALS, KEY_SYMBOL + " = ?",
				new String[]{String.valueOf(potential.getSymbol())});

        //db.close();

	}

	void addPosition(Position position) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_SYMBOL, position.getSymbol()); // position Name
		values.put(KEY_DIRECTION,  position.getDirection());
		values.put(KEY_STOP, position.getStop());
		values.put(KEY_LIMIT, position.getLimit()); // position Phone
		values.put(KEY_PPP, position.getPPP()); // position Phone
		values.put(KEY_CURRENT, position.getCurrent());
		values.put(KEY_PROFIT, position.getProfit());
		values.put(KEY_STATUS, position.getStatus());
		values.put(KEY_TRIGGERED, position.getTriggered());
        values.put(KEY_DATE_OPENED, position.getDateOpened());
		values.put(KEY_DAYSACTIVE, position.getDaysActive());

		db.insert(TABLE_POSITIONS, null, values);
 //db.close(); // Closing database connection
	}

	public int getPositionsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_POSITIONS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();
		 //db.close();
		// return count
		return count;
	}
	// Getting single candidate
	Position getPosition(int id) {

		Position position = null;
		SQLiteDatabase db = this.getReadableDatabase();

//		String CREATE_POSITIONS_TABLE = "CREATE TABLE " + TABLE_POSITIONS
//				+ "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_SYMBOL + " TEXT," + KEY_DIRECTION + " TEXT,"
//				+ KEY_STOP + " INTEGER," + KEY_LIMIT + " INTEGER," + KEY_PPP + " INTEGER,"
//				+ KEY_CURRENT + " INTEGER," + KEY_PROFIT + " INTEGER, " + KEY_STATUS + " TEXT" + ")";

		Cursor cursor = db.query(TABLE_POSITIONS, new String[] { KEY_ID,
				KEY_SYMBOL, KEY_DIRECTION, KEY_STOP, KEY_LIMIT, KEY_PPP, KEY_CURRENT, KEY_PROFIT, KEY_STATUS, KEY_TRIGGERED, KEY_DAYSACTIVE }, KEY_ID
				+ "=?", new String[] { String.valueOf(id) }, null, null, null,
				null);
		if( cursor != null && cursor.moveToFirst() ){

			final int ID = 0;
			final int SYMBOL = 1;
			final int DIRECTION = 2;
			final int STOP = 3;
			final int LIMIT = 4;
			final int PPP = 5;
			final int CURRENT = 6;
			final int PROFIT = 7;
			final int STATUS = 8;
			final int TRIGGERED = 9;
            final int DATE_OPENED = 10;
			final int DAYS_ACTIVE = 11;

			position = new Position(Integer.parseInt(cursor.getString(ID)),
					cursor.getString(SYMBOL), cursor.getString(DIRECTION),
					cursor.getDouble(STOP), cursor.getDouble(LIMIT),
					cursor.getDouble(PPP), cursor.getDouble(CURRENT), cursor.getDouble(PROFIT),
                    cursor.getString(STATUS), cursor.getDouble(TRIGGERED), cursor.getString(DATE_OPENED), cursor.getInt(DAYS_ACTIVE));

		}
//		Position position = new Position(Integer.parseInt(cursor
//				.getString(ID)), cursor.getString(SYMBOL), cursor.getString(DIRECTION),
//				cursor.getInt(STOP), cursor.getInt(LIMIT),
//				cursor.getInt(PPP), cursor.getInt(CURRENT), cursor.getInt(PROFIT), cursor.getString(STATUS));
 //db.close();
		cursor.close();
		return position;
	}

	// Getting All Positions
	public List<Position> getAllPositions() {
		List<Position> positionList = new ArrayList<Position>();

		String selectQuery = "SELECT  * FROM " + TABLE_POSITIONS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		final int ID = 0;
		final int SYMBOL = 1;
		final int DIRECTION = 2;
		final int STOP = 3;
		final int LIMIT = 4;
		final int PPP = 5;
		final int CURRENT = 6;
		final int PROFIT = 7;
		final int STATUS = 8;
		final int TRIGGERED = 9;
		final int DATEOPENED = 10;
		final int DAYS_ACTIVE = 11;

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Position position = new Position(0, "", "", 0, 0, 0, 0, 0, null,0,null,0);
				// position.setID(Integer.parseInt(cursor.getString(ID)));
				position.setId(cursor.getInt(ID));
				position.setSymbol(cursor.getString(SYMBOL));
				position.setDirection(cursor.getString(DIRECTION));
				position.setStop(cursor.getDouble(STOP));
				position.setLimit(cursor.getDouble(LIMIT));
				position.setPPP(cursor.getDouble(PPP));
				position.setCurrent(cursor.getDouble(CURRENT));
				position.setProfit(cursor.getDouble(PROFIT));
				position.setStatus(cursor.getString(STATUS));
				position.setTriggered(cursor.getDouble(TRIGGERED));
				position.setDateOpened(cursor.getString(DATEOPENED));
				position.setDaysActive(cursor.getInt(DAYS_ACTIVE));
				positionList.add(position);

			} while (cursor.moveToNext());
		}
		cursor.close();

 //db.close();
		return positionList;
	}

	// Updating single position
	public void updatePosition(Position position) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(KEY_DIRECTION, position.getDirection());
		values.put(KEY_CURRENT, position.getCurrent());
		values.put(KEY_STOP, position.getStop());
		values.put(KEY_LIMIT, position.getLimit());
		values.put(KEY_PROFIT, position.getProfit());
		values.put(KEY_STATUS, position.getStatus());
		values.put(KEY_TRIGGERED, position.getTriggered());
		values.put(KEY_DAYSACTIVE, position.getDaysActive());

		// updating row
		db.update(TABLE_POSITIONS, values, KEY_ID + " = ?",
				new String[]{String.valueOf(position.getId())});
 //db.close();
	}

	// Deleting single position
	public void deleteAllPosition() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from " + TABLE_POSITIONS);

		//db.close();
	}

	// Deleting single position
	public void deletePosition(String sym) {
		SQLiteDatabase db = this.getWritableDatabase();
//		Log.d(TAG, "About to delete POSITION with ID of: " + sym);
		db.delete(TABLE_POSITIONS, KEY_SYMBOL + " = ?",
				new String[]{sym});
 //db.close();
	}

	public String getIndexDate(String ind) {

		cartList.clear();

		SQLiteDatabase db = this.getWritableDatabase();
		String query = "select date from indexes WHERE indexName = '" + ind + "'";
		Cursor cursor = db.rawQuery(query, null);

		String indexDate = "";

		if (cursor.getCount() != 0) {

			if (cursor.moveToFirst()) {
				// Log.d(TAG,"Moved to first");
				do {
					indexDate = cursor.getString(cursor.getColumnIndex("date"));
				} while (cursor.moveToNext());
			}
		}
		cursor.close();
 //db.close();
		return indexDate;
	}



	// Updating single candidate
	public void updateIndex(String indx, String newDate) {
		SQLiteDatabase db = this.getWritableDatabase();


		String strFilter = "indexName=" + "'" + indx + "'";
		ContentValues args = new ContentValues();
		args.put(KEY_DATE, newDate);
		db.update(TABLE_INDEXES, args, strFilter, null);
		//db.close();
	}

	// Getting single check
	public String getIndex(int id) {

		String check = "";

		SQLiteDatabase db = this.getReadableDatabase();

		String q = "SELECT date FROM indexes WHERE id = " + 1;
		// String q = "SELECT date FROM checks";
		Cursor mCursor = db.rawQuery(q, null);

		if (mCursor.moveToFirst()) {
			check = mCursor.getString(0);
		}

 //db.close();
		return check;
	}

	public int getIndexesCount() {
		String countQuery = "SELECT  * FROM " + TABLE_INDEXES;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = 0;
		count = cursor.getCount();
		cursor.close();
 		//db.close();
		// return count
		return count;
	}

	// Adding new quote
	void addQuote(Quote quote) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_SYMBOL, quote.getSYMBOL()); // Quote Name
		values.put(KEY_DATE, quote.getDATE());
		values.put(KEY_HIGH, quote.getHIGH()); // Quote Phone
		values.put(KEY_LOW, quote.getLOW()); // Quote Phone
		values.put(KEY_CLOSE, quote.getCLOSE()); // Quote Phone
		values.put(KEY_ADX, quote.getADX());
		values.put(KEY_TR14, quote.getTR14());
		values.put(KEY_DM14_PLUS, quote.getDM14_PLUS());
		values.put(KEY_DM14_MINUS, quote.getDM14_MINUS());
		values.put(KEY_DI14_PLUS, quote.getDI14_PLUS());
		values.put(KEY_DI14_MINUS, quote.getDI14_MINUS());


		try {
		       db.beginTransaction();
		       db.insert(TABLE_QUOTES, null, values);
		       db.setTransactionSuccessful();
		       }
		   finally {
		       db.endTransaction();
				//db.close(); // Closing database connection

		}


	}

	void addQuote(Vector<Quote> noDupsQuote) {
		SQLiteDatabase db = this.getWritableDatabase();

		Quote quote = new Quote(0, "", "", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,0.0f);

		try {
			db.beginTransaction();

			for (int i=0; i<noDupsQuote.size(); i++) {

				quote = noDupsQuote.get(i);
				ContentValues values = new ContentValues();
				values.put(KEY_SYMBOL, quote.getSYMBOL()); // Quote Name
				values.put(KEY_DATE, quote.getDATE());
				values.put(KEY_HIGH, quote.getHIGH()); // Quote Phone
				values.put(KEY_LOW, quote.getLOW()); // Quote Phone
				values.put(KEY_CLOSE, quote.getCLOSE()); // Quote Phone
				values.put(KEY_ADX, quote.getADX());
				values.put(KEY_TR14, quote.getTR14());
				values.put(KEY_DM14_PLUS, quote.getDM14_PLUS());
				values.put(KEY_DM14_MINUS, quote.getDM14_MINUS());
				values.put(KEY_DI14_PLUS, quote.getDI14_PLUS());
				values.put(KEY_DI14_MINUS, quote.getDI14_MINUS());

				db.insert(TABLE_QUOTES, null, values);

				Log.d(TAG,"Inserted quote for date: " + quote.getDATE());
			}
			db.setTransactionSuccessful();
		}
		finally {
			db.endTransaction();
			//db.close(); // Closing database connection

		}

	}

	void addRawQuote(Vector<Quote> rawQuotes) {
		SQLiteDatabase db = this.getWritableDatabase();

		Quote rawQuote = new Quote(0, "", "", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,0.0f);

		try {
			db.beginTransaction();

			for (int i=0; i<rawQuotes.size(); i++) {

				rawQuote = rawQuotes.get(i);
				ContentValues values = new ContentValues();
				values.put(KEY_SYMBOL, rawQuote.getSYMBOL()); // Quote Name
				values.put(KEY_DATE, rawQuote.getDATE());
				values.put(KEY_LOW, rawQuote.getLOW()); // Quote Phone
				values.put(KEY_HIGH, rawQuote.getHIGH()); // Quote Phone
				values.put(KEY_CLOSE, rawQuote.getCLOSE()); // Quote Phone

				db.insert(TABLE_RAW_QUOTES, null, values);
//

			}
			db.setTransactionSuccessful();
		}
		finally {
			db.endTransaction();
			//db.close(); // Closing database connection

		}




	}


	// Getting single quote
	public void deleteQuote(int i) {

		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_QUOTES, KEY_ID + " = ?",
				new String[] { String.valueOf(1) });
		//db.close();
	}

	Quote getQuote(String symbol) {
		SQLiteDatabase db = this.getReadableDatabase();

		Quote quote = null;

		Cursor cursor = db.query(TABLE_QUOTES, new String[] { KEY_ID,
				KEY_SYMBOL, KEY_DATE, KEY_HIGH, KEY_LOW, KEY_CLOSE, KEY_ADX, KEY_TR14,
				KEY_DM14_PLUS, KEY_DM14_MINUS }, KEY_SYMBOL + "=?",
				new String[] { symbol }, null, null, null, null);


		if (cursor.moveToFirst()) {
			do {
				quote = new Quote(0, "", "", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,0.0f);
				// quote.setID(Integer.parseInt(cursor.getString(ID)));
				quote.setID(cursor.getInt(ID));
				quote.setSYMBOL(cursor.getString(SYMBOL));
				quote.setDATE(cursor.getString(DATE));
				quote.setHIGH(cursor.getDouble(HIGH));
				quote.setLOW(cursor.getDouble(LOW));
				quote.setCLOSE(cursor.getDouble(CLOSE));
				quote.setADX(cursor.getDouble(ADX));
				quote.setTR14(cursor.getDouble(TR14));
				quote.setDM14_PLUS(cursor.getDouble(DM14_PLUS));
				quote.setDM14_MINUS(cursor.getDouble(DM14_MINUS));
			} while (cursor.moveToNext());
			 //db.close();
		}

		return quote;

	}
	Quote getQuote(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_QUOTES, new String[] { KEY_ID,
				KEY_SYMBOL, KEY_DATE, KEY_HIGH, KEY_LOW, KEY_CLOSE, KEY_ADX, KEY_TR14,
				KEY_DM14_PLUS, KEY_DM14_MINUS }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}

		Quote quote = new Quote(0, "", "", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,0.0f);
		// quote.setID(Integer.parseInt(cursor.getString(ID)));
		quote.setID(cursor.getInt(ID));
		quote.setSYMBOL(cursor.getString(SYMBOL));
		quote.setDATE(cursor.getString(DATE));
		quote.setLOW(cursor.getDouble(LOW));
		quote.setHIGH(cursor.getDouble(HIGH));
		quote.setCLOSE(cursor.getDouble(CLOSE));
		quote.setADX(cursor.getDouble(ADX));
		quote.setTR14(cursor.getDouble(TR14));
		quote.setDM14_PLUS(cursor.getDouble(DM14_PLUS));
		quote.setDM14_MINUS(cursor.getDouble(DM14_MINUS));
 	//db.close();
		return quote;
	}

	// Getting All Quotes
	public List<Quote> getAllQuotesByTick(String sym) {
		List<Quote> quoteList = new ArrayList<Quote>();
		// Select All Query
//		String selectQuery = "SELECT  * FROM " + TABLE_QUOTES + " order by " + KEY_SYMBOL + "," + KEY_DATE;

		String selectQuery = "select * from " + TABLE_QUOTES + " WHERE " + KEY_SYMBOL + " = '" + sym + "'";


		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Quote quote = new Quote(0, "", "", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,0.0f);
				// quote.setID(Integer.parseInt(cursor.getString(ID)));
				quote.setID(cursor.getInt(ID));
				quote.setSYMBOL(cursor.getString(SYMBOL));
				quote.setDATE(cursor.getString(DATE));
				quote.setHIGH(cursor.getDouble(HIGH));
				quote.setLOW(cursor.getDouble(LOW));
				quote.setCLOSE(cursor.getDouble(CLOSE));
				quote.setADX(cursor.getDouble(ADX));
				quote.setDM14_PLUS(cursor.getDouble(DM14_PLUS));
				quote.setDM14_MINUS(cursor.getDouble(DM14_MINUS));
				quote.setDI14_PLUS(cursor.getDouble(DI14_PLUS));
				quote.setDI14_MINUS(cursor.getDouble(DI14_MINUS));

				quoteList.add(quote);
			} while (cursor.moveToNext());
		}

		if (quoteList.size() == 0) {
//			Log.d(TAG,"Warning: Returning 0 quoteList from getAllQuotesByTick: " + sym);
		}
		//db.close();
		return quoteList;
	}
	// Getting All Quotes
	public Vector<Quote> getAllRawQuotes(String date) {
		Vector<Quote> rawQuoteList = new Vector<Quote>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_RAW_QUOTES + " WHERE " + KEY_DATE + " < '" + date + "'" + " order by " + KEY_SYMBOL + "," + KEY_DATE + " DESC";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Quote quote = new Quote(0, "", "", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,0.0f);
				// quote.setID(Integer.parseInt(cursor.getString(ID)));
				quote.setID(cursor.getInt(ID));
				quote.setSYMBOL(cursor.getString(SYMBOL));
				quote.setDATE(cursor.getString(DATE));
				quote.setLOW(cursor.getDouble(LOW));
				quote.setHIGH(cursor.getDouble(HIGH));
				quote.setCLOSE(cursor.getDouble(CLOSE));

				rawQuoteList.add(quote);
			} while (cursor.moveToNext());
		}

		if (rawQuoteList.size() == 0) {
			Log.d(TAG,"Warning: Returning 0 rawQuoteList from getAllRawQuotes");
		}
		//db.close();
		return rawQuoteList;
	}

	// Getting All Quotes
	public List<Quote> getAllQuotes() {
		List<Quote> quoteList = new ArrayList<Quote>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_QUOTES + " order by " + KEY_SYMBOL + "," + KEY_DATE;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Quote quote = new Quote(0, "", "", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,0.0f);
				// quote.setID(Integer.parseInt(cursor.getString(ID)));
				quote.setID(cursor.getInt(ID));
				quote.setSYMBOL(cursor.getString(SYMBOL));
				quote.setDATE(cursor.getString(DATE));
				quote.setHIGH(cursor.getDouble(HIGH));
				quote.setLOW(cursor.getDouble(LOW));
				quote.setCLOSE(cursor.getDouble(CLOSE));
				quote.setADX(cursor.getDouble(ADX));
				quote.setDM14_PLUS(cursor.getDouble(DM14_PLUS));
				quote.setDM14_MINUS(cursor.getDouble(DM14_MINUS));
				quote.setDI14_PLUS(cursor.getDouble(DI14_PLUS));
				quote.setDI14_MINUS(cursor.getDouble(DI14_MINUS));

				quoteList.add(quote);
			} while (cursor.moveToNext());
		}

 //db.close();
		if (quoteList.size() == 0) {
//			Log.d(TAG,"Warning: Returning 0 quoteList from getAllQuotes");
		}
		return quoteList;
	}

	private boolean symbolActive(String symbol) {

		boolean exists = false;

		List<Potential> potentials = this.getAllPotentials();
		List<Position> positions = this.getAllPositions();

		for (int i=0; i<potentials.size(); i++) {
			Potential p = potentials.get(i);
			if (p.getSymbol().equals(symbol)) {
				exists = true;
			}
		}
		for (int i=0; i<positions.size(); i++) {
			Position p = positions.get(i);
			if (p.getSymbol().equals(symbol)) {
				exists = true;
			}
		}

		return exists;
	}

	// Getting All Quotes
	public void checkQuotesADX(String symbol, String date) {

		String selectQuery = "SELECT  * FROM " + TABLE_QUOTES + " WHERE " + KEY_SYMBOL + " = '" + symbol + "' AND " + KEY_DATE + " = '" + date + "'";
//
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
//
//		int count = getAllQuotes().size();
//		Log.d(TAG,"Quote count is: " + count);
		int ID=0;
		int SYMBOL=1;
		int DATE=2;
		int HIGH=3;
		int LOW=4;
		int CLOSE=5;
		int ADX=6;

//		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Log.d(TAG,"Move to first");
				Quote quote = new Quote(0, "", "", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,0.0f);
				// quote.setID(Integer.parseInt(cursor.getString(ID)));
				quote.setID(cursor.getInt(ID));
				quote.setSYMBOL(cursor.getString(SYMBOL));
				quote.setDATE(cursor.getString(DATE));
				quote.setLOW(cursor.getDouble(LOW));
				quote.setHIGH(cursor.getDouble(HIGH));
				quote.setCLOSE(cursor.getDouble(CLOSE));
				quote.setADX(cursor.getDouble(ADX));
//
//				String reportDate = null;
//
//				String string = quote.getDATE();
//				try {
//					Date newDate = new SimpleDateFormat("yyyy-MM-dd",
//							Locale.ENGLISH).parse(string);
//					SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
//					reportDate = df.format(newDate);
//
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}

				double close = cursor.getDouble(CLOSE);
				String sym = cursor.getString(SYMBOL);

				String dir = null;

				if ( cursor.getDouble(DM14_PLUS) > cursor.getDouble(DM14_MINUS) )  {
					dir = "BUY";
				} else {
					dir = "SELL";
				}

				if ( (cursor.getDouble(ADX) > ADX_THRESHOLD) && checkHighLow(close, sym, dir) && ! symbolActive(cursor.getString(SYMBOL)) ) {
//				if ( (cursor.getDouble(ADX) > ADX_THRESHOLD) && ! symbolActive(cursor.getString(SYMBOL)) ) {


					Potential pot = new Potential(0, "", "", 0, 0, 0, 0, 0,0,0,0,0,0);
					//pot.setId(cursor.getInt(ID));
					pot.setSymbol(cursor.getString(SYMBOL));
					pot.setDirection(dir);
					pot.setStartId(cursor.getInt(ID));
					pot.setCurrent(cursor.getDouble(CLOSE));

					pot.setHigh(cursor.getDouble(CLOSE));
					pot.setLow(cursor.getDouble(CLOSE));
//					pot.setTrigger(0);
//					pot.setDaysActive(0);
					if ( ! symbolActive(cursor.getString(SYMBOL)) ) {
						Log.d(TAG, "Found ADX: " + quote.getADX() + " on: " + quote.getDATE());
//
//						Log.d(TAG,"Adding potential as found ADX: " + quote.getSYMBOL() + " : " + quote.getADX() + " on: " + quote.getDATE());
						addPotential(pot);


					} else {
						Log.d(TAG,"Did not add potential, already exists");
					}


				} else {
//					Log.v(TAG,"Not adding potential " + cursor.getString(SYMBOL) + " ADX: " + cursor.getDouble(ADX));
				}
			} while (cursor.moveToNext());
		}
		cursor.close();
		//db.close();
	}



	private boolean checkHighLow(double close, String symbol, String dir) {
		// TODO Auto-generated method stub
		Tick tick = this.getTick(symbol);

		double high = tick.getTwoMonthHigh();
		double low = tick.getTwoMonthLow();

		if ( (close <= low && dir.equals("SELL")) || (close >= high && dir.equals("BUY")) ) {
			return true;

		} else {
//			Log.d(TAG,"Did not find a High/Low");
			return false;
		}

	}

	void deleteQuote(String date) {

		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_QUOTES, KEY_DATE + " > ?",
				new String[] { String.valueOf(date) });
//		Log.d(TAG,"value of date: " + String.valueOf(date));
		//db.close();

	}

	String getMaxQuoteDate(String symb) {

		String selectQuery1 = "select max(" + KEY_DATE + ") from " + TABLE_QUOTES + " WHERE " + KEY_SYMBOL + " = '" + symb + "'";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery1, null);

		String lastDate = null;
		if (cursor.moveToFirst()) {
			do {

				lastDate = cursor.getString(0);

			} while (cursor.moveToNext());
		}

//		if ( lastDate == null ) {
//			// date format yyyy-mm-dd
//			return START_DATE;
//		} else {
//			return lastDate;
//		}

		return lastDate;
	}


	Quote getQuoteByDate3(String symb) {

		String selectQuery1 = "select max(" + KEY_DATE + ") from " + TABLE_QUOTES + " WHERE " + KEY_SYMBOL + " = '" + symb + "'";
//		String selectQuery1 = "select " + KEY_DATE + " from " + TABLE_QUOTES;

		String countQuery = "SELECT  * FROM " + TABLE_QUOTES;
		SQLiteDatabase db1 = this.getReadableDatabase();
		Cursor cursor1 = db1.rawQuery(countQuery, null);
		cursor1.close();


//		String selectQuery = "select * from " + TABLE_QUOTES + " WHERE " + KEY_DATE + " = '" + date + "' AND " + KEY_SYMBOL + " = '" + symb + "'";



		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery1, null);

		Quote quote = null;
//		Quote returnQuote = null;
//		Quote quote = null;
//		Date tmpDate = null;
//		String newDate;
		String lastDate = null;
		if (cursor.moveToFirst()) {
			do {

				lastDate = cursor.getString(0);

			} while (cursor.moveToNext());
		}

		String selectQuery2 = "SELECT  * FROM " + TABLE_QUOTES + " WHERE " + KEY_DATE + " = '" + lastDate + "' AND " + KEY_SYMBOL + " = '" + symb + "'";
		cursor = db.rawQuery(selectQuery2, null);

		if (cursor.moveToFirst()) {
			do {
				quote = new Quote(0, "", "", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,0.0f);
				// quote.setID(Integer.parseInt(cursor.getString(ID)));
				quote.setID(cursor.getInt(ID));
				quote.setSYMBOL(cursor.getString(SYMBOL));
				quote.setDATE(cursor.getString(DATE));
				quote.setLOW(cursor.getDouble(LOW)) ;
				quote.setHIGH(cursor.getDouble(HIGH));
				quote.setCLOSE(cursor.getDouble(CLOSE));
				quote.setADX(cursor.getDouble(ADX));
				quote.setTR14(cursor.getDouble(TR14));
				quote.setDM14_PLUS(cursor.getDouble(DM14_PLUS));
				quote.setDM14_MINUS(cursor.getDouble(DM14_MINUS));
			} while (cursor.moveToNext());
		}
		cursor.close();
		return quote;
	}

	// Getting single quote
	Vector<Quote> getQuoteByDate2(String date) {

		Vector<Quote> quoteList = new Vector<Quote>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_RAW_QUOTES + " WHERE " + KEY_DATE + " = '" + date + "'";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		Quote quote = null;

		if (cursor.moveToFirst()) {
			do {
				// quote.setID(Integer.parseInt(cursor.getString(ID)));

				quote = new Quote(0, "", "", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,0.0f);
				// quote.setID(Integer.parseInt(cursor.getString(ID)));
				quote.setID(cursor.getInt(ID));
				quote.setSYMBOL(cursor.getString(SYMBOL));
				quote.setDATE(cursor.getString(DATE));
				quote.setLOW(cursor.getDouble(LOW)) ;
				quote.setHIGH(cursor.getDouble(HIGH));
				quote.setCLOSE(cursor.getDouble(CLOSE));
				quoteList.add(quote);
			} while (cursor.moveToNext());
		}

		cursor.close();
		 //db.close();

		return quoteList;
	}

	Vector<Quote> getQuoteByDate4(String date) {

		Vector<Quote> quoteList = new Vector<Quote>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_RAW_QUOTES + " WHERE " + KEY_DATE + " < '" + date + "'";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		Quote quote = null;

		int counter = 0;


		try {
			if (cursor.moveToFirst()) {
                do {
                    // quote.setID(Integer.parseInt(cursor.getString(ID)));
                    counter++;
                    quote = new Quote(0, "", "", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,0.0f);
                    // quote.setID(Integer.parseInt(cursor.getString(ID)));
                    quote.setID(cursor.getInt(ID));
                    quote.setSYMBOL(cursor.getString(SYMBOL));
                    quote.setDATE(cursor.getString(DATE));
                    quote.setLOW(cursor.getDouble(LOW)) ;
					quote.setHIGH(cursor.getDouble(HIGH));
                    quote.setCLOSE(cursor.getDouble(CLOSE));
                    quoteList.add(quote);
                } while (cursor.moveToNext());
            }
		} finally {
			cursor.close();
			 //db.close();

		}


		return quoteList;
	}

	// Updating single quote
	public void updateQuote(Quote quote) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_SYMBOL, quote.getSYMBOL());
		values.put(KEY_LOW, quote.getLOW());
		values.put(KEY_HIGH, quote.getHIGH());
		values.put(KEY_CLOSE, quote.getCLOSE());
		values.put(KEY_ADX, quote.getADX());
		// updating row
		db.update(TABLE_QUOTES, values, KEY_ID + " = ?",
				new String[]{String.valueOf(quote.getID())});
 //db.close();
	}

	// Deleting single quote
	public void deleteQuote(Quote quote) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_QUOTES, KEY_ID + " = ?",
				new String[]{String.valueOf(quote.getID())});
 		//db.close();
	}

	// Getting quotes Count
	public int getQuotesCount() {
		String countQuery = "SELECT  * FROM " + TABLE_QUOTES;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();

		return count;
	}


	// Getting quotes Count
	public int getPotentialsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_POTENTIALS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();

		return count;
	}

    public void deleteAllHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_CLOSED_HIST);
        //db.close();

    }

	void addClosedHist(Position position) {
		SQLiteDatabase db = this.getWritableDatabase();


		//        this.id = id;
//        this.symbol = symbol;
//        this.direction = direction;
//        this.profit = profit;
//        this.triggered = triggered;
//        this.dateOpened = dateOpened;
//        this.dateClosed = dateClosed;

		String closedDate = getClosedDate(position);

		closedDate = String.valueOf(position.getDaysActive());

		Log.d(TAG, "Adding closed_hist with symbol: " + position.getSymbol());
		Log.d(TAG, "Adding closed_hist with triggered: " + position.getTriggered());
		Log.d(TAG, "Adding closed_hist with profit: " + position.getProfit());
		Log.d(TAG, "Adding closed_hist with date opened: " + position.getDateOpened());
		// Why is closedDate 0?
		Log.d(TAG, "Adding closed_hist with date closed: " + closedDate);


		ContentValues values = new ContentValues();
		values.put(KEY_SYMBOL, position.getSymbol()); // position Name
		values.put(KEY_DIRECTION, position.getDirection());
		values.put(KEY_PROFIT, position.getProfit());
		values.put(KEY_TRIGGERED, position.getTriggered());
		values.put(KEY_DATE_OPENED, position.getDateOpened());
		values.put(KEY_DATE_CLOSED, closedDate);
		try {
			db.insert(TABLE_CLOSED_HIST, null, values);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//sendMessage(10, e.getMessage());
		} finally {
			//db.close();
		}

	}



	public List<History> getAllClosedHist() {
		List<History> historyList = new ArrayList<History>();
//       this.deleteAllHistory();
		String selectQuery = "SELECT  * FROM " + TABLE_CLOSED_HIST;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);



        final int ID = 0;
		final int SYMBOL = 1;
		final int DIRECTION = 2;
		final int PROFIT = 3;
		final int TRIGGERED = 4;
        final int DATE_OPENED = 5;
        final int DATE_CLOSED = 6;

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				History history = new History(0, "", "", 0, 0, 0, "", "");
				// history.setID(Integer.parseInt(cursor.getString(ID)));
				history.setId(cursor.getInt(ID));
				history.setSymbol(cursor.getString(SYMBOL));
				history.setDirection(cursor.getString(DIRECTION));
				history.setProfit(cursor.getDouble(PROFIT));
				history.setTriggered(cursor.getDouble(TRIGGERED));
                history.setDateOpened(cursor.getString(DATE_OPENED));
                history.setDateClosed(cursor.getString(DATE_CLOSED));
				// Adding history to list
//				Log.v(TAG,"Adding status: + " + cursor.getString(STATUS));
				historyList.add(history);

			} while (cursor.moveToNext());
		}
		cursor.close();
		return historyList;
	}

	public void setLastPotUpdateDate() {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String today = dateFormat.format(date);

		values.put(KEY_DATE, today);

		try {
			db.beginTransaction();
			db.delete(TABLE_LASTPOTUPDATE,null,null);
//			db.update(TABLE_LASTUPDATE, values, null, null);
			db.insert(TABLE_LASTPOTUPDATE, null, values);
			db.setTransactionSuccessful();
//			Log.d(TAG,"Updated with values: " + values);
		}
		finally {
			db.endTransaction();
		}
	}

	public String getLastPotUpdate() {

		String selectQuery = "SELECT  * FROM " + TABLE_LASTPOTUPDATE;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		final int DATE = 0;

		String thisDate = null;

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				thisDate = cursor.getString(DATE);
//				Log.d(TAG,"Got cursor: " + cursor.getString(DATE));
			} while (cursor.moveToNext());
		}
		cursor.close();

		//db.close();
//		Log.d(TAG,"Returning lastPotUpdate date: " + thisDate);
		return thisDate;
	}

	public void setLastQuoteUpdateDate() {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String today = dateFormat.format(date);

		values.put(KEY_DATE, today);

		try {
			db.beginTransaction();
			db.delete(TABLE_LASTQUOTEUPDATE,null,null);
			db.insert(TABLE_LASTQUOTEUPDATE, null, values);
			db.setTransactionSuccessful();
//			Log.d(TAG,"Updated pot with values: " + values);
		}
		finally {
			db.endTransaction();
		}
	}

	public String getLastQuoteUpdate() {

		String selectQuery = "SELECT  * FROM " + TABLE_LASTQUOTEUPDATE;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		final int DATE = 0;

		String thisDate = null;

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				thisDate = cursor.getString(DATE);
//				Log.d(TAG,"Got quote cursor: " + cursor.getString(DATE));
			} while (cursor.moveToNext());
		}
		cursor.close();
		//db.close();

//		Log.d(TAG,"Returning lastQuoteUpdate date: " + thisDate);
		return thisDate;
	}

    private String getClosedDate(Position p) {

//        String date;
//
        Calendar cal = Calendar.getInstance();
//
//        date = cal.getTime().toString();

		String dateOpened = p.getDateOpened();
		int daysActive = p.getDaysActive();

//		Log.d(TAG,"dateOpened: " + dateOpened + " and daysActive " + daysActive);

        String strdate = null;



        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        if (cal != null) {
            strdate = sdf.format(cal.getTime());
        }



//        Log.d(TAG,"todays date is: " + strdate);

        return String.valueOf(daysActive);

    }

	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	public void insertLine(String symbol, String data) {
		SQLiteDatabase db = this.getWritableDatabase();

		String[] parts = data.split(",");
//		Data: 2016-01-29,99.370003,101.839996,99.07,101.839996,3383800,101.604821
		String date = parts[0];
//		data = parts[2] + "," + parts[3] + "," + parts[4];
		double part2 = round(Double.parseDouble(parts[2]),2);
		double part3 = round(Double.parseDouble(parts[3]),2);
		double part4 = round(Double.parseDouble(parts[4]),2);

		data = part2 + "," + part3 + "," + part4;


		if ( !date.equals("Date") ) {
//            Log.d(TAG,"Date is: " + date);
			ContentValues values = new ContentValues();
			values.put(KEY_SYMBOL, symbol);
			values.put(KEY_DATE, date);
			values.put(KEY_STREAM_DATA, data); // tick Name
			try {
				db.beginTransaction();
				db.insert(TABLE_RAW_STREAM, null, values);
				db.setTransactionSuccessful();
			} catch (Exception e) {
			Log.d(TAG,"insertLine Exception: " + e.getMessage());
		}	 finally {
				db.endTransaction();
				//db.close();
			}

		}

		int count=0;
	}

	public int getRawCount() {

		SQLiteDatabase db = this.getWritableDatabase();


		int count=0;

		String countQuery = "SELECT  * FROM " + TABLE_RAW_STREAM;
		db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		count = cursor.getCount();
		cursor.close();

		return count;
	}

	public void showAllPotentials() {
		List rawList = new ArrayList<>();
//       this.deleteAllHistory();
		String selectQuery = "SELECT  * FROM " + TABLE_POTENTIALS;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);


		final int ID = 0;
		final int SYMBOL = 1;
		final int DIRECTION = 2;
		final int PROFIT = 3;
		final int TRIGGERED = 4;
		final int DATE_CLOSED = 6;


//		values.put(KEY_SYMBOL, potential.getSymbol());
//		values.put(KEY_TRIGGER,  potential.getTrigger());
//		values.put(KEY_DIRECTION, potential.getDirection());
//		values.put(KEY_DAYSACTIVE, potential.getDaysActive());
//		values.put(KEY_HOT, potential.getHot());
//		values.put(KEY_STOP, potential.getStop());
//		values.put(KEY_LIMIT, potential.getLimit());
//		values.put(KEY_PPP, potential.getPpp());
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {

				Log.d(TAG,"DB: " + cursor.getString(0));
				Log.d(TAG,"DB: " + cursor.getString(1));
				Log.d(TAG,"DB: " + cursor.getString(2));
				Log.d(TAG,"DB: " + cursor.getString(3));

				Log.d(TAG,"DB: " + cursor.getString(4));

				Log.d(TAG,"DB: " + cursor.getString(5));
				Log.d(TAG,"DB: " + cursor.getString(6));
				Log.d(TAG,"DB: " + cursor.getString(8));

				Log.d(TAG,"DB: " + cursor.getString(9));
				Log.d(TAG,"DB: " + cursor.getString(10));
				Log.d(TAG,"DB: " + cursor.getString(11));





			} while (cursor.moveToNext());
		}
		cursor.close();
//		return historyList;
	}

	public void showAllRaw() {
		List rawList = new ArrayList<>();
//       this.deleteAllHistory();
		String selectQuery = "SELECT  * FROM " + TABLE_RAW_STREAM;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);


		final int ID = 0;
		final int SYMBOL = 1;
		final int DIRECTION = 2;
		final int PROFIT = 3;
		final int TRIGGERED = 4;
		final int DATE_CLOSED = 6;

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {

				Log.d(TAG,"DB: " + cursor.getString(0));
				Log.d(TAG,"DB: " + cursor.getString(1));
				Log.d(TAG,"DB: " + cursor.getString(2));

			} while (cursor.moveToNext());
		}
		cursor.close();
//		return historyList;
	}

	public Vector<Quote> getQuotesFromDB(String tickSymbol, String fromDate, String toNewDate) {

		Vector<Quote> allQuotesForTick = new Vector<Quote>();
		String selectQuery = "SELECT  * FROM " + TABLE_RAW_STREAM + " WHERE " + KEY_SYMBOL + " = '" + tickSymbol + "' and " + KEY_DATE + " <= '" + toNewDate + "' and " + KEY_DATE + " > '" + fromDate + "'";


//		Log.d(TAG,"selectQuery: " + selectQuery);
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		Quote quote = null;

		final int SYMBOL = 0;
		final int DATE = 1;
		final int DATA = 2;

		String close;
		String high;
		String low;
		String symbol;
		String date;
		String data;

		try {
			if (cursor.moveToFirst()) {
				do {
					// quote.setID(Integer.parseInt(cursor.getString(ID)));
					quote = new Quote(0, "", "", 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0);
					// quote.setID(Integer.parseInt(cursor.getString(ID)));
//					quote.setID(cursor.getInt(SYMBOL));
//					quote.setSYMBOL(cursor.getString(DATE));
//					quote.setDATE(cursor.getString(DATA));

					symbol = cursor.getString(SYMBOL);
					date = cursor.getString(DATE);
					data = cursor.getString(DATA);
//					Log.d(TAG,"data: " + data);
					String[] parts = data.split(",");
//					Log.d(TAG,"parts[0]: " + parts[0]);
//					Log.d(TAG,"parts[1]: " + parts[1]);

					quote.setSYMBOL(symbol);
					quote.setDATE(date);
					quote.setHIGH(Double.valueOf(parts[0]));
					quote.setLOW(Double.valueOf(parts[1]));
					quote.setCLOSE(Double.valueOf(parts[2]));
//
					allQuotesForTick.add(quote);
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
		}

//		for (int i=0; i<allQuotesForTick.size(); i++) {
//			Quote q = allQuotesForTick.get(i);
//			Log.d(TAG,"HERE: " + q.getCLOSE());
//		}

		return allQuotesForTick;

	}

	public void truncateDatabase() {

		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM " + TABLE_QUOTES);
		db.execSQL("DELETE FROM " + TABLE_INDEXES);
		db.execSQL("DELETE FROM " + TABLE_POSITIONS);
		db.execSQL("DELETE FROM " + TABLE_POTENTIALS);
		db.execSQL("DELETE FROM " + TABLE_TICKS);
		db.execSQL("DELETE FROM " + TABLE_CLOSED_HIST);
		db.execSQL("DELETE FROM " + TABLE_LASTPOTUPDATE);
		db.execSQL("DELETE FROM " + TABLE_LASTQUOTEUPDATE);
		db.execSQL("DELETE FROM " + TABLE_RAW_QUOTES);
		db.execSQL("DELETE FROM " + TABLE_RAW_STREAM);
		
		//db.close();

		Log.d(TAG,"Tables deleted");

		
	}

}