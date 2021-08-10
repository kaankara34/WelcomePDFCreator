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
@Entity(name = "WELCOME_CALL_ADDRESS")
@Table(name = "welcome_call_address")
public class WelcomeCallAddressModel implements Serializable {
    private static final long serialVersionUID = 256437438843297716L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cdDeliveryLogId;
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
