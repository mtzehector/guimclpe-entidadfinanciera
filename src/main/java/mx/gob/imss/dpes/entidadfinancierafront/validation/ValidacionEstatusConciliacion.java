package mx.gob.imss.dpes.entidadfinancierafront.validation;

import mx.gob.imss.dpes.interfaces.estatusConciliacion.model.EstatusConciliacionRequest;

import javax.ws.rs.ext.Provider;

@Provider
public class ValidacionEstatusConciliacion {

    public boolean isEstatusValido(EstatusConciliacionRequest req){
        if (req == null)
            return false;
        if (req.getPeriodo() == null || req.getPeriodo().isEmpty())
            return false;
        if (req.getActivo() == null)
            return false;

        return true;
    }

}
