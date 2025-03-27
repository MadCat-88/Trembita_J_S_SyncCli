/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ega.springclientsoap.services;

import com.ega.springclientsoap.WebConfig;
import com.ega.springclientsoap.interfaces.SpringClientSoapInterface;
import com.ega.springclientsoap.models.Answer;
import com.ega.springclientsoap.models.AppSettings;
import com.ega.springclientsoap.models.LogRecord;
import com.ega.springclientsoap.models.Persona;
import io.spring.guides.gs_producing_web_service.AddPersonaRequest;
import io.spring.guides.gs_producing_web_service.AddPersonaResponse;
import io.spring.guides.gs_producing_web_service.AnswerXml;
import io.spring.guides.gs_producing_web_service.DeletePersonaRequest;
import io.spring.guides.gs_producing_web_service.DeletePersonaResponse;
import io.spring.guides.gs_producing_web_service.GetPersonaListByBirthDateRequest;
import io.spring.guides.gs_producing_web_service.GetPersonaListByFirstNameRequest;
import io.spring.guides.gs_producing_web_service.GetPersonaListByLastNameRequest;
import io.spring.guides.gs_producing_web_service.GetPersonaListByPasportRequest;
import io.spring.guides.gs_producing_web_service.GetPersonaListByUnzrRequest;
import io.spring.guides.gs_producing_web_service.GetPersonaListRequest;
import io.spring.guides.gs_producing_web_service.GetPersonaListResponse;
import io.spring.guides.gs_producing_web_service.GetPersonaRequest;
import io.spring.guides.gs_producing_web_service.ObjectFactory;
import io.spring.guides.gs_producing_web_service.PersonaXml;
import io.spring.guides.gs_producing_web_service.UpdatePersonaRequest;
import io.spring.guides.gs_producing_web_service.UpdatePersonaResponse;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.soap.Name;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPHeaderElement;
import jakarta.xml.soap.SOAPMessage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.soap.SoapEnvelope;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.SoapHeaderException;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.xml.namespace.QNameUtils;
import org.springframework.xml.transform.StringSource;
import reactor.core.publisher.Flux;


/**
 *
 * @author sa
 */
@Service
public class SpringClientSoapService implements SpringClientSoapInterface{
    // Constructor-based dependency injection for WebClient

    
    @Override
    public String showAll() {
        GetPersonaListRequest request = new GetPersonaListRequest();

        WebConfig webServiceTemplate = new WebConfig();

        //webServiceTemplate.web.getMessageFactory()
        //        .SetHeader("id", "26f22fcd-c61a-4169-b8ca-9603a7a67bfe");
        /*
        SOAPHeader header;
        try {
            SOAPEnvelope soapEnvelope = webServiceTemplate.message.getSOAPPart().getEnvelope();
            header = webServiceTemplate.message.getSOAPHeader();

            Name headerElementName = soapEnvelope.createName(
                    "Security",
                    "wsse",
                    "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"
            );
            SOAPHeaderElement soapHeaderElement = header.addHeaderElement(headerElementName);

            SOAPElement usernameTokenSOAPElement = soapHeaderElement.addChildElement("UsernameToken", "wsse");

            SOAPElement userNameSOAPElement = usernameTokenSOAPElement.addChildElement("Username", "wsse");
            userNameSOAPElement.addTextNode("myusername");
            
            //SOAPElement element = header.addChildElement("name", "string");
            webServiceTemplate.message.saveChanges();
        } catch (SOAPException ex) {
            System.out.println("Error: "+ex.getMessage());
        }
        
        //request.
        GetPersonaListResponse response = (GetPersonaListResponse) webServiceTemplate.web.marshalSendAndReceive(AppSettings.SERVER_PATH,request
            {  public void doWithMessage(WebServiceMessage message) {
                ((SoapMessage)message).setSoapAction("http://tempuri.org/Action");
            }
            );
        */
          GetPersonaListResponse response = (GetPersonaListResponse) SendAndReceive((GetPersonaListRequest) request);

/*
        GetPersonaListResponse response = (GetPersonaListResponse) webServiceTemplate.web.marshalSendAndReceive(AppSettings.SERVER_PATH,request, new WebServiceMessageCallback() {
            @Override
            public void doWithMessage(WebServiceMessage message) {
                try {
                    
                } catch (SOAPException ex) {
                    Logger.getLogger(SpringClientSoapService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        
        });
*/        
        
        String html = getHtml(response);
        
        return html;

        
    }
    
