package org.example.util;

import org.example.model.domain.Visitor;

import java.util.Comparator;

public class VisitorNameComparator implements Comparator<Visitor> {
    @Override
    public int compare(Visitor o1, Visitor o2) {
        return (o1.getName() + o1.getSurname()).compareTo(o2.getName() + o2.getSurname());
    }
}
