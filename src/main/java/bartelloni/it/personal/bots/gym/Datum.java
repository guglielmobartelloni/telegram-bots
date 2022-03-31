
package bartelloni.it.personal.bots.gym;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.ToString;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
@ToString
@Data
public class Datum {

    @SerializedName("AppointmentId")
    @Expose
    private String appointmentId;
    @SerializedName("ParentCourseId")
    @Expose
    private String parentCourseId;
    @SerializedName("SkillName")
    @Expose
    private Object skillName;
    @SerializedName("Booking360Code")
    @Expose
    private Object booking360Code;
    @SerializedName("AvailabilitiesAlmostOver")
    @Expose
    private Boolean availabilitiesAlmostOver;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("TimeDescription")
    @Expose
    private String timeDescription;
    @SerializedName("EventName")
    @Expose
    private String eventName;
    @SerializedName("EventDescription")
    @Expose
    private String eventDescription;
    @SerializedName("EventDescription2")
    @Expose
    private Object eventDescription2;
    @SerializedName("EventDescription3")
    @Expose
    private String eventDescription3;
    @SerializedName("Notes")
    @Expose
    private String notes;
    @SerializedName("StartDateTime")
    @Expose
    private String startDateTime;
    @SerializedName("EndDateTime")
    @Expose
    private String endDateTime;
    @SerializedName("DurationMinutes")
    @Expose
    private Integer durationMinutes;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Icon")
    @Expose
    private String icon;
}
