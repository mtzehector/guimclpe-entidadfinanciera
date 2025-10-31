/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.config;

/**
 *
 * @author antonio
 */

import java.util.Set;
import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
       resources.add(mx.gob.imss.dpes.common.exception.AlternateFlowMapper.class);
       resources.add(mx.gob.imss.dpes.common.exception.BusinessMapper.class);
       resources.add(mx.gob.imss.dpes.common.rule.MontoTotalRule.class);
        resources.add(mx.gob.imss.dpes.common.rule.PagoMensualRule.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.assembler.BeneficiosAssembler.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.assembler.CondicionOfertaAssembler.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.assembler.CondicionesAssembler.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.assembler.EntidadCondicionAssembler.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.assembler.EnviarTokenAssembler.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.assembler.RequestEntidadFinancieraAssembler.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.assembler.PrestadorServiciosMultiPartToModelAssembler.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.endpoint.CartaReciboEndPoint.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.endpoint.CatMaximoEndpoint.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.endpoint.CondicionesEntidadFinancieraEndPoint.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.endpoint.EntidadFinancieraEndPoint.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.endpoint.ObtenerEntidadaRegPatronalEndPoint.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.endpoint.OfertaCondCapturaEndPoint.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.endpoint.OfertaEndPoint.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.endpoint.PlazosEndPoint.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.endpoint.RegistrosPatronalesEndPoint.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.endpoint.PrestadorServiciosEndPoint.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.endpoint.ConciliacionEndPoint.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.endpoint.ConciliacionReportesEndPoint.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.BeneficioService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.CatMaximoBitacoraCatEFService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.BitacoraCatImssService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.CartaReciboService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.CatMaximoService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.CondicionEntFedService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.CondicionOfertaPersistanceService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.CondicionOfertaUpdateService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.CorreoCatMaximoService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.CorreoService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.EntidadFinancieraAdminService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.EntidadFinancieraService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.ObtenerRegPatronalService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.RegistroPatronalesService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.UpdateDocumentsService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.ValidarCapacidadCreditoService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.EntidadFinancieraBitacoraCatEFService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.EntidadFinancieraOfertasService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.CondicionesEntidadFinancieraOfertasService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.CondicionesEntidadFinancieraBitacoraCatEFService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.EntidadFinancieraOTService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.PrestadorServiciosEFOTService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.DocumentoEFOTService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.PrestadorServiciosService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.BovedaPrestadorServiciosService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.CargaDocumentoPrestadorServicioService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.BitacoraPrestadorServiciosService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.EntidadFinancieraBitacoraEstatusEFService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.EstatusConciliacionService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.EstatusConciliacionPersistenciaService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.ConciliacionService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.ConciliacionBitacoraService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.GeneraReporteDetalleConciliacionService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.ConciliacionBovedaService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.ConciliacionDocumentoService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.ConciliacionDocumentoConciliacionService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.CartaReciboBitacoraService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.GeneraReporteCartaReciboInicialService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.CartaReciboFechaDescargaService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.GeneraReporteEFPorCuentaContableService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.ConciliacionMultipleDocumentoConciliacionService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.ConciliacionBitacoraEFPorCuentaContableService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.GeneraReporteRetencionPorDelegacionService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.GeneraReporteSolicitudTransferenciaService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.GeneraReporteResumenConciliacionService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.ConciliacionResumenBitacoraService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.GeneraTramiteErogacionesTransferenciaService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.GeneraTramiteErogacionesContableService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.GeneraTramiteErogacionesDelegacionService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.EstatusPeriodoConciliacionService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.TramiteErogacionService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.EstatusPeriodoTramiteErogacionService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.ActualizarTramiteErogacionesService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.ObtencionIVAService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.ObtenerAdministradorRFCService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.CartaReciboExisteEFTodasService.class);
        resources.add(mx.gob.imss.dpes.entidadfinancierafront.service.CartaReciboExisteEFService.class);

    }
    
}