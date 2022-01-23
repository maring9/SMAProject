package com.example.smaproject;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;

public class DayAdapter extends FirebaseRecyclerAdapter<UserNeeds,DayAdapter.UserNeedsViewholder> {


public DayAdapter(@NonNull FirebaseRecyclerOptions<UserNeeds> options)
        {
        super(options);
        }

protected void onBindViewHolder(@NonNull UserNeedsViewholder holder, final int position, @NonNull UserNeeds model)
        {

        holder.kCal.setText(String.valueOf(Math.round(model.getkCal())));
        holder.protein.setText(String.valueOf(Math.round(model.getProtein())));
        holder.fats.setText(String.valueOf(Math.round(model.getFats())));
        holder.carbs.setText(String.valueOf(Math.round(model.getCarbs())));
        holder.day.setText(model.getDay());

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
        public UserNeedsViewholder
        onCreateViewHolder(@NonNull ViewGroup parent,
        int viewType)
        {
        View view
        = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.history_item, parent, false);
        return new UserNeedsViewholder(view);
        }

// Sub Class to create references of the views in Crad
// view (here "person.xml")
static class UserNeedsViewholder extends RecyclerView.ViewHolder {

    TextView kCal, protein, carbs, fats, name, day;

    public UserNeedsViewholder(@NonNull View itemView)
    {
        super(itemView);

        day = itemView.findViewById(R.id.FoodNameInLayoutCardHistory);
        kCal = itemView.findViewById(R.id.KcalInLayoutCardValHistory);
        protein = itemView.findViewById(R.id.proteinInCardHistory);
        carbs = itemView.findViewById(R.id.carbsInCardHistory);
        fats = itemView.findViewById(R.id.fatsInCardHistory);
        name = itemView.findViewById(R.id.FoodNameInLayoutCardHistory);
    }
}




}

