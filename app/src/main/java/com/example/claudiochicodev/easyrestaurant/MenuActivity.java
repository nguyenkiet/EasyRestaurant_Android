package com.example.claudiochicodev.easyrestaurant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MenuActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize

        //Get all menus
        testDB(SharedResources.dbHandler.getMenuCount() == 0);  //create some testing menus if the DB is empty.
        SharedResources.menuList = SharedResources.dbHandler.getAllMenus(true);

        // Create the adapter that will return a fragment for each of the tabs
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, SelectTableActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
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
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.tb_table) {
            Intent intent = new Intent(this, SelectTableActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.tb_order) {
            Intent intent = new Intent(this, OrderActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void testDB(boolean bool){
        if (bool){
            //testMenus. These menus will be loaded if the DB is empty for testing purposes.
            Menus menu1 = new Menus("Starters");
            SharedResources.dbHandler.addMenu(menu1);
            Items item1 = new Items("Bread", 2.5f);
            SharedResources.dbHandler.addItem(item1);
            Items item2 = new Items("Cheese", 5.5f);
            SharedResources.dbHandler.addItem(item2);
            Items item3 = new Items("Ham", 5.5f);
            SharedResources.dbHandler.addItem(item3);
            Menus menu2 = new Menus("Main");
            SharedResources.dbHandler.addMenu(menu2);
            Items item4 = new Items("Soup", 4.5f);
            SharedResources.dbHandler.addItem(item4);
            Items item5 = new Items("Chicken", 7.5f);
            SharedResources.dbHandler.addItem(item5);
            Items item6 = new Items("Beef", 8.5f);
            SharedResources.dbHandler.addItem(item6);
            Menus menu3 = new Menus("Desserts");
            menu3.set_id(3);
            SharedResources.dbHandler.addMenu(menu3);
            Items item7 = new Items("Cake", 3.5f);
            SharedResources.dbHandler.addItem(item7);
            Items item8 = new Items("Ice cream", 3.5f);
            SharedResources.dbHandler.addItem(item8);
            Items item9 = new Items("Fruit bowl", 4.0f);
            SharedResources.dbHandler.addItem(item9);
            SharedResources.menuList = SharedResources.dbHandler.getAllMenus(true);
            Menus m = SharedResources.menuList.get(0);
            SharedResources.dbHandler.addMenu_Item(m, item1);
            SharedResources.dbHandler.addMenu_Item(m, item2);
            SharedResources.dbHandler.addMenu_Item(m, item3);
            m = SharedResources.menuList.get(1);
            SharedResources.dbHandler.addMenu_Item(m, item4);
            SharedResources.dbHandler.addMenu_Item(m, item5);
            SharedResources.dbHandler.addMenu_Item(m, item6);
            m = SharedResources.menuList.get(2);
            SharedResources.dbHandler.addMenu_Item(m, item7);
            SharedResources.dbHandler.addMenu_Item(m, item8);
            SharedResources.dbHandler.addMenu_Item(m, item9);
        }
    }
}
