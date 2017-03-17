package com.example.silve.ago01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.support.design.widget.TabLayout;
import android.widget.Toast;

import com.example.silve.ago01.activity.SwipeActivity;
import com.example.silve.ago01.models.DataBaseHelper;
import com.example.silve.ago01.models.entity.Category;
import com.example.silve.ago01.models.repository.CategoryRepository;
import com.example.silve.ago01.models.specification.sql.category.CategoriesSpecification;
import com.example.silve.ago01.activity.ItemRegisterActivity;
import com.example.silve.ago01.services.AgostickNotification;
import com.example.silve.ago01.services.ExpireNotifierService;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView listView;

    private static final String[] foods = {
            "たまご", "とうふ", "バナナ", "レタス", "エリンギ", "巨乳",
            "たまご", "とうふ", "バナナ", "レタス", "エリンギ", "巨乳",
            "たまご", "とうふ", "バナナ", "レタス", "エリンギ", "巨乳",
            "たまご", "とうふ", "バナナ", "レタス", "エリンギ", "巨乳",
            "たまご", "とうふ", "バナナ", "レタス", "エリンギ", "巨乳",
            "たまご", "とうふ", "バナナ", "レタス", "エリンギ", "巨乳",
            "たまご", "とうふ", "バナナ", "レタス", "エリンギ", "巨乳",
            "たまご", "とうふ", "バナナ", "レタス", "エリンギ", "巨乳",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = (TabLayout)  findViewById(R.id.tabs);

        DataBaseHelper dbHelper = new DataBaseHelper(getApplicationContext());
        CategoryRepository cRepository = new CategoryRepository(dbHelper);

        CategoriesSpecification cAllSpec = new CategoriesSpecification();

        List<Category> categories =  cRepository.query(cAllSpec);


        for(Category category :categories){
            tabLayout.addTab(tabLayout.newTab().setText(category.getCategoryName()));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        DataBaseHelper mDbHelper  = new DataBaseHelper(this);
        SQLiteDatabase db =  mDbHelper.getReadableDatabase();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            // 通知のテスト
            AgostickNotification notificator = new AgostickNotification(getApplicationContext());

            String message = "輝きたい！";
            String title = "とぅるるるるるるる";

            notificator.doNotice(message, title);
        } else if (id == R.id.nav_slideshow) {
            // BroadcastReceiverを登録する
            Toast.makeText(this, "Receiverを(AgostickReceiver)を登録します", Toast.LENGTH_LONG).show();
            this.registerReceiver(this.getAgostickReceiver(), new IntentFilter("android.intent.action.TIME_TICK"));
        } else if (id == R.id.nav_manage) {
            // BroadcastReceiverを解除する
            Toast.makeText(this, "Receiverを(AgostickReceiver)を解除します", Toast.LENGTH_LONG).show();
            this.unregisterReceiver(this.getAgostickReceiver());
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void changeItemRegisterDisplay(View view){
        Intent intent = new Intent(this, ItemRegisterActivity.class);
        startActivity(intent);

    }

    public void agostickDisplay(View view)
    {
        Intent intent = new Intent(this, SwipeActivity.class);
        startActivity(intent);
    }

    /**
     * Agostickレシーバーを取得
     *
     * @return BroadcastReceiver
     */
    private BroadcastReceiver getAgostickReceiver()
    {
        BroadcastReceiver agostickReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                // Serviceを開始
                Intent expireNotifierService = new Intent(getApplication(), ExpireNotifierService.class);
                expireNotifierService.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startService(expireNotifierService);
            }
        };

        return agostickReceiver;
    }

}
