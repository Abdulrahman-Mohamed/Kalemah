package com.Motawer.kalemah.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.Motawer.kalemah.Fragments.chartsFragments.Tests_Charts_frag;
import com.Motawer.kalemah.Fragments.chartsFragments.Words_Charts_frag;

public class examFragmentAdapter extends FragmentStateAdapter
{


  public examFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
    super(fragmentActivity);
  }

  @NonNull
  @Override
  public Fragment createFragment(int position)

  {
    switch (position)
    {
      case 0:
        return new Tests_Charts_frag();
      case 1:
        return new Words_Charts_frag();
//      case 2:
//        return new S_F_Charts_frag();



    }
    return new Tests_Charts_frag();

  }

  @Override
  public int getItemCount()
  {
    return 2;
  }
}