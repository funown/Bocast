package per.funown.bocast.library.entity.dao;

import static androidx.room.OnConflictStrategy.REPLACE;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.lifecycle.LiveData;

import per.funown.bocast.library.entity.SubscribedPodcast;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@Dao
public interface SubscribedPodcastsDao {
  @Insert(onConflict = REPLACE)
  void insertPodcast(SubscribedPodcast... podcasts);
  @Delete
  void deletePodcast(SubscribedPodcast... podcasts);
  @Update
  void updatePodcast(SubscribedPodcast... podcasts);
  @Query("SELECT * FROM SubscribedPodcast ORDER BY id DESC")
  LiveData<List<SubscribedPodcast>> getAll();
  @Query("DELETE FROM SubscribedPodcast")
  void deleteAll();
  @Query("SELECT * FROM SubscribedPodcast WHERE podcastId == :podcastId LIMIT 1")
  SubscribedPodcast getPodcastByPodcastId(long podcastId);

}
