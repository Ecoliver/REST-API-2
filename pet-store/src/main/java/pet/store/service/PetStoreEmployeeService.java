package pet.store.service;

import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Data;
import lombok.NoArgsConstructor;
import pet.store.controller.model.PetStoreEmployee;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Data
@Service
@NoArgsConstructor
public class PetStoreEmployeeService {

	@Autowired
	private PetStoreDao petStoreDao;

	@Autowired
	private EmployeeDao employeeDao;


	@Transactional(readOnly = false)
    public PetStoreEmployee saveEmployee(Long petStoreId, PetStoreEmployee petStoreEmployee) {
        PetStore petStore = findPetStoreById(petStoreId);
        Employee employee = findOrCreateEmployee(petStoreEmployee.getEmployeeId(), petStoreId);
        transformEmployee (employee, petStoreEmployee);
        employee.setPetStore(petStore);
        petStore.getEmployees().add(employee);
        
        employeeDao.save(employee);
        return new PetStoreEmployee(employee);
    }
	public Employee transformEmployee(Employee employee, PetStoreEmployee psed) {
        employee.setEmployeeFirstName(psed.getEmployeeFirstName());
        employee.setEmployeeLastName(psed.getEmployeeLastName());
        employee.setEmployeePhone(psed.getEmployeePhone());
        employee.setEmployeeJobTitle(psed.getEmployeeJobTitle());
		return employee;
    }
	
    private PetStore findPetStoreById(Long petStoreId) {
        return petStoreDao.findById(petStoreId)
                .orElseThrow(() -> new NoSuchElementException("Invalid pet store id"));
    }

    private Employee findEmployeeById(Long petStoreId, Long employeeId) {
        Employee employee = employeeDao.findById(employeeId)
                .orElseThrow(() -> new NoSuchElementException("Employee not found"));
        if (!employee.getPetStore().getPetStoreId().equals(petStoreId)) {
            throw new IllegalArgumentException("Employee does not belong to the specified pet store");
        }
        return employee;
    }

    private Employee findOrCreateEmployee(Long employeeId, Long petStoreId) {
        if (Objects.isNull(employeeId)) {
        	return new Employee();
        } 
            return findEmployeeById(petStoreId, employeeId);
        }
    

//    private PetStoreEmployee createEmployeeForPetStore(PetStoreEmployee pse, PetStore petStore) {
//    	petStore.getPetStoreEmployees().add(pse);
//		return pse;
//    }
    
}