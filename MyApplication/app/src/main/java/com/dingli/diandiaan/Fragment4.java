package com.dingli.diandiaan;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.login.LoginActivity;


public class Fragment4 extends Fragment {
	@Override
	public View onCreateView(
			LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
            View viewone= inflater.inflate(
                    R.layout.activity_guid_item_4,
                    container,
                    false);
        final ImageView fbton=(ImageView)viewone.findViewById(R.id.fbton);
        fbton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbton.setClickable(false);
                DianTool.share(getActivity());
                Intent intent=new Intent(getActivity(),MaintiAcvity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
            }
        });
        return viewone;
        }
}