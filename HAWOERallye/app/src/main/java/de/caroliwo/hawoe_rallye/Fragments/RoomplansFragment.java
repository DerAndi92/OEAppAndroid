package de.caroliwo.hawoe_rallye.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.barteksc.pdfviewer.PDFView;

import de.caroliwo.hawoe_rallye.R;

public class RoomplansFragment extends Fragment {

    PDFView pdfView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_roomplans, container,false);
        pdfView = rootView.findViewById(R.id.pdfView);
        pdfView.fromAsset("Raumplaene.pdf").load();


        return rootView;

    }
}
