package pet.store.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreData;
import pet.store.dao.PetStoreDao;
import pet.store.entity.PetStore;

@Service
public class PetStoreService {
	@Autowired
	private PetStoreDao petStoreDao;

	@Transactional(readOnly = false)
	public PetStoreData savePetStore(PetStoreData petStoreData) {
		Long petStoreId = petStoreData.getPetStoreId();
		PetStore petStore = findOrCreatePetStore(petStoreId);

		copyPetStoreFields(petStore, petStoreData);
		return new PetStoreData(petStoreDao.save(petStore));

	}

	private void copyPetStoreFields(PetStore petStore, PetStoreData petStoreData) {
		petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
		petStore.setPetStoreCity(petStoreData.getPetStoreCity());
		petStore.setPetStoreId(petStoreData.getPetStoreId());
		petStore.setPetStoreName(petStoreData.getPetStoreName());
		petStore.setPetStorePhone(petStoreData.getPetStorePhone());
		petStore.setPetStoreState(petStoreData.getPetStoreAddress());
		petStore.setPetStoreZip(petStoreData.getPetStoreZip());
	}

	public PetStore findOrCreatePetStore(Long petStoreId) {
		if (Objects.isNull(petStoreId)) {
			return new PetStore();
		} else {
			return findPetStoreById(petStoreId);
		}

	}

	@Transactional(readOnly = true)
	public PetStoreData retrievePetStoreById(Long storeId) {
		PetStore petStore = findPetStoreById(storeId);
		return new PetStoreData(petStore);
	}

	public PetStore createPetStore(PetStoreData petStoreData) {

		if (petStoreData == null) {
			throw new IllegalArgumentException("Pet store data cannot be null");
		}

		PetStore petStore = new PetStore();
		setPetStoreFields(petStoreData, petStore);
		petStoreDao.save(petStore);

		return petStore;
	}

	@Transactional(readOnly = true)
	public List<PetStoreData> retrieveAllPetStores() {
		List<PetStore> petStores = petStoreDao.findAll();
		List<PetStoreData> petStoreDataList = new ArrayList<>();
		for (PetStore petStore : petStores) {
			PetStoreData data = new PetStoreData(petStore);
			data.setEmployee(Collections.emptySet());
			data.setCustomer(Collections.emptySet());
			petStoreDataList.add(data);
		}
		return petStoreDataList;
	}

	private void setPetStoreFields(PetStoreData petStoreData, PetStore petStore) {
		petStore.setPetStoreName(petStoreData.getPetStoreName());
		petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
		petStore.setPetStoreCity(petStoreData.getPetStoreCity());
		petStore.setPetStoreState(petStoreData.getPetStoreState());
		petStore.setPetStoreZip(petStoreData.getPetStoreZip());
		petStore.setPetStorePhone(petStoreData.getPetStorePhone());
	}

	public void deletePetStoreById(Long storeId) {
		PetStore petStore = findPetStoreById(storeId);
		if (petStore != null) {
			petStoreDao.delete(petStore);
		} else {
			throw new NoSuchElementException("PetStore with ID " + storeId + " does not exist.");
		}
	}

	private PetStore findPetStoreById(Long petStoreId) {
		return petStoreDao.findById(petStoreId)
				.orElseThrow(() -> new NoSuchElementException("Pet store with ID =" + petStoreId + "was not found."));
	}

}
