package com.yyy.huojia.output;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yyy.huojia.R;
import com.yyy.huojia.interfaces.OnItemClickListener;
import com.yyy.huojia.output.model.OutputList;
import com.yyy.huojia.util.StringUtil;

import java.util.List;

public class OutputListAdapter extends RecyclerView.Adapter<OutputListAdapter.VH> {
    Context context;
    List<OutputList> list;
    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OutputListAdapter(Context context, List<OutputList> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_output_list, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        OutputList outputList = list.get(position);
        holder.tvOrder.setText(outputList.getSBillNo());
        holder.tvRed.setText(outputList.getSRed().equals("是") ? "红冲" : "");
        holder.tvStock.setText("仓库名称：" + outputList.getSStockName());
        holder.tvWeight.setText("领用重量：" + outputList.getFQty() + "kg");
        holder.tvDate.setText("制单时间：" + StringUtil.formatDate(outputList.getDDate()));
        holder.tvMan.setText("制单人：" + outputList.getSUserName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView tvOrder;
        TextView tvRed;
        TextView tvStock;
        TextView tvWeight;
        TextView tvDate;
        TextView tvMan;

        public VH(View v) {
            super(v);
            tvOrder = v.findViewById(R.id.tv_order);
            tvRed = v.findViewById(R.id.tv_red);
            tvStock = v.findViewById(R.id.tv_stock);
            tvWeight = v.findViewById(R.id.tv_weight);

            tvDate = v.findViewById(R.id.tv_date);
            tvMan = v.findViewById(R.id.tv_man);
        }
    }
}
