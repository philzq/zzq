package zzq.springboot.sonarqubejacocojmockit.test;

import mockit.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LoginControllerTest {

    @Injectable
    private LoginDao loginDao;
    @Injectable
    private LoginService loginService;
    @Tested
    private LoginController loginController;

    @Before
    public void setup() {
        loginController = new LoginController();
    }

    @Test
    public void assertThatNoMethodHasBeenCalled() {
        loginController.login(null);
        new FullVerifications(loginService) {
        };
    }

    @Test
    public void mockupTest() {

        new MockUp<LoginService>(){
            @Mock
            public boolean login(UserForm userForm){
                return false;
            }
        };
    }

    @Test
    public void assertTwoMethodsHaveBeenCalled() {
        UserForm userForm = new UserForm();
        userForm.setUsername("foo");
        new Expectations() {{
            loginService.login(userForm);
            result = true;
            loginService.setCurrentUser("foo");
        }};

        String login = loginController.login(userForm);

        Assert.assertEquals("SUCCESS", login);
        new FullVerifications(loginService) {
        };
    }

    @Test
    public void assertOnlyOneMethodHasBeenCalled() {
        UserForm userForm = new UserForm();
        userForm.setUsername("foo");
        new Expectations() {{
            loginService.login(userForm);
            result = false;
            // no expectation for setCurrentUser
        }};

        String login = loginController.login(userForm);

        Assert.assertEquals("FAIL", login);
        new FullVerifications(loginService) {
        };
    }

    @Test
    public void mockExceptionThrowing() {
        UserForm userForm = new UserForm();
        new Expectations() {{
            loginService.login(userForm);
            result = new IllegalArgumentException();
            // no expectation for setCurrentUser
        }};

        String login = loginController.login(userForm);

        Assert.assertEquals("ERROR", login);
        new FullVerifications(loginService) {
        };
    }

    @Test
    public void mockAnObjectToPassAround(@Mocked UserForm userForm) {
        new Expectations() {{
            userForm.getUsername();
            result = "foo";
            loginService.login(userForm);
            result = true;
            loginService.setCurrentUser("foo");
        }};

        String login = loginController.login(userForm);

        Assert.assertEquals("SUCCESS", login);
        new FullVerifications(loginService) {
        };
        new FullVerifications(userForm) {
        };
    }

//    @Test
//    public void argumentMatching() {
//        UserForm userForm = new UserForm();
//        userForm.setUsername("foo");
//        // default matcher
//        new Expectations() {{
//            loginService.login((UserForm) any);
//            result = true;
//            // complex matcher
//            loginService.setCurrentUser(withArgThat(new BaseMatcher<String>() {
//                @Override
//                public boolean matches(Object item) {
//                    return item instanceof String && ((String) item).startsWith("foo");
//                }
//
//                @Override
//                public void describeTo(Description description) {
//                    //NOOP
//                }
//            }));
//        }};
//
//        String login = loginController.login(userForm);
//
//        Assert.assertEquals("OK", login);
//        new FullVerifications(loginService) {
//        };
//    }

    /**
     * Partial mocking with JMockit is especially easy.
     * Every method call for which no mocked behavior has been defined in an Expectations(){{}}
     * uses the “real” implementation.
     *
     * In this case as no expectation is given for LoginService.
     * login(UserForm) the actual implementation (and the call to LoginDAO.login(UserForm)) is performed.
     */
    @Test
    public void partialMocking() {
        // use partial mock
        LoginService partialLoginService = new LoginService();
        partialLoginService.setLoginDao(loginDao);
        loginController.loginService = partialLoginService;

        UserForm userForm = new UserForm();
        userForm.setUsername("foo");
        // let service's login use implementation so let's mock DAO call
        new Expectations() {{
            loginDao.login(userForm);
            result = 1;
            // no expectation for loginService.login
            partialLoginService.setCurrentUser("foo");
        }};

        String login = loginController.login(userForm);

        Assert.assertEquals("SUCCESS", login);
        // verify mocked call
        new FullVerifications(partialLoginService) {
        };
        new FullVerifications(loginDao) {
        };
    }

}
