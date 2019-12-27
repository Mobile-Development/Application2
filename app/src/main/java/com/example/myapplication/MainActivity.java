package com.example.myapplication;

import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.example.myapplication.Greendao.DBHelper;
import com.example.myapplication.Greendao.entry.UserInfo;
import com.example.myapplication.ui.BaseActivity;
import com.example.myapplication.ui.login.LoginActivity;
import com.example.myapplication.ui.login.RegisterActivity;
import com.example.myapplication.utils.DateUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.hardware.SensorManager;
import android.util.Log;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        login();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    private void login(){
        UserInfo userInfo = DBHelper.getUserInfo();
        if(userInfo == null){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            //startActivityForResult(intent,0);
            startActivity(intent);
        }

    }

}
