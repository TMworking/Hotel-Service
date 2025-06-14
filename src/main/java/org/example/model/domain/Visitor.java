package org.example.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "visitor")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Visitor implements Exportable {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "name", length = 50)
    private String name;

    @NonNull
    @Column(name = "surname", length = 50)
    private String surname;

    @NonNull
    @Column(name = "passport", unique = true, length = 50)
    private String passport;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @BatchSize(size = 20)
    @OneToMany(mappedBy = "visitor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookingList = new ArrayList<>();

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @BatchSize(size = 20)
    @OneToMany(mappedBy = "visitor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FacilityOrder> facilityOrderList = new ArrayList<>();

    public void addBooking(Booking booking) {
        this.bookingList.add(booking);
        booking.setVisitor(this);
    }

    public void addFacilityOrder(FacilityOrder facilityOrder) {
        this.facilityOrderList.add(facilityOrder);
        facilityOrder.setVisitor(this);
    }

    @Override
    public String toCsvString() {
        return name + "," +
                surname + "," +
                passport;
    }

    @Override
    public String csvHeader() {
        return "name,surname,passport";
    }
}
