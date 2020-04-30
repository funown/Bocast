package per.funown.bocast.library.repo;

import android.content.Context;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutionException;
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

  private DownloadedEpisodeDao dao;
  private LiveData<List<DownloadEpisode>> episodes;

  public DownloadedEpisodeRepository(Context context) {
    dao = DownloadedEpisodeDatabase.getINSTANCE(context).getDownloadedEpisodeDao();
    episodes = dao.getAll();
  }

  public String isDownloaded(String guid, String podcastId) {
    List<DownloadEpisode> episodeList = episodes.getValue();
    for (DownloadEpisode episode : episodeList) {
      if (episode.getGuid().equals(guid) && podcastId == episode.getPodcastId()) {
        return episode.getFilename();
      }
    }
    return null;
  }

  public LiveData<List<DownloadEpisode>> getUndownloadedEpisodes() {
    return dao.getUndownloadedEpisode();
  }


  public LiveData<List<DownloadEpisode>> getdownloadedEpisodes() {
    return dao.getdownloadedEpisode();
  }

  public void setDownloaded(DownloadEpisode... episodes) {
    new SetDownloadedAsyncTask(dao).execute(episodes);
  }

  public void addDownload(DownloadEpisode... episodes) {
    new SetDownloadedAsyncTask(dao).execute(episodes);
  }

  public void setUnDownloaded(DownloadEpisode... episodes) {
    new SetUnDownloadedAsyncTask(dao).doInBackground(episodes);
  }

  public DownloadEpisode getDownloadEpisode(String url) {
    try {
      return new GetDownloadEpisode(dao).execute(url).get();
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void deleteAllDownloaded() {
    new DeleteAllDownloadedAsyncTask(dao).doInBackground();
  }

  static class SetDownloadedAsyncTask extends AsyncTask<DownloadEpisode, Void, Void> {

    private DownloadedEpisodeDao dao;

    public SetDownloadedAsyncTask(DownloadedEpisodeDao dao) {
      this.dao = dao;
    }

    @Override
    protected Void doInBackground(DownloadEpisode... episodes) {
      dao.addEpisode(episodes);
      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
    }
  }

  static class SetUnDownloadedAsyncTask extends AsyncTask<DownloadEpisode, Void, Void> {

    private DownloadedEpisodeDao dao;

    public SetUnDownloadedAsyncTask(DownloadedEpisodeDao dao) {
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

  static class GetDownloadEpisode extends AsyncTask<String, Void, DownloadEpisode> {

    private DownloadedEpisodeDao dao;

    public GetDownloadEpisode(DownloadedEpisodeDao dao) {
      this.dao = dao;
    }

    @Override
    protected DownloadEpisode doInBackground(String... strings) {
      return dao.getEpisode(strings[0]);
    }

    @Override
    protected void onPostExecute(DownloadEpisode episode) {
      super.onPostExecute(episode);
    }
  }
}
