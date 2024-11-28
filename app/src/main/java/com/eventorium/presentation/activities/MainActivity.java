package com.eventorium.presentation.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.eventorium.R;
import com.eventorium.databinding.ActivityMainBinding;
import com.eventorium.presentation.viewmodels.SplashScreenViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private SplashScreenViewModel viewModel;
    private ActivityMainBinding binding;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private NavigationView navigationView;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(SplashScreenViewModel.class);
        SplashScreen
                .installSplashScreen(this)
                .setKeepOnScreenCondition(() -> Boolean.TRUE.equals(viewModel.getIsLoading().getValue()));

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupStatusBarAndToolbar();

        setNavigation();

        setupDrawer();

        configureNavigation();

        navController = Navigation.findNavController(this, R.id.fragment_nav_content_main);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_favourite, R.id.nav_login, R.id.nav_new, R.id.nav_signup)
                .setOpenableLayout(drawer)
                .build();

        setupBottomNavigationVisibility();
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
        updateMenu(false);
        return true;
    }

    private void setNavigation() {
        bottomNavigationView = binding.baseLayout.bottomNavigation;
        navigationView = binding.baseLayout.navigationView;
        toolbar = binding.baseLayout.toolbar;
    }

    private void configureNavigation() {
        navigationView.setNavigationItemSelectedListener(this::handleSideNavigationSelection);
        bottomNavigationView.setOnItemSelectedListener(this::handleBottomNavigationSelection);
    }

    private void setupDrawer() {
        drawer = binding.baseLayout.drawerLayout;
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }


    private boolean handleSideNavigationSelection(MenuItem item) {
        // TODO: add messages, notification, booking calendar
        int id = item.getItemId();

        if (id == R.id.nav_login) {
            navController.navigate(R.id.loginFragment);
            hideBottomNavigation();
        } else if (id == R.id.nav_signup) {
            navController.navigate(R.id.registerFragment);
            hideBottomNavigation();
        } else if (id == R.id.nav_logout) {
            navController.navigate(R.id.homepageFragment);
            logOutUser();
        } else if (id == R.id.nav_services) {
            navController.navigate(R.id.manageServicesFragment);
        } else if (id == R.id.nav_invitations) { // TODO: delete later - just for testing purposes (until budget and agenda are created)!
            navController.navigate(R.id.invitationsFragment);
        } else if (id == R.id.nav_categories) {
            navController.navigate(R.id.categoryOverviewFragment);
        } else if (id == R.id.nav_booking){
            navController.navigate(R.id.bookingFragment);
        }
        else {
            showBottomNavigation();
            return false;
        }
        return true;
    }

    private boolean handleBottomNavigationSelection(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            navController.navigate(R.id.homepageFragment);
        } else if(id == R.id.nav_new) {
            navController.navigate(R.id.createServiceFragment);
        } else if (id == R.id.nav_account) {
            navController.navigate(R.id.accountDetailsFragment);
        }
        else {
            return false;
        }
        return true;
    }

    @SuppressLint("UnsafeIntentLaunch")
    private void logOutUser() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void setupBottomNavigationVisibility() {
        navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            int id = navDestination.getId();
            if (id == R.id.nav_login || id == R.id.nav_signup || id == R.id.companyRegisterFragment) {
                hideBottomNavigation();
            } else {
                showBottomNavigation();
            }
            drawer.closeDrawers();
        });
    }

    private void hideBottomNavigation() {
        bottomNavigationView.setVisibility(View.GONE);
    }

    private void showBottomNavigation() {
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    public void updateMenuAfterLogin() {
        updateMenu(true);
    }

    private void updateMenu(boolean isLoggedIn) {
        Menu bottomMenu = bottomNavigationView.getMenu();
        bottomMenu.findItem(R.id.nav_favourite).setVisible(isLoggedIn);
        bottomMenu.findItem(R.id.nav_account).setVisible(isLoggedIn);
        bottomMenu.findItem(R.id.nav_new).setVisible(isLoggedIn);

        Menu navMenu = navigationView.getMenu();
        navMenu.findItem(R.id.nav_login).setVisible(!isLoggedIn);
        navMenu.findItem(R.id.nav_signup).setVisible(!isLoggedIn);
        navMenu.findItem(R.id.nav_logout).setVisible(isLoggedIn);
        navMenu.findItem(R.id.nav_notification).setVisible(isLoggedIn);
        navMenu.findItem(R.id.nav_messages).setVisible(isLoggedIn);
        navMenu.findItem(R.id.nav_calendar).setVisible(isLoggedIn);
        navMenu.findItem(R.id.nav_services).setVisible(isLoggedIn);
        navMenu.findItem(R.id.nav_categories).setVisible(isLoggedIn);
    }

    private void setupStatusBarAndToolbar() {
        toolbar = binding.baseLayout.toolbar;
        setSupportActionBar(toolbar);

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.md_theme_secondaryContainer));

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }
}