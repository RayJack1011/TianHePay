package com.tianhe.pay.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import com.tianhe.pay.common.StatesContainer.ListStatesContainer;

public abstract class BaseFragment extends DialogFragment
        implements StackableFragment, BaseView {

    // region Constants

    private static final String PREFIX_CHILD_REQUEST = "RequestCode:";

    // endregion Constants

    // region Members

    private ListStatesContainer<String, Object> statesContainer;
    private FragmentStack fragmentStack;

    // endregion Members

    // region Constructors

    public BaseFragment() {
        setArguments(new Bundle());
    }

    // endregion Constructors

    // region Lifecycle

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        if (getActivity() instanceof FragmentManagersProvider) {
            FragmentManagers managers = ((FragmentManagersProvider) getActivity()).provideFragmentManagers();
            if (managers != null) {
                statesContainer = managers.getRetainStates();
            }
        }
        if (getActivity() instanceof FragmentStack) {
            fragmentStack = (FragmentStack) getActivity();
        }
    }

    @Override
    public void onDestroy() {
        statesContainer = null;
        fragmentStack = null;
        super.onDestroy();
    }

    // endregion Lifecycle

    // region Inherited Methods

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (getParentFragment() instanceof BaseFragment) {
            BaseFragment parent = (BaseFragment) getParentFragment();
            parent.saveChildRequest(this, requestCode);
            parent.startActivityForResult(intent, requestCode);
        } else {
            super.startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!dispatchChildActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public StatesContainer<String, Object> getStatesContainer() {
        return statesContainer;
    }

    // endregion Inherited Methods

    // region Instance Methods

    public boolean onBackPressed() {
        if (getShowsDialog()) {
            dismissAllowingStateLoss();
            return true;
        }
        if (fragmentStack != null) {
            return fragmentStack.pop(this, this);
        }
        return false;
    }

    private boolean dispatchChildActivityResult(int requestCode, int resultCode, Intent data) {
        int requestedChildId = requestedChildId(requestCode);
        if (requestedChildId == 0) {
            return false;
        }
        removeChildRequest(requestCode);
        Fragment child = getChildFragmentManager().findFragmentById(requestedChildId);
        if (child != null && child instanceof BaseFragment) {
            child.onActivityResult(requestCode, resultCode, data);
            return true;
        }
        return false;
    }

    private void saveChildRequest(BaseFragment child, int requestCode) {
        getArguments().putInt(childRequestKey(requestCode), child.getId());
    }

    private int requestedChildId(int requestCode) {
        return getArguments().getInt(childRequestKey(requestCode));
    }

    private String childRequestKey(int requestCode) {
        return PREFIX_CHILD_REQUEST + requestCode;
    }

    private void removeChildRequest(int requestCode) {
        getArguments().remove(childRequestKey(requestCode));
    }

    // endregion Instance Methods
}
