package com.mac.runtu.global;

public interface GlobalConstants {

    //public static final String BASE_IP = "192.168.1.254:8080";
     public static final String BASE_IP = "101.201.102.161";
    //public static final String BASE_IP = "www.runtu365.cn";
    //public static final String BASE_IP = "101.201.102.16";

    public static final String BASE_ALL_URL = "http://" + BASE_IP + "/";
    //public static final String BASE_ALL_URL = "http://101.201.102.161/";

    //基础路径
    public static final String BASE_URL = BASE_ALL_URL + "app/";

    //图片访问路径
    public static final String Base_imgae_url = BASE_ALL_URL + "upload/";


    //版更更新路径
    public static final String NEW_VERSION_URL = BASE_ALL_URL + "upload/app/";

    public static final String check_version_url = NEW_VERSION_URL +
            "runtuupdata.json";
    public static final String apk_down_load_url = NEW_VERSION_URL + "runtu" +
            ".apk";

    public static final String apk_down_load_image_url = NEW_VERSION_URL +
            "runtuimage.png";


    //验证手机号是否可用
    public static final String NUMBER_CHECK_CANUSE = BASE_URL +
            "UserAction/checkPhoneCanUsable_app.action";

    //验证码验证
    public static final String get_code      = BASE_URL +
            "UserAction/sendMobileMessage_app.action";
    //注册信息路径
    public static final String REGISTER_INFO = BASE_URL +
            "UserAction/registerUser_App.action";

    //忘记密码

    public static final String user_forget_password = BASE_URL +
            "UserAction/modifyUserPassword_app.action";

    //广告轮播
    public static final String AD_INFO_URL = BASE_URL +
            "PictureAction/pictureList_app.action";

    //生成订单以后 得到拼接好的支付字符串
    public static final String get_pay_way_str_url = BASE_URL +
            "CreateOrderAction/dopayAction_app.action";


    /**
     * 个人信息
     */
    //登录
    public static final String LOGIN_URL = BASE_URL +
            "UserAction/checkUserLogin_app.action";

    //上传图片的地址
    public static final String upLoading_images_url = BASE_URL +
            "UploadPictureAction/uploadPicture_app.action";


    //图片个人图片信息提交页

    //个人资料信息提交页
    public static final String USER_INFO_COMMIT_URL = BASE_URL +
            "PersonalCenterAction/personalProfile_app.action";

    //个人订单列表路径
    public static final String user_order_data_url = BASE_URL +
            "UserOrderAction/orderList_app.action";


    //用户资料提交
    public static final String USER_COMMIT_URL = BASE_URL +
            "PersonalCenterAction/modifyPersonalProfile_app.action";


    //地址交互的路径
    public static final String ADDRESS_URL = BASE_URL +
            "District/loadDistrict.action";

    //查询用户地址列表
    public static final String user_get_good_address_url = BASE_URL +
            "ShippingAddressAction/shippingAddressList_app.action";
    //添加地址
    public static final String user_add_address_url      = BASE_URL +
            "ShippingAddressAction/addShippingAddress_app.action";
    //设置默认地址
    public static final String user_nomal_address_url    = BASE_URL +
            "ShippingAddressAction/modifyShippingAddressStatus_app.action";
    //删除地址
    public static final String user_delete_address_url   = BASE_URL +
            "ShippingAddressAction/removeShippingAddress_app.action";

    //意见反馈
    public static final String user_suggest_upload_url = BASE_URL +
            "SuggestionFeedbackAction/addSuggestionFeedback_app.action";
    //我的发布
    public static final String user_publish_data_url   = BASE_URL +
            "UserPublishAction/userPublish_app.action";

    //查询个人购物车
    public static final String user_shoppint_car_url = BASE_URL +
            "ShopCatAction/shopCatList_app.action";

    //购物车删除
    public static final String user_shoppint_car_delete_url = BASE_URL +
            "ShopCatAction/removeCartByUuid_app.action";

    //支付生成订单
    public static final String user_get_order_detail_str_url = BASE_URL +
            "CreateOrderAction/createOder_app.action";

    //订单收货通知服务端
    public static final String order_get_good_inform_fwd_url = BASE_URL +
            "UserOrderAction/modifyOrderStatus_app.action";

    //订单删除
    public static final String user_order_delete_url = BASE_URL +
            "UserOrderAction/removeUserOrder_app.action";

    //个人中心,获得用户信息,
    public static final String user_get_info_url = BASE_URL +
            "PersonalCenterAction/personalProfile_app.action";

    //个人中心,获取订单数量,
    public static final String user_get_order_num_url = BASE_URL +
            "UserOrderAction/userOrderNum_app.action";

    //个人 是否在别的地方登陆
    public static final String user_status_choeck_url = BASE_URL +
            "UserAction/checkUserLoginStatus_app.action";

