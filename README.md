# SpinWheelNew_AN

Step 1. Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.loyltworks:SpinWheelNew_AN:V1.0.0'
	}

Step 3. In your xml layout add below code
<com.loyaltyworks.loyltyspinwheel.SpinWheel
        android:id="@+id/spinView"
        android:layout_width="300dp"
        android:layout_height="300dp"
        android:layout_centerInParent="true"
        SpinWheel:background_color="@color/light_blue_bg"
        SpinWheel:text_size="60"/>
