package com.github.paulosalonso.research.adapter.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = "notifierClient", url = "${notifier.url}")
public interface NotifierClient {

    @PostMapping("/notifications")
    void notify(@RequestBody @Valid NotificationDTO notificationDTO);
}
