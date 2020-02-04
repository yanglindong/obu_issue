package com.bsit.obu_issue;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


public class LoadingDialog extends AlertDialog {

	private TextView tips_loading_msg;
	private ImageView image;

	private String message = null;
	Context context;

	public LoadingDialog(Context context) {
		super(context);
		message = " ";
		this.setCancelable(true);//back按钮返回取消，默认false
		this.setCanceledOnTouchOutside(false);//点击外部不可以取消，默认true
		this.context = context;
	}

	public LoadingDialog(Context context, String message) {
		super(context);
		this.message = message;
		this.setCancelable(true);
		this.setCanceledOnTouchOutside(false);
		this.context = context;
	}

	public LoadingDialog(Context context, int theme, String message) {
		super(context, theme);
		this.message = message;
		this.setCancelable(true);
		this.setCanceledOnTouchOutside(false);
        this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.view_tips_loading);
		tips_loading_msg = (TextView) findViewById(R.id.tips_loading_msg);
		image = (ImageView) findViewById(R.id.image);

		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.anim_loading);
		// 使用ImageView显示动画
		image.startAnimation(hyperspaceJumpAnimation);
		tips_loading_msg.setText(this.message);
	}

	public void setText(String message) {
		this.message = message;
		tips_loading_msg.setText(this.message);
	}

	public void setText(int resId) {
		setText(getContext().getResources().getString(resId));
	}
}
