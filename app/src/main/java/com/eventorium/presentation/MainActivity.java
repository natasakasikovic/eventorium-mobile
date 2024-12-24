package com.eventorium.presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.eventorium.R;
import com.eventorium.databinding.ActivityMainBinding;
import com.eventorium.presentation.util.viewmodels.SplashScreenViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private SplashScreenViewModel viewModel;
    private ActivityMainBinding binding;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private NavigationView navigationView;
    private NavController navController;

    private String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(SplashScreenViewModel.class);
        SplashScreen
                .installSplashScreen(this)
                .setKeepOnScreenCondition(() -> Boolean.TRUE.equals(viewModel.getIsLoading().getValue()));

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        userRole = "provider"; // TODO: get user from shared prefs
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
        setNavigation();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (navController.getCurrentDestination() != null &&
                navController.getCurrentDestination().getId() == R.id.homepageFragment) {
            return true;
        }
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
        setUpMenu(R.menu.user_menu);

        switch (userRole) {
            case "user" -> navGraph = navController.getNavInflater().inflate(R.navigation.nav_user);

            case "admin" -> {
                navGraph = navController.getNavInflater().inflate(R.navigation.nav_admin);
                setUpMenu(R.menu.admin_menu);
            }

            case "provider" -> {
                navGraph = navController.getNavInflater().inflate(R.navigation.nav_provider);
                setUpMenu(R.menu.provider_menu);
            }

            case "organizer" -> {
                navGraph = navController.getNavInflater().inflate(R.navigation.nav_organizer);
                setUpMenu(R.menu.organizer_menu);
            }

            default -> {
                hideBottomNavigation();
                clearMenu();
                setUpMenu(R.menu.guest_menu);
                navGraph = navController.getNavInflater().inflate(R.navigation.nav_guest);
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

        bottomNavigationView.setOnItemSelectedListener(item -> {
            navController.popBackStack(R.id.homepageFragment, false);
            int id = item.getItemId();
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

            switch (userRole) {
                case "user" -> handleUserMenuItemSelection(id);
                case "admin" -> handleAdminMenuItemSelection(id);
                case "organizer" -> handleOrganizerMenuItemSelection(id);
                case "provider" -> handleProviderMenuItemSelection(id);
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

    @SuppressLint("UnsafeIntentLaunch")
    private void logOutUser() { // TODO: remove user from shared prefs
        Intent intent = getIntent();
        finish();
        startActivity(intent);
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