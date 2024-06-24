package org.personal.solarpanel.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(schema = "public",name = "solar_panel")
public class SolarPanel extends BaseEntity {

    @Column(name = "category")
    private String category;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "serial_number")
    private String serialNumber;

    public SolarPanel(String category, String manufacturer, String serialNumber) {
        this.category = category;
        this.manufacturer = manufacturer;
        this.serialNumber = serialNumber;
    }
}
