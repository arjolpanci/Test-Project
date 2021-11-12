package com.arjolpanci.restservice.dbmodels;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Date;

@Entity(name="flight_request")
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "flight_id"})
})
@Data
public class FlightRequest {

    public static final String REQUEST_STATUS_PENDING = "pending";
    public static final String REQUEST_STATUS_REJECTED = "rejected";
    public static final String REQUEST_STATUS_ACCEPTED = "accepted";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @CreationTimestamp
    private Date created_at;

    @Column private String status;
    @Column private String rejection_reason;

    @OneToOne
    private User user;

    @OneToOne
    private Flight flight;
}
