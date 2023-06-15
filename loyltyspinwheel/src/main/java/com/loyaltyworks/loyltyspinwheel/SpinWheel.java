package com.loyaltyworks.loyltyspinwheel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Created by Sujeet on 13/06/2023. */

public class SpinWheel extends FrameLayout implements OnRotationListener{
    private WheelViewDraw wheelView;
    private ImageView arrow;
    private int target = -1;
    private boolean isRotate = false;

    int position = -1;
    String resultValue = "";
    String[] elements;
    String gameId = "";
    String finalUrl = "";

//    List<String> colorCodes = new ArrayList<>();

    public SpinWheel(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initComponent();
        applyAttribute(attrs);
    }

    public SpinWheel(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent();
        applyAttribute(attrs);
    }

    private void initComponent() {
        inflate(getContext(), R.layout.spin_wheel_layout, this);
//        setOnTouchListener(this);
        wheelView = findViewById(R.id.wv_main_wheel);
        wheelView.setOnRotationListener(this);
        arrow = findViewById(R.id.iv_arrow);
    }

    /** Function to add items to wheel items */
    public void addWheelItems(List<WheelItem> wheelItems) {
        wheelView.addWheelItems(wheelItems);

    }

    public void applyAttribute(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SpinWheel, 0, 0);
        try {
            int backgroundColor = typedArray.getColor(R.styleable.SpinWheel_background_color, Color.GREEN);
            int arrowImage = typedArray.getResourceId(R.styleable.SpinWheel_arrow_image, R.drawable.spin_arrow);
            int imagePadding = typedArray.getDimensionPixelSize(R.styleable.SpinWheel_image_padding , 0);
            float textSize = typedArray.getFloat(R.styleable.SpinWheel_text_size , 30);
            wheelView.setWheelBackgoundWheel(backgroundColor);
            wheelView.setItemsImagePadding(imagePadding);
            arrow.setImageResource(arrowImage);
            wheelView.setWheelTextSize(textSize);
            Log.d("rgvfgrvf", "brhvb : " + textSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        typedArray.recycle();
    }

    /** Function to set lucky wheel reach the target listener */
    public void setSpinWheelTargetReach(OnSpinWheelTargetReach onSpinWheelTargetReach) {
        wheelView.setWheelListener(onSpinWheelTargetReach);
    }

    /** target to rotate before swipe (initial position) */
    public void setTarget(int target) {
        this.target = target;
    }

    /** Function to get data from backend in encoded format */
    public void getFinalDataFromBackend(String s) {
        String decodedData = decodeString(s);
        Log.d("brrvr","final data : " + decodedData);

        String r1 = decodedData.split("r1=")[1];
        resultValue = r1.split("&")[0];
        Log.d("brrvr","resultValue : " + resultValue);

        String s2 = decodedData.split("s2=")[1];
        String itemValue = s2.split("&")[0];
        elements = itemValue.split(",");
        Log.d("brrvr","itemValue : " + itemValue);

        String g4 = decodedData.split("g4=")[1];
        gameId = g4.split("&")[0];
        Log.d("brrvr","gameId : " + gameId);

        String url = decodedData.split("url=")[1];
        finalUrl = url.split("&")[0];
        Log.d("brrvr","finalUrl : " + finalUrl);

        /*  For Static color code
        colorCodes.add("#FF0000");
        colorCodes.add("#4B0082");
        colorCodes.add("#0000FF");
        colorCodes.add("#00FF00");
        colorCodes.add("#94b30c");
        colorCodes.add("#FF7F00");
        colorCodes.add("#9400D3");
        colorCodes.add("#49f5e4");
        colorCodes.add("#499cf5");
        colorCodes.add("#95ff00");
        colorCodes.add("#c77042");*/

        /** Set dynamic text value in WheelItem model start ***/
        List<WheelItem> itemToAddInWheel = new ArrayList<>();

        // If total item length from backend is less than 12 then should add 0 in first position
        if (elements.length < 12){
            // Add 0 at the first index
            itemToAddInWheel.add(0, new WheelItem(Color.parseColor("#e8070e"),"0"));
        }

        for (int i = 0; i < elements.length; i++) {
            int randomColor = generateRandomColorHex();                 // To get Random color code
//            String colorCode = colorCodes.get(i % colorCodes.size()); // Get a color code from the list cyclically
            itemToAddInWheel.add(new WheelItem(randomColor,elements[i]));
        }
        /** Set dynamic text value in WheelItem model close ***/

        /*** Get the position of result value in the array list ***/
        for (int i = 0; i < itemToAddInWheel.size(); i++) {
            if (itemToAddInWheel.get(i).text.equals(resultValue)){
                position = i;
                break;// Exit the loop once the value is found
            }
        }

        addWheelItems(itemToAddInWheel);
    }

    /** Function to generate a random color */
    private int  generateRandomColorHex() {
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return Color.rgb(r, g, b);
    }


    /** Function to decode string  */
    private String decodeString(String encoded) {
        String decodedString = "";
        try {
            byte[] dataDec = Base64.decode(encoded, Base64.NO_WRAP);
            decodedString = new String(dataDec, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            return decodedString;
        }
    }

    /** Function to start rotation after click on start button */
    public void startRotation() {
        rotateWheelTo(position + 1);    // Set Rotation position

        JSONObject data = new JSONObject();
        try {
            data.put("ActionType", "2");
            data.put("CustomerGamifyTransactionId", gameId);
            data.put("PointResult", resultValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = finalUrl;

        url += "/MobileApp/MobileApi.svc/JSON/UpdateGamificationTransaction";

        NetworkTask networkTask = new NetworkTask(url, data);
        networkTask.execute();

    }

    /** Function to rotate wheel to degree
     *** Set the target where it will reach after spin complete
     */
    public void rotateWheelTo(int number) {
        isRotate = true;
        wheelView.resetRotationLocationToZeroAngle(number);
    }

    @Override
    public void onFinishRotation() {
        isRotate = false;
    }


}
