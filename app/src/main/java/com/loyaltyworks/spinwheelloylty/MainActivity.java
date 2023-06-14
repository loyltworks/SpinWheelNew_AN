package com.loyaltyworks.spinwheelloylty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.loyaltyworks.loyltyspinwheel.OnSpinWheelTargetReach;
import com.loyaltyworks.loyltyspinwheel.SpinWheel;

import java.io.UnsupportedEncodingException;
import android.util.Base64;

/** Created by Sujeet on 13/06/2023. */
public class MainActivity extends AppCompatActivity {
    private SpinWheel mspinWheel;
    Context context = null;
    String mainUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        mspinWheel = findViewById(R.id.spinView);
        mspinWheel.setTarget(1);
        LinearLayout startSpin = findViewById(R.id.start_btn);

        /***
         * r1 = resultValue
         * s2 = spin wheel items
         * g4 = game id
         * url = to send data to backend after play
         * format should be : r1=resValue&s2=value1,value2,...,valueN&g4=gameid&url=baseUrl
         */

        /*** Please give data into mainUrl otherwise it will not run */
        mainUrl = "data format should be given here";
        String encoded =  encodeString(mainUrl);

        /*** Call SpinWheel & pass data ***/
        mspinWheel.getFinalDataFromBackend(encoded);

       /** When Clicks on start button */
        startSpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSpin.setVisibility(View.GONE);
                mspinWheel.startRotation();
            }
        });

        /** After spin completed listener  */
        mspinWheel.setSpinWheelTargetReach(new OnSpinWheelTargetReach() {
            @Override
            public void onReachTarget() {

                String winValue = mainUrl.toString().split("r1=")[1].split("&")[0];

                SuccessDialog.getInstance().setPopupMessages(context, "1", winValue, new SuccessDialog.CallBacksPopup() {
                    @Override
                    public void onOKResponse() {
                        mspinWheel.setTarget(1);
                        startSpin.setVisibility(View.VISIBLE);
                    }
                });

            }
        });

    }


    private String encodeString(String s) {
        byte[] data = new byte[0];
        try {
            data = s.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            return Base64.encodeToString(data, Base64.NO_WRAP);
        }
    }


}