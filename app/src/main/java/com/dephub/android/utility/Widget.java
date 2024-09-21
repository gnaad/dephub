package com.dephub.android.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.widget.Toast;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import com.dephub.android.R;

public class Widget {

    public static void Toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void alertDialog(Context context,
                                   Boolean cancellable,
                                   String message,
                                   String positiveButtonTitle,
                                   String negativeButtonTitle,
                                   DialogInterface.OnClickListener positiveButtonListener,
                                   DialogInterface.OnClickListener negativeButtonListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.customAlertDialog);
        alertDialogBuilder.setCancelable(cancellable);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(positiveButtonTitle, positiveButtonListener);
        alertDialogBuilder.setNegativeButton(negativeButtonTitle, negativeButtonListener);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorAccent));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorAccent));
    }

    public static void openInBrowser(Activity activity, String webURL) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        builder.setShowTitle(true);
        builder.addDefaultShareMenuItem();
        builder.setUrlBarHidingEnabled(false);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(activity, Uri.parse(webURL));
    }
}