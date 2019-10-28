package com.yyy.huojia.input;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yyy.huojia.R;
import com.yyy.huojia.input.model.NotArrived;
import com.yyy.huojia.util.StringUtil;

import java.util.List;

public class InputNoArrivedAdapter extends RecyclerView.Adapter<InputNoArrivedAdapter.VH> {
    Context context;
    List<NotArrived> list;

    public InputNoArrivedAdapter(Context context, List<NotArrived> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_input_not_arrived, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.tvCode.setText("物料编号：" + list.get(position).getSCode());
        holder.tvBarcode.setText("钢卷号：" + list.get(position).getSBatchNo());
        holder.tvName.setText("" + list.get(position).getSName());
        holder.tvElement.setText("规格：" + list.get(position).getSElements());
        holder.tvSupplier.setText("供应商：" + list.get(position).getSCustShortName());
        holder.tvPurchase.setText("采购单号：" + list.get(position).getSPurOrderNo());
        holder.tvArrived.setText("到货单号：" + list.get(position).getSBillNo());
        holder.tvWeight.setText("总重量：" + list.get(position).getFQty() + "kg");
        holder.tvWeightNotArrived.setText("未入库重量：" + list.get(position).getFNotInQty() + "kg");
        holder.tvMoney.setText("总金额：" + list.get(position).getFTotal());
        holder.tvMoneyNotArrived.setText("未入库金额：" + list.get(position).getFNotInTotal());
        holder.tvPrice.setText("单价：" + list.get(position).getFPrice());
        holder.tvDate.setText("到货日期：" + StringUtil.formatDate(list.get(position).getDDate()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        private TextView tvCode;
        private TextView tvBarcode;

        private TextView tvName;
        private TextView tvElement;

        private TextView tvSupplier;

        private TextView tvPurchase;
        private TextView tvArrived;

        private TextView tvWeight;
        private TextView tvWeightNotArrived;

        private TextView tvMoney;
        private TextView tvMoneyNotArrived;

        private TextView tvPrice;
        private TextView tvDate;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvCode = itemView.findViewById(R.id.tv_code);
            tvBarcode = itemView.findViewById(R.id.tv_barcode);

            tvName = itemView.findViewById(R.id.tv_name);
            tvElement = itemView.findViewById(R.id.tv_element);

            tvSupplier = itemView.findViewById(R.id.tv_supplier);

            tvPurchase = itemView.findViewById(R.id.tv_purchase);
            tvArrived = itemView.findViewById(R.id.tv_arrived);


            tvWeight = itemView.findViewById(R.id.tv_weight);
            tvWeightNotArrived = itemView.findViewById(R.id.tv_not_arrived_weight);

            tvMoney = itemView.findViewById(R.id.tv_money);
            tvMoneyNotArrived = itemView.findViewById(R.id.tv_not_arrived_money);

            tvPrice = itemView.findViewById(R.id.tv_price);
            tvDate = itemView.findViewById(R.id.tv_date);
        }
    }
}
