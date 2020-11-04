package per.funown.bocast.modules.home.model;

import android.app.Application;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import androidx.lifecycle.MutableLiveData;
import java.util.HashMap;
import java.util.List;

import java.util.Map;
import per.funown.bocast.library.entity.Genre;
import per.funown.bocast.library.model.ItunesResponseEntity;
import per.funown.bocast.library.net.NetworkState;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/01
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class PodcastViewModel extends AndroidViewModel {

  private static final int PAGE_SIZE = 12;
  private static final String TAG = PodcastViewModel.class.getSimpleName();

  private LiveData<Map<Genre, MutableLiveData<List<ItunesResponseEntity>>>> topPodcasts;

  private Application application;
  private PodcastRepository podcastRepository;
  private GenreRepository genreRepository;

  private int totalPage = 1;
  private int currentPage = 1;
  private MutableLiveData<NetworkState> networkState;
  private MutableLiveData<DataStatus> dataStatus = new MutableLiveData<>();

  public enum DataStatus {
    DATA_STATUS_CAN_LOAD_MORE, DATA_STATUS_NO_MORE, DATA_STATUS_NETWORK_ERROR
  }

  public PodcastViewModel(@NonNull Application application) {
    super(application);
    this.application = application;
    currentPage = 1;
    genreRepository = new GenreRepository(application);
    podcastRepository = new PodcastRepository(genreRepository.getGenres().getValue());
    totalPage = genreRepository.getGenres().getValue() == null ? 1
        : genreRepository.getGenres().getValue().size();
    networkState = new MutableLiveData<>(podcastRepository.getNetworkState());
  }

  public LiveData<List<Genre>> getGenres() {
    return genreRepository.getGenres();
  }

  public LiveData<Map<Genre, MutableLiveData<List<ItunesResponseEntity>>>> getTopPodcasts() {
    return topPodcasts;
  }


  public Map<String, Genre> getMapGenres() {
    List<Genre> genres = genreRepository.getAll();
    Map<String, Genre> genreMap = new HashMap<>();
    if (genres != null) {
      Log.e(TAG, "--> " + genres.size());
      for (Genre genre : genres) {
        genreMap.put(genre.getGenre().trim(), genre);
      }
    }
    return genreMap;
  }

  public LiveData<DataStatus> getDataStatus() {
    return dataStatus;
  }

  public synchronized void fetchTopPodcasts() {
    networkState = new MutableLiveData<>(podcastRepository.getNetworkState());
    if (networkState.getValue().equals(NetworkState.LOADING)) {
      return;
    }
    if (currentPage > totalPage) {
      dataStatus.setValue(DataStatus.DATA_STATUS_NO_MORE);
    }
    podcastRepository.fetchData(currentPage, PAGE_SIZE);
    if (networkState.getValue().equals(NetworkState.FAILED)) {
      dataStatus.setValue(DataStatus.DATA_STATUS_NETWORK_ERROR);
    }
    dataStatus.setValue(DataStatus.DATA_STATUS_CAN_LOAD_MORE);
    currentPage++;
  }

  public void reset(List<Genre> genreList) {
    if (genreList != null) {
      currentPage = 1;
      podcastRepository.setGenreList(genreList);
      totalPage = genreList.size() / PAGE_SIZE;
      networkState = new MutableLiveData<>(podcastRepository.getNetworkState());
      topPodcasts = podcastRepository.getTopPodcasts();
      fetchTopPodcasts();
    }
  }

  public void addGenres(Genre... genres) {
    genreRepository.addGenres(genres);
  }

  public void deleteGenres(Genre... genres) {
    genreRepository.deleteGenres(genres);
  }

  public void addWeight(Genre... genres) {
    genreRepository.addWeight(genres);
  }

  public void cutWeight(Genre... genres) {
    genreRepository.cutWeight(genres);
  }
}
