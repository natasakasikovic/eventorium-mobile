package com.eventorium.presentation.solution.fragments.product;

import static java.util.stream.Collectors.toList;

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
import android.widget.TextView;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.auth.models.ChatUserDetails;
import com.eventorium.data.auth.models.UserDetails;
import com.eventorium.data.solution.models.product.Product;
import com.eventorium.databinding.FragmentProductDetailsBinding;
import com.eventorium.presentation.chat.fragments.ChatFragment;
import com.eventorium.presentation.company.fragments.CompanyDetailsFragment;
import com.eventorium.presentation.solution.viewmodels.ProductViewModel;
import com.eventorium.presentation.shared.models.ImageItem;
import com.eventorium.presentation.shared.adapters.ImageAdapter;
import com.eventorium.presentation.user.fragments.UserProfileFragment;
import com.google.android.material.button.MaterialButton;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProductDetailsFragment extends Fragment {

    private FragmentProductDetailsBinding binding;
    private ProductViewModel productViewModel;

    public static final String ARG_ID = "ARG_PRODUCT_ID";
    private MaterialButton favouriteButton;
    private boolean isFavourite;
    private Long id;
    private UserDetails provider;
    private Long companyId;

    public ProductDetailsFragment() {
    }

    public static ProductDetailsFragment newInstance(Long id) {
        ProductDetailsFragment fragment = new ProductDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            id = getArguments().getLong(ARG_ID);
        }
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false);
        renderButtons();

        favouriteButton = binding.favButton;
        productViewModel.getProduct(id).observe(getViewLifecycleOwner(), this::loadProductDetails);

        productViewModel.isFavourite(id).observe(getViewLifecycleOwner(), result -> {
            isFavourite = result;
            favouriteButton.setIconResource(
                    result
                            ? R.drawable.ic_favourite
                            : R.drawable.ic_not_favourite
            );
        });

        favouriteButton.setOnClickListener(v -> handleIsFavourite());
        binding.chatButton.setOnClickListener(v -> navigateToChat());
        binding.providerButton.setOnClickListener(v -> navigateToProvider());
        binding.companyButton.setOnClickListener(v -> navigateToCompany());
        return binding.getRoot();
    }

    private void navigateToCompany() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
        Bundle args = new Bundle();
        args.putLong(CompanyDetailsFragment.ARG_COMPANY_ID, companyId);
        navController.navigate(R.id.action_productDetails_to_company, args);
    }

    private void navigateToProvider() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
        Bundle args = new Bundle();
        args.putLong(UserProfileFragment.ARG_ID, provider.getId());
        navController.navigate(R.id.action_productDetails_to_provider, args);
    }

    private void renderButtons() {
        String role = productViewModel.getUserRole();
        if(role == null || role.isEmpty()) {
            binding.favButton.setVisibility(View.GONE);
            binding.chatButton.setVisibility(View.GONE);
            changeMargin();
            return;
        }
        if(!role.equals("EVENT_ORGANIZER")) {
            binding.chatButton.setVisibility(View.GONE);
        }
    }

    private void changeMargin() {
        TextView productTextView = binding.productTextView;
        float density = getResources().getDisplayMetrics().density;
        int leftMargin = (int) (20 * density);

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) productTextView.getLayoutParams();
        layoutParams.setMargins(leftMargin, layoutParams.topMargin, layoutParams.rightMargin, layoutParams.bottomMargin);

        productTextView.setLayoutParams(layoutParams);
    }

    private void navigateToChat() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
        Bundle args = new Bundle();
        args.putParcelable(ChatFragment.ARG_RECIPIENT, provider);
        navController.navigate(R.id.chatFragment, args);
    }

    @SuppressLint("SetTextI18n")
    private void loadProductDetails(Product product) {
        if (product != null) {
            ChatUserDetails sender = product.getProvider();
            provider = new UserDetails(sender.getId(), sender.getName(), sender.getLastname());
            companyId = product.getCompany().getId();

            binding.productName.setText(product.getName());
            binding.productPrice.setText(product.getPrice().toString());
            binding.productDescription.setText(product.getDescription());
            binding.productCategory.setText("Category: " + product.getCategory().getName());
            binding.rating.setText(product.getRating().toString());
            binding.providerName.setText(product.getProvider().getName() + " " + product.getProvider().getLastname());
            binding.companyName.setText(product.getCompany().getName());

            productViewModel.getProductImages(product.getId()).observe(getViewLifecycleOwner(), images -> {
                binding.images.setAdapter(new ImageAdapter(images.stream().map(ImageItem::new).collect(toList())));
            });
        }
    }

    private void handleIsFavourite() {
        if(isFavourite) {
            productViewModel.removeFavouriteProduct(id).observe(getViewLifecycleOwner(), result -> {
                if(result) {
                    isFavourite = false;
                    favouriteButton.setIconResource(R.drawable.ic_not_favourite);
                    Toast.makeText(
                            requireContext(),
                            R.string.removed_service_from_favourites,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });
        } else {
            productViewModel.addFavouriteProduct(id).observe(getViewLifecycleOwner(), result -> {
                if(result.getError() == null) {
                    isFavourite = true;
                    favouriteButton.setIconResource(R.drawable.ic_favourite);
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.added_product)
                                + binding.productName.getText() + " "
                                + getString(R.string.to_favourites),
                        Toast.LENGTH_SHORT
                    ).show();
                } else {
                    Toast.makeText(
                            requireContext(),
                            result.getError(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}