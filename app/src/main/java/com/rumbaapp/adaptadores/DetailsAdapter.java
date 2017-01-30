package com.rumbaapp.adaptadores;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.rumbaapp.clasesVO.GridItemVo;

import java.util.ArrayList;

/**
 * Created by Rangel on 07/06/2016.
 */
public class DetailsAdapter extends FragmentPagerAdapter {
    Context context;
    public ArrayList<GridItemVo> arrayList=new ArrayList<>();

    public DetailsAdapter(FragmentManager fm, Context context, ArrayList<GridItemVo> arrayList) {
        super(fm);
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public Fragment getItem(int position) {
        FragmentDetailsAdapter fragmentDetailsAdapter=new FragmentDetailsAdapter();
        return fragmentDetailsAdapter.newInstance(position, arrayList.get(position).getImage());
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }
}
