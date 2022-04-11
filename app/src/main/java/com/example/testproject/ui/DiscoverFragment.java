package com.example.testproject.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.testproject.MainActivity;
import com.example.testproject.R;
import com.example.testproject.databinding.FragmentDiscoverBinding;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DiscoverFragment extends Fragment implements OnMapReadyCallback {

    private FragmentDiscoverBinding binding;
    MapView mapView;
    GoogleMap mMap;

    private ImageButton searchBtn;
    private EditText searchText;
    private TextView errorTxt;

    private DatabaseReference RecipeRef;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        searchBtn = (ImageButton) view.findViewById(R.id.searchBtn);
        searchText = (EditText) view.findViewById(R.id.searchText);
        errorTxt = (TextView) view.findViewById(R.id.errorTxt);

        RecipeRef = FirebaseDatabase.getInstance().getReference();

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTextValue = searchText.getText().toString();
                MainActivity activity = (MainActivity) getActivity();
                CloseKeyboard();

                RecipeRef.child("Recipes")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String retrieveRecipeName = snapshot.child("recipeName").getValue().toString();
                                    if (retrieveRecipeName.contentEquals(searchTextValue)) {
                                        activity.displayRecipeClick(view, searchTextValue);
                                    } else {
                                        errorTxt.setText("Recipe not found.");
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }

                        });
            }
        });

        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

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

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void CloseKeyboard() {
        View view = this.getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}