    //个人订单
    public static final String key_order_status            = "status";
    public static final String value_order_sta__all        = "";
    public static final String value_order_sta__waitpay    = "0";
    public static final String value_order_sta_waitsetgood = "1";
    public static final String value_order_sta_waitgetgood = "2";
    public static final String value_order_sta_final       = "3";
    public static final String value_order_sta_cancel      = "4";


    /**
     * 客商模块
     */
    //搜索条
    public static final String business_search_title_url = BASE_URL +
            "BusinessDynamicAction/businessDynamicSearchByTitle_app.action";

    //选择搜索
    public static final String business_search_area_url = BASE_URL +
            "BusinessDynamicAction/businessDynamicSearchByArea_app.action";
    //客商列表
    public static final String business_List_url        = BASE_URL +
            "BusinessDynamicAction/businessDynamicList_app.action";

    public static final String business_detail_url = BASE_URL +
            "BusinessDynamicAction/businessDynamicByUuid_app.action";

    //客商动态同类信息
    public static final String business_qita_url = BASE_URL +
            "BusinessDynamicAction/businessDynamicByKind_app .action";

    //客商信息上传
    public static final String business_keshang_data_uploading_url = BASE_URL +
            "BusinessDynamicAction/addbusinessDynamic_app.action";
    //得到Uuid

    public static final String business_data_commiet_uuid_url = BASE_URL
            + "GetUuidAction/getUuid_app.action";

    /**
     * 产权流转
     */
    //列表
    public static final String property_list_url            = BASE_URL +
            "PropertyAction/propertyList_app.action";
    //选择地址搜索
    public static final String property_select_adddress_url = BASE_URL +
            "PropertyAction/propertySearchByArea_app.action";
    //产权流转的详情
    public static final String property_info_Detail_url     = BASE_URL +
            "PropertyAction/propertyByUuid_app.action";

    //内容搜索
    public static final String property_search_title_url = BASE_URL +
            "PropertyAction/propertySearchByTitle_app.action";

    //内容提交页
    public static final String property_commite_url = BASE_URL +
            "PropertyAction/addProperty_app.action";

    /**
     * 乡村旅游
     */
    public static final String tour_list_url           = BASE_URL +
            "TourAction/tourList_app.action";
    //详情页
    public static final String tour_detail_url         = BASE_URL +
            "TourAction/tourByUuid_app.action";
    //乡村旅游选择查询
    public static final String tour_select_address_url = BASE_URL +
            "TourAction/tourSearchByArea_app.action";
    //搜索
    public static final String tour_search_tilte_url   = BASE_URL +
            "TourAction/tourSearchByTitle_app.action";


    /**
     * 创业培训
     */
    public static final String entre_train_list_url   = BASE_URL +
            "EnterpriseTrainingAction/enterpriseTrainingList_app.action";
    //详情页
    public static final String entre_train_detail_url = BASE_URL +
            "EnterpriseTrainingAction/enterpriseTrainingByUuid_app.action";
    //地址选择查询
    public static final String entre_train_select_url = BASE_URL +
            "EnterpriseTrainingAction/enterpriseTrainingSearchByArea_app" +
            ".action";

    //内容搜索
    public static final String entre_train_serarch_title_url = BASE_URL +
            "EnterpriseTrainingAction/enterpriseTrainingSearchByTitle_app" +
            ".action";

    /**
     * 农资农机
     */

    public static final String mach_list_url   = BASE_URL +
            "AgriculturalAction/agriculturalList_app.action";
    //农资农机详情页
    public static final String mach_detail_url = BASE_URL +
            "AgriculturalAction/agriculturalByUuid_app.action";

    //选择条
    public static final String mach_select_url = BASE_URL +
            "AgriculturalAction/agriculturalSearchByArea_app.action";

    //搜索条
    public static final String mach_search_title_url = BASE_URL +
            "AgriculturalAction/agriculturalSearchByTitle_app.action";

    //信息提交页
    public static final String mach_commit_url      = BASE_URL +
            "AgriculturalAction/addAgricultural_app.action";
    /**
     * 劳务合作
     */
    public static final String lao_service_list_url = BASE_URL +
            "LabourCooperationAction/labourCooperationList_app.action";

    //地址选择路径
    public static final String lao_service_address_select_url = BASE_URL +
            "LabourCooperationAction/labourCooperationSearchByArea_app.action";

    //详情信息
    public static final String lao_service_detail_url = BASE_URL +
            "LabourCooperationAction/labourCooperationByUuid_app.action";
    //同类信息
    public static final String lao_service_qita_url   = BASE_URL +
            "LabourCooperationAction/labourCooperationByKind_app.action";

