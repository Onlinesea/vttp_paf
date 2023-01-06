package vttp2022.paf.assessment.eshop.respositories;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.exceptions.TemplateAssertionException;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp2022.paf.assessment.eshop.models.LineItem;
import vttp2022.paf.assessment.eshop.models.Order;
import vttp2022.paf.assessment.eshop.models.OrderStatus;
import vttp2022.paf.assessment.eshop.services.WarehouseService;

import static vttp2022.paf.assessment.eshop.respositories.Queries.*;

import java.util.Date;
import java.util.Random;

@Repository
public class OrderRepository {

	@Autowired
	private WarehouseService svc; 
	
	@Autowired
	private JdbcTemplate template;

	@Autowired
	private CustomerRepository customerRepo;

	// TODO: Task 3

	private synchronized String generateId() {
        Random r = new Random();
		int numChars = 8;
        StringBuilder sb = new StringBuilder();
        while (sb.length() < numChars) {
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, numChars);
    }

	public boolean saveOrderStatus(OrderStatus os){

		int orderStatusAdded = template.update(SQL_INSERT_NEW_ORDER_STATUS_DETAILS, os.getOrderId(),os.getDeliveryId(),os.getStatus(),new Date());
		return orderStatusAdded>0;
	}

	public JsonObject getOrderStatusCount(String user){
		System.out.println("Getting the order status count by user");

		//Sql request for the getting the count fo the respective status 
		SqlRowSet rs = template.queryForRowSet(SQL_GET__ORDER_STATUS_FOR_USER, user);
		int dispatched=0;
		int pending=0;

		
		while(rs.next()){
		//Assuming there is either "dispatched" or "pending"
			String status = rs.getString("status");
			int count = rs.getInt("count(os.status)");
			if("dispatched".equalsIgnoreCase(status)){
				dispatched=count;
			}else if("pending".equalsIgnoreCase(status)){
				pending=count;
			}

		}

		// Creating a JsonObject with name and the status counts 
		JsonObject statusObject = Json.createObjectBuilder()
							.add("name", user)
							.add("dispatched", dispatched)
							.add("pending", pending)
							.build();

		return statusObject;
	}


	//Adding transactional to enure the integrity of the data, making sure that both the orders adn the items are being saved in the database 
	@Transactional
	public Order saveOrder(Order order){

		//Generating a random unique ID for the order
		order.setOrderId(generateId());

		//Set the order date before saving it  
		order.setOrderDate(new Date());
		System.out.println("Saving Orders in OrderRepo...");
		int orderAdded = template.update(SQL_INSERT_NEW_ORDER_DETAILS, order.getOrderId(),order.getOrderDate(),order.getName());
		System.out.println("Order Added in OrderRepo >> " + orderAdded);
		
		//Loop through each item in the itemList 
		for(LineItem item: order.getLineItems()){
			System.out.println("Saving items in OrderRepo.. ");

			//Save each item in the database
			int itemAdded=template.update(SQL_INSERT_NEW_ITEM_DETAILS, item.getItem(),item.getQuantity(), order.getOrderId());
			System.out.println("Item Added in OrderRepo >> " + itemAdded);

		}
		return order;

	}
}
