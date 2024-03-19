package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DebetcardTest {
    private WebDriver driver;

    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldEnterInvalidName() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] .input__control")).sendKeys("Ivanov Ivan");
        driver.findElement(By.cssSelector("[data-test-id=phone] .input__control")).sendKeys("+79173352555");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.tagName("button")).click();

        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();

        assertEquals(expected, actual);
    }

    @Test
    void shouldEnterInvalidPhone() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] .input__control")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] .input__control")).sendKeys("89173352555");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.tagName("button")).click();

        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=phone] .input__sub")).getText().trim();

        assertEquals(expected, actual);
    }

    @Test
    void shouldLeaveNameEmpty() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] .input__control")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id=phone] .input__control")).sendKeys("89173352555");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.tagName("button")).click();

        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id=name] .input__sub")).getText().trim();

        assertEquals(expected, actual);
    }

    @Test
    void shouldLeavePhoneEmpty() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] .input__control")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] .input__control")).sendKeys("");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.tagName("button")).click();

        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id=name] .input__sub")).getText().trim();

        assertEquals(expected, actual);
    }

    @Test
    void shouldEnterValidData() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] .input__control")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] .input__control")).sendKeys("+79173352555");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.tagName("button")).click();

        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.className("paragraph")).getText().trim();

        assertEquals(expected, actual);
    }

    @Test
    void shouldNotClickCheckbox() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] .input__control")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] .input__control")).sendKeys("+79173352555");
        driver.findElement(By.tagName("button")).click();

        String expected = "Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";
        String actual = driver.findElement(By.cssSelector("[data-test-id=agreement].input_invalid .checkbox__text")).getText().trim();

        assertEquals(expected, actual);
    }
}
