package edu.sjsu.android.StockSearch;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Placeholder;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class FragmentPageAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    final int pageCount = 2;
    private String tabTitles[] = new String[]{"Current Info", "Historical Data"};

    public FragmentPageAdapter(@NonNull FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new stockNews();
            case 1:
                return new stockHistory();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return pageCount;
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Current Info";
            case 1:
                return "Description";
        }
        return null;
    }
}
