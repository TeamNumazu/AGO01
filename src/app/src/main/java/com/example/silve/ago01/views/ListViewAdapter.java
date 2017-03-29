package com.example.silve.ago01.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.example.silve.ago01.R;
import com.example.silve.ago01.models.entity.Item;

import java.util.List;

public class ListViewAdapter extends BaseSwipeAdapter {

    private Context mContext;
    private List<Item> mItemList;

    public ListViewAdapter(Context mContext, List<Item> itemList) {
        this.mContext = mContext;
        this.mItemList = itemList;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.listview_item, null);
        SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                Toast.makeText(mContext, "open", Toast.LENGTH_SHORT).show();
            }
        });
        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(mContext, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });
        v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "click delete", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
        Item item = mItemList.get(position);

        // 商品名
        TextView name = (TextView)convertView.findViewById(R.id.position_name);
        name.setText((position + 1) + ". " + item.getItemName());

        // 個数
        TextView quantity = (TextView)convertView.findViewById(R.id.position_quantity);
        quantity.setText(String.valueOf(item.getNumber()));

        // 期限
        TextView expire = (TextView)convertView.findViewById(R.id.position_expire);
        expire.setText(item.getExpiredAt());
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}