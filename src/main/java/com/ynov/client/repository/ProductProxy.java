package com.ynov.client.repository;


import java.nio.charset.Charset;

import java.util.List;
import java.util.Optional;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.ynov.client.ApiProperties;
import com.ynov.client.model.Product;

@Component
public class ProductProxy {

	@Autowired
	private ApiProperties props;
	
	
	private HttpHeaders createHeaders(String username, String password) {
		return new HttpHeaders() {{
			String auth = username + ":" + password;
			byte[] encodeAuth = Base64.encodeBase64(
					auth.getBytes(Charset.forName("US-ASCII"))
		);
		String authHeader = "Basic" + new String(encodeAuth);
		set( "Authorization", authHeader);
	}};
	
	}
	
	public List<Product> getProducts() {
		
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<List<Product>> response =
				restTemplate.exchange(
						props.getUrl() + "/api/private/products", 
						HttpMethod.GET, 
						new HttpEntity<>(createHeaders("admin", "12345")), 
						new ParameterizedTypeReference<List<Product>>() {}
					);
		return response.getBody();
	}

	public Product getProductById(Integer id) {
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<Product> response =
				restTemplate.exchange(
						props.getUrl() + "/api/private/product/" + id, 
						HttpMethod.GET, 
						new HttpEntity<>(createHeaders("admin", "12345")),
						Product.class);
		return response.getBody();
	}
	
	public void save(Product product) {
		RestTemplate restTemplate = new RestTemplate();
		
		HttpEntity<Product> request = new HttpEntity<Product>(product, createHeaders("admin", "12345"));
		
		ResponseEntity<Product> response = restTemplate.exchange(
				props.getUrl() + "/api/private/product",
				HttpMethod.POST,
				request,
				Product.class				
				);
	}
	
	
}
