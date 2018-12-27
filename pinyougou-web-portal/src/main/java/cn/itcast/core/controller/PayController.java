package cn.itcast.core.controller;

import cn.itcast.core.service.PayService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 支付管理
 */
@RestController
@RequestMapping("/pay")
public class PayController {

    @Reference
    private PayService payService;

    //生成二维码
    @RequestMapping("/createNative")
    public Map<String, String> createNative() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return payService.createNative(name);
    }

    //根据支付订单ID 查询支付状态
    @RequestMapping("/queryPayStatus")
    public Result queryPayStatus(String out_trade_no) {
        try {
            int x = 0;
            while (true) {
                Map<String, String> map = payService.queryPayStatus(out_trade_no);
                System.out.println("支付状态:" + map.get("trade_state"));
                if ("SUCCESS".equals(map.get("trade_state"))) {
                    return new Result(true,"支付成功");
                }else{
                    Thread.sleep(3000);
                    x++;
                    if(x > 100){
                        //调用微信服务器端 (同学完成了)
                        return new Result(false,"二维码超时");
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new Result(false,"查询失败");
        }


//        //支付状态
//        SUCCESS—支付成功
//
//        REFUND—转入退款
//
//        NOTPAY—未支付
//
//        CLOSED—已关闭
//
//        REVOKED—已撤销（付款码支付）
//
//        USERPAYING--用户支付中（付款码支付）
//
//        PAYERROR--支付失败(其他原因，如银行返回失败)

    }
}
