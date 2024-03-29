package emma.londonloopapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Created by Emma on 06/05/2015.
 */
public class StartWalkFragment extends Fragment {

    private String modes = "bus-tube-train-boat";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_start, container, false);

        //checkboxes

        CheckBox bus = (CheckBox) rootView.findViewById(R.id.checkbox_bus);
        CheckBox tube = (CheckBox) rootView.findViewById(R.id.checkbox_tube);
        CheckBox train = (CheckBox) rootView.findViewById(R.id.checkbox_train);
        CheckBox boat = (CheckBox) rootView.findViewById(R.id.checkbox_boat);

        bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckboxClicked(v);
            }
        });

        tube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckboxClicked(v);
            }
        });

        train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckboxClicked(v);
            }
        });

        boat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckboxClicked(v);
            }
        });

        bus.setChecked(true);
        tube.setChecked(true);
        train.setChecked(true);
        boat.setChecked(true);

        //get walk number
        final long walkNumber = getArguments().getLong("walkNumber", 0);

        // Destination
        final EditText start_location  = (EditText) rootView.findViewById(R.id.startWalkText);

        final Button navButton = (Button) rootView.findViewById(R.id.gpsButton);
        navButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                if (start_location.getText().toString().trim().length()>0){
                    // plan new journey
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                    RouteListFragment wdf = RouteListFragment.newInstance(walkNumber, modes, replaceAtTheEnd(start_location.getText().toString()),0);

                    fragmentManager.beginTransaction()
                            .add(R.id.container, wdf)
                                    // Add this transaction to the back stack
                            .addToBackStack("startFrag")
                            .commit();
                } else {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                    RouteListFragment wdf = RouteListFragment.newInstance(walkNumber, modes, "",0);

                    fragmentManager.beginTransaction()
                            .add(R.id.container, wdf)
                                    // Add this transaction to the back stack
                            .addToBackStack("startFrag")
                            .commit();
                }
            }
        });

        final Button noNavButton = (Button) rootView.findViewById(R.id.noNavButton);
        noNavButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                GPSSectionFragment wdf = GPSSectionFragment.newInstance(walkNumber);

                fragmentManager.beginTransaction()
                        .add(R.id.container, wdf)
                                // Add this transaction to the back stack
                        .addToBackStack("startFrag")
                        .commit();
            }
        });

        return rootView;
    }

    private static String replaceAtTheEnd(String input){
        input = input.replaceAll("\\s+$", "");
        return input;
    }

    private void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_bus:
                if (checked){
                    if (modes.equals("")){
                        modes = "bus";
                    } else if(modes.contains("bus")) {
                        //do nothing
                    } else {
                        modes += "-bus";
                    }
                } else{
                    if (modes.contains("-bus")){
                        modes =  modes.replace("-bus", "");
                    } else if (modes.contains("bus")){
                        modes =  modes.replace("bus","");
                    }
                }
                break;
            case R.id.checkbox_tube:
                if (checked) {
                    if (modes.equals("")){
                        modes = "tube";
                    } else if(modes.contains("tube")) {
                    //do nothing
                    }else {
                        modes += "-tube";
                    }
                }else {
                    if (modes.contains("-tube")){
                        modes = modes.replace("-tube", "");
                    } else if (modes.contains("tube")){
                        modes = modes.replace("tube","");
                    }
                }
                break;
            case R.id.checkbox_train:
                if (checked) {
                    if (modes.equals("")){
                        modes = "train";
                    } else if (modes.contains("train")){
                        //do nothing
                    } else{
                        modes += "-train";
                    }
                }else {
                    if (modes.contains("-train")){
                        modes = modes.replace("-train", "");
                    } else if (modes.contains("train")){
                        modes = modes.replace("train","");
                    }
                }
                break;
            case R.id.checkbox_boat:
                if (checked) {
                    if (modes.equals("")){
                        modes = "boat";
                    } else if(modes.contains("boat")) {
                        //do nothing
                    } else{
                        modes += "-boat";
                    }
                }else {
                    if (modes.contains("-boat")){
                        modes = modes.replace("-boat", "");
                    } else if (modes.contains("boat")){
                        modes = modes.replace("boat","");
                    }
                }break;
        }
    }

    public static StartWalkFragment newInstance(long walk)
    {
        StartWalkFragment f = new StartWalkFragment();
        final Bundle bdl = new Bundle(1);
        bdl.putLong("walkNumber", walk);
        f.setArguments(bdl);
        return f;

    }

}
