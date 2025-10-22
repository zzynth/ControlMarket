package com.example.controlmarket;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.PurchaseViewHolder> {

    private List<Purchase> compras;
    private OnCompraClickListener listener;

    public interface OnCompraClickListener {
        void onCompraClick(Purchase compra);
    }

    public PurchaseAdapter(List<Purchase> compras, OnCompraClickListener listener) {
        this.compras = compras;
        this.listener = listener;
    }

    public void setData(List<Purchase> nuevasCompras) {
        this.compras = nuevasCompras;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PurchaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_purchase, parent, false);
        return new PurchaseViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseViewHolder holder, int position) {
        Purchase compra = compras.get(position);
        holder.tvNombreProducto.setText(compra.nombre);
        holder.tvMontoTotal.setText(String.format(Locale.getDefault(), "$%.0f", compra.calcularTotal()));
        holder.tvDireccionCompra.setText(compra.direccion != null ? compra.direccion : "Sin dirección");
    }

    @Override
    public int getItemCount() {
        return compras.size();
    }

    class PurchaseViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreProducto, tvMontoTotal, tvDireccionCompra;

        public PurchaseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreProducto = itemView.findViewById(R.id.tvNombreProducto);
            tvMontoTotal = itemView.findViewById(R.id.tvMontoTotal);
            tvDireccionCompra = itemView.findViewById(R.id.tvDireccionCompra);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && listener != null) {
                    listener.onCompraClick(compras.get(pos));
                }
            });
        }
    }
}