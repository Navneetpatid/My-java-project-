package com.janaushadhi.adminservice.externalservices;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "JANAUSHADHI-NOTIFICATION-SERVICE", url = "10.192.92.45:7003/notification") //url = "151.106.39.5:7003/notification  //NOSONAR
public interface NotificationService {

   
    @GetMapping("/sendEmail")
    public ResponseEntity<?> sendOtpNew(@RequestParam String to,@RequestParam String subject, @RequestParam String message);
    
//    @PostMapping("/sendMessage")
//    public ResponseEntity<?> sendMessage(@RequestBody OtpMessageRequest request);
}
