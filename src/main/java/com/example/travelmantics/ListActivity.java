package com.example.travelmantics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static com.example.travelmantics.FirebaseUtil.mFirebaseAuth;

public class ListActivity extends AppCompatActivity {
    //String snackbarMessage;

    ArrayList<TravelDeal> deals;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildListener;

    ProgressBar myProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        myProgressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_activity_menu, menu);
        MenuItem insertMenu = menu.findItem(R.id.insert_menu);

//  Original location of admin check code - moved to onPrepareOptionsMenu
/*
        if (FirebaseUtil.isAdmin == true) {
            insertMenu.setVisible(true);
        }
        else {
            insertMenu.setVisible(false);
        }
*/
//        Toast.makeText(this, "Welcome " + FirebaseUtil.userDisplayName + "!", Toast.LENGTH_LONG).show();

        View parentLayout = findViewById(android.R.id.content);
        Snackbar mySnackbar = ThemedSnackbar.make(parentLayout, "Welcome " + FirebaseUtil.userDisplayName + "!", Snackbar.LENGTH_LONG).setDuration(5000);
        mySnackbar.show();

/*
        TextView myTextView = (TextView) mySnackbarView.findViewById(R.id.snackbar_text);
        mySnackbar.setActionTextColor(Color.GREEN);
        myTextView.setTextColor(Color.BLUE);
        mySnackbar.show();
*/

//        mySnackbar.getView(parentLayout);
//        mySnackbar.show();
//        mySnackbar.getView(Color.parseColor("#1565c0"));



        //        Snackbar.make(parentLayout, "Welcome " + FirebaseUtil.userDisplayName + "!", Snackbar.LENGTH_LONG).show();

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem insertMenu = menu.findItem(R.id.insert_menu);

        if (FirebaseUtil.isAdmin == true) {
            insertMenu.setVisible(true);
        }
        else {
            insertMenu.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.insert_menu:
                Intent intent = new Intent(this, DealActivity.class);
                startActivity(intent);
                return true;

            case R.id.logout_menu:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                FirebaseUtil.attachListener();
                            }
                        });

                FirebaseUtil.detachListener();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseUtil.detachListener();
    }

    @Override
    protected void onResume() {
        super.onResume();

//        ProgressBar myProgressBar;
//        myProgressBar = (ProgressBar) findViewById(R.id.progressBar);
//        myProgressBar.setVisibility(View.VISIBLE);


/*  Snackbar attempt for delete deal
        // Get the transferred data from source activity.
        View parentLayout = findViewById(android.R.id.content);

        Intent intent = getIntent();
        snackbarMessage = intent.getStringExtra("snackbarMessage");
        if (snackbarMessage != null) {
            //Toast.makeText(this,"snackBar: " + snackbarMessage, Toast.LENGTH_LONG).show();
            Snackbar.make(parentLayout, snackbarMessage, Snackbar.LENGTH_LONG).show();
            snackbarMessage = null;
        }
*/

        FirebaseUtil.openFbReference("traveldeals", this);
        RecyclerView rvDeals = (RecyclerView) findViewById(R.id.rv_deals);
        final DealAdapter adapter = new DealAdapter();
        rvDeals.setAdapter(adapter);
        //LinearLayoutManager dealsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false); //correction - LinearLayout to RecyclerView
        LinearLayoutManager dealsLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvDeals.setLayoutManager(dealsLayoutManager);
        FirebaseUtil.attachListener();


    }

    public void showMenu() {
        invalidateOptionsMenu();
    }
}
