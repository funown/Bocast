package per.funown.bocast.modules.home.model;

import java.util.List;

import android.os.AsyncTask;
import android.content.Context;

import androidx.lifecycle.LiveData;

import per.funown.bocast.library.entity.Genre;
import per.funown.bocast.library.entity.dao.GenreDao;
import per.funown.bocast.library.entity.dao.GenreDatabase;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class GenreRepository {

  private static final String TAG = GenreRepository.class.getSimpleName();
  private GenreDao dao;

  public GenreRepository(Context context) {

    GenreDatabase instance = GenreDatabase.getInstance(context);
    dao = instance.getGenreDao();
  }

  public LiveData<List<Genre>> getGenres() {
    return dao.getAllGenres();
  }

  public void addWeight(Genre... genres) {
    new AddWeightAsyncTask(dao).doInBackground(genres);
  }

  public void cutWeight(Genre... genres) {
    new CutWeightAsyncTask(dao).doInBackground(genres);
  }

  public void addGenres(Genre... genres) {
    new AddGenreAsyncTask(dao).doInBackground(genres);
  }

  public void deleteGenres(Genre... genres) {
    new DeleteGenreAsyncTask(dao).doInBackground(genres);
  }

  public List<Genre> getAll() {
    return new GetAllAsyncTask(dao).doInBackground();
  }

  public void deleteAllGenres() {
    new DeleteAllGenreAsyncTask(dao).doInBackground();
  }

  static class GetAllAsyncTask extends AsyncTask<Void, Void, List<Genre>> {
    private GenreDao dao;

    public GetAllAsyncTask(GenreDao dao) {
      this.dao = dao;
    }

    @Override
    protected List<Genre> doInBackground(Void... voids) {
      return dao.getAll();
    }
  }

  static class AddWeightAsyncTask extends AsyncTask<Genre, Void, Void> {
    private GenreDao dao;

    public AddWeightAsyncTask(GenreDao dao) {
      this.dao = dao;
    }

    @Override
    protected Void doInBackground(Genre... genres) {
      for (Genre genre : genres) {
        genre.setWeight(genre.getWeight() + 1);
      }
      dao.updateGenre(genres[0]);
      return null;
    }
  }

  static class CutWeightAsyncTask extends AsyncTask<Genre, Void, Void> {
    private GenreDao dao;

    public CutWeightAsyncTask(GenreDao dao) {
      this.dao = dao;
    }

    @Override
    protected Void doInBackground(Genre... genres) {
      for (Genre genre : genres) {
        genre.setWeight(genre.getWeight() > 0 ? genre.getWeight() - 1 : 0);
      }
      dao.updateGenre(genres);
      return null;
    }
  }

  static class AddGenreAsyncTask extends AsyncTask<Genre, Void, Void> {
    private GenreDao dao;

    public AddGenreAsyncTask(GenreDao dao) {
      this.dao = dao;
    }

    @Override
    protected Void doInBackground(Genre... genres) {
      this.dao.addGenre(genres);
      return null;
    }
  }

  static class DeleteGenreAsyncTask extends AsyncTask<Genre, Void, Void> {
    private GenreDao dao;

    public DeleteGenreAsyncTask(GenreDao dao) {
      this.dao = dao;
    }

    @Override
    protected Void doInBackground(Genre... genres) {
      dao.deleteGenre(genres);
      return null;
    }
  }

  static class DeleteAllGenreAsyncTask extends AsyncTask<Void, Void, Void> {
    private GenreDao dao;

    public DeleteAllGenreAsyncTask(GenreDao dao) {
      this.dao = dao;
    }

    @Override
    protected Void doInBackground(Void... voids) {
      dao.deleteAllGenres();
      return null;
    }
  }


}
