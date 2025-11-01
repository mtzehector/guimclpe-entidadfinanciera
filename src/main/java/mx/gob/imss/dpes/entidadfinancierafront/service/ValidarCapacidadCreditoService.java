/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.service;

import javax.ws.rs.ext.Provider;

import mx.gob.imss.dpes.common.enums.TipoSimulacionEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.MontoException;
import mx.gob.imss.dpes.entidadfinancierafront.exception.DescuentoException;
import mx.gob.imss.dpes.entidadfinancierafront.model.PlazosRequest;

/**
 *
 * @author Diego
 */
@Provider
public class ValidarCapacidadCreditoService extends ServiceDefinition<PlazosRequest, PlazosRequest> {

	@Override
	public Message<PlazosRequest> execute(Message<PlazosRequest> request) throws BusinessException {

		if (request.getPayload().getTipoSimulacion().getId().equals(TipoSimulacionEnum.DESCUENTO_MENSUAL.getId())) {

			if (request.getPayload().getCapacidadCredito() <= request.getPayload().getMonto()) {

				throw new DescuentoException();

			}

		}
		return request;
	}
}
