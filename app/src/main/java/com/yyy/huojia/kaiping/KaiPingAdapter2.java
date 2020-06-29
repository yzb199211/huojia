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

public class KaiPingAdapter2 extends RecyclerView.Adapter<KaiPingAdapter2.VH> {
    private Context context;
    private List<KaiPingB> list;

    public KaiPingAdapter2(Context context, List<KaiPingB> list) {
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
        holder.tvCode.setText(list.get(position).getSBatchNo());
        holder.tvEle.setText(list.get(position).getSElements());
        holder.tvLength.setText("总长度：" + list.get(position).getFLength());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        private TextView tvCode;
        private TextView tvName;
        private TextView tvEle;
        private TextView tvLength;


        public VH(@NonNull View itemView) {
            super(itemView);
            tvCode = itemView.findViewById(R.id.tv_code);
            tvName = itemView.findViewById(R.id.tv_name);
            tvEle = itemView.findViewById(R.id.tv_element);
            tvLength = itemView.findViewById(R.id.tv_length);

        }
    }
}
