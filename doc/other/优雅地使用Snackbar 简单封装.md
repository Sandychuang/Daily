# ���ŵ�ʹ��Snackbar �򵥷�װ

## �ŵ�

- �򵥵���,��װ����һ��util����
- ���Զ���Snackbar�ı�����ɫ
- ���Զ���Snackbar��������ɫ
- ���Զ���Snackbar����ʾʱ��
- Ҳ����ʹ��Ĭ�ϵ�2��ʱ��������ʾ(1570ms,2750ms)
- ���Զ���action���ֵ���ɫ
- ���Զ������¼�
- ����Snackbar�����View

## Ч��

��˵��,�Ϲ��,����ͼ
![](http://olg7c0d2n.bkt.clouddn.com/17-10-15/66518582.jpg)
![](http://olg7c0d2n.bkt.clouddn.com/17-10-15/37271204.jpg)
![](http://olg7c0d2n.bkt.clouddn.com/17-10-15/19441533.jpg)
![](http://olg7c0d2n.bkt.clouddn.com/17-10-15/91463196.jpg)

## ˼·

1. �����ȶ����˼�����Ϣ����,Ȼ��ͬ����Ϣ���Ͷ����˲�ͬ����ɫ,��������
2. ����ʱ�����岻ͬ�ľ�̬����
3. ���ñ�����ɫ:�Ȼ�ȡ��Snackbar��View,Ȼ��ȥ���ñ�����ɫ����
4. �����ı���ɫ:�Ȼ�ȡ��Snackbar��View,Ȼ��findViewById(�����SnackbarԴ������id),�ҵ�View�����TextView,Ȼ������������ɫֵ����
5. ����action��ɫ,��ʵ��������Snackbar�����е��Ǹ�Button����ɫ,���һ�������Snackbar���־�֪����,�����ﲻ�ض�˵.
6. ʹ��ע��淶(����null,��Ϣ���͵�)

Ϊ�˷��������,������Snackbar�Ĳ���Դ��(Google)
``` xml
<?xml version="1.0" encoding="utf-8"?>
<!--
  ~ Copyright (C) 2015 The Android Open Source Project
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~      http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
-->

<view
    xmlns:android="http://schemas.android.com/apk/res/android"
    class="android.support.design.internal.SnackbarContentLayout"
    android:theme="@style/ThemeOverlay.AppCompat.Dark"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_gravity="bottom">

    <TextView
        android:id="@+id/snackbar_text"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:paddingTop="@dimen/design_snackbar_padding_vertical"
        android:paddingBottom="@dimen/design_snackbar_padding_vertical"
        android:paddingLeft="@dimen/design_snackbar_padding_horizontal"
        android:paddingRight="@dimen/design_snackbar_padding_horizontal"
        android:textAppearance="@style/TextAppearance.Design.Snackbar.Message"
        android:maxLines="@integer/design_snackbar_text_max_lines"
        android:layout_gravity="center_vertical|left|start"
        android:ellipsize="end"
        android:textAlignment="viewStart"/>

    <Button
        android:id="@+id/snackbar_action"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginLeft="@dimen/design_snackbar_extra_spacing_horizontal"
        android:layout_marginStart="@dimen/design_snackbar_extra_spacing_horizontal"
        android:layout_gravity="center_vertical|right|end"
        android:minWidth="48dp"
        android:visibility="gone"
        android:textColor="?attr/colorAccent"
        style="?attr/borderlessButtonStyle"/>

</view>
```

## Դ��

����������util���Դ��,ע��д�û��ǱȽ���ϸ��,���Ŵ���ܿ���

``` java
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xfhy.androidbasiclibs.R;

/**
 * author feiyang
 * create at 2017/10/12 16:57
 * description��SnakeBar�ļ򵥷�װ,����ʹ��
 * <p>
 * <p>
 * <p>
 * Դ����:
 * Snackbar snackbar = new Snackbar(findSuitableParent(view));
 * Snackbar�ĵ�һ������:view
 * <p>
 * findSuitableParent(view)��������Ҫ����:
 * 1.�������View��Ϊ��ʱ����������ڲ����з�����CoordinatorLayout���֣���ô���ص�View����CoordinatorLayout��
 * 2.���û��CoordinatorLayout���֣����Ǿ����ҵ�һ��idΪandroid.R.id.content��FrameLayout�������������ײ�ĸ����֣���
 * ��View����Ϊ��FrameLayout��
 * 3.��������¾�ʹ��View��Parent����һֱ�����View��Ϊ�ա�
 * <p>
 * �������ɫ���ò�����R.color������  Snackbar������ֻ����@ColorInt�����ñ�����ɫ
 */
public class SnackbarUtil {

    /**
     * ��Ϣ����
     */
    public static final int INFO = 1;
    /**
     * ȷ����Ϣ����
     */
    public static final int CONFIRM = 2;
    /**
     * ��������
     */
    public static final int WARNING = 3;
    /**
     * ��������
     */
    public static final int ALERT = 4;

    /**
     * ��Ϣ���͵ı�����ɫ
     */
    public final static int BLUE = 0xff2195f3;
    /**
     * ȷ����Ϣ���ͱ�����ɫ
     */
    public final static int GREEN = 0xff4caf50;
    /**
     * �������ͱ�����ɫ
     */
    public final static int ORANGE = 0xffffc107;
    /**
     * �������ͱ�����ɫ
     */
    public final static int RED = 0xfff44336;
    /**
     * action�ı���ɫ  ��ɫ
     */
    public final static int WHITE = 0xffFFFFFF;

    /**
     * ��Ϣ����   ���Java�е�ö������
     */
    @IntDef({INFO, CONFIRM, WARNING, ALERT})
    private @interface MessageType {
    }

    /**
     * ��ʾSnackbar,ʱ��:��ʱ��(1570ms)�����Զ�����ɫ
     *
     * @param view            The view to find a parent from.   view����Ϊ��,
     *                        ������׳�IllegalArgumentException("No suitable parent found from the
     *                        given view.Please provide a valid view.");
     * @param message         ��Ҫ��ʾ����Ϣ
     * @param messageColor    ��Ϣ�ı���ɫ
     * @param backgroundColor ������ɫ
     */
    public static void showBarShortTime(@NonNull View view, @NonNull String message, @ColorInt int
            messageColor, @ColorInt int backgroundColor) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        setSnackbarColor(snackbar, messageColor, backgroundColor);

        snackbar.show();
    }

    /**
     * ��ʾSnackbar,ʱ��:��ʱ��(2750ms)�����Զ�����ɫ
     *
     * @param view            The view to find a parent from.
     * @param message         ��Ҫ��ʾ����Ϣ
     * @param messageColor    ��Ϣ�ı���ɫ
     * @param backgroundColor ������ɫ
     */
    public static void showBarLongTime(@NonNull View view, @NonNull String message, @ColorInt int
            messageColor, @ColorInt int backgroundColor) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        setSnackbarColor(snackbar, messageColor, backgroundColor);
        snackbar.show();
    }

    /**
     * �Զ���ʱ����ʾSnackbar���Զ�����ɫ
     *
     * @param view            The view to find a parent from.
     * @param message         ��Ҫ��ʾ����Ϣ
     * @param duration        ��ʾʱ��   ��λ:ms
     * @param messageColor    ��Ϣ�ı���ɫ
     * @param backgroundColor ������ɫ
     */
    public static void showCustomCATSnackbar(@NonNull View view, @NonNull String message,
                                             @IntRange(from = 1) int duration,
                                             @ColorInt int messageColor,
                                             @ColorInt int backgroundColor) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
                .setDuration(duration);
        setSnackbarColor(snackbar, messageColor, backgroundColor);
        snackbar.show();
    }

    /**
     * ��ʾSnackbar,ʱ��:��ʱ��(1570ms)����ѡԤ������
     * android.support.design.widget.SnackbarManager.SHORT_DURATION_MS
     *
     * @param view    The view to find a parent from.
     * @param message ��Ҫ��ʾ����Ϣ
     * @param type    ��Ҫ��ʾ����Ϣ���� SnackbarUtil INFO,CONFIRM,WARNING,ALERT
     */
    public static void showBarShortTime(@NonNull View view, @NonNull String message, @MessageType
            int type) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        switchType(snackbar, type);
        snackbar.show();
    }

    /**
     * ��ʾSnackbar,ʱ��:��ʱ��(1570ms)����ѡԤ������
     * android.support.design.widget.SnackbarManager.SHORT_DURATION_MS
     *
     * @param view    The view to find a parent from.
     * @param message ��Ҫ��ʾ����Ϣ
     * @param type    ��Ҫ��ʾ����Ϣ���� SnackbarUtil INFO,CONFIRM,WARNING,ALERT
     */
    public static void showBarShortTime(@NonNull View view, @NonNull String message, @MessageType
            int type, @Nullable CharSequence text, @NonNull View.OnClickListener listener) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT).setAction(text,
                listener).setActionTextColor(WHITE);
        switchType(snackbar, type);
        snackbar.show();
    }

    /**
     * ��ʾSnackbar,ʱ��:��ʱ��(2750ms)����ѡԤ������
     * android.support.design.widget.SnackbarManager.LONG_DURATION_MS
     *
     * @param view    The view to find a parent from.
     * @param message ��Ҫ��ʾ����Ϣ
     * @param type    ��Ҫ��ʾ����Ϣ���� SnackbarUtil INFO,CONFIRM,WARNING,ALERT
     */
    public static void showBarLongTime(@NonNull View view, @NonNull String message, @MessageType
            int type) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        switchType(snackbar, type);
        snackbar.show();
    }

    /**
     * ��ʾSnackbar,ʱ��:��ʱ��(2750ms)����ѡԤ������
     * android.support.design.widget.SnackbarManager.LONG_DURATION_MS
     *
     * @param view    The view to find a parent from.
     * @param message ��Ҫ��ʾ����Ϣ
     * @param type    ��Ҫ��ʾ����Ϣ���� SnackbarUtil INFO,CONFIRM,WARNING,ALERT
     */
    public static void showBarLongTime(@NonNull View view, @NonNull String message, @MessageType
            int type, @Nullable CharSequence text, @NonNull View.OnClickListener listener) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction(text,
                listener).setActionTextColor(WHITE);
        switchType(snackbar, type);
        snackbar.show();
    }

    /**
     * �Զ���ʱ�� ��ʾSnackbar����ѡԤ������
     *
     * @param view     The view to find a parent from.
     * @param message  ��Ҫ��ʾ����Ϣ
     * @param duration ��ʾʱ��   ��λ:ms
     * @param type     ��Ҫ��ʾ����Ϣ���� SnackbarUtil INFO,CONFIRM,WARNING,ALERT
     */
    public static void showCustomTimeSnackbar(@NonNull View view, @NonNull String message,
                                              @IntRange(from = 1) int duration, @MessageType int
                                                      type) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
                .setDuration(duration);
        switchType(snackbar, type);
        snackbar.show();
    }

    /**
     * ����Snackbar������ɫ
     *
     * @param snackbar        Snackbar
     * @param backgroundColor ������ɫ
     */
    private static void setSnackbarBgColor(Snackbar snackbar, @ColorInt int backgroundColor) {
        View view = snackbar.getView();
        view.setBackgroundColor(backgroundColor);
    }

    /**
     * ����Snackbar���ֺͱ�����ɫ
     *
     * @param snackbar        Snackbar
     * @param messageColor    ������ɫ
     * @param backgroundColor ������ɫ
     */
    private static void setSnackbarColor(Snackbar snackbar, @ColorInt int messageColor,
                                         @ColorInt int backgroundColor) {
        View view = snackbar.getView();  //��ȡSnackbar�Լ��Ĳ���
        //����Snackbar�Լ��Ĳ��ֵı�����ɫ
        view.setBackgroundColor(backgroundColor);
        //����Snackbar�Լ��Ĳ����е�TextView����ɫ
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(messageColor);
    }

    /**
     * �л�Ԥ����Ϣ����
     *
     * @param snackbar Snackbar
     * @param type     ��Ϣ���� SnackbarUtil INFO,CONFIRM,WARNING,ALERT
     */
    private static void switchType(Snackbar snackbar, @MessageType int type) {
        switch (type) {
            case INFO:
                setSnackbarBgColor(snackbar, BLUE);
                break;
            case CONFIRM:
                setSnackbarBgColor(snackbar, GREEN);
                break;
            case WARNING:
                setSnackbarBgColor(snackbar, ORANGE);
                break;
            case ALERT:
                setSnackbarColor(snackbar, Color.YELLOW, RED);
                break;
        }
    }

    /**
     * ��Snackbar�����view
     *
     * @param snackbar Snackbar
     * @param layoutId ��Ҫ��ӵĲ��ֵ�id
     * @param index    �¼Ӳ�����Snackbar�е�λ��
     */
    public static void SnackbarAddView(Snackbar snackbar, @LayoutRes int layoutId, int index) {
        View snackbarView = snackbar.getView();
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbarView;

        View addView = LayoutInflater.from(snackbarView.getContext()).inflate(layoutId, null);

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams
                .WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        p.gravity = Gravity.CENTER_VERTICAL;
        snackbarLayout.addView(addView, index, p);
    }

}

```

# �ܽ�

����ʱ������װһ��Snackbar����ֵ�õ�,��Ŀ�кܶ�ط�����Ҫ�õ�,���һ����Ը�����ɫֵ,˲�������Ӧ����ֵ�������,�ǲ���?��������дһ����Ŀ��ʱ��,���ںܶ�ط�����Ҫʹ�õ�,Ȼ��򵥷�װһ��,����ʹ��.
��Ŀ��ַ: https://github.com/xfhy/Daily
