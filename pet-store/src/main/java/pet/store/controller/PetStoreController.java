package pet.store.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pet.store.controller.model.PetStoreData;
import pet.store.entity.PetStore;
import pet.store.service.PetStoreService;

@RestController
@RequestMapping("/pet_store")
@Slf4j
public class PetStoreController {
	@Autowired
	private PetStoreService petStoreService;

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStore createPetStore(@RequestBody PetStoreData petStoreData) {
		log.info("Creating pet store {}", petStoreData);
			return petStoreService.createPetStore(petStoreData);
		
	}

	@PutMapping("/{petStoreId}")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreData updatePetStore(@PathVariable Long petStoreId,
			@RequestBody PetStoreData petStoreData) {
		petStoreData.setPetStoreId(petStoreId);
		log.info("Updating store {}", petStoreData);
		return petStoreService.savePetStore(petStoreData);

	}

	@GetMapping("/{storeId}")
	public PetStoreData retrievePetStoreById(@PathVariable Long storeId) {
		log.info("Retrieving pet store {}", storeId);
		return petStoreService.retrievePetStoreById(storeId);
	}

	@GetMapping
	public List<PetStoreData> getAllPetStores() {
		log.info("Retrieving all pet stores");
		return petStoreService.retrieveAllPetStores();
	}

	@DeleteMapping("/{storeId}")
	public Map<String, String> deletePetStoreById(@PathVariable Long storeId) {
		log.info("Deleting pet store {}", storeId);
		petStoreService.deletePetStoreById(storeId);
		return Collections.singletonMap("message", "Deletion successful");
	}

}
