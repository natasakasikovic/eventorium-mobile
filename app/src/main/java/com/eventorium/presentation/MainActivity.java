package com.eventorium.presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

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
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.eventorium.R;
import com.eventorium.databinding.ActivityMainBinding;
import com.eventorium.presentation.util.viewmodels.SplashScreenViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(SplashScreenViewModel.class);
        SplashScreen
                .installSplashScreen(this)
                .setKeepOnScreenCondition(() -> Boolean.TRUE.equals(viewModel.getIsLoading().getValue()));

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

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

        String userRole = "admin"; // TODO: get role from shared prefs

        NavGraph navGraph;

        if (userRole.equals("admin")) {
            navGraph = navController.getNavInflater().inflate(R.navigation.nav_admin);
        } else if (userRole.equals("user")) {
            navGraph = navController.getNavInflater().inflate(R.navigation.nav_user);
        } else {
            hideBottomNavigation();
            navGraph = navController.getNavInflater().inflate(R.navigation.nav_guest);
        }

        navController.setGraph(navGraph);
        navController.popBackStack(R.id.homepageFragment, false);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void setNavigation() {
        bottomNavigationView = binding.baseLayout.bottomNavigation;
        navigationView = binding.baseLayout.navigationView;

        setupNavController();


        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_account) {
                navController.navigate(R.id.accountDetailsFragment);
            } else if (id == R.id.nav_home) {
                navController.popBackStack(R.id.homepageFragment, false);
                navController.navigate(R.id.homepageFragment);
            }
            return true;
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_login) {
                navController.navigate(R.id.loginFragment);
            } else if (id == R.id.nav_signup) {
                navController.navigate(R.id.registerFragment);
            }
            drawer.closeDrawers();
            return true;
        });
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

    private void setupStatusBarAndToolbar() {
        toolbar = binding.baseLayout.toolbar;
        setSupportActionBar(toolbar);

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.md_theme_secondaryContainer));

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }
}