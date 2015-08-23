/************************************************************************************************
 * @author Patrick				
 * 										
 * Diese Klasse dient zur Kommunikation zwischen den einzelnen Activities dieser App.
 * Dazu bietet diese Klasse alle Funktionen, die zur Verwaltung der Datenbank notwendig sind,
 * an. Somit kann die komplette Handler-Klasse privat gehalten werden. Auch die Instanzen
 * der Datenbank werden in der jeweiligen Methode instanziiert, sodass unnötige Parameter-
 * übergaben vermieden werden.
 * 
 * DEKLARIERUNG IN ANDEREN KLASSEN WIE FOLGT:
 * 
 * DatabaseHandler dbHandler = new DatabaseHandler(this); ODER "[NAME DER ACTIVITY].this"
 * 
 * Diese Klasse baut sich wie folgt auf:
 * - Variablendeklarationen
 * - onCreate der Datenbank
 * - onUpgrade der Datenbank
 * - Methoden für die einzelnen Activities
 * 
 * Nähere Informationen zu den Methoden finden Sie an der passenden Stelle!
 * 
 ***********************************************************************************************/
package de.bg.fhdw.bfwi413a.karthago.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	
	//DECLARE GENERAL-PARAMETERS
	public static final String DB_NAME = "db.SQLite";
	public static final int DB_VERSION = 1;
	
	//DECLARE COLUMNS-CONFIG
	public static final String TB_NAME_CONFIG ="config";
	public static final String ID = "ID";
	public static final String KEY = "KEY";
	public static final String VALUE = "VALUE";
	
	//DECLARE COLUMNS-CARDFILE NAMES
	public static final String TB_NAME_CARDFILE_NAMES ="cardfiles";
	private static final String CARDFILE_NAME = "CARDFILE_NAME";
	
	//DECLARE COLUMNS-USER
	public static final String TB_NAME_USER ="user";
	private static final String USER = "USER";
	private static final String FIRST_START = "FIRST_START";
	
	//DECLARE COLUMNS-TIMESTAMP
	public static final String TB_NAME_TIMESTAMP = "timestamp";
	private static final String LEVEL = "LEVEL";
	private static final String INCREASED_TIME_FOR_LEVEL = "INCREASED_TIME_FOR_LEVEL_IN_SECONDS";
	
	//DECLARE COLUMNS-CARDS
	public static final String TB_NAME_CARDS = "cards";
	private static final String QUESTION_ID = "QUESTION_ID";
	private static final String ANSWER_TYPE = "ANSWER_TYPE";
	private static final String EVALUATION_TIMESTAMP = "EVALUATION_TIMESTAMP";
	private static final String ACTIVE_FLAG = "ACTIVE_FLAG";
	
	//DECLARE COULUMNS-EVENTS
	public static final String TB_NAME_EVENTS ="events";
	public static final String NAME = "EVENT_NAME";
	
	//DECLARE FIRST APP-START
	public static final String TB_NAME_APP_FIRST_START = "first_start_app";
	public static final String APP_FIRST_START = "APP_FIRST_START";
	
	
	//CONSTRUCTOR TO INITIALIZE THE DATABASE IF NOT EXISTS
	public DatabaseHandler(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//CREATE CONFIG-TABLE
		String CREATE_CONFIG_TABLE = "CREATE TABLE IF NOT EXISTS " + TB_NAME_CONFIG
				+ " (" 
				+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
				+ KEY + " TEXT,"
				+ VALUE + " INTEGER"
				+ ");";
		
		//CREATE CARDFILE_NAMES_TABLE
		String CREATE_CARDFILE_NAMES_TABLE = "CREATE TABLE IF NOT EXISTS " + TB_NAME_CARDFILE_NAMES
				+ " (" 
				+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
				+ CARDFILE_NAME + " TEXT"
				+ ");";
		
		//CREATE USER-TABLE
		String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + TB_NAME_USER
				+ " (" 
				+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
				+ USER + " TEXT,"
				+ FIRST_START + " INTEGER"
				+ ");";
		
		//CREATE CARDS-TABLE
		String CREATE_CARDS_TABLE = "CREATE TABLE IF NOT EXISTS " + TB_NAME_CARDS
				+ " (" 
				+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
				+ QUESTION_ID + " INTEGER,"
				+ USER + " TEXT,"
				+ LEVEL + " INTEGER,"
				+ EVALUATION_TIMESTAMP + " INTEGER,"
				+ ANSWER_TYPE + " TEXT,"
				+ CARDFILE_NAME + " TEXT,"
				+ ACTIVE_FLAG + " INTEGER" // 0 = active; 1 = not active
				+ ");";
		
		//CREATE TIMESTAMP-TABLE
		String CREATE_TIMESTAMP_TABLE = "CREATE TABLE IF NOT EXISTS " + TB_NAME_TIMESTAMP
				+ " (" 
				+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
				+ LEVEL + " INTEGER,"
				+ INCREASED_TIME_FOR_LEVEL + " INTEGER"
				+ ");";
		
		//CREATE EVENTS-TABLE
		String CREATE_EVENTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TB_NAME_EVENTS
				+ " (" 
				+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
				+ NAME + " TEXT,"
				+ EVALUATION_TIMESTAMP + " INTEGER,"
				+ USER + " TEXT,"
				+ CARDFILE_NAME + " TEXT"
				+ ");";
		
		//CREATE FIRST_START_APP-Table
		String CREATE_FIRST_START_APP_TABLE = "CREATE TABLE IF NOT EXISTS " + TB_NAME_APP_FIRST_START
				+ " (" 
				+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
				+ APP_FIRST_START + " INTEGER"
				+ ");";
		
		
		//EXECUTE THE SQL-STATEMENTS
		db.execSQL(CREATE_CONFIG_TABLE);
		db.execSQL(CREATE_CARDFILE_NAMES_TABLE);
		db.execSQL(CREATE_USER_TABLE);
		db.execSQL(CREATE_TIMESTAMP_TABLE);
		db.execSQL(CREATE_CARDS_TABLE);
		db.execSQL(CREATE_EVENTS_TABLE);
		db.execSQL(CREATE_FIRST_START_APP_TABLE);
		
		//INITIALIZE TABLES FOR USING FIRST TIME
		initializeTablesForFirstStart(db);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//DROP ALL OLD TABLES IF EXISTED
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME_CONFIG);
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME_CARDFILE_NAMES);
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME_TIMESTAMP);
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME_CARDS);
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME_APP_FIRST_START);
  
        //RESTART THE CREATE PROCESS
        onCreate(db);

	}
	
	public void initializeTablesForFirstStart(SQLiteDatabase db){
		//INITIALIZING THE DATABASES (USER AND CONFIG) FOR FIRST START
		String initializeSorttyp = "INSERT INTO " + TB_NAME_CONFIG +" VALUES (0, 'Option1', 0);";
		String initializeLearnmode = "INSERT INTO " + TB_NAME_CONFIG +" VALUES (1, 'Option2', 0);";
		
		//ADMIN USER HAS TO EXIST EVERYTIME, SO HE IS NOT DELETEABLE (NO INFLUENCE ON SYSTEM)
		String SET_FIRST_USER = "INSERT INTO " + TB_NAME_USER +" VALUES (0, 'ADMIN', 0);";
		
		//SET THE LEVELS AND TIMES FOR TIMESTAMPS
		String LEVEL_1 = "INSERT INTO " + TB_NAME_TIMESTAMP +" VALUES (0,1,60)";
		String LEVEL_2 = "INSERT INTO " + TB_NAME_TIMESTAMP +" VALUES (1,2,300)";
		String LEVEL_3 = "INSERT INTO " + TB_NAME_TIMESTAMP +" VALUES (2,3,3600)";
		String LEVEL_4 = "INSERT INTO " + TB_NAME_TIMESTAMP +" VALUES (3,4,43200)";
		String LEVEL_5 = "INSERT INTO " + TB_NAME_TIMESTAMP +" VALUES (4,5,86400)";
		String LEVEL_6 = "INSERT INTO " + TB_NAME_TIMESTAMP +" VALUES (5,6,172800)";
		String LEVEL_7 = "INSERT INTO " + TB_NAME_TIMESTAMP +" VALUES (6,7,604800)";
		
		//SET THE FIRST APP-START TO 1 - ANOTHER VALUE CAN'T BE POSSIBLE
		String APP_FIRST_START = "INSERT INTO " + TB_NAME_APP_FIRST_START + " VALUES (0,0)";
		
		//EXCECUTE SQL-STATEMENTS
		db.execSQL(initializeSorttyp);
		db.execSQL(initializeLearnmode);
		db.execSQL(SET_FIRST_USER);
		db.execSQL(LEVEL_1);
		db.execSQL(LEVEL_2);
		db.execSQL(LEVEL_3);
		db.execSQL(LEVEL_4);
		db.execSQL(LEVEL_5);
		db.execSQL(LEVEL_6);
		db.execSQL(LEVEL_7);
		db.execSQL(APP_FIRST_START);
	}
	
	public void updateConfigOption1(int value){
		//CREATE DATABASE-INSTANCE
		SQLiteDatabase db = this.getWritableDatabase();
		
		//PUT CONFIG-OPTION1
		ContentValues newValues = new ContentValues();
		newValues.put(VALUE, value);
		
		//UPDATE-DATA IN SQLITE
		db.update(TB_NAME_CONFIG, newValues, "ID=0", null);
		db.close();
	}
	
	public void updateConfigOption2(int value){
		//CREATE DATABASE-INSTANCE
		SQLiteDatabase db = this.getWritableDatabase();
		
		//PUT CONFIG-OPTION2
		ContentValues newValues = new ContentValues();
		newValues.put(VALUE, value);
		
		//UPDATE-DATA IN SQLITE
		db.update(TB_NAME_CONFIG, newValues, "ID=1", null);
		db.close();
	}
	
	public int readConfigOption1(){
		//CREATE DATABASE-INSTANCE
		SQLiteDatabase db = this.getReadableDatabase();
		
		//IF OPERATION FAILS, EXIT PER ERROR
		int value = -1;
		
		//GET DATA OF ROW
		String sqlStatement = "SELECT VALUE FROM " + TB_NAME_CONFIG + " WHERE ID = 0";
		Cursor cursor = db.rawQuery(sqlStatement, null);
		
		//GET DATA AND WRITE INTO VALUE
		if (null != cursor && cursor.moveToFirst()) {
		    value = Integer.parseInt(cursor.getString(0));
		}
		
		db.close();
		return value;
		
	}
	
	public int readConfigOption2(){
		//CREATE DATABASE_INSTANCE
		SQLiteDatabase db = this.getReadableDatabase();
		
		//IF OPERATION FAILS, EXIT PER ERROR
		int value = -1;
		
		//GET DATA OF ROW
		String sqlStatement = "SELECT VALUE FROM " + TB_NAME_CONFIG + " WHERE ID = 1";
		Cursor cursor = db.rawQuery(sqlStatement, null);
		
		//GET DATA AND WRITE IT INTO VALUE
		if (null != cursor && cursor.moveToFirst()) {
		    value = Integer.parseInt(cursor.getString(0));
		}
		
		db.close();
		return value;
		
	}
	
	public List<String> getUserList(){
		//ARRAY TO STORE USERS
        List<String> users = new ArrayList<String>();
         
        //SELECT ALL FROM USERS
        String selectQuery = "SELECT  * FROM " + TB_NAME_USER;
      
        //CREATE DATABASE-INSTANCE AND FETCH DATA
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
      
        //LOOPING THROUGH ALLS ROWS AND ADD TO LIST
        if (cursor.moveToFirst()) {
            do {
                users.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
         
        cursor.close();
        db.close();
         
        return users;
    }
	
	 public void insertUser(String user){
		//CREATE DATABASE-INSTANCE
        SQLiteDatabase db = this.getWritableDatabase();
        
        //PREPARE SQL
        ContentValues values = new ContentValues();
        values.put(USER, user);
        values.put(FIRST_START, 0);
          
        //INSERT ROW
        db.insert(TB_NAME_USER, null, values);
        db.close();
	 }
	 
	 public void deleateUser (String user){
		 //CREATE DATABASE-INSTANCE
		 SQLiteDatabase db = this.getWritableDatabase();
		 
		 //PREPARE SQL
		 db.delete(TB_NAME_USER, USER + " = '" + user +"'", null);
		 db.close();
	 }

	
	public boolean FirstStart(String user){
		//CREATE VARIABLE TO STORE DATA
		boolean first_start_boolean = true;
		//CREATE DATABASE-INSTANCE
		SQLiteDatabase db = this.getReadableDatabase();
		
		String selectQuery = "SELECT " + FIRST_START + " FROM " + TB_NAME_USER + " WHERE " + USER + " = '" + user +"'";
		Cursor cursor = db.rawQuery(selectQuery, null);
	      
        //LOOPING THROUGH ALLS ROWS AND ADD TO LIST
		if (null != cursor && cursor.moveToFirst()) {
			if(Integer.parseInt(cursor.getString(0)) == 0){
				first_start_boolean = true;}
			else{
				first_start_boolean = false;
			}
		}
         
        cursor.close();
        db.close();
		
		return first_start_boolean;
	}
	
	public void updateFirstStart(String user){
		//CREATE DATABASE-INSTANCE
		SQLiteDatabase db = this.getWritableDatabase();
		//PUT FIRST-START VALUE
		ContentValues newValues = new ContentValues();
		newValues.put(FIRST_START, 1);
		
		//UPDATE-DATA IN SQLITE
		db.update(TB_NAME_USER, newValues, USER + " = '" + user + "'", null);
		db.close();
		
	}
	

	public void insertCardFileNames(String string) {
		//CREATE DATABASE-INSTANCE
		SQLiteDatabase db = this.getWritableDatabase();
		
		//PREPARE SQL
		ContentValues values = new ContentValues();
        values.put(CARDFILE_NAME, string);
        
        //INSERT ROW
        db.insert(TB_NAME_CARDFILE_NAMES, null, values);
        db.close();
	}
	
	public void insertDataFromXMLToDB(Integer question_id, String user, Long timestamp, String cardfile_name, String answer_type) {
		//CREATE DATABASE-INSTANCE
		SQLiteDatabase db = this.getWritableDatabase();
		
		//PREPARE SQL
		ContentValues values = new ContentValues();
        values.put(QUESTION_ID, question_id);
        values.put(USER, user);
        values.put(LEVEL, 1);
        values.put(EVALUATION_TIMESTAMP, timestamp);
        values.put(CARDFILE_NAME, cardfile_name);
        values.put(ANSWER_TYPE, answer_type);
        values.put(ACTIVE_FLAG, 0);
        
        //INSERT ROW
        db.insert(TB_NAME_CARDS, null, values);
        db.close();
	}
	
	public String getRequiredQuestionIDs(Long timestamp, String cardfile_name, String user){
		//ARRAY TO STORE REQUIRED IDs
        String required_id = new String();
        
        //SORTORDER
        String sort = new String();
        if(readConfigOption1() == 0){ //Ältestes zuerst
        	sort = "ASC";
        }else if(readConfigOption1() == 1){ //Neustes zuerst
        	sort = "DESC";
        }else{
        	sort = "ASC";
        }
         
        //SELECT QUESTION_ID FROM CARDs
        String selectQuery = "SELECT " + QUESTION_ID +" FROM " + TB_NAME_CARDS + " WHERE " + EVALUATION_TIMESTAMP + " < " + timestamp + " AND " + CARDFILE_NAME + " = '" + cardfile_name +"' AND " + USER + " = '" + user + "' AND " + ACTIVE_FLAG + " = 0" + " ORDER BY " + EVALUATION_TIMESTAMP + " " + sort + " LIMIT 1";
      
        //CREATE DATABASE-INSTANCE AND FETCH DATA
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
      
        //LOOPING THROUGH ALLS ROWS AND ADD TO LIST
        if (cursor.moveToFirst()) {
            do {
                required_id = cursor.getString(0);
            } while (cursor.moveToNext());
        }
         
        cursor.close();
        db.close();
         
        return required_id;
		
	}

	public String getAnswerTypeForCertainQuestionID(String questionID) {
		//ARRAY TO STORE REQUIRED IDs
        String answer_type = new String();
         
        //SELECT QUESTION_ID FROM CARDs
        String selectQuery = "SELECT " + ANSWER_TYPE +" FROM " + TB_NAME_CARDS + " WHERE " + QUESTION_ID + " = " + questionID;
      
        //CREATE DATABASE-INSTANCE AND FETCH DATA
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
      
        //LOOPING THROUGH ALLS ROWS AND ADD TO LIST
        if (cursor.moveToFirst()) {
            do {
                answer_type = cursor.getString(0);
            } while (cursor.moveToNext());
        }
         
        cursor.close();
        db.close();
        
		return answer_type;
	}
	
	public void IncreaseOrDecreaseLevelAndSetNewTimestamp(boolean isRight, String questionID, Long ActualTime){
	 
		//CREATE DATABASE-INSCTANCE
		SQLiteDatabase db = this.getWritableDatabase();
		int level = 0;
		int timeToIncrease = 0;
		
		//PREPARE SQL-STATEMENT
		String sqlStatement = "SELECT " + LEVEL + " FROM " + TB_NAME_CARDS + " WHERE " + QUESTION_ID + " = " + questionID;
		Cursor cursor = db.rawQuery(sqlStatement, null);
		
		//GET DATA AND WRITE IT INTO VALUE
		if (null != cursor && cursor.moveToFirst()) {
		    level = Integer.parseInt(cursor.getString(0));
		}
		
		if(level >= 1 && isRight == true){
			level++;
		}else if(level == 1 && isRight == false){
			
		}else if(level == 7 && isRight == true){
			
		}else if(level > 1 && isRight == false){
			level--;
		}
		
		String selectIncreaseTime = "SELECT " + INCREASED_TIME_FOR_LEVEL + " FROM " + TB_NAME_TIMESTAMP + " WHERE " + LEVEL + " = " + level;
		Cursor cursor2 = db.rawQuery(selectIncreaseTime, null);
		
		//GET DATA AND WRITE IT INTO VALUE
		if (null != cursor2 && cursor2.moveToFirst()) {
		    timeToIncrease = Integer.parseInt(cursor2.getString(0))*1000;
		}
		
		ContentValues newValues = new ContentValues();
		newValues.put(EVALUATION_TIMESTAMP, timeToIncrease + ActualTime);
		newValues.put(LEVEL, level);
		
		//UPDATE-DATA IN SQLITE
		db.update(TB_NAME_CARDS, newValues, QUESTION_ID + " = " + questionID + "", null);
		db.close();
		
	}
	
	public int getCurrentLevelForQuestionId (String questionID){
		//CREATE DATABASE-INSCTANCE
		SQLiteDatabase db = this.getWritableDatabase();
		int level = 0;
		
		//PREPARE SQL-STATEMENT
		String sqlStatement = "SELECT " + LEVEL + " FROM " + TB_NAME_CARDS + " WHERE " + QUESTION_ID + " = " + questionID;
		Cursor cursor = db.rawQuery(sqlStatement, null);
		
		//GET DATA AND WRITE IT INTO VALUE
		if (null != cursor && cursor.moveToFirst()) {
		    level = Integer.parseInt(cursor.getString(0));
		}
		return level;	
	}

	public ArrayList<String> getAllQuestionIDsOfUser(String user){
		//ARRAY TO STORE ALL IDs
        ArrayList<String> ids = new ArrayList<String>();
         
        //SELECT QUESTION_ID FROM CARDs
        String selectQuery = "SELECT " + QUESTION_ID +" FROM " + TB_NAME_CARDS + " WHERE " + USER + " = '" + user +"'";
      
        //CREATE DATABASE-INSTANCE AND FETCH DATA
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
      //LOOPING THROUGH ALLS ROWS AND ADD TO LIST
        if (cursor.moveToFirst()) {
            do {
                ids.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        
        return ids;
	}

	public void setCardAsNotActive(Integer questionID, String user) {
			//CREATE DATABASE-INSTANCE
			SQLiteDatabase db = this.getWritableDatabase();
			//PUT FIRST-START VALUE
			ContentValues newValues = new ContentValues();
			newValues.put(ACTIVE_FLAG, 1);
			
			//UPDATE-DATA IN SQLITE
			db.update(TB_NAME_CARDS, newValues, USER + " = '" + user + "' AND " + QUESTION_ID + " = " + questionID + "", null);
			db.close();
			
		}
	
	public void setCardAsActive(Integer questionID, String user) {
		//CREATE DATABASE-INSTANCE
		SQLiteDatabase db = this.getWritableDatabase();
		//PUT FIRST-START VALUE
		ContentValues newValues = new ContentValues();
		newValues.put(ACTIVE_FLAG, 0);
		
		//UPDATE-DATA IN SQLITE
		db.update(TB_NAME_CARDS, newValues, USER + " = '" + user + "' AND " + QUESTION_ID + " = " + questionID + "", null);
		db.close();
		
	}
	
	public boolean getAppStartFlag(){
		//CREATE DATABASE-INSCTANCE
		SQLiteDatabase db = this.getWritableDatabase();
		boolean flag = false;
		
		//PREPARE SQL-STATEMENT
		String sqlStatement = "SELECT " + APP_FIRST_START + " FROM " + TB_NAME_APP_FIRST_START;
		Cursor cursor = db.rawQuery(sqlStatement, null);
		
		//GET DATA AND WRITE IT INTO VALUE
		if (null != cursor && cursor.moveToFirst()) {
			int iflag = Integer.parseInt(cursor.getString(0)); 
			if( iflag == 0){
				flag = true;
				String UPDATE_FIRST_START = "UPDATE " + TB_NAME_APP_FIRST_START + " SET " + APP_FIRST_START + " = 1";
				db.execSQL(UPDATE_FIRST_START);
			}else{
				flag = false;
			}
		}
		return flag;	
	}
}
