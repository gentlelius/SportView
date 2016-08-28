package ls.main.adapter;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import ls.main.bean.BMIBean;
import ls.main.bean.PathRecord;

public class DbAdapter {
	private final static String DATABASE_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/recordPath";
	static final String DATABASE_NAME=DATABASE_PATH+"/"+"record_nim.db";
	private static final int DATABASE_VERSION=2;
	private static final String RECORD_TABLE="record";
	private static final String RECORD_CREATE=
			"create table if not exists record("
			+"id integer primary key,"
			+"stratpoint STRING,"
			+"endpoint STRING,"
			+"pathline STRING,"
			+"distance STRING,"
			+"duration STRING,"
			+"averagespeed STRING,"
			+"date STRING"
			+");";
	private static final String BMI_CREATE=
			"create table if not exists bmi("
					+"id integer primary key autoincrement,"
					+"weight STRING,"
					+"height STRING,"
					+"bmi STRING,"
					+"time STRING"
					+");";
	public static class DatabaseHelper extends SQLiteOpenHelper{
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(RECORD_CREATE);
			db.execSQL(BMI_CREATE);
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(BMI_CREATE);
		}	
		}
		private Context mCtx =null;
		private DatabaseHelper dbHelper;
		private SQLiteDatabase db;	
		//constructor
		public DbAdapter(Context ctx)
		{
			this .mCtx=ctx;
			dbHelper=new DatabaseHelper(mCtx);
		}
		public DbAdapter open() throws SQLException{
			
			db=dbHelper.getWritableDatabase();
			return this;
		}	
		public void close(){
			dbHelper.close();
		}
		public Cursor getall()
		{
			return db.rawQuery("SELECT * FROM record", null);
		}
		//remove an entry
		public boolean delete(long rowId)
		{

			return db.delete(RECORD_TABLE, "id="+rowId, null) > 0;
		}
		
			public static final String KEY_ROWID="id";
			public static final String KEY_DISTANCE="distance";
			public static final String KEY_DURATION="duration";
			public static final String KEY_SPEED="averagespeed";
			public static final String KEY_LINE="pathline";
			public static final String KEY_STRAT="stratpoint";
			public static final String KEY_END="endpoint";
			public static final String KEY_DATE="date";
				
		public long createrecord(String distance,String duration, String averagespeed,String pathline, String stratpoint, String endpoint, String date)
			{
				ContentValues args =new ContentValues();
				args.put("distance", distance);
				args.put("duration", duration);
				args.put("averagespeed", averagespeed);
				args.put("pathline", pathline);
				args.put("stratpoint", stratpoint);
				args.put("endpoint", endpoint);
				args.put("date", date);
				return db.insert(RECORD_TABLE, null, args);
			}

	public long createBmi(int id,String weight,String height,String bmi,String time)
	{
		ContentValues args =new ContentValues();
		args.put("id", id);
		args.put("weight", weight);
		args.put("height", height);
		args.put("bmi", bmi);
		args.put("time", time);
		return db.insert("bmi", null, args);
	}
 		public Cursor getallrecord()
			{
				return db.query(RECORD_TABLE, new String []{KEY_DISTANCE,KEY_DURATION,KEY_SPEED,KEY_LINE,KEY_STRAT,KEY_END,KEY_DATE}, null, null, null, null, null);
			}


		public Cursor getRecordByDate(){
			SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd");
			String datestr = format.format(new Date());
			Log.e("TAG",datestr+"==========================");
//			Cursor mCursor = db.rawQuery("select * from record where date like ?", new String[]{datestr+"%"});
			return db.query(RECORD_TABLE, new String []{KEY_DISTANCE,KEY_DURATION,KEY_SPEED,KEY_LINE,KEY_STRAT,KEY_END,KEY_DATE}, "date LIKE ?", new String[]{"%"+datestr+"%"}, null, null, null);

		}

		public double getSumToday(){
			SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd");
			String datestr = format.format(new Date());
//			Cursor mCursor = db.rawQuery("select * from record where date like ?", new String[]{"%"+datestr+"%"});
			Cursor mCursor = db.query(RECORD_TABLE, new String []{KEY_DISTANCE,KEY_DURATION,KEY_SPEED,KEY_LINE,KEY_STRAT,KEY_END,KEY_DATE}, "date LIKE ?", new String[]{"%"+datestr+"%"}, null, null, null);
			double sum = 0;
			while (mCursor.moveToNext()) {
				PathRecord record = new PathRecord();
//			record.setId(mCursor.getInt(mCursor.getColumnIndex(DbAdapter.KEY_ROWID)));
				double distance = Double.parseDouble(mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_DISTANCE)));
				sum+=distance;
				Log.e("distance",distance+"ddd");
			}
			return  sum;
		}

		public void delete(){
			db.delete("record","date like ?",new String[]{"%07:45:27%"});
		}


		public void updateBmi(BMIBean bean){
			db.execSQL("insert into bmi(weight,height,bmi,time) values(?,?,?,?)",
					new String[]{bean.getWeight(),bean.getHeight(),bean.getBmi(),bean.getTime()});
		}

		public Cursor getTop10Bmi(){
			return db.rawQuery("select * from bmi order by time desc limit 10",null);
		}

		public void getLatesBmi(){
			db.execSQL("insert into bmi(weight,height,bmi,time) values(?,?,?,?)",
					new String[]{"100","170","20","1472351171673"});
		}

	}


