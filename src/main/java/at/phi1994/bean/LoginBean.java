package at.phi1994.bean;

import at.phi1994.model.User;
import at.phi1994.service.UserService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.Serializable;

@Named("login")
@RequestScoped
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 8146813023784381429L;

    @Inject
    private UserService userService;

    private String username;
    private String password;

    public void login() throws IOException {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext eCtx = ctx.getExternalContext();

        User user = userService.findByUsernameAndPassword(username, password);

        if (user == null) {
            eCtx.redirect("login.xhtml");
            return;
        }

        HttpSession session = (HttpSession) eCtx.getSession(true);
        session.setAttribute("username", user.getUsername());
        eCtx.redirect("dashboard.xhtml");
    }

    public void logout() throws IOException {
        ExternalContext eCtx = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) eCtx.getSession(false);
        session.invalidate();
        eCtx.redirect("home.xhtml");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
