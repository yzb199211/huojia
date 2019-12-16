package com.yyy.huojia.exchange;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yyy.huojia.R;
import com.yyy.huojia.interfaces.OnItemClickListener;
import com.yyy.huojia.interfaces.OnItemLongClickListener;

import java.util.List;

public class BarCodeAdapter extends RecyclerView.Adapter<BarCodeAdapter.VH> {
    Context context;
    List<BarCode> list;
    OnItemClickListener onItemClickListener;
    OnItemLongClickListener onItemLongClickListener;

    public BarCodeAdapter(Context context, List<BarCode> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_barcode_output, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.tvCode.setText("物料编号：" + list.get(position).getSCode());
        holder.tvName.setText(list.get(position).getSName());
        holder.tvElement.setText("规格：" + list.get(position).getSElements());
        holder.tvPrice.setText("单价：" + list.get(position).getFPrice());
        holder.tvWeight.setText("总重量：" + list.get(position).getFQty() + "kg");
        holder.tvMoney.setText("总金额：" + list.get(position).getFTotal());
        holder.tvSupplier.setText(list.get(position).getSStockShortName());
        holder.tvBatch.setText("钢卷号：" + list.get(position).getSBatchNo());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onLongClick(v, position);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        private TextView tvCode;
        private TextView tvName;
        private TextView tvElement;
        private TextView tvPrice;
        private TextView tvWeight;
        private TextView tvMoney;
        private TextView tvSupplier;
        private TextView tvBatch;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvCode = itemView.findViewById(R.id.tv_code);
            tvName = itemView.findViewById(R.id.tv_name);

            tvElement = itemView.findViewById(R.id.tv_element);
            tvPrice = itemView.findViewById(R.id.tv_price);

            tvWeight = itemView.findViewById(R.id.tv_weight);
            tvMoney = itemView.findViewById(R.id.tv_money);

            tvSupplier = itemView.findViewById(R.id.tv_supplier);
            tvBatch = itemView.findViewById(R.id.tv_batch);
        }
    }
}
