/*
 * Copyright (C) 2011 Wglxy.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.uberdust.mobileclient;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * This is the About activity in the dashboard application.
 * It displays some text and provides a way to get back to the home activity.
 *
 */

public class AboutActivity extends DashboardActivity 
{

/**
 * onCreate
 *
 * Called when the activity is first created. 
 * This is where you should do all of your normal static set up: create views, bind data to lists, etc. 
 * This method also provides you with a Bundle containing the activity's previously frozen state, if there was one.
 * 
 * Always followed by onStart().
 *
 * @param savedInstanceState Bundle
 */
	TextView tv;
protected void onCreate(Bundle savedInstanceState) 
{
    super.onCreate(savedInstanceState);

    setContentView (R.layout.activity_about);
    setTitleFromActivityLabel (R.id.title_text);
    
    LinearLayout mainContainer = (LinearLayout) findViewById(R.id.mainContainer);
    LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(AboutActivity.this, R.anim.main_layout_animation);
    mainContainer.setLayoutAnimation(controller);
    
}//onCreate

public void onImage(View v){
	AnimationSet animationSet = new AnimationSet(true);
	
	RotateAnimation rotateAnimation = new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
	rotateAnimation.setDuration(500);
	rotateAnimation.setRepeatCount(2);
	
	animationSet.addAnimation(rotateAnimation);
	
	TranslateAnimation translateAnimation = new TranslateAnimation(0, 400, 0, 0);
	
	//setting offset and duration to start after first rotation completed, and end at the same time with the last roration
	translateAnimation.setStartOffset(500);
	translateAnimation.setDuration(1000);
	
	animationSet.addAnimation(translateAnimation);
	
	AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
	alphaAnimation.setStartOffset(500);
	alphaAnimation.setDuration(1000);
	
	animationSet.addAnimation(alphaAnimation);

	v.startAnimation(animationSet);
}
    
} // end class
