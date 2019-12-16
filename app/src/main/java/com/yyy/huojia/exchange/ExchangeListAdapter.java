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
import com.yyy.huojia.output.model.OutputList;
import com.yyy.huojia.util.StringUtil;

import java.util.List;

public class ExchangeListAdapter extends RecyclerView.Adapter<ExchangeListAdapter.VH> {
    Context context;
    List<ExchangeList> list;
    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ExchangeListAdapter(Context context, List<ExchangeList> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_exchange_list, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        ExchangeList exchangeList = list.get(position);
        holder.tvOrder.setText(exchangeList.getSBillNo());
        holder.tvMan.setText("制单人：" + exchangeList.getSUserName());
        holder.tvStockIn.setText("调入仓：" + exchangeList.getSInStockName());
        holder.tvStockOut.setText("调出仓：" + exchangeList.getSOutStockName());
        holder.tvWeight.setText("重量：" + exchangeList.getFQty() + "kg");
        holder.tvNum.setText("金额：" + exchangeList.getFTotal() + "kg");
        holder.tvDate.setText("调拨日期：" + StringUtil.formatDate(exchangeList.getDDate()));
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
        TextView tvMan;
        TextView tvStockIn;
        TextView tvStockOut;
        TextView tvWeight;
        TextView tvDate;
        TextView tvNum;


        public VH(View v) {
            super(v);
            tvOrder = v.findViewById(R.id.tv_order);
            tvStockIn = v.findViewById(R.id.tv_stock_in);
            tvStockOut = v.findViewById(R.id.tv_stock_out);
            tvWeight = v.findViewById(R.id.tv_weight);
            tvWeight = v.findViewById(R.id.tv_weight);
            tvDate = v.findViewById(R.id.tv_date);
            tvMan = v.findViewById(R.id.tv_man);
            tvNum = v.findViewById(R.id.tv_num);
        }
    }
}