    //信息发布
    public static final String lao_commit_url = BASE_URL +
            "LabourCooperationAction/addLabourCooperation_app.action";

    /**
     * 物流信息
     */
    public static final String Logistics_address_select_url = BASE_URL +
            "LogisticsAction/logisticsSearchByArea_app.action";

    //列表
    public static final String Logistics_list_url = BASE_URL +
            "LogisticsAction/logisticsList_app.action";

    //详情
    public static final String Logistics_info_detail_url  = BASE_URL +
            "LogisticsAction/logisticsByUuid_app.action";
    //内容搜索
    public static final String Logistics_search_title_url = BASE_URL +
            "LogisticsAction/logisticsSearchByTitle_app.action";

    //地址搜索
    public static final String Logistics_adddress_select_url = BASE_URL +
            "LogisticsAction/logisticsSearchByArea_app.action";
    //信息提交页
    public static final String Logistics_commit_url          = BASE_URL +
            "LogisticsAction/addlogistics_app.action";

    //捎货信息提交页
    public static final String Logistics_add_good_commit_url = BASE_URL +
            "cargoAction/addCargo_app.action";

    /**
     * 体验农场
     */

    public static final String exper_farm_gps_location_url = BASE_URL +
            "GetGPSAction/getGPS_app.action";

    public static final String key_gspProt = "gpsPort";


    /**
     * 特产电商
     */

    // 特产电商 信息列表页      // 体验农场
    public static final String specialty_list_url = BASE_URL +
            "BusinessAction/businessList_app.action";

    //产品推荐
    public static final String specialty_product_recommend_url = BASE_URL +
            "BusinessAction/businessRecommendationList_app.action";

    //产品详情
    public static final String specialty_product_detail_url = BASE_URL +
            "BusinessAction/businessByUuid_app.action";

    //购物车添加地址
    public static final String shopping_car_add_url = BASE_URL +
            "ShopCatAction/addShopCart_app.action";

    //特产电商 搜索 筛选以后 得到数据列表
    public static final String specialtey_search_filter_url = BASE_URL +
            "BusinessAction/businessSearch_app.action";

    public static final String key_payment  = "payment";
    public static final String key_minPrice = "minPrice";
    public static final String key_hitCount = "hitCount";

    /**
     * 查询类型 (苹果)
     */
    public static final String select_type_lsit_url = BASE_URL +
            "KindDictionaryAction/kindDictionaryList_app.action";


    /**
     * 筛选类型获得路径
     */
    public static final String good_filter_url = BASE_URL +
            "BusinessTypeAction/loadBusinessType_app.action";

    public static final String key_parentCode              = "parentCode";
    public static final String value_filter_one            = "0";
    //区分筛选来自哪个
    public static final String value_mode_filter_specialty = "1";
    public static final String value_mode_filter_farm      = "2";


    /**
     * 实名认证
     */

    public static final String user_rel_name_image_url = BASE_URL +
            "PersonalCenterAction/uploadIDcard_app.action";

    public static final String user_rel_name_detail_url = BASE_URL +
            "PersonalCenterAction/uploadVerifyInfo_app.action";

    //账号存储名
    public static final String SP_USERNUM           = "username";
    public static final String SP_USERPASS          = "userpassword";
    public static final String SP_USERID            = "userId";
    public static final String SP_use_iamgepath     = "imagepath";
    public static final String sp_use_ordercount    = "orderCount";
    public static final String sp_use_finishcount   = "finishCount";
    public static final String sp_use_unfinishcount = "unfinishCount";
    public static final String sp_loginStatus       = "loginStatus";
    public static final String userType       = "userType";


    public static final String sp_user_name = "user_name";
    public static final String sp_really_name_status = "reallyNameStatus";


    //手机的key
    public static final String KEY_PHONENUM = "phoneNumber";
    public static final String KEY_PASSWRED = "userPassword";

    public static final String key_new_passwred = "newPassword";


    public static final String KEY_PAGENUM  = "pageNum";
    public static final String KEY_PAGESIZE = "pageSize";
    public static final String KEY_USERID   = "userUuid";


    //页面地址的key
    public static final String KEY_ADDRESS = "parentCode";

    //搜索
    public static final String key_tilte = "title";
    //内容搜索

    public static final String key_province = "province";
    public static final String key_city     = "city";
    public static final String key_county   = "county";
    public static final String key_town     = "town";

    public static final String key_final_province = "targetProvince";
    public static final String key_final_city     = "targetCity";
    public static final String key_final_county   = "targetCounty";
    public static final String key_final_town     = "targetTown";
    public static final String key_final_village  = "targetVillage";

    public static final String key_start_province = "startProvince";
    public static final String key_start_city     = "startCity";
    public static final String key_start_county   = "startCounty";
    public static final String key_start_town     = "startTown";
    public static final String key_start_village  = "startVillage";
    public static final String key_service_more   = "mora";

