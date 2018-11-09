package uitcourse.j11.nt118.appmusichtcl.Adapter;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> arrayFragment = new ArrayList<>();
    private ArrayList<String> arraytitle = new ArrayList<>();
    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    // Click den dau thi tra ve vi tri cua no
    public Fragment getItem(int position) {
        return arrayFragment.get(position);
    }

    @Override
    // So luong fragment muon hien thi
    public int getCount() {
        return arrayFragment.size();
    }

    // Them fragment
    public void addFragment(Fragment fragment, String title)
    {
        arrayFragment.add(fragment);
        arraytitle.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return arraytitle.get(position);
    }

}
