package per.funown.bocast.modules.user.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.alibaba.android.arouter.launcher.ARouter;
import per.funown.bocast.library.constant.ArouterConstant;
import per.funown.bocast.library.service.MusicService;
import per.funown.bocast.library.utils.ItemTouchHelperCallback;
import per.funown.bocast.modules.user.adapter.DownloadCellAdapter;
import per.funown.bocast.modules.user.databinding.FragmentDowloadBinding;
import per.funown.bocast.modules.user.viewmodel.DownloadViewModel;

@Route(path = ArouterConstant.FRAGMENT_USER_DOWNLOAD)
public class DowloadFragment extends Fragment {

  DownloadCellAdapter downloadCellAdapter;

  DownloadViewModel downloadViewModel;
  FragmentDowloadBinding binding;
  private ItemTouchHelper mItemTouchHelper;

  public DowloadFragment() {}

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
    downloadCellAdapter.setContainerId(binding.getRoot().getId());
    MusicService service = ARouter.getInstance().navigation(MusicService.class);
    downloadCellAdapter.setService(service);
    ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(downloadCellAdapter,
        this.getContext());
    mItemTouchHelper = new ItemTouchHelper(callback);
    mItemTouchHelper.attachToRecyclerView(binding.DownloadItems);

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
