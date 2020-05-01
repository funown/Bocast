package per.funown.bocast.library.service;

import static com.lzx.starrysky.notification.INotification.CHANNEL_ID;

import android.app.Notification;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LiveData;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.util.ArrayList;
import java.util.List;
import per.funown.bocast.library.model.RssChannel;
import per.funown.bocast.library.model.RssFeed;
import per.funown.bocast.library.entity.SubscribedPodcast;
import per.funown.bocast.library.net.RssCacheUtil;
import per.funown.bocast.library.repo.SubscribedPodcastRepository;
import per.funown.bocast.library.utils.DateUtils;
import per.funown.bocast.library.utils.RssFetchUtils;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/04/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class PollingIntentService extends Worker {

  private static final String TAG = PollingIntentService.class.getSimpleName();
  private SubscribedPodcastRepository repository;
  private LiveData<List<SubscribedPodcast>> allPodcasts;
  String GROUP_KEY_WORK_PODCAST = "per.bocast.UPDATE_PODCAST";
  private volatile List<Notification> newPodcastEpisodesNotifications = new ArrayList<>();
  volatile boolean isUpdated = false;
  Context context;

  public PollingIntentService(@NonNull Context context, @NonNull WorkerParameters workerParams) {
    super(context, workerParams);
    this.context = context;
    repository = new SubscribedPodcastRepository(context);
    allPodcasts = repository.getAllPodcasts();
  }

  @NonNull
  @Override
  public Result doWork() {
    polling();
    return Result.success();
  }

  private void polling() {
    List<SubscribedPodcast> podcasts = allPodcasts.getValue();
    for (SubscribedPodcast podcast : podcasts) {
      RssFeed feed = RssFetchUtils.getFeed(podcast.getRssLink());
      if (feed != null) {
        RssChannel channel = feed.getChannel();
        if (channel.getItems().size() > podcast.getEpisodes()
            || DateUtils.stringToDate(channel.getItems().get(0).getPubDate())
            .after(podcast.getUpdateTime())) {
          RssCacheUtil.cacheFeed(podcast.getRssLink(), feed);
          podcast.setTitle(channel.getTitle());
          podcast.setLogoLink(channel.getImage().getHref());
          podcast.setAuthor(channel.getAuthor());
          podcast.setEpisodes(channel.getItems().size());
          podcast.setUpdateTime(DateUtils.stringToDate(channel.getItems().get(0).getPubDate()));
          repository.updatePodcast(podcast);
          isUpdated = true;

          Notification newEpisodeNotification =
              new NotificationCompat.Builder(context, CHANNEL_ID)
                  .setContentTitle(podcast.getTitle())
                  .setContentText(channel.getItems().get(0).getTitle())
                  .setGroup(GROUP_KEY_WORK_PODCAST)
                  .build();
          newPodcastEpisodesNotifications.add(newEpisodeNotification);
        }
      } else {
        Toast.makeText(context, "Update fail - " + podcast.getTitle(), Toast.LENGTH_SHORT);
      }
    }

    if (isUpdated) {
      Notification summaryNotification =
          new NotificationCompat.Builder(context, CHANNEL_ID)
              .setContentTitle(context.getApplicationInfo().name)
              .setStyle(new NotificationCompat.InboxStyle())
              //specify which group this notification belongs to
              .setGroup(GROUP_KEY_WORK_PODCAST)
              //set this notification as the summary for the group
              .setGroupSummary(true)
              .build();
      NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
      for (int i = 0; i < newPodcastEpisodesNotifications.size(); i++) {
        notificationManager.notify(i + 1, newPodcastEpisodesNotifications.get(i));
      }
      notificationManager.notify(0, summaryNotification);
      isUpdated = false;
    }
  }

  private void runOnUIThread(Runnable runnable) {
    new Handler(Looper.getMainLooper()).post(runnable);
  }
}
