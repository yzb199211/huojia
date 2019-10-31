package com.yyy.huojia.check;

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
import com.yyy.huojia.check.model.BarCode;

import java.util.List;

public class BarCodeAdapter extends RecyclerView.Adapter<BarCodeAdapter.VH> {
    Context context;
    List<BarCode> list;

    public BarCodeAdapter(Context context, List<BarCode> list) {
        this.context = context;
        this.list = list;
    }

    OnItemClickListener onItemClickListener;
    OnItemLongClickListener onItemLongClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_barcode, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        BarCode item = list.get(position);
        holder.tvCode.setText("物料编码：" + item.getSCode());
        holder.tvName.setText(item.getSName());
        holder.tvPurchase.setText("采购单号：" + item.getSPurOrderNo());
        holder.tvArrived.setText("到货单号：" + item.getSPurOrderMatMBillNo());
        holder.tvBatch.setText("钢卷号：" + item.getSBatchNo());
        holder.tvElement.setText("规格：" + item.getSElements());
        holder.tvWeight.setText("重量：" + item.getFQty() + "kg");
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
        private TextView tvPurchase;
        private TextView tvArrived;
        private TextView tvBatch;
        private TextView tvElement;
        private TextView tvWeight;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvCode = itemView.findViewById(R.id.tv_code);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPurchase = itemView.findViewById(R.id.tv_purchase);
            tvArrived = itemView.findViewById(R.id.tv_arrived);
            tvBatch = itemView.findViewById(R.id.tv_batch);
            tvElement = itemView.findViewById(R.id.tv_element);
            tvWeight = itemView.findViewById(R.id.tv_weight);
        }
    }
}
