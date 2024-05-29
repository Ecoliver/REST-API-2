package pet.store.service;

import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreCustomer;
import pet.store.dao.CustomerDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
import pet.store.entity.PetStore;

@Service
public class PetStoreCustomerService {

	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private PetStoreDao petStoreDao;
	
	@Autowired
	private PetStoreService petStoreService;


	@Transactional(readOnly = false)
	public PetStoreCustomer postCustomer(Long petStoreId, PetStoreCustomer petStoreCustomer) {
		PetStore petStore = petStoreService.findOrCreatePetStore(petStoreId);

		if (petStore.equals(null)) {
			throw new NoSuchElementException("Invalid pet store id");
		}


		Customer customer = findOrCreateCustomer(petStoreId, petStoreCustomer.getCustomerId());
		transformCustomer(customer, petStoreCustomer);
		
		petStore.getCustomer().add(customer);
		customer.getPetStore().add(petStore);
		return new PetStoreCustomer(customerDao.save(customer));
	}

//	private PetStoreCustomer saveCustomer(Customer customer, PetStoreCustomer petStoreCustomer) {
//	    Customer newCustomer = new Customer();
//	    transformer.transform(newCustomer, petStoreCustomer);
//
//	    customerDao.save(newCustomer);
//	    return newCustomer;
//	}
	
	private Customer findOrCreateCustomer(Long petStoreId, Long customerId) {
		if(Objects.isNull(customerId)) {
			return new Customer();
		}
		return findCustomerById(petStoreId, customerId);
	}

	private Customer findCustomerById(Long petStoreId, Long customerId) {
		Customer customer = customerDao.findById(customerId).orElseThrow(() -> new NoSuchElementException("Customer with ID =" + customerId + " was not found"));
		boolean found = false;
		for(PetStore petStore: customer.getPetStore()) {
			if(petStore.getPetStoreId()== petStoreId) {
				found = true;
				break;
			}
		}
		if(!found) {
			throw new IllegalArgumentException("Customer with the ID =" + customerId + "not a member of the pet store with ID");
		}
		return customer;
	}

	public Customer transformCustomer(Customer customer, PetStoreCustomer pscd) {
		customer.setCustomerFirstName(pscd.getCustomerFirstName());
		customer.setCustomerLastName(pscd.getCustomerLastName());
		customer.setCustomerEmail(pscd.getCustomerEmail());
		return customer;
	}
	
	
	@SuppressWarnings("unused")
	private Customer findCustomerById(Long customerId) {
		Customer customer = customerDao.findById(customerId)
				.orElseThrow(() -> new NoSuchElementException("Customer not found"));
		boolean found = customer.getPetStore().stream().anyMatch(store -> store.getPetStoreId().equals(customerId));
		if (!found) {
			throw new IllegalArgumentException("Customer does not belong to the specified pet store");
		}
		return customer;
	}

}
