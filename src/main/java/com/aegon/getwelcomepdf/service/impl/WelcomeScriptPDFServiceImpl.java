package com.aegon.getwelcomepdf.service.impl;

import com.aegon.getwelcomepdf.entity.*;
import com.aegon.getwelcomepdf.model.GetWelcomeScriptRequest;
import com.aegon.getwelcomepdf.model.GetWelcomeScriptResponse;
import com.aegon.getwelcomepdf.model.SbmAddressResponse;
import com.aegon.getwelcomepdf.model.WelcomeData;
import com.aegon.getwelcomepdf.repository.*;
import com.aegon.getwelcomepdf.service.WelcomeScriptPDFService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.tempuri.Service1Soap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class WelcomeScriptPDFServiceImpl implements WelcomeScriptPDFService {

    private static final String INVALID_POLID = "Geçersiz polid";
    private static final String SIGORTA_ETTIREN_BILGISI_NULL = "Welcome Sigorta Ettiren bilgisi null";
    private static final String CANNOT_ACCESS_TO_POLICE_BILGILERI = "Hata! Poliçe bilgileri getirilemedi.";
    private static final String PDF_FORMAT_NOT_FOUND = "Pdf Formatı bulunamadı";

    private final WelcomeCallInfoCustomRepository welcomeCallInfoCustomRepository;
    private final WelcomeCallSabitKurluCustomRepository welcomeCallSabitKurluCustomRepository;
    private final ApiDocumentLogRepository apiDocumentLogRepository;
    private final ApiLogRepository apiLogRepository;
    private final WelcomeCallAddressRepository welcomeCallAddressRepository;
    private final ApiCallAddressRepository apiCallAddressRepository;
    private final EDeliveryPDFCoordinatesRepository eDeliveryPDFCoordinatesRepository;
    private final PdfFormatsRepository pdfFormatsRepository;
    private final Service1Soap client;



    @Override
    public GetWelcomeScriptResponse getWelcomeScriptPDFParameters(GetWelcomeScriptRequest getWelcomeScriptRequest)
            throws Exception {


        GetWelcomeScriptResponse getWelcomeScriptResponse = new GetWelcomeScriptResponse();
        ApiLogModel apiLogModel = new ApiLogModel();
        long inp_polid = 0;
        apiLogModel.setInptPolid(inp_polid);
        try {
            apiLogModel.setInptAgentName(getWelcomeScriptRequest.getAgentName() + " / " + getWelcomeScriptRequest.getAgentSurname());
            apiLogModel.setInptGsmphone(getWelcomeScriptRequest.getGSMphone());
            apiLogModel.setLogTime(new Date());
            apiLogModel.setDetail("GetWelcomeScriptPDF");

            if (StringUtils.isEmpty(getWelcomeScriptRequest.getPolid())) {
                log.error("errorCode:1, isThereError:1, errorMessage:{}, returnTime:{}", INVALID_POLID, new Date());
                saveApiLog(apiLogModel,1,1,"Geçersiz polid");
                getWelcomeScriptResponse.setErrorText("Geçersiz polid");
                getWelcomeScriptResponse.setIsThereError(1);
                getWelcomeScriptResponse.setError_code(new Double("1"));
                return getWelcomeScriptResponse;
            }
            Map<String, Object> parametersList = welcomeCallInfoCustomRepository
                    .getWelcomeCallInfo(getWelcomeScriptRequest.getPolid());
            inp_polid = getWelcomeScriptRequest.getPolid();
            apiLogModel.setInptPolid(inp_polid);
            WelcomeData welPolItem = formatGetWelcomeCallInfo(parametersList, inp_polid, apiLogModel);
            String adresDB = welPolItem.getAdres();
            String adresIlDB = welPolItem.getAdresIl();
            welPolItem.setAdres("");
            welPolItem.setAdresIl("");
            if (welPolItem.isThereError() == false) {
                if (!StringUtils.isEmpty(welPolItem.getSigortaEttirenTck())) {
                    try {
                        Long TC = Long.parseLong(welPolItem.getSigortaEttirenTck());
                        List<WelcomeCallAddressModel> lstDeliveryAddress = welcomeCallAddressRepository.getAllByCdTc(TC);
                        List<ApiCallAddressModel> lstApiAddress = apiCallAddressRepository.getAllByCdTc(TC);
                        if (lstApiAddress.size() > 0) {
                            welPolItem.setAdres(lstApiAddress.stream().findFirst().orElse(null).getChFullAddressText());
                            welPolItem.setAdresIl(lstApiAddress.stream().findFirst().orElse(null).getChCityName());
                            welPolItem.setAdresIlce(lstApiAddress.stream().findFirst().orElse(null).getChTown());
                        } else if (lstDeliveryAddress.size() > 0) {
                            welPolItem.setAdres(lstDeliveryAddress.stream().findFirst().orElse(null).getChFullAddressText());
                            welPolItem.setAdresIl(lstDeliveryAddress.stream().findFirst().orElse(null).getChCityName());
                            welPolItem.setAdresIlce(lstDeliveryAddress.stream().findFirst().orElse(null).getChTown());

                        } else {
                            ApiCallAddressModel add = new ApiCallAddressModel();
                            add.setDtCreateDate(new Date());
                            add.setCdApiLogId(apiLogModel.getLogId());
                            add.setCdTc(TC);
                            SbmAddressResponse sbmAddressResponse = findAdressBySbmService(welPolItem.getSigortaEttirenTck());
                            welPolItem.setAdres(sbmAddressResponse.getAcikAdres());
                            if (StringUtils.isEmpty(welPolItem.getAdres())) {
                                add.setCdIsThereError(1);
                                apiCallAddressRepository.save(add);

                                if (!StringUtils.isEmpty(adresDB) && !StringUtils.isEmpty(adresIlDB)) {
                                    welPolItem.setAdres(adresDB);
                                    welPolItem.setAdresIl(adresIlDB);


                                }

                            } else {
                                add.setCdIsThereError(0);

                                welPolItem.setAdresIl(sbmAddressResponse.getIlAd());
                                add.setChCityName(welPolItem.getAdresIl());
                                add.setCdCityId(0);


                                try {
                                    welPolItem.setAdresIlce(sbmAddressResponse.getIlceAd());
                                    add.setChTown(welPolItem.getAdresIlce());
                                    String cityCode = sbmAddressResponse.getIlKodu();
                                    if (!StringUtils.isEmpty(cityCode)) {
                                        add.setCdCityId(Integer.parseInt(cityCode));
                                        add.setChQuarter(sbmAddressResponse.getMahalle());
                                        add.setChDistrict(sbmAddressResponse.getCsbm());

                                    }

                                } catch (Exception ex) {
                                    saveApiLog(apiLogModel,1,2,"Adres Bilgisine Ulaşılamadı");
                                    log.error("errorCode:2, isThereError:1, errorMessage:{}, returnTime:{}", SIGORTA_ETTIREN_BILGISI_NULL, new Date());
                                    getWelcomeScriptResponse.setIsThereError(1);
                                    getWelcomeScriptResponse.setErrorText("Welcome Sigorta Ettiren bilgisi null");
                                    getWelcomeScriptResponse.setError_code(new Double("2"));
                                    return getWelcomeScriptResponse;

                                }
                                apiCallAddressRepository.save(add);
                            }

                        }


                    } catch (Exception e) {
                        if (!StringUtils.isEmpty(adresDB) && !StringUtils.isEmpty(adresIlDB))
                            welPolItem.setAdres(adresDB);
                        welPolItem.setAdresIl(adresIlDB);
                    }

                } else {
                    saveApiLog(apiLogModel,1,2,"Welcome Data bilgisi sigEttirenTc bilgisi null, polid id:" + getWelcomeScriptRequest.getPolid());
                    log.error("errorCode:2, isThereError:1, errorMessage:{}, returnTime:{}", SIGORTA_ETTIREN_BILGISI_NULL, new Date());
                    getWelcomeScriptResponse.setIsThereError(1);
                    getWelcomeScriptResponse.setErrorText("Welcome Sigorta Ettiren bilgisi null");
                    getWelcomeScriptResponse.setError_code(new Double("2"));
                    return getWelcomeScriptResponse;
                }

            } else {
                log.error("errorCode:3, isThereError:1, errorMessage:{}, returnTime:{}", CANNOT_ACCESS_TO_POLICE_BILGILERI, new Date());
                getWelcomeScriptResponse.setErrorText("Hata! Poliçe bilgileri getirilemedi.");
                getWelcomeScriptResponse.setIsThereError(1);
                getWelcomeScriptResponse.setError_code(new Double("3"));
                return getWelcomeScriptResponse;

            }
            String readpath = String.valueOf(getClass().getResource("/pdf_templates"));
            String documentName = "";
            InetAddress ip = InetAddress.getLocalHost();
            String returnURL = "http://" + ip.getHostAddress() + "/EDelivery_API/GetWelcomeScriptPDF/";
            String projectName = "edelivery-WelcomeScriptServ";
            List<PdfFormatsModel> lstFORMAT = pdfFormatsRepository.getAllByBranchCodeAndTarifeNoAndIsValid(welPolItem.getBransKod(), welPolItem.getTarifeNo(), 1);
            if (lstFORMAT.size() > 0) {
                String readerDocName = lstFORMAT.stream().findFirst().orElse(null).getPortaServWelcomeFormat();
                if (readerDocName == null) {
                    saveApiLog(apiLogModel,1,4,"Geçersiz validTar,bransKod veya tarifeNo");
                    log.error("errorCode:4, isThereError:1, errorMessage:{}, returnTime:{}", PDF_FORMAT_NOT_FOUND, new Date());
                    getWelcomeScriptResponse.setIsThereError(1);
                    getWelcomeScriptResponse.setErrorText("Pdf Formatı bulunamadı");
                    getWelcomeScriptResponse.setError_code(new Double("4"));
                    return getWelcomeScriptResponse;
                }
                PdfReader reader = new PdfReader( readpath+"/" +readerDocName);
                Rectangle size = reader.getPageSizeWithRotation(1);
                Document doc = new Document(size);
                UUID uuid = UUID.randomUUID();
                String g = uuid.toString();
                g = g.substring(0, 6);
                DateFormat format = new SimpleDateFormat("dd MM yyyy");
                String date = format.format(new Date());
                documentName = getWelcomeScriptRequest.getPolid() + "_" + date.replace(" ", "") + "_" + g + ".pdf";
                returnURL += documentName;
                apiLogModel.setReturnVal(returnURL);
                ByteArrayOutputStream fs = new ByteArrayOutputStream();
                PdfWriter pdfwriter1 = PdfWriter.getInstance(doc, fs);
                doc.open();//Pdf Okuma
                PdfContentByte cb = pdfwriter1.getDirectContent();
                cb.setColorFill(BaseColor.BLACK);
                PdfImportedPage page = pdfwriter1.getImportedPage(reader, 1);
                cb.addTemplate(page, 0, 0);
                cb.beginText();//write the text in the pdf content
                String productName = readerDocName.substring(0, readerDocName.length() - 4);
                List<EDeliveryPDFCoordinatesModel> listCoordinates = eDeliveryPDFCoordinatesRepository.findAllByProjectAndProductNameAndIsvalid(projectName, productName, 1);
                int len = welPolItem.getAdres().length();
                String adres1 = "";
                String adres2 = "";
                String adres3 = "";

                if (len > 80) {
                    adres1 = welPolItem.getAdres().substring(0, 41);
                    int enSonBosluk1 = adres1.lastIndexOf(" ");
                    adres1 = adres1.substring(0, enSonBosluk1).trim();
                    adres2 = welPolItem.getAdres().substring(enSonBosluk1, 41);
                    int enSonBosluk2 = adres2.lastIndexOf(" ");
                    adres2 = adres2.substring(0, enSonBosluk2).trim();
                    adres3 = welPolItem.getAdres().substring(enSonBosluk1 + enSonBosluk2).trim();


                } else if (len > 41 && len <= 80) {
                    adres1 = welPolItem.getAdres().substring(0, 41);
                    int enSonBosluk = adres1.lastIndexOf(" ");
                    adres1 = adres1.substring(0, enSonBosluk).trim();

                    adres2 = welPolItem.getAdres().substring(enSonBosluk).trim();
                } else {
                    adres1 = welPolItem.getAdres();

                }
                Double tprim = welPolItem.getYillikPrim() * welPolItem.getPolDuration();

                for (EDeliveryPDFCoordinatesModel item : listCoordinates) {
                    if (item.getAreaname().equals("AgentNameandSurname")) {

                        pushDatatoPDF(cb, item.getAreaname(), getWelcomeScriptRequest.getAgentName() + " " + getWelcomeScriptRequest.getAgentSurname(), listCoordinates, null, null);

                    } else if (item.getAreaname().equals("CustomerNameSurname")) {

                        pushDatatoPDF(cb, item.getAreaname(), welPolItem.getSigortaEttirenAd() + " " + welPolItem.getSigortaEttirenSoyad(), listCoordinates, null, null);

                    } else if (item.getAreaname().equals("CustomerNameSecure")) {

                        pushDatatoPDF(cb, item.getAreaname(), welPolItem.getSigortaEttirenAd(), listCoordinates, null, null);

                    } else if (item.getAreaname().equals("CustomerNameFinal")) {

                        pushDatatoPDF(cb, item.getAreaname(), welPolItem.getSigortaEttirenAd(), listCoordinates, null, null);

                    } else if (item.getAreaname().equals("CustomerName1")) {

                        pushDatatoPDF(cb, item.getAreaname(), welPolItem.getSigortaEttirenAd(), listCoordinates, null, null);

                    } else if (item.getAreaname().equals("CustomerName2")) {

                        pushDatatoPDF(cb, item.getAreaname(), welPolItem.getSigortaEttirenAd(), listCoordinates, null, null);

                    } else if (item.getAreaname().equals("CustomerName3")) {

                        pushDatatoPDF(cb, item.getAreaname(), welPolItem.getSigortaEttirenAd(), listCoordinates, null, null);

                    } else if (item.getAreaname().equals("CustomerName4")) {

                        pushDatatoPDF(cb, item.getAreaname(), welPolItem.getSigortaEttirenAd(), listCoordinates, null, null);

                    } else if (item.getAreaname().equals("CustomerName5")) {

                        pushDatatoPDF(cb, item.getAreaname(), welPolItem.getSigortaEttirenAd(), listCoordinates, null, null);

                    } else if (item.getAreaname().equals("CustomerName6")) {

                        pushDatatoPDF(cb, item.getAreaname(), welPolItem.getSigortaEttirenAd(), listCoordinates, null, null);

                    } else if (item.getAreaname().equals("CustomerName7")) {

                        pushDatatoPDF(cb, item.getAreaname(), welPolItem.getSigortaEttirenAd(), listCoordinates, null, null);

                    } else if (item.getAreaname().equals("CustomerName8")) {

                        pushDatatoPDF(cb, item.getAreaname(), welPolItem.getSigortaEttirenAd(), listCoordinates, null, null);

                    } else if (item.getAreaname().equals("Mail")) {
                        if (!StringUtils.isEmpty(welPolItem.getEmail())) {
                            String[] parts = welPolItem.getEmail().split("@");
                            String mail = parts[0];
                            if (mail.length() > 12) {
                                pushDatatoPDF(cb, item.getAreaname(), mail, listCoordinates, 12, null);
                            } else {
                                pushDatatoPDF(cb, item.getAreaname(), mail, listCoordinates, null, null);
                            }
                        }


                    } else if (item.getAreaname().equals("MailExtension")) {
                        if (!StringUtils.isEmpty(welPolItem.getEmail())) {
                            String[] parts = welPolItem.getEmail().split("@");
                            if (parts.length > 1) {
                                String maildomain = parts[1];
                                if (maildomain.length() > 12) {
                                    pushDatatoPDF(cb, item.getAreaname(), maildomain, listCoordinates, 12, 0);
                                } else {
                                    pushDatatoPDF(cb, item.getAreaname(), maildomain, listCoordinates, null, 0);

                                }
                            }
                        }


                    } else if (item.getAreaname().equals("Phone")) {

                        pushDatatoPDF(cb, item.getAreaname(), getWelcomeScriptRequest.getGSMphone(), listCoordinates, null, null);

                    } else if (item.getAreaname().equals("FirstAddress")) {

                        pushDatatoPDF(cb, item.getAreaname(), adres1, listCoordinates, null, null);

                    } else if (item.getAreaname().equals("SecondAddress")) {

                        pushDatatoPDF(cb, item.getAreaname(), adres2, listCoordinates, null, null);

                    } else if (item.getAreaname().equals("ThirdAdress")) {
                        if (!StringUtils.isEmpty(adres3)) {
                            pushDatatoPDF(cb, item.getAreaname(), adres3, listCoordinates, null, null);
                        }


                    } else if (item.getAreaname().equals("CaymaBitisTarihi")) {
                        Calendar c = Calendar.getInstance();
                        c.setTime(welPolItem.getCaymaTar());
                        c.add(Calendar.DATE, 30);//Add 30 days.
                        Date cayma = c.getTime();
                        pushDatatoPDF(cb, item.getAreaname(), DateFormat(cayma), listCoordinates, null, null);
                    } else if (item.getAreaname().equals("PoliceBaslangicTarihi")) {


                        pushDatatoPDF(cb, item.getAreaname(), DateFormat(welPolItem.getPolBasTar()), listCoordinates, null, null);

                    } else if (item.getAreaname().equals("PoliceBaslangicTarihi2")) {

                        pushDatatoPDF(cb, item.getAreaname(), DateFormat(welPolItem.getPolBasTar()), listCoordinates, null, null);

                    } else if (item.getAreaname().equals("polDuration")) {

                        pushDatatoPDF(cb, item.getAreaname(), welPolItem.getPolDuration().toString(), listCoordinates, null, null);

                    } else if (item.getAreaname().equals("polDuration2")) {

                        pushDatatoPDF(cb, item.getAreaname(), welPolItem.getPolDuration().toString(), listCoordinates, null, null);

                    } else if (item.getAreaname().equals("PoliceBitisTarihi")) {

                        pushDatatoPDF(cb, item.getAreaname(), DateFormat(welPolItem.getPolBitTar()), listCoordinates, null, null);

                    } else if (item.getAreaname().equals("PoliçeYılı")) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(welPolItem.getPolBitTar());
                        Integer year = calendar.get(Calendar.YEAR);

                        pushDatatoPDF(cb, item.getAreaname(), year.toString(), listCoordinates, null, null);

                    } else if (item.getAreaname().equals("GeriÖdemeSüresi")) {

                        pushDatatoPDF(cb, item.getAreaname(), welPolItem.getGeriodemeyil().toString(), listCoordinates, null, null);

                    } else if (item.getAreaname().equals("VefatTeminati")) {
                        Double tahsilat = (Math.ceil(welPolItem.getTahsTutar()));
                        DecimalFormat formatter = new DecimalFormat("#,###");
                        String vefatTeminatı = formatter.format(tahsilat).toString();
                        vefatTeminatı = vefatTeminatı.replace(",", ".");

                        pushDatatoPDF(cb, item.getAreaname(), vefatTeminatı, listCoordinates, null, null);

                    } else if (item.getAreaname().equals("MaluliyetTutari")) {
                        if (Math.ceil(welPolItem.getHktdmTutar()) == -1) {
                            pushDatatoPDF(cb, item.getAreaname(), "", listCoordinates, null, null);
                        } else {
                            Double hktdm = Math.ceil(welPolItem.getHktdmTutar());
                            DecimalFormat formatter = new DecimalFormat("#,###");
                            String Hktdm = formatter.format(hktdm).toString();
                            Hktdm = Hktdm.replace(",", ".");
                            pushDatatoPDF(cb, item.getAreaname(), Hktdm, listCoordinates, null, null);
                        }


                    } else if (item.getAreaname().equals("ToplamPrimTutari")) {
                        Double prim = (Math.floor(tprim));
                        DecimalFormat formatter = new DecimalFormat("#,###");
                        String Prim = formatter.format(prim).toString();
                        Prim = Prim.replace(",", ".");
                        pushDatatoPDF(cb, item.getAreaname(), Prim, listCoordinates, null, null);

                    } else if (item.getAreaname().equals("YillikPrim")) {
                        Double yillikPrim = (Math.floor(welPolItem.getYillikPrim()));
                        DecimalFormat formatter = new DecimalFormat("#,###");
                        String yPrim = formatter.format(yillikPrim).toString();
                        yPrim = yPrim.replace(",", ".");
                        pushDatatoPDF(cb, item.getAreaname(), yPrim, listCoordinates, null, null);

                    } else if (item.getAreaname().equals("BonoNo")) {

                        pushDatatoPDF(cb, item.getAreaname(), welPolItem.getBonoNo().toString(), listCoordinates, null, null);

                    } else if (item.getAreaname().equals("BonoVadeTar")) {

                        pushDatatoPDF(cb, item.getAreaname(), welPolItem.getBonovadetar().toString(), listCoordinates, null, null);

                    } else if (item.getAreaname().equals("EsikDegeri1")) {

                        pushDatatoPDF(cb, item.getAreaname(), "%" + welPolItem.getFaizOran().toString(), listCoordinates, null, null);

                    } else if (item.getAreaname().equals("EsikDegeri2")) {

                        pushDatatoPDF(cb, item.getAreaname(), "%" + welPolItem.getFaizOran().toString(), listCoordinates, null, null);

                    } else if (item.getAreaname().equals("EsikDegeri3")) {

                        pushDatatoPDF(cb, item.getAreaname(), "%" + welPolItem.getFaizOran().toString(), listCoordinates, null, null);

                    } else if (item.getAreaname().equals("SabitKur")) {

                        pushDatatoPDF(cb, item.getAreaname(), welPolItem.getDovKur().toString(), listCoordinates, null, null);

                    } else if (item.getAreaname().equals("SabitKur2")) {

                        pushDatatoPDF(cb, item.getAreaname(), welPolItem.getDovKur().toString(), listCoordinates, null, null);

                    } else if (item.getAreaname().equals("CalDovTutar")) {
                        Long calDovTutar = Math.round(welPolItem.getDovTutar() * welPolItem.getDovKur());
                        pushDatatoPDF(cb, item.getAreaname(), calDovTutar.toString(), listCoordinates, null, null);

                    } else if (item.getAreaname().equals("DovTutar")) {

                        pushDatatoPDF(cb, item.getAreaname(), welPolItem.getDovTutar().toString(), listCoordinates, null, null);

                    } else if (item.getAreaname().equals("AylikPrimTutari")) {
                        Long round = Long.valueOf(Math.round(welPolItem.getAylikPrim()));
                        pushDatatoPDF(cb, item.getAreaname(), round.toString(), listCoordinates, null, null);

                    } else if (item.getAreaname().equals("Aylıkek2")) {
                        //07.05.2019 aylikek2 kullanılmıyor. Yerine caldovtutar kullanılması istendi. Tasarımlardan aylikek2 çıkarıldı.
                        // PushDatatoPDF(cb, item.getAreaname(), welPolItem.get, listCoordinates, null, null);

                    } else if (item.getAreaname().equals("TeminatPrimOrani")) {
                        if (welPolItem.getTahsTutar() == 0) {
                            pushDatatoPDF(cb, item.getAreaname(), "%" + "0", listCoordinates, null, null);
                        } else {
                            Long primOrani = Long.valueOf(Math.round(welPolItem.getFirstPremium() / welPolItem.getTahsTutar()));
                            pushDatatoPDF(cb, item.getAreaname(), primOrani.toString(), listCoordinates, null, null);
                        }


                    } else if (item.getAreaname().equals("ÖdemeYöntemi")) {

                        pushDatatoPDF(cb, item.getAreaname(), welPolItem.getOdeTip(), listCoordinates, null, null);

                    } else if (item.getAreaname().equals("ÖdemePeriyodu")) {

                        pushDatatoPDF(cb, item.getAreaname(), welPolItem.getOdeFrekans(), listCoordinates, null, null);

                    } else if (item.getAreaname().equals("MaluliyetÖdemePeriyodu")) {
                        if (Math.ceil(welPolItem.getHktdmTutar()) == -1) {

                            pushDatatoPDF(cb, item.getAreaname(), "", listCoordinates, null, null);
                        } else {
                            pushDatatoPDF(cb, item.getAreaname(), welPolItem.getOdeFrekans(), listCoordinates, null, null);
                        }


                    } else if (item.getAreaname().equals("PoliceYıllıkSüre")) {

                        // PushDatatoPDF(cb, projectName, productName, "TotalYıl2", "10-2");

                    } else if (item.getAreaname().equals("TotalYıl")) {

                        pushDatatoPDF(cb, item.getAreaname(), welPolItem.getPolDuration() + "", listCoordinates, null, null);

                    } else if (item.getAreaname().equals("TotalYıl2")) {

                        //PushDatatoPDF(cb, projectName, productName, "TotalYıl2", "10-2");
                    } else if (item.getAreaname().equals("TotalYıl3")) {

                        pushDatatoPDF(cb, item.getAreaname(), welPolItem.getPolDuration() + "", listCoordinates, null, null);

                    } else if (item.getAreaname().equals("PrimTutari")) {

                        // PushDatatoPDF(cb, projectName, productName, "PrimTutari", "40000");

                    } else if (item.getAreaname().equals("VefatTeminatTarihi")) {

                        //PushDatatoPDF(cb, projectName, productName, "VefatTeminatTarihi", "17/02/2019");

                    } else if (item.getAreaname().equals("PoliçeTarihi")) {

                        //PushDatatoPDF(cb, projectName, productName, "PoliçeTarihi", "17/03/2019");

                    } else if (item.getAreaname().equals("KazaSonucuVefatTeminati")) {

                        //PushDatatoPDF(cb, projectName, productName, "KazaSonucuVefatTeminati", "10000");

                    } else if (item.getAreaname().equals("TeminatTutari")) {

                        // PushDatatoPDF(cb, projectName, productName, "TeminatTutari", "60000");

                    } else if (item.getAreaname().equals("HayatSigortasiSuresi")) {

                        //PushDatatoPDF(cb, projectName, productName, "HayatSigortasiSuresi", "10");

                    } else if (item.getAreaname().equals("PoliçeGeriÖdemeSüresi")) {

                        //PushDatatoPDF(cb, projectName, productName, "PoliçeGeriÖdemeSüresi", "12");

                    } else if (item.getAreaname().equals("PoliçeYürürlülükTarihi")) {

                        pushDatatoPDF(cb, item.getAreaname(), DateFormat(welPolItem.getPolBasTar()) + "", listCoordinates, null, null);

                    }


                }
                cb.endText();
                doc.close();
                pdfwriter1.close();
                reader.close();
                byte[] pdfBytes = fs.toByteArray();
                try {
                    saveApiLog(apiLogModel,0,0,null);
                    ApiDocumentLogModel apiDocumentLog = new ApiDocumentLogModel();
                    apiDocumentLog.setCdApiLogId(apiLogModel.getLogId());
                    apiDocumentLog.setCbDoc(pdfBytes);
                    apiDocumentLog.setDtCreateDate(new Date());
                    apiDocumentLog.setCdPolid(apiLogModel.getInptPolid());
                    apiDocumentLog.setChFilename(documentName);
                    apiDocumentLogRepository.save(apiDocumentLog);
                    getWelcomeScriptResponse.setValue(pdfBytes);
                    getWelcomeScriptResponse.setIsThereError(0);
                    getWelcomeScriptResponse.setErrorText(null);
                    getWelcomeScriptResponse.setError_code(0.0);
                } catch (Exception ex) {

                    String err = "";
                    err = ex + " : " + ex.getMessage();
                    if (err.length() > 500) {
                        err = err.substring(0, 500);
                    }
                   saveApiLog(apiLogModel,1,-1,err);
                    getWelcomeScriptResponse.setError_code(-1.0);
                    getWelcomeScriptResponse.setIsThereError(1);
                    getWelcomeScriptResponse.setErrorText("Sistemde beklenmeyen bir hata oluştu.");
                    return getWelcomeScriptResponse;
                }

            } else {
                saveApiLog(apiLogModel,1,4,"Geçersiz validTar,bransKod veya tarifeNo");
                log.error("errorCode:4, isThereError:1, errorMessage:{}, returnTime:{}", PDF_FORMAT_NOT_FOUND, new Date());
                getWelcomeScriptResponse.setIsThereError(1);
                getWelcomeScriptResponse.setErrorText("Pdf Formatı bulunamadı");
                getWelcomeScriptResponse.setError_code(new Double("4"));
                return getWelcomeScriptResponse;


            }


            return getWelcomeScriptResponse;
        } catch (Exception ex) {

            String err = "";
            err = ex + " : " + ex.getMessage();
            if (err.length() > 500) {
                err = err.substring(0, 500);
            }
            saveApiLog(apiLogModel,1,-1,err);
            getWelcomeScriptResponse.setError_code(-1.0);
            getWelcomeScriptResponse.setIsThereError(1);
            getWelcomeScriptResponse.setErrorText("Sistemde beklenmeyen bir hata oluştu.");
            return getWelcomeScriptResponse;
        }


    }

    public WelcomeData formatGetWelcomeCallInfo(Map<String, Object> parameterList, Long polid, ApiLogModel apiLogModel) throws ParseException {
        WelcomeData welcomeData = new WelcomeData();
        if (parameterList.get("hataMes") != null && parameterList.get("hataMes").toString().equals("0")) {
            if (parameterList.get("aciklama") != null) {
                welcomeData.setAciklama(parameterList.get("aciklama").toString());
            }
            if (parameterList.get("bransKod") != null) {
                welcomeData.setBransKod(parameterList.get("bransKod").toString());
            }
            if (parameterList.get("email") != null) {
                welcomeData.setEmail(parameterList.get("email").toString());
            }

            if (parameterList.get("odeFrekans") != null) {
                welcomeData.setOdeFrekans(parameterList.get("odeFrekans").toString().toLowerCase());
            }
            if (parameterList.get("odeTip") != null) {
                welcomeData.setOdeTip(parameterList.get("odeTip").toString());
                if (welcomeData.getOdeTip().equals("TORBA HESAP")) {
                    welcomeData.setOdeTip("Havale");

                }
            }

            welcomeData.setPolid(polid);
            if (parameterList.get("policeNo") != null) {
                welcomeData.setPoliceNo(parameterList.get("policeNo").toString());
            }
            if (parameterList.get("sigortaliAd") != null) {
                welcomeData.setSigortaliAd(parameterList.get("sigortaliAd").toString());
            }
            if (parameterList.get("sigortaEttirenAd") != null) {
                welcomeData.setSigortaEttirenAd(parameterList.get("sigortaEttirenAd").toString());
            }
            if (parameterList.get("sigortaEttirenSoyad") != null) {
                welcomeData.setSigortaEttirenSoyad(parameterList.get("sigortaEttirenSoyad").toString());
            }
            if (parameterList.get("sigortaEttrenTck") != null) {
                welcomeData.setSigortaEttirenTck(parameterList.get("sigortaEttrenTck").toString());
            }
            if (parameterList.get("sigortaEttirenPid") != null) {
                welcomeData.setSigortaEttirenPid(Long.parseLong(parameterList.get("sigortaEttirenPid").toString()));
            }
            if (parameterList.get("sigortaliSoyad") != null) {
                welcomeData.setSigortaliSoyad(parameterList.get("sigortaliSoyad").toString());
            }
            if (parameterList.get("sigortaliTck") != null) {
                welcomeData.setSigortaliTck(parameterList.get("sigortaliTck").toString());
            }
            if (parameterList.get("tahsTip") != null) {
                welcomeData.setTahsTip(parameterList.get("tahsTip").toString());
            }
            if (parameterList.get("tahsTutar") != null) {
                welcomeData.setTahsTutar(Long.parseLong(parameterList.get("tahsTutar").toString()));
            }

            if (parameterList.get("tarifeNo") != null) {
                welcomeData.setTarifeNo(parameterList.get("tarifeNo").toString());
            }
            if (parameterList.get("urunTip") != null) {
                welcomeData.setUrunTip(parameterList.get("urunTip").toString());
            }
            if (parameterList.get("polBasTar") != null) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date = format.parse(parameterList.get("polBasTar").toString());
                welcomeData.setPolBasTar(date);
            }
            if (parameterList.get("polDuration") != null) {
                welcomeData.setPolDuration(Long.parseLong(parameterList.get("polDuration").toString()));
            }
            if (parameterList.get("urunAdi") != null) {
                welcomeData.setUrunAdi(parameterList.get("urunAdi").toString());
            }
            if (parameterList.get("basvuruNo") != null) {
                welcomeData.setBasvuruNo(parameterList.get("basvuruNo").toString());
            }

            if (parameterList.get("polBitTar") != null) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date = format.parse(parameterList.get("polBitTar").toString());
                welcomeData.setPolBitTar(date);
            }
            if (parameterList.get("outSatisKanali") != null) {
                welcomeData.setOutSatisKanali(parameterList.get("outSatisKanali").toString());
            }
            if (parameterList.get("yillikPrim") != null) {
                welcomeData.setYillikPrim(Double.parseDouble(parameterList.get("yillikPrim").toString()));
            }
            if (parameterList.get("oran") != null) {
                if (parameterList.get("oran").toString().equals("")) {
                    welcomeData.setOran(0.0);
                } else {
                    welcomeData.setOran(Double.parseDouble(parameterList.get("oran").toString()));
                }

            }
            if (parameterList.get("caymaTar") != null) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date = format.parse(parameterList.get("caymaTar").toString());
                welcomeData.setCaymaTar(date);
            }
            if (parameterList.get("teminatlar") != null) {
                welcomeData.setTeminatlar(parameterList.get("teminatlar").toString());
            }
            if (parameterList.get("hktdmTutar") != null) {
                welcomeData.setHktdmTutar(Long.parseLong(parameterList.get("hktdmTutar").toString()));
            }
            if (parameterList.get("anaTeminatIsmi") != null) {
                welcomeData.setAnaTeminatIsmi(parameterList.get("anaTeminatIsmi").toString());
            }
            if (parameterList.get("faizOran") != null) {
                welcomeData.setFaizOran(Long.parseLong(parameterList.get("faizOran").toString()));
            }
            if (parameterList.get("geriodemeyil") != null) {
                welcomeData.setGeriodemeyil(Long.parseLong(parameterList.get("geriodemeyil").toString()));
            }
            if (parameterList.get("adres") != null) {
                welcomeData.setAdres(parameterList.get("adres").toString());
            }
            if (parameterList.get("adresIl") != null) {
                welcomeData.setAdresIl(parameterList.get("adresIl").toString());
            }
            if (parameterList.get("dogTar") != null) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date = format.parse(parameterList.get("dogTar").toString());
                welcomeData.setDogTar(date);
            }
            if (parameterList.get("dovKod") != null) {
                welcomeData.setDovKod(parameterList.get("dovKod").toString());
            }
            if (parameterList.get("bonoNo") != null) {
                welcomeData.setBonoNo(parameterList.get("bonoNo").toString());
            }
            if (parameterList.get("bonovadetar") != null) {
                welcomeData.setBonovadetar(parameterList.get("bonovadetar").toString());
            }
            if (parameterList.get("sonZeylNo") != null) {
                welcomeData.setSonZeylNo(Long.parseLong(parameterList.get("sonZeylNo").toString()));
            }
            welcomeData.setThereError(false);
        } else {
            welcomeData.setThereError(true);
            String tempError = "";
            if (parameterList.get("hataMes") != null) {
                tempError = parameterList.get("hataMes").toString();
                tempError = "Poliçe bilgisi prosedürle alınırken hata oluştu. " + tempError;
                saveApiLog(apiLogModel,1,3,tempError);
            }


        }
        try {
            if (welcomeData.getUrunAdi().contains("SABİT KURLU")) {
                Map<String, Object> sk_parameterList = welcomeCallSabitKurluCustomRepository.getWelcomeSabitKurlu(polid);
                if (sk_parameterList.get("hataMes") != null && sk_parameterList.get("hataMes").toString().equals("0")) {

                    if (sk_parameterList.get("dovTutar") != null) {
                        welcomeData.setDovTutar(Long.parseLong(sk_parameterList.get("dovTutar").toString()));
                    }
                    if (sk_parameterList.get("dovKur") != null) {
                        welcomeData.setDovKur(Double.parseDouble(sk_parameterList.get("dovKur").toString()));
                    }
                    if (sk_parameterList.get("firstPremium") != null) {
                        welcomeData.setFirstPremium(Double.parseDouble(sk_parameterList.get("firstPremium").toString()));
                    }
                    if (sk_parameterList.get("aylıkPrim") != null) {
                        welcomeData.setAylikPrim(Long.parseLong(sk_parameterList.get("aylıkPrim").toString()));
                    }
                    welcomeData.setThereError(false);
                } else {
                    welcomeData.setThereError(true);
                    String tempError = "";
                    if (!StringUtils.isEmpty(sk_parameterList.get("hataMes").toString())) {
                        tempError = sk_parameterList.get("hataMes").toString();
                        tempError = "Sabit Kurlu Data bilgisi prosedürle alınırken hata oluştu. " + tempError;
                        saveApiLog(apiLogModel,1,3,tempError);
                    }
                }

            }
        } catch (Exception ex) {

        }
        return welcomeData;
    }

    private void pushDatatoPDF(PdfContentByte cb, String AreaName, String data, List<EDeliveryPDFCoordinatesModel> listCoordinates, Integer font, Integer align) throws IOException, DocumentException {
        BaseFont bf = BaseFont.createFont("Helvetica", "Cp1254", BaseFont.NOT_EMBEDDED);
        List<EDeliveryPDFCoordinatesModel> record = listCoordinates.stream().filter((x -> x.getAreaname().equals(AreaName))).collect(Collectors.toList());
        if (!record.isEmpty()) {
            EDeliveryPDFCoordinatesModel coordinatedata = record.stream().findFirst().orElse(null);
            if (font != null) {
                cb.setFontAndSize(bf, font);
            } else {
                cb.setFontAndSize(bf, coordinatedata.getFont());
            }

            if (align != null) {
                cb.showTextAligned(align, data, coordinatedata.getXcoordinate(), coordinatedata.getYcoordinate(), 0);
            } else {
                cb.showTextAligned(Element.ALIGN_CENTER, data, coordinatedata.getXcoordinate(), coordinatedata.getYcoordinate(), 0);
            }


        }

    }

    private SbmAddressResponse findAdressBySbmService(String TC) throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(client.callSbmService(TC)));
        SbmAddressResponse sbmAddressResponse = new SbmAddressResponse();
        sbmAddressResponse.setAcikAdres(getInfoFromSbmResponse(is, db, "acikAdres"));
        sbmAddressResponse.setCsbm(getInfoFromSbmResponse(is, db, "csbm"));
        sbmAddressResponse.setIlAd(getInfoFromSbmResponse(is, db, "ilAd"));
        sbmAddressResponse.setIlceAd(getInfoFromSbmResponse(is, db, "ilceAd"));
        sbmAddressResponse.setIlKodu(getInfoFromSbmResponse(is, db, "ilKodu"));
        sbmAddressResponse.setMahalle(getInfoFromSbmResponse(is, db, "mahalle"));
        return sbmAddressResponse;

    }

    private String getInfoFromSbmResponse(InputSource inputSource, DocumentBuilder documentBuilder, String tag) throws IOException, SAXException {
        org.w3c.dom.Document xmlDoc = documentBuilder.parse(inputSource);
        NodeList nodeList = xmlDoc.getElementsByTagName(tag);
        List<String> ids = new ArrayList<String>(nodeList.getLength());
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node x = nodeList.item(i);
            ids.add(x.getFirstChild().getNodeValue());
        }
        List<String> output = ids;
        String[] strarray = new String[output.size()];
        output.toArray(strarray);


        return strarray[0];

    }

    private static String DateFormat(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("tr"));
        return sdf.format(date);
    }

    private void saveApiLog(ApiLogModel apiLogModel,Integer isThereError, Integer errorCode, String errMsg){
        apiLogModel.setIsThereError(isThereError);
        apiLogModel.setErrorCode(errorCode);
        apiLogModel.setErrMsg(errMsg);
        apiLogRepository.save(apiLogModel);

    }


}
