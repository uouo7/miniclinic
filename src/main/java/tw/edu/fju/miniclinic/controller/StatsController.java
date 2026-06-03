package tw.edu.fju.miniclinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import tw.edu.fju.miniclinic.model.AppointmentRepository;
import tw.edu.fju.miniclinic.model.DoctorRepository;
import tw.edu.fju.miniclinic.model.PatientRepository;
import tw.edu.fju.miniclinic.model.Appointment;
import tw.edu.fju.miniclinic.model.Doctor;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class StatsController {

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @GetMapping("/stats")
    public String statsPage(Model model) {
        long doctorCount = doctorRepo.count();
        long patientCount = patientRepo.count();
        long apptCount = appointmentRepo.count();

        List<Object[]> byDept = appointmentRepo.countGroupedByDepartment();
        Map<String, Long> apptByDept = new LinkedHashMap<>();
        for (Object[] row : byDept) {
            String dept = (String) row[0];
            Long cnt = (Long) row[1];
            apptByDept.put(dept, cnt);
        }

        model.addAttribute("doctorCount", doctorCount);
        model.addAttribute("patientCount", patientCount);
        model.addAttribute("appointmentCount", apptCount);
        model.addAttribute("apptByDept", apptByDept);

        return "stats";
    }
}

