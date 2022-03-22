package bartelloni.it.tenaxscraper;

import bartelloni.it.tenaxscraper.scraping.TenaxEventsFacade;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TenaxEventsFacadeTest {

    @Test
    void areThereNewEvents() {
        System.out.println(new TenaxEventsFacade().getNewEvents());
    }
}