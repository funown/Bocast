package per.funown.bocast.library.download;

import android.view.View;
import android.widget.ImageView;
import java.util.Map;
import java.util.List;

import android.util.Log;
import android.content.Context;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.SpeedCalculator;
import com.liulishuo.okdownload.core.Util;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.breakpoint.BlockInfo;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.listener.DownloadListener4WithSpeed;
import com.liulishuo.okdownload.core.listener.assist.Listener4SpeedAssistExtend.Listener4SpeedModel;
import me.tankery.lib.circularseekbar.CircularSeekBar;
import per.funown.bocast.library.R;
import per.funown.bocast.library.entity.DownloadEpisode;
import per.funown.bocast.library.repo.DownloadedEpisodeRepository;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/28
 *     desc   : 下载监听
 *     version: 1.0
 * </pre>
 */
public class BaseDownloadListener extends DownloadListener4WithSpeed {

  private static final String TAG = BaseDownloadListener.class.getSimpleName();

  private DownloadEpisode item;
  private long totalLength;
  private String readableTotalLength;
  private CircularSeekBar progressBar;
  private Context context;
  private ImageView btn_download;
  private DownloadedEpisodeRepository repository;

  public BaseDownloadListener() {
  }

  public BaseDownloadListener(DownloadEpisode item, Context context,
      CircularSeekBar progressBar, ImageView btn_download) {
    this.item = item;
    this.context = context;
    this.progressBar = progressBar;
    this.btn_download = btn_download;
    progressBar.setMax((int) item.getTotal());
    repository = new DownloadedEpisodeRepository(context);
  }

  public DownloadEpisode getItem() {
    return item;
  }

  public long getTotalLength() {
    return totalLength;
  }

  public String getReadableTotalLength() {
    return readableTotalLength;
  }

  public CircularSeekBar getProgressBar() {
    return progressBar;
  }

  public void setProgressBar(CircularSeekBar progressBar) {
    this.progressBar = progressBar;
  }

  public void setItem(DownloadEpisode item) {
    this.item = item;
  }

  @Override
  public void taskStart(@NonNull DownloadTask task) {
    Log.i(TAG, String
        .format("[%s] : %s Start Downloading, Priority: %d", task.getId(), task.getFilename(),
            task.getPriority()));
    // 改变下载按钮以及进度条状态
    btn_download.setImageDrawable(context.getDrawable(R.drawable.ic_pause));
    progressBar.setVisibility(View.VISIBLE);
    int checkColor = context.getColor(R.color.colorChecked);
    btn_download.setColorFilter(checkColor);
  }

  @Override
  public void connectStart(@NonNull DownloadTask task, int blockIndex,
      @NonNull Map<String, List<String>> requestHeaderFields) {

  }

  @Override
  public void connectEnd(@NonNull DownloadTask task, int blockIndex, int responseCode,
      @NonNull Map<String, List<String>> responseHeaderFields) {
  }

  /**
   * 设置下载信息
   * @param task
   * @param info
   * @param fromBreakpoint
   * @param model
   */
  @Override
  public void infoReady(@NonNull DownloadTask task, @NonNull BreakpointInfo info,
      boolean fromBreakpoint, @NonNull Listener4SpeedModel model) {
    totalLength = info.getTotalLength();
    readableTotalLength = Util.humanReadableBytes(totalLength, true);
    Log.i(TAG, String.format("[%s] : info ready", task.getId()));
    item.setStatus(DownloadStatus.READY);
    item.setTotal(totalLength);
    progressBar.setMax((int) info.getTotalLength());
    progressBar.setProgress(0);
  }

  @Override
  public void progressBlock(@NonNull DownloadTask task, int blockIndex, long currentBlockOffset,
      @NonNull SpeedCalculator blockSpeed) {
  }

  /**
   * 实时调整进度条进度
   * @param task
   * @param currentOffset
   * @param taskSpeed
   */
  @Override
  public void progress(@NonNull DownloadTask task, long currentOffset,
      @NonNull SpeedCalculator taskSpeed) {
    item.setStatus(DownloadStatus.DOWNLOADING);
    String readableOffset = Util.humanReadableBytes(currentOffset, true);
    String progressStatus = String.format("%s/%s", readableOffset, readableTotalLength);
    String speed = taskSpeed.speed();
    Log.i(TAG,
        "[" + task.getId() + "] : Progress offset " + currentOffset + "[" + progressStatus
            + "], Speed " + speed + ", progress " + currentOffset + "%");
    progressBar.setProgress((int) currentOffset);
    item.setOffset(currentOffset);
  }

  @Override
  public void blockEnd(@NonNull DownloadTask task, int blockIndex, BlockInfo info,
      @NonNull SpeedCalculator blockSpeed) {

  }

  @Override
  public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause,
      @Nullable Exception realCause, @NonNull SpeedCalculator taskSpeed) {
    Log.e(TAG, String.format("[%s] : Task end - cause: %s - %s", task.getId(), cause.name(),
        (realCause != null ? realCause.getMessage() : "Null Exception")));
    // 处理下载完成
    if (cause.equals(EndCause.COMPLETED)) {
      item.setStatus(DownloadStatus.FINISHED);
      progressBar.setVisibility(View.GONE);
      btn_download.setImageDrawable(context.getDrawable(R.drawable.ic_finish));
      btn_download.setClickable(false);
      progressBar = null;
    }
    // 下载停止
    else {
      item.setStatus(DownloadStatus.PAUSE);
      item.setOffset((long) progressBar.getProgress());
      btn_download.setImageDrawable(context.getDrawable(R.drawable.ic_arrow_down));
    }
    // 将该任务信息存入数据库
    repository.addDownload(item);
  }
}
