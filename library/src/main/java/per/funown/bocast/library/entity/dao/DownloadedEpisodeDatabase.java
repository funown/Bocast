package per.funown.bocast.library.entity.dao;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import per.funown.bocast.library.entity.DownloadEpisode;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@Database(entities = DownloadEpisode.class, version = 1, exportSchema = false)
public abstract class DownloadedEpisodeDatabase extends RoomDatabase {
  private static DownloadedEpisodeDatabase INSTANCE;

  public synchronized static DownloadedEpisodeDatabase getINSTANCE(Context context) {
    if (INSTANCE == null) {
      INSTANCE = Room
          .databaseBuilder(context.getApplicationContext(), DownloadedEpisodeDatabase.class,
              "DownloadEpisodes").build();
    }
    return INSTANCE;
  }

  public abstract DownloadedEpisodeDao getDownloadedEpisodeDao();
}
