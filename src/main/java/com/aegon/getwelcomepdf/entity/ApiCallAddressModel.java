package com.aegon.getwelcomepdf.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "API_CALL_ADDRESS")
@Table(name = "api_call_address")
public class ApiCallAddressModel implements Serializable {
    private static final long serialVersionUID = 129787438843297736L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cdApiLogId;
    private String chFullAddressText;
    private Integer cdCityId;
    private String chTown;
    private String chDistrict;
    private Date dtCreateDate;
    private String chQuarter;
    private Integer cdIsThereError;
    private Long cdTc;
    private String chCityName;
}
