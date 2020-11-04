package per.funown.bocast.library.entity.dao;

import androidx.room.Update;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Insert;

import androidx.lifecycle.LiveData;

import per.funown.bocast.library.entity.DownloadEpisode;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@Dao
public interface DownloadedEpisodeDao {

  @Insert
  void addEpisode(DownloadEpisode... episodes);

  @Delete
  void deleteEpisode(DownloadEpisode... episodes);

  @Update
  void updateEpisode(DownloadEpisode... episodes);

  @Query("SELECT * FROM DownloadEpisode")
  LiveData<List<DownloadEpisode>> getLiveDataAll();

  @Query("SELECT * FROM DownloadEpisode")
  List<DownloadEpisode> getAll();

  @Query("DELETE FROM DownloadEpisode")
  void deleteAll();

  @Query("SELECT * FROM DownloadEpisode WHERE status NOT LIKE 'COMPLETED'")
  LiveData<List<DownloadEpisode>> getUndownloadedEpisode();

  @Query("SELECT * FROM DownloadEpisode WHERE status LIKE 'COMPLETED'")
  LiveData<List<DownloadEpisode>> getdownloadedEpisode();

  @Query("SELECT * FROM DownloadEpisode WHERE url LIKE :url LIMIT 1")
  DownloadEpisode getEpisode(String url);
}
