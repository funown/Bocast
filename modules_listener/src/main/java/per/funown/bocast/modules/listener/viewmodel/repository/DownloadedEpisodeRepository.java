package per.funown.bocast.modules.listener.viewmodel.repository;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import androidx.lifecycle.LiveData;
import java.util.List;
import per.funown.bocast.library.entity.DownloadEpisode;
import per.funown.bocast.library.entity.dao.DownloadedEpisodeDao;
import per.funown.bocast.library.entity.dao.DownloadedEpisodeDatabase;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class DownloadedEpisodeRepository {

  private static final String TAG = DownloadedEpisodeRepository.class.getSimpleName();
  private DownloadedEpisodeDao dao;

  public DownloadedEpisodeRepository(Context context) {
    dao = DownloadedEpisodeDatabase.getINSTANCE(context).getDownloadedEpisodeDao();
  }

  public DownloadEpisode getEpisode(long episodeId) {
    List<DownloadEpisode> episodeList = dao.getLiveDataAll().getValue();
    Log.e(TAG, "-->"+(episodeList != null));
    if (episodeList != null) {
      for (DownloadEpisode episode : episodeList) {
        if (episode.getEpisodeId() == episodeId) {
          return episode;
        }
      }
    }
    return null;
  }

  public void AddEpisode(DownloadEpisode... episodes) {
    new AddEpisodesAsyncTask(dao).doInBackground(episodes);
  }

  public void updateEpisode(DownloadEpisode... episodes) {
    new UpdateEpisodesAsyncTask(dao).doInBackground(episodes);
  }

  public void deleteEpisodes(DownloadEpisode... episodes) {
    new DeleteEpisodesAsyncTask(dao).doInBackground(episodes);
  }

  public void deleteAllDownloaded() {
    new DeleteAllDownloadedAsyncTask(dao).doInBackground();
  }

  static class AddEpisodesAsyncTask extends AsyncTask<DownloadEpisode, Void, Void> {

    private DownloadedEpisodeDao dao;

    public AddEpisodesAsyncTask(DownloadedEpisodeDao dao) {
      this.dao = dao;
    }

    @Override
    protected Void doInBackground(DownloadEpisode... episodes) {
      dao.addEpisode(episodes);
      return null;
    }
  }

  static class UpdateEpisodesAsyncTask extends AsyncTask<DownloadEpisode, Void, Void> {

    private DownloadedEpisodeDao dao;

    public UpdateEpisodesAsyncTask(DownloadedEpisodeDao dao) {
      this.dao = dao;
    }

    @Override
    protected Void doInBackground(DownloadEpisode... episodes) {
      dao.updateEpisode(episodes);
      return null;
    }
  }

  static class DeleteEpisodesAsyncTask extends AsyncTask<DownloadEpisode, Void, Void> {

    private DownloadedEpisodeDao dao;

    public DeleteEpisodesAsyncTask(DownloadedEpisodeDao dao) {
      this.dao = dao;
    }

    @Override
    protected Void doInBackground(DownloadEpisode... episodes) {
      dao.deleteEpisode(episodes);
      return null;
    }
  }

  static class DeleteAllDownloadedAsyncTask extends AsyncTask<Void, Void, Void> {

    private DownloadedEpisodeDao dao;

    public DeleteAllDownloadedAsyncTask(DownloadedEpisodeDao dao) {
      this.dao = dao;
    }

    @Override
    protected Void doInBackground(Void... voids) {
      dao.deleteAll();
      return null;
    }
  }
}
