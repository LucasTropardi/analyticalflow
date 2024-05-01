package br.com.ltsoftwaresupport.analyticalflow.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.authentication.AnonymousAuthenticationToken;

import br.com.ltsoftwaresupport.analyticalflow.model.Role;
import br.com.ltsoftwaresupport.analyticalflow.model.User;
import br.com.ltsoftwaresupport.analyticalflow.repository.UserRepository;

@Component
public class SecurityService {

    private static final String LOGOUT_SUCCESS_URL = "/";

    @Autowired
    private UserRepository userRepository;

    private Optional<Authentication> getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        return Optional.ofNullable(context.getAuthentication())
                .filter(authentication -> !(authentication instanceof AnonymousAuthenticationToken));
    }

    public Optional<User> getCurrentUser() {
        Optional<Authentication> authentication = getAuthentication();

        return authentication.flatMap(value -> userRepository.findById(value.getName()));
    }

    public boolean isUserAuthenticated() {
        Optional<Authentication> authentication = getAuthentication();
        return authentication.isPresent() && !(authentication.get() instanceof AnonymousAuthenticationToken);
    }

    public boolean isUserAdmin() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && getCurrentUser().isPresent()) {
            return getCurrentUser().get().getRole().equals(Role.ADMIN);
        }

        return false;
    }

    public void logout() {
        UI.getCurrent().getPage().setLocation(LOGOUT_SUCCESS_URL);
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(VaadinServletRequest.getCurrent().getHttpServletRequest(), null, null);
    }

    public User getCurrentUserOrNull() {
        return getAuthentication()
            .map(Authentication::getName)
            .flatMap(userRepository::findByUsername)
            .orElse(null);
    }

}