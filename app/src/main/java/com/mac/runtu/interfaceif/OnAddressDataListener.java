package com.mac.runtu.interfaceif;


import com.mac.runtu.databasehelper.AddressDataInfo;

import java.util.ArrayList;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/24 0024 下午 3:05
 */
public interface OnAddressDataListener {


    void onSuccess(ArrayList<AddressDataInfo> info);

    void onfail();

}
