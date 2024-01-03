package site.timecapsulearchive.core.global.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.timecapsulearchive.core.global.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
