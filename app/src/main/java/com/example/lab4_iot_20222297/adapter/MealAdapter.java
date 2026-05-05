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
import com.example.lab4_iot_20222297.model.Meal;

import java.util.ArrayList;
import java.util.List;

// Adapter para el RecyclerView de platos (MealsFragment)
// Muestra: strMeal, idMeal, strMealThumb
// LLM asistido: Claude (Anthropic) - alineado con lo visto en clase (RecyclerView Adapter)
public class MealAdapter extends RecyclerView.Adapter<MealAdapter.ViewHolder> {

    public interface OnMealClickListener {
        void onMealClick(Meal meal);
    }

    private List<Meal> meals = new ArrayList<>();
    private final OnMealClickListener listener;

    public MealAdapter(OnMealClickListener listener) {
        this.listener = listener;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.tvMealName.setText(meal.getStrMeal());
        holder.tvMealId.setText("ID: " + meal.getIdMeal());
        // Glide carga la imagen desde strMealThumb
        Glide.with(holder.itemView.getContext())
                .load(meal.getStrMealThumb())
                .into(holder.ivMealThumb);
        holder.itemView.setOnClickListener(v -> listener.onMealClick(meal));
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMealThumb;
        TextView tvMealName;
        TextView tvMealId;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMealThumb = itemView.findViewById(R.id.ivMealThumb);
            tvMealName = itemView.findViewById(R.id.tvMealName);
            tvMealId = itemView.findViewById(R.id.tvMealId);
        }
    }
}