    public Object SendAndReceive(Object request){
        WebConfig webServiceTemplate = new WebConfig();
        
        String uuid =UUID.randomUUID().toString();
        
        Object response = webServiceTemplate.web.marshalSendAndReceive(AppSettings.SERVER_PATH,request, new WebServiceMessageCallback() {
            @Override
            public void doWithMessage(WebServiceMessage message) {
                try
                {
/*          Додаємо заголовки для Трембіта 1.х 
            Заголовки повинни виглядяти наступним чином     
                                        
                    <soapenv:Header>
                      <xro:client iden:objectType="SUBSYSTEM">
                         <iden:xRoadInstance>SEVDEIR-TEST</iden:xRoadInstance>
                         <iden:memberClass>GOV</iden:memberClass>
                         <iden:memberCode>11110014</iden:memberCode>
                         <!--Optional:-->
                         <iden:subsystemCode>SUB_prod</iden:subsystemCode>
                      </xro:client>
                      <xro:service iden:objectType="SERVICE">
                         <iden:xRoadInstance>SEVDEIR-TEST</iden:xRoadInstance>
                         <iden:memberClass>GOV</iden:memberClass>
                         <iden:memberCode>11110015</iden:memberCode>
                         <!--Optional:-->
                         <iden:subsystemCode>88_Test_prod</iden:subsystemCode>
                         <iden:serviceCode>COUNTRY</iden:serviceCode>
                         <!--Optional:-->
                         <iden:serviceVersion>1</iden:serviceVersion>
                      </xro:service>
                      <xro:id>2346222</xro:id>
                      <xro:protocolVersion>4.0</xro:protocolVersion>
                   </soapenv:Header>
*/           
                    
                    
                    SOAPMessage soapMessage = ((SaajSoapMessage)message).getSaajMessage();
                    SOAPHeader header = soapMessage.getSOAPHeader();
                    QName qname = new QName("http://tempuri.org/","client","xro");
                    SOAPHeaderElement client = header.addHeaderElement(qname);
                    QName iden = new QName("http://tempuri.org/","objectType","iden");
                    client.addAttribute(iden, "SUBSYSTEM");

                    for (String key : AppSettings.CLIENT_HEADERS.keySet()) {
                        String value = AppSettings.CLIENT_HEADERS.get(key);

                        SOAPElement clientHeader = client.addChildElement(key, "iden");
                        clientHeader.setTextContent(value);
                    }
  
                    qname = new QName("http://tempuri.org/","service","xro");
                    SOAPHeaderElement service = header.addHeaderElement(qname);
                    service.addAttribute(iden, "SERVICE");
                    for (String key : AppSettings.SERVICE_HEADERS.keySet()) {
                        String value = AppSettings.SERVICE_HEADERS.get(key);

                        SOAPElement serviceHeader = service.addChildElement(key, "iden");
                        serviceHeader.setTextContent(value);
                    }

                    for (String key : AppSettings.HEADERS.keySet()) {
                        String value = AppSettings.HEADERS.get(key);

                        SOAPHeaderElement soapHeader = header.addHeaderElement(new QName("http://tempuri.org/", key, "xro"));
                        soapHeader.setTextContent(value);
                    }

                    SOAPHeaderElement soapHeader = header.addHeaderElement(new QName("http://tempuri.org/", "id", "xro"));
                    soapHeader.setTextContent(uuid);
                    
                    
                } catch (SOAPException ex) {
                    System.out.println("Error: "+ex.getMessage());
                }

            }
        
        });
        
        
        //логуемо УІД щоб потім можно було отримати ASIC-контейнер
        loguuid(uuid);
        
        return response;
    }

    @Override
    public String findByRnokpp(String searchData) {
        GetPersonaRequest request = new GetPersonaRequest();
        request.setRnokpp(searchData);


        //request.
        GetPersonaListResponse response = (GetPersonaListResponse) SendAndReceive(request);
        
        String html = getHtml(response);
        
        return html;

        
    }

    @Override
    public String findByFirstName(String firstName) {
        GetPersonaListByFirstNameRequest request = new GetPersonaListByFirstNameRequest();
        request.setFirstName(firstName);
        
        //request.
        GetPersonaListResponse response = (GetPersonaListResponse) SendAndReceive(request);
        
        String html = getHtml(response);
        
        return html;
    }

    @Override
    public String findByLastName(String lastName) {
        GetPersonaListByLastNameRequest request = new GetPersonaListByLastNameRequest();
        request.setLastName(lastName);
        
        //request.
        GetPersonaListResponse response = (GetPersonaListResponse) SendAndReceive(request);
        
        String html = getHtml(response);
        
        return html;
    }

