package per.funown.bocast.library.repo;

import android.content.Context;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutionException;
import per.funown.bocast.library.entity.Episode;
import per.funown.bocast.library.entity.dao.EpisodeDao;
import per.funown.bocast.library.entity.dao.EpisodeDatabase;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/05/10
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class EpisodeRepository {

  private EpisodeDao dao;

  public EpisodeRepository(Context context) {
    dao = EpisodeDatabase.getINSTANCE(context).getEpisodeDao();
  }

  public LiveData<List<Episode>> getAll() {
    return dao.getEpisodes();
  }

  public long insertEpisode(Episode episode) {
    try {
      return new InsertEpisodeAsyncTask(dao).execute(episode).get()[0];
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return 0;
  }

  public Episode getEpisodeByTitle(String title) {
    try {
      return new QueryEpisodeByTitleAsyncTask(dao).execute(title).get();
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return null;
  }

  public Episode getEpisodeById(long id) {
    try {
      return new QueryEpisodeByIdAsyncTask(dao).execute(id).get();
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return null;
  }

  class InsertEpisodeAsyncTask extends AsyncTask<Episode, Void, long[]> {

    private EpisodeDao dao;
    public InsertEpisodeAsyncTask(EpisodeDao dao) {
      this.dao = dao;
    }

    @Override
    protected long[] doInBackground(Episode... episodes) {
      return dao.InsertEpisode(episodes);
    }

    @Override
    protected void onPostExecute(long[] longs) {
      super.onPostExecute(longs);
    }
  }

  class UpdateEpisodeAsyncTask extends AsyncTask<Episode, Void, Void> {

    private EpisodeDao dao;
    public UpdateEpisodeAsyncTask(EpisodeDao dao) {
      this.dao = dao;
    }

    @Override
    protected Void doInBackground(Episode... episodes) {
      dao.UpdateEpisode(episodes);
      return null;
    }
  }
  class DeleteEpisodeAsyncTask extends AsyncTask<Episode, Void, Void> {

    private EpisodeDao dao;
    public DeleteEpisodeAsyncTask(EpisodeDao dao) {
      this.dao = dao;
    }

    @Override
    protected Void doInBackground(Episode... episodes) {
      dao.deleteEpisode(episodes);
      return null;
    }
  }

  class QueryEpisodeByTitleAsyncTask extends AsyncTask<String, Void, Episode> {

    private EpisodeDao dao;
    public QueryEpisodeByTitleAsyncTask(EpisodeDao dao) {
      this.dao = dao;
    }

    @Override
    protected Episode doInBackground(String... strings) {
      return dao.getEpisodeByTitle(strings[0]);
    }

    @Override
    protected void onPostExecute(Episode episode) {
      super.onPostExecute(episode);
    }
  }

  class QueryEpisodeByIdAsyncTask extends AsyncTask<Long, Void, Episode> {

    private EpisodeDao dao;
    public QueryEpisodeByIdAsyncTask(EpisodeDao dao) {
      this.dao = dao;
    }

    @Override
    protected Episode doInBackground(Long... longs) {
      return dao.getEpisodeById(longs[0]);
    }

    @Override
    protected void onPostExecute(Episode episode) {
      super.onPostExecute(episode);
    }
  }

}
