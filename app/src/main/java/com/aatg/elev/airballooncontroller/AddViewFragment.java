package com.aatg.elev.airballooncontroller;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aatg.elev.airballooncontroller.Module;

import java.util.ArrayList;
import java.util.List;

public class AddViewFragment extends Fragment {

    List<Module> moduleList = new ArrayList<>();
    private boolean ignoreErrors = false;

    public AddViewFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_view, container, false);

        //Recyclerview
        final RecyclerView module_viewer = view.findViewById(R.id.module_viewer);
        module_viewer.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        module_viewer.setLayoutManager(layoutManager);
        module_viewer.setAdapter(new ModuleAdapter(moduleList));

        //Module type
        final Spinner module_type = view.findViewById(R.id.module_type);
        ArrayAdapter<CharSequence> adapter_type = ArrayAdapter.createFromResource(getContext(), R.array.module_types, android.R.layout.simple_spinner_item);
        adapter_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        module_type.setAdapter(adapter_type);

        //Module port
        final Spinner module_port = view.findViewById(R.id.module_port);
        ArrayAdapter<CharSequence> adapter_port = ArrayAdapter.createFromResource(getContext(), R.array.port_number, android.R.layout.simple_spinner_item);
        adapter_port.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        module_port.setAdapter(adapter_port);

        //Add button
        final TextView module_name = view.findViewById(R.id.module_name);


        Button add_module = view.findViewById(R.id.add_module);
        add_module.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String name = module_name.getText().toString();
                String type = module_type.getSelectedItem().toString();
                String port = module_port.getSelectedItem().toString();

                Module module = new Module(name,type,port);


                boolean hasModule = false;


                for (Module items : moduleList){
                    if(module.getName().equals(items.getName()) ||module.getPort()==items.getPort()){
                        hasModule = true;
                        break;
                    }
                }
                if(!hasModule){
                    moduleList.add(module);
                    module_viewer.setAdapter(new ModuleAdapter(moduleList));
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}
