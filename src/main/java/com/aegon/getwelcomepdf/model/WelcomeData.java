package com.aegon.getwelcomepdf.model;

import lombok.Data;

import java.util.Date;

@Data
public class WelcomeData {

    public WelcomeData() {
    }


    private boolean isThereError;
    private Long polid;
    private String sigortaEttirenTck;
    private String sigortaEttirenAd;
    private String sigortaEttirenSoyad;
    private Long sigortaEttirenPid;
    private String sigortaliTck;
    private String sigortaliAd;
    private String sigortaliSoyad;
    private String policeNo;
    private String aciklama;
    private String urunTip;
    private String tahsTip;
    private String tarifeNo;
    private String urunAdi;
    private String bransKod;
    private String email;
    private Long tahsTutar;
    private String odeTip;
    private String odeFrekans;
    private String hataMes;
    private Date polBasTar;
    private Long polDuration;
    private String basvuruNo;
    private Date polBitTar;
    private String outSatisKanali;
    private Double yillikPrim;
    private Double oran;
    private Date caymaTar;
    private String teminatlar;
    private Long hktdmTutar;
    private String anaTeminatIsmi;
    private Long faizOran;
    private Long geriodemeyil;
    private String adres;
    private String adresIl;
    private String adresIlce;
    private Date dogTar;
    private String dovKod;
    private String bonoNo;
    private String bonovadetar;
    private Long sonZeylNo;
    private Double dovKur;
    private Long dovTutar;
    private Long aylikPrim;
    private Double firstPremium;


}
