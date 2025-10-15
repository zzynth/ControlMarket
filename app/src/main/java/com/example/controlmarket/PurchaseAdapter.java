package com.example.controlmarket;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.ViewHolder> {

    private List<Purchase> compras;

    public PurchaseAdapter(List<Purchase> compras) {
        this.compras = compras;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_purchase, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Purchase compra = compras.get(position);

        holder.tvNombre.setText(compra.nombre);

        double total = compra.calcularTotal();
        holder.tvMonto.setText(String.format("$%.2f", total));
    }

    @Override
    public int getItemCount() {
        return compras.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvMonto;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreProducto);
            tvMonto = itemView.findViewById(R.id.tvMontoTotal);
        }
    }
    public void setData(List<Purchase> nuevasCompras) {
        this.compras = nuevasCompras;
        notifyDataSetChanged();
    }

}