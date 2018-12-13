package com.mac.runtu.javabean;

import java.util.List;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/9 0009 下午 2:31
 */
public class AddressInfo {


    public List<ProvinceBean> citylist;

    //省
    public class ProvinceBean {

        //省
        public String p;

        public List<CityBean> c;
    }

    //市
    public class CityBean {

        public String n;

        public List<AreaBean> a;

    }

    //县,区
    public class AreaBean {

        public String s;
    }

}
