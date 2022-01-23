package com.example.smaproject.ui.dashboard;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smaproject.CalorieLogic;
import com.example.smaproject.Food;
import com.example.smaproject.R;
import com.example.smaproject.UserNeeds;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DashboardFragment extends Fragment {

    public static ArrayList<String> food = new ArrayList<>();
    private static ArrayList<Food> foodList = new ArrayList<>();
    Button BtnAddFoodBreakfastMeal, BtnDeleteBreakfastMeal, BtnAddFoodDinnerMeal;
    DatabaseReference rootRef, foodRef, needsRef,breakfastRef,breakfastTotalNumbersRef, lunchRef, lunchTotalNumbersRef, dinnerRef, dinnerTotalNumbersRef, dinnerCurrentDayRef;
    FirebaseUser currentUser;
    DatabaseReference lunchCurrentDayRef;
    private RecyclerView recyclerViewBreakfast;
    FoodAdapter adapter, adapterLunch,adapterDinner;
    Button BtnAddFoodLunchMeal;
    RecyclerView recyclerViewLunchMeals, recyclerViewDinnerMeals;


    TextView breakfastKcal;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_dashboard, null);

        BtnAddFoodBreakfastMeal = rootView.findViewById(R.id.BtnAddFoodBreakfastMeal);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        breakfastKcal = rootView.findViewById(R.id.TxtviewBreakfastKcal);

        assert currentUser != null;
        final String user_id = currentUser.getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();
        foodRef = rootRef.child("Foods");
        needsRef = rootRef.child("Users").child(user_id).child("Needs");

        final String date = CalorieLogic.getDate(); //current date

        

        breakfastRef = rootRef.child("Users").child(user_id).child("History").child(date).child("Breakfast");
        breakfastTotalNumbersRef = rootRef.child("Users").child(user_id).child("History").child(date).child("breakfastTotals");







        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////BEGIN OF BREAKFAST CODE
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        breakfastRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double k = 0;
                double p = 0;
                double f = 0;
                double c = 0;
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    Food foodFromDatabase = dataSnapshot.getValue(Food.class);
                    k = k + foodFromDatabase.getKcal();
                    p = p + foodFromDatabase.getProtein();
                    f = f + foodFromDatabase.getFats();
                    c = c + foodFromDatabase.getCarbs();
                }

                UserNeeds mealMacros = new UserNeeds(k,p,c,f);
                breakfastTotalNumbersRef.setValue(mealMacros);
                breakfastKcal.setText(String.valueOf(Math.round(mealMacros.getkCal())));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        ////reclyerView breakfast code                                                  START
        recyclerViewBreakfast = rootView.findViewById(R.id.recyclerViewBreakfastMeals);

        DatabaseReference breakFastCurrentDayRef= rootRef.child("Users").child(user_id).child("History").child(date).child("Breakfast");
        recyclerViewBreakfast.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>().setQuery(breakFastCurrentDayRef, Food.class).build();


        adapter = new FoodAdapter(options);
        recyclerViewBreakfast.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerViewBreakfast);

        ////reclyerView breakfast code                                                          END

        foodRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                food.clear();

                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    // String david = dataSnapshot.getChildrenCount()+"";
                    food.add(dataSnapshot1.getKey());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        //foodRef.child("Plazma").setValue(new Food(39.8,20.3,50.6,"Plazma",300.3,"grams",100));
       // foodRef.child("Chicken").setValue(new Food(4,2,140,"Chicken",25,"grams",100));
        /*breakfast buttons*/
        BtnAddFoodBreakfastMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add Food");
                // set the custom layout
                final View customLayout = getLayoutInflater().inflate(R.layout.dialog_addfood, null);

                final Spinner spinnerFood = customLayout.findViewById(R.id.EdFoodChoiceDialog);

                for(int i = 0; i< food.size(); i++){
                    System.out.println( food.get(i));
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, food);
                spinnerFood.setAdapter(adapter);
                builder.setView(customLayout);

                // add a button

                Button btnDialogAddFood = customLayout.findViewById(R.id.BtnAddFoodPopup);
                final Button btnDialogCancel = customLayout.findViewById(R.id.BtnCancelPopup);
                final EditText edGetGrams = customLayout.findViewById(R.id.EdGramsDialog);

                builder.setCancelable(true);

                final AlertDialog dialog = builder.create();
                btnDialogAddFood.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //getting the grams of the user
                        final int g1 = Integer.parseInt(edGetGrams.getText().toString());
                        final double grams = g1;
                        final String getFoodSelected = spinnerFood.getSelectedItem().toString();


                        foodRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                    Food foodFromDatabase = dataSnapshot.getValue(Food.class);

                                    if(foodFromDatabase.getName().equals(getFoodSelected)){

                                        Food addingThisFood = new Food();

                                        addingThisFood.setName(foodFromDatabase.getName());
                                        addingThisFood.setKcal(foodFromDatabase.getKcal() * (double)(grams / 100));
                                        addingThisFood.setProtein(foodFromDatabase.getProtein() * (double)(grams / 100));
                                        addingThisFood.setFats(foodFromDatabase.getFats() * (double)(grams / 100));
                                        addingThisFood.setCarbs(foodFromDatabase.getCarbs() * (double)(grams / 100));
                                        addingThisFood.setGrams(g1);
                                        addingThisFood.setQt(foodFromDatabase.getQt());





                                        rootRef.child("Users").child(user_id).child("History").child(date).child("Breakfast").push().setValue(addingThisFood);


                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });



                        //rootRef.child("Users").child(user_id).child("History").child(date).child("Breakfast").updateChildren();




                        dialog.dismiss();
                    }
                });


                btnDialogCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.cancel();

                    }
                });

                dialog.show();
            }
        }
        );



        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////END OF BREAKFAST CODE



        lunchRef = rootRef.child("Users").child(user_id).child("History").child(date).child("Lunch");
        lunchTotalNumbersRef = rootRef.child("Users").child(user_id).child("History").child(date).child("lunchTotals");


        final TextView lunchKcal = rootView.findViewById(R.id.TxtviewLunchKcal);


        BtnAddFoodLunchMeal = rootView.findViewById(R.id.BtnAddFoodLunchMeal);

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////BEGIN OF LUNCH CODE
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        lunchRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double k = 0;
                double p = 0;
                double f = 0;
                double c = 0;
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {

                    Food foodFromDatabase = dataSnapshot.getValue(Food.class);
                    k = k + foodFromDatabase.getKcal();
                    p = p + foodFromDatabase.getProtein();
                    f = f + foodFromDatabase.getFats();
                    c = c + foodFromDatabase.getCarbs();
                }

                UserNeeds mealMacros = new UserNeeds(k,p,c,f);
                lunchTotalNumbersRef.setValue(mealMacros);
                lunchKcal.setText(String.valueOf(Math.round(mealMacros.getkCal())));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        ////reclyerView breakfast code                                                  START
        recyclerViewLunchMeals = rootView.findViewById(R.id.recyclerViewLunchMeals);

        lunchCurrentDayRef= rootRef.child("Users").child(user_id).child("History").child(date).child("Lunch");
        recyclerViewLunchMeals.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<Food> optionsLunch = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(lunchCurrentDayRef, Food.class)
                .build();


        adapterLunch = new FoodAdapter(optionsLunch);
        recyclerViewLunchMeals.setAdapter(adapterLunch);



        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapterLunch.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerViewLunchMeals);
        ////reclyerView breakfast code                                                          END



        //foodRef.child("Plazma").setValue(new Food(39.8,20.3,50.6,"Plazma",300.3,"grams",100));
        // foodRef.child("Chicken").setValue(new Food(4,2,140,"Chicken",25,"grams",100));
        /*breakfast buttons*/
        BtnAddFoodLunchMeal.setOnClickListener(new View.OnClickListener() {
                                                       @Override
                                                       public void onClick(View v) {
                                                           // Create an alert builder
                                                           AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                           builder.setTitle("Add Food");
                                                           // set the custom layout
                                                           final View customLayout = getLayoutInflater().inflate(R.layout.dialog_addfood, null);

                                                           final Spinner spinnerFood = customLayout.findViewById(R.id.EdFoodChoiceDialog);

                                                           for(int i = 0; i< food.size(); i++){
                                                               System.out.println( food.get(i));
                                                           }

                                                           ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, food);
                                                           spinnerFood.setAdapter(adapter);
                                                           builder.setView(customLayout);

                                                           // add a button

                                                           Button btnDialogAddFood = customLayout.findViewById(R.id.BtnAddFoodPopup);
                                                           final Button btnDialogCancel = customLayout.findViewById(R.id.BtnCancelPopup);
                                                           final EditText edGetGrams = customLayout.findViewById(R.id.EdGramsDialog);

                                                           builder.setCancelable(true);

                                                           final AlertDialog dialog = builder.create();
                                                           btnDialogAddFood.setOnClickListener(new View.OnClickListener() {
                                                               @Override
                                                               public void onClick(View v) {
                                                                   //getting the grams of the user
                                                                   final int g1 = Integer.parseInt(edGetGrams.getText().toString());
                                                                   final double grams = g1;
                                                                   final String getFoodSelected = spinnerFood.getSelectedItem().toString();


                                                                   foodRef.addValueEventListener(new ValueEventListener() {
                                                                       @Override
                                                                       public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                           for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                                                               Food foodFromDatabase = dataSnapshot.getValue(Food.class);

                                                                               if(foodFromDatabase.getName().equals(getFoodSelected)){

                                                                                   Food addingThisFood = new Food();

                                                                                   addingThisFood.setName(foodFromDatabase.getName());
                                                                                   addingThisFood.setKcal(foodFromDatabase.getKcal() * (double)(grams / 100));
                                                                                   addingThisFood.setProtein(foodFromDatabase.getProtein() * (double)(grams / 100));
                                                                                   addingThisFood.setFats(foodFromDatabase.getFats() * (double)(grams / 100));
                                                                                   addingThisFood.setCarbs(foodFromDatabase.getCarbs() * (double)(grams / 100));
                                                                                   addingThisFood.setGrams(g1);
                                                                                   addingThisFood.setQt(foodFromDatabase.getQt());





                                                                                   rootRef.child("Users").child(user_id).child("History").child(date).child("Lunch").push().setValue(addingThisFood);


                                                                               }
                                                                           }
                                                                       }

                                                                       @Override
                                                                       public void onCancelled(@NonNull DatabaseError error) {

                                                                       }
                                                                   });



                                                                   //rootRef.child("Users").child(user_id).child("History").child(date).child("Breakfast").updateChildren();




                                                                   dialog.dismiss();
                                                               }
                                                           });


                                                           btnDialogCancel.setOnClickListener(new View.OnClickListener() {
                                                               @Override
                                                               public void onClick(View v) {

                                                                   dialog.cancel();

                                                               }
                                                           });

                                                           dialog.show();
                                                       }
                                                   }
        );





        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////END OF LUNCH CODE


        dinnerRef = rootRef.child("Users").child(user_id).child("History").child(date).child("Dinner");
        dinnerTotalNumbersRef = rootRef.child("Users").child(user_id).child("History").child(date).child("dinnerTotals");


        final TextView dinnerKcal = rootView.findViewById(R.id.TxtviewDinnerKcal);


        BtnAddFoodDinnerMeal = rootView.findViewById(R.id.BtnAddFoodDinnerMeal);


        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////BEGIN OF DINNER CODE
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        dinnerRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double k = 0;
                double p = 0;
                double f = 0;
                double c = 0;

                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    Food foodFromDatabase = dataSnapshot.getValue(Food.class);
                    k = k + foodFromDatabase.getKcal();
                    p = p + foodFromDatabase.getProtein();
                    f = f + foodFromDatabase.getFats();
                    c = c + foodFromDatabase.getCarbs();
                }

                UserNeeds mealMacros = new UserNeeds(k,p,c,f);
                dinnerTotalNumbersRef.setValue(mealMacros);
                dinnerKcal.setText(String.valueOf(Math.round(mealMacros.getkCal())));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        ////reclyerView breakfast code                                                  START
        recyclerViewDinnerMeals = rootView.findViewById(R.id.recyclerViewDinnerMeals);

        dinnerCurrentDayRef= rootRef.child("Users").child(user_id).child("History").child(date).child("Dinner");
        recyclerViewDinnerMeals.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<Food> optionsDinner = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(dinnerCurrentDayRef, Food.class)
                .build();


        adapterDinner = new FoodAdapter(optionsDinner);
        recyclerViewDinnerMeals.setAdapter(adapterDinner);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapterDinner.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerViewDinnerMeals);
        ////reclyerView breakfast code                                                          END



        //foodRef.child("Plazma").setValue(new Food(39.8,20.3,50.6,"Plazma",300.3,"grams",100));
        // foodRef.child("Chicken").setValue(new Food(4,2,140,"Chicken",25,"grams",100));
        /*breakfast buttons*/
        BtnAddFoodDinnerMeal.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       // Create an alert builder
                                                       AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                       builder.setTitle("Add Food");
                                                       // set the custom layout
                                                       final View customLayout = getLayoutInflater().inflate(R.layout.dialog_addfood, null);

                                                       final Spinner spinnerFood = customLayout.findViewById(R.id.EdFoodChoiceDialog);

                                                       for(int i = 0; i< food.size(); i++){
                                                           System.out.println( food.get(i));
                                                       }

                                                       ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, food);
                                                       spinnerFood.setAdapter(adapter);
                                                       builder.setView(customLayout);

                                                       // add a button

                                                       Button btnDialogAddFood = customLayout.findViewById(R.id.BtnAddFoodPopup);
                                                       final Button btnDialogCancel = customLayout.findViewById(R.id.BtnCancelPopup);
                                                       final EditText edGetGrams = customLayout.findViewById(R.id.EdGramsDialog);

                                                       builder.setCancelable(true);

                                                       final AlertDialog dialog = builder.create();
                                                       btnDialogAddFood.setOnClickListener(new View.OnClickListener() {
                                                           @Override
                                                           public void onClick(View v) {
                                                               //getting the grams of the user
                                                               final int g1 = Integer.parseInt(edGetGrams.getText().toString());
                                                               final double grams = g1;
                                                               final String getFoodSelected = spinnerFood.getSelectedItem().toString();


                                                               foodRef.addValueEventListener(new ValueEventListener() {
                                                                   @Override
                                                                   public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                       for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                                                           Food foodFromDatabase = dataSnapshot.getValue(Food.class);

                                                                           if(foodFromDatabase.getName().equals(getFoodSelected)){

                                                                               Food addingThisFood = new Food();

                                                                               addingThisFood.setName(foodFromDatabase.getName());
                                                                               addingThisFood.setKcal(foodFromDatabase.getKcal() * (double)(grams / 100));
                                                                               addingThisFood.setProtein(foodFromDatabase.getProtein() * (double)(grams / 100));
                                                                               addingThisFood.setFats(foodFromDatabase.getFats() * (double)(grams / 100));
                                                                               addingThisFood.setCarbs(foodFromDatabase.getCarbs() * (double)(grams / 100));
                                                                               addingThisFood.setGrams(g1);
                                                                               addingThisFood.setQt(foodFromDatabase.getQt());





                                                                               rootRef.child("Users").child(user_id).child("History").child(date).child("Dinner").push().setValue(addingThisFood);


                                                                           }
                                                                       }
                                                                   }

                                                                   @Override
                                                                   public void onCancelled(@NonNull DatabaseError error) {

                                                                   }
                                                               });



                                                               //rootRef.child("Users").child(user_id).child("History").child(date).child("Breakfast").updateChildren();




                                                               dialog.dismiss();
                                                           }
                                                       });


                                                       btnDialogCancel.setOnClickListener(new View.OnClickListener() {
                                                           @Override
                                                           public void onClick(View v) {

                                                               dialog.cancel();

                                                           }
                                                       });

                                                       dialog.show();
                                                   }
                                               }
        );



        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////END OF DINNER CODE







        DatabaseReference TotalNumbersRef = rootRef.child("Users").child(user_id).child("History").child(date);



        TotalNumbersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double k = 0 ;
                double c = 0;
                double p = 0;
                double f = 0;

                UserNeeds newNeeds = new UserNeeds();
                UserNeeds gottenNeeds;
                gottenNeeds= snapshot.child("breakfastTotals").getValue(UserNeeds.class);

                k = gottenNeeds.getkCal() + k;
                c = gottenNeeds.getCarbs() + c;
                f = gottenNeeds.getFats() + f;
                p = gottenNeeds.getProtein() + p;



                gottenNeeds= snapshot.child("lunchTotals").getValue(UserNeeds.class);
                k = gottenNeeds.getkCal() + k;
                c = gottenNeeds.getCarbs() + c;
                f = gottenNeeds.getFats() + f;
                p = gottenNeeds.getProtein() + p;


                gottenNeeds= snapshot.child("dinnerTotals").getValue(UserNeeds.class);
                k = gottenNeeds.getkCal() + k;
                c = gottenNeeds.getCarbs() + c;
                f = gottenNeeds.getFats() + f;
                p = gottenNeeds.getProtein() + p;



                newNeeds.setCarbs(c);
                newNeeds.setProtein(p);
                newNeeds.setFats(f);
                newNeeds.setkCal(k);
                newNeeds.setDay(date);

                DatabaseReference pushData = rootRef.child("Users").child(user_id).child("History").child(date);
                pushData.child("Totals").setValue(newNeeds);


                Map<String, Object> hashMap = new HashMap<>();
                hashMap.put("carbs",newNeeds.getCarbs());
                hashMap.put("fats", newNeeds.getFats());
                hashMap.put("protein", newNeeds.getProtein());
                hashMap.put("kCal", newNeeds.getkCal());
                hashMap.put("day", newNeeds.getDay());


                pushData.updateChildren(hashMap);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return rootView;
}

    @Override
    public void onStart()
    {
        super.onStart();
        adapter.startListening();
        adapterLunch.startListening();
        adapterDinner.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stoping of the activity
    @Override
    public void onStop()
    {
        super.onStop();
        adapter.stopListening();
        adapterLunch.stopListening();
        adapterDinner.stopListening();
    }

}