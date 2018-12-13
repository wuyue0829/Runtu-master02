package com.mac.runtu.utils;

public class StringUtils
{
  public static String getRedSpots(int paramInt)
  {
    if (paramInt == 1) {
      return "红色景点";
    }
    return "其他";
  }

  public static String setContent(String paramString)
  {
    String str = paramString;
    if (paramString.contains("img")) {
      str = paramString.substring(0, paramString.indexOf("<img"));
    }
    return str;
  }

}
