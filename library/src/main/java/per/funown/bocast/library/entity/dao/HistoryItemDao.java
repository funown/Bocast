package per.funown.bocast.library.entity.dao;

import java.util.List;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.lifecycle.LiveData;
import per.funown.bocast.library.entity.HistoryItem;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/30
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@Dao
public interface HistoryItemDao {

  @Insert
  void addHistory(HistoryItem... items);

  @Update
  void updateHistory(HistoryItem... items);

  @Delete
  void deleteHistory(HistoryItem... items);

  @Query("SELECT * FROM HistoryItem")
  LiveData<List<HistoryItem>> getAll();

  @Query("DELETE FROM HistoryItem")
  void deleteAll();

  @Query("DELETE FROM HistoryItem")
  void dropTable();

  @Query("SELECT * FROM HistoryItem WHERE episodeId=:episodeId")
  HistoryItem getById(long episodeId);
}
