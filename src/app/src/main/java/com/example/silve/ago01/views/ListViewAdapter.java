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
import com.example.silve.ago01.models.DataBaseHelper;
import com.example.silve.ago01.models.entity.Item;
import com.example.silve.ago01.models.repository.ItemRepository;

import java.util.List;

public class ListViewAdapter extends BaseSwipeAdapter {

    private Context mContext;
    private List<Item> mItemList;
    private ItemRepository mIRepository;
    private int mCategoryId;

    public ListViewAdapter(Context mContext, List<Item> itemList, int categoryId) {
        this.mContext = mContext;
        this.mItemList = itemList;
        this.mCategoryId = categoryId;
        initialize();
    }

    public void initialize(){
        mIRepository = new ItemRepository(new DataBaseHelper(mContext));

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
                // スライドしてメニューを開いたとき
            }
        });
        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                // ダブルタップしたとき
            }
        });
        v.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            // 編集おしたとき
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "click edit", Toast.LENGTH_SHORT).show();
            }
        });
        v.findViewById(R.id.trash).setOnClickListener(new View.OnClickListener() {
            // 削除おしたとき
            @Override
            public void onClick(View view) {


                Item item = mItemList.get(0);
                mIRepository.remove(item);
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
        name.setText((position + 1) + ". " + item.getItemName() + "    CategoryID = " + this.mCategoryId);

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