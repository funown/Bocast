package per.funown.bocast.modules.user.fragment;

import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.alibaba.android.arouter.facade.annotation.Route;
import java.util.ArrayList;
import per.funown.bocast.library.constant.ArouterConstant;
import per.funown.bocast.library.utils.FragmentTransitionUtil;
import per.funown.bocast.library.utils.ItemTouchHelperCallback;
import per.funown.bocast.modules.user.adapter.HistoryCellAdapter;
import per.funown.bocast.modules.user.adapter.HistoryCellAdapter.OnStartDragListener;
import per.funown.bocast.modules.user.databinding.FragmentHistoryBinding;
import per.funown.bocast.modules.user.viewmodel.HistoryViewModel;

@Route(path = ArouterConstant.FRAGMENT_USER_HISTORY)
public class HistoryFragment extends Fragment {

  FragmentHistoryBinding binding;

  HistoryViewModel historyViewModel;
  HistoryCellAdapter historyCellAdapter;

  private ItemTouchHelper mItemTouchHelper;

  public HistoryFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = FragmentHistoryBinding.inflate(getLayoutInflater());
    historyViewModel = new ViewModelProvider(requireActivity()).get(HistoryViewModel.class);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
    binding.getRoot().setLayoutParams(lp);

    binding.getRoot().setClickable(true);
    historyCellAdapter = new HistoryCellAdapter(getContext(), onStartDragListener,
        binding.historyItems, historyViewModel);
    historyCellAdapter.setItems(new ArrayList<>());
    historyCellAdapter.setActivity(requireActivity());
    FragmentTransitionUtil.getINSTANCE().setManager(requireActivity().getSupportFragmentManager());
    historyCellAdapter.setContainerId(binding.getRoot().getId());
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
    linearLayoutManager.setItemPrefetchEnabled(true);
    binding.historyItems.setLayoutManager(linearLayoutManager);
    binding.historyItems.setAdapter(historyCellAdapter);
    historyViewModel.getItems().observe(getViewLifecycleOwner(), historyItems -> {
      if (historyItems != null) {
        historyCellAdapter.setItems(historyItems);
        historyCellAdapter.notifyDataSetChanged();
      }
    });

    ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(historyCellAdapter,
        this.getContext());
    mItemTouchHelper = new ItemTouchHelper(callback);
    mItemTouchHelper.attachToRecyclerView(binding.historyItems);

    return binding.getRoot();
  }

  private HistoryCellAdapter.OnStartDragListener onStartDragListener = new OnStartDragListener() {

    @Override
    public void onStartDrag(ViewHolder viewHolder) {
      mItemTouchHelper.startDrag(viewHolder);
    }
  };

}
