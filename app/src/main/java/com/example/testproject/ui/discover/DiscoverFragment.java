package com.example.testproject.ui.discover;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.testproject.MainActivity;
import com.example.testproject.R;
import com.example.testproject.databinding.FragmentDiscoverBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubePlayerView;

public class DiscoverFragment extends Fragment {

    private FragmentDiscoverBinding binding;
    MapView mapView;
    GoogleMap mMap;

    private WebView webView;
    private ImageButton searchBtn;
    private EditText searchText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        searchBtn = (ImageButton) view.findViewById(R.id.searchBtn);
        searchText = (EditText) view.findViewById(R.id.searchText);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTextValue = searchText.getText().toString();
                MainActivity activity = (MainActivity) getActivity();

                activity.displayRecipeClick(view, searchTextValue);

//                if (searchTextValue == "Macaroons") {
//
//                }
            }
        });

//        mapView = (MapView) view.findViewById(R.id.mapView);
//        mapView.onCreate(savedInstanceState);
//        mapView.getMapAsync(this);

        webView = (WebView) view.findViewById(R.id.webView);

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.google.co.uk/maps");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        MainActivity activity = (MainActivity) getActivity();
        activity.checkMyPermission();

        VideoView simpleVideoView = (VideoView) view.findViewById(R.id.videoView); // initiate a video view
        simpleVideoView.setVideoURI(Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.baking_video));
                simpleVideoView.start();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

//    @SuppressLint("MissingPermission")
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
////        mMap.setMyLocationEnabled(true);
////        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//
//        // Add a marker in Sydney and move the camera
////        LatLng sydney = new LatLng(-34, 151);
////        mMap.addMarker(new MarkerOptions()
////                .position(sydney)
////                .title("Marker in Sydney"));
////        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//    }


//    @Override
//    public void onResume() {
//        mapView.onResume();
//        super.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        mapView.onPause();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mapView.onDestroy();
//    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        mapView.onLowMemory();
//    }

}