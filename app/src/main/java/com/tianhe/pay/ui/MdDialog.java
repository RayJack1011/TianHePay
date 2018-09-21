package com.tianhe.pay.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.tianhe.pay.common.BaseDialog;
import com.tianhe.pay.utils.Objects;
import com.tianhe.pay.utils.Strings;

public class MdDialog extends BaseDialog {

    private static final String ARGS_DIALOG_ID = "dialogId";
    private static final String ARGS_DIALOG_TITLE = "dialogTitle";
    private static final String ARGS_DIALOG_TITLE_RES = "dialogTitleRes";
    private static final String ARGS_DIALOG_CONTENT = "dialogContent";
    private static final String ARGS_DIALOG_CONTENT_RES = "dialogContentRes";
    private static final String ARGS_DIALOG_MODE = "dialogMode";
    private static final String ARGS_DIALOG_PROGRESS = "dialogProgress";
    private static final String ARGS_DIALOG_POSITIVE = "dialogPositive";
    private static final String ARGS_DIALOG_POSITIVE_RES = "dialogPositiveRes";
    private static final String ARGS_DIALOG_NEGATIVE = "dialogNegative";
    private static final String ARGS_DIALOG_NEGATIVE_RES = "dialogNegativeRes";

    private static final int MODE_BASIC = 1;
    /**
     * 有明确进度的progress
     */
    private static final int MODE_PROGRESS_STEP = 2;
    /**
     * 不确定的圆形进度条
     */
//    private static final int MODE_PROGRESS_INDEFINITE = 3;
    /**
     * 不确定的横线进度条
     */
    private static final int MODE_PROGRESS_INDEFINITE_HORIZONTAL = 4;

    public static MdDialog newInstance(Builder builder) {
        MdDialog dialog = new MdDialog();
        Bundle bundle = dialog.getArguments();
        bundle.putInt(ARGS_DIALOG_ID, builder.dialogId);
        bundle.putString(ARGS_DIALOG_TITLE, builder.title);
        bundle.putInt(ARGS_DIALOG_TITLE_RES, builder.titleRes);
        bundle.putString(ARGS_DIALOG_CONTENT, builder.message);
        bundle.putInt(ARGS_DIALOG_CONTENT_RES, builder.messageRes);
        bundle.putString(ARGS_DIALOG_POSITIVE, builder.positiveText);
        bundle.putInt(ARGS_DIALOG_POSITIVE_RES, builder.positiveTextRes);
        bundle.putString(ARGS_DIALOG_NEGATIVE, builder.negativeText);
        bundle.putInt(ARGS_DIALOG_NEGATIVE_RES, builder.negativeTextRes);
        bundle.putInt(ARGS_DIALOG_MODE, matchMode(builder));
        bundle.putInt(ARGS_DIALOG_PROGRESS, builder.progress);
        return dialog;
    }

    private static int matchMode(Builder builder) {
        if (builder.indeterminate) {
            return MODE_PROGRESS_INDEFINITE_HORIZONTAL;
        } else if (builder.progress >= 0) {
            return MODE_PROGRESS_STEP;
        } else {
            return MODE_BASIC;
        }
    }

    private int dialogId;
    private String title;
    private int titleRes;
    private String content;
    private int contentRes;
    private int mode;
    private int progress;
    private String positiveText;
    private int positiveTextRes;
    private String negativeText;
    private int negativeTextRes;

    private MdListenerAdapter mdListener;

