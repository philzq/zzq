package zzq.springboot.sonarqubejacocojmockit.test;

public class LoginController {

    public LoginService loginService;

    public String login(UserForm userForm){
        System.out.println(this.getClass().getTypeName() + ".login is called actually.");
        if(null == userForm){
            return "Please enter the user information.";
        }

        boolean isLoginSuccess;

        try{
            isLoginSuccess = loginService.login(userForm);
        }catch(Exception e){
            return "ERROR";
        }

        if(isLoginSuccess){
            loginService.setCurrentUser(userForm.getUsername());
            return "SUCCESS";
        }else{
            return "FAIL";
        }
    }
}
