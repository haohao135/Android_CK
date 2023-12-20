package com.example.myapplication.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.activity.Buy_ticket;
import com.example.myapplication.activity.List_movie;
import com.example.myapplication.activity.ViewShowtime;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 * @noinspection ALL, deprecation
 */
public class UserFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    TextView helloName;
    ImageView imageView;
    DrawerLayout drawerLayout;
    Switch switchLanguage;
    NavigationView navigationView;
    TextView logout;
    FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        db = FirebaseFirestore.getInstance();
        drawerLayout = view.findViewById(R.id.drawerLayout2);
        navigationView = view.findViewById(R.id.navigationView2);
        imageView = view.findViewById(R.id.iconImageViewMenu2);
        logout = view.findViewById(R.id.logout);
        helloName = view.findViewById(R.id.HelloName);
        switchLanguage = view.findViewById(R.id.switch_language);
        if (Locale.getDefault().getLanguage().equals("vi")) {
            switchLanguage.setOnCheckedChangeListener((buttonView, isChecked) -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                if (isChecked) {
                    builder.setMessage("Bạn có chắc muốn chuyển ngôn ngữ không?")
                            .setPositiveButton("OK", (dialog, which) -> {
                                setLanguage("en");
                                switchLanguage.setChecked(false);
                            })
                            .setNegativeButton("Cancel", (dialog, which) -> {
                                dialog.dismiss();
                            });
                    builder.create().show();
                }
            }); //
        } else {
            switchLanguage.setOnCheckedChangeListener((buttonView, isChecked) -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                if (isChecked) {
                    builder.setMessage("Are you sure to change language?")
                            .setPositiveButton("OK", (dialog, which) -> {
                                setLanguage("vi");
                                switchLanguage.setChecked(false);
                            })
                            .setNegativeButton("Cancel", (dialog, which) -> {
                                dialog.dismiss();
                            });
                    builder.create().show();
                }
            });

        }

        if (MovieFragment.login_status == true) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            getUserNameById(user.getUid());
        }

        logout.setMovementMethod(LinkMovementMethod.getInstance());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView);
                } else {
                    drawerLayout.openDrawer(navigationView);
                }
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.movie) {
                    startActivity(new Intent(getContext(), List_movie.class));
                } else if (itemId == R.id.buy_ticket) {
                    startActivity(new Intent(getContext(), Buy_ticket.class));
                } else if (itemId == R.id.showtime) {
                    startActivity(new Intent(getContext(), ViewShowtime.class));
                }
                return true;
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
                return;
            }
        });
        return view;
    }

    public void setLanguage(String language) {
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        ((Activity) getContext()).recreate();
    }

    public void logout() {
        MovieFragment.login_status = false;
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));
        getActivity().finish();
    }

    public void getUserNameById(String id) {
        CollectionReference moviesCollectionRef = db.collection("Users");
        Query query = moviesCollectionRef.whereEqualTo("id", id);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String s = documentSnapshot.getString("username");
                updateHelloName(s);
            }
        });
    }

    private void updateHelloName(String name) {
        helloName.setText(name);
    }
}