    @Override
    public String findByBirthDate(String birthDateStr) {
        GetPersonaListByBirthDateRequest request = new GetPersonaListByBirthDateRequest();
        request.setBirthDate(birthDateStr);
        
        //request.
        GetPersonaListResponse response = (GetPersonaListResponse) SendAndReceive(request);
        
        String html = getHtml(response);
        
        return html;
    }

    @Override
    public String findByPasport(String pasport) {
        GetPersonaListByPasportRequest request = new GetPersonaListByPasportRequest();
        request.setPasport(pasport);
        
        //request.
        GetPersonaListResponse response = (GetPersonaListResponse) SendAndReceive(request);
        
        String html = getHtml(response);
        
        return html;
    }

    @Override
    public String findByUnzr(String unzr) {
        GetPersonaListByUnzrRequest request = new GetPersonaListByUnzrRequest();
        request.setUnzr(unzr);
        
        //request.
        GetPersonaListResponse response = (GetPersonaListResponse) SendAndReceive(request);
        
        String html = getHtml(response);
        
        return html;
    }

    @Override
    public Answer savePersona(Persona persona) {
        
        Answer ans = Answer.builder().status(Boolean.FALSE).descr("Unknown error").build();
        XMLGregorianCalendar xmlBirthDate;

        try {
                xmlBirthDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(persona.getBirthDate().toString());
            } catch (DatatypeConfigurationException ex) {
                xmlBirthDate = null;  
        }

        if(persona.getId()==null){ //ADD new persona
            AddPersonaRequest request = new AddPersonaRequest();
            request.setFirstName(persona.getFirstName());
            request.setLastName(persona.getLastName());
            request.setBirthDate(xmlBirthDate);
            request.setGender(persona.getGender());
            request.setRnokpp(persona.getRnokpp());
            request.setPasport(persona.getPasport());
            request.setUnzr(persona.getUnzr());
            request.setPatronymic(persona.getPatronymic());

            //request.
            AddPersonaResponse response = (AddPersonaResponse) SendAndReceive(request);
            AnswerXml ansxml = response.getAnswerXml();
            ans.setStatus(ansxml.isStatus());
            ans.setDescr(ansxml.getDescr());
            ans.setResult(ansxml.getResult());
        }else{  //UPDATE persona
            UpdatePersonaRequest request = new UpdatePersonaRequest();
            request.setFirstName(persona.getFirstName());
            request.setLastName(persona.getLastName());
            request.setBirthDate(xmlBirthDate);
            request.setGender(persona.getGender());
            request.setRnokpp(persona.getRnokpp());
            request.setPasport(persona.getPasport());
            request.setUnzr(persona.getUnzr());
            request.setPatronymic(persona.getPatronymic());
            UpdatePersonaResponse response = (UpdatePersonaResponse) SendAndReceive(request);
    
            AnswerXml ansxml = response.getAnswerXml();
            ans.setStatus(ansxml.isStatus());
            ans.setDescr(ansxml.getDescr());
            ans.setResult(ansxml.getResult());
        }

        return ans;
    }

    @Override
    public Answer deletePersona(String rnokpp) {
        Answer ans = Answer.builder().status(Boolean.TRUE).descr("Not implemented").build();

        DeletePersonaRequest request = new DeletePersonaRequest();
        request.setRnokpp(rnokpp);
        //request.
        DeletePersonaResponse response = (DeletePersonaResponse) SendAndReceive(request);
        AnswerXml ansxml = response.getAnswerXml();
        ans.setStatus(ansxml.isStatus());
        ans.setDescr(ansxml.getDescr());
        ans.setResult(ansxml.getResult());

        return ans;
     
    }
    
    @Override
    public Answer checkPersona(String rnokpp) {
        /*
        WebClient webClient = new WebConfig().getWebClient();

        return  webClient.get()
                .uri("/check/"+rnokpp)
                .retrieve()
                .bodyToMono(Answer.class);
        */
        Answer ans = Answer.builder().status(Boolean.TRUE).descr("Not implemented").build();
        return ans;
        
    }

    @Override
    public Answer checkup(String rnokpp) {
        Answer ans = Answer.builder().status(Boolean.TRUE).descr("Not implemented").build();
        return ans;
    }
    
// Обробка даних, та формування вихідного HTML

