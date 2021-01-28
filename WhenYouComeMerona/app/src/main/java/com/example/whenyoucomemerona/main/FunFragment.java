package com.example.whenyoucomemerona.main;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.whenyoucomemerona.R;
import com.example.whenyoucomemerona.controller.BaseFragment;
import com.example.whenyoucomemerona.lib.StaticFunction;
import com.example.whenyoucomemerona.view.mGLSurfaceRenderer;

public class FunFragment extends BaseFragment {

    private GLSurfaceView mGLSurfaceView;

    public FunFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fun, container, false);

        mGLSurfaceView = view.findViewById(R.id.mgl);

        mGLSurfaceView = new GLSurfaceView(getContext());

        mGLSurfaceView.setEGLContextClientVersion(2);
        mGLSurfaceView.setRenderer(new mGLSurfaceRenderer(getContext()));

        mGLSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 상황 종료
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return mGLSurfaceView;

    }

}
