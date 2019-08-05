package com.example.travelmantics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.example.travelmantics.FirebaseUtil.mFirebaseAuth;

public class ListActivity extends AppCompatActivity {
    //String snackbarMessage;

    ArrayList<TravelDeal> deals;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("CustomMessage", "ListActivity onCreate");
        setContentView(R.layout.activity_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("CustomMessage", "ListActivity onCreateOptionsMenu");
        Log.d("CustomMessage", "FirebaseUtil.isAdmin is: " + FirebaseUtil.isAdmin);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_activity_menu, menu);
        MenuItem insertMenu = menu.findItem(R.id.insert_menu);

/*
        if (FirebaseUtil.isAdmin == true) {
            Log.d("CustomMessage", "FirebaseUtil.isAdmin tested TRUE");
            //Log.d("CustomMessage", "ListActivity onCreateOptionsMenu - User is admin, set insertMenu to visible");
            insertMenu.setVisible(true);
            Log.d("CustomMessage", "insertMenu.setVisible set to TRUE");
        }
        else {
            Log.d("CustomMessage", "FirebaseUtil.isAdmin tested FALSE");
//            Log.d("CustomMessage", "ListActivity onCreateOptionsMenu - User is not admin, set insertMenu to invisible");
            insertMenu.setVisible(false);
            Log.d("CustomMessage", "insertMenu.setVisible set to FALSE");
        }
*/
//        Toast.makeText(this, "onCreateOptionsMenu() end : Current user is: " + FirebaseUtil.userDisplayName, Toast.LENGTH_LONG).show();
        Toast.makeText(this, "Welcome " + FirebaseUtil.userDisplayName + "!", Toast.LENGTH_LONG).show();

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d("CustomMessage", "ListActivity onPrepareOptionsMenu");
        Log.d("CustomMessage", "FirebaseUtil.isAdmin is: " + FirebaseUtil.isAdmin);

        MenuItem insertMenu = menu.findItem(R.id.insert_menu);

        if (FirebaseUtil.isAdmin == true) {
            Log.d("CustomMessage", "FirebaseUtil.isAdmin tested TRUE");
            //Log.d("CustomMessage", "ListActivity onCreateOptionsMenu - User is admin, set insertMenu to visible");
            insertMenu.setVisible(true);
            Log.d("CustomMessage", "insertMenu.setVisible set to TRUE");
        }
        else {
            Log.d("CustomMessage", "FirebaseUtil.isAdmin tested FALSE");
//            Log.d("CustomMessage", "ListActivity onCreateOptionsMenu - User is not admin, set insertMenu to invisible");
            insertMenu.setVisible(false);
            Log.d("CustomMessage", "insertMenu.setVisible set to FALSE");
        }

        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {

        Log.d("CustomMessage", "ListActivity onMenuOpened");

        if(menu!=null) {
            Log.d("CustomMessage", "ListActivity onMenuOpened - Menu is ready");
        }
        else {
            Log.d("CustomMessage", "ListActivity onMenuOpened - Menu is null");
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("CustomMessage", "ListActivity onOptionsItemsSelected()");
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
                                //Log.d("Log", "User logged out");
                                Log.d("CustomMessage", "User logged out");
                                FirebaseUtil.attachListener();
                            }
                        });

                FirebaseUtil.detachListener();
                Log.d("CustomMessage", "FirebaseUtil.detachListener()");
/*
                FirebaseUtil.isAdmin=false;
                Log.d("CustomMessage", "FirebaseUtil.isAdmin=false");
                invalidateOptionsMenu();
                Log.d("CustomMessage", "invalidateOptionsMenu()");
*/
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d("CustomMessage", "ListActivity onPause");
        FirebaseUtil.detachListener();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("CustomMessage", "ListActivity onResume");
        Log.d("CustomMessage", "FirebaseUtil.isAdmin is: " + FirebaseUtil.isAdmin);



//        Log.d("CustomMessage", "Menu item getVisibility() returns: " + view.getVisibility());
        //Log.d("CustomMessage", "FirebaseUtil.isAdmin is: " + insertM);


/*
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
        Log.d("CustomMessage", "showMenu method");
        invalidateOptionsMenu();
    }

}
