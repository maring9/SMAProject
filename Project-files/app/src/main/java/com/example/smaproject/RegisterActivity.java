package com.example.smaproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import static com.example.smaproject.CalorieLogic.getCarb;
import static com.example.smaproject.CalorieLogic.getFats;
import static com.example.smaproject.CalorieLogic.getKcalFloss;
import static com.example.smaproject.CalorieLogic.getKcalGain;
import static com.example.smaproject.CalorieLogic.getKcalMaintain;
import static com.example.smaproject.CalorieLogic.getProtein;

public class RegisterActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

    private EditText edEmReg, edPwReg, edAgeReg, edWeightReg, edHeightReg, edNameReg;
    private Spinner edGoalReg, edGenderReg;
    private String edGoalRegToString, edGenderRegToString;
    private Button BtnRegReg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        edEmReg = findViewById(R.id.edEmReg);
        edPwReg = findViewById(R.id.edPwReg);
        edAgeReg = findViewById(R.id.edAgeReg);
        edWeightReg = findViewById(R.id.edWeightReg);
        edHeightReg = findViewById(R.id.edHeightReg);
        edGoalReg = findViewById(R.id.edGoalReg);
        edGenderReg = findViewById(R.id.edGenderReg);
        edNameReg = findViewById(R.id.edNameReg);
        BtnRegReg = findViewById(R.id.BtnRegReg);

        ///dropdown goal logic
        String[] items = new String[]{"Fat loss", "Muscle gain", "Maintain weight"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        edGoalReg.setAdapter(adapter);
        ///

        ///dropdown gender logic
        String[] items2 =new String[]{ "Male", "Female"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items2);
        edGenderReg.setAdapter(adapter2);
        ///
        BtnRegReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerTheUser();
            }
        });
    }

    public void registerTheUser() {

        final String email, password, name;

        email = edEmReg.getText().toString();
        password = edPwReg.getText().toString();
        name = edNameReg.getText().toString();
        final Integer age, weight, height;

        int goal = edGoalReg.getSelectedItemPosition();
        switch (goal) {
            case 0:
                this.edGoalRegToString = "Fat loss";
                // Whatever you want to happen when the first item gets selected
                break;
            case 1:
                this.edGoalRegToString = "Muscle gain";
                // Whatever you want to happen when the second item gets selected
                break;
            case 2:
                this.edGoalRegToString = "Maintain weight";
                // Whatever you want to happen when the thrid item gets selected
                break;
        }

        int gender = edGenderReg.getSelectedItemPosition();
        switch(gender){
            case 0:
                this.edGenderRegToString = "Male";
                break;
            case 1:
                this.edGenderRegToString = "Female";
                break;


        }

        age = Integer.parseInt(edAgeReg.getText().toString());
        weight = Integer.parseInt(edWeightReg.getText().toString());
        height = Integer.parseInt(edHeightReg.getText().toString());


        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter an email address!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter a password!", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();


                            HashMap<String, Object> map = new HashMap<>();

                            map.put("email", email);
                            map.put("name", name);
                            map.put("id", mAuth.getCurrentUser().getUid());
                            map.put("age", age);
                            map.put("gender", edGenderRegToString);
                            map.put("weight", weight);
                            map.put("height", height);
                            map.put("goal", edGoalRegToString);
                            myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("Data").setValue(map);

                            double kcalToPush;

                            if(edGoalRegToString.equals("Fat loss")) {

                                kcalToPush = getKcalFloss(edGenderRegToString, age, weight, height);

                            } else if (edGoalRegToString.equals("Muscle gain")){

                                kcalToPush = getKcalGain(edGenderRegToString, age, weight, height);

                            } else {
                                kcalToPush = getKcalMaintain(edGenderRegToString, age, weight, height);

                            }


                            double fatsToPush = getFats(kcalToPush);
                            double proteinToPush = getProtein(kcalToPush);
                            double carbsToPush = getCarb(kcalToPush);
                            UserNeeds needsToPush = new UserNeeds();
                            needsToPush.setkCal(kcalToPush);
                            needsToPush.setCarbs(carbsToPush);
                            needsToPush.setFats(fatsToPush);
                            needsToPush.setProtein(proteinToPush);

                            myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("Needs").setValue(needsToPush);


                            HashMap <String, Object> map1 = new HashMap<>();
                            String date = CalorieLogic.getDate();

                            map1.put("carbs", 0 );
                            map1.put("fats", 0 );
                            map1.put("protein", 0 );
                            map1.put("kCal", 0 );
                            map1.put("day", date);


                            myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("History").child(date).updateChildren(map1);


                            HashMap <String, Object> map2 = new HashMap<>();


                            map2.put("carbs", 0 );
                            map2.put("fats", 0 );
                            map2.put("protein", 0 );
                            map2.put("kCal", 0 );
                            map2.put("day", date);





                            myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("History").child(date).child("Totals").updateChildren(map2);

                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Registration failed! Please try again later", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }


}