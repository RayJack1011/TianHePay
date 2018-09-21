package com.tianhe.pay.ui;

import com.tianhe.pay.common.BaseView;
import com.tianhe.pay.common.SavablePresenter;
import com.tianhe.pay.common.StatesContainer;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class TianHePresenter<V extends BaseView> implements SavablePresenter<V> {

    private String keyOfSaved;

    protected V view;

    private CompositeDisposable disposables;
    private boolean isSaved;

    public void bindView(V view) {
        if (this.view != null) {
            throw new IllegalStateException("the bind view can not reset");
        }
        this.view = view;
    }

    @Override
    public void restore(StatesContainer<String, Object> saved) {
        if (saved.contains(getKeyOfSaved())) {
            saved.remove(getKeyOfSaved());
            onRestore(saved);
            isSaved = false;
        }
    }

    @Override
    public void save(StatesContainer<String, Object> out) {
        out.save(getKeyOfSaved(), true);
        onSave(out);
        isSaved = true;
    }

    @Override
    public void onDestroy() {
        disposeAll();
    }

    protected void onRestore(StatesContainer<String, Object> saved) {

    }

    protected void onSave(StatesContainer<String, Object> out) {

    }

    protected boolean isSaved() {
        return isSaved;
    }

    protected void addDisposable(Disposable disposable) {
        if (disposable != null) {
            if (disposables == null) {
                disposables = new CompositeDisposable();
            }
            disposables.add(disposable);
        }
    }

    private void disposeAll() {
        if (disposables != null
                && !disposables.isDisposed()
                && disposables.size() > 0) {
            disposables.dispose();
        }
    }

    private String getKeyOfSaved() {
        if (keyOfSaved == null) {
            keyOfSaved = this.getClass().getName() + "PresenterSaved";
        }
        return keyOfSaved;
    }
}
