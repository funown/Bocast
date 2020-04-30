package per.funown.bocast.modules.listener.viewmodel.repository;

import java.util.List;

import android.os.AsyncTask;
import android.content.Context;
import androidx.lifecycle.LiveData;

import per.funown.bocast.library.entity.CurrentEpisode;
import per.funown.bocast.library.entity.dao.CurrentEpisodeDao;
import per.funown.bocast.library.entity.dao.CurrentEpisodeDatabase;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class PlayerViewModelRepository {

  private CurrentEpisodeDao dao;
  private LiveData<List<CurrentEpisode>> currentEpisode;

  public PlayerViewModelRepository(Context context) {
    CurrentEpisodeDatabase instance = CurrentEpisodeDatabase.getINSTANCE(context);
    dao = instance.getCurrentEpisodeDao();
    currentEpisode = dao.getCurrentEpisode();
  }

  public LiveData<List<CurrentEpisode>> getCurrentEpisode() {
    return currentEpisode;
  }

  public void addCurrentEpisode(CurrentEpisode episode) {
    if (currentEpisode.getValue().size() > 0) {
      new UpdateCurrentEpisodeAsyncTask(dao).doInBackground(episode);
    }
    else {
      new AddCurrentEpisodeAsyncTask(dao).doInBackground(episode);
    }
  }

  public void updateCurrentEpisode(CurrentEpisode episode) {
    new UpdateCurrentEpisodeAsyncTask(dao).doInBackground(episode);
  }

  public void deleteCurrentEpisode() {
    new DeleteCurrentEpisodeAsyncTask(dao).doInBackground();
  }

  /**
   *
   */
  static class AddCurrentEpisodeAsyncTask extends AsyncTask<CurrentEpisode, Void, Void> {

    private CurrentEpisodeDao dao;

    public AddCurrentEpisodeAsyncTask(
        CurrentEpisodeDao dao) {
      this.dao = dao;
    }

    @Override
    protected Void doInBackground(CurrentEpisode... currentEpisodes) {
      dao.InsertCurrentEpisode(currentEpisodes[0]);
      return null;
    }
  }

  static class UpdateCurrentEpisodeAsyncTask extends AsyncTask<CurrentEpisode, Void, Void> {
    private CurrentEpisodeDao dao;

    public UpdateCurrentEpisodeAsyncTask(
        CurrentEpisodeDao dao) {
      this.dao = dao;
    }

    @Override
    protected Void doInBackground(CurrentEpisode... currentEpisodes) {
      dao.updateCurrentEpisode(currentEpisodes[0]);
      return null;
    }
  }

  static class DeleteCurrentEpisodeAsyncTask extends  AsyncTask<Void, Void, Void> {
    private CurrentEpisodeDao dao;

    public DeleteCurrentEpisodeAsyncTask(
        CurrentEpisodeDao dao) {
      this.dao = dao;
    }

    @Override
    protected Void doInBackground(Void... voids) {
      dao.deleteCurrentEpisode();
      return null;
    }
  }
}
