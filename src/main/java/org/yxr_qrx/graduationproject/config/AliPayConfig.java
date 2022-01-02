package org.yxr_qrx.graduationproject.config;

import org.springframework.stereotype.Component;

@Component
public class AliPayConfig {
    // [沙箱环境]应用ID,您的APPID，收款账号既是你的APPID对应支付宝账号
    public static String app_id = "2021000118649672";
    // [沙箱环境]商户私钥，你的PKCS8格式RSA2私钥
    public static String merchant_private_key ="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDeljnbmQ0lLkYcSByy9F6ks3uhVIGK2cwFzYZHpN1QcoPqBmbUr+XlzTTY460X3hrr1mvK5t5b/H5kDOCkCJsrsXj33TBKawqkXqfpAJH4jT1jSxqxn8Sb8KCqYK6RK1x9PZahSnCAQWro76C981ImvFq3ca1yLk/ci04e448GI0qgFvBLHcfP4yeyZOUDmGL0qxwRxZxw6NacE9HDRmdu9cVxELhhMbHGkk1edFl2Etmr167PrguAJHJBHEqZlu01h2PIMADZAq43JHE7HecDq7+YyVeGhf4FEsrmgQrprGHK7l4Y06SwsB5sk622NBQ7FadElfrNhVmW58UZC3LzAgMBAAECggEALn1B6FO3IyTfD9kf5WCUw+GY2MFFrTKAIlYizaPpdXv1gVOE9rcmVTDe8M3cwTdqeAd4zBAcMSozW8I0yQ3jGuUeVuajx2dD+cCrYUb1NeyJ/csZ8C9maQuBAxTeYQOldaL/awCZLrB7G9uwvvurFNwSNm5Pw6FYnL+buWfd33Hr1M9v2/4Zt+Hen63CEVVNMeaAZhVJrLefxdzxX4smBgPhvyG+5v2eUdv4BNsxO8ktNAaLYg7kEUhxEIIj3VU5i+xBtLCxmM0iy2sz/Pcx3giMofOgEPf/dQgLijs1DZdo0LaWS7ZbTWAVoHclaxIjoodKZUcG0zvzlgiurp2ngQKBgQDwCrJtWWKdH8qTuV8btc1wt9tOCBxEf/KBQpui6HHKGF3yTW+PxqyexOXEj/npEi+ptd6Md2TKpiZWlmtRksPCnT7INSU4q/THcz1NqrtXw+kz+S1Ejwlqk1n0pQDw+A/fn0BmP7Qp+2qEYrAk1VMH+w1p86sKWZ+wyZbFgEVkOwKBgQDtYnXuAhPvLULMgAwv9dPzcw4A+PKDJNsrDdK3rF0dF5E+9XsxHkAmUTqjbGrDv2jt4Y4EaNn4lO7z2ZyxOAeN69AO6e0wbuktYCTLIqdoGbYTuyKLeYFbF89E7qJgsvyYKZiXTNeHzgukgHLCvW1qbGomCh7uKogE7k14DdhYqQKBgQCFnW60b4z06T1Z/VVzlz9D/xOT2+/gMVhLm0gG5lxwoh+pQHFZFr3/oppDT+FMW1MspI+8oLj6FIJCsHgMcCWHrv/DcdHGOfGHFB6LnP2rXOHyjEEmaHhVWrA3/aEIgQxU7dBrGgqvU8N27XgfCJ+vUY+l6nzKxhidz5idV0lJAQKBgQCEYdhqdvYu+Aq8iNxDEtC0f4FbwXlc8XzYMclFbPYwdygk+DlPVxNnSv1sjdWsfF5D6vmMM/z6x/PKQZ1Ep35vAwbwijwgOnoHOArMzfKV92C2+DF3nrco2cpZIsujKa38HZZfKhc4QQv1HTzipUkThIpd1F8TatyYA11oijAQoQKBgAfTWkg/8llrNTxvtBme13prrBAWSzsNgUJtXXG4Jo0Ro4NcPvm2VwmJgOzomxTqMiwnMBPYSgW2HhN7uW5QRwnhFbT96PcpYTId2aADISAYbEIZOikHjMFsV9/WS98578+pRvRvubqjrzNP814SfHy6ujRmgiYB55zgl+fSD+aN";
    // [沙箱环境]支付宝公钥
    public static String alipay_public_key ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlvx0S05Vg9GTF4FZ54hbjj8BWg0WbLylQL3tcKTT+6vZ748VI7o+MWvpGa03iU7MWsCaSfCwJZORdxY1ANUBvUv5fzD/+YD8INva8OrBkBzcNMkwPOt4fCLMoEmBkdbNHvPoOkDsqbG0WlAx131oEJfvArvXnGh3jPwgVDHIjBRdVNuT/MltIVkUpMy4tzq/+UFo4si26D9dGup5FyyXGOAmeazHCHzLRb5BkxEnp90wLzKOIStgwo7+JiE0G3exyoCB2+gfQsrtU+1HfSbI++hwZdw8AvQ0+j551I2b5G90UG9yVKVdrmKYV1l+5uiYinGOMF0KDGV3d0yEQJuOEwIDAQAB";
    // [沙箱环境]服务器异步通知页面路径
    public static String notify_url="http://ngrok.sscai.club/alipay/aliPayNotify_url";

    // [沙箱环境]页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url ="http://localhost:8080/order/ReturnPay";
    // [沙箱环境]
    public static String gatewayUrl ="https://openapi.alipaydev.com/gateway.do";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";
}
