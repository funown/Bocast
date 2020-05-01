package per.funown.bocast.library.download;

import android.os.Environment;
import androidx.lifecycle.LiveData;
import com.liulishuo.okdownload.StatusUtil;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

import android.util.Log;
import android.content.Context;

import com.liulishuo.okdownload.core.Util;
import com.liulishuo.okdownload.OkDownload;
import com.liulishuo.okdownload.DownloadListener;
import com.liulishuo.okdownload.UnifiedListenerManager;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.DownloadTask.Builder;

import per.funown.bocast.library.entity.DownloadEpisode;
import per.funown.bocast.library.repo.DownloadedEpisodeRepository;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class DownloadFactory {

  private static final String TAG = DownloadFactory.class.getSimpleName();
  private static DownloadFactory INSTANCE;

  private static File queueDir;
  private static List<DownloadTask> tasks = new ArrayList<>();
  private UnifiedListenerManager manager;
  private static DownloadedEpisodeRepository repository;
  private static Context mContext;
  private static File parentDir;


  public static DownloadFactory getINSTANCE(Context context) {
    mContext = context;
    if (INSTANCE == null) {
      parentDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PODCASTS),
          "bocast");
      Log.i(TAG, "--> " + parentDir.getAbsolutePath() + " - " + parentDir.exists());
      if (!parentDir.exists()) {
        parentDir.mkdir();
      }
      INSTANCE = new DownloadFactory(context, parentDir);
      repository = new DownloadedEpisodeRepository(context);
      LiveData<List<DownloadEpisode>> undownloadedEpisodes = repository.getUndownloadedEpisodes();
      if (undownloadedEpisodes.getValue() != null) {
        undownloadedEpisodes.getValue().forEach(episode -> {
          BreakpointInfo currentInfo = StatusUtil
              .getCurrentInfo(episode.getUrl(), parentDir.getPath(), episode.getFilename());
          DownloadTask task = new Builder(episode.getUrl(), queueDir)
              .setFilename(episode.getFilename())
              .setMinIntervalMillisCallbackProcess(150)
              .setPassIfAlreadyCompleted(true)
              .build();
          tasks.add(task);
        });
      }
    }
    return INSTANCE;
  }

  private DownloadFactory(Context context, File parentDir) {
    // TODO: remove this before release
    Util.enableConsoleLog();
    DownloadUtil.getINSTANCE(context);
    this.queueDir = parentDir;
    manager = new UnifiedListenerManager();
  }

  public int getTasksAccount() {
    return tasks.size();
  }

  public File getQueueDir() {
    return queueDir;
  }

  public List<DownloadTask> getTasks() {
    return tasks;
  }

  public DownloadTask getTask(String url) {
    for (DownloadTask task : tasks) {
      if (task.getUrl().equals(url)) {
        return task;
      }
    }
    return null;
  }

  public synchronized void addTask(String url, String filename, DownloadEpisode episode,
      DownloadListener listener) {
    DownloadTask task = new Builder(url, queueDir).setFilename(filename)
        .setMinIntervalMillisCallbackProcess(150)
        .setPassIfAlreadyCompleted(true)
        .build();
    task.setTag(episode);
    tasks.add(task);
    manager.addAutoRemoveListenersWhenTaskEnd(task.getId());
    manager.enqueueTaskWithUnifiedListener(task, listener);
  }

  public void changeListener(DownloadTask task, DownloadListener listener) {
    manager.attachListener(task, listener);
  }

  public void allStart() {
    DownloadTask.enqueue(tasks.toArray(new DownloadTask[tasks.size()]), manager.getHostListener());
  }

  public void allStop() {
    DownloadTask.cancel(tasks.toArray(new DownloadTask[tasks.size()]));
  }

  public void stop(int position) {
    manager.detachListener(tasks.get(position).getId());
    tasks.get(position).cancel();
  }

  public void stop(DownloadTask task) {
    Log.i(TAG, "cancel..." + task.getUrl());
    manager.detachListener(task.getId());
    task.cancel();
  }

  public void restart(String url, BaseDownloadListener listener) {
    DownloadTask task = getTask(url);
    DownloadEpisode downloadEpisode = repository.getDownloadEpisode(url);
    if (task != null) {
      manager.enqueueTaskWithUnifiedListener(task, listener);
    } else {
      if (downloadEpisode != null) {
        DownloadTask newTask = new Builder(url, queueDir).setFilename(downloadEpisode.getFilename())
            .setMinIntervalMillisCallbackProcess(150)
            .setPassIfAlreadyCompleted(true)
            .build();
        manager.enqueueTaskWithUnifiedListener(newTask, listener);
      }
    }
  }

  public void remove(int position, boolean removeFile) {
    DownloadTask task = tasks.remove(position);
    task.cancel();
    OkDownload.with().breakpointStore().remove(task.getId());
    manager.detachListener(task.getId());
    if (removeFile) {
      File file = task.getFile();
      if (file.delete()) {
        Log.d(TAG, String.format("%s has been deleted!", file.getAbsolutePath()));
      } else {
        throw new RuntimeException(String.format("%s deletion failed!"));
      }
    }
  }


}
