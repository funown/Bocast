package per.funown.bocast.library.repo;

import android.os.AsyncTask;
import android.util.Log;
import java.util.Date;
import java.util.List;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.concurrent.ExecutionException;
import per.funown.bocast.library.entity.Podcast;
import per.funown.bocast.library.entity.SubscribedPodcast;
import per.funown.bocast.library.entity.dao.SubscribedPodcastDatabase;
import per.funown.bocast.library.entity.dao.SubscribedPodcastsDao;
import per.funown.bocast.library.repo.PodcastRepository.GetPodcastByIdAsyncTask;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/02/20
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class SubscribedPodcastRepository {

  private static final String TAG = SubscribedPodcastRepository.class.getSimpleName();
  private SubscribedPodcastsDao dao;
  private LiveData<List<SubscribedPodcast>> allSubscribedPodcasts;

  public SubscribedPodcastRepository(Context context) {
    SubscribedPodcastDatabase instance = SubscribedPodcastDatabase
        .getInstance(context.getApplicationContext());
    dao = instance.getSubscribedPodcastDao();
    allSubscribedPodcasts = dao.getAll();
  }

  public LiveData<List<SubscribedPodcast>> getAllPodcasts() {
    return allSubscribedPodcasts;
  }

  public void subscribe(Podcast podcast) {
    Log.e(TAG, podcast.toString());
    SubscribedPodcast subscribedPodcast = new SubscribedPodcast(podcast.getId(), new Date(),
        new Date());
    new SubscribePodcastAsyncTask(dao).execute(subscribedPodcast);
  }

  public void unsubscribe(SubscribedPodcast podcast) {
    new DeleteSubscribedPodcastsAsyncTask(dao).execute(podcast);
  }

  public void updatePodcast(SubscribedPodcast... podcasts) {
    new UpdateSubscribedPodcastsAsyncTask(dao).execute(podcasts);
  }

  public SubscribedPodcast getPodcast(long podcastId) {
    try {
      return new GetSubscribedPodcastsAsyncTask(dao).execute(podcastId).get();
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return null;
  }

  // 订阅
  static class SubscribePodcastAsyncTask extends AsyncTask<SubscribedPodcast, Void, Void> {

    private SubscribedPodcastsDao dao;

    public SubscribePodcastAsyncTask(
        SubscribedPodcastsDao dao) {
      this.dao = dao;
    }

    @Override
    protected Void doInBackground(SubscribedPodcast... podcasts) {
      dao.insertPodcast(podcasts);
      return null;
    }
  }

  static class UpdateSubscribedPodcastsAsyncTask extends AsyncTask<SubscribedPodcast, Void, Void> {
    private SubscribedPodcastsDao dao;

    public UpdateSubscribedPodcastsAsyncTask(SubscribedPodcastsDao dao) {
      this.dao = dao;
    }

    @Override
    protected Void doInBackground(SubscribedPodcast... podcasts) {
      dao.updatePodcast(podcasts);
      return null;
    }
  }

  static class DeleteSubscribedPodcastsAsyncTask extends AsyncTask<SubscribedPodcast, Void, Void> {
    private SubscribedPodcastsDao dao;

    public DeleteSubscribedPodcastsAsyncTask(SubscribedPodcastsDao dao) {
      this.dao = dao;
    }

    @Override
    protected Void doInBackground(SubscribedPodcast... podcasts) {
      dao.deletePodcast(podcasts);
      return null;
    }
  }

  static class DeleteAllSubscribedPodcastsAsyncTask extends AsyncTask<SubscribedPodcast, Void, Void> {
    private SubscribedPodcastsDao dao;

    public DeleteAllSubscribedPodcastsAsyncTask(SubscribedPodcastsDao dao) {
      this.dao = dao;
    }

    @Override
    protected Void doInBackground(SubscribedPodcast... podcasts) {
      dao.deleteAll();
      return null;
    }
  }

  static class GetSubscribedPodcastsAsyncTask extends AsyncTask<Long, Void, SubscribedPodcast> {
    private SubscribedPodcastsDao dao;

    public GetSubscribedPodcastsAsyncTask(SubscribedPodcastsDao dao) {
      this.dao = dao;
    }

    @Override
    protected SubscribedPodcast doInBackground(Long... longs) {
      return dao.getPodcastByPodcastId(longs[0]);
    }

    @Override
    protected void onPostExecute(SubscribedPodcast subscribedPodcast) {
      super.onPostExecute(subscribedPodcast);
    }
  }

}
