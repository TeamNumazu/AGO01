package com.example.silve.ago01.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.example.silve.ago01.models.DataBaseHelper;
import com.example.silve.ago01.models.entity.Item;
import com.example.silve.ago01.models.repository.ItemRepository;
import com.example.silve.ago01.models.specification.sql.item.FindAllSpecification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 商品期限を監視して通知するやつ
 */
public class ExpireNotifierService extends Service {

    /**
     * 何分おきに実行するか
     * <p>
     * 30なら30分毎、最大59まで設定可。
     */
    private static final int NOTIFIER_EXEC_PER = 1;

    /**
     * 初期化するだけ
     */
    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * サービスで実行させたいコードはここに記述
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "Broadcasting", Toast.LENGTH_LONG).show();

        if (this.shouldTaskStart() == true) {
            Toast.makeText(this, "商品の監視を実行します..", Toast.LENGTH_LONG).show();

            Context context = getApplicationContext();

            // すべての商品をDBから取得
            List<Item> itemList = this.findItemAll(context);

            // 有効期限の切れている商品だけを取得
            List<Item> expiredItemList = this.getExpiredList(itemList);

            if (expiredItemList.size() > 0) {
                // まとめて通知
                this.doBulkNotice(context, expiredItemList);
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 処理を実行する条件
     *
     * @return boolean
     */
    private boolean shouldTaskStart() {
        CharSequence textMinutes = android.text.format.DateFormat.format("mm", Calendar.getInstance());
        int intMinutes = Integer.parseInt(textMinutes.toString());

        return (intMinutes % NOTIFIER_EXEC_PER) == 0;
    }

    /**
     * 商品一覧をすべて取得
     *
     * @return
     */
    private List<Item> findItemAll(Context context) {
        // リポジトリ用意
        DataBaseHelper dbHelper = new DataBaseHelper(context);
        ItemRepository cRepository = new ItemRepository(dbHelper);

        // Specification
        FindAllSpecification iAllSpec = new FindAllSpecification();

        // 取得実行
        List<Item> itemList = cRepository.query(iAllSpec);

        return itemList;
    }

    /**
     * 有効期限が今日より前日の商品リストを取得します
     *
     * @param itemList
     * @return
     */
    private List<Item> getExpiredList(List<Item> itemList) {
        List<Item> expiredList = new ArrayList<>();

        // 比較用日付フォーマット
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

        for (int i = 0, size = itemList.size(); i < size; i++) {
            Item item = itemList.get(i);
            String itemExpireAt = item.getExpiredAt();

            if (itemExpireAt == null) {
                continue;
            }

            try {
                // 現在日付
                Date now = new Date();
                Date itemExpiredAt = format.parse(item.getExpiredAt());

                // 比較
                if (now.after(itemExpiredAt)) {
                    // 今日より前日ならリストに追加
                    expiredList.add(item);
                }
            } catch (ParseException ex) {
                // ナイスキャッチ！
            }

        }

        return expiredList;
    }

    /**
     * 通知を行います
     *
     * @param context
     * @param itemList
     */
    private void doBulkNotice(Context context, List<Item> itemList)
    {
        // 賞味期限切れの商品名（改行で結合）
        List itemNameList = new ArrayList<>();

        for (int i = 0, size = itemList.size(); i < size; i++) {
            Item item = itemList.get(i);
            itemNameList.add(item);
        }

        // 通知の準備
        AgostickNotification notifier = new AgostickNotification(context);
        String title = "次の商品が賞味期限切れです！";

        // 通知を実行
        notifier.doNotice(itemNameList, title);
    }

}
