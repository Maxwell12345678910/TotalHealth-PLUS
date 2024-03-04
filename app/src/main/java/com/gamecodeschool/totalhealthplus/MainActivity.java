package com.gamecodeschool.totalhealthplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    String result = "";

    private BottomNavigationView bottomNavigationView;
    private SparseArray<Fragment> fragmentMap = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) this);

        // Initialize fragments
        fragmentMap.put(R.id.person, new FirstFragment());
        fragmentMap.put(R.id.home, new SecondFragment());
        fragmentMap.put(R.id.settings, new ThirdFragment());

        // Set the initial fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flFragment, fragmentMap.get(R.id.home))
                    .commit();
        }

        databaseHelper = new DatabaseHelper(this);
//        use db browser to view data but runnig again creates duplicates in db
//        databaseHelper.insertExercise("running", 67);
        databaseHelper.insertFood("Hardboiled Egg","Protein", 70, 50);

        result = databaseHelper.selectFoods();
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        Log.d("RESULT WAS: ", result);
    }


    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = fragmentMap.get(item.getItemId());
        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flFragment, selectedFragment)
                    .commit();
            return true;
        }
        return false;
    }


}