package com.example.lab4_iot_20222297.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lab4_iot_20222297.R;
import com.example.lab4_iot_20222297.model.Category;

import java.util.ArrayList;
import java.util.List;

// Adapter para el RecyclerView de categorías (CategoriesFragment)
// LLM asistido: Claude (Anthropic) - alineado con lo visto en clase (RecyclerView Adapter)
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    private List<Category> categories = new ArrayList<>();
    private final OnCategoryClickListener listener;

    public CategoryAdapter(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.tvCategoryName.setText(category.getStrCategory());
        // Glide carga la imagen desde strCategoryThumb
        Glide.with(holder.itemView.getContext())
                .load(category.getStrCategoryThumb())
                .into(holder.ivCategoryThumb);
        holder.itemView.setOnClickListener(v -> listener.onCategoryClick(category));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCategoryThumb;
        TextView tvCategoryName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCategoryThumb = itemView.findViewById(R.id.ivCategoryThumb);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }
    }
}
