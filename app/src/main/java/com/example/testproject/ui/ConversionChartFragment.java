package com.example.testproject.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.testproject.R;
import com.example.testproject.databinding.FragmentConversionChartBinding;
import com.squareup.picasso.Picasso;

public class ConversionChartFragment extends Fragment {

    private FragmentConversionChartBinding binding;
    private ImageView conversionChart;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_conversion_chart, container, false);

        conversionChart = (ImageView) view.findViewById(R.id.imageView);

        String imageLink = "https://firebasestorage.googleapis.com/v0/b/test-87fff.appspot.com/o/conversion_chart_image.jpg?alt=media&token=6fcc6312-98f4-4cf4-bdde-051a73d8280a";
        Picasso.get().load(imageLink).into(conversionChart);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}