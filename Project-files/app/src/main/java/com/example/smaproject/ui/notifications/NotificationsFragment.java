package com.example.smaproject.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smaproject.EditActivity;
import com.example.smaproject.History;
import com.example.smaproject.Home;
import com.example.smaproject.MainActivity;
import com.example.smaproject.R;
import com.example.smaproject.UserClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationsFragment extends Fragment {

    DatabaseReference dataRef;
    FirebaseUser currentUser;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_notifications, null);

        /*CardViewLogic*/
         final TextView nameToDisplay, ageToDisplay, weightToDisplay, heightToDisplay, goalToDisplay;

        nameToDisplay = rootView.findViewById(R.id.nameShowSettings);
        ageToDisplay = rootView.findViewById(R.id.ageShowSettings);
        weightToDisplay = rootView.findViewById(R.id.weightShowSettings);
        heightToDisplay = rootView.findViewById(R.id.heightShowSettings);
        goalToDisplay = rootView.findViewById(R.id.goalShowSettings);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        assert currentUser != null;
        String user_id = currentUser.getUid();
        dataRef =FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("Data");


        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                UserClass userClass = dataSnapshot.getValue(UserClass.class);


                System.out.println(userClass.getName());
                nameToDisplay.setText(userClass.getName());
                ageToDisplay.setText(userClass.ageToString());
                weightToDisplay.setText(userClass.weightToString());
                heightToDisplay.setText(userClass.heightToString());
                goalToDisplay.setText(userClass.getGoal());
                }


            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        dataRef.addListenerForSingleValueEvent(eventListener);



       /* nameToDisplay.setText(s);/*
        ageToDisplay.setText(ageGot[0]);
        weightToDisplay.setText();
        heightToDisplay.setText();





        /**/



        /*BUTTON history LOGIC*/
        Button btn1 = (Button) rootView.findViewById(R.id.BtnSettingsHistory);

        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), History.class);
                ((Home) getActivity()).startActivity(intent);

            }
        });

        /**/

        /*BUTTON EDIT LOGIC*/
        Button btn2 = (Button) rootView.findViewById(R.id.BtnSettingsEdit);

        btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), EditActivity.class);
                ((Home) getActivity()).startActivity(intent);

            }
        });
        /**/

        /*BUTTON LOGOUT LOGIC*/
        Button btn3 = (Button) rootView.findViewById(R.id.BtnSettingsLogOut);

        btn3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                ((Home) getActivity()).startActivity(intent);

            }
        });
        /**/
/*
        Button btn4 = (Button) rootView.findViewById(R.id.BtnSettingsAddfood);

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), AddFood.class);
                ((Home) getActivity()).startActivity(intent);

            }
        });*/


        return rootView;



    }

}
