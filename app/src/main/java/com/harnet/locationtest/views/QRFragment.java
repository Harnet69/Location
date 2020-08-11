package com.harnet.locationtest.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harnet.locationtest.R;
import com.harnet.locationtest.viewmodels.QRActivityViewModel;

public class QRFragment extends Fragment {
    private QRActivityViewModel qrActivityViewModel = new QRActivityViewModel();

    public QRFragment() {
        // Required empty public constructor
    }

    public static QRFragment newInstance(String param1, String param2) {
        QRFragment fragment = new QRFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        qrActivityViewModel.init(getContext(), getActivity());
        return inflater.inflate(R.layout.fragment_q_r, container, false);
    }
}