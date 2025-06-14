package org.example.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.example.model.enums.FacilityType;
import org.hibernate.annotations.BatchSize;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "facility")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Facility implements Exportable {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "cost", precision = 10, scale = 2)
    private BigDecimal cost;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "facility_type")
    private FacilityType facilityType;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @BatchSize(size = 20)
    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FacilityOrder> facilityOrderList = new ArrayList<>();

    public void addFacilityOrder(FacilityOrder facilityOrder) {
        this.facilityOrderList.add(facilityOrder);
        facilityOrder.setFacility(this);
    }

    @Override
    public String toCsvString() {
        return cost + "," +
                facilityType;
    }

    @Override
    public String csvHeader() {
        return "cost,facility_type";
    }
}
