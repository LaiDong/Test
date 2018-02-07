package slideshow.lab411.com.slideshow.ui.passcode.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hanks.passcodeview.PasscodeView;

import butterknife.ButterKnife;
import slideshow.lab411.com.slideshow.R;
import slideshow.lab411.com.slideshow.base.BaseFragment;
import slideshow.lab411.com.slideshow.ui.imagegrid.service.ServiceUtils;
import slideshow.lab411.com.slideshow.ui.passcode.model.SharedPrefsHelper;

/**
 * Created by ld on 19/12/2017.
 */

public class SetPasscodeFragment extends BaseFragment {
    private SharedPrefsHelper mSharedPrefsHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_set_passcode, container, false);

        mSharedPrefsHelper = new SharedPrefsHelper(getContext());

        PasscodeView passcodeView = view.findViewById(R.id.passcodeView);
        passcodeView.setListener(new PasscodeView.PasscodeViewListener() {
            @Override
            public void onFail() {

            }

            @Override
            public void onSuccess(String number) {
                mSharedPrefsHelper.putPassCode(number);
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        ServiceUtils.startService(getContext());
        mSharedPrefsHelper.putPassCodeUI(false);
    }
}
