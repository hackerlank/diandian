package com.dingli.diandiaan;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class Fragment1 extends Fragment {
	@Override
	public View onCreateView(
			LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
            View viewone= inflater.inflate(
                    R.layout.activity_guid_item_1,
                    container,
                    false);
        return viewone;
        }
    }