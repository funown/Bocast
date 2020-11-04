package per.funown.bocast.library.entity.dao;

import androidx.room.Room;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import android.content.Context;

import per.funown.bocast.library.entity.CurrentEpisode;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@Database(entities = {CurrentEpisode.class}, version = 1, exportSchema = false)
public abstract class CurrentEpisodeDatabase extends RoomDatabase {

  /**
   * Singleton
   */
  private static CurrentEpisodeDatabase INSTANCE;

  public static CurrentEpisodeDatabase getINSTANCE(Context context) {
    if (INSTANCE == null) {
      INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CurrentEpisodeDatabase.class,
          "CurrentEpisode").build();
    }
    return INSTANCE;
  }

  public abstract CurrentEpisodeDao getCurrentEpisodeDao();

}
