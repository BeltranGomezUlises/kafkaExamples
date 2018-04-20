/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wrp.kafkaconsumer.models;

import java.util.Date;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com, at 19/04/2018
 */
public class ModelBitacora {

    private long usuarioId;
    private Date fecha;
    private String accion;
    private String ipCliente;
    private String sistemaOp;
    private Object data;

    public ModelBitacora() {
    }

    public ModelBitacora(long usuarioId, Date fecha, String accion, String ipCliente, String sistemaOp, Object data) {
        this.usuarioId = usuarioId;
        this.fecha = fecha;
        this.accion = accion;
        this.ipCliente = ipCliente;
        this.sistemaOp = sistemaOp;
        this.data = data;
    }

    public long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getIpCliente() {
        return ipCliente;
    }

    public void setIpCliente(String ipCliente) {
        this.ipCliente = ipCliente;
    }

    public String getSistemaOp() {
        return sistemaOp;
    }

    public void setSistemaOp(String sistemaOp) {
        this.sistemaOp = sistemaOp;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
