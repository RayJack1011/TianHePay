package com.tianhe.pay.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

public class StatesManager extends Fragment {
    private static final String STATES_KEY = "StatesManager:Keys";
    private static StatesContainer.HashStatesContainer<String, Object> TRACK_OF_STATES_KEY
            = new StatesContainer.HashStatesContainer<>();

    private StatesContainer.ListStatesContainer<String, Object> retainStates;

    public StatesManager() {
        setArguments(new Bundle());
    }

    public StatesContainer.ListStatesContainer<String, Object> getRetainStates() {
        return retainStates;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        retainStates = new StatesContainer.ListStatesContainer<>(TRACK_OF_STATES_KEY);
        if (savedInstanceState != null) {
            ArrayList<String> keyList = savedInstanceState.getStringArrayList(STATES_KEY);
            if (keyList != null) {
                retainStates.setKeys(keyList);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(STATES_KEY, new ArrayList<>(retainStates.getKeys()));
    }
}
