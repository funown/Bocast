package per.funown.bocast.library.entity.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import per.funown.bocast.library.entity.Podcast;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/02/19
 *     desc   : Podcast database access object
 *     version: 1.0
 * </pre>
 */
@Dao
public interface PodcastDao {

  @Insert
  void insertPodcasts(Podcast... podcasts);

  @Update
  void updatePodcasts(Podcast... podcasts);

  @Delete
  void deletePodcasts(Podcast... podcasts);

  @Query("DELETE FROM PODCAST")
  void deleteAllPodcasts();

  @Query("SELECT * FROM PODCAST ORDER BY id DESC")
  LiveData<List<Podcast>> getAllPodcasts();
}
