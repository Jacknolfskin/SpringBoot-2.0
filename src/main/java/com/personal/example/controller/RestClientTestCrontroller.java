package com.personal.example.controller;

import com.personal.example.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.List;
/**
 * 模拟调用订单接口
 * @author hufei
 *
 */
@Controller
@RequestMapping("/test") 
public class RestClientTestCrontroller {

	@Value(value = "${api.order}")
	String base ;

	@Autowired
	RestTemplateBuilder restTemplateBuilder;
	
	@GetMapping("/get/{orderId}")
	public @ResponseBody Order testGetOrder(@PathVariable String orderId) throws Exception{
		RestTemplate client = restTemplateBuilder.build();
		String uri = base+"/order/{orderId}";
		//Order order = client.getForObject(uri, Order.class,orderId);
		ResponseEntity<Order> responseEntity = client.getForEntity(uri, Order.class, orderId);
		HttpHeaders headers = responseEntity.getHeaders();
		Order order = responseEntity.getBody();
		return order;
	}
	
	@GetMapping("/getorders")
	public @ResponseBody List<Order> queryOrder() throws Exception{
		RestTemplate client = restTemplateBuilder.build();
		String uri = base+"/orders?offset={offset}";
		Integer offset = 1;
		//无参数
		HttpEntity body = null;
		ParameterizedTypeReference<List<Order>> typeRef = new ParameterizedTypeReference<List<Order>>() {};
		ResponseEntity<List<Order>> rs = client.exchange(uri, HttpMethod.GET, body, typeRef, offset);
		List<Order> order = rs.getBody();
		return order;
	
	}

	@GetMapping("/addorder")
	public @ResponseBody String testAddOrder() throws Exception{
		RestTemplate client = restTemplateBuilder.build();
		String uri = base+"/order";
		Order order = new Order();
		order.setName("test");
		HttpEntity<Order> body = new HttpEntity<Order>(order);
		String ret = client.postForObject(uri, body, String.class);
		return ret;
	}
}
