package vttp2022.paf.assessment.eshop.controllers;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp2022.paf.assessment.eshop.models.Customer;
import vttp2022.paf.assessment.eshop.models.LineItem;
import vttp2022.paf.assessment.eshop.models.Order;
import vttp2022.paf.assessment.eshop.models.OrderStatus;
import vttp2022.paf.assessment.eshop.respositories.CustomerRepository;
import vttp2022.paf.assessment.eshop.respositories.OrderRepository;
import vttp2022.paf.assessment.eshop.services.WarehouseService;

@Controller
public class OrderController {

	@Autowired
	private WarehouseService svc;

	@Autowired
	private CustomerRepository customerRepo;

	@Autowired
	private OrderRepository orderRepo;

	@GetMapping("/testing")
	public String checkUser(@RequestParam String user){

		// Testing will a dummy order to try saving 
		Order dummyOrder = new Order();
		List<LineItem> itemsList = new LinkedList<>();
		LineItem apple = new LineItem();
		apple.setItem("apple");
		apple.setQuantity(2);
		itemsList.add(apple);

		Customer customer = new Customer();
		customer.setName("fred");
		customer.setEmail("fredflintstone@bedrock.com");
		customer.setAddress("201 Cobblestone Lane");

		dummyOrder.setAddress("testing");
		dummyOrder.setName("fred");
		dummyOrder.setEmail("fredflintstone@bedrock.com");
		dummyOrder.setAddress("201 Cobblestone Lane");
		dummyOrder.setLineItems(itemsList);
		dummyOrder.setOrderDate(new Date());

		//Dispatch the orders once it is being saved
		Order savedOrder = orderRepo.saveOrder(dummyOrder);
		OrderStatus dispatchOrder = svc.dispatch(savedOrder);


		Optional<Customer> getUser = customerRepo.findCustomerByName(user);
		if(getUser.isEmpty()){
			JsonObject errJson = Json.createObjectBuilder()
            .add("error","User " + user + " not found")
            .build();

			return errJson.toString();
		}
		return "testing";
	}

	@PostMapping("/checkout")
	public String saveOrder(Model model,MultiValueMap<String, String> form ){

		Order newOrder = new Order();
		String user = form.getFirst("Name");

		Optional<Customer> getUser = customerRepo.findCustomerByName(form.getFirst("user"));
		if(getUser.isEmpty()){
			JsonObject errJson = Json.createObjectBuilder()
            .add("error","User " + user + " not found")
            .build();

			return errJson.toString();
		}
		Order savedOrder = orderRepo.saveOrder(newOrder);
		OrderStatus dispatchOrder = svc.dispatch(savedOrder);

		return "index";
	
	}
}
