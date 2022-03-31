package bartelloni.it.personal.bots.tenax;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Scraper implements AutoCloseable {

    private WebClient webClient;

    public Scraper() {
        this.webClient = new WebClient();
        final WebClientOptions options = webClient.getOptions();
        options.setJavaScriptEnabled(false);
        options.setCssEnabled(false);
    }

    public List<TenaxEvent> getEvents() throws IOException {
        final HtmlPage page = webClient.getPage("https://tenax.org/eventi/");

        final List<DomNode> events = page.getByXPath("//*[@id=\"dhrk-loop\"]/div");

        List<TenaxEvent> tenaxEventsList = new ArrayList<>();
        for (DomNode event : events) {
            final DomNode eventDetail = event.querySelector("div > div.vice-post__content > div.vice-post--event__contents.vice-3d-element__front > h2 > a");
            final String eventTitle = eventDetail.asNormalizedText();

            final HtmlAnchor anchor = (HtmlAnchor) eventDetail;
            String eventLink = anchor.getHrefAttribute();

            final HtmlImage eventImage = event.querySelector("div > div.vice-post__thumbnail > a > img");
            String eventImageLink = eventImage.getSrc();

            final String eventDay = event.querySelector("div > div.vice-post__content > div.vice-post--event__date > h3").asNormalizedText();
            final String eventMonthAndYear = event.querySelector("div > div.vice-post__content > div.vice-post--event__date > h5").asNormalizedText();

            String eventDate = eventDay + " " + eventMonthAndYear;

            final TenaxEvent tenaxEvent = TenaxEvent.builder()
                    .title(eventTitle)
                    .link(eventLink)
                    .imageLink(eventImageLink)
                    .eventDate(eventDate)
                    .build();
            tenaxEventsList.add(tenaxEvent);
        }
        tenaxEventsList.stream().map(TenaxEvent::toString).forEach(log::info);
        return tenaxEventsList;

    }


    public void writeEventsToFile(List<TenaxEvent> eventList) {
        try (FileOutputStream fos = new FileOutputStream("events.dat");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            for (TenaxEvent tenaxEvent : eventList) {
                oos.writeObject(tenaxEvent);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public List<TenaxEvent> getEventsFromFile() {
        List<TenaxEvent> eventList = new ArrayList<>();
        try (FileInputStream fi = new FileInputStream("events.dat");
             ObjectInputStream oi = new ObjectInputStream(fi);) {
            Object event = oi.readObject();
            for (; event != null; event = oi.readObject()) {
                eventList.add((TenaxEvent) event);
            }
        } catch (EOFException ex) {

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return eventList;
    }

    @Override
    public void close() throws Exception {
        webClient.close();
    }
}
