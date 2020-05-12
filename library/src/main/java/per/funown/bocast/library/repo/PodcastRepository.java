package per.funown.bocast.library.repo;

import android.content.Context;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutionException;
import per.funown.bocast.library.entity.Podcast;
import per.funown.bocast.library.entity.dao.PodcastDao;
import per.funown.bocast.library.entity.dao.PodcastDatabase;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/05/09
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class PodcastRepository {

  private PodcastDao dao;

  public PodcastRepository(Context context) {
    dao = PodcastDatabase.getInstance(context).getPodcastDao();
  }

  public LiveData<List<Podcast>> getAll() {
    return dao.getAllPodcasts();
  }

  public Podcast getPodcastById(long id) {
    try {
      return new GetPodcastByIdAsyncTask(dao).execute(id).get();
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return null;
  }

  public Podcast getPodcastByRss(String rss) {
    try {
      return new GetPodcastByRssAsyncTask(dao).execute(rss).get();
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void deletePodcast(Podcast podcast) {
    new DeletePodcastAsyncTask(dao).execute(podcast);
  }

  public long addPodcast(Podcast podcast) {
    try {
      return new AddPodcastAsyncTask(dao).execute(podcast).get()[0];
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return 0;
  }

  class GetPodcastByIdAsyncTask extends AsyncTask<Long, Void, Podcast> {
    private PodcastDao dao;

    public GetPodcastByIdAsyncTask(PodcastDao dao) {
      this.dao = dao;
    }

    @Override
    protected Podcast doInBackground(Long... longs) {
      return dao.selectPodcastById(longs[0]);
    }

    @Override
    protected void onPostExecute(Podcast podcast) {
      super.onPostExecute(podcast);
    }
  }

  class GetPodcastByRssAsyncTask extends AsyncTask<String, Void, Podcast> {
    private PodcastDao dao;

    public GetPodcastByRssAsyncTask(PodcastDao dao) {
      this.dao = dao;
    }

    @Override
    protected Podcast doInBackground(String... strings) {
      return dao.selectPodcastByRss(strings[0]);
    }

    @Override
    protected void onPostExecute(Podcast podcast) {
      super.onPostExecute(podcast);
    }
  }

  class DeletePodcastAsyncTask extends AsyncTask<Podcast, Void, Void> {
    private PodcastDao dao;

    protected DeletePodcastAsyncTask(PodcastDao dao) {
      this.dao = dao;
    }
    @Override
    protected Void doInBackground(Podcast... podcasts) {
      dao.deletePodcasts(podcasts);
      return null;
    }
  }

  class AddPodcastAsyncTask extends AsyncTask<Podcast, Void, long[]> {
    private PodcastDao dao;

    protected AddPodcastAsyncTask(PodcastDao dao) {
      this.dao = dao;
    }
    @Override
    protected long[] doInBackground(Podcast... podcasts) {
      return dao.insertPodcasts(podcasts);
    }

    @Override
    protected void onPostExecute(long[] longs) {
      super.onPostExecute(longs);
    }
  }


}
