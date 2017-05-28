package com.example.silve.ago01.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.example.silve.ago01.R;
import com.example.silve.ago01.models.DataBaseHelper;
import com.example.silve.ago01.models.entity.Item;
import com.example.silve.ago01.models.repository.ItemRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static java.lang.Math.abs;

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

    public void initialize() {
        mIRepository = new ItemRepository(new DataBaseHelper(mContext));

    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.listview_item, null);
        final SwipeLayout swipeLayout = (SwipeLayout) v.findViewById(getSwipeLayoutResourceId(position));
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
                Toast.makeText(mContext, "まだ未実装です！", Toast.LENGTH_SHORT).show();
            }
        });
        v.findViewById(R.id.trash).setOnClickListener(new View.OnClickListener() {
            // 削除おしたとき
            @Override
            public void onClick(View view) {
                for (int i = 0; i < mItemList.size(); i++) {

                    if (mItemManger.isOpen(i)) {
                        Item item = mItemList.get(i);
                        mIRepository.remove(item);
                        mItemList.remove(item);
                        break;

                    }

                }

                notifyDataSetChanged();
                Toast.makeText(mContext, "削除しました", Toast.LENGTH_SHORT).show();

                // スワイプを閉じる
                swipeLayout.close(true, true);
            }

        });

        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
        Item item = mItemList.get(position);

        // インデックス
        TextView itemIndex = (TextView) convertView.findViewById(R.id.position_index);
        String index = String.valueOf(position + 1);
        itemIndex.setText(index);

        // 商品名
        TextView name = (TextView) convertView.findViewById(R.id.position_name);
        name.setText(item.getItemName());

        // 個数
        TextView quantity = (TextView) convertView.findViewById(R.id.position_quantity);
        quantity.setText(String.valueOf(item.getNumber()));

        // 期限
        TextView expire = (TextView) convertView.findViewById(R.id.position_expire);

        String remainingExpiredDays = "";

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            Date expiredDate = format.parse(item.getExpiredAt());
            remainingExpiredDays = calcDaysUntil(expiredDate.getTime());

        } catch (Exception e) {
            //ナイスキャッチ
        }

        expire.setText(remainingExpiredDays);

        ImageView itemView = (ImageView) convertView.findViewById(R.id.item_row_view);

        Uri bitmapUrl = null;

        try {
            File mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "IMG"
            );


            File bitmapFile = new File(mediaStorageDir.getPath() + File.separator + item.getItemImage());

            bitmapUrl = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".provider", bitmapFile);

        } catch (Exception e) {
            Log.d("file open error", e.toString());
        }

        Bitmap resizedBitmap = null;

        try {
            BitmapFactory.Options imageOptions = new BitmapFactory.Options();
            imageOptions.inSampleSize = 8;
            resizedBitmap = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(bitmapUrl),null,imageOptions);

        }catch(Exception e){
            //ナイスキャッチ
        }

        itemView.setImageBitmap(resizedBitmap);

        //開けた日
        TextView openedDayView = (TextView) convertView.findViewById(R.id.opened_at);
        openedDayView.setText(item.getCreatedAt());

        // 経過日数
        TextView openPassed = (TextView) convertView.findViewById(R.id.position_open_passed);

        String pastDays = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            Date itemCreatedAt = format.parse(item.getCreatedAt());

            pastDays = calcPastOpenedDays(itemCreatedAt.getTime());

        } catch (ParseException ex) {
            // ナイスキャッチ！
        }
        openPassed.setText(pastDays);
    }

    /**
     * 登録日から現在までの経過日数を返却する
     *
     * @param createTime
     * @return
     */
    protected String calcPastOpenedDays(long createTime) {
        Date now = new Date();
        return calcDays(createTime, now.getTime());

    }

    /**
     * 登録日から現在までの経過日数を返却する
     *
     * @param expiredDate
     * @return
     */
    protected String calcDaysUntil(long expiredDate) {
        Date now = new Date();
        long nowTime = now.getTime();

        return calcDays(nowTime, expiredDate);

    }

    protected String calcDays(long from, long to) {
        int diffDays = -1;

        // 経過ミリ秒÷(1000ミリ秒×60秒×60分×24時間)。端数切り捨て。
        long diffTime = abs(to - from);
        int dayOfMinSec = 1000 * 60 * 60 * 24;
        diffDays = (int) (diffTime / dayOfMinSec);
        return Integer.toString(diffDays);

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