    public String getHtml(GetPersonaListResponse response){
        String html = render_template("list_person.html");

        String param = ""+UUID.randomUUID().toString();
        JSONArray jsArray = new JSONArray();

        String body = "";
        
        for (PersonaXml persona : response.getPersonaXml()) {
            JSONObject jspersona = new JSONObject();
                
                body += "\nІм'я: " +persona.getFirstName()+"\n";
                body += "Прізвище: " +persona.getLastName()+"\n";
                body += "По батькові: " +persona.getPatronymic()+"\n";
                body += "Дата народження: " +persona.getBirthDate()+"\n";
                body += "Вік: " +persona.getAge()+"\n";
                body += "РНОКПП: " +persona.getRnokpp()+"\n";
                body += "УНЗР: " +persona.getUnzr()+"\n";
                body += "Паспорт: " +persona.getPasport()+"\n";
                body += "===============================================================================================\n";
                
                jspersona.put("id", persona.getId());
                jspersona.put("firstName", persona.getFirstName());
                jspersona.put("lastName", persona.getLastName());
                jspersona.put("gender", persona.getGender());
                jspersona.put("birthDate", persona.getBirthDate());
                jspersona.put("age", persona.getAge());
                jspersona.put("rnokpp", persona.getRnokpp());
                jspersona.put("unzr", persona.getUnzr());
                jspersona.put("pasport", persona.getPasport());
                jspersona.put("patronymic", persona.getPatronymic());
                
                jsArray.put(jspersona);
            }
        
    //    System.out.println("Found: "+body);
        
        String result = transformToTable(jsArray,html,param);     //модіфікуємо сторінку list_person.html
        
        
        
        return result;
    }
    
    public String transformToTable(JSONArray jsArray, String html,String queryId){
        Boolean isSuccess;
        String replaceString = "";
        String result = html;
     
        HashMap log = new HashMap();
        
        log.put("type", "RESPONSE");
        log.put("httpMethod", "GET");
        log.put("uri", AppSettings.SERVER_PATH);
        log.put("resource", "");
        log.put("queryId", queryId);
        //log.put("body", ans.toString());

        //writeLog(log);
        
        String res = "";
        res = jsArray.toString();
        try{
            jsArray = new JSONArray(res);
            for(int i=0;i<jsArray.length();i++){
                JSONObject item = jsArray.getJSONObject(i);

                //System.out.println(item.getString("lastName"));
                replaceString +=  "<tr>\n"
                    +"        <td>"+item.optString("firstName","")+"</td>  <!-- Відображаємо ім'я -->\n"
                    +"        <td>"+item.optString("lastName","")+"</td>  <!-- Відображаємо прізвище -->\n"
                    +"        <td>"+item.optString("patronymic","")+"</td>  <!-- Відображаємо по батькові -->\n"
                    +"        <td>"+item.optString("unzr","")+"</td>  <!-- Відображаємо УНЗР -->\n"
                    +"        <td>\n"
                    +"            <button class=\"btn btn-info\" onclick=\"showDetails("+item.getLong("id")+")\">Детальніше</button>  <!-- Кнопка для відображення деталей -->\n"
                    +"        </td>\n"
                    +"    </tr>\n";

            }
        }
        catch(Exception e){
            System.out.println("Error: "+e.getMessage());
//                    res="[{}]";
        }
                
        
        
        result = result.replaceAll("<!--@PersonsTable-->", replaceString);
        result = result.replaceAll("history.back()", "history.back(0)");
        result = result.replaceAll("@dataToJson", res);
    
        
        return result;
    }

    public String render_template(String templateName){
 
        // the stream holding the file content
        InputStream is = getClass().getClassLoader().getResourceAsStream("templates/"+templateName);
          
        String html = null;
        try (Scanner scanner = new Scanner(is, StandardCharsets.UTF_8.name())) {
            html = scanner.useDelimiter("\\A").next();

            return html;
        }catch(Exception ex){
            return "Error: File not found";
        }
        
    }
    
    
    
        //запис лога
    private void writeLog(HashMap logrecord){
    
        LogRecordService logService = new LogRecordService();
        
        LogRecord log = new LogRecord();
        
        log.setType((String)logrecord.getOrDefault("type",""));
        log.setUri((String)logrecord.getOrDefault("uri",AppSettings.SERVER_PATH));
        log.setHttpMethod((String)logrecord.getOrDefault("httpMethod",""));
        log.setQuieryId((String)logrecord.getOrDefault("queryId",""));
        log.setResource((String)logrecord.getOrDefault("resource",""));
        //log.setHeaders((String)logrecord.getOrDefault("headers"));

        log.setBody((String)logrecord.getOrDefault("body",""));
        /*
        if(ans!=null){
            log.setError(!ans.getStatus());
            log.setResult(ans);
            log.setDescr(ans.getDescr());
            log.setBody("");
        }
        */
        
        logService.addRecord(log);
        
    }

    @Override
    public String listCerts() {
        String html;
        
        html = render_template_files(AppSettings.CERTS_PATH, "list_certs.html");
    
        return html;
    }
    
