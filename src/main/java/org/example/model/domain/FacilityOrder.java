package org.example.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.time.LocalDateTime;

@Entity
@Table(name = "facilityorder")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacilityOrder implements Exportable {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "facility_id")
    private Facility facility;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "visitor_id")
    private Visitor visitor;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "date")
    private LocalDateTime date;

    @Override
    public String toCsvString() {
        return facility.getId() + "," +
                visitor.getId() + "," +
                date.toLocalDate();
    }

    @Override
    public String csvHeader() {
        return "facility_id,visitor_id,date";
    }
}
