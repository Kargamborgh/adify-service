package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@SpringBootApplication

class Main {

  public static void main(String[] args) throws Exception {
    Service.service(new Subscription[]{
        new Subscription("ad", "fetch-product-page", (body, sender) -> {
          AdifyService adify = new AdifyService(new Adify(new HerokuGetRequest("adify")), body, sender);
          adify.execute();
        })
    });
      SpringApplication.run(Main.class, args);
  }

}
