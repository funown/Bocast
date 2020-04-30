package per.funown.bocast.library.entity.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.lifecycle.LiveData;
import java.util.List;
import per.funown.bocast.library.entity.CurrentEpisode;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@Dao
public interface CurrentEpisodeDao {

  @Update
  void updateCurrentEpisode(CurrentEpisode episode);

  @Query("SELECT * FROM CurrentEpisode")
  LiveData<List<CurrentEpisode>> getCurrentEpisode();

  @Insert
  void InsertCurrentEpisode(CurrentEpisode episode);

  @Query("DELETE FROM CurrentEpisode")
  void deleteCurrentEpisode();

}
