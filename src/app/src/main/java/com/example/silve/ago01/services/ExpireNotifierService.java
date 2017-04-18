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
     * 期限の何日前に通知するか
     * 10なら期限が10日以内の場合通知する。
     * ※現状だと一度通知した商品も毎回通知
     */
    private static final int NOTIFIER_DAY_MERGIN = 10;

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
        if (this.shouldTaskStart() == true) {
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
     * 有効期限が近い商品のリストを取得します
     * 期限切れの商品は返却しません
     *
     * @param itemList
     * @return
     */
    private List<Item> getExpiredList(List<Item> itemList) {
        List<Item> expiredList = new ArrayList<>();

        SimpleDateFormat format = this.getDateFormat();

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

                // 期限の近い商品をリストに追加
                long expireTime = itemExpiredAt.getTime();
                long nowTime = now.getTime();

                // 経過ミリ秒÷(1000ミリ秒×60秒×60分×24時間)。端数切り捨て。
                int diffDays = (int) ((expireTime - nowTime) / (1000 * 60 * 60 * 24));

                // 期限切れ
                if (diffDays < 0) {
                    continue;
                }

                // 通知期間のやつはリストに追加
                if (diffDays < NOTIFIER_DAY_MERGIN) {
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
    private void doBulkNotice(Context context, List<Item> itemList) {
        // 賞味期限切れの商品名（改行で結合）
        List itemNameList = new ArrayList<>();

        for (int i = 0, size = itemList.size(); i < size; i++) {
            Item item = itemList.get(i);

            // 通知に出力するメッセージ（1行）
            itemNameList.add(item.getItemName() + " - " + item.getExpiredAt());
        }

        // 通知の準備
        AgostickNotification notifier = new AgostickNotification(context);

        // 通知のタイトル
        String title = "次の商品の賞味期限が近づいています";

        // 通知を実行
        notifier.doNotice(itemNameList, title);
    }

    /**
     * 日付フォーマット yyyy/MM/dd
     *
     * @return SimpleDateFormat
     */
    private SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy/MM/dd");
    }

}
