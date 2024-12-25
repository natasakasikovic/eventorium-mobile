package com.eventorium.presentation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.eventorium.R;
import com.eventorium.data.util.services.NotificationService;
import com.eventorium.databinding.ActivityMainBinding;
import com.eventorium.presentation.auth.viewmodels.LoginViewModel;
import com.eventorium.presentation.util.viewmodels.SplashScreenViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 100;
    private SplashScreenViewModel viewModel;
    private LoginViewModel loginViewModel;
    private ActivityMainBinding binding;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private NavigationView navigationView;
    private NavController navController;

    private String userRole = "GUEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(SplashScreenViewModel.class);

        SplashScreen
                .installSplashScreen(this)
                .setKeepOnScreenCondition(() -> Boolean.TRUE.equals(viewModel.getIsLoading().getValue()));

        super.onCreate(savedInstanceState);

        loginViewModel = provider.get(LoginViewModel.class);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupStatusBarAndToolbar();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_hamburger);
            actionBar.setHomeButtonEnabled(true);
        }
        setupDrawer();

        setContentView(binding.getRoot());
        refresh("GUEST");
    }

    public void refresh(String role) {
        userRole = role;
        setNavigation();
        if (role.equals("GUEST")) hideBottomNavigation();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    private void setupDrawer() {
        drawer = binding.baseLayout.drawerLayout;
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void setupNavController() {
        navController = Navigation.findNavController(this, R.id.fragment_nav_content_main);
        NavGraph navGraph;
        clearMenu();
        setUpMenu(R.menu.user_menu);

        switch (userRole) {
            case "USER" -> navGraph = navController.getNavInflater().inflate(R.navigation.nav_user);

            case "ADMIN" -> {
                navGraph = navController.getNavInflater().inflate(R.navigation.nav_admin);
                setUpMenu(R.menu.admin_menu);
            }

            case "PROVIDER" -> {
                navGraph = navController.getNavInflater().inflate(R.navigation.nav_provider);
                setUpMenu(R.menu.provider_menu);
            }

            case "EVENT_ORGANIZER" -> {
                navGraph = navController.getNavInflater().inflate(R.navigation.nav_organizer);
                setUpMenu(R.menu.organizer_menu);
            }

            default -> {
                hideBottomNavigation();
                clearMenu();
                navGraph = navController.getNavInflater().inflate(R.navigation.nav_guest);
                setUpMenu(R.menu.guest_menu);
            }
        }

        navController.setGraph(navGraph);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void setNavigation() {
        bottomNavigationView = binding.baseLayout.bottomNavigation;
        navigationView = binding.baseLayout.navigationView;

        setupNavController();
        bottomNavigationView.setVisibility(View.VISIBLE);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            navController.popBackStack(R.id.homepageFragment, false);
            if (id == R.id.nav_account) {
                navController.navigate(R.id.accountDetailsFragment);
            } else if (id == R.id.nav_home) {
                navController.navigate(R.id.homepageFragment);
            } else if (id == R.id.nav_favourite) {
                navController.navigate(R.id.favourites);
            } else if (id == R.id.nav_calendar) {
                navController.navigate(R.id.bookingCalendarFragment);
            }
            return true;
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            navController.popBackStack(R.id.homepageFragment, false);
            switch (userRole) {
                case "USER" -> handleUserMenuItemSelection(id);
                case "ADMIN" -> handleAdminMenuItemSelection(id);
                case "EVENT_ORGANIZER" -> handleOrganizerMenuItemSelection(id);
                case "PROVIDER" -> handleProviderMenuItemSelection(id);
                default -> handleGuestMenuItemSelection(id);
            }

            drawer.closeDrawers();
            return true;
        });
    }

    private void handleProviderMenuItemSelection(int id) {
        handleUserMenuItemSelection(id);
        if (id == R.id.nav_services) {
            navController.navigate(R.id.manageServicesFragment);
        } else if (id == R.id.nav_company) {
            navController.navigate(R.id.companyDetailsFragment);
        } else if (id == R.id.nav_new_service)
            navController.navigate(R.id.createServiceFragment);
    }

    private void handleOrganizerMenuItemSelection(int id) {
        handleUserMenuItemSelection(id);
    }

    private void handleAdminMenuItemSelection(int id) {
        handleUserMenuItemSelection(id);
        if (id == R.id.nav_create_event_type) {
            navController.navigate(R.id.createEventTypeFragment);
        } else if (id == R.id.nav_create_category) {
            navController.navigate(R.id.createCategoryFragment);
        } else if (id == R.id.nav_categories) {
            navController.navigate(R.id.createCategoryFragment);
        } else if (id == R.id.nav_category_proposals) {
            navController.navigate(R.id.categoryProposalsFragment);
        }
    }

    private void handleUserMenuItemSelection(int id) {
        if (id == R.id.nav_logout) {
            logOutUser();
        } else if (id == R.id.nav_notification) {
            // TODO: navigate to notifications fragment
            Toast.makeText(MainActivity.this, "Add navigation in MainActivity.java :)", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_messages) {
            // TODO: navigate to messages fragment
            Toast.makeText(MainActivity.this, "Add navigation in MainActivity.java :)", Toast.LENGTH_LONG).show();
        }
    }

    private void handleGuestMenuItemSelection(int id) {
        if (id == R.id.nav_login) {
            navController.navigate(R.id.loginFragment);
        } else if (id == R.id.nav_signup) {
            navController.navigate(R.id.registerFragment);
        }
    }

    private void logOutUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        refresh("GUEST");
        loginViewModel.closeWebSocket();
    }

    private void hideBottomNavigation() {
        bottomNavigationView.setVisibility(View.GONE);
    }

    private void clearMenu() {
        navigationView.getMenu().clear();
    }

    private void setupStatusBarAndToolbar() {
        toolbar = binding.baseLayout.toolbar;
        setSupportActionBar(toolbar);

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.md_theme_secondaryContainer));

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
    }

    private void setUpMenu(int menuId) {
        navigationView.inflateMenu(menuId);
    }
}