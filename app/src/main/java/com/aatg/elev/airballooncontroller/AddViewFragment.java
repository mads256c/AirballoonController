package com.aatg.elev.airballooncontroller;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.aatg.elev.airballooncontroller.R;

public class AddViewFragment extends Fragment {



    public AddViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        Spinner module_type = (Spinner) findViewById(R.id.module_type);
        ArrayAdapter<CharSequence> adapter_type = ArrayAdapter.createFromResource(this,R.array.module_types,android.R.layout.simple_spinner_item);
        adapter_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        module_type.setAdapter(adapter_type);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_view, container, false);
    }

}
