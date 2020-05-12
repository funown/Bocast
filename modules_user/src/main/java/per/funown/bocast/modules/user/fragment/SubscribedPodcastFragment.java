package per.funown.bocast.modules.user.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.alibaba.android.arouter.facade.annotation.Route;
import java.util.ArrayList;
import per.funown.bocast.library.constant.ArouterConstant;
import per.funown.bocast.library.utils.FragmentTransitionUtil;
import per.funown.bocast.modules.user.adapter.SubscribedPodcastCellAdapter;
import per.funown.bocast.modules.user.databinding.FragmentSubscribedPodcastBinding;
import per.funown.bocast.modules.user.viewmodel.SubscribedViewModel;

@Route(path = ArouterConstant.FRAGMENT_USER_SUBSCRIBED)
public class SubscribedPodcastFragment extends Fragment {

  FragmentSubscribedPodcastBinding binding;
  SubscribedViewModel viewModel;
  SubscribedPodcastCellAdapter adapter;

  public SubscribedPodcastFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    viewModel = new ViewModelProvider(getActivity()).get(SubscribedViewModel.class);
    binding = FragmentSubscribedPodcastBinding.inflate(getLayoutInflater());
    binding.getRoot().setClickable(true);
    adapter = new SubscribedPodcastCellAdapter();
    adapter.setViewModel(viewModel);
    adapter.setPodcasts(new ArrayList<>());
    FragmentTransitionUtil.getINSTANCE().setManager(requireActivity().getSupportFragmentManager());
    adapter.setContainerId(binding.getRoot().getId());
    LinearLayoutManager manager = new LinearLayoutManager(getContext());
    manager.setItemPrefetchEnabled(true);
    binding.SubscribedPanel.setLayoutManager(manager);
    binding.SubscribedPanel.setAdapter(adapter);
    viewModel.getSubscribedPodcastList().observe(getViewLifecycleOwner(),
        subscribedPodcasts -> {
          adapter.setPodcasts(subscribedPodcasts);
          adapter.notifyDataSetChanged();
        });
    // Inflate the layout for this fragment
    return binding.getRoot();
  }
}
