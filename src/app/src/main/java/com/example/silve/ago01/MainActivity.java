package com.example.silve.ago01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.design.widget.TabLayout;
import android.widget.Toast;

import com.example.silve.ago01.fragments.PageFragment;
import com.example.silve.ago01.models.DataBaseHelper;
import com.example.silve.ago01.models.entity.Category;
import com.example.silve.ago01.models.repository.CategoryRepository;
import com.example.silve.ago01.models.specification.sql.category.CategoriesSpecification;
import com.example.silve.ago01.activity.ItemRegisterActivity;
import com.example.silve.ago01.services.ExpireNotifierService;
import com.example.silve.ago01.utils.SwipeDirection;
import com.example.silve.ago01.views.AgostickPagerAdapter;
import com.example.silve.ago01.views.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener, PageFragment.OnFragmentInteractionListener, PageFragment.OnButtonClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.createViewPager();

        // BroadcastReceiver（賞味期限監視サービス）を登録する
        this.registerReceiver(this.getAgostickReceiver(), new IntentFilter("android.intent.action.TIME_TICK"));
    }

    @Override
    public void onResume() {
        super.onResume();

        this.createViewPager();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // BroadcastReceiver（賞味期限監視サービス）を登録する
        this.registerReceiver(this.getAgostickReceiver(), new IntentFilter("android.intent.action.TIME_TICK"));
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

        if (id == R.id.nav_add_category) {
            Toast.makeText(this, "まだ未実装です！", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_toggle_notice) {
            Toast.makeText(this, "まだ未実装です！", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_import) {
            Toast.makeText(this, "まだ未実装です！", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_export) {
            Toast.makeText(this, "まだ未実装です！", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_truncate) {
            Toast.makeText(this, "まだ未実装です！", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Agostickレシーバーを取得
     *
     * @return BroadcastReceiver
     */
    private BroadcastReceiver getAgostickReceiver() {
        BroadcastReceiver agostickReceiver = new BroadcastReceiver() {
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

    /**
     * フローティングボタンクリック
     *
     * @param categoryId
     */
    public void onFloatingButtonClick(int categoryId) {
        Intent intent = new Intent(this, ItemRegisterActivity.class);
        intent.putExtra(ItemRegisterActivity.ARG_PARAM_CATEGORY_ID, categoryId);
        startActivity(intent);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    /**
     * タブ、Pager、リストビューを生成
     *
     * @return void
     */
    private void createViewPager() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        /**
         * カテゴリのタブを生成
         */
        DataBaseHelper dbHelper = new DataBaseHelper(getApplicationContext());
        CategoryRepository cRepository = new CategoryRepository(dbHelper);
        CategoriesSpecification cAllSpec = new CategoriesSpecification();

        List<Category> categories = cRepository.query(cAllSpec);

        final ArrayList<String> tabNames = new ArrayList<>();
        final ArrayList<Long> tabIdList = new ArrayList<>();
        for (Category category : categories) {
            tabNames.add(category.getCategoryName());
            tabIdList.add(category.get_id());
        }

        // xmlからCustomViewPagerを取得
        CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.pager);

        // 表示Pageに必要な項目を設定
        FragmentPagerAdapter adapter = new AgostickPagerAdapter(getSupportFragmentManager(), tabNames, tabIdList);

        // ViewPagerにページを設定
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);

        // ViewPagerのスワイプを制御
        // SwipeListViewのスワイプと操作が重複するためスワイプでの移動を禁止する。
        viewPager.setAllowedSwipeDirection(SwipeDirection.none);

        // ViewPagerをTabLayoutを設定
        tabLayout.setupWithViewPager(viewPager);
    }

}
