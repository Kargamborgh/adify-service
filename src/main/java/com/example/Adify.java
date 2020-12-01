package com.example;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.optimizely.ab.Optimizely;
import com.optimizely.ab.OptimizelyFactory;

class Adify {
  private final ExternalService service;

  Adify(ExternalService service) {
    this.service = service;
  }

  String fetch(String productId) {
    String sdkKey = "WYEjq8y2NG46HFWxmyMjJ";
    Optimizely optimizelyClient = OptimizelyFactory.newDefaultInstance(sdkKey);
    Boolean enabled = optimizelyClient.isFeatureEnabled("Product-name", Math.random()+"");
    try {
      String content = service.get("?product=" + productId);
      JSONObject obj = (JSONObject) new JSONParser().parse(content);
      if (enabled) {
        return (String) obj.get("product-name");
      } else {
        return (String) obj.get("product");
      }
    } catch (Exception e) {
      return "";
    }
  }
}
