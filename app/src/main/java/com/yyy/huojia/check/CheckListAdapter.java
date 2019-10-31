package com.yyy.huojia.check;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yyy.huojia.R;
import com.yyy.huojia.check.model.CheckList;
import com.yyy.huojia.input.model.InputList;
import com.yyy.huojia.interfaces.OnItemClickListener;
import com.yyy.huojia.output.model.OutputList;
import com.yyy.huojia.util.StringUtil;

import java.util.List;

public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.VH> {
    Context context;
    List<CheckList> list;
    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public CheckListAdapter(Context context, List<CheckList> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_check_list, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        CheckList inputList = list.get(position);
        holder.tvOrder.setText(inputList.getSBillNo());
        holder.tvStock.setText(inputList.getSStockName());
        holder.tvWeight.setText(inputList.getFQty() + "kg");
        holder.tvSupplier.setText(inputList.getSCustShortName());
        holder.tvDate.setText(StringUtil.formatDate(inputList.getDDate()));
        holder.tvMan.setText(inputList.getSUserName());
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
        TextView tvSupplier;
        TextView tvDate;
        TextView tvMan;

        public VH(View v) {
            super(v);
            tvOrder = v.findViewById(R.id.tv_order);
            tvRed = v.findViewById(R.id.tv_red);
            tvStock = v.findViewById(R.id.tv_stock);
            tvWeight = v.findViewById(R.id.tv_weight);
            tvSupplier = v.findViewById(R.id.tv_supplier);
            tvDate = v.findViewById(R.id.tv_date);
            tvMan = v.findViewById(R.id.tv_man);
        }
    }
}
