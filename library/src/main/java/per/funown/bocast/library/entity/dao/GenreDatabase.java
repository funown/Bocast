package per.funown.bocast.library.entity.dao;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import per.funown.bocast.library.R;
import per.funown.bocast.library.entity.Genre;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@Database(entities = Genre.class, version = 1, exportSchema = false)
public abstract class GenreDatabase extends RoomDatabase {
  private static GenreDatabase INSTANCE;

  public static synchronized GenreDatabase getInstance(Context context) {
    if (INSTANCE == null) {
      INSTANCE = Room.databaseBuilder(context.getApplicationContext(), GenreDatabase.class,
          context.getString(R.string.Podcast_Genre_Database)).allowMainThreadQueries()
          .build();
    }
    return INSTANCE;
  }

  public abstract GenreDao getGenreDao();
}
