package edu.vlsu.taskplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Set;

import edu.vlsu.taskplanner.settings.Theme;
import edu.vlsu.taskplanner.tasks.Task;
import edu.vlsu.taskplanner.tasks.TaskList;

public class Database extends  SQLiteOpenHelper{
    static String name = "database.db";
    static int version = 1;

    static String ID_COLUMN = "id";
    static String NAME_COLUMN = "displayName";
    static String DESCRIPTION_COLUMN = "description";
    static String START_DATE_COLUMN = "startDate";
    static String ALARM_NEEDED = "alarmNeeded";
    static String GROUP_COLUMN = "task_group";
    static String CREATION_TIME_COLUMN = "create_time";

    private static Database instance;

    public static Database getInstance() {
        return instance;
    }

    public static void createInstance(Context context){
        instance = new Database(context);
    }

    private Database(Context context){
        super(context, name, null, version);

        onCreate(getWritableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS tasks (" +
                    ID_COLUMN + " INTEGER PRIMARY KEY," +
                    NAME_COLUMN + " TEXT," +
                    DESCRIPTION_COLUMN + " TEXT," +
                    START_DATE_COLUMN + " INTEGER," +
                    ALARM_NEEDED + " TEXT," +
                    GROUP_COLUMN + " TEXT," +
                    CREATION_TIME_COLUMN + " INTEGER" +
                    ")");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Settings " +
                    "(id INTEGER PRIMARY KEY," +
                    "NAME TEXT," +
                    "VALUE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tasks");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Settings");
        onCreate(sqLiteDatabase);
    }

    public void writeTaskToDB(Task task){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_COLUMN, task.getId());
        contentValues.put(NAME_COLUMN, task.getDisplayName());
        contentValues.put(DESCRIPTION_COLUMN, task.getDescription());
        if (task.getStartTime() != null) {
            contentValues.put(START_DATE_COLUMN, task.getStartTime().getTime().getTime());
        }
        else
            contentValues.put(START_DATE_COLUMN, -1);

        contentValues.put(ALARM_NEEDED, task.isAlarmNeeded());
        contentValues.put(GROUP_COLUMN, task.getTaskGroup().systemName);
        contentValues.put(CREATION_TIME_COLUMN, task.getTimeOfCreation().getTime().getTime());

        if (TaskList.getTaskByName(task.getDisplayName()) == null)
            getWritableDatabase().execSQL("INSERT INTO tasks VALUES" + "(" +
                    contentValues.get(ID_COLUMN) + ", '" +
                    contentValues.get(NAME_COLUMN) + "', '" +
                    contentValues.get(DESCRIPTION_COLUMN) + "', " +
                    contentValues.get(START_DATE_COLUMN) + ", " +
                    contentValues.get(ALARM_NEEDED) + ", '" +
                    contentValues.get(GROUP_COLUMN) + "', " +
                    contentValues.get(CREATION_TIME_COLUMN) +
                    ");");

        else
            getWritableDatabase().update("tasks", contentValues, "id = " + task.getId(), new String[]{});
    }

    public void writeSettings(){
        getWritableDatabase().execSQL("DELETE FROM Settings");
        getWritableDatabase().execSQL("INSERT INTO Settings VALUES(0, 'Theme', '" + SettingsController.currentTheme + "')");
    }

    public void loadSettings(){
        Cursor cursor = Database.getInstance().getReadableDatabase().rawQuery("SELECT * FROM Settings",null);
        while (cursor.moveToNext()){
            String theme = cursor.getString(2);
            if (theme.equals("DARK"))
                SettingsController.currentTheme = Theme.DARK;
            else if (theme.equals("LIGHT"))
                SettingsController.currentTheme = Theme.LIGHT;
        }
    }
}
