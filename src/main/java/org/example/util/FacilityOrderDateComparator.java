package org.example.util;

import org.example.model.domain.FacilityOrder;

import java.util.Comparator;

public class FacilityOrderDateComparator implements Comparator<FacilityOrder> {
    @Override
    public int compare(FacilityOrder o1, FacilityOrder o2) {
        return o1.getDate().compareTo(o2.getDate());
    }
}
