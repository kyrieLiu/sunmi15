package ys.app.pad;

/**
 * 应用常量类
 */
public class Constants {
    public static final String intent_type = "intent_type";
    public static final String intent_modify = "intent_modify";
    public static final String base_url = "http://101.200.196.107:7080/";//正式服务器206453
    public static final String base_update_url = "http://119.23.145.136:7080/";//负载均衡

   //public static final String base_url = "http://101.200.196.107:7890/";//测试服务器206241
    //public static final String base_url = "http://192.168.10.74:8080/";//本地服务器

    public static final String base_sand_url = "https://pay.sandgate.cn/sandpay/api/";    //杉德正式环境
//    public static final String base_sand_url = "https://scan.sandgate.cn:20042/sandpay/api/";       // 杉德测试环境

    public static final String base_img_url = "http://101.200.196.107:7080";
    public static final String intent_name = "intent_name";
    public static final String intent_info = "intent_info";
    public static final String intent_id = "intent_id";
    public static final String intent_flag = "intent_flag";

    public static final String intent_sale = "intent_sale";
    public static final int intent_type_goods = 101;
    public static final int intent_type_service = 102;
    public static final String type_vip_info = "type_vip_info";
    public static final String bus_type_click_btn = "bus_type_click_btn";
    public static final String bus_type_http_result = "bus_type_http_result";
    public static final int type_add_good = 300;
    public static final int type_add_service = 301;
    public static final int type_addGood_success = 302;
    public static final int type_addService_success = 303;
    public static final int type_addVipCard_success = 304;
    public static final int type_updateVipCard_success = 313;
    public static final int type_deleteVipCard_success = 305;
    public static final int type_updateVip_success = 306;
    public static final String bus_type_delete_position = "bus_type_delete_position";
    public static final int type_updateAnimal_success = 307;
    public static final int type_update_commitOrder_db = 308;
    public static final String bus_db = "bus_db";
    public static final int type_updateGoods_success = 310;
    public static final int type_charge_finish = 311;
    public static final int type_order_pay_finish = 312;
    public static final String network_error_warn = "当前无可用网络,请检查";
    public static final String intent_boolean = "intent_boolean";
    public static final String bus_type_goodInfo = "bus_type_goodInfo";
    public static final String bus_type_info = "bus_type_info";
    public static final String confirm_type_info = "confirm_type_info";
    public static final String intent_orderNo = "intent_orderNo";
    public static final String intent_is_vip_pay = "intent_is_vip_pay";


    public static final String delete_select = "delete_select";
    public static final String intent_vip_name = "intent_vip_name";
    public static final String intent_vip_card_no = "intent_vip_card_no";
    public static final String intent_vip_user_id = "intent_vip_user_id";
    /**
     * 会员交易记录和会员充值记录的传值标记
     */
    public static final String intent_vip_shop_id = "intent_vip_shop_id";
    public static final String type_add_employee = "type_add_employee";
    public static final String intent_service_info = "intent_service_info";
    /**
     * 修改服务后的更新服务详情的RX标识
     */
    public static final String type_update_service = "type_update_service";

    /**
     * 服务的位置
     */
    public static final String intent_service_position = "intent_service_position";
    /**
     * 商品详情的折扣
     */
    public static final String intent_promotion = "intent_promotion";


    public static final String intent_search_type = "intent_search_type";
    public static final String intent_search_from = "intent_search_from";
    public static final int intent_search_type_vip = 315;
    public static final int intent_search_type_goods = 316;
    public static final int intent_search_type_service = 317;


    /**
     * 交易详情的intent传值标识
     */
    public static final String intent_transaction = "intent_transaction";

    public static final int intent_form_shangpin_cuxiaoliebiao = 318;
    public static final int intent_form_shangpin_cuxiaoshezhi = 319;
    public static final int intent_form_shangpin_kucunliebiao = 320;
    public static final int intent_form_shangpin_rukuliebiao = 321;
    public static final int intent_form_shangpin_chukuliebiao = 322;
    public static final int intent_form_shangpin_goumai = 330;

    public static final int intent_form_fuwu_cuxiaoliebiao = 323;
    public static final int intent_form_fuwu_cuxiaoshezhi = 324;
    public static final int intent_form_fuwu_kucunliebiao = 325;
    public static final int intent_form_fuwu_goumai = 326;

    public static final int intent_form_shangpin_ruku = 328;
    public static final int intent_form_shangpin_chuku = 329;
    public static final int is_promotion = 1;
    public static final int is_not_promotion = 0;

    /**
     * 充值成功后刷新订单详情
     */
    public static final String bus_type = "bus_type";
    public static final int bus_type_update_employee = 329;
    public static final int type_addAppointment_success=330;
    public static final int type_addAppointment_vip=331;
    public static final String barCodeTemp_int = "0000000000000000";

    public static final String type_today_achivement = "type_today_achivement";
    public static final String type_today_achivement_detail = "type_today_achivement_detail";
    public static final String type_card = "type_num_card";
    public static final int  request_code=333;
    public static final int  result_code=334;
    public static final int  scan_result_code=335;
    public static final int  permission_request_code=336;
    public static final String  scan_result="scan_result";

    public static final String infomations = "infomations";
    public static final String T1mini = "T1mini";
    public static final String TYPE = "type";  //类型
    public static final String fragment_args = "fragment_args";//fragment参数
}
