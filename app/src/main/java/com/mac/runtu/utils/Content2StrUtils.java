package com.mac.runtu.utils;

import android.text.Html;
import android.widget.TextView;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/26 0026 下午 2:29
 */
public class Content2StrUtils {

    public static void setContentStr(String content, TextView tv) {

        if (content.contains("<span")) {
            tv.setText(Html.fromHtml(content));
        } else {
            tv.setText(content);
        }
    }


    public static void setContentStr1(String paramString, TextView paramTextView)
    {
        if ((paramString.contains("<")) || (paramString.contains(">")))
        {
            int i = paramString.indexOf("<img");
            if (i > 0) {
                paramString = paramString.substring(0, i);
            }else if(i == -1){
                paramString = paramString.substring(0, paramString.length());
            }else{
                paramString = "详情为图片，请点击查看";
            }
            paramTextView.setText(Html.fromHtml(paramString));
        }else{
            paramTextView.setText(paramString);
        }
    }
}
