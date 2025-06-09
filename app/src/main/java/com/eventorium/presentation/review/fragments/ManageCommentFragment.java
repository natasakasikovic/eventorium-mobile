package com.eventorium.presentation.review.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.auth.models.UserDetails;
import com.eventorium.data.interaction.models.comment.Comment;
import com.eventorium.data.interaction.models.review.ReviewType;
import com.eventorium.data.shared.models.Result;
import com.eventorium.data.shared.models.Status;
import com.eventorium.databinding.FragmentManageCommentBinding;
import com.eventorium.presentation.event.fragments.EventDetailsFragment;
import com.eventorium.presentation.review.adapters.CommentAdapter;
import com.eventorium.presentation.review.listeners.OnManageCommentListener;
import com.eventorium.presentation.review.viewmodels.CommentViewModel;
import com.eventorium.presentation.solution.fragments.product.ProductDetailsFragment;
import com.eventorium.presentation.solution.fragments.service.ServiceDetailsFragment;
import com.eventorium.presentation.user.fragments.UserProfileFragment;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ManageCommentFragment extends Fragment {

    private FragmentManageCommentBinding binding;
    private CommentViewModel commentViewModel;
    private CommentAdapter adapter;

    public ManageCommentFragment() {
    }
    public static ManageCommentFragment newInstance() {
        return new ManageCommentFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        commentViewModel = new ViewModelProvider(this).get(CommentViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding  = FragmentManageCommentBinding.inflate(inflater, container, false);
        adapter = new CommentAdapter(new ArrayList<>(), configureAdapter());
        binding.commentsRecycleView.setAdapter(adapter);

        loadComments();
        return binding.getRoot();
    }


    private OnManageCommentListener configureAdapter() {
        return new OnManageCommentListener() {
            @Override
            public void navigateToProvider(UserDetails provider) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                Bundle args = new Bundle();
                args.putLong(UserProfileFragment.ARG_ID, provider.getId());
                navController.navigate(R.id.otherProfileFragment, args);
            }

            @Override
            public void navigateToCommentable(ReviewType type, Long id) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                Bundle args = new Bundle();
                switch(type) {
                    case PRODUCT -> {
                        args.putLong(ProductDetailsFragment.ARG_ID, id);
                        navController.navigate(R.id.productDetailsFragment, args);
                    }
                    case SERVICE -> {
                        args.putLong(ServiceDetailsFragment.ARG_ID, id);
                        navController.navigate(R.id.serviceDetailsFragment, args);
                    }
                    case EVENT -> {
                        args.putLong(EventDetailsFragment.ARG_EVENT_ID, id);
                        navController.navigate(R.id.eventDetailsFragment, args);
                    }
                }
            }

            @Override
            public void acceptComment(Long id) {
                commentViewModel.updateComment(id, Status.ACCEPTED)
                        .observe(getViewLifecycleOwner(), result -> handleUpdateResult(id, result));
            }

            @Override
            public void declineComment(Long id) {
                commentViewModel.updateComment(id, Status.DECLINED)
                        .observe(getViewLifecycleOwner(), result -> handleUpdateResult(id, result));
            }

            private void handleUpdateResult(Long id, Result<Comment> result) {
                if(result.getError() == null) {
                    Toast.makeText(
                            requireContext(),
                            getString(R.string.successfully_updated_comment),
                            Toast.LENGTH_SHORT
                    ).show();
                    commentViewModel.removeComment(id);
                } else {
                    Toast.makeText(
                            requireContext(),
                            result.getError(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        };
    }

    private void loadComments() {
        commentViewModel.getPendingComments();
        commentViewModel.getComments().observe(getViewLifecycleOwner(), comments -> {
            adapter.setData(comments);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}