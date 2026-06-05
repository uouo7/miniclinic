package tw.edu.fju.miniclinic.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByApptDate(LocalDate apptDate);

    List<Appointment> findByDoctor(Doctor doctor);

    List<Appointment> findByPatient(Patient patient);

    long countByApptDateBetween(LocalDate from, LocalDate to);

    long countByStatus(String status);

    List<Appointment> findByDoctorAndApptDate(Doctor doctor, LocalDate apptDate);  // 新加入

    // 回傳每個科別的掛號數：{ department, count }
    @Query("SELECT d.department, COUNT(a) FROM Appointment a JOIN a.doctor d GROUP BY d.department ORDER BY d.department")
    List<Object[]> countGroupedByDepartment();

    List<Appointment> findByApptDateAndDoctor(LocalDate localDate, Doctor doctor);
}