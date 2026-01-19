//package com.example.user.controller;
//@RestController
//@RequestMapping("/api/cart")
//@CrossOrigin(origins = "http://localhost:4200")
//public class CheckoutController {
//
//    private final CheckoutService checkoutService;
//
//    public CheckoutController(CheckoutService checkoutService) {
//        this.checkoutService = checkoutService;
//    }
//
//    @PostMapping("/checkout")
//    public ResponseEntity<CheckoutResponse> checkout(
//            @RequestBody CheckoutRequest request) {
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(checkoutService.placeOrder(request));
//    }
//
//    @GetMapping("/checkout/{orderId}")
//    public ResponseEntity<CheckoutResponse> getOrder(
//            @PathVariable Long orderId) {
//
//        return ResponseEntity.ok(checkoutService.getOrder(orderId));
//    }
//}
