package cz.mendelu.busitweek_app1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogUtility {

    public static void skipTask(Context context, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Skip task").setMessage("Would you like to skip the task?")
                .setCancelable(false)
                .setPositiveButton("Yes", onClickListener)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
