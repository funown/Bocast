package per.funown.bocast.library.entity.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import per.funown.bocast.library.R;
import per.funown.bocast.library.entity.Podcast;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/02/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@Database(entities = {Podcast.class}, version = 1, exportSchema = false)
public abstract class PodcastDatabase extends RoomDatabase {
  /**
   * Singleton
   */
  private static PodcastDatabase INSTANCE;

  public static synchronized PodcastDatabase getInstance(Context context) {
    if (INSTANCE == null) {
      INSTANCE = Room.databaseBuilder(context.getApplicationContext(), PodcastDatabase.class,
          context.getString(R.string.Podcast_database_name)).allowMainThreadQueries()
          .build();
    }
    return INSTANCE;
  }

  public abstract PodcastDao getPodcastDao();

}
