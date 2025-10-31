package mx.gob.imss.dpes.entidadfinancierafront.assembler;

import mx.gob.imss.dpes.common.enums.TipoDocumentoEnum;
import mx.gob.imss.dpes.entidadfinancierafront.model.BeneficioRequest;
import mx.gob.imss.dpes.entidadfinancierafront.model.CatMaximoModel;
import mx.gob.imss.dpes.entidadfinancierafront.model.CondicionPersistenciaModel;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Provider
public class AssamblerCatMaximoModelFromMultipartFormDataInput {

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

    private byte[]  getFileArrayFromFormDataMap(Map<String, List<InputPart>> formDataMap, String parameter) {
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

    public CatMaximoModel assemble(MultipartFormDataInput source) {
        Map<String, List<InputPart>> formDataMap = source.getFormDataMap();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        try {
            CatMaximoModel catMaximoModel = new CatMaximoModel();
            catMaximoModel.setCatText(getStringFromFormDataMap(formDataMap, "catNuevo"));
            catMaximoModel.setCatIMSS(new BigDecimal(catMaximoModel.getCatText()));
            catMaximoModel.setCurp(getStringFromFormDataMap(formDataMap, "curp"));
            catMaximoModel.setCatAnterior(getStringFromFormDataMap(formDataMap, "catAnterior"));
            catMaximoModel.setNombreArchivo("CATMaximo" + dateFormat.format(new Date()));
            catMaximoModel.setSolicitud("0");
            catMaximoModel.setTipoDocumento(TipoDocumentoEnum.CAT_MAXIMO);
            catMaximoModel.setArchivo(getFileArrayFromFormDataMap(formDataMap, "archivo"));
            String sesion = this.getStringFromFormDataMap(formDataMap, "sesion");
            catMaximoModel.setSesion(sesion == null? 0: Long.parseLong(sesion));
            return catMaximoModel;
        } catch (Exception e) {
            return null;
        }
    }
}
