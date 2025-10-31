package ru.netology.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.test.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.test.DataGenerator.Registration.getUser;
import static ru.netology.test.DataGenerator.getRandomLogin;
import static ru.netology.test.DataGenerator.getRandomPassword;


public class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $("h2").shouldHave(Condition.exactText("  Личный кабинет"));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("[data-test-id='login'] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id='password'] input").setValue(notRegisteredUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $("[data-test-id='error-notification']").shouldBe(visible).shouldHave(exactText("Ошибка\n" +
                "Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $("[data-test-id='error-notification']").shouldBe(visible).shouldHave(exactText("Ошибка\n" +
                "Ошибка! Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id='login'] input").setValue(wrongLogin);
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $("[data-test-id='error-notification']").shouldBe(visible).shouldHave(exactText("Ошибка\n" +
                "Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(wrongPassword);
        $$("button").find(exactText("Продолжить")).click();
        $("[data-test-id='error-notification']").shouldBe(visible).shouldHave(exactText("Ошибка\n" +
                "Ошибка! Неверно указан логин или пароль"));
    }

}
