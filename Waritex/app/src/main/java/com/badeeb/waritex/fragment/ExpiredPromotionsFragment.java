package com.badeeb.waritex.fragment;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.badeeb.waritex.PromotionDetailsActivity;
import com.badeeb.waritex.R;
import com.badeeb.waritex.adapter.PromotionsRecyclerViewAdaptor;
import com.badeeb.waritex.listener.RecyclerItemClickListener;
import com.badeeb.waritex.model.Promotion;
import com.badeeb.waritex.view.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpiredPromotionsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private PromotionsRecyclerViewAdaptor mAdapter;
    private List<Promotion> mPromotionList;
    private int mPromotionPerLine;


    public ExpiredPromotionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPromotionPerLine = 2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_expired_promotions, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.expired_promotion_recycler_view);

        mPromotionList = new ArrayList<Promotion>();
        mAdapter = new PromotionsRecyclerViewAdaptor(getActivity(), mPromotionList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), mPromotionPerLine);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), PromotionDetailsActivity.class);

                        // TODO replace it with sending promotion object that will implement Parceable
                        TextView titleTV = (TextView) view.findViewById(R.id.promotion_card_title);
                        String title = titleTV.getText().toString();
                        intent.putExtra(ActivePromotionsFragment.PROMOTION_TITLE_EXTRA, title);

                        TextView dueDateTV = (TextView) view.findViewById(R.id.promotion_card_due_date);
                        String dueDate = dueDateTV.getText().toString();
                        intent.putExtra(ActivePromotionsFragment.PROMOTION_DUE_DATE_EXTRA, dueDate);

                        startActivity(intent);
                    }
                })
        );
        preparePromotions();

        return view;
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    private void preparePromotions() {
        int[] photos = {
                R.drawable.photo1,
                R.drawable.photo2,
                R.drawable.photo3,
                R.drawable.photo4,
                R.drawable.photo5

        };
        for (int i = 0 ; i< photos.length ; i++){
            Promotion promo = new Promotion();
            promo.setDueDateFormated("1"+i+"-Jan-2017");
            promo.setTitle("Promotion Title "+i+1);
            promo.setMainPhoto(photos[i]+"");
            mPromotionList.add(promo);
        }
        mAdapter.notifyDataSetChanged();
    }

}