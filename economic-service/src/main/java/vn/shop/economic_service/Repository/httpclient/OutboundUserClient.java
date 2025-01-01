package vn.shop.economic_service.Repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.shop.economic_service.dto.response.OutboundUserInfoResponse;


@FeignClient(name = "outbound-user", url = "https://www.googleapis.com")
public interface OutboundUserClient {
    @GetMapping(value = "/oauth2/v1/userinfo")
    OutboundUserInfoResponse getUserInfo(@RequestParam("alt") String alt,
                                     @RequestParam("access_token")String accessToken);

}
