package com.example;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import java.io.IOException;


import static org.junit.jupiter.api.Assertions.assertEquals;

class AdifyServiceTest {

  class SenderSpy implements Subscription.Sender {
    String event;
    String body;

    @Override
    public void send(String event, String body) {
      this.event = event;
      this.body = body;
    }
  }

  class Stub implements ExternalService {
    public String get(String arg) throws IOException{return "{\"product\":\"Paper\"}";}
  }


  @Test
  @Tag("slow")
  void foo() {
    SenderSpy spy = new SenderSpy();
    AdifyService a = new AdifyService(new Adify(new HerokuGetRequest("adify")), "SESSION_ID,USER_ID,PRODUCT_ID", spy);
    a.execute();
    assertEquals("display", spy.event);
    System.out.println(spy.body);
    String[] values = spy.body.split(",");
    assertEquals("SESSION_ID", values[0]);
    assertEquals("advert", values[1]);
    assertEquals("PRODUCT_ID", values[2]);
    assertEquals("PRODUCT_NAME", values[3]);
  }

//component test
  @Test
  @Tag("fast")
  void component_test() {
    SenderSpy spy = new SenderSpy();
    AdifyService a = new AdifyService(new Adify(new Stub()), "SESSION_ID,USER_ID,PRODUCT_ID", spy);
    a.execute();
    assertEquals("display", spy.event);
    System.out.println(spy.body);
    String[] values = spy.body.split(",");
    assertEquals("SESSION_ID", values[0]);
    assertEquals("advert", values[1]);
    assertEquals("PRODUCT_ID", values[2]);
    assertEquals("Paper", values[3]);
  }

// integration test
  @Test
  @Tag("slow")
  void integration_test() throws Exception {
    Adify a = new Adify(new HerokuGetRequest("adify"));
    String result = a.fetch("PRODUCT_ID");
    assertEquals("Paper", result);
  }

// contract test
  @Test
  @Tag("slow")
  void contract_test() throws Exception {
    HerokuGetRequest a = new HerokuGetRequest("adify");
    String content = a.get("?product=" + "PRODUCT_ID");
    assertEquals("{\"product\":\"Paper\"}", content);
  }
}