package bartelloni.it.personal.bots;

import bartelloni.it.personal.bots.tenax.TenaxEventsFacade;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TenaxEventsFacadeTest {

    @Test
    void areThereNewEvents() {
        System.out.println(new TenaxEventsFacade().getNewEvents());
    }
}