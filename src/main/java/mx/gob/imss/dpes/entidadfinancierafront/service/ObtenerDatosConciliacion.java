package mx.gob.imss.dpes.entidadfinancierafront.service;


import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.entidadfinancierafront.exception.ConciliacionException;
import mx.gob.imss.dpes.entidadfinancierafront.model.ConciliacionBean;
import mx.gob.imss.dpes.entidadfinancierafront.model.ConciliacionImportes;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.ConciliacionSolicitudClient;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.ConciliacionRequest;
import mx.gob.imss.dpes.interfaces.solicitud.model.Conciliacion;
import mx.gob.imss.dpes.support.util.CalculosReportesConciliacion;
import mx.gob.imss.dpes.support.util.ConvertirNumerosALetras;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
public class ObtenerDatosConciliacion {

    @Inject
    @RestClient
    private ConciliacionSolicitudClient conciliacionSolicitudClient;

    @Getter
    @Setter
    private BigDecimal[] sumas = new BigDecimal[]{
            new BigDecimal("0"),
            new BigDecimal("0"),
            new BigDecimal("0")
    };

    private Logger log = Logger.getLogger(this.getClass().getName());

    public List<ConciliacionBean> obtenerCasosRecuperados(ConciliacionRequest req) throws BusinessException {
        try {
            Response respCasosRecuperados = conciliacionSolicitudClient.buscarCasosRecuperadosPorNomina(
                    req.getCveEntidadFinanciera(),
                    req.getPeriodo()
            );
            if (respCasosRecuperados != null && respCasosRecuperados.getStatus() == 200) {
                List<Conciliacion> listCasosRecuperados = respCasosRecuperados.readEntity(new GenericType<List<Conciliacion>>(){});
                return this.adaptadorListConciliacion(listCasosRecuperados,0);
            }
        }catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConciliacionGeneraDocumentoService.obtenerCasosRecuperados()", e);
            throw new ConciliacionException(ConciliacionException.ERROR_AL_INVOCAR_SERVICIOS_REPORTE_CONCILIACION);
        }
        return new ArrayList<ConciliacionBean>();
    }

    public List<ConciliacionBean> obtenerCasosNoRecuperados(ConciliacionRequest req) throws BusinessException{
        try {
            Response respCasosNoRecuperados = conciliacionSolicitudClient.buscarCasosNoRecuperadosPorNomina(
                    req.getCveEntidadFinanciera(),
                    req.getPeriodo()
            );
            if (respCasosNoRecuperados != null && respCasosNoRecuperados.getStatus() == 200) {
                List<Conciliacion> listCasosNoRecuperados = respCasosNoRecuperados.readEntity(new GenericType<List<Conciliacion>>(){});
                return this.adaptadorListConciliacion(listCasosNoRecuperados,1);
            }
        }catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConciliacionGeneraDocumentoService.obtenerCasosNoRecuperados()", e);
            throw new ConciliacionException(ConciliacionException.ERROR_AL_INVOCAR_SERVICIOS_REPORTE_CONCILIACION);
        }
        return new ArrayList<ConciliacionBean>();
    }

    public List<ConciliacionBean> obtenerCasosDefuncion(ConciliacionRequest req) throws BusinessException{
        try {
            Response respCasosDefuncion = conciliacionSolicitudClient.buscarCasosDefuncionPorNomina(
                    req.getCveEntidadFinanciera(),
                    req.getPeriodo()
            );
            if (respCasosDefuncion != null && respCasosDefuncion.getStatus() == 200) {
                List<Conciliacion> listCasosDefuncion = respCasosDefuncion.readEntity(new GenericType<List<Conciliacion>>(){});
                return this.adaptadorListConciliacion(listCasosDefuncion,2);
            }
            return new ArrayList<ConciliacionBean>();
        }catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConciliacionGeneraDocumentoService.obtenerCasosDefuncion()", e);
            throw new ConciliacionException(ConciliacionException.ERROR_AL_INVOCAR_SERVICIOS_REPORTE_CONCILIACION);
        }
    }

    private List<ConciliacionBean> adaptadorListConciliacion(List<Conciliacion> lista, Integer posicion){
        List<ConciliacionBean> listConciliacionBean = new ArrayList<ConciliacionBean>();
        BigDecimal sumarImporte = new BigDecimal(0);
        for (Conciliacion con: lista) {
            ConciliacionBean bean = new ConciliacionBean();
            bean.setNss(con.getNSS());
            bean.setGrupoFamiliar(con.getGRUPOFAMILIAR());
            bean.setCveDelegacion(con.getCVEDELEGACION());
            bean.setNumeroFolio(con.getNUMEROFOLIO());
            bean.setTotalPagar(con.getTOTALPAGAR() == null ? 0.0 : con.getTOTALPAGAR().doubleValue());
            bean.setSaldoPendiente(con.getSALDOPENDIENTE() == null ? 0.0 : con.getSALDOPENDIENTE().doubleValue());
            bean.setDescuentoNomina(con.getDESCUENTONOMINA() == null ? 0.0 : con.getDESCUENTONOMINA().doubleValue());
            bean.setPlazo(con.getPLAZO() == null ? 0 : con.getPLAZO().intValue());
            bean.setNumeroMensualidad(con.getNUMEROMENSUALIDAD() == null ? 0: con.getNUMEROMENSUALIDAD().intValue());
            bean.setConcepto(con.getCONCEPTO() == null ? 0.0 : con.getCONCEPTO().doubleValue());
            bean.setImporteRecuperado(con.getIMPORTERECUPERADO() == null ? 0.0 : con.getIMPORTERECUPERADO().doubleValue());
            bean.setCveRechazoSpes(con.getCVERECHAZOSPES());
            listConciliacionBean.add(bean);
            sumarImporte = sumarImporte.add(new BigDecimal(bean.getImporteRecuperado()));
        }
        sumas[posicion] = sumarImporte;
        return listConciliacionBean;
    }

    public ConciliacionImportes obtenerImportes(BigDecimal iva, BigDecimal porcentajeAdministracion,
        BigDecimal porcentajePermisoItinerante) {

        BigDecimal importeCostoAdministrativo = CalculosReportesConciliacion.obtenerImporteTasa(
            porcentajeAdministracion, sumas[0]);

        BigDecimal importeIVACostoAdministrativo =
            CalculosReportesConciliacion.obtenerImporteTasa(iva, importeCostoAdministrativo);

        BigDecimal importePermisoItinerante = CalculosReportesConciliacion.obtenerImporteTasa(
                porcentajePermisoItinerante, sumas[0]);

        BigDecimal importeIVAPermisoItinerante =
                CalculosReportesConciliacion.obtenerImporteTasa(iva, importePermisoItinerante);

        BigDecimal importeNetoRecuperado = CalculosReportesConciliacion.obtenerImporteBruto(
                sumas[0], importeCostoAdministrativo, importeIVACostoAdministrativo,
                importePermisoItinerante, importeIVAPermisoItinerante
        );

        BigDecimal importeNetoDefuncion = CalculosReportesConciliacion.obtenerImporteNeto(importeNetoRecuperado, sumas[2]);

        ConciliacionImportes importes = new ConciliacionImportes();

        importes.setImporteCostoAdministrativo(importeCostoAdministrativo);
        importes.setImporteIVACostoAdministrativo(importeIVACostoAdministrativo);
        importes.setImportePermisoItinerante(importePermisoItinerante);
        importes.setImporteIVAPermisoItinerante(importeIVAPermisoItinerante);

        importes.setImporteNetoRecuperado(importeNetoRecuperado);
        importes.setImporteNetoDefuncion(importeNetoDefuncion);
        importes.setImporteLetra(ConvertirNumerosALetras.Convertir(
                importeNetoRecuperado.toString(),
                "peso",
                "pesos",
                "centavo",
                "centavos",
                " con ",
                true
        ));
        importes.setSumas(this.getSumas());

        return importes;
    }

}
