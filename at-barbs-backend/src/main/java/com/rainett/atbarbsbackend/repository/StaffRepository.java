package com.rainett.atbarbsbackend.repository;

import com.rainett.atbarbsbackend.dto.StaffDto;
import com.rainett.atbarbsbackend.model.users.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    @Query("select new com.rainett.atbarbsbackend.dto.StaffDto(" +
            "s.id, s.firstName, s.lastName, s.email, s.role, " +
            "s.createdAt, s.updatedAt, s.specialization, s.startTime, s.endTime) " +
            "from Staff s " +
            "join s.services srv " +
            "where srv.id = :id")
    Page<StaffDto> findStaffByServiceId(Long id, Pageable pageable);
}
