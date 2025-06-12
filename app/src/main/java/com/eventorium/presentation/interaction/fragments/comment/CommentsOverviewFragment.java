package com.eventorium.presentation.interaction.fragments.comment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.data.interaction.models.review.ReviewType;
import com.eventorium.databinding.FragmentCommentsOverviewBinding;
import com.eventorium.presentation.interaction.adapters.CommentAdapter;
import com.eventorium.presentation.interaction.viewmodels.CommentViewModel;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CommentsOverviewFragment extends Fragment {

    private CommentViewModel viewModel;
    private CommentAdapter adapter;
    private FragmentCommentsOverviewBinding binding;
    private ReviewType type;
    private Long objectId;
    public static final String ARG_TYPE = "ARG_TYPE";
    public static final String ARG_ID = "ARG_ITEM_ID";

    public CommentsOverviewFragment() { }

    public static CommentsOverviewFragment newInstance(ReviewType type, long id) {
        CommentsOverviewFragment fragment = new CommentsOverviewFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TYPE, type);
        args.putLong(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = (ReviewType) getArguments().getSerializable(ARG_TYPE);
            objectId = getArguments().getLong(ARG_ID);
        }

        viewModel = new ViewModelProvider(this).get(CommentViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding  = FragmentCommentsOverviewBinding.inflate(inflater, container, false);
        adapter = new CommentAdapter(new ArrayList<>());
        binding.commentsRecycleView.setAdapter(adapter);
        loadComments();
        return binding.getRoot();
    }

    private void loadComments() {
        viewModel.getAcceptedCommentsForTarget(type, objectId).observe(getViewLifecycleOwner(), result -> {
            if (result.getError() == null)
                adapter.setData(result.getData());
        });
    }
}