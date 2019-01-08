package com.aatg.elev.airballooncontroller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aatg.elev.airballooncontroller.selectpaireddevice.PairedDeviceItem;
import com.aatg.elev.bluetoothdebugger.Module;

import java.util.List;

public final class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ViewHolder> {
    private final List<Module> modules;

    ModuleAdapter(List<Module> items) {
        modules = items;
    }

    @Override
    public ModuleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ModuleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ModuleAdapter.ViewHolder holder, int position) {
        holder.mName.setText(modules.get(position).name);
        holder.mType.setText(modules.get(position).type);
        holder.mPort.setText(modules.get(position).port);
    }

    @Override
    public int getItemCount() {
        return modules.size();
    }

    public final class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mName;
        public final TextView mType;
        public final TextView mPort;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mName = (TextView) view.findViewById(R.id.module_name_item);
            mType = (TextView) view.findViewById(R.id.module_type_item);
            mPort = (TextView) view.findViewById(R.id.module_port_item);
        }
    }
}
