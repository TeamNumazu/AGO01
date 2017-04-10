package com.example.silve.ago01.fragments;

import android.app.ActionBar;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;
import com.example.silve.ago01.R;
import com.example.silve.ago01.models.DataBaseHelper;
import com.example.silve.ago01.models.entity.Item;
import com.example.silve.ago01.models.repository.ItemRepository;
import com.example.silve.ago01.models.specification.sql.item.FindByCategorySpecification;
import com.example.silve.ago01.views.ListViewAdapter;

import java.util.List;

public class PageFragment extends Fragment {

    // Activityのボタンクリック用リスナ
    public interface OnButtonClickListener {
        public void onFloatingButtonClick(int categoryId);
    }

    private static final String ARG_PARAM = "page";
    private static final String ARG_PARAM_CATEGORY_ID = "category_id";
    private String mParam;
    private OnFragmentInteractionListener mListener;
    private OnButtonClickListener mButtonClickListener;

    // コンストラクタ
    public PageFragment() {
    }

    public static PageFragment newInstance(int page, Long categoryId) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM, page);
        args.putInt(ARG_PARAM_CATEGORY_ID, categoryId.intValue());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getString(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // ビュー取得
        View view = inflater.inflate(R.layout.fragment_page, container, false);

        // リストビュー生成
        final int categoryId = this.getCategoryId();
        this.createListView(view, categoryId);

        // 商品追加フローティングボタン
        FloatingActionButton floatingButton = (FloatingActionButton) view.findViewById(R.id.fab);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mButtonClickListener != null) {
                    mButtonClickListener.onFloatingButtonClick(categoryId);
                }
            }
        });

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        if (context instanceof OnButtonClickListener) {
            mButtonClickListener = ((OnButtonClickListener) context);
        } else {
            throw new ClassCastException("activity が OnButtonClickListener を実装していません.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    /**
     * このカテゴリタブのリストビューを生成
     *
     * @param view
     */
    public void createListView(View view, Integer categoryId) {
        /**
         * データ取得
         */
        DataBaseHelper dbHelper = new DataBaseHelper(getActivity());
        ItemRepository iRepository = new ItemRepository(dbHelper);

        FindByCategorySpecification iByCategorySpec = new FindByCategorySpecification(categoryId);
        List<Item> itemList = iRepository.query(iByCategorySpec);

        /**
         * SwipeListViewをつくる
         */
        final ListView listView;
        ListViewAdapter listAdapter;

        listView = (ListView) view.findViewById(R.id.listview);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActivity().getActionBar();
            if (actionBar != null) {
                actionBar.setTitle("ListView");
            }
        }

        listAdapter = new ListViewAdapter(getActivity(), itemList, categoryId);
        listView.setAdapter(listAdapter);
        listAdapter.setMode(Attributes.Mode.Single);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((SwipeLayout) (listView.getChildAt(position - listView.getFirstVisiblePosition()))).open(true);
            }
        });
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "OnItemLongClickListener", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.e("ListView", "onScrollStateChanged");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("ListView", "onItemSelected:" + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("ListView", "onNothingSelected:");
            }
        });
    }

    /**
     * 表示中タブのカテゴリIDを取得
     *
     * @return
     */
    private int getCategoryId() {
        return getArguments().getInt(ARG_PARAM_CATEGORY_ID, 0);
    }

}