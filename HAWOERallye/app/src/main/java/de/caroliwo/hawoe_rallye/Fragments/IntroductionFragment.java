package de.caroliwo.hawoe_rallye.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.caroliwo.hawoe_rallye.R;

public class IntroductionFragment extends Fragment {

    //Anleitung --> Text in values/strings.xml

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manual, container, false );
    }
}
