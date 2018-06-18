package com.dmk.busara_androidtask;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.ISyncModel;
import net.SyncModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

import adapter.CategoriesAdapter;
import model.Category;
import model.Location;


public class MainActivity extends AppCompatActivity {

    private ArrayList<Category> categories = new ArrayList<>();
    private ArrayList<Location> locationList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final GridView gridCategories =  findViewById(R.id.gridCategories);

        final ProgressBar progressBar = findViewById(R.id.progressBar);

        final TextView txtUser = findViewById(R.id.txtUser);

        final SyncModel syncModel = new SyncModel(this);

        syncModel.setLocationsDelegate(new ISyncModel.LocationsDelegate() {
            @Override
            public void onResponse(ArrayList<Location> locations) {
                locationList = locations;
                syncModel.getCurrentUser();//get current user
            }
        });

        syncModel.setCurrentUserDelegate(new ISyncModel.CurrentUserDelegate() {
            @Override
            public void onResponse(String first_name, String last_name, int location_id) {

                String user_location = "";
               //get user location
               for(Location location: locationList) {
                   if(location.id == location_id){
                      user_location = location.name ;
                      break;
                   }
               }

               txtUser.setText("Welcome "+last_name+", "+first_name+" \n Location: "+user_location);
            }
        });
        syncModel.getLocations();//get locations



        syncModel.setCategoriesDelegate(new ISyncModel.CategoriesDelegate() {
            @Override
            public void onReponse(ArrayList<Category> _categories) {
                progressBar.setVisibility(View.GONE);
                categories = _categories;
                gridCategories.setAdapter(new CategoriesAdapter(MainActivity.this,categories));
            }
        });
        syncModel.getCategories();


        gridCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(MainActivity.this,VideoListActivity.class)
                        .putExtra("category_id",categories.get(i).id)
                .putExtra("title",categories.get(i).name)
                .putExtra("desc",categories.get(i).description)
                );
            }
        });


    }
}