    public MdDialog() {
        super();
        mdListener = new MdListenerAdapter(this);
        setCancelable(false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        dialogId = args.getInt(ARGS_DIALOG_ID);
        title = args.getString(ARGS_DIALOG_TITLE);
        titleRes = args.getInt(ARGS_DIALOG_TITLE_RES);
        content = args.getString(ARGS_DIALOG_CONTENT);
        contentRes = args.getInt(ARGS_DIALOG_CONTENT_RES);
        mode = args.getInt(ARGS_DIALOG_MODE);
        positiveText = args.getString(ARGS_DIALOG_POSITIVE);
        positiveTextRes = args.getInt(ARGS_DIALOG_POSITIVE_RES);
        negativeText = args.getString(ARGS_DIALOG_NEGATIVE);
        negativeTextRes = args.getInt(ARGS_DIALOG_NEGATIVE_RES);
        progress = args.getInt(ARGS_DIALOG_PROGRESS);
        return createDialog();
    }

    private MaterialDialog createDialog() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        builder.cancelable(isCancelable())
                .canceledOnTouchOutside(isCancelable())
                .tag(dialogId);
        if (titleRes != 0) {
            builder.title(titleRes);
        } else if (!Strings.isBlank(title)) {
            builder.title(title);
        }
        if (contentRes != 0) {
            builder.content(contentRes);
        } else if (!Strings.isBlank(content)) {
            builder.content(content);
        }
        switch (mode) {
            case MODE_BASIC:
                if (positiveTextRes != 0) {
                    builder.positiveText(positiveTextRes);
                } else {
                    builder.positiveText(positiveText);
                }
                if (negativeTextRes != 0) {
                    builder.negativeText(negativeTextRes);
                } else {
                    builder.negativeText(negativeText);
                }
                builder.onAny(mdListener);
                break;
            case MODE_PROGRESS_INDEFINITE_HORIZONTAL:
                builder.progress(true, 0);
                builder.progressIndeterminateStyle(false);
                break;
            case MODE_PROGRESS_STEP:
                builder.progress(false, 100);
                break;
        }
        return builder.build();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mode == MODE_PROGRESS_STEP && progress != 0) {
            getMaterialDialog().setProgress(progress);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save changed config
        getArguments().putInt(ARGS_DIALOG_PROGRESS, progress);
        getArguments().putString(ARGS_DIALOG_TITLE, title);
        getArguments().putInt(ARGS_DIALOG_TITLE_RES, titleRes);
        getArguments().putString(ARGS_DIALOG_CONTENT, content);
        getArguments().putInt(ARGS_DIALOG_CONTENT_RES, contentRes);
        getArguments().putString(ARGS_DIALOG_POSITIVE, positiveText);
        getArguments().putInt(ARGS_DIALOG_POSITIVE_RES, positiveTextRes);
        getArguments().putString(ARGS_DIALOG_NEGATIVE, negativeText);
        getArguments().putInt(ARGS_DIALOG_NEGATIVE_RES, negativeTextRes);
    }

    @Override
    public int getDialogId() {
        return dialogId;
    }

    @Override
    public void updateState(BaseDialog.Builder builder) {
        if (dialogId != builder.getDialogId()) {
            return;
        }
        if (builder instanceof Builder) {
            Builder mdBuilder = (Builder) builder;
            int mode = matchMode(mdBuilder);
            if (this.mode != mode) {
                return;
            }
            if(!Objects.equal(title, mdBuilder.title)) {
                getMaterialDialog().setTitle(mdBuilder.title);
            }
            if (!Objects.equal(content, mdBuilder.message)) {
                getMaterialDialog().setContent(mdBuilder.message);
            }
            if (!Objects.equal(positiveText, mdBuilder.positiveText)) {
                getMaterialDialog().setActionButton(DialogAction.POSITIVE, mdBuilder.positiveText);
            }
            if (!Objects.equal(negativeText, mdBuilder.negativeText)) {
                getMaterialDialog().setActionButton(DialogAction.NEGATIVE, mdBuilder.negativeText);
            }
            if (this.progress < mdBuilder.progress) {
                getMaterialDialog().setProgress(mdBuilder.progress);
            }
        }
    }

    private MaterialDialog getMaterialDialog() {
        return (MaterialDialog) super.getDialog();
    }

    public static class Builder implements BaseDialog.Builder {
        private int dialogId;
        private String title;
        private int titleRes;
        private String message;
        private int messageRes;
        private String positiveText;
        private int positiveTextRes;
        private String negativeText;
        private int negativeTextRes;
        private boolean indeterminate;
        private int progress = -1;

        public Builder(int dialogId) {
            this.dialogId = dialogId;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder title(int titleRes) {
            this.titleRes = titleRes;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder message(int messageRes) {
            this.messageRes = messageRes;
            return this;
        }

        public Builder positiveText(String positiveText) {
            this.positiveText = positiveText;
            return this;
        }

        public Builder positiveText(int positiveTextRes) {
            this.positiveTextRes = positiveTextRes;
            return this;
        }

        public Builder negativeText(String negativeText) {
            this.negativeText = negativeText;
            return this;
        }

        public Builder negativeText(int negativeTextRes) {
            this.negativeTextRes = negativeTextRes;
            return this;
        }

        public Builder progress() {
            this.indeterminate = true;
            return this;
        }

        public Builder progress(int progress) {
            this.indeterminate = false;
            this.progress = (progress < 0) ? 0 : progress;
            return this;
        }

        @Override
        public int getDialogId() {
            return dialogId;
        }

        @Override
        public BaseDialog build() {
            return MdDialog.newInstance(this);
        }
    }

    private static class MdListenerAdapter implements MaterialDialog.SingleButtonCallback {
        private BaseDialog wrap;

        public MdListenerAdapter(MdDialog dialog) {
            this.wrap = dialog;
        }

        @Override
        public void onClick(MaterialDialog dialog, DialogAction which) {
            Listener listener = wrap.getListener();
            if (listener == null) {
                return;
            }
            switch (which) {
                case POSITIVE:
                    listener.onPositive(wrap);
                    break;
                case NEUTRAL:
                    listener.onNeutral(wrap);
                    break;
                case NEGATIVE:
                    listener.onNegative(wrap);
                    break;
                default:
                    break;
            }
        }
    }
}
