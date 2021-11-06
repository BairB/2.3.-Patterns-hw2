package ru.netology.domain;

import lombok.val;
import lombok.var;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.domain.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.domain.DataGenerator.Registration.getUser;
import static ru.netology.domain.DataGenerator.randomLogin;
import static ru.netology.domain.DataGenerator.randomPassword;


public class IbankTest {

    @Test
    void shouldRequestWithValidLogin() {
        open("http://localhost:9999");
        val registeredUser = getRegisteredUser("active");
        $("[data-test-id = 'login'] .input__control").sendKeys(registeredUser.getLogin());
        $("[data-test-id = 'password'] .input__control").sendKeys(registeredUser.getPassword());
        $("[data-test-id = 'action-login'] .button__text").click();
        $(withText("Личный кабинет")).shouldBe(visible);
    }

    @Test
    void shouldGetErrorIfNotRegisteredUser() {
        open("http://localhost:9999");
        val notRegisteredUser = getUser("active");
        $("[data-test-id = 'login'] .input__control").sendKeys(notRegisteredUser.getLogin());
        $("[data-test-id = 'password'] .input__control").sendKeys(notRegisteredUser.getPassword());
        $("[data-test-id = 'action-login'] .button__text").click();
        $("[data-test-id = 'error-notification'").shouldBe(visible)
                .shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    void shouldGetErrorIfBlockedUser() {
        open("http://localhost:9999");
        val blockedUser = getRegisteredUser("blocked");
        $("[data-test-id = 'login'] .input__control").sendKeys(blockedUser.getLogin());
        $("[data-test-id = 'password'] .input__control").sendKeys(blockedUser.getPassword());
        $("[data-test-id = 'action-login'] .button__text").click();
        $("[data-test-id = 'error-notification'").shouldBe(visible)
                .shouldHave(text("Ошибка! Пользователь заблокирован"));
    }

    @Test
    void shouldGetErrorIfWrongLogin() {
        open("http://localhost:9999");
        val registeredUser = getRegisteredUser("active");
        $("[data-test-id = 'login'] .input__control").sendKeys(randomLogin());
        $("[data-test-id = 'password'] .input__control").sendKeys(registeredUser.getPassword());
        $("[data-test-id = 'action-login'] .button__text").click();
        $("[data-test-id = 'error-notification'").shouldBe(visible)
                .shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    void shouldGetErrorIfWrongPassword() {
        open("http://localhost:9999");
        val registeredUser = getRegisteredUser("active");
        $("[data-test-id = 'login'] .input__control").sendKeys(registeredUser.getLogin());
        $("[data-test-id = 'password'] .input__control").sendKeys(randomPassword());
        $("[data-test-id = 'action-login'] .button__text").click();
        $("[data-test-id = 'error-notification'").shouldBe(visible)
                .shouldHave(text("Неверно указан логин или пароль"));
    }
}
