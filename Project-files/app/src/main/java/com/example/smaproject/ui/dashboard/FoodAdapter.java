package com.example.smaproject.ui.dashboard;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smaproject.Food;
import com.example.smaproject.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;


public class FoodAdapter extends FirebaseRecyclerAdapter<Food,FoodAdapter.foodViewholder> {



    public FoodAdapter(@NonNull FirebaseRecyclerOptions<Food> options)
    {
        super(options);
    }

    protected void
    onBindViewHolder(@NonNull foodViewholder holder,
                     final int position, @NonNull Food model)
    {

        holder.kcalGrams.setText(String.valueOf(Math.round(model.getGrams())));
        holder.kCal.setText(String.valueOf(Math.round(model.getKcal())));
        holder.protein.setText(String.valueOf(Math.round(model.getProtein())));
        holder.fats.setText(String.valueOf(Math.round(model.getFats())));
        holder.carbs.setText(String.valueOf(Math.round(model.getCarbs())));
        holder.name.setText(model.getName());

    }

    public void deleteItem(int position){

        DatabaseReference food = getSnapshots().getSnapshot(position).getRef();

        Log.d("Food key:",food.getKey());
        food.removeValue();

        Log.d("Food key:",food.getKey());




    }

    // Function to tell the class about the Card view (here
    // "person.xml")in
    // which the data will be shown
    @NonNull
    @Override
    public foodViewholder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType)
    {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_item_in_meal, parent, false);
        return new foodViewholder(view);
    }

    // Sub Class to create references of the views in Crad
    // view (here "person.xml")
    static class foodViewholder extends RecyclerView.ViewHolder {
        TextView kCal, protein, carbs, fats, name, kcalGrams;

        public foodViewholder(@NonNull View itemView)
        {
            super(itemView);

            kcalGrams = itemView.findViewById(R.id.gramsInLayoutCardVal);
            kCal = itemView.findViewById(R.id.KcalInLayoutCardVal);
            protein = itemView.findViewById(R.id.proteinInCard);
            carbs = itemView.findViewById(R.id.carbsInCard);
            fats = itemView.findViewById(R.id.fatsInCard);
            name = itemView.findViewById(R.id.FoodNameInLayoutCard);
        }
    }




}
