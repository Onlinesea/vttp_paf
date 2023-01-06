package vttp2022.paf.assessment.eshop.services;

import java.util.LinkedList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.sym.Name;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import vttp2022.paf.assessment.eshop.models.LineItem;
import vttp2022.paf.assessment.eshop.models.Order;
import vttp2022.paf.assessment.eshop.models.OrderStatus;

@Service
public class WarehouseService {

	// You cannot change the method's signature
	// You may add one or more checked exceptions
	public OrderStatus dispatch(Order order) {

		// TODO: Task 4

		System.out.println("Dispatching order through warehouse service...");
		final String SERVER_URL = "http://paf.chuklee.com/dispatch";

		// Creating the URL string for query
		String uri = SERVER_URL + "/" + order.getOrderId();
		System.out.println("URl that is being generated >> " + uri);

		List<JsonObject> items = new LinkedList<>();
		
		//Creating JsonObject for the list of lineItems
		for(LineItem item: order.getLineItems()){
			items.add(Json.createObjectBuilder()
			.add("item", item.getItem())
			.add("quautity", item.getQuantity())
			.build());
		}
		
		//Constructing payload of the dispatch
		JsonObject payload = Json.createObjectBuilder()
							.add("orderId", order.getOrderId())
							.add("name", order.getName())
							.add("address", order.getAddress())
							.add("email", order.getEmail())
							.add("lineItems",items.toString())
							.add("createdBy","Chen Xinhai")
							.build();

		System.out.println("Payload that is generated >> " + payload);
		//Constructing the dispatch request
		RequestEntity<JsonObject> req = RequestEntity 
		.post(uri)
		.accept(MediaType.APPLICATION_JSON)
		.contentType(MediaType.APPLICATION_JSON )
		.body(payload) ;

		RestTemplate template = new RestTemplate();

		//Sending the post request and getting a response from the server 
		//Somehow the response is not received
		ResponseEntity<JsonObject> resp = template.exchange(req, JsonObject.class);
		System.out.println("Response received >>> " + resp.getBody());
		OrderStatus orderStatus = new OrderStatus();
		JsonObject respJsonObject = resp.getBody();

		// Set the orderId of the order status
		orderStatus.setOrderId(order.getOrderId());

		//If the resp contains error, set the status to pending and return the orderStatus
		if(respJsonObject.containsKey("error")){
			orderStatus.setStatus("pending");
			
			return orderStatus;
		}
		
		//Else the order will be dispatched and set the delieveryId 
		orderStatus.setStatus("dispatched");
		orderStatus.setDeliveryId(respJsonObject.getString("deliveryId"));

		return orderStatus;




	}
}
