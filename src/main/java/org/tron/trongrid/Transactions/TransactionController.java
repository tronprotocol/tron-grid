package org.tron.trongrid.Transactions;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

@RestController
@Component
@PropertySource("classpath:tronscan.properties")
public class TransactionController {
    @Value("${url.transaction}")
    private String url;

    @RequestMapping(method = RequestMethod.GET, value = "/totaltransactions")
    public Long totaltransaction() {

        JSONObject result = this.getResponse(this.url);
        return result.getLong("total");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/transactions")
    public JSONArray getTranssactions(
        /******************* Page Parameters ****************************************************/
        @RequestParam(value="limit", required=false, defaultValue = "40" ) int limit,
        @RequestParam(value="count", required=false, defaultValue = "true" ) boolean count,
        @RequestParam(value="sort", required=false, defaultValue = "-timestamp") String sort,
        @RequestParam(value="start", required=false, defaultValue = "0") Long start,
        @RequestParam(value="total", required=false, defaultValue = "0") Long total,
        /****************** Filter parameters *****************************************************/
        @RequestParam(value="block", required=false, defaultValue = "-1") long block

    ){

        String url = String.format("%s?limit=%d&sort=%s&count=%b&start=%d&total=%d",
                this.url,limit,sort,count,start,total);

        if(block > 0)
            url = String.format("%s&block=%d", url, block);

        JSONObject result = this.getResponse(url);
        return result.getJSONArray("data");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/transactions/{hash}")
    public JSONObject getTransactionbyHash(
            @PathVariable String hash
    ){
        String url = String.format("%s/%s",this.url,hash);
        JSONObject result = this.getResponse(url);
        return result;
    }

    private JSONObject getResponse(String url){
        System.out.println(url);
        RestTemplate restTemplate = new RestTemplate();
        return JSON.parseObject(restTemplate.getForObject(url, String.class));
    }

}
