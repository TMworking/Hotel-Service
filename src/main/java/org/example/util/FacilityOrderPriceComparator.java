package org.example.util;

import org.example.model.domain.FacilityOrder;

import java.util.Comparator;

public class FacilityOrderPriceComparator implements Comparator<FacilityOrder> {
    @Override
    public int compare(FacilityOrder o1, FacilityOrder o2) {
        return o1.getFacility().getCost().compareTo(o2.getFacility().getCost());
    }
}
