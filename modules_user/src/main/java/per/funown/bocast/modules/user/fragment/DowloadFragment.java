package per.funown.bocast.modules.user.fragment;

import android.os.Bundle;
import android.widget.Adapter;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.alibaba.android.arouter.facade.annotation.Route;
import per.funown.bocast.library.constant.ArouterConstant;
import per.funown.bocast.modules.user.R;
import per.funown.bocast.modules.user.adapter.DownloadCellAdapter;
import per.funown.bocast.modules.user.databinding.FragmentDowloadBinding;
import per.funown.bocast.modules.user.viewmodel.DownloadViewModel;

@Route(path = ArouterConstant.FRAGMENT_USER_DOWNLOAD)
public class DowloadFragment extends Fragment {

  DownloadCellAdapter downloadCellAdapter;

  DownloadViewModel downloadViewModel;
  FragmentDowloadBinding binding;

  public DowloadFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = FragmentDowloadBinding.inflate(getLayoutInflater());
    downloadViewModel = new ViewModelProvider(this).get(DownloadViewModel.class);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding.getRoot().setClickable(true);
    downloadCellAdapter = new DownloadCellAdapter(getContext());
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
    binding.DownloadItems.setLayoutManager(linearLayoutManager);
    binding.DownloadItems.setAdapter(downloadCellAdapter);
    downloadViewModel.getTasks().observe(getViewLifecycleOwner(), downloadTasks -> {
      downloadCellAdapter.setTasks(downloadTasks);
      downloadCellAdapter.notifyDataSetChanged();
    });
    return binding.getRoot();
  }
}
