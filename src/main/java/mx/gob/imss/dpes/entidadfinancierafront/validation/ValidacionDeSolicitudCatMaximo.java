package mx.gob.imss.dpes.entidadfinancierafront.validation;

import mx.gob.imss.dpes.common.enums.TipoDocumentoEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Provider
public class ValidacionDeSolicitudCatMaximo {

    private String getStringFromFormDataMap(Map<String, List<InputPart>> formDataMap, String parameter) {
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

    private int getFileArrayFromFormDataMap(Map<String, List<InputPart>> formDataMap, String parameter) {
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

    public boolean esMultipartFormDataInputValidoParaActualizacionCATMaximo(MultipartFormDataInput source) {
        if(source == null)
            return false;

        Map<String, List<InputPart>> formDataMap = source.getFormDataMap();

        if(formDataMap == null)
            return false;

        String catText = null;
        String curp = null;
        String catAnterior = null;

        try {
            if (
                    (catText = getStringFromFormDataMap(formDataMap, "catNuevo")) != null
                            && !catText.isEmpty()
                            && new BigDecimal(catText).compareTo(BigDecimal.ZERO) >= 0
                            && (curp = getStringFromFormDataMap(formDataMap, "curp")) != null
                            && curp.length() == 18
                            && (catAnterior = getStringFromFormDataMap(formDataMap, "catAnterior")) != null
                            && !catAnterior.isEmpty()
                            && new BigDecimal(catAnterior).compareTo(BigDecimal.ZERO) >= 0
                            && getFileArrayFromFormDataMap(formDataMap, "archivo") > 0
            )
                return true;
        } catch (Exception e) {
            return false;
        }

        return false;
    }
}
