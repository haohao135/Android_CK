package com.example.myapplication.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.fragment.Account_Manager_Fragment;
import com.example.myapplication.fragment.MovieFragment;
import com.example.myapplication.fragment.Movie_Manager_Fragment;
import com.example.myapplication.fragment.Showtime_manager_Fragment;
import com.example.myapplication.fragment.TheaterFragment;
import com.example.myapplication.fragment.Theater_Manager_Fragment;
import com.example.myapplication.fragment.Ticket_Manager_Fragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Admin_home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    private static final int FRAGMENT_MOVIE = 1;
    private static final int FRAGMENT_THEATER = 2;
    private static final int FRAGMENT_TICKET = 3;
    private static final int FRAGMENT_ACCOUNT = 4;
    private static final int FRAGMENT_SHOWTIME = 5;
    private int curFragment = FRAGMENT_MOVIE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Admin");
        drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(new Movie_Manager_Fragment());
        navigationView.getMenu().findItem(R.id.movie_manager).setChecked(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.movie_manager){
            if(curFragment!=FRAGMENT_MOVIE){
                replaceFragment(new Movie_Manager_Fragment());
                curFragment = FRAGMENT_MOVIE;
                Log.e("TAG", curFragment+"");
            }
        } else if(item.getItemId()==R.id.theater_manager){
            if(curFragment!=FRAGMENT_THEATER){
                replaceFragment(new Theater_Manager_Fragment());
                curFragment = FRAGMENT_THEATER;
                Log.e("TAG", curFragment+"");
            }
        } else if(item.getItemId()==R.id.ticket_manager){
            if(curFragment!=FRAGMENT_TICKET){
                replaceFragment(new Ticket_Manager_Fragment());
                curFragment = FRAGMENT_TICKET;
                Log.e("TAG", curFragment+"");
            }
        }else if(item.getItemId()==R.id.account_manager){
            if(curFragment!=FRAGMENT_ACCOUNT){
                replaceFragment(new Account_Manager_Fragment());
                curFragment = FRAGMENT_ACCOUNT;
                Log.e("TAG", curFragment+"");
            }
        } else if(item.getItemId()==R.id.showtime_manager){
            if(curFragment!=FRAGMENT_SHOWTIME){
                replaceFragment(new Showtime_manager_Fragment());
                curFragment = FRAGMENT_SHOWTIME;
                Log.e("TAG", curFragment+"");
            }
        } else if(item.getItemId()==R.id.logout_manager){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }
}