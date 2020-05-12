package per.funown.bocast.library.entity.dao;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import per.funown.bocast.library.entity.Episode;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/05/10
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@Database(entities = Episode.class, version = 1, exportSchema = false)
public abstract class EpisodeDatabase extends RoomDatabase {

  private static EpisodeDatabase INSTANCE;

  public static EpisodeDatabase getINSTANCE(Context context) {
    if (INSTANCE == null) {
      INSTANCE = Room
          .databaseBuilder(context.getApplicationContext(), EpisodeDatabase.class, "Episode")
          .allowMainThreadQueries().build();
    }
    return INSTANCE;
  }

  public abstract EpisodeDao getEpisodeDao();
}
