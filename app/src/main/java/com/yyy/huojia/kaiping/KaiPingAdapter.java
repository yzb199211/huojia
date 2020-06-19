package com.yyy.huojia.kaiping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yyy.huojia.R;

import java.util.List;

public class KaiPingAdapter extends RecyclerView.Adapter<KaiPingAdapter.VH> {
    private Context context;
    private List<KaiPingDetailB> list;

    public KaiPingAdapter(Context context, List<KaiPingDetailB> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_kaiping, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.tvName.setText(list.get(position).getSElements());
        holder.tvQty.setText("数量：" + list.get(position).getIQty());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvQty;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvQty = itemView.findViewById(R.id.tv_qty);
        }
    }
}
