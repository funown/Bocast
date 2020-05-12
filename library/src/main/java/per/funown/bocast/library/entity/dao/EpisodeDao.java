package per.funown.bocast.library.entity.dao;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import per.funown.bocast.library.entity.Episode;
import retrofit2.http.DELETE;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/05/10
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@Dao
public interface EpisodeDao {

  @Insert(onConflict = REPLACE)
  long[] InsertEpisode(Episode... episodes);

  @Update
  void UpdateEpisode(Episode... episodes);

  @Delete
  void deleteEpisode(Episode... episodes);

  @Query("SELECT * FROM Episode")
  LiveData<List<Episode>> getEpisodes();

  @Query("SELECT * FROM Episode WHERE title LIKE :title LIMIT 1")
  Episode getEpisodeByTitle(String title);

  @Query("SELECT * FROM Episode WHERE id = :id LIMIT 1")
  Episode getEpisodeById(long id);
}
