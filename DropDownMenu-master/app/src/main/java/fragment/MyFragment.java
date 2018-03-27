package fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yyy.djk.dropdownmenu.R;

import butterknife.ButterKnife;
import butterknife.InjectView;



public class MyFragment extends Fragment {

    public MyFragment(){super();}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myview = inflater.inflate(R.layout.activity_my, container, false);
        ButterKnife.inject(this, myview);
        return myview;
    }

}
