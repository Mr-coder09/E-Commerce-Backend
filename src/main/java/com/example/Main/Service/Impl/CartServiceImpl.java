package com.example.Main.Service.Impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
//import java.util.Iterator;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Main.DTO.CartItemsRequest;
//import com.example.Main.DTO.CartItemsResponse;
import com.example.Main.DTO.CartResponse;
import com.example.Main.Entity.Cart;
import com.example.Main.Entity.CartItems;
import com.example.Main.Entity.Product;
import com.example.Main.Entity.User;
import com.example.Main.Enum.CartStatus;
import com.example.Main.Exceptions.IdNotFoundException;
import com.example.Main.Exceptions.BadRequestException;
import com.example.Main.HelperClass.RecalculateTotalAmount;
import com.example.Main.Mapper.CartMapper;
import com.example.Main.Repository.CartItemsRepository;
import com.example.Main.Repository.CartRepository;
import com.example.Main.Repository.ProductRepository;
import com.example.Main.Repository.UserRepository;
import com.example.Main.Service.AuditService;
import com.example.Main.Service.CartService;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	CartRepository cartRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CartItemsRepository cartItemsRepository;
	
	@Autowired
	AuditService auditService;
	
	@Autowired
	CartMapper cartMapper;

	private static final  Logger log = LoggerFactory.getLogger(CartServiceImpl.class);
	
	@Transactional(readOnly = true)
	private Cart getOrCreateActiveCart(Long userId) {
		return cartRepository.findByUser_IdAndStatus(userId, CartStatus.ACTIVE)

				.orElseGet(() -> {

					User user = userRepository.findById(userId)
							.orElseThrow(() -> new IdNotFoundException(" User Not Found "));

					Cart cart = new Cart();
					cart.setUser(user);
					cart.setStatus(CartStatus.ACTIVE);
					cart.setTotalAmount(BigDecimal.ZERO);
//					cart.setUpdatedAt(LocalDateTime.now());
//					cart.setCreatedAt(LocalDateTime.now());
					cart.setCartItems(new ArrayList<CartItems>());

					return cartRepository.save(cart);
					
					
				});

	}

