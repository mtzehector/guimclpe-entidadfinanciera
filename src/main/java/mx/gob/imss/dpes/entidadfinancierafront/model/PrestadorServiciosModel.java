package mx.gob.imss.dpes.entidadfinancierafront.model;

import lombok.Data;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.PrestadorServiciosEF;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;

@Data
public class PrestadorServiciosModel extends BaseModel {

    private String curp;
    private Long cveEntidadFinanciera;
    private PrestadorServiciosEF psCertificacionAlta;
    private PrestadorServiciosEF psCertificacionBaja;
    private byte[] archivoPSCertificacion;
    private RespuestaBoveda respuestaBovedaPSCertificacion;
    private Documento documentoPSCertificacion;
    private PrestadorServiciosEF psValidacionBiometricaAlta;
    private PrestadorServiciosEF psValidacionBiometricaBaja;
    private byte[] archivoPSValidacionBiometrica;
    private RespuestaBoveda respuestaBovedaPSValidacionBiometrica;
    private Documento documentoPSValidacionBiometrica;
    private Long sesion;
}
