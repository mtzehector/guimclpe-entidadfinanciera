package mx.gob.imss.dpes.entidadfinancierafront.assembler;

import com.fasterxml.jackson.databind.ObjectMapper;
import mx.gob.imss.dpes.entidadfinancierafront.model.PrestadorServiciosModel;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.PrestadorServiciosEF;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Provider
public class PrestadorServiciosMultiPartToModelAssembler {

    private String getStringFormDataMap(Map<String, List<InputPart>> formDataMap, String parameter) {
        if(!(parameter != null && !parameter.trim().isEmpty())) {
            return null;
        }
        List<InputPart> inputPartList = formDataMap.get(parameter);
        if(inputPartList != null && inputPartList.size() == 1) {
            try {
                return inputPartList.get(0).getBodyAsString().trim();
            } catch (IOException e) {
                return null;
            }
        }
        return null;
    }

    private byte[]  getFileArrayFormDataMap(Map<String, List<InputPart>> formDataMap, String parameter) {
        if(!(parameter != null && !parameter.trim().isEmpty())) {
            return null;
        }
        List<InputPart> inputPartList = formDataMap.get(parameter);
        if(inputPartList != null && inputPartList.size() == 1) {
            try {
                return inputPartList.get(0).getBody(byte[].class, null);
            } catch (IOException e) {
                return null;
            }
        }
        return null;
    }

    private PrestadorServiciosEF parseStringJsonToObject(String json){
        try{
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, PrestadorServiciosEF.class);
        }catch (IOException e){
            return null;
        }
    }

    public PrestadorServiciosModel assemble(MultipartFormDataInput source) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        try{
            Map<String, List<InputPart>> formDataMap = source.getFormDataMap();
            PrestadorServiciosModel model = new PrestadorServiciosModel();

            model.setCveEntidadFinanciera(new Long(getStringFormDataMap(formDataMap, "cveEntidadFinanciera")));
            model.setCurp(getStringFormDataMap(formDataMap, "curp"));

            model.setPsCertificacionAlta(this.parseStringJsonToObject(
                    getStringFormDataMap(formDataMap, "psCertificacionAlta")));
            if(model.getPsCertificacionAlta() != null) {
                if(model.getPsCertificacionAlta().getCveEntidadFinanciera() == null)
                    model.getPsCertificacionAlta().setCveEntidadFinanciera(model.getCveEntidadFinanciera());
                model.setPsCertificacionBaja(
                    getStringFormDataMap(formDataMap, "psCertificacionBaja") != null ?
                        this.parseStringJsonToObject(getStringFormDataMap(formDataMap, "psCertificacionBaja")):
                    null
                );
                if(model.getPsCertificacionBaja() != null)
                    model.getPsCertificacionBaja().setBajaRegistro(new Date());

                model.setArchivoPSCertificacion(getFileArrayFormDataMap(formDataMap, "archivoPSCertificacion"));
            }

            model.setPsValidacionBiometricaAlta(this.parseStringJsonToObject(
                    getStringFormDataMap(formDataMap, "psValidacionBiometricaAlta")));
            //if(model.getPsCertificacionAlta() != null) {
            //    model.setPsCertificacionAlta(this.parseStringJsonToObject(
            //            getStringFormDataMap(formDataMap, "psValidacionBiometricaBaja")));
            //    model.setArchivoPSCertificacion(getFileArrayFormDataMap(formDataMap, "archivoPSValidacionBiometrica"));
            //}
            if(model.getPsValidacionBiometricaAlta() != null) {
                if(model.getPsValidacionBiometricaAlta().getCveEntidadFinanciera() == null)
                    model.getPsValidacionBiometricaAlta().setCveEntidadFinanciera(model.getCveEntidadFinanciera());
                model.setPsValidacionBiometricaBaja(
                    getStringFormDataMap(formDataMap, "psValidacionBiometricaBaja") != null ?
                        this.parseStringJsonToObject(getStringFormDataMap(formDataMap, "psValidacionBiometricaBaja")):
                            null
                );
                if(model.getPsValidacionBiometricaBaja() != null)
                    model.getPsValidacionBiometricaBaja().setBajaRegistro(new Date());

                model.setArchivoPSValidacionBiometrica(getFileArrayFormDataMap(formDataMap, "archivoPSValidacionBiometrica"));
            }
            model.setSesion(new Long(getStringFormDataMap(formDataMap, "sesion")));
            return model;
        }catch(Exception e){
            return null;
        }
    }
}
