package com.loyaltyworks.spinwheelloylty;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/** Created by Sujeet on 13/06/2023. */

public class SuccessDialog {
    private static SuccessDialog  successDialog = null;
    private  Context context;
    private Dialog dialog;

    public static SuccessDialog getInstance(){
        if(successDialog==null)successDialog=new SuccessDialog();
        return successDialog;
    }

    public interface CallBacksPopup{
        void  onOKResponse();
    }

    public void setPopupMessages(Context context , String imageIcon, String points, final CallBacksPopup callBacksPopup ) {

        this.context = context;
        if (context == null)
            return;

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.common_custom_alert);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //            window.setBackgroundDrawable(context.getDrawable(R.color.black_transparenc));
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView textDialog = (TextView) dialog.findViewById(R.id.textDialog);
//        ImageView image = (ImageView) dialog.findViewById(R.id.image_icons);


        TextView textOk = (TextView) dialog.findViewById(R.id.textOk);

        /*if (points.equals("0")){
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.zero));
        }if (points.equals("20")){
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.one));
        }if (points.equals("40")){
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.two));
        }if (points.equals("60")){
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.three));
        }if (points.equals("80")){
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.four));
        }if (points.equals("100")){
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.five));
        }if (points.equals("500")){
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.seven));
        }if (points.equals("1000")){
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.six));
        }*/

        if (points.equals("0")){
            textDialog.setText("Oops! Better luck next time!");
        }else {
            textDialog.setText("Congratulation you have won ₹ " + points);
        }
        textDialog.setPadding(15, 15, 15, 15);
        textDialog.setGravity(Gravity.CENTER);

        textOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callBacksPopup.onOKResponse();

            }
        });
        dialog.show();
    }

}
