package per.funown.bocast.modules.listener.viewmodel.repository;

import android.content.Context;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutionException;
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

  public List<HistoryItem> getAllItems() {
    try {
      return new GetAllHistoryAsyncTask(dao).execute().get();
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return null;
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
//      dao.addHistory(items);
      for (HistoryItem item : items) {
        HistoryItem original = dao.getById(item.getEpisodeId());
        if (original != null) {
          original.setPercent(item.getPercent());
          dao.updateHistory(original);
        }
        else {
          dao.addHistory(item);
        }
      }
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

  static class GetAllHistoryAsyncTask extends AsyncTask<Void, Void, List<HistoryItem>> {

    private HistoryItemDao dao;

    public GetAllHistoryAsyncTask(HistoryItemDao dao) {
      this.dao = dao;
    }

    @Override
    protected List<HistoryItem> doInBackground(Void... voids) {
      return dao.getAllHistory();
    }

    @Override
    protected void onPostExecute(List<HistoryItem> historyItems) {
      super.onPostExecute(historyItems);
    }
  }

}