    private String render_template_files(String path, String filename){
        
        List<File> files = Stream.of(new File(path).listFiles())
            .filter(file -> !file.isDirectory())
            //.map(File::getName)
            .collect(Collectors.toList());

        String page;
        if(path.equalsIgnoreCase(AppSettings.ASIC_PATH)){
            page = "asic";
        }else{
            page = "cert";
        }
        
        
        String replaceString = "";
        String html;
        //LocalDateTime fileLastModified;
        
        for(int i=0;i<files.size();i++){

            Long timestamp = files.get(i).lastModified();
            String fileLastModified = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), 
                                TimeZone.getDefault().toZoneId()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));    
            
            //службовий файл пропускаємо
            if(files.get(i).getName().equalsIgnoreCase("asic.log")){
                continue;
            }            
            
            replaceString +=  "<tr>\n"
            +"    <td> "+files.get(i).getName() +"</td>  <!-- Відображаємо ім'я файлу -->\n"
            +"    <td> "+fileLastModified +"</td>  <!-- Відображаємо дату і час створення файлу -->\n"
            +"    <td>\n"
            +"        <a href=\"/download/"+page+"/"+files.get(i).getName()+"\" class=\"btn btn-primary\">Скачати</a>  <!-- Посилання для завантаження файлу -->\n"
            +"    </td>\n"
            +"    </tr>\n";
            
        }

            //html = render_template("list_certs.html");
            html = render_template(filename);
            html = html.replaceAll("<!--@DataTable -->",replaceString);

            

        
        return html;
    }

    @Override
    public String downloadFile(String path) {
    
        return "";
    }
    
    private boolean getASIC(String queryId){
        Path path = Paths.get(AppSettings.ASIC_PATH+"file.asic");
        boolean result = false;
        
        try{
            WebClient webClient = WebClient.builder()                
                        .baseUrl(AppSettings.SERVER_PATH) // Set the base URL for the requests
                        .defaultCookie("cookie-name", "cookie-value") // Set a default cookie for the requests
                        .defaultHeaders(HttpHeaders-> {
                        //    HttpHeaders.set(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE);

                        }) // Set a default header for the requests
                        .build();

            Flux<DataBuffer> dataBufferFlux = webClient.get()
                    .uri("signature?"+queryId)
                    .retrieve().bodyToFlux(DataBuffer.class);
            DataBufferUtils.write(dataBufferFlux, path, StandardOpenOption.CREATE).block(); //Creates new file or overwrites exisiting file
            result = true;
        }catch (Exception ex){
            System.out.println("Error: "+ex.getMessage());
            
        }

        return result;
    }
    

    private void loguuid(String uuid){
        
        String xRoadInstance = AppSettings.CLIENT_HEADERS.get("xRoadInstance");
        String memberClass = AppSettings.CLIENT_HEADERS.get("memberClass");
        String memberCode = AppSettings.CLIENT_HEADERS.get("memberCode");
        String subsystemCode = AppSettings.CLIENT_HEADERS.get("subsystemCode");
    
        String queryid = "?queryId="+uuid+"&xRoadInstance="+xRoadInstance+"&memberClass="+memberClass+"&memberCode="+memberCode+"&subsystemCode="+subsystemCode;
        
        BufferedWriter writer;
        try {
            File f = new File(AppSettings.ASIC_PATH+"/asic.log");
            if(f.exists() && !f.isDirectory()) { 
                writer = new BufferedWriter(new FileWriter(AppSettings.ASIC_PATH+"/asic.log", true));
                writer.append(queryid+"\n");
                writer.close();
            }else{
                writer = new BufferedWriter(new FileWriter(AppSettings.ASIC_PATH+"/asic.log", true));
                writer.write(queryid+"\n");
                writer.close();
            }

        } catch (IOException ex) {
            System.out.println("Неможливо зробити запис в asic-файл: "+ex.getMessage());
        }
        
    }

    @Override
    public String listAsic() {
        List<String> queries = readLogQueries();
        
        for(int i=0; i<queries.size();i++){
            String parametres = queries.get(i);
            boolean isSuccess = getASIC(parametres);
            if (isSuccess){
                
            }
        }
        
        String html = render_template_files(AppSettings.ASIC_PATH,"list_files.html");
        
        return html;
    }
    
        
    private List <String> readLogQueries(){
        List<String> queries = new ArrayList<String>(); 
        
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(AppSettings.ASIC_PATH+"/asic.log"));
            String line = br.readLine();

            while (line != null) {
                queries.add(line);
                line = br.readLine();
            }
            br.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Error: "+ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Error: "+ex.getMessage());
        }
        
        return queries;
    }
}

