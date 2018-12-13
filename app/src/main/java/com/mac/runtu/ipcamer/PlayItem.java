package com.mac.runtu.ipcamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mac.runtu.R;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import hfsdk.light.pro.HFColorLightPro;
import socket.hfsdk.spro.Utils;

public class PlayItem
        extends LinearLayout
{
    private static final String MANIP = "192.168.0.19";
    private static final int SET_INFO_MESSAGE = 1;
    private static final int SHOW_UDP_INFO_TOAST = 2;
    private ToggleButton c1_toggle_deng;
    private boolean isOpen;
    private Context mContext;
    private OutputStream manout;
    private BufferedReader manreader;
    private Socket mansocket;
    private String name;
    private char[] read_buf = new char[1024];
    private String recvData = null;
    private Socket socket;
    private TextView tv_name;
    private String uuid;
    private OutputStream wanout;
    private BufferedReader wanreader;

    public PlayItem(Context paramContext)
    {
        super(paramContext);
    }

    public PlayItem(Context paramContext, String paramString1, boolean paramBoolean, String paramString2, String paramString3)
    {
        this(paramContext);
        this.mContext = paramContext;
        this.uuid = paramString1;
        this.isOpen = paramBoolean;
        this.name = paramString2;
        init();
        initSocket();
        initManSocket();
    }

    private void CloseSwitch()
    {
        caozuo("HFSPROCLOSE", this.uuid, "WAN", "888888");
    }

    private void OpenSwitch()
    {
        String str = this.uuid;
        caozuo("HFSPROOPEN", str, "WAN", "888888");
    }

    private String caozuo(String paramString1, String paramString2, String paramString3, String paramString4)
    {
        if ((paramString1 != null) && (paramString2 != null) && (paramString3 != null) && (paramString4 != null))
        {
            String orderStr = getSendOrder(paramString1,paramString2,paramString3,paramString4);
            if(orderStr != null && !orderStr.equals("")){
                try {
                    if(paramString2.equals("WAN")){
                        if(socket != null){
                            try {
                                wanreader = new BufferedReader(new InputStreamReader(
                                        socket.getInputStream(), "utf-8"));
                                wanout = socket.getOutputStream();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if(wanout != null){
                                wanout.write(orderStr.getBytes());
                            }else{
                            }
                        }else{

                        }
                    }else if(paramString2.equals("MAN") || paramString2.equals("AP")){
                        if(manout != null){
                            manout.write(orderStr.getBytes());
                        }else{
                        }
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                if(paramString2.equals("WAN")){
                    //外网获取返回数据
                    if(wanreader != null){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                recvData = null;
                                read_buf = new char[1024];
                                int read_buf_len = 0;
                                try {
                                    read_buf_len = wanreader.read(read_buf);
                                    if (read_buf_len > 0) {
                                        recvData = new String(read_buf).substring(0, read_buf_len);
                                    }
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                }else if(paramString2.equals("MAN")|| paramString2.equals("AP")){
                    if(manreader != null){
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                recvData = null;
                                read_buf = new char[1024];
                                int read_buf_len = 0;
                                try {
                                    read_buf_len = manreader.read(read_buf);
                                    if (read_buf_len > 0) {
                                        recvData = new String(read_buf).substring(0, read_buf_len);
                                    }
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    }
                    }
                }

            }
        return recvData;
    }

    private String getSendOrder(String paramString1, String paramString2, String paramString3, String paramString4)
    {
        String arg0 = paramString1;
        String arg1 = paramString2;
        String arg2 = paramString3;
        String arg3 = paramString4;
        String result = "";
        if(arg0.equals("HFCLPROSTATE")){//获取状态
            result = HFColorLightPro.sendHFCLPROStateOrder(arg0, arg1, arg2, arg3);
        }else if(arg0.equals("HFCLPROOPEN")){//打开设备
            result = HFColorLightPro.sendHFCLPROOpenOrder(arg0, arg1, arg2, arg3);
        }else if(arg0.equals("HFCLPROCLOSE")){//关闭设备
            result = HFColorLightPro.sendHFCLPROCloseOrder(arg0, arg1, arg2,
                    arg3);
        }else if(arg0.equals("HFCLPROTIMETASK")){//获取定时任务信息
            result = HFColorLightPro.getHFCLPROTimeTaskOrder(arg0, arg1, arg2,
                    arg3);
        }else if(arg0.split("=").length == 2 && arg0.split("=")[0].equals("HFCLPROTIMETASK")){//设置定时任务
            result = HFColorLightPro.setHFCLPROTimeTaskOrder(arg0, arg1, arg2,
                    arg3);
        }else if(arg0.equals("HFCLPROTIMESHUT")){//获取延时
            result = HFColorLightPro.getHFCLPROTimeShutOrder(arg0, arg1, arg2,
                    arg3);
        }else if(arg0.split("=").length == 2 && arg0.split("=")[0].equals("HFCLPROTIMESHUT")){//获取延时
            result = HFColorLightPro.setHFCLPROTimeShutOrder(arg0, arg1, arg2,
                    arg3);
        }else if(arg0.length()>14 && arg0.subSequence(0, 15).equals("HFCLPROTIMESHUT=")){//设置延时
            result = HFColorLightPro.setHFCLPROTimeShutOrder(arg0, arg1, arg2,
                    arg3);
        }else if(arg0.equals("HFCLPROTIME")){//获取设备的时间
            result = HFColorLightPro.getHFCLPROTimeOrder(arg0, arg1, arg2,
                    arg3);
        }else if(arg0.length()>10 && arg0.subSequence(0, 11).equals("HFCLPROTIME=")){//设备校时
            result = HFColorLightPro.setHFCLPROTimeOrder(arg0, arg1, arg2,
                    arg3);
        }else if(arg0.length()>12 && arg0.split("=")[0].equals("HFCLPROCMPPWD")){//密码判断
            result = HFColorLightPro.sendHFCLPROCmpPwdOrder(arg0, arg1, arg2,
                    arg3);
        }else if(arg0.split("=")[0].equals("HFCLPROUPDATEPWD")){//修改密码
            result = HFColorLightPro.sendHFCLPROUpdatePwdOrder(arg0, arg1, arg2, arg3);
        }else if(arg0.split("=")[0].equals("HFCLPROUPDATE")){//固件更新
            result = HFColorLightPro.sendHFCLPROUpdateOrder(arg0, arg1, arg2, arg3);
        }else if(arg0.equals("HFCLPROV")){//获取设备固件版本信息
            result = HFColorLightPro.getHFCLPROVOrder(arg0, arg1, arg2,
                    arg3);
        }else if(arg0.equals("HFCLPRODFT")){//恢复出厂
            result = HFColorLightPro.sendHFCLPRODFTOrder(arg0, arg1, arg2, arg3);
        }else if(arg0.equals("HFCLPRORESET")){//设备重启
            result = HFColorLightPro.sendHFCLPROResetOrder(arg0, arg1, arg2, arg3);
        }else if(arg0.equals("HFREQUESTTIMEZONE")){
            result = Utils.getHFDEVTimeZoneOrder(arg0, arg1);
        }else if(arg0.split("=").length == 2 && arg0.split("=")[0].equals("HFPUSHTIMEZONE")){
            result = Utils.setHFDEVTimeZoneOrder(arg0, arg1);
        }else if(arg0.equals("HFCLPRORSSI")){
            result = HFColorLightPro.getHFCLPRORssiOrder(arg0, arg1, arg2, arg3);
        }else if(arg0.equals("HFCLPROAUTODFT")){
            result = HFColorLightPro.getHFCLPROAutoDftOrder(arg0, arg1, arg2, arg3);
        }else if(arg0.split("=").length == 2 && arg0.split("=")[0].equals("HFCLPROAUTODFT")){
            result = HFColorLightPro.setHFCLPROAutoDftOrder(arg0, arg1, arg2, arg3);
        }else if(arg0.equals("HFCLPRORGBW")){
            result = HFColorLightPro.getHFCLPRORGBWOrder(arg0, arg1, arg2, arg3);
        }else if(arg0.split("=").length == 2 && arg0.split("=")[0].equals("HFCLPRORGBW")){
            result = HFColorLightPro.setHFCLPRORGBWOrder(arg0, arg1, arg2, arg3);
        }else if(arg0.split("=").length == 2 && arg0.split("=")[0].equals("HFCLPROMODE")){
            result = HFColorLightPro.setHFCLPROModeOrder(arg0, arg1, arg2, arg3);
        }else if(arg0.equals("HFSERVTIME")){
            result = Utils.sendHFServTimeOrder();
        }else if(arg0.equals("HFLINK")){
            result = Utils.sendHFServLinkOrder(arg1);
        }
        return result;
    }

    private void init()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.play_item, this, true);
        this.tv_name = ((TextView)findViewById(R.id.tv_name));
        this.c1_toggle_deng = ((ToggleButton)findViewById(R.id.c1_toggle_deng));
        this.c1_toggle_deng.setChecked(isOpen);
        this.tv_name.setText(name);
        this.c1_toggle_deng.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean)
            {
                if (paramAnonymousBoolean)
                {
                    OpenSwitch();
                    return;
                }
                CloseSwitch();
            }
        });
    }

    public void initManSocket()
    {
        new Thread() {
            public void run() {
                try {
                    mansocket = new Socket();
                    mansocket = Utils.initManOrAPSocket(3000, MANIP);
                    manreader = new BufferedReader(new InputStreamReader(
                            mansocket.getInputStream(), "utf-8"));
                    manout = mansocket.getOutputStream();
                    if (mansocket.isConnected()) {
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void initSocket()
    {
        new Thread()
        {
            public void run()
            {
                socket = new Socket();
                //外网连接与验证
                recvData = Utils.initWanSocket(3000, socket);
                if ((PlayItem.this.recvData != null) && (PlayItem.this.recvData.equals("HFSERVOK")) && (PlayItem.this.socket != null)) {
                    recvData = Utils.requestDevLinkOrder(uuid, socket);
                }
            }
        }.start();
    }
}
