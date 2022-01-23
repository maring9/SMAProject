package com.example.smaproject.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smaproject.CalorieLogic;
import com.example.smaproject.R;
import com.example.smaproject.UserClass;
import com.example.smaproject.UserNeeds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public class HomeFragment extends Fragment {



    DatabaseReference rootRef, foodRef, needsRef,totalNumbersRef;
    FirebaseUser currentUser;

    TextView kcalCurr, protCurr, fatCurr, carbCurr;
    TextView kcalGoal, protGoal, fatGoal, carbGoal;
    PieChart pieChart;
    TextView InsertNameHome;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_home, null);

        pieChart = rootView.findViewById(R.id.piechart);
        kcalCurr=rootView.findViewById(R.id.KcalInLayoutCardValHome);
        protCurr=rootView.findViewById(R.id.proteinInCardHome);
        carbCurr=rootView.findViewById(R.id.carbsInCardHome);
        fatCurr=rootView.findViewById(R.id.fatsInCardHome);

        InsertNameHome=rootView.findViewById(R.id.InsertNameHome);


        kcalGoal=rootView.findViewById(R.id.KcalInLayoutCardValHomeGoal);
        protGoal=rootView.findViewById(R.id.proteinInCardHomeGoal);
        carbGoal=rootView.findViewById(R.id.carbsInCardHomeGoal);
        fatGoal=rootView.findViewById(R.id.fatsInCardHomeGoal);





        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        assert currentUser != null;
        final String user_id = currentUser.getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();
        foodRef = rootRef.child("Foods");
        needsRef = rootRef.child("Users").child(user_id).child("Needs");

        final String date = CalorieLogic.getDate(); //current date


        totalNumbersRef = rootRef.child("Users").child(user_id).child("History").child(date).child("Totals");

        rootRef.child("Users").child(user_id).child("Data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserClass dataGot = snapshot.getValue(UserClass.class);

                InsertNameHome.setText(dataGot.getName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        totalNumbersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserNeeds gottenMealTotals = new UserNeeds();
                gottenMealTotals = snapshot.getValue(UserNeeds.class);

                kcalCurr.setText(String.valueOf(Math.round(gottenMealTotals.getkCal())));
                protCurr.setText(String.valueOf(Math.round(gottenMealTotals.getProtein())));
                fatCurr.setText(String.valueOf(Math.round(gottenMealTotals.getFats())));
                carbCurr.setText(String.valueOf(Math.round(gottenMealTotals.getCarbs())));

                pieChart.addPieSlice(
                        new PieModel(
                                "Carbs",
                                Integer.parseInt(carbCurr.getText().toString()),
                                Color.parseColor("#FF0000")));
                pieChart.addPieSlice(
                        new PieModel(
                                "Protein",
                                Integer.parseInt(protCurr.getText().toString()),
                                Color.parseColor("#2196F3")));
                pieChart.addPieSlice(
                        new PieModel(
                                "Fats",
                                Integer.parseInt(fatCurr.getText().toString()),
                                Color.parseColor("#FF5722")));


                pieChart.startAnimation();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        needsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserNeeds gottenMealTotals2 = new UserNeeds();
                gottenMealTotals2 = snapshot.getValue(UserNeeds.class);


                double kcGot = gottenMealTotals2.getkCal();

                Log.d("KcVal: ", String.valueOf(kcGot));

                Log.d("KcValRounded: ", String.valueOf(Math.round(kcGot)));
                kcalGoal.setText(String.valueOf((kcGot)));
                protGoal.setText(String.valueOf(Math.round(gottenMealTotals2.getProtein())));
                fatGoal.setText(String.valueOf(Math.round(gottenMealTotals2.getFats())));
                carbGoal.setText(String.valueOf(Math.round(gottenMealTotals2.getCarbs())));




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return rootView;
    }
}