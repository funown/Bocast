package per.funown.bocast.library.repo;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
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

  private static final String TAG = DownloadedEpisodeRepository.class.getSimpleName();
  private DownloadedEpisodeDao dao;
  private LiveData<List<DownloadEpisode>> episodes;

  public DownloadedEpisodeRepository(Context context) {
    dao = DownloadedEpisodeDatabase.getINSTANCE(context).getDownloadedEpisodeDao();
    episodes = dao.getLiveDataAll();
  }

  public String isDownloaded(String guid, String podcastId) {
    List<DownloadEpisode> episodeList = null;
    try {
      episodeList = new GetAllAsyncTask(dao).execute().get();
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    if (episodeList != null) {
      for (DownloadEpisode episode : episodeList) {
        if (episode.getGuid().equals(guid) && episode.getPodcastId().equals(podcastId)) {
          return episode.getFilename();
        }
      }
    }
    return null;
  }

  public DownloadEpisode getEpisode(String guid, String podcastId) {
    List<DownloadEpisode> episodeList = null;
    try {
      episodeList = new GetAllAsyncTask(dao).execute().get();
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    Log.e(TAG, podcastId + "-->"+guid);
    if (episodeList != null) {
      for (DownloadEpisode episode : episodeList) {
        Log.e(TAG, episode.getPodcastId() + "-->"+episode.getGuid());
        if (episode.getGuid().equals(guid) && episode.getPodcastId().equals(podcastId)) {
          return episode;
        }
      }
    }
    return null;
  }

  public List<DownloadEpisode> getAllDownloadEpisodes() {
    List<DownloadEpisode> episodeList = null;
    try {
      episodeList = new GetAllAsyncTask(dao).execute().get();
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return episodeList;
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
    DownloadEpisode downloadEpisode = null;
    List<DownloadEpisode> downloadEpisodeList = getAllDownloadEpisodes();
    if (downloadEpisodeList != null) {
      for (DownloadEpisode episode : downloadEpisodeList) {
        Log.e(TAG, "-->" + (episode.getUrl()==episodes[0].getUrl()));
        if (episode.getUrl().equals(episodes[0].getUrl())) {
          downloadEpisode = episode;
        }
      }
    }
    if (downloadEpisode == null) {
      new SetDownloadedAsyncTask(dao).execute(episodes);
    }
    else {
      downloadEpisode.setOffset(episodes[0].getOffset());
      downloadEpisode.setStatus(episodes[0].getStatus());
      new UpdateDownloadedAsyncTask(dao).execute(downloadEpisode);
    }
  }

  public void setUnDownloaded(DownloadEpisode... episodes) {
    new SetUnDownloadedAsyncTask(dao).execute(episodes);
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

  public LiveData<List<DownloadEpisode>> getAll(){
    return dao.getLiveDataAll();
  }

  static class GetAllAsyncTask extends AsyncTask<Void, Void, List<DownloadEpisode>> {

    private DownloadedEpisodeDao dao;

    public GetAllAsyncTask(DownloadedEpisodeDao dao) {
      this.dao = dao;
    }

    @Override
    protected List<DownloadEpisode> doInBackground(Void... voids) {
      return dao.getAll();
    }

    @Override
    protected void onPostExecute(List<DownloadEpisode> downloadEpisodes) {
      super.onPostExecute(downloadEpisodes);
    }
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

  static class UpdateDownloadedAsyncTask extends AsyncTask<DownloadEpisode, Void, Void> {

    private DownloadedEpisodeDao dao;

    public UpdateDownloadedAsyncTask(DownloadedEpisodeDao dao) {
      this.dao = dao;
    }

    @Override
    protected Void doInBackground(DownloadEpisode... episodes) {
      dao.updateEpisode(episodes);
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
