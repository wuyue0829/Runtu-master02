package com.mac.runtu.activity.ruraltourism;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.mac.runtu.R;
import com.mac.runtu.business.AdBiz;
import com.mac.runtu.business.TopImageBannerBiz;
import com.mac.runtu.global.GlobalConstants;
import com.mac.runtu.javabean.PictureInfo;
import com.mac.runtu.javabean.PuralTourismInfoFromUuid;
import com.mac.runtu.javabean.RuralTourismListInfo;
import com.mac.runtu.utils.Content2StrUtils;
import com.mac.runtu.utils.GSonUtil;
import com.mac.runtu.utils.LogUtils;
import com.mac.runtu.utils.MyHttpUtils;
import com.mac.runtu.utils.ToastUtils;
import com.youth.banner.Banner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 旅游信息详情页
 */
public class AttractionDetailsActivity extends AppCompatActivity {

    private static final String TAG = "AttractionDetailsActivity";

    @BindView(R.id.back_Iv)
    ImageView backIv;
    @BindView(R.id.banner)
    Banner    banner;
    @BindView(R.id.banner_title)
    TextView  bannerTitle;
    @BindView(R.id.banner_content)
    TextView  bannerContent;


    @BindView(R.id.map_navigation_Iv)
    ImageView mapNavigationIv;
    @BindView(R.id.name_Tv)
    TextView  nameTv;
    @BindView(R.id.phone_Tv)
    TextView  phoneTv;
    @BindView(R.id.location_Tv)
    TextView  locationTv;

    @BindView(R.id.merchants_release)
    TextView merchantsRelease;

    @BindView(R.id.tv_content)
    TextView tvContent;


    @BindView(R.id.wv)
    WebView wv;

    private String uuid;
    private ArrayList<String> images = new ArrayList<>();
    private RuralTourismListInfo data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction_details);
        ButterKnife.bind(this);

        uuid = getIntent().getStringExtra(GlobalConstants
                .key_uuid);

        if (TextUtils.isEmpty(uuid)) {
            return;
        }
        initData();
        initBannerData();

    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();

            res.updateConfiguration(newConfig, res.getDisplayMetrics());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                createConfigurationContext(newConfig);
            } else {
                res.updateConfiguration(newConfig, res.getDisplayMetrics());
            }
        }
        return res;
    }

    private void initData() {
        MyHttpUtils.getStringDataFromNet(GlobalConstants.tour_detail_url,
                null, GlobalConstants.key_uuid, uuid, new MyHttpUtils
                        .OnNewWorkRequestListener() {


                    @Override
                    public void onNewWorkSuccess(String result) {
                        if (result != null) {
                            parseData(result);
                        }
                    }

                    @Override
                    public void onNewWorkError(String msg) {
                        ToastUtils.makeTextShowNoNet
                                (AttractionDetailsActivity.this);
                    }
                });


    }

    public static String getNewContent(String paramString)
    {
        try
        {
            Object localObject = Jsoup.parse(paramString);
            Iterator localIterator = ((Document)localObject).getElementsByTag("img").iterator();
            while (localIterator.hasNext()) {
                ((Element)localIterator.next()).attr("width", "100%").attr("height", "auto");
            }
            String str = (localObject).toString();
            return str;
        }
        catch (Exception localException) {}
        return paramString;
    }

    //解析数据
    private void parseData(String result) {
        PuralTourismInfoFromUuid info = GSonUtil
                .parseJson(result, PuralTourismInfoFromUuid.class);
        data = info.objList;

        if (data != null) {
            //有数据
            bannerTitle.setText(data.title);
            Content2StrUtils.setContentStr(data.content, bannerContent);
            WebSettings webSettings = this.wv.getSettings();
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webSettings.setJavaScriptEnabled(true);
            LogUtils.e("内容",data.content);
            this.wv.loadDataWithBaseURL("about:blank", getNewContent(data.content), "text/html", "utf-8", null);
            this.wv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            this.wv.setWebViewClient(new WebViewClient()
            {
                public void onPageFinished(WebView paramAnonymousWebView, String paramAnonymousString)
                {
                    super.onPageFinished(paramAnonymousWebView, paramAnonymousString);
                    paramAnonymousWebView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                }
            });

            ArrayList<PictureInfo> pictureInfos = data.pictureInfos;


            images = AdBiz.getImageListUrl(pictureInfos);
            initBannerData();


            nameTv.setText(data.title);
            phoneTv.setText(data.phone);
            locationTv.setText(data.address); //地址
            Content2StrUtils.setContentStr(data.content, tvContent);

        } else {
            //无数据
            ToastUtils.makeTextShowNoData
                    (AttractionDetailsActivity.this);
        }


    }

    void initBannerData() {

        TopImageBannerBiz bannerBiz = new TopImageBannerBiz
                (banner);

        if (images != null && images.size() > 0) {
            bannerBiz.autoShowImage(images);
        } else {
            bannerBiz.autoShowImage(AdBiz.getImageListId());
        }

    }

    @OnClick({R.id.back_Iv, R.id.map_navigation_Iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_Iv:
                finish();
                break;
            case R.id.map_navigation_Iv:

                if (data != null) {



                    Intent intent = new Intent(AttractionDetailsActivity.this,
                            RuralTourismMapGPSActivity.class);
                    //将坐标传过去
                    //intent.putExtra("", data.province);

                    intent.putExtra(GlobalConstants.key_latitude, data
                            .latitude);
                    intent.putExtra(GlobalConstants.key_longitude, data
                            .longitude);

                    startActivity(intent);
                }

                break;
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
