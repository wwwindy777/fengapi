package com.mulan.fengapiclient.client;

import java.util.Map;

/**
 * @author wwwwind
 */
public interface ApiClient {
    String getApiRequest(String interfaceName,Map<String, Object> params);
    String postApiRequest(String interfaceName,Map<String, Object> body);

}
