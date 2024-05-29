package pet.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pet.store.controller.model.PetStoreCustomer;
import pet.store.service.PetStoreCustomerService;

@RestController
@RequestMapping("/pet_store_customer")
@Slf4j
public class PetStoreCustomerController {
	@Autowired
	private PetStoreCustomerService pscService;
	
	@PostMapping("/{petStoreId}")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreCustomer addCustomer(@PathVariable Long petStoreId,
			@RequestBody PetStoreCustomer petStoreCustomer) {
	    log.info("Adding customer to store {}: {}", petStoreId, petStoreCustomer);
	    return pscService.postCustomer(petStoreId, petStoreCustomer);
	}
}
