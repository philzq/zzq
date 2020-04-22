package zzq.springboot.sonarqubejacocojmockit.test;

import lombok.Setter;

public class LoginService {

    @Setter
    private LoginDao loginDao;

    private String currentUser;

    public boolean login(UserForm userForm){
        System.out.println(this.getClass().getTypeName() + ".login is called actually.");
        assert null != userForm;
        int loginReturnValue = loginDao.login(userForm);
        switch (loginReturnValue) {
            case 1:
                System.out.println("Login successfully.");
                return true;
            default:
                System.out.println("Login fail.");
                return false;
        }
    }

    public void setCurrentUser(String currentUser){
        System.out.println(this.getClass().getTypeName() + ".setCurrentUser is called actually.");
        if(null != currentUser){
            this.currentUser = currentUser;
        }
    }
}
