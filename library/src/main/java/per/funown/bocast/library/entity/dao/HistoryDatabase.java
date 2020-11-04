package per.funown.bocast.library.entity.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import per.funown.bocast.library.entity.HistoryItem;
import per.funown.bocast.library.utils.Converters;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/30
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@Database(entities = {HistoryItem.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class HistoryDatabase extends RoomDatabase {

  private static HistoryDatabase ourInstance;

  public synchronized static HistoryDatabase getInstance(Context context) {
    if (ourInstance == null) {
      ourInstance = Room
          .databaseBuilder(context.getApplicationContext(), HistoryDatabase.class, "HistoryItem")
          .allowMainThreadQueries()
          .build();
    }
    return ourInstance;
  }

  public abstract HistoryItemDao getHistoryDao();

  public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
    @Override
    public void migrate(SupportSQLiteDatabase database) {
//      database.execSQL("DROP TABLE HistoryItem");
    }
  };

}
