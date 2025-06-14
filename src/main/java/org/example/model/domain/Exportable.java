package org.example.model.domain;

public interface Exportable {
    String toCsvString();
    String csvHeader();
}
