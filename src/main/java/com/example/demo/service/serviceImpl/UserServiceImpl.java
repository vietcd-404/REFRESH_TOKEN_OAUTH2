package com.example.demo.service.serviceImpl;

import com.example.demo.dto.request.SignupRequest;
import com.example.demo.dto.response.MessageResponse;
import com.example.demo.email.EmailSender;
import com.example.demo.entity.Role;
import com.example.demo.entity.Tokens;
import com.example.demo.entity.UserRole;
import com.example.demo.entity.Users;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.repo.UsersRepo;
import com.example.demo.service.RoleService;
import com.example.demo.service.TokenService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailSender emailSender;

    @Override
    public Users findByUsername(String username) {
        return usersRepo.findByUsername(username);
    }

    @Override
    public Boolean existByUsername(String username) {
        return usersRepo.existsByUsername(username);

    }

    @Override
    public Boolean existByEmail(String email) {
        return usersRepo.existsByEmail(email);
    }

    @Override
    public Users saveOrUpdate(Users users) {
        return usersRepo.save(users);
    }


    public ResponseEntity<?> register(SignupRequest signupRequest) {


        if (userService.existByUsername(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username đã tồn tại"));
        }
        if (userService.existByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email đã tồn tại"));
        }


        Users users = new Users();
        users.setUsername(signupRequest.getUsername());
        users.setEmail(signupRequest.getEmail());
        users.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        users.setStatus(signupRequest.getStatus());
        users.setPhone(signupRequest.getPhone());
        users.setCreated(signupRequest.getCreated());
        Set<String> strSet = signupRequest.getRole();
        Set<Role> listRole = new HashSet<>();


        if (strSet == null) {
            Role userRole = roleService.findByRoleName(UserRole.ROLE_USER).orElseThrow(() -> new RuntimeException("Role is not found"));
            listRole.add(userRole);
        } else {
            strSet.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleService.findByRoleName(UserRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Role is not found"));
                        listRole.add(adminRole);
                    case "user":
                        Role userRole = roleService.findByRoleName(UserRole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Role is not found"));
                        listRole.add(userRole);
                }
            });
        }
        users.setRoles(listRole);
        userService.saveOrUpdate(users);
        String token = UUID.randomUUID().toString();

        Tokens confirmationToken = new Tokens();
        confirmationToken.setToken(token);
        confirmationToken.setCreatedAt(LocalDateTime.now());
        confirmationToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        confirmationToken.setUsers(users);
        tokenService.saveToken(confirmationToken);
        // TODO: Gửi mail để xác nhận
        System.out.println(token);
        String link = "http://localhost:8080/api/auth/confirm?token=" + token;
        emailSender.send(
                signupRequest.getEmail(),
                buildEmail(signupRequest.getUsername(), link));
        return ResponseEntity.badRequest().body(new MessageResponse(token));

    }

    @Transactional
    public String confirmToken(String token) {
        Tokens confirmationToken = tokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        tokenService.setConfirmedAt(token);
        usersRepo.statusUsername(
                confirmationToken.getUsers().getUsername());
        return "Xác nhận thành công";
    }

    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Xác nhận email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Cảm ơn bạn đã đăng ký. Xin hãy click vào đường link dưới đây để kích hoạt tài khoản của bạn: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link sẽ mất trong 15 phút tới. <p>Hẹn sớm gặp lại</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
}