    public static final String key_village     = "village";
    public static final String key_status      = "status";
    public static final String key_kind        = "kind";
    public static final String key_uuid        = "uuid";
    public static final String key_type        = "type";
    public static final String key_content_ui  = "content_ui";
    public static final String key_model       = "model";
    //跳转详情页页面的表示
    public static final String key_detail_uuid = "detail_uuid";
    public static final String key_object      = "object";

    public static final String key_loginStatus = "loginStatus";

    //客商信息提交
    public static final String key_info = "info";


    //用户发布跳转自己的发布
    public static final String key_user           = "user";
    //意见反馈
    public static final String key_content        = "content";
    public static final String key_contact        = "contact";
    //区分筛选是从那边打开
    public static final String key_calssfity_open = "open";

    //地址选择来自特产电商的页面商品
    public static final String key_specialty_detail = "address";
    public static final String key_my_address_uuid  = "address_uuid";

    //客商发布
    public static final String value_type_buiss_shenfn  = "1";
    public static final String value_type_fram_shenfn   = "2";
    public static final String value_from_select        = "select";
    public static final String value_from_sreach_item   = "sreach";
    public static final String value_shenfen_busin_type = "1";
    public static final String value_shenfen_fram_type  = "2";
    public static final String value_page_size          = "10";
    //购物车


    public static final String key_allPay = "allPay";

    //购物车跳转付款页面
    public static final String key_shop_car_info = "shop_car_info";

    //订单定位坐标
    public static final String key_IMEI = "IMEI";

    //乡村旅游坐标key

    /**
     * 纬度
     */
    public static final String key_latitude  = "latitude";
    /**
     * 经度
     */
    public static final String key_longitude = "longitude";

    /* 1代表电商产品模块类型，2代表劳务合作模块类型，3代表客商动态模块，
             4代表农资农机模块，5代表产权信息模块，6代表旅游信息模块，7创业培训信息模块*/
    public static final String AD_TYPE                = "type";
    public static final String AD_TYPE_SHOP           = "1";
    public static final String AD_TYPE_SERVICE        = "2";
    public static final String AD_TYPE_BUSINESS       = "3";
    public static final String AD_TYPE_MACHINE        = "4";
    public static final String AD_TYPE_PROPERTY_RIGHT = "5";
    public static final String AD_TYPE_TRAVEL         = "6";
    public static final String AD_TYPE_EDUCATION      = "7";
    public static final String AD_TYPE_wului          = "9";//车辆类型
    public static final String AD_TYPE_take_goods     = "8";//货物类型

    public static final String AD_TYPE_exper_farm    = "10";//体验农场
    public static final String AD_TYPE_home_ad_image = "11";//首页轮播条
    public static final String AD_TYPE_speci_ad      = "12";//特产电商广告条

    //订单mode
    public static final String value_mode_user_center_order = "1";//个人中心订单
    public static final String value_mode_expm_farm         = "2";//体验农场


    //model 识别  在个人订单列表页跳转页面时使用
    public static final int mode_user_center_order_1 = 1;//个人中心订单
    public static final int mode_expm_farm_2         = 2;//体验农场

    //广播

    //跟新数据
    public static final String ORDER_UPDATE = "com.dqn.pdkj" +
            ".ORDER_UPDATE";

    //客服电话
    public static final String service_phone = "0911-3638333";

    public static final String WXZF_APPID = "wx3e7b5d7312d65089";

    //验证码用到的key
    public static final String key_phone    = "phone";
    public static final String key_password = "password";

    public static final String key_from_fitler  = "from_fitler";
    public static final String key_isChangerPwd = "isChangerPwd";
    public static final String key_fromOrder    = "fromOrder";
    public static final String key_position     = "position";


    public static final String sp_pay_from       = "pay_from";
    public static final String sp_user_type_name = "user_type_name";
    public static final String sp_tuichu_deng_lu = "isDeleteUserInfo";

    public static final String json_cache_name = "cacheFile";
    public static final int    json_cache_time = 30;

    public static final String cache_key_addressRecommend = "addressRecommend";
    public static final String cache_key_productRecommend = "productRecommend";


    /**
     * 通知时间所用的字符串标志
     */
    public static final String send_updata_Logistics = "get_good_logistice";//捎货

    //订单有变化
    public static final String user_Order_Updata = "userOrderUpdata";

    //通知去跳转
    public static  final String go_2_publish = "go2publish";

    /**
     * 身份审核状态
     */
    public static final int notReaNemAttestation  = 0;
    public static final int reaNemAttestationIng  = 3;
    public static final int reaNemAttestationSucc = 1;
    public static final int reaNemAttestationFail = 2;


}
