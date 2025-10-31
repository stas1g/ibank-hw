package ru.netology.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class AuthTest {

    @BeforeEach
    void setup() {
        // Открываем страницу авторизации
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        // Создаем и регистрируем активного пользователя через API
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");

        // Заполняем форму авторизации
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();

        // Проверяем успешный вход
        $("h2").shouldHave(Condition.exactText("Личный кабинет"));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        // Создаем НЕзарегистрированного пользователя
        var notRegisteredUser = DataGenerator.Registration.getUser("active");

        $("[data-test-id='login'] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id='password'] input").setValue(notRegisteredUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();

        $("[data-test-id='error-notification']").shouldBe(visible).shouldHave(exactText("Ошибка\n" +
                "Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        // Создаем и регистрируем заблокированного пользователя
        var blockedUser = DataGenerator.Registration.getRegisteredUser("blocked");

        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();

        $("[data-test-id='error-notification']").shouldBe(visible).shouldHave(exactText("Ошибка\n" +
                "Ошибка! Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        // Создаем зарегистрированного пользователя
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        // Используем неправильный логин
        var wrongLogin = DataGenerator.getRandomLogin();

        $("[data-test-id='login'] input").setValue(wrongLogin);
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();

        $("[data-test-id='error-notification']").shouldBe(visible).shouldHave(exactText("Ошибка\n" +
                "Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        // Создаем зарегистрированного пользователя
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        // Используем неправильный пароль
        var wrongPassword = DataGenerator.getRandomPassword();

        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(wrongPassword);
        $$("button").find(exactText("Продолжить")).click();

        $("[data-test-id='error-notification']").shouldBe(visible).shouldHave(exactText("Ошибка\n" +
                "Ошибка! Неверно указан логин или пароль"));
    }
}