package com.tianhe.pay.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.tianhe.pay.common.BaseFragment;
import com.tianhe.pay.common.BaseDialog;
import com.tianhe.pay.common.FragmentManagersProvider;
import com.tianhe.pay.common.SavablePresenter;
import com.tianhe.pay.utils.ToastFactory;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public abstract class TianHeFragment extends BaseFragment {

    // region Constants

    private static final int PROGRESS_DIALOG_ID = BaseDialog.getAutoId();
    private static final String TAG_BUILDER_KEYS = "builder_keys";

    // endregion Constants

    // region Members

    private ArrayList<Integer> builderKeys = new ArrayList<>();
    private DialogControlHelper dialogControl;

    @Inject
    protected ToastFactory toastFactory;
    @Inject
    protected Nav nav;

    // endregion Members

    // region Lifecycle

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getPresenter() != null) {
            getPresenter().bindView(this);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dialogControl = new DialogControlHelper((FragmentManagersProvider) getActivity());
        if (savedInstanceState != null) {
            builderKeys = savedInstanceState.getIntegerArrayList(TAG_BUILDER_KEYS);
            for (int dialogId : builderKeys) {
                bindDialogListener(dialogId);
            }
        } else {
            builderKeys = new ArrayList<>();
        }
        if (getPresenter() != null) {
            getPresenter().restore(getStatesContainer());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList(TAG_BUILDER_KEYS, builderKeys);
        if (getPresenter() != null) {
            getPresenter().save(getStatesContainer());
        }
    }

    @Override
    public void onDestroy() {
        for (int dialogId : builderKeys) {
            unbindDialogListener(dialogId);
        }
        builderKeys.clear();
        dialogControl = null;
        if (getPresenter() != null) {
            getPresenter().onDestroy();
        }
        super.onDestroy();
    }

    // endregion Lifecycle

    // region Inherited Methods

    @Override
    public void showMessage(CharSequence message) {
        toastFactory.makeText(message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showMessage(int messageResId) {
        toastFactory.makeText(messageResId, Toast.LENGTH_LONG).show();
    }

    // endregion Inherited Methods

    // region Abstract Methods

    protected abstract <P extends SavablePresenter> P getPresenter();

    // endregion Abstract Methods

    // region Instance Methods

    public void onDialogOk(int dialogId) {
        // default do nothing
    }

    public void onDialogCancel(int dialogId) {
        // default do nothing
    }

    public void dismissDialog(int dialogId) {
        dialogControl.dismiss(dialogId);
    }

    public void showDialog(MdDialog.Builder builder) {
        int dialogId = builder.getDialogId();
        if (!hasBoundDialogListener(dialogId)) {
            bindDialogListener(dialogId);
        }
        dialogControl.showDialog(builder);
    }

    public void showProgress(int dialogId, String message) {
        MdDialog.Builder builder = new MdDialog.Builder(dialogId);
        builder.message(message);
        builder.progress();
        showDialog(builder);
    }

    public void showProgress(int dialogId, int messageRes) {
        MdDialog.Builder builder = new MdDialog.Builder(dialogId);
        builder.message(messageRes);
        builder.progress();
        showDialog(builder);
    }

    private boolean hasBoundDialogListener(int dialogId) {
        return builderKeys.contains(dialogId);
    }

    private void bindDialogListener(int dialogId) {
        builderKeys.add(dialogId);
        dialogControl.bindDialogListener(dialogId, dialogListener);
    }

    private void unbindDialogListener(int dialogId) {
        dialogControl.unbindDialogListener(dialogId);
    }

    // endregion Instance Methods

    // region Anonymous Classes

    private final BaseDialog.EmptyListener dialogListener = new BaseDialog.EmptyListener() {
        @Override
        public void onNegative(BaseDialog dialog) {
            onDialogCancel(dialog.getDialogId());
        }

        @Override
        public void onPositive(BaseDialog dialog) {
            onDialogOk(dialog.getDialogId());
        }
    };

    // endregion Anonymous Classes
}
