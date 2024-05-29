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
import pet.store.controller.model.PetStoreEmployee;
import pet.store.service.PetStoreEmployeeService;

@RestController
@RequestMapping("/pet_store_employee")
@Slf4j
public class PetStoreEmployeeController {
	@Autowired
	private PetStoreEmployeeService pseService;

	@PostMapping("/{petStoreId}")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreEmployee addEmployee(@PathVariable Long petStoreId,
			@RequestBody PetStoreEmployee petStoreEmployee) {
		log.info("Adding employee to store {}: {}", petStoreId, petStoreEmployee);
		return pseService.saveEmployee(petStoreId, petStoreEmployee);
	}
}
