package it.a4smart.vate.proximity;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ProximityAdapter extends FragmentStatePagerAdapter {
    private ProximityVM viewModel;

    public ProximityAdapter(FragmentManager fragmentManager, ProximityVM viewModel) {
        super(fragmentManager);
        this.viewModel = viewModel;
    }

    @Override
    public Fragment getItem(int position) {
        return viewModel.getFragment(position);
    }

    @Override
    public int getCount() {
        return viewModel.getBeaconsNumber();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return viewModel.getFragment(position).getID();
    }
}
