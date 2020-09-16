
package bbangshop.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

//@FeignClient(name="bread", url="http://bread:8080")
@FeignClient(name="Bread", url="${api.url.bread}")
public interface BreadService {

    @RequestMapping(method= RequestMethod.GET, path="/breads/check",produces = "application/json")
    //public @ResponseBody String checkBreadQuantity(@RequestBody Bread bread);
    @ResponseBody String checkBreadQuantity(@RequestParam("breadId") String breadId);

}