//	@Transactional
//	@Override
//	public CartResponse createCart(Long userId) {
//
//		Cart cart = getOrCreateActiveCart(userId);
//		CartResponse response = new CartResponse();
//		response.setId(cart.getId());
//		response.setTotalAmount(cart.getTotalAmount());
//		response.setStatus(cart.getStatus());
//		response.setCartItems(new ArrayList<>());
//
//		return response;
//	}

	@Override
	public CartResponse getMyCart(Long userId) {

		
		Cart cart = getOrCreateActiveCart(userId);

//		CartResponse response = mapToResponse(cart);
		CartResponse response = cartMapper.toResponse(cart);
//		response.setId(cart.getId());
//		response.setTotalAmount(cart.getTotalAmount());
//		response.setStatus(cart.getStatus());
//
////		List<CartItems> cartItems = cart.getCartItems();
//		List<CartItemsResponse> responseItems = new ArrayList<CartItemsResponse>();
//		for (CartItems items : cart.getCartItems()) {
//			CartItemsResponse cartItemsResponse = new CartItemsResponse();
//			cartItemsResponse.setId(items.getId());
//			cartItemsResponse.setProductId(items.getProduct().getId());
//			cartItemsResponse.setProductName(items.getProduct().getName());
//			cartItemsResponse.setQuantity(items.getQuantity());
//			cartItemsResponse.setPriceAtThatTime(items.getPriceAtThatTime());
//			responseItems.add(cartItemsResponse);
//
//		}
//
//		response.setCartItems(responseItems);
		
		
		
		
		
//		log.info("User {} had Created Cart ", userId);
		log.info("Fetched cart for userId={}", userId);
		
		
		
//		auditService.log(
//			    userId,                    // Long userId
//			    email,                     // String email
//			    role,                      // String role
//			    "CHECKOUT",                // action
//			    "ORDER",                   // entityType
//			    savedOrder.getId(),        // entityId
//			    "SUCCESS",                 // status
//			    "Order placed successfully"
//			);

		return response;
	}

	@Transactional
	@Override
	public CartResponse addItem(Long userId, CartItemsRequest request) {

//		Cart cart =  cartRepository.findByUser_IdAndStatus(userId, CartStatus.ACTIVE)
//				
//				.orElseGet(()-> {
//				createCart(userId);
//				return cartRepository.findByUser_IdAndStatus(userId, CartStatus.ACTIVE).get();
//				});

//		Cart cart = cartRepository.findByUser_IdAndStatus(userId, CartStatus.ACTIVE)
//				.orElseThrow(() -> new IdNotFoundException("No active cart found"));

		Cart cart = getOrCreateActiveCart(userId);
		
		

		Product product = productRepository.findById(request.getProductId())
				.orElseThrow(() -> new IdNotFoundException("Product Not Found "));

		if (request.getQuantity() <= 0) {
			throw new BadRequestException("Quantity must be greater than zero");
		}

		if (request.getQuantity() > product.getStockQuantity()) {
			throw new BadRequestException("Only " + product.getStockQuantity() + " items left in stock");
		}

		List<CartItems> itemList = cart.getCartItems();

		boolean found = false;

		for (CartItems items : itemList) {

			if ((items.getProduct().getId()).equals(product.getId())) {

				int newQty = items.getQuantity() + request.getQuantity();

				if (newQty > product.getStockQuantity()) {
					throw new BadRequestException("Only " + product.getStockQuantity() + " items left in stock");
				}

				items.setQuantity(newQty);
				items.setUpdatedAt(LocalDateTime.now());

				found = true;
				break;

			}
		}

		if (!found) {

			CartItems newCartItems = new CartItems();
			newCartItems.setCart(cart);
			newCartItems.setProduct(product);
			newCartItems.setQuantity(request.getQuantity());
			newCartItems.setPriceAtThatTime(product.getPrice());
//			newCartItems.setCreatedAt(LocalDateTime.now());
//			newCartItems.setUpdatedAt(LocalDateTime.now());

			itemList.add(newCartItems);
		}

//		BigDecimal total = BigDecimal.ZERO;
//		
//		for (CartItems items : itemList) {
//			
//			BigDecimal totalItem = items.getPriceAtThatTime()
//						.multiply(BigDecimal.valueOf(items.getQuantity()));
//			
//			total = total.add(totalItem);
//			
//		}
//		cart.setTotalAmount(total);
		cart.setTotalAmount(RecalculateTotalAmount.calculateTotalAmount(itemList));

		cart.setUpdatedAt(LocalDateTime.now());

//		cart.setCartItems(itemList);

		Cart savedCart = cartRepository.save(cart);
		
//		CartResponse response = mapToResponse(savedCart);
		CartResponse response = cartMapper.toResponse(savedCart);
		
		
//		CartResponse response = new CartResponse();
//		response.setId(savedCart.getId());
//		response.setStatus(savedCart.getStatus());
//		response.setTotalAmount(savedCart.getTotalAmount());
//
//		List<CartItemsResponse> responseItems = new ArrayList<>();
//		for (CartItems item : savedCart.getCartItems()) {

//			CartItemsResponse res = new CartItemsResponse();
//			CartItemsResponse res = cartMapper.toItemResponse(item);
//			res.setId(item.getId());
//			res.setProductId(item.getProduct().getId());
//			res.setQuantity(item.getQuantity());
//			res.setProductName(item.getProduct().getName());
//			res.setPriceAtThatTime(item.getPriceAtThatTime());

//			responseItems.add(res);
//		}

//		response.setCartItems(responseItems);
		
		
		
		log.info(
			    "User {} added product {} qty={} to cart {}",
			    userId,
			    product.getId(),
			    request.getQuantity(),
			    cart.getId()
			);

		auditService.log(
		        userId,
		        cart.getUser().getEmail(),
		        cart.getUser().getRole().name(),
		        "ADD_TO_CART",
		        "CART",
		        cart.getId(),
		        "SUCCESS",
		        "Product " + product.getId() + " added to cart"
		);
		
		
		


		return response;
	}

	@Transactional
	@Override
	public CartResponse updateItem(Long userId, Long cartItemId, int quantity) {

		Cart cart = cartRepository.findByUser_IdAndStatus(userId, CartStatus.ACTIVE)
				.orElseThrow(() -> new IdNotFoundException("NO active User Cart")

//					createCart(userId);
//					return cartRepository.findByUser_IdAndStatus(userId, CardStatus.ACTIVE).get();
				);

		List<CartItems> cartItems = cart.getCartItems();

		CartItems targetItems = null;

		for (CartItems items : cartItems) {

			if (items.getId().equals(cartItemId)) {
				targetItems = items;
				break;

			}
		}
		
		
		
		

		if (targetItems == null) {
			throw new IdNotFoundException("CartItem not found");
		}

		if (quantity == 0) {

			cartItems.remove(targetItems);

		} else if (quantity < 0) {
			throw new BadRequestException("Quantity SHould be more then 0");
		}

		
		
		else {
			
			Product product = targetItems.getProduct();
			if (quantity > product.getStockQuantity()) {
				
				log.warn("Invalid cart quantity: userId={}, productId={}, requestedQty={}",
				        userId, product.getId(), quantity );
				
				auditService.log(userId, cart.getUser().getEmail(), cart.getUser().getRole().name(),
						"UPDATE_CART_ITEMS", "CART", cartItemId, "FAILED", " Insufficient stock for product ID: " + product.getId());
				
				throw new BadRequestException("Only " + product.getStockQuantity() + " items left in stock");
				
			}
			
			
			
			
			targetItems.setQuantity(quantity);
			targetItems.setUpdatedAt(LocalDateTime.now());

		}

		cart.setTotalAmount(RecalculateTotalAmount.calculateTotalAmount(cartItems));
		cart.setUpdatedAt(LocalDateTime.now());

		Cart savedCart = cartRepository.save(cart);

//		CartResponse response = new CartResponse();
//		response.setId(savedCart.getId());
//		response.setTotalAmount(savedCart.getTotalAmount());
//		response.setStatus(savedCart.getStatus());
//
//		List<CartItemsResponse> responseItems = new ArrayList<CartItemsResponse>();
//
//		for (CartItems cartItemsResponse : savedCart.getCartItems()) {
//
//			CartItemsResponse res = new CartItemsResponse();
//			res.setId(cartItemsResponse.getId());
//			res.setProductId(cartItemsResponse.getProduct().getId());
//			res.setProductName(cartItemsResponse.getProduct().getName());
//			res.setPriceAtThatTime(cartItemsResponse.getPriceAtThatTime());
//			res.setQuantity(cartItemsResponse.getQuantity());
//
//			responseItems.add(res);
//		}
//
//		response.setCartItems(responseItems);
		
//		CartResponse response = mapToResponse(savedCart);
		CartResponse response = cartMapper.toResponse(savedCart);
		
		
		auditService.log(userId, savedCart.getUser().getEmail(), savedCart.getUser().getRole().name(),
				"UPDATE_CART_ITEMS ", "CART ", cartItemId, "SUCCESS", "Cart item updated  ");
		
		log.info("Cart item updated: userId={}, cartItemId={}, qty={}",
		        userId, cartItemId, quantity);

		return response;
	}

	@Transactional
	@Override
	public CartResponse removeItem(Long userId, Long cartItemId) {

		Cart cart = cartRepository.findByUser_IdAndStatus(userId, CartStatus.ACTIVE)
				.orElseThrow(() -> new IdNotFoundException("No active cart found for user"));

		List<CartItems> cartItems = cart.getCartItems();
		CartItems targetItems = null;

		for (CartItems items : cartItems) {

			if (items.getId().equals(cartItemId)) {
				targetItems = items;
				break;
			}
		}

		if (targetItems == null) {
			
			auditService.log(userId, cart.getUser().getEmail(), cart.getUser().getRole().name(), 
					"REMOVE ITEMS", "CART", cartItemId, "FAILED", "Cart item not found ");
			throw new IdNotFoundException("Cart item not found");
			
			
		}

		cartItems.remove(targetItems);

		cart.setTotalAmount(RecalculateTotalAmount.calculateTotalAmount(cartItems));

		cart.setUpdatedAt(LocalDateTime.now());

		Cart savedCart = cartRepository.save(cart);

//		CartResponse response = new CartResponse();
//		response.setId(savedCart.getId());
//		response.setTotalAmount(savedCart.getTotalAmount());
//		response.setStatus(savedCart.getStatus());
//
//		List<CartItemsResponse> cartItemsResponses = new ArrayList<CartItemsResponse>();
//
//		for (CartItems itemsResponse : savedCart.getCartItems()) {
//
//			CartItemsResponse res = new CartItemsResponse();
//			res.setId(itemsResponse.getId());
//			res.setProductId(itemsResponse.getProduct().getId());
//			res.setProductName(itemsResponse.getProduct().getName());
//			res.setQuantity(itemsResponse.getQuantity());
//			res.setPriceAtThatTime(itemsResponse.getPriceAtThatTime());
//			cartItemsResponses.add(res);
//		}
//
//		response.setCartItems(cartItemsResponses);
		
//		CartResponse response = mapToResponse(savedCart);
		CartResponse response = cartMapper.toResponse(savedCart);
		
		log.info("Cart item removed: userId={}, cartItemId={}",
		        userId, cartItemId);
		
		auditService.log(userId, cart.getUser().getEmail(), cart.getUser().getRole().name(), 
				"REMOVE ITEMS", "CART", cartItemId, "SUCCESS", " CartItem Removed Successfully ");

		return response;
	}

	@Transactional
	@Override
	public void clearCart(Long userId) {

		Cart cart = cartRepository.findByUser_IdAndStatus(userId, CartStatus.ACTIVE)
				.orElseThrow(() -> new IdNotFoundException("No active cart found for user"));

		cart.getCartItems().clear();

		cart.setTotalAmount(BigDecimal.ZERO);
		cart.setUpdatedAt(LocalDateTime.now());

		cartRepository.save(cart);
		log.info("Cart cleared for userId={}", userId);
		
		auditService.log(userId, cart.getUser().getEmail(), cart.getUser().getRole().name(), 
				"CLEAR CART", "CART", null, "SUCCESS", " Cart Clear Successfully ");
		
	}
	
	
//	private CartResponse mapToResponse(Cart cart) {
//		
//		CartResponse response = new CartResponse();
//		
//		response.setId(cart.getId());
//		response.setTotalAmount(cart.getTotalAmount());
//		response.setStatus(cart.getStatus());
//
////		List<CartItems> cartItems = cart.getCartItems();
//		List<CartItemsResponse> responseItems = new ArrayList<CartItemsResponse>();
//		for (CartItems items : cart.getCartItems()) {
//			CartItemsResponse cartItemsResponse = new CartItemsResponse();
//			cartItemsResponse.setId(items.getId());
//			cartItemsResponse.setProductId(items.getProduct().getId());
//			cartItemsResponse.setProductName(items.getProduct().getName());
//			cartItemsResponse.setQuantity(items.getQuantity());
//			cartItemsResponse.setPriceAtThatTime(items.getPriceAtThatTime());
//			responseItems.add(cartItemsResponse);
//
//		}
//
//		response.setCartItems(responseItems);
//		
//		
//		return response;
//		
//		
//		
//	}
	

}
