package com.example.olio_ht;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    TextView factView;
    View view;

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        factView = (TextView) this.view.findViewById(R.id.factView);
        String[] facts = getResources().getStringArray(R.array.facts);
        String fact = facts[0];
        factView.setText("Did you know?\n" + fact);
    }

    public void changeFact() {
        try {
            String arg = getArguments().getString("fact");
            factView = (TextView) this.view.findViewById(R.id.factView);
            factView.setText(arg);
        } catch (Exception e) {}
    }
}

