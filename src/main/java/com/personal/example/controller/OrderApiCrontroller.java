package com.personal.example.controller;

import com.personal.example.entity.Order;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1") 
public class OrderApiCrontroller {

	
	@GetMapping("/order/{orderId}")
	public Order getOrder(@PathVariable String orderId) throws Exception{
		Order order = new Order();
		order.setId("O170501-"+orderId);
		return order;
	}
	
	@GetMapping("/order")
	public List<Order> getOrder(Integer offset) throws Exception{
		Order order = new Order();
		order.setId("O170501-1");
		List<Order> list = new ArrayList<Order>();
		list.add(order);
		return list;
	}
	
	@PostMapping("/order")
	public String addOrder(@RequestBody Order order) throws Exception{
		return "{\"success\":true,\"message\":\"添加成功\"}";
	}
	
	
	@PostMapping("/orders")
	public String batchAdd(@RequestBody List<Order> order) throws Exception{
		return "{success:true,message:\"批量添加成功\"}";
	}
	
	@PutMapping("/order")
	public String updateOrder(Order order) throws Exception{
		return "{\"success\":true,\"message\":\"更新成功\"}";
	}
	
	
	@DeleteMapping("/order/{orderId}")
	public String cancelOrder(@PathVariable() String orderId) throws Exception{
		return "{\"success\":true,\"message\":\"订单取消成功\"}";
	}
	
}
