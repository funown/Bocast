package per.funown.bocast.modules.discover.fragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.alibaba.android.arouter.facade.annotation.Route;
import java.util.List;
import java.util.regex.Pattern;
import per.funown.bocast.library.constant.ArouterConstant;
import per.funown.bocast.library.utils.FragmentTransitionUtil;
import per.funown.bocast.modules.discover.R;
import per.funown.bocast.modules.discover.databinding.FragmentDiscoverBinding;
import per.funown.bocast.modules.discover.fragment.adapter.SearchResultAdapter;
import per.funown.bocast.modules.discover.viewModel.DiscoverViewModel;

@Route(path = ArouterConstant.FRAGMENT_DISCOVER)
public class DiscoverFragment extends Fragment {

  private static final String TAG = DiscoverFragment.class.getSimpleName();
  FragmentDiscoverBinding binding;
  SearchResultAdapter adapter;

  DiscoverViewModel viewModel;

  public DiscoverFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    viewModel = new ViewModelProvider(getActivity()).get(DiscoverViewModel.class);
    binding = FragmentDiscoverBinding.inflate(getLayoutInflater());

//    binding.Searchbar.setOnSearchClickListener(v -> {
//      Log.e(TAG, "Search...");
//      String query = String.valueOf(binding.Searchbar.getQuery());
//      if (Pattern.matches("(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]",
//          query)) {
//        viewModel.SearchRss(query);
//      }
//      else {
//        viewModel.SearchTerms(query);
//      }
//    });

    binding.Searchbar.setOnQueryTextListener(new OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        Log.e(TAG, "Search...");
        if (Pattern
            .matches("(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]",
                query)) {
          viewModel.SearchRss(query);
        } else {
          viewModel.SearchTerms(query);
        }
        binding.Searchbar.clearFocus();
        return true;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        return false;
      }
    });

    binding.Searchbar.setOnCloseListener(new OnCloseListener() {
      @Override
      public boolean onClose() {
        viewModel.clearSearch();
        adapter.notifyDataSetChanged();
        return true;
      }
    });

    FragmentTransitionUtil.getINSTANCE().setManager(requireActivity().getSupportFragmentManager());
    adapter = new SearchResultAdapter();
    adapter.setContainerId(binding.getRoot().getId());
    LinearLayoutManager manager = new LinearLayoutManager(getContext());
    binding.ResultPanel.setLayoutManager(manager);
    binding.ResultPanel.setAdapter(adapter);
    viewModel.getItunesResponseEntityList().observe(getViewLifecycleOwner(),
        itunesResponseEntities -> {
          adapter.setItunesRespones(itunesResponseEntities);
          adapter.notifyDataSetChanged();
        });
    viewModel.getRssSearch().observe(getViewLifecycleOwner(), feed -> {
      adapter.setResponse(feed);
      adapter.notifyDataSetChanged();
    });

    // Inflate the layout for this fragment
    return binding.getRoot();
  }
}
