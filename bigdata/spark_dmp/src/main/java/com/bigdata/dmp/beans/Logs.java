package com.bigdata.dmp.beans;


import com.bigdata.dmp.utils.Utils;
import com.google.inject.internal.util.$ToStringBuilder;
import org.apache.commons.lang.StringUtils;

public class Logs {


    private String sessionid; //String, //会话标识
    private Integer advertisersid; //Int, //广告主id
    private Integer adorderid; //Int, //广告id
    private Integer adcreativeid; //Int, //广告创意id   ( >= 200000  dsp ,  < 200000 oss)
    private Integer adplatformproviderid; //Int, //广告平台商id  (>= 100000 rtb  , < 100000  api )
    private String sdkversionnumber; //String, //sdk版本号
    private String adplatformkey; //String, //平台商key
    private Integer putinmodeltype; //Int, //针对广告主的投放模式,1：展示量投放 2：点击量投放
    private Integer requestmode; //Int, //数据请求方式（1请求、2展示、3点击）
    private Double adprice; //Double, //广告价格
    private Double adppprice; //Double, //平台商价格
    private String requestdate; //String, //请求时间,格式为：yyyy-m-dd hhmmss
    private String ip; //String, //设备用户的真实ip地址
    private String appid; //String, //应用id
    private String appname; //String, //应用名称
    private String uuid; //String, //设备唯一标识，比如imei或者androidid等
    private String device; //String, //设备型号，如htc、iphone
    private Integer client; //Int, //设备类型 （1：android 2：ios 3：wp）
    private String osversion; //String, //设备操作系统版本，如4.0
    private String density; //String, //备屏幕的密度 android的取值为0.75、1、1.5,ios的取值为：1、2
    private Integer pw; //Int, //设备屏幕宽度
    private Integer ph; //Int, //设备屏幕高度
    private String longitude; //String, //设备所在经度
    private String lat; //String, //设备所在纬度
    private String provincename; //String, //设备所在省份名称
    private String cityname; //String, //设备所在城市名称
    private Integer ispid; //Int, //运营商id
    private String ispname; //String, //运营商名称
    private Integer networkmannerid; //Int, //联网方式id
    private String networkmannername; //String, //联网方式名称
    private Integer iseffective; //Int, //有效标识（有效指可以正常计费的）(0：无效 1：有效)
    private Integer isbilling; //Int, //是否收费（0：未收费 1：已收费）
    private Integer adspacetype; //Int, //广告位类型（1：banner 2：插屏 3：全屏）
    private String adspacetypename; //String, //广告位类型名称（banner、插屏、全屏）
    private Integer devicetype; //Int, //设备类型（1：手机 2：平板）
    private Integer processnode; //Int, //流程节点（1：请求量kpi 2：有效请求 3：广告请求）
    private Integer apptype; //Int, //应用类型id
    private String district; //String, //设备所在县名称
    private Integer paymode; //Int, //针对平台商的支付模式，1：展示量投放(CPM) 2：点击量投放(CPC)
    private Integer isbid; //Int, //是否rtb
    private Double bidprice; //Double, //rtb竞价价格
    private Double winprice; //Double, //rtb竞价成功价格
    private Integer iswin; //Int, //是否竞价成功
    private String cur; //String, //uesusd|rmb等
    private Double rate; //Double, //汇率
    private Double cnywinprice; //Double, //rtb竞价成功转换成人民币的价格
    private String imei; //String, //imei
    private String mac; //String, //mac
    private String idfa; //String, //idfa
    private String openudid; //String, //openudid
    private String androidid; //String, //androidid
    private String rtbprovince; //String, //rtb 省
    private String rtbcity; //String, //rtb 市
    private String rtbdistrict; //String, //rtb 区
    private String rtbstreet; //String, //rtb 街道
    private String storeurl; //String, //app的市场下载地址
    private String realip; //String, //真实ip
    private Integer isqualityapp; //Int, //优选标识
    private Double bidfloor; //Double, //底价
    private Integer aw; //Int, //广告位的宽
    private Integer ah; //Int, //广告位的高
    private String imeimd5; //String, //imei_md5
    private String macmd5; //String, //mac_md5
    private String idfamd5; //String, //idfa_md5
    private String openudidmd5; //String, //openudid_md5
    private String androididmd5; //String, //androidid_md5
    private String imeisha1; //String, //imei_sha1
    private String macsha1; //String, //mac_sha1
    private String idfasha1; //String, //idfa_sha1
    private String openudidsha1; //String, //openudid_sha1
    private String androididsha1; //String, //androidid_sha1
    private String uuidunknow; //String, //uuid_unknow tanx密文
    private String decuuidunknow; //String, // 解密的tanx 明文
    private String userid; //String, //平台用户id
    private String reqdate; //String, //日期
    private String reqhour; //String, //小时
    private Integer iptype; //Int, //表示ip库类型，1为点媒ip库，2为广告协会的ip地理信息标准库，默认为1
    private Double initbidprice; //Double, //初始出价
    private Double adpayment; //Double, //转换后的广告消费（保留小数点后6位）
    private Double agentrate; //Double, //代理商利润率
    private Double lomarkrate; //Double, //代理利润率
    private Double adxrate; //Double, //媒介利润率
    private String title; //String, //标题
    private String keywords; //String, //关键字
    private String tagid; //String, //广告位标识(当视频流量时值为视频ID号)
    private String callbackdate; //String, //回调时间 格式为YYYY/mm/dd hhmmss
    private String channelid; //String, //频道ID
    private Integer mediatype; //Int


