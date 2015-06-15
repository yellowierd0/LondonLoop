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
public class EndWalkFragment extends Fragment {


    private String modes = "bus-tube-train-boat";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_end, container, false);

        CheckBox bus = (CheckBox) rootView.findViewById(R.id.end_checkbox_bus);
        CheckBox tube = (CheckBox) rootView.findViewById(R.id.end_checkbox_tube);
        CheckBox train = (CheckBox) rootView.findViewById(R.id.end_checkbox_train);
        CheckBox boat = (CheckBox) rootView.findViewById(R.id.end_checkbox_boat);

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

        final long walkNumber = getArguments().getLong("walkNumber", 0);

        final Button navButton = (Button) rootView.findViewById(R.id.nextWalkButton);
        navButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                GPSSectionFragment wdf = GPSSectionFragment.newInstance(walkNumber+1);

                fragmentManager.beginTransaction()
                        .add(R.id.container, wdf)
                                // Add this transaction to the back stack
                        .addToBackStack("endFrag")
                        .commit();
            }
        });

        final EditText destination = (EditText) rootView.findViewById(R.id.leaveWalkText);

        final Button leaveButton = (Button) rootView.findViewById(R.id.leaveWalkButton);
        leaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                /*RouteListFragment wdf = RouteListFragment.newInstance(modes, destination.getText().toString());
                System.out.println(destination.getText().toString());

                fragmentManager.beginTransaction()
                        .add(R.id.container, wdf)
                                // Add this transaction to the back stack
                        .addToBackStack("endFrag")
                        .commit();
*/
            }
        });

        return rootView;
    }

    public void onCheckboxClicked(View view) {
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

    public static EndWalkFragment newInstance(long walk)
    {
        EndWalkFragment f = new EndWalkFragment();
        final Bundle bdl = new Bundle(1);
        bdl.putLong("walkNumber", walk);
        f.setArguments(bdl);
        return f;

    }

}
