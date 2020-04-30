package per.funown.bocast.library.entity.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.lifecycle.LiveData;

import per.funown.bocast.library.entity.Genre;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@Dao
public interface GenreDao {

  @Insert
  void addGenre(Genre... genres);

  @Update
  void updateGenre(Genre... genres);

  @Delete
  void deleteGenre(Genre... genres);

  @Query("SELECT * FROM Genre ORDER by weight DESC")
  LiveData<List<Genre>> getAllGenres();

  @Query("SELECT * FROM Genre ORDER by weight DESC")
  List<Genre> getAll();

  @Query("DELETE FROM Genre")
  void deleteAllGenres();
}
