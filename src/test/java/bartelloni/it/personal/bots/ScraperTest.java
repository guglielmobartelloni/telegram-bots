package bartelloni.it.personal.bots;

import bartelloni.it.personal.bots.tenax.Scraper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ScraperTest {


    @Test
    void testGetEvents() {
        try (Scraper scraper = new Scraper()) {
            assertThat(scraper.getEvents()).isNotEmpty();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void writeEventsToFile() {
        try (Scraper scraper = new Scraper()) {
            scraper.writeEventsToFile(scraper.getEvents());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void readFromFile(){
        try (Scraper scraper = new Scraper()) {
            assertThat(scraper.getEventsFromFile()).isNotEmpty();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}