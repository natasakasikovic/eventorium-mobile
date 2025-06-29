package com.eventorium.presentation.event.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.databinding.FragmentLocationMapBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LocationMapFragment extends Fragment {

    private static final String ARG_ADDRESS = "address";
    private FragmentLocationMapBinding binding;
    private MapView mapView;
    private String address;

    public static LocationMapFragment newInstance(String address) {
        LocationMapFragment fragment = new LocationMapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ADDRESS, address);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            address = getArguments().getString(ARG_ADDRESS);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLocationMapBinding.inflate(inflater, container, false);
        Configuration.getInstance().setUserAgentValue(requireContext().getPackageName());

        mapView = binding.map;
        mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);

        loadLocationAsync();

        return binding.getRoot();
    }

    private void loadLocationAsync() {
        if (binding != null)
            binding.progressBar.setVisibility(View.VISIBLE);

        new Thread(() -> {
            try {
                GeoPoint point = getCoordinatesFromAddress(address);
                if (point != null && isAdded() && binding != null)
                    requireActivity().runOnUiThread(() -> {
                        if (binding != null) {
                            showLocation(point);
                        }
                    });

                else
                    requireActivity().runOnUiThread(this::showError);

            } catch (Exception e) {
                Log.e("LocationMapFragment", "Error getting coordinates");
                if (isAdded())
                    requireActivity().runOnUiThread(this::showError);

            }
        }).start();
    }

    private void showError() {
        if (binding == null)
            return;
        binding.progressBar.setVisibility(View.GONE);
        binding.map.setVisibility(View.GONE);
        binding.locationErrorText.setVisibility(View.VISIBLE);
    }

    private void showLocation(GeoPoint point) {
        binding.progressBar.setVisibility(View.GONE);
        binding.map.setVisibility(View.VISIBLE);
        binding.locationErrorText.setVisibility(View.GONE);

        mapView.getOverlays().clear();

        Marker marker = new Marker(mapView);
        marker.setPosition(point);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setTitle(address);
        mapView.getOverlays().add(marker);

        IMapController controller = mapView.getController();
        controller.setZoom(16.0);
        controller.setCenter(point);

        mapView.invalidate();
    }

    private GeoPoint getCoordinatesFromAddress(String address) throws IOException, JSONException {
        String url = "https://nominatim.openstreetmap.org/search?format=json&q="
                + address.replace(" ", "+");

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "AndroidApp")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                JSONArray results = new JSONArray(responseBody);
                if (results.length() > 0) {
                    JSONObject result = results.getJSONObject(0);
                    double lat = result.getDouble("lat");
                    double lon = result.getDouble("lon");
                    return new GeoPoint(lat, lon);
                }
            }
        }

        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (binding != null) {
            binding.map.onDetach();
            binding = null;
        }
    }

}