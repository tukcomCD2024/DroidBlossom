package site.timecapsulearchive.core.domain.capsule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.timecapsulearchive.core.domain.capsule.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
