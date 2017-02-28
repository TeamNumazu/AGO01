package com.example.silve.ago01.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.ArraySwipeAdapter;
import com.example.silve.ago01.R;

public class SwipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipe_view);

        final String[] aqours = {
                "Chika Takami", "You Watanabe", "Riko Sakurauchi",
                "Ruby Kurosawa", "Hanamaru Kunikida", "Yoshiko Tsushima",
                "Dia Kurosawa", "Kanan Matsuura", "Mari Ohara"
        };
        ArraySwipeAdapter<String> arraySwipeAdapter= new ArraySwipeAdapter<String>(this, R.layout.swipe_view, aqours) {
            @Override
            public int getSwipeLayoutResourceId(int position) {
                return 0;
            }
        };

        SwipeLayout swipeLayout =  (SwipeLayout)findViewById(R.id.sample1);
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Right, findViewById(R.id.bottom_wrapper_2));

        swipeLayout.findViewById(R.id.star2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SwipeActivity.this, "Star", Toast.LENGTH_SHORT).show();
            }
        });

        swipeLayout.findViewById(R.id.trash2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SwipeActivity.this, "Trash Bin", Toast.LENGTH_SHORT).show();
            }
        });

        swipeLayout.findViewById(R.id.magnifier2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SwipeActivity.this, "Magnifier", Toast.LENGTH_SHORT).show();
            }
        });

        swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SwipeActivity.this, "Click on surface", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
