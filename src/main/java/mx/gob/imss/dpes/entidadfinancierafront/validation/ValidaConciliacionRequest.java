package mx.gob.imss.dpes.entidadfinancierafront.validation;

import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.ConciliacionRequest;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class ValidaConciliacionRequest {

    public boolean validaCurpEntidadesPeriodo(ConciliacionRequest request){
        if (request == null)
            return false;

        if (request.getCurp() == null || request.getCurp().isEmpty())
            return false;

        if (request.getArregloIdEntidadFinanciera() == null || request.getArregloIdEntidadFinanciera().length == 0)
            return false;

        if (request.getPeriodo() == null || request.getPeriodo().isEmpty())
            return false;

        return true;
    }
    public boolean isRequestValido(ConciliacionRequest request){

        if (request == null)
            return false;

        if (request.getCurp() == null || request.getCurp().isEmpty())
            return false;

        if (request.getCveTipoDocumento() == null)
            return false;

        if (request.getArregloIdEntidadFinanciera() == null || request.getArregloIdEntidadFinanciera().length == 0)
            return false;

        if (request.getPeriodo() == null || request.getPeriodo().isEmpty())
            return false;

        return true;
    }

    public boolean validaCurpTipoDocumentoPeriodo(ConciliacionRequest request){

        if (request == null)
            return false;

        if (request.getCurp() == null || request.getCurp().isEmpty())
            return false;

        if (request.getCveTipoDocumento() == null)
            return false;

        if (request.getPeriodo() == null || request.getPeriodo().isEmpty())
            return false;

        return true;
    }


}
