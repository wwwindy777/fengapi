package com.mulan.fengapiclient.client;

import java.util.Map;

/**
 * @author wwwwind
 */
public interface ApiClient {
    String getApiRequest(String url,String interfaceId,Map<String, Object> params);
    String postApiRequest(String url,String interfaceId,Map<String, Object> body);

}
