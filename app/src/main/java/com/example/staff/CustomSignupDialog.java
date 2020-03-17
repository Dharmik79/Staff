package com.example.staff;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomSignupDialog {

    public static CustomSignupDialog mDialog;
    public IDialogClickListner iDialogClickListner;
    @BindView(R.id.txt_title)
    TextView txt_title;
    @BindView(R.id.edt_email)
    TextInputEditText edt_email;
    @BindView(R.id.edt_password)
    TextInputEditText edt_password;
    @BindView(R.id.edt_name)
    TextInputEditText edt_name;
    @BindView(R.id.edt_button)
    Button edt_button;

    public static CustomSignupDialog getInstance()
    {
        if (mDialog == null)
        {
            mDialog = new CustomSignupDialog();
        }
        return mDialog;
    }

    public void showSignupDialog(String title, String positiveText, Context context, final IDialogClickListner iDialogClickListner)
    {
        this.iDialogClickListner = iDialogClickListner;

        final Dialog dialog =new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_signup);

        ButterKnife.bind(this,dialog);
        if(!TextUtils.isEmpty(title))
        {
            txt_title.setText(title);
            txt_title.setVisibility(View.VISIBLE);
        }
        edt_button.setText(positiveText);
        dialog.setCancelable(false);
        dialog.show();

        Window window=dialog.getWindow();

        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        edt_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iDialogClickListner.onClickPositiveButton(dialog,edt_name.getText().toString(),edt_email.getText().toString(),edt_password.getText().toString());

            }
        });

    }

}
