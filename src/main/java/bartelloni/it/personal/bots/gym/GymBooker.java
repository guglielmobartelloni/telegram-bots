package bartelloni.it.personal.bots.gym;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.Cookie;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class GymBooker {
    private WebClient webClient;
    private String username;
    private String password;


    public GymBooker(@Value("${username}") String username, @Value("${password}") String password) {
        this.username = username;
        this.password = password;
        webClient = new WebClient();
        final WebClientOptions options = webClient.getOptions();
//        options.setJavaScriptEnabled(false);
        options.setCssEnabled(false);

    }


    public void book(Timestamp timestamp) throws IOException {
        final HtmlPage loginPage = webClient.getPage("https://ecomm.sportrick.com/palestraunivers/Account/Login?returnUrl=%2Fpalestraunivers");
        final HtmlForm loginForm = loginPage.querySelector("body > div.content > form.login-form");
        final HtmlInput userNameInput = loginForm.getInputByName("UserName");
        final HtmlInput passwordInput = loginForm.getInputByName("Password");
        final HtmlButton submitInput = loginPage.querySelector("body > div.content > form.login-form > div.form-actions > button");
        userNameInput.type(username);
        passwordInput.type(password);
        final HtmlPage afterLoginPage = submitInput.click();
        final DomElement bookingPageButton = afterLoginPage.querySelector("#home_btt_booking > div > a");
        final HtmlPage newBookingPage = bookingPageButton.click();
        final DomElement newBookingButton = newBookingPage.querySelector("body > div.page-container > div.page-content-wrapper > div > div.row > div > a");
        final HtmlPage gymChoicePage = newBookingButton.click();
        final HtmlElement salaPesiButton = gymChoicePage.querySelector("body > div.page-container > div.page-content-wrapper > div > div.row > div > form > div > a:nth-child(3)");
        final HtmlPage bookingPage = salaPesiButton.click();
        final Set<Cookie> cookies = webClient.getCookieManager().getCookies();
        String formattedCoockies = cookies.stream().map(e -> e.getName() + "=" + e.getValue()).collect(Collectors.joining(";"));
        String activityId = bookingPage.getBaseURI().substring("https://ecomm.sportrick.com/palestraunivers/Booking/BookActivity?activityid=".length()).substring(0, 18);


        HttpResponse<Appointments> response = Unirest.post("https://ecomm.sportrick.com/palestraunivers/Booking/GetActivitySchedule")
                .header("Connection", "keep-alive")
                .header("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"")
                .header("DNT", "1")
                .header("sec-ch-ua-mobile", "?0")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.83 Safari/537.36")
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("Accept", "*/*")
                .header("X-Requested-With", "XMLHttpRequest")
                .header("sec-ch-ua-platform", "\"macOS\"")
                .header("Origin", "https://ecomm.sportrick.com")
                .header("Sec-Fetch-Site", "same-origin")
                .header("Sec-Fetch-Mode", "cors")
                .header("Sec-Fetch-Dest", "empty")
                .header("Accept-Language", "en-US,en;q=0.9,it;q=0.8,ja;q=0.7")
                .header("Cookie", formattedCoockies)
                .header("sec-gpc", "1")
                .body("currentDate=" + new SimpleDateFormat("yyyy-MM-dd").format(timestamp) + "&activityID=" + activityId + "&skillID=0&activityFlags=Classes&getForCatchUp=false")
                .asObject(Appointments.class);

        final Appointments responseBody = response.getBody();
        final List<Datum> appointmentsList = responseBody.getData();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        final Datum appointment = appointmentsList.stream().filter(e -> {
            try {
                return dateFormat.parse(e.getStartDateTime()).equals(timestamp);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
            return false;
        }).findFirst().orElseThrow();
        log.info("Appointment: {}", appointment);

        HttpResponse<String> bookingResponse = Unirest.post("https://ecomm.sportrick.com/palestraunivers/Booking/BookAppointment")
                .header("Connection", "keep-alive")
                .header("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"99\", \"Google Chrome\";v=\"99\"")
                .header("DNT", "1")
                .header("sec-ch-ua-mobile", "?0")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.83 Safari/537.36")
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("Accept", "*/*")
                .header("X-Requested-With", "XMLHttpRequest")
                .header("sec-ch-ua-platform", "\"macOS\"")
                .header("Origin", "https://ecomm.sportrick.com")
                .header("Sec-Fetch-Site", "same-origin")
                .header("Sec-Fetch-Mode", "cors")
                .header("Sec-Fetch-Dest", "empty")
                .header("Accept-Language", "en-US,en;q=0.9,it;q=0.8,ja;q=0.7")
                .header("Cookie", formattedCoockies)
                .header("sec-gpc", "1")
                .body("appointmentID=" + appointment.getAppointmentId())
                .asString();

        log.info("Booking response: {}", bookingResponse.getBody());

    }

    public static Map<String, String> getQueryMap(String query) {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<String, String>();

        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }
}
