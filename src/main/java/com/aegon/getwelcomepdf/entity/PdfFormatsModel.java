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
@Entity(name = "PDF_FORMAT")
@Table(name="pdf_format")
public class PdfFormatsModel implements Serializable {
    private static final long serialVersionUID = 248687433213297736L;
    @Id
    private Long id;
    private String branchCode;
    private String tarifeNo;
    private String makbuzPdfFormat;
    private String welcomePdfFormat;
    private Integer isValid;
    private Date createTime;
    private Date startDate;
    private Date  endDate;
    private String makbuzHtmlFormat;
    private String productName;
    private String infografikkodMail;
    private String infografikkodPosta;
    private String docVersionNo;
    private String status;
    private String substatus;
    private String welcomeUlasilamayanFormat;
    private String welcomeMailFormatUlasilan;
    private String welcomemailformatUlasilamayan;
    private String beforeCancellationFormat;
    private String portaServWelcomeFormat;
private String welcomecallPolicyFormat;

}
