package com.xnumberkeyboard.android.samples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.xnumberkeyboard.android.CommonPopupWindow;
import com.xnumberkeyboard.android.PasswordEditText;
import com.xnumberkeyboard.android.XNumberKeyboardView;

public class MainActivity extends AppCompatActivity
//        implements XNumberKeyboardView.IOnKeyboardListener
{

    PasswordEditText editText;
    XNumberKeyboardView keyboardView;
    private CommonPopupWindow window;
    private View rvKeyboard;
    PopupWindow instance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvKeyboard=findViewById(R.id.rv_keyboard);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                initPopupWindow();
            }
        });
    }

    private void initPopupWindow() {
        // get the height of screen
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenHeight = metrics.heightPixels;
        // create popup window
        window = new CommonPopupWindow(this, R.layout.popwindow_keybord, ViewGroup.LayoutParams.MATCH_PARENT, (int) (screenHeight * 0.5)) {
            @Override
            protected void initView() {
                View view = getContentView();
                editText = (PasswordEditText) view.findViewById(R.id.edit_text);
                keyboardView = (XNumberKeyboardView) view.findViewById(R.id.view_keyboard);
                keyboardView.shuffleKeyboard();
            }

                @Override
                protected void initEvent () {
                    keyboardView.setIOnKeyboardListener(new XNumberKeyboardView.IOnKeyboardListener() {
                        @Override
                        public void onInsertKeyEvent(String text) {
                             editText.append(text);
                        }

                        @Override
                        public void onDeleteKeyEvent() {
                            int start = editText.length() - 1;
                            if (start >= 0) {
                                editText.getText().delete(start, start + 1);
                            }
                        }

                        @Override
                        public void onInputMaxEvent() {
                            Toast.makeText(getApplicationContext(),editText.getText().toString(),Toast.LENGTH_SHORT).show();
                            Log.d("max==",editText.getText().length()+","+editText.getText().toString());
                            instance.dismiss();
                        }
                    });
                }

                @Override
                protected void initWindow () {
                    super.initWindow();
                     instance = getPopupWindow();
                    instance.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            WindowManager.LayoutParams lp = getWindow().getAttributes();
                            lp.alpha = 1.0f;
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                            getWindow().setAttributes(lp);
                        }
                    });
                }
            };
        PopupWindow win=window.getPopupWindow();
        win.setAnimationStyle(R.style.animTranslate);
        window.showAtLocation(rvKeyboard, Gravity.BOTTOM, 0, 0);
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha=0.3f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }

    }
