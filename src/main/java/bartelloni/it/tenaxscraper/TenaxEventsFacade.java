package bartelloni.it.tenaxscraper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TenaxEventsFacade {
    public Collection<TenaxEvent> getNewEvents() {
        try (Scraper scraper = new Scraper()) {
            final List<TenaxEvent> scrapedEvents = scraper.getEvents();
            final List<TenaxEvent> savedEvents = scraper.getEventsFromFile();
            scrapedEvents.removeAll(savedEvents);

            //Save scraped events to file
            scraper.writeEventsToFile(scrapedEvents);

            return scrapedEvents;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}
