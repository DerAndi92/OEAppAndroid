package de.caroliwo.hawoe_rallye.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.barteksc.pdfviewer.PDFView;

import de.caroliwo.hawoe_rallye.R;

public class RoomplansFragment extends Fragment {

    //Zeigt PDF der Raumpläne. PDF liegt in assets.

    PDFView pdfView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_roomplans, container, false);
        pdfView = rootView.findViewById(R.id.pdfView);
        pdfView.fromAsset("Raumplaene.pdf")
                .spacing(5) //Abstand zwischen den einzelnen Seiten
                .load();

        return rootView;
    }
}
