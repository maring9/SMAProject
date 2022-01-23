package com.example.smaproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddFood extends AppCompatActivity {
    EditText edName, edKcal, edGrams, edProt, edCarb, edFat;
    Button btnAdd;

    DatabaseReference rootRef;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        rootRef = FirebaseDatabase.getInstance().getReference();




        edName = findViewById(R.id.edAddFoodName);
        edKcal = findViewById(R.id.edAddFoodKcal);
        edGrams = findViewById(R.id.edAddFoodGrams);
        edProt = findViewById(R.id.edAddFoodProt);
        edCarb = findViewById(R.id.edAddFoodCarbs);
        edFat = findViewById(R.id.edAddFoodFats);

        btnAdd = findViewById(R.id.BtnAddFoodAdd);



        onclickeryyy();



    }



    public void onclickeryyy(){

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name;
                Double kc, prot, carb, fat;
                Integer  gram;

                name = edName.getText().toString();
                kc = Double.valueOf(edKcal.getText().toString());
                prot = Double.valueOf(edProt.getText().toString());
                carb = Double.valueOf(edCarb.getText().toString());
                fat = Double.valueOf(edFat.getText().toString());
                gram = Integer.valueOf(edGrams.getText().toString());


                Food food = new Food();
                food.setGrams(gram);
                food.setName(name);
                food.setKcal(kc);
                food.setProtein(prot);
                food.setCarbs(carb);
                food.setFats(fat);


                rootRef.child("Foods").child(name).setValue(food);


            }
        });





    }
}