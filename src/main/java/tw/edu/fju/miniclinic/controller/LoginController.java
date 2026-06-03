package tw.edu.fju.miniclinic.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import tw.edu.fju.miniclinic.model.*;

@Controller
public class LoginController {

    @Autowired
    private DoctorRepository doctorRepo;

    // GET：顯示登入頁
    @GetMapping("/login")
    public String loginForm(Model model) {
        if (!model.containsAttribute("loginForm")) {
            model.addAttribute("loginForm", new LoginForm());
        }
        return "login";
    }

    // POST：處理登入
    @PostMapping("/login")
    public String login(
            @Valid @ModelAttribute("loginForm") LoginForm form,
            BindingResult result,
            HttpSession session,
            Model model) {

        // 步驟 1：檢查表單驗證
        if (result.hasErrors()) {
            return "login";  // 顯示錯誤訊息
        }

        // 步驟 2：查詢醫師
        Doctor doctor = doctorRepo.findById(form.getDoctorId()).orElse(null);

        // 步驟 3：檢查密碼（醫師不存在或密碼錯都給同樣的錯誤訊息，避免洩漏帳號是否存在）
        if (doctor == null || !BCrypt.checkpw(form.getPassword(), doctor.getPasswordHash())) {
            model.addAttribute("errorMessage", "醫師編號或密碼錯誤");
            return "login";
        }

        // 步驟 4：登入成功，存入 Session
        session.setAttribute("loggedInDoctorId", doctor.getDoctorId());
        session.setAttribute("loggedInDoctorName", doctor.getName());

        return "redirect:/dashboard";
    }

    @GetMapping("/password")
    public String passwordForm(HttpSession session, Model model) {
        String doctorId = (String) session.getAttribute("loggedInDoctorId");
        String doctorName = (String) session.getAttribute("loggedInDoctorName");
        if (doctorId == null) {
            return "redirect:/login";
        }
        if (!model.containsAttribute("passwordForm")) {
            model.addAttribute("passwordForm", new PasswordForm());
        }
        model.addAttribute("loggedInDoctorName", doctorName);
        return "password";
    }

    @PostMapping("/password")
    public String updatePassword(
            @Valid @ModelAttribute("passwordForm") PasswordForm form,
            BindingResult result,
            HttpSession session,
            Model model) {

        String doctorId = (String) session.getAttribute("loggedInDoctorId");
        String doctorName = (String) session.getAttribute("loggedInDoctorName");
        if (doctorId == null) {
            return "redirect:/login";
        }

        Doctor doctor = doctorRepo.findById(doctorId).orElse(null);
        if (doctor == null) {
            session.invalidate();
            return "redirect:/login";
        }

        if (!BCrypt.checkpw(form.getOldPassword(), doctor.getPasswordHash())) {
            result.rejectValue("oldPassword", "invalid.oldPassword", "舊密碼錯誤");
        }
        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "mismatch.confirmPassword", "兩次密碼不相符");
        }
        if (form.getNewPassword() != null && form.getNewPassword().length() < 8) {
            result.rejectValue("newPassword", "short.newPassword", "密碼至少需要 8 個字元");
        }

        if (result.hasErrors()) {
            model.addAttribute("loggedInDoctorName", doctorName);
            return "password";
        }

        doctor.setPasswordHash(BCrypt.hashpw(form.getNewPassword(), BCrypt.gensalt()));
        doctorRepo.save(doctor);

        model.addAttribute("successMessage", "密碼已更新");
        model.addAttribute("passwordForm", new PasswordForm());
        model.addAttribute("loggedInDoctorName", doctorName);
        return "password";
    }

    // 登出
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // 清除 Session
        return "redirect:/login";
    }
}