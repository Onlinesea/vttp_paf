package vttp2022.paf.assessment.eshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp2022.paf.assessment.eshop.respositories.OrderRepository;

@RestController
@RequestMapping(path= "/api", produces=MediaType.APPLICATION_JSON_VALUE )
public class OrderRestController {
    
    @Autowired
    private OrderRepository orderRepo;

    @GetMapping("/order/{name}/status")
    public ResponseEntity<String> OrderStatus(@PathVariable String name ){
        // Here I will get the list of order status and count 
        JsonObject result = orderRepo.getOrderStatusCount(name);

        //result is obtained 
        return ResponseEntity.ok(result.toString());
    }
    
}
