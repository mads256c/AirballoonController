package com.aatg.elev.airballooncontroller;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import com.aatg.elev.airballooncontroller.R;

public class AddViewFragment extends Fragment {



    public AddViewFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_view, container, false);

        //Module type
        Spinner module_type = view.findViewById(R.id.module_type);
        ArrayAdapter<CharSequence> adapter_type = ArrayAdapter.createFromResource(getContext(), R.array.module_types, android.R.layout.simple_spinner_item);
        adapter_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        module_type.setAdapter(adapter_type);

        //Module port
        Spinner module_port = view.findViewById(R.id.module_port);
        ArrayAdapter<CharSequence> adapter_port = ArrayAdapter.createFromResource(getContext(), R.array.port_number, android.R.layout.simple_spinner_item);
        adapter_port.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        module_port.setAdapter(adapter_port);

        // Inflate the layout for this fragment
        return view;
    }

}
