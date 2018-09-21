package com.tianhe.pay.ui;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.tianhe.pay.R;
import com.tianhe.pay.common.BaseActivity;
import com.tianhe.pay.common.BaseDialog;
import com.tianhe.pay.common.StackableFragment;
import com.tianhe.pay.utils.ToastFactory;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * 只包含一个Fragment的基类Activity.
 */
public abstract class TianHeActivity extends BaseActivity implements HasSupportFragmentInjector,
        BaseDialog.ListenerProvider {

    // region Constants

    private static final String TAG_FRAGMENT_SINGLE = "SingleFragmentTag";

    // endregion Constants

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;
    @Inject
    ToastFactory toastFactory;
    @Inject
    protected Nav nav;

    // region Lifecycle

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(layoutOfContentView());
        if (isSingleFragment() && findContentFragment() == null) {
            Fragment fragment = createSingleFragment();
            if (fragment != null) {
                getFM().beginTransaction()
                        .add(containerViewId(), fragment, TAG_FRAGMENT_SINGLE)
                        .commit();
                getFM().executePendingTransactions();
            }
        }
    }

    // endregion Lifecycle

    // region Inherited Methods

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }

    @Override
    public boolean push(StackableFragment stackable, Fragment replace) {
        return false;
    }

    @Override
    public boolean pop(StackableFragment stackable, Fragment replace) {
        finish();
        return true;
    }

    @Override
    public BaseDialog.Listener provideDialogListener() {
        return fragmentManagers;
    }

    // endregion Inherited Methods

    // region Instance Methods

    public void showMessage(String message)  {
        toastFactory.makeText(message, Toast.LENGTH_LONG).show();
    }

    public void showMessage(int messageResId) {
        toastFactory.makeText(messageResId, Toast.LENGTH_LONG).show();
    }

    protected Fragment createSingleFragment() {
        return null;
    }

    protected boolean isSingleFragment() {
        return true;
    }

    @LayoutRes
    protected int layoutOfContentView() {
        return R.layout.activity_single_frame;
    }

    @IdRes
    protected int containerViewId() {
        return R.id.activity_single_frame_container;
    }

    protected Fragment findContentFragment() {
        return getFM().findFragmentByTag(TAG_FRAGMENT_SINGLE);
    }

    // endregion Instance Methods
}
