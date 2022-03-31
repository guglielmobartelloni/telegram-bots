package bartelloni.it.personal.bots.tenax;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TenaxEvent implements Serializable {
    private String title;
    private String link;
    private String imageLink;
    private String eventDate;
}
