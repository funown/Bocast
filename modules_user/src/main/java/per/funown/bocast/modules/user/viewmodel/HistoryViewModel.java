package per.funown.bocast.modules.user.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;
import per.funown.bocast.library.entity.HistoryItem;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/30
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class HistoryViewModel extends AndroidViewModel {

  private HistoryItemRepository repository;
  private LiveData<List<HistoryItem>> items;

  public HistoryViewModel(@NonNull Application application) {
    super(application);
    repository = new HistoryItemRepository(application);
    items = repository.getItems();
  }

  public LiveData<List<HistoryItem>> getItems() {
    return items;
  }

  public void addHistory(HistoryItem... items) {
    repository.addHistory(items);
  }

  public void updateHistory(HistoryItem... items) {
    repository.updateHistory(items);
  }

  public void deleteHistory(HistoryItem... items) {
    repository.deleteHistory(items);
  }

  public void deleteAll() {
    repository.deleteAllHistory();
  }
}