    public Logs  line2Log (String line){
        if (StringUtils.isNotEmpty(line)){
            String[] fields = line.split(",");
            Logs logs = new Logs(fields[0], Integer.parseInt(fields[1]), Integer.parseInt(fields[2]), Integer.parseInt(fields[3]), Integer.parseInt(fields[4]), fields[5], fields[6], Integer.parseInt(fields[7]), Integer.parseInt(fields[8]), Double.parseDouble(fields[9]), Double.parseDouble(fields[10]),
                    fields[11], fields[12], fields[13], fields[14], fields[15], fields[16], Integer.parseInt(fields[17]), fields[18], fields[19], Integer.parseInt(fields[20]),
                    Integer.parseInt(fields[21]), fields[22], fields[23], fields[24], fields[25],Integer.parseInt(fields[26]), fields[27], Integer.parseInt(fields[28]), fields[29], Integer.parseInt(fields[30]),
                    Integer.parseInt(fields[31]), Integer.parseInt(fields[32]), fields[33], Integer.parseInt(fields[34]), Integer.parseInt(fields[35]), Integer.parseInt(fields[36]), fields[37], Integer.parseInt(fields[38]), Integer.parseInt(fields[39]), Double.parseDouble(fields[40]),
                    Double.parseDouble(fields[41]), Integer.parseInt(fields[42]), fields[43], Double.parseDouble(fields[44]), Double.parseDouble(fields[45]), fields[46], fields[47], fields[48], fields[49], fields[50],
                    fields[51], fields[52], fields[53], fields[54], fields[55], fields[56], Integer.parseInt(fields[57]), Double.parseDouble(fields[58]), Integer.parseInt(fields[59]), Integer.parseInt(fields[60]),
                    fields[61], fields[62], fields[63], fields[64], fields[65], fields[66], fields[67], fields[68], fields[69], fields[70],
                    fields[71], "", fields[72],
                    Utils.fmtDate(fields[11]),Utils.fmtTime(fields[11]),
                    Integer.parseInt(fields[73]), Double.parseDouble(fields[74]), Double.parseDouble(fields[75]), Double.parseDouble(fields[76]), Double.parseDouble(fields[77]), Double.parseDouble(fields[78]), "", "", "", "", "", 1);
            return logs;
        }
        else{
            Logs logs = new Logs("", 0, 0, 0, 0, "", "", 0, 0, 0.0, 0.0, "", "", "", "", "", "", 0, "",
                    "", 0, 0, "", "", "", "", 0, "", 0, "", 0, 0, 0, "", 0, 0, 0, "", 0, 0,
                    0.0, 0.0, 0, "", 0.0, 0.0, "", "", "", "", "", "", "", "", "", "", "", 0, 0.0, 0, 0,
                    "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 0, 0.0, 0.0, 0.0, 0.0, 0.0, "", "", "", "", "", 0);
            return logs;
        }

    }

    public Logs(String sessionid, Integer advertisersid, Integer adorderid, Integer adcreativeid, Integer adplatformproviderid, String sdkversionnumber, String adplatformkey, Integer putinmodeltype, Integer requestmode, Double adprice, Double adppprice, String requestdate, String ip, String appid, String appname, String uuid, String device, Integer client, String osversion, String density, Integer pw, Integer ph, String longitude, String lat, String provincename, String cityname, Integer ispid, String ispname, Integer networkmannerid, String networkmannername, Integer iseffective, Integer isbilling, Integer adspacetype, String adspacetypename, Integer devicetype, Integer processnode, Integer apptype, String district, Integer paymode, Integer isbid, Double bidprice, Double winprice, Integer iswin, String cur, Double rate, Double cnywinprice, String imei, String mac, String idfa, String openudid, String androidid, String rtbprovince, String rtbcity, String rtbdistrict, String rtbstreet, String storeurl, String realip, Integer isqualityapp, Double bidfloor, Integer aw, Integer ah, String imeimd5, String macmd5, String idfamd5, String openudidmd5, String androididmd5, String imeisha1, String macsha1, String idfasha1, String openudidsha1, String androididsha1, String uuidunknow, String decuuidunknow, String userid, String reqdate, String reqhour, Integer iptype, Double initbidprice, Double adpayment, Double agentrate, Double lomarkrate, Double adxrate, String title, String keywords, String tagid, String callbackdate, String channelid, Integer mediatype) {
        this.sessionid = sessionid;
        this.advertisersid = advertisersid;
        this.adorderid = adorderid;
        this.adcreativeid = adcreativeid;
        this.adplatformproviderid = adplatformproviderid;
        this.sdkversionnumber = sdkversionnumber;
        this.adplatformkey = adplatformkey;
        this.putinmodeltype = putinmodeltype;
        this.requestmode = requestmode;
        this.adprice = adprice;
        this.adppprice = adppprice;
        this.requestdate = requestdate;
        this.ip = ip;
        this.appid = appid;
        this.appname = appname;
        this.uuid = uuid;
        this.device = device;
        this.client = client;
        this.osversion = osversion;
        this.density = density;
        this.pw = pw;
        this.ph = ph;
        this.longitude = longitude;
        this.lat = lat;
        this.provincename = provincename;
        this.cityname = cityname;
        this.ispid = ispid;
        this.ispname = ispname;
        this.networkmannerid = networkmannerid;
        this.networkmannername = networkmannername;
        this.iseffective = iseffective;
        this.isbilling = isbilling;
        this.adspacetype = adspacetype;
        this.adspacetypename = adspacetypename;
        this.devicetype = devicetype;
        this.processnode = processnode;
        this.apptype = apptype;
        this.district = district;
        this.paymode = paymode;
        this.isbid = isbid;
        this.bidprice = bidprice;
        this.winprice = winprice;
        this.iswin = iswin;
        this.cur = cur;
        this.rate = rate;
        this.cnywinprice = cnywinprice;
        this.imei = imei;
        this.mac = mac;
        this.idfa = idfa;
        this.openudid = openudid;
        this.androidid = androidid;
        this.rtbprovince = rtbprovince;
        this.rtbcity = rtbcity;
        this.rtbdistrict = rtbdistrict;
        this.rtbstreet = rtbstreet;
        this.storeurl = storeurl;
        this.realip = realip;
        this.isqualityapp = isqualityapp;
        this.bidfloor = bidfloor;
        this.aw = aw;
        this.ah = ah;
        this.imeimd5 = imeimd5;
        this.macmd5 = macmd5;
        this.idfamd5 = idfamd5;
        this.openudidmd5 = openudidmd5;
        this.androididmd5 = androididmd5;
        this.imeisha1 = imeisha1;
        this.macsha1 = macsha1;
        this.idfasha1 = idfasha1;
        this.openudidsha1 = openudidsha1;
        this.androididsha1 = androididsha1;
        this.uuidunknow = uuidunknow;
        this.decuuidunknow = decuuidunknow;
        this.userid = userid;
        this.reqdate = reqdate;
        this.reqhour = reqhour;
        this.iptype = iptype;
        this.initbidprice = initbidprice;
        this.adpayment = adpayment;
        this.agentrate = agentrate;
        this.lomarkrate = lomarkrate;
        this.adxrate = adxrate;
        this.title = title;
        this.keywords = keywords;
        this.tagid = tagid;
        this.callbackdate = callbackdate;
        this.channelid = channelid;
        this.mediatype = mediatype;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public Integer getAdvertisersid() {
        return advertisersid;
    }

    public void setAdvertisersid(Integer advertisersid) {
        this.advertisersid = advertisersid;
    }

    public Integer getAdorderid() {
        return adorderid;
    }

    public void setAdorderid(Integer adorderid) {
        this.adorderid = adorderid;
    }

    public Integer getAdcreativeid() {
        return adcreativeid;
    }

    public void setAdcreativeid(Integer adcreativeid) {
        this.adcreativeid = adcreativeid;
    }

    public Integer getAdplatformproviderid() {
        return adplatformproviderid;
    }

    public void setAdplatformproviderid(Integer adplatformproviderid) {
        this.adplatformproviderid = adplatformproviderid;
    }

    public String getSdkversionnumber() {
        return sdkversionnumber;
    }

    public void setSdkversionnumber(String sdkversionnumber) {
        this.sdkversionnumber = sdkversionnumber;
    }

    public String getAdplatformkey() {
        return adplatformkey;
    }

    public void setAdplatformkey(String adplatformkey) {
        this.adplatformkey = adplatformkey;
    }

    public Integer getPutinmodeltype() {
        return putinmodeltype;
    }

    public void setPutinmodeltype(Integer putinmodeltype) {
        this.putinmodeltype = putinmodeltype;
    }

    public Integer getRequestmode() {
        return requestmode;
    }

    public void setRequestmode(Integer requestmode) {
        this.requestmode = requestmode;
    }

    public Double getAdprice() {
        return adprice;
    }

    public void setAdprice(Double adprice) {
        this.adprice = adprice;
    }

    public Double getAdppprice() {
        return adppprice;
    }

    public void setAdppprice(Double adppprice) {
        this.adppprice = adppprice;
    }

    public String getRequestdate() {
        return requestdate;
    }

    public void setRequestdate(String requestdate) {
        this.requestdate = requestdate;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Integer getClient() {
        return client;
    }

    public void setClient(Integer client) {
        this.client = client;
    }

    public String getOsversion() {
        return osversion;
    }

    public void setOsversion(String osversion) {
        this.osversion = osversion;
    }

    public String getDensity() {
        return density;
    }

    public void setDensity(String density) {
        this.density = density;
    }

    public Integer getPw() {
        return pw;
    }

    public void setPw(Integer pw) {
        this.pw = pw;
    }

    public Integer getPh() {
        return ph;
    }

    public void setPh(Integer ph) {
        this.ph = ph;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getProvincename() {
        return provincename;
    }

    public void setProvincename(String provincename) {
        this.provincename = provincename;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public Integer getIspid() {
        return ispid;
    }

    public void setIspid(Integer ispid) {
        this.ispid = ispid;
    }

    public String getIspname() {
        return ispname;
    }

    public void setIspname(String ispname) {
        this.ispname = ispname;
    }

    public Integer getNetworkmannerid() {
        return networkmannerid;
    }

    public void setNetworkmannerid(Integer networkmannerid) {
        this.networkmannerid = networkmannerid;
    }

    public String getNetworkmannername() {
        return networkmannername;
    }

    public void setNetworkmannername(String networkmannername) {
        this.networkmannername = networkmannername;
    }

    public Integer getIseffective() {
        return iseffective;
    }

    public void setIseffective(Integer iseffective) {
        this.iseffective = iseffective;
    }

    public Integer getIsbilling() {
        return isbilling;
    }

    public void setIsbilling(Integer isbilling) {
        this.isbilling = isbilling;
    }

    public Integer getAdspacetype() {
        return adspacetype;
    }

    public void setAdspacetype(Integer adspacetype) {
        this.adspacetype = adspacetype;
    }

    public String getAdspacetypename() {
        return adspacetypename;
    }

    public void setAdspacetypename(String adspacetypename) {
        this.adspacetypename = adspacetypename;
    }

    public Integer getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(Integer devicetype) {
        this.devicetype = devicetype;
    }

    public Integer getProcessnode() {
        return processnode;
    }

    public void setProcessnode(Integer processnode) {
        this.processnode = processnode;
    }

    public Integer getApptype() {
        return apptype;
    }

    public void setApptype(Integer apptype) {
        this.apptype = apptype;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Integer getPaymode() {
        return paymode;
    }

    public void setPaymode(Integer paymode) {
        this.paymode = paymode;
    }

    public Integer getIsbid() {
        return isbid;
    }

    public void setIsbid(Integer isbid) {
        this.isbid = isbid;
    }

    public Double getBidprice() {
        return bidprice;
    }

    public void setBidprice(Double bidprice) {
        this.bidprice = bidprice;
    }

    public Double getWinprice() {
        return winprice;
    }

    public void setWinprice(Double winprice) {
        this.winprice = winprice;
    }

    public Integer getIswin() {
        return iswin;
    }

    public void setIswin(Integer iswin) {
        this.iswin = iswin;
    }

    public String getCur() {
        return cur;
    }

    public void setCur(String cur) {
        this.cur = cur;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getCnywinprice() {
        return cnywinprice;
    }

    public void setCnywinprice(Double cnywinprice) {
        this.cnywinprice = cnywinprice;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIdfa() {
        return idfa;
    }

    public void setIdfa(String idfa) {
        this.idfa = idfa;
    }

    public String getOpenudid() {
        return openudid;
    }

    public void setOpenudid(String openudid) {
        this.openudid = openudid;
    }

    public String getAndroidid() {
        return androidid;
    }

    public void setAndroidid(String androidid) {
        this.androidid = androidid;
    }

    public String getRtbprovince() {
        return rtbprovince;
    }

    public void setRtbprovince(String rtbprovince) {
        this.rtbprovince = rtbprovince;
    }

    public String getRtbcity() {
        return rtbcity;
    }

    public void setRtbcity(String rtbcity) {
        this.rtbcity = rtbcity;
    }

    public String getRtbdistrict() {
        return rtbdistrict;
    }

    public void setRtbdistrict(String rtbdistrict) {
        this.rtbdistrict = rtbdistrict;
    }

    public String getRtbstreet() {
        return rtbstreet;
    }

    public void setRtbstreet(String rtbstreet) {
        this.rtbstreet = rtbstreet;
    }

    public String getStoreurl() {
        return storeurl;
    }

    public void setStoreurl(String storeurl) {
        this.storeurl = storeurl;
    }

    public String getRealip() {
        return realip;
    }

    public void setRealip(String realip) {
        this.realip = realip;
    }

    public Integer getIsqualityapp() {
        return isqualityapp;
    }

    public void setIsqualityapp(Integer isqualityapp) {
        this.isqualityapp = isqualityapp;
    }

    public Double getBidfloor() {
        return bidfloor;
    }

    public void setBidfloor(Double bidfloor) {
        this.bidfloor = bidfloor;
    }

    public Integer getAw() {
        return aw;
    }

    public void setAw(Integer aw) {
        this.aw = aw;
    }

    public Integer getAh() {
        return ah;
    }

    public void setAh(Integer ah) {
        this.ah = ah;
    }

    public String getImeimd5() {
        return imeimd5;
    }

    public void setImeimd5(String imeimd5) {
        this.imeimd5 = imeimd5;
    }

    public String getMacmd5() {
        return macmd5;
    }

    public void setMacmd5(String macmd5) {
        this.macmd5 = macmd5;
    }

    public String getIdfamd5() {
        return idfamd5;
    }

    public void setIdfamd5(String idfamd5) {
        this.idfamd5 = idfamd5;
    }

    public String getOpenudidmd5() {
        return openudidmd5;
    }

    public void setOpenudidmd5(String openudidmd5) {
        this.openudidmd5 = openudidmd5;
    }

    public String getAndroididmd5() {
        return androididmd5;
    }

    public void setAndroididmd5(String androididmd5) {
        this.androididmd5 = androididmd5;
    }

    public String getImeisha1() {
        return imeisha1;
    }

    public void setImeisha1(String imeisha1) {
        this.imeisha1 = imeisha1;
    }

    public String getMacsha1() {
        return macsha1;
    }

    public void setMacsha1(String macsha1) {
        this.macsha1 = macsha1;
    }

    public String getIdfasha1() {
        return idfasha1;
    }

    public void setIdfasha1(String idfasha1) {
        this.idfasha1 = idfasha1;
    }

    public String getOpenudidsha1() {
        return openudidsha1;
    }

    public void setOpenudidsha1(String openudidsha1) {
        this.openudidsha1 = openudidsha1;
    }

    public String getAndroididsha1() {
        return androididsha1;
    }

    public void setAndroididsha1(String androididsha1) {
        this.androididsha1 = androididsha1;
    }

    public String getUuidunknow() {
        return uuidunknow;
    }

    public void setUuidunknow(String uuidunknow) {
        this.uuidunknow = uuidunknow;
    }

    public String getDecuuidunknow() {
        return decuuidunknow;
    }

    public void setDecuuidunknow(String decuuidunknow) {
        this.decuuidunknow = decuuidunknow;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getReqdate() {
        return reqdate;
    }

    public void setReqdate(String reqdate) {
        this.reqdate = reqdate;
    }

    public String getReqhour() {
        return reqhour;
    }

    public void setReqhour(String reqhour) {
        this.reqhour = reqhour;
    }

    public Integer getIptype() {
        return iptype;
    }

    public void setIptype(Integer iptype) {
        this.iptype = iptype;
    }

    public Double getInitbidprice() {
        return initbidprice;
    }

    public void setInitbidprice(Double initbidprice) {
        this.initbidprice = initbidprice;
    }

    public Double getAdpayment() {
        return adpayment;
    }

    public void setAdpayment(Double adpayment) {
        this.adpayment = adpayment;
    }

    public Double getAgentrate() {
        return agentrate;
    }

    public void setAgentrate(Double agentrate) {
        this.agentrate = agentrate;
    }

    public Double getLomarkrate() {
        return lomarkrate;
    }

    public void setLomarkrate(Double lomarkrate) {
        this.lomarkrate = lomarkrate;
    }

    public Double getAdxrate() {
        return adxrate;
    }

    public void setAdxrate(Double adxrate) {
        this.adxrate = adxrate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getTagid() {
        return tagid;
    }

    public void setTagid(String tagid) {
        this.tagid = tagid;
    }

    public String getCallbackdate() {
        return callbackdate;
    }

    public void setCallbackdate(String callbackdate) {
        this.callbackdate = callbackdate;
    }

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }

    public Integer getMediatype() {
        return mediatype;
    }

    public void setMediatype(Integer mediatype) {
        this.mediatype = mediatype;
    }

    @Override
    public String toString() {
        return "Logs{" +
                "sessionid='" + sessionid + '\'' +
                ", advertisersid=" + advertisersid +
                ", adorderid=" + adorderid +
                ", adcreativeid=" + adcreativeid +
                ", adplatformproviderid=" + adplatformproviderid +
                ", sdkversionnumber='" + sdkversionnumber + '\'' +
                ", adplatformkey='" + adplatformkey + '\'' +
                ", putinmodeltype=" + putinmodeltype +
                ", requestmode=" + requestmode +
                ", adprice=" + adprice +
                ", adppprice=" + adppprice +
                ", requestdate='" + requestdate + '\'' +
                ", ip='" + ip + '\'' +
                ", appid='" + appid + '\'' +
                ", appname='" + appname + '\'' +
                ", uuid='" + uuid + '\'' +
                ", device='" + device + '\'' +
                ", client=" + client +
                ", osversion='" + osversion + '\'' +
                ", density='" + density + '\'' +
                ", pw=" + pw +
                ", ph=" + ph +
                ", longitude='" + longitude + '\'' +
                ", lat='" + lat + '\'' +
                ", provincename='" + provincename + '\'' +
                ", cityname='" + cityname + '\'' +
                ", ispid=" + ispid +
                ", ispname='" + ispname + '\'' +
                ", networkmannerid=" + networkmannerid +
                ", networkmannername='" + networkmannername + '\'' +
                ", iseffective=" + iseffective +
                ", isbilling=" + isbilling +
                ", adspacetype=" + adspacetype +
                ", adspacetypename='" + adspacetypename + '\'' +
                ", devicetype=" + devicetype +
                ", processnode=" + processnode +
                ", apptype=" + apptype +
                ", district='" + district + '\'' +
                ", paymode=" + paymode +
                ", isbid=" + isbid +
                ", bidprice=" + bidprice +
                ", winprice=" + winprice +
                ", iswin=" + iswin +
                ", cur='" + cur + '\'' +
                ", rate=" + rate +
                ", cnywinprice=" + cnywinprice +
                ", imei='" + imei + '\'' +
                ", mac='" + mac + '\'' +
                ", idfa='" + idfa + '\'' +
                ", openudid='" + openudid + '\'' +
                ", androidid='" + androidid + '\'' +
                ", rtbprovince='" + rtbprovince + '\'' +
                ", rtbcity='" + rtbcity + '\'' +
                ", rtbdistrict='" + rtbdistrict + '\'' +
                ", rtbstreet='" + rtbstreet + '\'' +
                ", storeurl='" + storeurl + '\'' +
                ", realip='" + realip + '\'' +
                ", isqualityapp=" + isqualityapp +
                ", bidfloor=" + bidfloor +
                ", aw=" + aw +
                ", ah=" + ah +
                ", imeimd5='" + imeimd5 + '\'' +
                ", macmd5='" + macmd5 + '\'' +
                ", idfamd5='" + idfamd5 + '\'' +
                ", openudidmd5='" + openudidmd5 + '\'' +
                ", androididmd5='" + androididmd5 + '\'' +
                ", imeisha1='" + imeisha1 + '\'' +
                ", macsha1='" + macsha1 + '\'' +
                ", idfasha1='" + idfasha1 + '\'' +
                ", openudidsha1='" + openudidsha1 + '\'' +
                ", androididsha1='" + androididsha1 + '\'' +
                ", uuidunknow='" + uuidunknow + '\'' +
                ", decuuidunknow='" + decuuidunknow + '\'' +
                ", userid='" + userid + '\'' +
                ", reqdate='" + reqdate + '\'' +
                ", reqhour='" + reqhour + '\'' +
                ", iptype=" + iptype +
                ", initbidprice=" + initbidprice +
                ", adpayment=" + adpayment +
                ", agentrate=" + agentrate +
                ", lomarkrate=" + lomarkrate +
                ", adxrate=" + adxrate +
                ", title='" + title + '\'' +
                ", keywords='" + keywords + '\'' +
                ", tagid='" + tagid + '\'' +
                ", callbackdate='" + callbackdate + '\'' +
                ", channelid='" + channelid + '\'' +
                ", mediatype=" + mediatype +
                '}';
    }
}
