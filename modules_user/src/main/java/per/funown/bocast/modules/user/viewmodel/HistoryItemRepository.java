package per.funown.bocast.modules.user.viewmodel;

import java.util.List;
import android.os.AsyncTask;
import android.content.Context;
import androidx.lifecycle.LiveData;
import per.funown.bocast.library.entity.HistoryItem;
import per.funown.bocast.library.entity.dao.HistoryDatabase;
import per.funown.bocast.library.entity.dao.HistoryItemDao;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/30
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class HistoryItemRepository {

  private HistoryItemDao dao;

  public HistoryItemRepository(Context context) {
    dao = HistoryDatabase.getInstance(context).getHistoryDao();
  }

  public HistoryItemDao getDao() {
    return dao;
  }

  public LiveData<List<HistoryItem>> getItems() {
    return dao.getAll();
  }

  public void addHistory(HistoryItem... items) {
    new AddHistoryAsyncTask(dao).doInBackground(items);
  }

  public void updateHistory(HistoryItem... items) {
    new UpdateHistoryAsyncTask(dao).doInBackground(items);
  }

  public void deleteHistory(HistoryItem... items) {
    new DeleteHistoryAsyncTask(dao).doInBackground(items);
  }

  public void deleteAllHistory() {
    new DeleteAllHistoryAsyncTask(dao).doInBackground();
  }

  static class AddHistoryAsyncTask extends AsyncTask<HistoryItem, Void, Void> {

    private HistoryItemDao dao;

    public AddHistoryAsyncTask(HistoryItemDao dao) {
      this.dao = dao;
    }

    @Override
    protected Void doInBackground(HistoryItem... items) {
      dao.addHistory(items);
      return null;
    }
  }

  static class UpdateHistoryAsyncTask extends AsyncTask<HistoryItem, Void, Void> {

    private HistoryItemDao dao;

    public UpdateHistoryAsyncTask(HistoryItemDao dao) {
      this.dao = dao;
    }

    @Override
    protected Void doInBackground(HistoryItem... items) {
      dao.updateHistory(items);
      return null;
    }
  }

  static class DeleteHistoryAsyncTask extends AsyncTask<HistoryItem, Void, Void> {

    private HistoryItemDao dao;

    public DeleteHistoryAsyncTask(HistoryItemDao dao) {
      this.dao = dao;
    }

    @Override
    protected Void doInBackground(HistoryItem... items) {
      dao.deleteHistory(items);
      return null;
    }
  }

  static class DeleteAllHistoryAsyncTask extends AsyncTask<Void, Void, Void> {

    private HistoryItemDao dao;

    public DeleteAllHistoryAsyncTask(HistoryItemDao dao) {
      this.dao = dao;
    }

    @Override
    protected Void doInBackground(Void... voids) {
      dao.deleteAll();
      return null;
    }
  }

}
