package com.eventorium.presentation.review.fragments;

import android.annotation.SuppressLint;
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
import com.eventorium.data.interaction.models.review.ReviewType;
import com.eventorium.databinding.FragmentCommentBinding;
import com.eventorium.presentation.review.viewmodels.CommentViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CommentFragment extends Fragment {

    private FragmentCommentBinding binding;
    private CommentViewModel commentViewModel;
    public static final String ARG_COMMENTABLE_ID = "ARG_COMMENTABLE_ID";
    public static final String ARG_TYPE = "ARG_REVIEW_TYPE";
    public static final String ARG_NAME = "ARG_NAME";

    private Long id;
    private ReviewType type;
    private String name;

    public CommentFragment() {
    }

    public static CommentFragment newInstance(Long id, ReviewType type, String name) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_COMMENTABLE_ID, id);
        args.putString(ARG_NAME, name);
        args.putParcelable(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            id = getArguments().getLong(ARG_COMMENTABLE_ID);
            type = getArguments().getParcelable(ARG_TYPE);
            name = getArguments().getString(ARG_NAME);
        }
        commentViewModel = new ViewModelProvider(this).get(CommentViewModel.class);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCommentBinding.inflate(inflater, container, false);
        binding.commentText.setText("Comment " + name);
        binding.commentButton.setOnClickListener(v -> createComment());
        return binding.getRoot();
    }

    private void createComment() {
        String comment = String.valueOf(binding.commentInput.getText());

        if(comment.trim().isEmpty()) {
            showError(getString(R.string.comment_is_mandatory));
            return;
        }

        switch (type) {
            case PRODUCT -> commentProduct(comment);
            case SERVICE -> commentService(comment);
            case EVENT -> commentEvent(comment);
        }
    }

    private void commentService(String comment) {
        commentViewModel.createServiceComment(id, comment).observe(getViewLifecycleOwner(), result -> {
            if(result.getError() == null) {
                onSuccess();
            } else {
                showError(result.getError());
            }
        });
    }

    private void commentProduct(String comment) {
        commentViewModel.createProductComment(id, comment).observe(getViewLifecycleOwner(), result -> {
            if(result.getError() == null) {
                onSuccess();
            } else {
                showError(result.getError());
            }
        });
    }

    private void commentEvent(String comment) {
        commentViewModel.createEventComment(id, comment).observe(getViewLifecycleOwner(), result -> {
            if(result.getError() == null) {
                onSuccess();
            } else {
                showError(result.getError());
            }
        });
    }
    private void onSuccess() {
        Toast.makeText(requireContext(), R.string.successfully_create_comment, Toast.LENGTH_SHORT).show();
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
        navController.popBackStack();
    }

    private void showError(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}