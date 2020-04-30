package per.funown.bocast.library.entity.dao;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import per.funown.bocast.library.R;
import per.funown.bocast.library.entity.SubscribedPodcast;
import per.funown.bocast.library.utils.Converters;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@Database(entities = SubscribedPodcast.class, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class SubscribedPodcastDatabase extends RoomDatabase {
  private static SubscribedPodcastDatabase INSTANCE;

  public static synchronized SubscribedPodcastDatabase getInstance(Context context) {
    if (INSTANCE == null) {
      INSTANCE = Room.databaseBuilder(context.getApplicationContext(), SubscribedPodcastDatabase.class,
          context.getString(R.string.Subscribed_Podcast_database_name)).allowMainThreadQueries()
          .build();
    }
    return INSTANCE;
  }

  public abstract SubscribedPodcastsDao getSubscribedPodcastDao();
}
