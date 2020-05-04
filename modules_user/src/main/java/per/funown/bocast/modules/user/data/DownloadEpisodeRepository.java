package per.funown.bocast.modules.user.data;

import android.content.Context;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import java.util.List;
import per.funown.bocast.library.entity.DownloadEpisode;
import per.funown.bocast.library.entity.dao.DownloadedEpisodeDao;
import per.funown.bocast.library.entity.dao.DownloadedEpisodeDatabase;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/04/20
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class DownloadEpisodeRepository {

  private DownloadedEpisodeDao dao;

  public DownloadEpisodeRepository(Context context) {
    dao = DownloadedEpisodeDatabase.getINSTANCE(context).getDownloadedEpisodeDao();
  }

  public LiveData<List<DownloadEpisode>> getEpisodes() {
    return dao.getLiveDataAll();
  }

  public String isDownloaded(String guid, String podcastId) {
    List<DownloadEpisode> episodeList = getEpisodes().getValue();
    for (DownloadEpisode episode : episodeList) {
      if (episode.getGuid().equals(guid) && podcastId == episode.getPodcastId()) {
        return episode.getFilename();
      }
    }
    return null;
  }

  public void setDownloaded(DownloadEpisode... episodes) {
    new SetDownloadedAsyncTask(dao).doInBackground(episodes);
  }

  public void setUnDownloaded(DownloadEpisode... episodes) {
    new SetUnDownloadedAsyncTask(dao).doInBackground(episodes);
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

}
