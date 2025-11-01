package mx.gob.imss.dpes.entidadfinancierafront.validation;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Provider
public class ValidaPrestadorServicios {

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

    private int getFileArrayFormDataMap(Map<String, List<InputPart>> formDataMap, String parameter) {
        if(!(parameter != null && !parameter.trim().isEmpty())) {
            return 0;
        }
        List<InputPart> inputPartList = formDataMap.get(parameter);
        if(inputPartList != null && inputPartList.size() == 1) {
            try {
                return inputPartList.get(0).getBody(byte[].class, null).length;
            } catch (IOException e) {
                return 0;
            }
        }
        return 0;
    }

    public boolean esMultipartFormDataInputValido(MultipartFormDataInput source) {
        if(source == null)
            return false;

        Map<String, List<InputPart>> formDataMap = source.getFormDataMap();

        if(formDataMap == null)
            return false;

        String curp = null;
        String psCertificacionAlta = null;
        String psValidacionBiometricaAlta = null;

        try {
            if (
                (curp = getStringFormDataMap(formDataMap, "curp")) != null
                && curp.length() == 18
                && (Long.valueOf(getStringFormDataMap(formDataMap, "cveEntidadFinanciera"))) > 0
                && (psCertificacionAlta = getStringFormDataMap(formDataMap, "psCertificacionAlta")) != null
                && !psCertificacionAlta.isEmpty()
                && (psValidacionBiometricaAlta = getStringFormDataMap(formDataMap, "psValidacionBiometricaAlta")) != null
                && !psValidacionBiometricaAlta.isEmpty()
            )
                return true;
        } catch(Exception e) {
            return false;
        }

        return false;
    }
}
