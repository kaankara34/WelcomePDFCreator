package com.aegon.getwelcomepdf.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "EDELIVERY_PDF_COORDINATES")
@Table(name="edelivery_pdf_coordinates")
public class EDeliveryPDFCoordinatesModel implements Serializable {
    private static final long serialVersionUID = 248684231843297756L;

    private String productName;

    private Float xcoordinate;
    private Float ycoordinate;
    @Id
    private String areaname;
    private Integer isvalid;
    private String project;
    private Integer font;
    private Integer pageNum;
    private Integer red;
    private Integer green;
    private Integer blue;
}
