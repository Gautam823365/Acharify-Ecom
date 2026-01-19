package com.example.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.user.dto.OrderDTO;
import com.example.user.service.PurchaseOrderService;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:4200") // ✅ Allow Angular frontend
public class PurchaseOrderController {

    private final PurchaseOrderService orderService;

    public PurchaseOrderController(PurchaseOrderService orderService) {
        this.orderService = orderService;
    }

    // ✅ Get all orders
    @GetMapping("/dto")
    public ResponseEntity<List<OrderDTO>> getAllOrdersDTO() {
        List<OrderDTO> orders = orderService.getAllOrdersDTO();
        return ResponseEntity.ok(orders);
    }

    // ✅ Get single order by ID
    @GetMapping("/dto/{id}")
    public ResponseEntity<?> getOrderDTOById(@PathVariable Long id) {
        try {
            OrderDTO order = orderService.getOrderDTOById(id);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Order with ID " + id + " not found");
        }
    }

    // (Optional) ✅ Create new order
    /*
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderCreateRequest request) {
        OrderDTO createdOrder = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }
    */
}
