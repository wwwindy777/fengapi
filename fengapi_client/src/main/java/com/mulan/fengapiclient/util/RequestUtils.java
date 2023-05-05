package com.mulan.fengapiclient.util;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * 请求工具
 * @author wwwwind
 */
public class RequestUtils {
    /**
     * 生成签名
     * @param request 如果是get请求传入请求参数，post请求传入请求体
     * @return 加密后的字符串
     */
    public static String generateSign(String request,String secretKey) {
        //使用摘要加密
        Digester md5 = new Digester(DigestAlgorithm.MD5);
        String signContent = request + secretKey;
        return md5.digestHex(signContent);
    }
}
