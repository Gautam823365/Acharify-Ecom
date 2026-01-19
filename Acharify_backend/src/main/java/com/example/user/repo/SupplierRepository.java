package com.example.user.repo;

import com.example.user.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
	
	

    // 🔍 Find by Email
    Optional<Supplier> findBySuppliersEmail(String suppliersEmail);

    // 📞 Find by Phone
    Optional<Supplier> findBySuppliersPhone(String suppliersPhone);

    // 🔁 Find by Status (ACTIVE / INACTIVE)
    List<Supplier> findByStatus(String status);

    // 🏙️ Find by City
    List<Supplier> findByCity(String city);

    // 🏛️ Find by State
    List<Supplier> findByState(String state);

    // 🧾 Find by GST ID
    Optional<Supplier> findByGstIdNo(String gstIdNo);
}
